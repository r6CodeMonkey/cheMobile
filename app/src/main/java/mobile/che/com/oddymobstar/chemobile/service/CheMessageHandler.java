package mobile.che.com.oddymobstar.chemobile.service;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import message.Acknowledge;
import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.service.handler.AcknowledgeHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.AllianceHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.GameObjectHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.GridHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.MessageHandler;
import mobile.che.com.oddymobstar.chemobile.service.handler.MissileHandler;
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
       //update key...need models though.
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



}
