package mobile.che.com.oddymobstar.chemobile.service;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import message.Acknowledge;
import message.CheMessage;
import message.Player;
import message.UTM;
import message.UTMLocation;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.service.handler.AcknowledgeHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.AllianceHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.GameObjectHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.GridHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.MessageHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.MissileHandler;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.UUIDGenerator;
import util.Tags;

/**
 * Created by timmytime on 29/01/16.
 */
public class CheMessageHandler extends MessageHandler{

    //track everything.
    private final Map<String, CheMessage> sentAcks = new HashMap<>();


    private final AcknowledgeHandler acknowledgeHandler;
    private final AllianceHandler allianceHandler;
    private final GameObjectHandler gameObjectHandler;
    private final GridHandler gridHandler;
    private final MissileHandler missileHandler;

    public CheMessageHandler(DBHelper dbHelper) {
        super(dbHelper);

        acknowledgeHandler = new AcknowledgeHandler(dbHelper);
        allianceHandler = new AllianceHandler(dbHelper);
        gameObjectHandler = new GameObjectHandler(dbHelper);
        gridHandler = new GridHandler(dbHelper);
        missileHandler = new MissileHandler(dbHelper);
    }

    public void addCallback(AcknowledgeHandler.CheCallbackInterface callback){
        acknowledgeHandler.addCheCallback(callback);
    }

    public Map<String, CheMessage> getSentAcks(){ return sentAcks;}


    public void handleNewPlayer(Acknowledge acknowledge) throws JSONException{
        Config config = dbHelper.getConfig(Configuration.PLAYER_KEY);
        config.setValue(acknowledge.getValue());
        dbHelper.updateConfig(config);
    }

    public void handle(CheMessage cheMessage) throws JSONException{

        //we always handle this.  we need to send the response back as well.
        acknowledgeHandler.handle(cheMessage);

        /*
        simply a case of testing if we have the object
         */
        if(cheMessage.containsMessage(Tags.GAME_OBJECT)){
            gameObjectHandler.handle(cheMessage);
        }

        if(cheMessage.containsMessage(Tags.MISSILE)){
            missileHandler.handle(cheMessage);
        }

        if(cheMessage.containsMessage(Tags.ALLIANCE)){
            allianceHandler.handle(cheMessage);
        }

        if(cheMessage.containsMessage(Tags.UTM_LOCATION)){
            gridHandler.handle(cheMessage);
        }

    }

    public boolean isNewPlayer(){
        return dbHelper.getConfig(Configuration.PLAYER_KEY).getValue().isEmpty();
    }

    public CheMessage createNewPlayer() throws NoSuchAlgorithmException {
        CheMessage cheMessage = new CheMessage();
        cheMessage.create();

        Player player = new Player();
        player.create();
        player.setName(dbHelper.getConfig(Configuration.PLAYER_NAME).getValue());

        UTMLocation utmLocation = new UTMLocation();
        utmLocation.create();
        utmLocation.setLatitude(0.0);
        utmLocation.setLongitude(0.0);
        utmLocation.setAltitude(0.0);
        utmLocation.setSpeed(0.0);
        utmLocation.setState("");
        utmLocation.setValue("");

        UTM utm = new UTM();
        utm.create();
        utm.setUTMLatGrid("");
        utm.setUTMLongGrid("");

        utmLocation.setUTM(utm);
        utmLocation.setSubUTM(utm);

        player.setUTMLocation(utmLocation);

        Acknowledge acknowledge = new Acknowledge();
        acknowledge.create();
        acknowledge.setKey(UUIDGenerator.generateKey());

        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);

        return cheMessage;
    }



}
