package mobile.che.com.oddymobstar.chemobile.connect;

import android.content.Intent;

/**
 * Created by root on 25/04/15.
 */
public interface ConnectivityInterface {

    public void enable();

    public void disable();

    public void handle(int requestCode, int resultCode, Intent data);

    public void setMessage(byte[] message);
}
