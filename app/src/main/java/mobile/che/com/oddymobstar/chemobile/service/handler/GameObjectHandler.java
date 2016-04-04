package mobile.che.com.oddymobstar.chemobile.service.handler;

import android.util.Log;

import org.json.JSONException;

import message.CheMessage;
import message.GameObject;
import message.Missile;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.Message;
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
        Message message = null;
        Log.d("game object", "message state is " + gameObject.getState() + " value is " + gameObject.getValue());

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
            case Tags.SATELLITE_START_LISTEN:
                model = dbHelper.getGameObject(gameObject.getKey());
                model.setStatus(Tags.SATELLITE_START_LISTEN);
                dbHelper.updateGameObject(model, true, false);
                break;
            case Tags.SATELLITE_STOP_LISTEN:
                model = dbHelper.getGameObject(gameObject.getKey());
                model.setStatus(Tags.SATELLITE_STOP_LISTEN);
                dbHelper.updateGameObject(model, true, false);
                break;
            case Tags.GAME_OBJECT_ADD:
                //if we receive an ad
                model = dbHelper.getGameObject(gameObject.getKey());
                //really this should be in object / model.  well perhaps not who knows its only in this point...
                model.setKey(gameObject.getKey());
                model.setType(gameObject.getType());
                model.setSubType(gameObject.getSubType());
                model = updateLocation(model, gameObject);
                model.setStatus(Tags.GAME_OBJECT_IS_FIXED);

                dbHelper.updateGameObject(model, false, true);

                Log.d("add", "have added game object " + gameObject.toString());

                break;
            case Tags.GAME_OBJECT_MOVE:
                if (gameObject.getValue().equals(Tags.SUCCESS)) {
                    model = dbHelper.getGameObject(gameObject.getKey());
                    model = updateLocation(model, gameObject);
                    model.setStatus(Tags.GAME_OBJECT_IS_MOVING);
                    //done....talking to myself...
                    model.setDestLatitude(gameObject.getDestinationUtmLocation().getLatitude());
                    model.setDestLongitude(gameObject.getDestinationUtmLocation().getLongitude());

                    dbHelper.updateGameObject(model, false, true);

                } else {
                    //its invalid we need to send to user...note we need the screen to say so
                    Log.d("invalid move", "invalid move");
                }
                break;
            case Tags.GAME_OBJECT_STOP:  //only if we actually tell it to stop.
                model = dbHelper.getGameObject(gameObject.getKey());
                model = updateLocation(model, gameObject);
                model.setStatus(Tags.GAME_OBJECT_IS_FIXED);
                dbHelper.updateGameObject(model, true, true);
                break;
            case Tags.MESSAGE:  //these come from engine, not user actions.

                switch (gameObject.getValue()) {  //sorted...
                    case Tags.GAME_OBJECT_REPAIR:
                        model = dbHelper.getGameObject(gameObject.getKey());

                        message = new Message();
                        message.setTime(System.currentTimeMillis());
                        message.setMessage("Game Object " + gameObject.getKey() + " Repaired");
                        dbHelper.addVidiNews(message);

                        if (model != null) {
                            model.setStrength(gameObject.getStrength());
                            model.setStatus(Tags.GAME_OBJECT_IS_FIXED); //it would of been repair previously.
                            dbHelper.updateGameObject(model, true, false);
                        }

                        break;
                    case Tags.GAME_OBJECT_REINFORCE:  //this probably will not go in here. and not doing it yet.
                        break;
                    case Tags.GAME_OBJECT_IS_MOVING:

                        message = new Message();
                        message.setTime(System.currentTimeMillis());
                        message.setMessage("Game Object " + gameObject.getKey() + " Moving in Sector " +
                                gameObject.getUtmLocation().getUTM().getUTMLatGrid() + gameObject.getUtmLocation().getUTM().getUTMLongGrid() + " / " +
                                gameObject.getUtmLocation().getSubUTM().getUTMLatGrid() + gameObject.getUtmLocation().getSubUTM().getUTMLongGrid());
                        dbHelper.addVidiNews(message);
                        //so now we need to update the models current position.
                        model = dbHelper.getGameObject(gameObject.getKey());
                        if (model != null) {
                            model.setLatitude(gameObject.getUtmLocation().getLatitude());
                            model.setLongitude(gameObject.getUtmLocation().getLongitude());

                            dbHelper.updateGameObject(model, false, true);
                            dbHelper.updateMissiles(model);
                        }

                        break;
                    case Tags.GAME_OBJECT_IS_FIXED:
                        //actually...we just need to send a message to sever, to tell it to stop as we have ack,  but if it calls twice not a huge issue.
                        //really need to add a dont bother to reply to me tag.
                        message = new Message();
                        message.setTime(System.currentTimeMillis());
                        message.setMessage("Game Object " + gameObject.getKey() + " Stopped in Sector " +
                                gameObject.getUtmLocation().getUTM().getUTMLatGrid() + gameObject.getUtmLocation().getUTM().getUTMLongGrid() + " / " +
                                gameObject.getUtmLocation().getSubUTM().getUTMLatGrid() + gameObject.getUtmLocation().getSubUTM().getUTMLongGrid());
                        dbHelper.addVidiNews(message);
                        model = dbHelper.getGameObject(gameObject.getKey());
                        if (model != null) {
                            model = updateLocation(model, gameObject);
                            model.setStatus(Tags.GAME_OBJECT_IS_FIXED);
                            dbHelper.updateGameObject(model, true, true);
                            dbHelper.updateMissiles(model);
                        }
                        break;
                    case Tags.GAME_OBJECT_LEFT:
                        Log.d("game object moving", "game object has left sector"); //possible send a message only...
                        message = new Message();
                        message.setTime(System.currentTimeMillis());
                        message.setMessage("Game Object " + gameObject.getKey() + " Left Sector " +
                                gameObject.getUtmLocation().getUTM().getUTMLatGrid() + gameObject.getUtmLocation().getUTM().getUTMLongGrid() + " / " +
                                gameObject.getUtmLocation().getSubUTM().getUTMLatGrid() + gameObject.getUtmLocation().getSubUTM().getUTMLongGrid());
                        dbHelper.addVidiNews(message);
                        break;
                    case Tags.MISSILE_LAUNCHED:
                        //do nothing...
                        Log.d("missile moving", "missile is moving!"); //possible send a message only...
                        message = new Message();
                        message.setTime(System.currentTimeMillis());
                        message.setMessage("Missile " + gameObject.getKey() + " Travelling");
                        dbHelper.addVidiNews(message);
                        break;
                    case Tags.MISSILE_DESTROYED:
                        //new method. also need to capture information in alert if user not on game.
                        Log.d("missile moving", "missile has detonated! " + gameObject.getKey());
                        model = dbHelper.getGameObject(gameObject.getKey());
                        if (model != null) {
                            dbHelper.missileTargetReached(model);
                        }
                        break;
                    case Tags.GAME_OBJECT_DESTROYED:
                        //remove object and tell server
                        message = new Message();
                        message.setTime(System.currentTimeMillis());
                        message.setMessage("Game Object " + gameObject.getKey() + " Destroyed in Sector " +
                                gameObject.getUtmLocation().getUTM().getUTMLatGrid() + gameObject.getUtmLocation().getUTM().getUTMLongGrid() + " / " +
                                gameObject.getUtmLocation().getSubUTM().getUTMLatGrid() + gameObject.getUtmLocation().getSubUTM().getUTMLongGrid());
                        dbHelper.addVidiNews(message);
                        model = dbHelper.getGameObject(gameObject.getKey());
                        if (model != null) {
                            Log.d("object destroyed", "updating database");
                            dbHelper.deleteGameObject(model);
                        }
                        break;
                    case Tags.GAME_OBJECT_HIT:
                        //we simply indicate we have lost some strength to database.
                        message = new Message();
                        message.setTime(System.currentTimeMillis());
                        message.setMessage("Game Object " + gameObject.getKey() + " Hit in Sector " +
                                gameObject.getUtmLocation().getUTM().getUTMLatGrid() + gameObject.getUtmLocation().getUTM().getUTMLongGrid() + " / " +
                                gameObject.getUtmLocation().getSubUTM().getUTMLatGrid() + gameObject.getUtmLocation().getSubUTM().getUTMLongGrid());
                        dbHelper.addVidiNews(message);
                        model = dbHelper.getGameObject(gameObject.getKey());
                        if (model != null) {
                            Log.d("object hit", "updating database strength is " + gameObject.getStrength());
                            model.setStrength(gameObject.getStrength());
                            model.setStatus(Tags.GAME_OBJECT_HIT);
                            dbHelper.updateGameObject(model, true, true);
                        }
                        break;
                }
                break;
            case Tags.MISSILE_ADDED:
                ///so basically we just need to update the missile object...and save its data.  the server knows its active.
                // but if we want to view missiles on the actual vehicles we have a problem.
                model = dbHelper.getGameObject(gameObject.getMissiles().get(0).getKey());
                model = updateLocation(model, gameObject);
                dbHelper.updateGameObject(model, false, true);
                dbHelper.addMissileToGameObject(gameObject.getKey(), gameObject.getMissiles().get(0).getKey());
                break;
            case Tags.MISSILE_REMOVED:
                break;
            case Tags.MISSILE_TARGET:
                //this is really easy. simply confirm that the missile is set on target(need a status for missile)
                Missile missile = gameObject.getMissiles().get(0);
                model = dbHelper.getGameObject(missile.getKey());
                model.setStatus(Tags.MISSILE_TARGET);
                //we now need to get the actual missile
                model.setDestLatitude(missile.getTargetUTMLocation().getLatitude());
                model.setDestLongitude(missile.getTargetUTMLocation().getLongitude());
                dbHelper.updateGameObject(model, false, true); //well it could be moving.  anyway.  need to inform
                break;
            case Tags.MISSILE_CANCEL:
                break; //this will also be here.  fire is only thing missile knows about.
            case Tags.MISSILE_FIRE:
                model = dbHelper.getGameObject(gameObject.getKey());
                //we will receive updates that its launched.  so update the
                model.setStatus(Tags.MISSILE_LAUNCHED);
                dbHelper.updateGameObject(model, false, false); //no update yet.
                message = new Message();
                message.setTime(System.currentTimeMillis());
                message.setMessage("Missile " + gameObject.getKey() + " Launched");
                dbHelper.addVidiNews(message);
                break;

        }
    }


    private mobile.che.com.oddymobstar.chemobile.model.GameObject updateLocation(mobile.che.com.oddymobstar.chemobile.model.GameObject model, GameObject gameObject) {
        model.setLatitude(gameObject.getUtmLocation().getLatitude());
        model.setLongitude(gameObject.getUtmLocation().getLongitude());
        model.setUtmLat(gameObject.getUtmLocation().getUTM().getUTMLatGrid());
        model.setUtmLong(gameObject.getUtmLocation().getUTM().getUTMLongGrid());
        model.setSubUtmLat(gameObject.getUtmLocation().getSubUTM().getUTMLatGrid());
        model.setSubUtmLong(gameObject.getUtmLocation().getSubUTM().getUTMLongGrid());
        return model;

    }

}
