package mobile.che.com.oddymobstar.chemobile.util;

import android.location.Location;
import android.util.Log;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import message.Acknowledge;
import message.Alliance;
import message.CheMessage;
import message.Player;
import message.UTM;
import message.UTMLocation;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import util.Tags;

/**
 * Created by timmytime on 05/02/16.
 */
public class MessageFactory {

    private final DBHelper dbHelper;

    public MessageFactory(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public CheMessage createCheMessage() {
        CheMessage cheMessage = new CheMessage();
        cheMessage.create();

        return cheMessage;
    }

    private UTM createUTM() {
        UTM utm = new UTM();
        utm.create();
        utm.setUTMLatGrid(dbHelper.getConfig(Configuration.CURRENT_UTM_LAT).getValue());
        utm.setUTMLongGrid(dbHelper.getConfig(Configuration.CURRENT_UTM_LONG).getValue());

        return utm;
    }

    private UTM createSubUTM() {
        UTM utm = new UTM();
        utm.create();
        utm.setUTMLatGrid(dbHelper.getConfig(Configuration.CURRENT_SUBUTM_LAT).getValue());
        utm.setUTMLongGrid(dbHelper.getConfig(Configuration.CURRENT_SUBUTM_LONG).getValue());

        return utm;
    }

    private UTMLocation createUTMLocation(Location location) {
        UTMLocation utmLocation = new UTMLocation();
        utmLocation.create();

        utmLocation.setLongitude(location.getLongitude());
        utmLocation.setLatitude(location.getLatitude());
        utmLocation.setSpeed(location.getSpeed());
        utmLocation.setAltitude(location.getAltitude());

        utmLocation.setUTM(createUTM());
        utmLocation.setSubUTM(createSubUTM());

        return utmLocation;
    }

    private UTMLocation createUTMLocation() {
        UTMLocation utmLocation = new UTMLocation();
        utmLocation.create();

        utmLocation.setLongitude(0);
        utmLocation.setLatitude(0);
        utmLocation.setSpeed(0);
        utmLocation.setAltitude(0);

        utmLocation.setUTM(createUTM());
        utmLocation.setSubUTM(createSubUTM());

        return utmLocation;
    }

    private Player getPlayer() {
        Player player = new Player();
        player.create();
        player.setKey(dbHelper.getConfig(Configuration.PLAYER_KEY).getValue());
        player.setName(dbHelper.getConfig(Configuration.PLAYER_NAME).getValue());
        player.setKey(dbHelper.getConfig(Configuration.PLAYER_KEY).getValue());
        //    player.setImage(dbHelper.getConfig(Configuration.));

        return player;
    }

    private message.GameObject createNewGameObject(GameObject gameObject, int quantity) {

        message.GameObject gameObjectMessage = new message.GameObject();
        gameObjectMessage.create();

        gameObjectMessage.setState(Tags.PURCHASE);
        gameObjectMessage.setQuantity(quantity);
        gameObjectMessage.setType(gameObject.getType());
        gameObjectMessage.setSubType(gameObject.getSubType());
        gameObjectMessage.setUtmLocation(createUTMLocation());

        return gameObjectMessage;
    }

    private message.GameObject createGameObject(GameObject gameObject, Location location){
        message.GameObject gameObjectMessage = new message.GameObject();
        gameObjectMessage.create();

        gameObjectMessage.setState(Tags.GAME_OBJECT_ADD);
        gameObjectMessage.setKey(gameObject.getKey());
        gameObjectMessage.setType(gameObject.getType());
        gameObjectMessage.setSubType(gameObject.getSubType());
        gameObjectMessage.setUtmLocation(createUTMLocation(location));


        return gameObjectMessage;
    }

    public Player createPlayer() {
        Player player = getPlayer();
        player.setUTMLocation(createUTMLocation());

        return player;
    }

    public Acknowledge createCheAcknowledge(String key) {
        Acknowledge acknowledge = new Acknowledge(true);
        acknowledge.create();
        acknowledge.setKey(key);
        acknowledge.setState(Tags.CHE_ACK_ID);
        acknowledge.setValue(Tags.ACCEPT);


        return acknowledge;

    }

    public Acknowledge createAcknowledge() throws NoSuchAlgorithmException {

        Acknowledge acknowledge = new Acknowledge();
        acknowledge.create();
        acknowledge.setKey(UUIDGenerator.generateKey());

        return acknowledge;

    }

    public Player createPlayer(Location location) {
        Player player = getPlayer();
        player.setUTMLocation(createUTMLocation(location));


        return player;
    }

    public Alliance createAlliance(String name, String key) throws NoSuchAlgorithmException {
        Alliance alliance = new Alliance();
        alliance.create();

        alliance.setKey(key);
        alliance.setName(name);

        List<Player> allianceMembers = new ArrayList<>();
        allianceMembers.add(createPlayer());

        alliance.setMembers(allianceMembers);

        return alliance;
    }


    public CheMessage locationChangedMessage(Location location) throws NoSuchAlgorithmException {

        CheMessage cheMessage = createCheMessage();
        cheMessage.setMessage(Tags.PLAYER, createPlayer(location));
        cheMessage.setMessage(Tags.ACKNOWLEDGE, createAcknowledge());

        return cheMessage;
    }

    public CheMessage newAllianceMessage(String name, Location location) throws NoSuchAlgorithmException, JSONException {

        CheMessage cheMessage = createCheMessage();
        Alliance alliance = createAlliance(name, "");
        alliance.setState(Tags.ALLIANCE_CREATE);
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();

        cheMessage.setMessage(Tags.ALLIANCE, alliance);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);

        return cheMessage;
    }

    public CheMessage allianceChatPostMessage(mobile.che.com.oddymobstar.chemobile.model.Alliance alliance, String message, Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Alliance allianceMessage = createAlliance(alliance.getName(), alliance.getKey());
        allianceMessage.setState(Tags.ALLIANCE_POST);
        allianceMessage.setValue(message);
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.ALLIANCE, allianceMessage);

        return cheMessage;
    }

    /*
      need a purchase message (well a free one as the paid one maybe a bit different,..probably not but needs to go via playstore on ack
     */
    public CheMessage purchaseGameObject(GameObject gameObject, Location location, int quantity) throws NoSuchAlgorithmException {

        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();
        message.GameObject gameObjectMessage = createNewGameObject(gameObject, quantity);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);


        return cheMessage;
    }

    public CheMessage createDeploy(GameObject gameObject, Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();
        message.GameObject gameObjectMessage = createGameObject(gameObject, location);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        Log.d("deploy", "deploy msg "+cheMessage.toString());

        return  cheMessage;

    }


}
