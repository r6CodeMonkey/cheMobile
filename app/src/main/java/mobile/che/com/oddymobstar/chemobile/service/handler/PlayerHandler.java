package mobile.che.com.oddymobstar.chemobile.service.handler;

import android.util.Log;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

import message.Acknowledge;
import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.MessageFactory;
import util.GameObjectTypes;
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

    public CheMessage createNewPlayer() throws NoSuchAlgorithmException {
        CheMessage cheMessage = messageFactory.createCheMessage();

        cheMessage.setMessage(Tags.PLAYER, messageFactory.createPlayer());
        cheMessage.setMessage(Tags.ACKNOWLEDGE, messageFactory.createAcknowledge());

        Log.d("new player message ", "is " + cheMessage.toString());

        return cheMessage;
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

        /*
        bi below.



          as a new player we are now going to purchase our free items.  possibly not here...but its the confirmed point,  so add a handler.

          player gets
          * garrison
          * 2 outposts
          * 2 satellite
          * 2 tanks
          * 2 atv
          * 2 mini drones
          * 50 g2g
          * 50 gta
          * 10 groundmines



        tech logic.  we need to create a message per type and call it back with relevant data.  message factory time....
         */
        //garrison
        GameObject gameObject = new GameObject();
        gameObject.setType(GameObjectGridFragment.INFASTRUCTURE);
        gameObject.setSubType(GameObjectTypes.GARRISON);
        cheCallback.send(messageFactory.purchaseGameObject(gameObject));

        //outposts, sats, tanks, atv, minidrones
        for (int i = 0; i < 2; i++) {
            gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.LAND);
            gameObject.setSubType(GameObjectTypes.TANK);
            cheCallback.send(messageFactory.purchaseGameObject(gameObject));
            gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.LAND);
            gameObject.setSubType(GameObjectTypes.SATELLITE);
            cheCallback.send(messageFactory.purchaseGameObject(gameObject));
            gameObject.setType(GameObjectGridFragment.LAND);
            gameObject.setSubType(GameObjectTypes.RV);
            cheCallback.send(messageFactory.purchaseGameObject(gameObject));
            gameObject.setType(GameObjectGridFragment.AIR);
            gameObject.setSubType(GameObjectTypes.MINI_DRONE);
            cheCallback.send(messageFactory.purchaseGameObject(gameObject));

        }
        //g2g, gta
        for (int i = 0; i < 50; i++) {
            gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.MISSILE);
            gameObject.setSubType(GameObjectTypes.G2G);
            cheCallback.send(messageFactory.purchaseGameObject(gameObject));
            gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.MISSILE);
            gameObject.setSubType(GameObjectTypes.G2A);
            cheCallback.send(messageFactory.purchaseGameObject(gameObject));
        }
        //groundmines
        for (int i = 0; i < 10; i++) {
            gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.MISSILE);
            gameObject.setSubType(GameObjectTypes.GROUND_MINE);
            cheCallback.send(messageFactory.purchaseGameObject(gameObject));
        }

    }
}
