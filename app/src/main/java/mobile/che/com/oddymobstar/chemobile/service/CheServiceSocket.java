package mobile.che.com.oddymobstar.chemobile.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import message.Acknowledge;
import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.service.handler.CheCallbackInterface;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import util.Tags;

/**
 * Created by timmytime on 29/01/16.
 */
public class CheServiceSocket {

    private static final int BUFFER_SIZE = 4096;

    //we need our own player
    private final CheService cheService;
    private final Configuration configuration;
    private final CheMessageHandler cheMessageHandler;
    private final CheMessageQueue cheMessageQueue;
    //socket fuckery,  should look at bootstrap but it works.
    public Thread write;
    public Thread connect;
    public Thread read;
    public Socket socket;
    public DataOutputStream dOut = null;
    public DataInputStream dIn = null;

    private Thread cheReconnectThread;
    private final CheReconnectListener cheReconnectListener = new CheReconnectListener(new Runnable() {
        @Override
        public void run() {
            reConnect();
        }
    });

    public CheServiceSocket(CheService cheService, CheMessageHandler cheMessageHandler, Configuration configuration) {
        this.cheService = cheService;
        this.cheMessageHandler = cheMessageHandler;
        this.configuration = configuration;
        this.cheMessageQueue = new CheMessageQueue(this);

        this.cheMessageHandler.addCallback(new CheCallbackInterface() {
            @Override
            public void send(CheMessage cheMessage) {
                Log.d("callback", "sending ack callback");
                cheMessageQueue.addMessage(cheMessage);
            }
        });


    }

    public void clearQueue() {
        cheMessageQueue.clear();
    }


    private void socketListen() {

        /*
          this needs to be broken up and call a secondary class to handle the various input
          bar acknowledge.  that way the actions can be overriden.  part of refactor work.
         */

        try {


            byte[] buffer = new byte[BUFFER_SIZE];
            String partialObject = "";
            int partialOpenBracket = 0;
            int partialCloseBracket = 0;


            int charsRead = 0;

            while ((charsRead = dIn.read(buffer)) != -1) {

                //we need to grab each core message out.
                int openBracket = partialOpenBracket > 0 ? partialOpenBracket : 0;
                int closeBracket = partialCloseBracket > 0 ? partialCloseBracket : 0;
                partialOpenBracket = 0;
                partialCloseBracket = 0;

                //for the given line we need to read all of it
                char[] lineRead = new String(buffer).substring(0, charsRead).toCharArray();
                boolean objectsToRead = true;
                int charPos = 0;

                while (objectsToRead) {

                    boolean objectFound = false;
                    String object = partialObject.trim().isEmpty() ? "" : partialObject;
                    partialObject = "";


                    for (int i = charPos; i < lineRead.length && !objectFound; i++) {
                        if (lineRead[i] == '{') {
                            openBracket++;
                        }
                        if (lineRead[i] == '}') {
                            closeBracket++;
                        }

                        object = object + lineRead[i];

                        if (openBracket == closeBracket) {
                            objectFound = true;
                            charPos = i + 1;
                        }

                        if (i == lineRead.length - 1) {
                            objectsToRead = false;
                        }
                        //if we are partial we need to carry on.
                        if (i == lineRead.length - 1 && !objectFound) {
                            partialObject = object;
                            partialOpenBracket = openBracket;
                            partialCloseBracket = closeBracket;
                        }

                    }

                    if (objectFound) {
                        processJSON(object);
                    }

                }
            }

        } catch (IOException e) {
            Log.d("socket listen", "socket listen error " + e.toString());
        }

    }

    private void processJSON(final String object) {
        try {
            //each message we receive should be a JSON.  We need to work out the type.
            JSONObject jsonObject = new JSONObject(object);

            Log.d("socket listen", "we seem to have an object " + jsonObject.toString());
            //test for ack..
            if (!jsonObject.isNull(Tags.ACKNOWLEDGE)) {

                //   Log.d("socket listen", "we have an ack"); // also need to remove it from buffer at some point.

                Acknowledge acknowledge = new Acknowledge(object);

                switch (acknowledge.getState()) {

                    case Tags.ACCEPT:
                        //      Log.d("accept", "we are accept");
                        switch (acknowledge.getValue()) {
                            case Tags.ERROR:
                                //         Log.d("ack error", "ack error " + acknowledge.toString());
                                break;
                            case Tags.ACTIVE:
                                Log.d("active", "we are active");
                                if (cheMessageHandler.isNewPlayer()) {
                                    Log.d("socket listen", "create message to get che id");
                                    cheMessageQueue.addMessage(cheMessageHandler.createNewPlayer());
                                }

                                if (write != null) {


                                    if (connect != null) {
                                        connect.interrupt();
                                    }

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            synchronized (write) {
                                                write.notify();
                                            }
                                        }
                                    }).start();


                                }
                                break;
                            default:
                                //its nothing currently.
                                break;
                        }
                        break;
                    case Tags.UUID:
                        Log.d("uuid", "we are uuid");
                        stopCheSocketListener(acknowledge.getKey());
                        cheMessageQueue.receiveAck(acknowledge.getKey());
                        cheMessageHandler.handleNewPlayer(acknowledge);
                        break;
                    case Tags.SUCCESS:
                        switch (acknowledge.getValue()) {
                            case Tags.RECEIVED:
                                //         Log.d("rec", "standard rec");
                                //need to remove but its not che...
                                stopCheSocketListener(acknowledge.getKey());
                                cheMessageQueue.receiveAck(acknowledge.getKey());
                                break;
                            case Tags.CHE_RECEIVED:
                                //        Log.d("che rec", "che rec");
                                stopCheSocketListener(acknowledge.getKey());
                                cheMessageQueue.receiveAck(acknowledge.getKey());
                                //need to remove tag...
                                break;
                            default:
                                stopCheSocketListener(acknowledge.getKey());
                                cheMessageQueue.receiveAck(acknowledge.getKey());
                                break;
                        }
                    default:
                        break;

                }

            } else {
                //         Log.d("che message", "we have a che message");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cheMessageHandler.handle(new CheMessage(object));
                        } catch (JSONException e) {
                            // e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            //  e.printStackTrace();
                        }
                    }
                }).start();

            }

        } catch (NoSuchAlgorithmException e) {
            Log.d("security exception", "security exception " + e.toString());
        } catch (JSONException e) {
            Log.d("json exception", "json exception at a bad point!!! " + e.toString());
        }
    }


    public void reConnect() {

        Log.d("reconnect", "reconnect called");
        Intent messageIntent = new Intent(ProjectCheController.MESSAGE_INTENT);
        messageIntent.putExtra("message", "Reconnect called");
        LocalBroadcastManager.getInstance(cheService).sendBroadcast(messageIntent);


        try {
            socket.close();
        } catch (Exception e) {

        }
        if (write != null) {
            write.interrupt();
        }

        if (read != null) {
            read.interrupt();
        }

        socket = null;
        write = null;
        read = null;


        try {
            dIn.close();
        } catch (Exception e) {
        }
        try {
            dOut.close();
        } catch (Exception e) {
        }

        //try again
        connectSocket();


        //we now need to wait for the active connection message again.
        write = new Thread();


        synchronized (write) {
            try {
                write.wait();
            } catch (InterruptedException ie) {
                Log.d("wait", "wait " + ie.toString());
            } catch (Exception e) {
                Log.d("wait", "wait " + e.toString());
            }

            cheMessageQueue.reConnect();
            //write.interrupt();  //this may not be required anymore...

        }

        write.start();


    }

    private void startCheSocketListener(String key){
        cheReconnectListener.setKey(key);
        cheReconnectThread = new Thread(cheReconnectListener);
        cheReconnectThread.start();

    }

    private void stopCheSocketListener(String key){
        if(cheReconnectListener.getKey().equals(key)){
            cheReconnectThread.interrupt();
        }

    }

    public void write(CheMessage cheMessage) {
        try {
            if(!cheReconnectListener.isSocketActive()){
                startCheSocketListener(cheMessage.getMessage(Tags.ACKNOWLEDGE).getKey());
            }
            dOut.write(cheMessage.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            connect = new Thread(new Runnable() {
                @Override
                public void run() {
                    reConnect();
                }
            });

            connect.start();
        }
    }

    public void addToQueue(CheMessage cheMessage) {
        cheMessageQueue.addMessage(cheMessage);
    }

    /*
      this will also need to be used to reconnect the socket if it goes down or is shut
      (due to network signal etc).
     */
    public void connectSocket() {
        try {
            socket = new Socket(configuration.getConfig(Configuration.URL).getValue(), Integer.parseInt(configuration.getConfig(Configuration.PORT).getValue()));
            socket.setKeepAlive(true);

            //probably not required due to message issue. todo (TEST)
            connect.interrupt();
            connect = null;


            //set up the two stream readers
            dOut = new DataOutputStream(socket.getOutputStream());
            dIn = new DataInputStream(socket.getInputStream());


            //now start listening to the socket this is ongoing.
            read = new Thread((new Runnable() {
                @Override
                public void run() {
                    socketListen();
                }
            }));

            read.start();


        } catch (IOException e) {
            //crashed.
            Log.d("socket error", "socket " + e.toString());


        }

    }

}
