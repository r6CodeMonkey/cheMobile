package mobile.che.com.oddymobstar.chemobile.util;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import message.Acknowledge;
import message.Alliance;
import message.CheMessage;
import message.Missile;
import message.Player;
import message.UTM;
import message.UTMLocation;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import util.Tags;
import util.map.SubUTM;

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

    private UTM createUTM(String utmLat, String utmLong) {
        UTM utm = new UTM();
        utm.create();
        utm.setUTMLatGrid(utmLat);
        utm.setUTMLongGrid(utmLong);

        return utm;
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

    private UTMLocation createUTMLocation(double latitude, double longitude, String utmLat, String utmLong, String subUtmLat, String subUtmLong) {
        UTMLocation utmLocation = new UTMLocation();
        utmLocation.create();

        utmLocation.setLongitude(longitude);
        utmLocation.setLatitude(latitude);
        utmLocation.setSpeed(0);
        utmLocation.setAltitude(0);

        utmLocation.setUTM(createUTM(utmLat, utmLong));
        utmLocation.setSubUTM(createUTM(subUtmLat, subUtmLong));

        return utmLocation;
    }

    private UTMLocation createUTMLocation() {
        UTMLocation utmLocation = new UTMLocation();
        utmLocation.create();

        utmLocation.setLongitude(0.0);
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

    private Missile createMissile(GameObject missile, UTMLocation utmLocation, LatLng latLng) {
        Missile missileMessage = new Missile();
        missileMessage.create();

        missileMessage.setKey(missile.getKey());
        missileMessage.setCurrentUTMLocation(utmLocation);
        missileMessage.setStartUTMLocation(utmLocation);
        missileMessage.setTargetUTMLocation(latLng == null ? createUTMLocation() : createUTMLocation(latLng.latitude, latLng.longitude, "", "", "", ""));

        return missileMessage;
    }

    private message.GameObject createNewGameObject(GameObject gameObject, int quantity) {

        message.GameObject gameObjectMessage = new message.GameObject();
        gameObjectMessage.create();

        gameObjectMessage.setState(Tags.PURCHASE);
        gameObjectMessage.setQuantity(quantity);
        gameObjectMessage.setType(gameObject.getType());
        gameObjectMessage.setSubType(gameObject.getSubType());
        gameObjectMessage.setUtmLocation(createUTMLocation());
        gameObjectMessage.setDestinationUtmLocation(createUTMLocation());


        return gameObjectMessage;
    }

    private message.GameObject createGameObject(GameObject gameObject, Location location) {
        message.GameObject gameObjectMessage = new message.GameObject();
        gameObjectMessage.create();

        gameObjectMessage.setState(Tags.GAME_OBJECT_ADD);
        gameObjectMessage.setKey(gameObject.getKey());
        gameObjectMessage.setType(gameObject.getType());
        gameObjectMessage.setSubType(gameObject.getSubType());
        gameObject.setStrength(gameObject.getStrength());
        gameObjectMessage.setUtmLocation(createUTMLocation(location));
        gameObjectMessage.setDestinationUtmLocation(createUTMLocation());


        return gameObjectMessage;
    }

    private message.GameObject createGameObjectStop(GameObject gameObject) {
        message.GameObject gameObjectMessage = new message.GameObject();
        gameObjectMessage.create();

        gameObjectMessage.setState(Tags.GAME_OBJECT_STOP);
        gameObjectMessage.setKey(gameObject.getKey());
        gameObjectMessage.setType(gameObject.getType());
        gameObjectMessage.setSubType(gameObject.getSubType());
        gameObject.setStrength(gameObject.getStrength());
        //double latitude, double longitude, String utmLat, String utmLong, String subUtmLat, String subUtmLong
        gameObjectMessage.setUtmLocation(createUTMLocation(gameObject.getLatitude(), gameObject.getLongitude(), gameObject.getUtmLat(),
                gameObject.getUtmLong(), gameObject.getSubUtmLat(), gameObject.getSubUtmLong()));  //we need latest location.
        gameObjectMessage.setDestinationUtmLocation(createUTMLocation());


        return gameObjectMessage;
    }

    private message.GameObject createGameObjectWithExplosive(GameObject gameObject, GameObject explosive, LatLng latLng, String state) {
        message.GameObject gameObjectMessage = new message.GameObject();
        gameObjectMessage.create();

        gameObjectMessage.setState(state);
        gameObjectMessage.setKey(gameObject.getKey());
        gameObjectMessage.setType(gameObject.getType());
        gameObjectMessage.setSubType(gameObject.getSubType());
        gameObject.setStrength(gameObject.getStrength());

        UTMLocation utmLocation = createUTMLocation(gameObject.getLatitude(),
                gameObject.getLongitude(), gameObject.getUtmLat(),
                gameObject.getUtmLong(), gameObject.getSubUtmLat(),
                gameObject.getSubUtmLong());
        gameObjectMessage.setUtmLocation(utmLocation);
        gameObjectMessage.setDestinationUtmLocation(latLng == null ?
                createUTMLocation() :
                createUTMLocation(latLng.latitude, latLng.longitude, "", "", "", ""));

        //we only set the one we are adding...or do we send all of them?  just add
        List<Missile> missiles = new ArrayList<>();
        missiles.add(createMissile(explosive, utmLocation, latLng));
        gameObjectMessage.setMissiles(missiles);

        return gameObjectMessage;
    }

    private message.GameObject createGameObjectValidator(GameObject gameObject,  List<SubUTM> validators, String tag){
        message.GameObject gameObjectMessage = new message.GameObject();
        gameObjectMessage.create();

        gameObjectMessage.setState(tag);
        gameObjectMessage.setKey(gameObject.getKey());
        gameObjectMessage.setType(gameObject.getType());
        gameObjectMessage.setSubType(gameObject.getSubType());

        gameObjectMessage.setUtmLocation(createUTMLocation(gameObject.getLatitude(), gameObject.getLongitude(),
                gameObject.getUtmLat(), gameObject.getUtmLong(), gameObject.getSubUtmLat(), gameObject.getSubUtmLong()));

        gameObjectMessage.setDestinationUtmLocation(createUTMLocation(gameObject.getLatitude(), gameObject.getLongitude(),
                gameObject.getUtmLat(), gameObject.getUtmLong(), gameObject.getSubUtmLat(), gameObject.getSubUtmLong()));


        List<UTM> utms = new ArrayList<>();
        for (SubUTM subUTM : validators) {
            utms.add(createUTM(subUTM.getSubUtmLat(), subUTM.getSubUtmLong()));
        }

        gameObjectMessage.setDestinationValidator(utms);

        return gameObjectMessage;
    }

    private message.GameObject createGameObjectMove(GameObject gameObject, List<SubUTM> validators, LatLng destination) {
        message.GameObject gameObjectMessage = new message.GameObject();
        gameObjectMessage.create();

        gameObjectMessage.setState(Tags.GAME_OBJECT_MOVE);
        gameObjectMessage.setKey(gameObject.getKey());
        gameObjectMessage.setType(gameObject.getType());
        gameObjectMessage.setSubType(gameObject.getSubType());

        gameObjectMessage.setUtmLocation(createUTMLocation(gameObject.getLatitude(), gameObject.getLongitude(),
                gameObject.getUtmLat(), gameObject.getUtmLong(), gameObject.getSubUtmLat(), gameObject.getSubUtmLong()));
        gameObjectMessage.setDestinationUtmLocation(createUTMLocation(destination.latitude, destination.longitude, "", "", "", ""));


        List<UTM> utms = new ArrayList<>();
        for (SubUTM subUTM : validators) {
            utms.add(createUTM(subUTM.getSubUtmLat(), subUTM.getSubUtmLong()));
        }
        gameObjectMessage.setDestinationValidator(utms);

        return gameObjectMessage;
    }


    private message.GameObject createGameObjectRoundTrip(GameObject gameObject,LatLng destination) {
        message.GameObject gameObjectMessage = new message.GameObject();
        gameObjectMessage.create();

        gameObjectMessage.setState(Tags.GAME_OBJECT_MOVE_ROUNDTRIP);
        gameObjectMessage.setKey(gameObject.getKey());
        gameObjectMessage.setType(gameObject.getType());
        gameObjectMessage.setSubType(gameObject.getSubType());

        gameObjectMessage.setUtmLocation(createUTMLocation(gameObject.getLatitude(), gameObject.getLongitude(),
                gameObject.getUtmLat(), gameObject.getUtmLong(), gameObject.getSubUtmLat(), gameObject.getSubUtmLong()));
        gameObjectMessage.setDestinationUtmLocation(createUTMLocation(destination.latitude, destination.longitude, "", "", "", ""));

        List<UTM> utms = new ArrayList<>();

        gameObjectMessage.setDestinationValidator(utms);

        return gameObjectMessage;
    }

    private Acknowledge createCheAcknowledge(String key) {
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

    private Player createPlayer(Location location) {
        Player player = getPlayer();
        player.setUTMLocation(createUTMLocation(location));


        return player;
    }

    private Player createPlayer() {
        Player player = getPlayer();
        player.setUTMLocation(createUTMLocation());


        return player;
    }

    public CheMessage createNewPlayer() throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();

        cheMessage.setMessage(Tags.PLAYER, createPlayer());
        cheMessage.setMessage(Tags.ACKNOWLEDGE, createAcknowledge());

        return cheMessage;

    }

    public CheMessage createPlayerReconnect(Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();

        Player player = location == null ? createPlayer() : createPlayer(location);
        player.setState(Tags.CONNECT);
        player.setValue(Tags.SUCCESS);

        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.ACKNOWLEDGE, createAcknowledge());

        return cheMessage;
    }

    public CheMessage createCheAcknowledge(CheMessage cheContents) throws NoSuchAlgorithmException, JSONException {

        CheMessage cheMessage = createCheMessage();

        cheMessage.setMessage(Tags.CHE_ACKNOWLEDGE, createCheAcknowledge(cheContents.getMessage(Tags.CHE_ACKNOWLEDGE).getString(Tags.CHE_ACK_ID)).getContents());

        cheMessage.setMessage(Tags.ACKNOWLEDGE, createAcknowledge());
        cheMessage.setMessage(Tags.PLAYER, createPlayer());


        return cheMessage;
    }

    public Alliance createAlliance(String name, String key, Location location) throws NoSuchAlgorithmException {
        Alliance alliance = new Alliance();
        alliance.create();

        alliance.setKey(key);
        alliance.setName(name);

        List<Player> allianceMembers = new ArrayList<>();
        allianceMembers.add(createPlayer(location));

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
        Alliance alliance = createAlliance(name, "", location);
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
        Alliance allianceMessage = createAlliance(alliance.getName(), alliance.getKey(), location);
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

        Log.d("purchase", "purchase msg " + cheMessage.toString());


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

        Log.d("deploy", "deploy msg " + cheMessage.toString());

        return cheMessage;

    }

    public CheMessage createDeployToBase(GameObject gameObject, GameObject baseObject, Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();

        Location baseLocation = new Location("");
        baseLocation.setLatitude(baseObject.getLatitude());
        baseLocation.setLongitude(baseObject.getLongitude());
        message.GameObject gameObjectMessage = createGameObject(gameObject, baseLocation);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        Log.d("deploy", "deploy msg " + cheMessage.toString());

        return cheMessage;

    }


    public CheMessage armExplosive(GameObject gameObject, GameObject explosive, Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();
        message.GameObject gameObjectMessage = createGameObjectWithExplosive(gameObject, explosive, null, Tags.MISSILE_ADDED);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        return cheMessage;

    }

    public CheMessage setTarget(GameObject gameObject, GameObject explosive, LatLng target, Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();

        message.GameObject gameObjectMessage = createGameObjectWithExplosive(gameObject, explosive, target, Tags.MISSILE_TARGET);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        return cheMessage;

    }

    public CheMessage roundTripMessage(GameObject gameObject,  LatLng destination, Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();

        message.GameObject gameObjectMessage = createGameObjectRoundTrip(gameObject, destination);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        Log.d("move", "move msg " + cheMessage.toString());

        return cheMessage;
    }

    public CheMessage moveGameObject(GameObject gameObject, List<SubUTM> validators, LatLng destination, Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();

        message.GameObject gameObjectMessage = createGameObjectMove(gameObject, validators, destination);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        Log.d("move", "move vaidator size " + validators.size());
        Log.d("move", "move msg " + cheMessage.toString());

        return cheMessage;
    }





    public CheMessage stopGameObject(GameObject gameObject, Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();

        message.GameObject gameObjectMessage = createGameObjectStop(gameObject);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        Log.d("move", "stop msg " + cheMessage.toString());

        return cheMessage;
    }

    public CheMessage getMissileLaunch(GameObject gameObject, Location location) throws NoSuchAlgorithmException {


        Log.d("move", "key is " + gameObject.getKey());


        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();
        message.GameObject gameObjectMessage = createGameObjectWithExplosive(gameObject, gameObject, new LatLng(gameObject.getDestLatitude(), gameObject.getDestLongitude()), Tags.MISSILE_FIRE);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        Log.d("move", "launch message " + cheMessage.toString());

        return cheMessage;
    }

    public CheMessage getMissileHit(GameObject gameObject, Location location, String hitOrDestroyed) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();
        message.GameObject gameObjectMessage = createGameObject(gameObject, location);
        gameObjectMessage.setState(hitOrDestroyed);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        Log.d("move", "launch message " + cheMessage.toString());

        return cheMessage;
    }

    public CheMessage createRepairMessage(GameObject gameObject, Location location) throws NoSuchAlgorithmException {
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();

        message.GameObject gameObjectMessage = createGameObject(gameObject, location);
        gameObjectMessage.setState(Tags.GAME_OBJECT_REPAIR);

        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        Log.d("repair", "repair message " + cheMessage.toString());

        return cheMessage;
    }

    public CheMessage getSatelliteMessage(GameObject gameObject,List<SubUTM> validators, Location location, String tag) throws NoSuchAlgorithmException{
        CheMessage cheMessage = createCheMessage();
        Player player = createPlayer(location);
        Acknowledge acknowledge = createAcknowledge();

        message.GameObject gameObjectMessage = createGameObjectValidator(gameObject,validators, tag);


        cheMessage.setMessage(Tags.ACKNOWLEDGE, acknowledge);
        cheMessage.setMessage(Tags.PLAYER, player);
        cheMessage.setMessage(Tags.GAME_OBJECT, gameObjectMessage);

        Log.d("satellite", "satellite message " + cheMessage.toString());


        return cheMessage;

    }


}
