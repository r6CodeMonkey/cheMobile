package mobile.che.com.oddymobstar.chemobile.service.handler;

import android.location.Location;
import android.util.Log;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

import message.Acknowledge;
import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.MessageFactory;
import util.Tags;

/**
 * Created by timmytime on 11/02/16.
 */
public class PlayerHandler extends MessageHandler {

    private final MessageFactory messageFactory;
    private CheCallbackInterface cheCallback;

    public PlayerHandler(DBHelper dbHelper, MessageFactory messageFactory) {
        super(dbHelper);
        this.messageFactory = messageFactory;
    }


    public void addCheCallback(CheCallbackInterface cheCallback) {
        this.cheCallback = cheCallback;
    }



    @Override
    public void handle(CheMessage cheMessage) throws JSONException, NoSuchAlgorithmException {
        //null
    }



    public void handleNewPlayer(Acknowledge acknowledge) throws JSONException, NoSuchAlgorithmException {

        //
        //its new only...
        Config config = dbHelper.getConfig(Configuration.PLAYER_KEY);
        config.setValue(acknowledge.getValue());
        dbHelper.updateConfig(config);
        dbHelper.handleNewPlayer(acknowledge.getValue());

    }
}
