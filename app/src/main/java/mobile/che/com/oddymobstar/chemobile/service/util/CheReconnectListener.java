package mobile.che.com.oddymobstar.chemobile.service.util;

import android.os.Handler;

/**
 * Created by timmytime on 24/02/16.
 */
public class CheReconnectListener implements Runnable {

    //allow a maximum 10 seconds to receive ack....
    public static final long TIMEOUT = 1000 * 60 * 20;
    private final Handler handler = new Handler();
    private final Runnable callback;


    public CheReconnectListener(Runnable callback) {
        this.callback = callback;
    }


    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        handler.postDelayed(callback, TIMEOUT + startTime);

    }

}
