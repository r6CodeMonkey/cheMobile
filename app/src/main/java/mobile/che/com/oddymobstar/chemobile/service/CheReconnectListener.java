package mobile.che.com.oddymobstar.chemobile.service;

import android.os.Handler;

/**
 * Created by timmytime on 24/02/16.
 */
public class CheReconnectListener implements Runnable {

    //allow a maximum 10 seconds to receive ack....
    public static final long TIMEOUT = 1000*60*20;
    private final Handler handler = new Handler();
    private final Runnable callback;
    private String key;
    private volatile boolean socketActive = false;

    private long startTime;

    public CheReconnectListener(Runnable callback){
        this.callback = callback;
    }

    public void setKey(String key){
        this.key = key;
        startTime = System.currentTimeMillis();
    }

    public String getKey(){
        return key;
    }

    public boolean isSocketActive(){
        return socketActive;
    }

    @Override
    public void run() {

        socketActive = true;

          if (System.currentTimeMillis() > TIMEOUT + startTime) {
              //force a reconnect...
              socketActive = false;
              handler.post(callback);
              return;
          }

    }

}
