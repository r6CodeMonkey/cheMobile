package mobile.che.com.oddymobstar.chemobile.service;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

import message.Acknowledge;
import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.service.handler.AcknowledgeHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.AllianceHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.CheCallbackInterface;
import mobile.che.com.oddymobstar.chemobile.service.handler.GameObjectHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.GridHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.MessageHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.MissileHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.PlayerHandler;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.MessageFactory;
import util.Tags;

/**
 * Created by timmytime on 29/01/16.
 */
public class CheMessageHandler extends MessageHandler {

    private final AcknowledgeHandler acknowledgeHandler;
    private final PlayerHandler playerHandler;
    private final AllianceHandler allianceHandler;
    private final GameObjectHandler gameObjectHandler;
    private final GridHandler gridHandler;
    private final MissileHandler missileHandler;
    private MessageFactory messageFactory;

    // private List<CheMessage> buffer = new ArrayList<>();

    public CheMessageHandler(DBHelper dbHelper) {
        super(dbHelper);

        messageFactory = new MessageFactory(dbHelper);

        acknowledgeHandler = new AcknowledgeHandler(dbHelper, messageFactory);
        playerHandler = new PlayerHandler(dbHelper, messageFactory);
        allianceHandler = new AllianceHandler(dbHelper);
        gameObjectHandler = new GameObjectHandler(dbHelper, messageFactory);
        gridHandler = new GridHandler(dbHelper);
        missileHandler = new MissileHandler(dbHelper);

    }

    public void addCallback(CheCallbackInterface callback) {
        acknowledgeHandler.addCheCallback(callback);
        playerHandler.addCheCallback(callback);
        gameObjectHandler.addCheCallback(callback);
    }


    public void handleNewPlayer(Acknowledge acknowledge) throws JSONException, NoSuchAlgorithmException {
        playerHandler.handleNewPlayer(acknowledge);
    }

    public synchronized void handle(CheMessage cheMessage) throws JSONException, NoSuchAlgorithmException {

        //we always handle this.  we need to send the response back as well.
        acknowledgeHandler.handle(cheMessage);

        if (cheMessage.containsMessage(Tags.GAME_OBJECT)) {
            //         Log.d("handle", "handle game object");
            gameObjectHandler.handle(cheMessage);
        }

        if (cheMessage.containsMessage(Tags.MISSILE)) {
            //        Log.d("handle", "handle missile");
            missileHandler.handle(cheMessage);
        }

        if (cheMessage.containsMessage(Tags.ALLIANCE)) {
            allianceHandler.handle(cheMessage);
        }

        if (cheMessage.containsMessage(Tags.UTM_LOCATION)) {
            //       Log.d("handle", "handle utm location " + cheMessage.toString());
            gridHandler.handle(cheMessage);
        }

    }


    public boolean isNewPlayer() {
        return dbHelper.getConfig(Configuration.PLAYER_KEY).getValue().isEmpty();
    }

    public CheMessage createNewPlayer() throws NoSuchAlgorithmException {
        return playerHandler.createNewPlayer();
    }


}
