package mobile.che.com.oddymobstar.chemobile.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;

/**
 * Created by timmytime on 29/01/16.
 */
public class CheServiceSocket {

    private static final int BUFFER_SIZE = 2048;

    public final List<CheMessage> messageBuffer = new ArrayList<>();

    //socket fuckery,  should look at bootstrap but it works.
    public Thread write;
    public Thread connect;
    public Thread read;
    public Socket socket;
    public DataOutputStream dOut = null;
    public DataInputStream dIn = null;

    private final CheService cheService;
    private final Configuration configuration;

    public CheServiceSocket(CheService cheService, Configuration configuration){
        this.cheService = cheService;
        this.configuration = configuration;
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
                try {
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

                        Log.d("the buffer is ", "buffer " + new String(buffer).substring(0, charsRead));
                        //each message we receive should be a JSON.  We need to work out the type.
                        InCoreMessage coreMessage = new InCoreMessage(object);


                        Intent messageIntent = new Intent(DemoActivityController.MESSAGE_INTENT);
                        messageIntent.putExtra("message", coreMessage.getJsonObject().toString());
                        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
                        Log.d("message received", "message received " + coreMessage.getJsonObject().toString());
                        //at this point we could have more than 1 core message here.


                        Acknowledge ack = null;

                        //what are we?
                        if (!coreMessage.getJsonObject().isNull(InCoreMessage.ACKNOWLEDGE)) {

                            ack = new Acknowledge(coreMessage.getJsonObject().getJSONObject(InCoreMessage.ACKNOWLEDGE));

                            /*
                            acknowledges either tell us of a fail (we can log it etc) or tell us of a success and generally a UUID.
                           */

                            if (ack.getState().equals(InCoreMessage.ERROR)) {
                                Log.d("ack error", "error information is " + ack.getInfo());
                            } else {
                                if (ack.getInfo().equals(InCoreMessage.ACTIVE)) {
                                    //we need to wake up our write thread.
                                    if (write != null) {

                                        Log.d("ack error", "trying to wake up thread");

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
                                }

                            }

                        }

                        messageHandler.handleMessage(coreMessage, ack);

                    }


                } catch (JSONException jse) {
                    Log.d("json exception", "json exception " + jse.toString());
                }

            }


        } catch (IOException e) {

            Log.d("socket listen", "socket listen error " + e.toString());

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

            for (OutCoreMessage coreMessage : messageBuffer) {
                writeToSocket(coreMessage);
            }

            messageBuffer.clear();
            write.interrupt();

        }

        write.start();


    }

    public void writeToSocket(final CheMessage cheMessage) {


        try {

            messageHandler.getSentAcks().put(coreMessage.getMessage().getJSONObject(OutCoreMessage.CORE_OBJECT).getString(OutCoreMessage.ACK_ID), coreMessage);
            if (coreMessage.isPost()) {
                messageHandler.getSentPosts().put(coreMessage.getMessage().getJSONObject(OutCoreMessage.CORE_OBJECT).getString(OutCoreMessage.ACK_ID), coreMessage);
            }

            dOut.write(coreMessage.getMessage().toString().getBytes("UTF-8"));


        } catch (Exception e) {
            Log.d("socket exception", "socket " + e.toString() + coreMessage.getMessage());
            messageBuffer.add(coreMessage);

            connect = new Thread(new Runnable() {
                @Override
                public void run() {
                    reConnect();
                }
            });

            connect.start();

        }

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
