package mobile.che.com.oddymobstar.chemobile.model;

import android.database.Cursor;

import message.CoreMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;

/**
 * Created by timmytime on 30/01/16.
 */
public class GameObject implements CheModelInterface {

    private String utmLat, utmLong, subUtmLat, subUtmLong, key, status;
    private double latitude, longitude, destLat, destLong, mass, force, range, impactRadius, maxSpeed, strength, maxStrength;
    private int type, subType;


    public GameObject() {

    }

    public GameObject(Cursor cursor) {

        setKey(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_KEY)));
        setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_STATUS)));
        setType(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_TYPE)));
        setSubType(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE)));
        setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_LAT)));
        setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_LONG)));
        setDestLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_DEST_LAT)));
        setDestLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_DEST_LONG)));
        setUtmLat(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)));
        setUtmLong(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LONG)));
        setSubUtmLat(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBUTM_LAT)));
        setSubUtmLong(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBUTM_LONG)));
        setMass(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_MASS)));
        setMaxSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_MAX_SPEED)));
        setImpactRadius(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_IMPACT_RADIUS)));
        setRange(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_RANGE)));
        setForce(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_FORCE)));
        setStrength(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_STRENGTH)));
        setMaxStrength(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_MAX_STRENGTH)));

    }

    @Override
    public void create(CoreMessage coreMessage) {

    }

    @Override
    public CoreMessage getMessage() {
        return null;
    }


    public String getUtmLat() {
        return utmLat;
    }

    public void setUtmLat(String utmLat) {
        this.utmLat = utmLat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUtmLong() {
        return utmLong;
    }

    public void setUtmLong(String utmLong) {
        this.utmLong = utmLong;
    }

    public String getSubUtmLat() {
        return subUtmLat;
    }

    public void setSubUtmLat(String subUtmLat) {
        this.subUtmLat = subUtmLat;
    }

    public String getSubUtmLong() {
        return subUtmLong;
    }

    public void setSubUtmLong(String subUtmLong) {
        this.subUtmLong = subUtmLong;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDestLatitude() {
        return destLat;
    }

    public void setDestLatitude(double latitude) {
        this.destLat = latitude;
    }

    public double getDestLongitude() {
        return destLong;
    }

    public void setDestLongitude(double longitude) {
        this.destLong = longitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getImpactRadius() {
        return impactRadius;
    }

    public void setImpactRadius(double impactRadius) {
        this.impactRadius = impactRadius;
    }

    public double getForce() {
        return force;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getMaxStrength() {
        return maxStrength;
    }

    public void setMaxStrength(double maxStrength) {
        this.maxStrength = maxStrength;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

}
