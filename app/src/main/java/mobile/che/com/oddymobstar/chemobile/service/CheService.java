package mobile.che.com.oddymobstar.chemobile.service;

import android.app.IntentService;
import android.content.Intent;

import mobile.che.com.oddymobstar.chemobile.activity.handler.MessageHandler;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;

/**
 * Created by timmytime on 29/01/16.
 */
public class CheService extends IntentService {

    private final DBHelper dbHelper = new DBHelper(this);


    public CheService() {
        super("CheService");
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        dbHelper.setMessageHandler(messageHandler);
    }

    /*
     this is key for testing....need to wire in all the old shit without the old shit.

     so test 1: can we run a netty bootstrap?  if we could that rocks...so thats what i need to test.

     therfore i do need to break the other code? or add a controller.

     and the controller dipshit.  its a long night of cut and paste coding with careful eye on not actually keeping any of it.

     (but we need the connect shit to work and it needs methods installed).

     */


    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
