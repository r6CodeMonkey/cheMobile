package mobile.che.com.oddymobstar.chemobile.service;

import android.util.Log;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import message.Acknowledge;
import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.service.handler.AcknowledgeHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.AllianceHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.GameObjectHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.GridHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.MessageHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.MissileHandler;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.MessageFactory;
import util.Tags;

/**
 * Created by timmytime on 29/01/16.
 */
public class CheMessageHandler extends MessageHandler {

    //track everything.
    private final Map<String, CheMessage> sentAcks = new HashMap<>();
    private final AcknowledgeHandler acknowledgeHandler;
    private final AllianceHandler allianceHandler;
    private final GameObjectHandler gameObjectHandler;
    private final GridHandler gridHandler;
    private final MissileHandler missileHandler;
    private MessageFactory messageFactory;

    public CheMessageHandler(DBHelper dbHelper) {
        super(dbHelper);

        messageFactory = new MessageFactory(dbHelper);

        acknowledgeHandler = new AcknowledgeHandler(dbHelper, messageFactory);
        allianceHandler = new AllianceHandler(dbHelper);
        gameObjectHandler = new GameObjectHandler(dbHelper);
        gridHandler = new GridHandler(dbHelper);
        missileHandler = new MissileHandler(dbHelper);

    }

    public void addCallback(AcknowledgeHandler.CheCallbackInterface callback) {
        acknowledgeHandler.addCheCallback(callback);
    }

    public Map<String, CheMessage> getSentAcks() {
        return sentAcks;
    }


    public void handleNewPlayer(Acknowledge acknowledge) throws JSONException {
        Config config = dbHelper.getConfig(Configuration.PLAYER_KEY);
        config.setValue(acknowledge.getValue());
        dbHelper.updateConfig(config);
        dbHelper.handleNewPlayer(acknowledge.getValue());
    }

    public void handle(CheMessage cheMessage) throws JSONException, NoSuchAlgorithmException {

        Log.d("handle", "handle che");
        //we always handle this.  we need to send the response back as well.
        acknowledgeHandler.handle(cheMessage);

        /*
        simply a case of testing if we have the object
         */
        if (cheMessage.containsMessage(Tags.GAME_OBJECT)) {
            Log.d("handle", "handle game object");
            gameObjectHandler.handle(cheMessage);
        }

        if (cheMessage.containsMessage(Tags.MISSILE)) {
            Log.d("handle", "handle missile");
            missileHandler.handle(cheMessage);
        }

        if (cheMessage.containsMessage(Tags.ALLIANCE)) {
            Log.d("handle", "handle alliance " + cheMessage.toString());
            allianceHandler.handle(cheMessage);
        }

        if (cheMessage.containsMessage(Tags.UTM_LOCATION)) {
            Log.d("handle", "handle utm location " + cheMessage.toString());
            gridHandler.handle(cheMessage);
        }

    }

    public boolean isNewPlayer() {
        return dbHelper.getConfig(Configuration.PLAYER_KEY).getValue().isEmpty();
    }

    public CheMessage createNewPlayer() throws NoSuchAlgorithmException {
        CheMessage cheMessage = messageFactory.createCheMessage();

        cheMessage.setMessage(Tags.PLAYER, messageFactory.createPlayer());
        cheMessage.setMessage(Tags.ACKNOWLEDGE, messageFactory.createAcknowledge());

        Log.d("new player message ", "is " + cheMessage.toString());

        return cheMessage;
    }


}
