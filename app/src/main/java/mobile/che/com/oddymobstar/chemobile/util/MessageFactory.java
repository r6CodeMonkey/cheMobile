package mobile.che.com.oddymobstar.chemobile.util;

import android.location.Location;

import java.security.NoSuchAlgorithmException;

import message.Acknowledge;
import message.CheMessage;
import message.Player;
import message.UTM;
import message.UTMLocation;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
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

        utm.setUTMLongGrid(dbHelper.getConfig(Configuration.CURRENT_UTM_LONG).getValue());
        utm.setUTMLatGrid(dbHelper.getConfig(Configuration.CURRENT_UTM_LAT).getValue());

        return utm;
    }

    private UTM createSubUTM() {
        UTM utm = new UTM();
        utm.create();

        utm.setUTMLongGrid(dbHelper.getConfig(Configuration.CURRENT_SUBUTM_LONG).getValue());
        utm.setUTMLatGrid(dbHelper.getConfig(Configuration.CURRENT_SUBUTM_LAT).getValue());

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
        player.setName(dbHelper.getConfig(Configuration.PLAYER_NAME).getValue());
        player.setKey(dbHelper.getConfig(Configuration.PLAYER_KEY).getValue());
        //    player.setImage(dbHelper.getConfig(Configuration.));

        return player;
    }

    public Player createPlayer() {
        Player player = getPlayer();
        player.setUTMLocation(createUTMLocation());

        return player;
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


    public CheMessage locationChanged(Location location) throws NoSuchAlgorithmException {

        CheMessage cheMessage = createCheMessage();
        cheMessage.setMessage(Tags.PLAYER, createPlayer(location));
        cheMessage.setMessage(Tags.ACKNOWLEDGE, createAcknowledge());

        return cheMessage;
    }
}
