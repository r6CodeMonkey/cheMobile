package mobile.che.com.oddymobstar.chemobile.service.util;

import android.os.Handler;
import android.util.Log;

/**
 * Created by timmytime on 24/02/16.
 */
public class CheReconnectListener implements Runnable {

    //allow a maximum 10 seconds to receive ack....
    public static final long TIMEOUT = 10000;
    private final Handler handler = new Handler();
    private final Runnable callback;


    public CheReconnectListener(Runnable callback) {
        this.callback = callback;
    }


    @Override
    public void run() {
        handler.postDelayed(callback, TIMEOUT);
    }

}
