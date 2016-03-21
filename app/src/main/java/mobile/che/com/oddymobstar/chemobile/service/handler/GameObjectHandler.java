package mobile.che.com.oddymobstar.chemobile.service.handler;

import android.location.Location;
import android.util.Log;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

import message.CheMessage;
import message.GameObject;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.util.MessageFactory;
import util.Tags;

/**
 * Created by timmytime on 30/01/16.
 */
public class GameObjectHandler extends MessageHandler {

    private final MessageFactory messageFactory;
    private CheCallbackInterface cheCallback;

    public GameObjectHandler(DBHelper dbHelper, MessageFactory messageFactory) {
        super(dbHelper);
        this.messageFactory = messageFactory;
    }

    //probably not required anyway leave here.
    public void addCheCallback(CheCallbackInterface cheCallback) {
        this.cheCallback = cheCallback;
    }

    @Override
    public void handle(CheMessage cheMessage) throws JSONException {

        GameObject gameObject = (GameObject) cheMessage.getMessage(Tags.GAME_OBJECT);

        Log.d("game object", "message state is "+gameObject.getState()+" value is "+gameObject.getValue());

        mobile.che.com.oddymobstar.chemobile.model.GameObject model = null;
        switch (gameObject.getState()) {
            case Tags.PURCHASE:
                //simple case under test now.
                model = new mobile.che.com.oddymobstar.chemobile.model.GameObject();
                model.setKey(gameObject.getKey());
                model.setType(gameObject.getType());
                model.setSubType(gameObject.getSubType());
                model.setStatus("");

                model.setForce(gameObject.getForce());
                model.setStrength(gameObject.getStrength());
                model.setRange(gameObject.getRange());
                model.setImpactRadius(gameObject.getImpactRadius());
                model.setMass(gameObject.getMass());
                model.setMaxSpeed(gameObject.getMaxSpeed());

                dbHelper.addGameObject(model);

                Log.d("purchase", "have purchased " + model.getType() + " " + model.getSubType());
                break;
            case Tags.GAME_OBJECT_ADD:
                //if we receive an ad
                model = new mobile.che.com.oddymobstar.chemobile.model.GameObject();
                //really this should be in object / model.  well perhaps not who knows its only in this point...
                model.setKey(gameObject.getKey());
                model.setType(gameObject.getType());
                model.setSubType(gameObject.getSubType());
                model = updateLocation(model, gameObject);
                model.setStatus(Tags.GAME_OBJECT_IS_FIXED);

                dbHelper.updateGameObject(model, false);

                Log.d("add", "have added game object " + gameObject.toString());

                break;
            case Tags.GAME_OBJECT_HIT:
                break;
            case Tags.GAME_OBJECT_MOVE:
                if (gameObject.getValue().equals(Tags.SUCCESS)) {
                    model = dbHelper.getGameObject(gameObject.getKey());
                    model = updateLocation(model, gameObject);
                    model.setStatus(Tags.GAME_OBJECT_IS_MOVING);
                    //done....talking to myself...
                    model.setDestLatitude(gameObject.getDestinationUtmLocation().getLatitude());
                    model.setDestLongitude(gameObject.getDestinationUtmLocation().getLongitude());

                    dbHelper.updateGameObject(model, false);

                } else {
                    //its invalid we need to send to user...note we need the screen to say so
                    Log.d("invalid move", "invalid move");
                }
                break;
            case Tags.GAME_OBJECT_STOP:  //only if we actually tell it to stop.
                model = dbHelper.getGameObject(gameObject.getKey());
                model = updateLocation(model, gameObject);
                model.setStatus(Tags.GAME_OBJECT_IS_FIXED);
                dbHelper.updateGameObject(model, true);
                break;
            case Tags.MESSAGE:  //these come from engine, not user actions.
                switch( gameObject.getValue()) {  //sorted...
                    case Tags.GAME_OBJECT_IS_MOVING:

                        //so now we need to update the models current position.
                        model = dbHelper.getGameObject(gameObject.getKey());
                        model.setLatitude(gameObject.getUtmLocation().getLatitude());
                        model.setLongitude(gameObject.getUtmLocation().getLongitude());

                        dbHelper.updateGameObject(model, false);

                        break;
                    case Tags.GAME_OBJECT_IS_FIXED:
                        //actually...we just need to send a message to sever, to tell it to stop as we have ack,  but if it calls twice not a huge issue.
                        //really need to add a dont bother to reply to me tag.
                        model = dbHelper.getGameObject(gameObject.getKey());
                        model = updateLocation(model, gameObject);
                        model.setStatus(Tags.GAME_OBJECT_IS_FIXED);
                        dbHelper.updateGameObject(model, true);
                        break;
                }
                break;
            case Tags.GAME_OBJECT_DESTROYED:
                break;
            case Tags.MISSILE_ADDED:
                ///so basically we just need to update the missile object...and save its data.  the server knows its active.
                // but if we want to view missiles on the actual vehicles we have a problem.
                model = dbHelper.getGameObject(gameObject.getMissiles().get(0).getKey());
                model = updateLocation(model, gameObject);
                dbHelper.updateGameObject(model, false);
                dbHelper.addMissileToGameObject(gameObject.getKey(), gameObject.getMissiles().get(0).getKey());
                break;
            case Tags.MISSILE_REMOVED:
                break;

        }
    }



    private mobile.che.com.oddymobstar.chemobile.model.GameObject updateLocation(mobile.che.com.oddymobstar.chemobile.model.GameObject model, GameObject gameObject){
        model.setLatitude(gameObject.getUtmLocation().getLatitude());
        model.setLongitude(gameObject.getUtmLocation().getLongitude());
        model.setUtmLat(gameObject.getUtmLocation().getUTM().getUTMLatGrid());
        model.setUtmLong(gameObject.getUtmLocation().getUTM().getUTMLongGrid());
        model.setSubUtmLat(gameObject.getUtmLocation().getSubUTM().getUTMLatGrid());
        model.setSubUtmLong(gameObject.getUtmLocation().getSubUTM().getUTMLongGrid());
        return model;

    }

}
