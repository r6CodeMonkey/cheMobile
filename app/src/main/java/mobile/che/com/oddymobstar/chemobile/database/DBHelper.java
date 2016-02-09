package mobile.che.com.oddymobstar.chemobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.handler.MessageHandler;
import mobile.che.com.oddymobstar.chemobile.model.Alliance;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.model.Message;
import mobile.che.com.oddymobstar.chemobile.model.UserImage;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;

/**
 * Created by timmytime on 19/01/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    //table names.
    public static final String CONFIG_TABLE = "CONFIG";
    public static final String ALLIANCES_TABLE = "ALLIANCES";
    public static final String ALLIANCE_MEMBERS_TABLE = "ALLIANCE_MEMBERS";
    public static final String MESSAGE_TABLE = "MESSAGES";
    public static final String IMAGE_TABLE = "USER_IMAGES";
    public static final String UTM = "utm";
    public static final String SUBUTM = "subutm";
    //column tags
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String SPEED = "speed";
    public static final String ALTITUDE = "altitude";
    public static final String CONFIG_ID = "config_id";
    public static final String CONFIG_NAME = "config_name";
    public static final String CONFIG_VALUE = "config_value";
    public static final String CONFIG_MARKUP = "config_markup";
    public static final String CONFIG_TYPE = "config_type";
    public static final String CONFIG_VISIBLE = "config_visible";
    public static final String USER_IMAGE_KEY = "user_key";
    public static final String USER_IMAGE = "user_image";
    public static final String GRID_KEY = "grid_key";
    public static final String ALLIANCE_KEY = "alliance_key";
    public static final String PLAYER_KEY = "player_key";
    public static final String ALLIANCE_NAME = "alliance_name";
    public static final String PLAYER_NAME = "player_name";
    public static final String MESSAGE_ID = "message_id";
    public static final String MESSAGE_CONTENT = "message_content";
    public static final String MESSAGE_TIME = "message_time";
    public static final String MESSAGE_KEY = "message_key";
    public static final String MESSAGE_TYPE = "message_type";
    public static final String MY_MESSAGE = "my_message";
    public static final String MESSAGE_AUTHOR = "message_author";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PROJECTCHE";
    //table creates
    private static final String CREATE_CONFIG = "CREATE TABLE " + CONFIG_TABLE + " (" + CONFIG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + CONFIG_NAME + " VARCHAR2(30)," + CONFIG_VALUE + " VARCHAR2(30), " + CONFIG_MARKUP + " VARCHAR2(50)," + CONFIG_TYPE + " CHAR(1), " + CONFIG_VISIBLE + " CHAR(1))";
    private static final String CREATE_ALLIANCES = "CREATE TABLE " + ALLIANCES_TABLE + " (" + ALLIANCE_KEY + " VARCHAR2(200) UNIQUE NOT NULL," + ALLIANCE_NAME + " VARCHAR2(30))";
    private static final String CREATE_ALLIANCE_MEMBERS = "CREATE TABLE " + ALLIANCE_MEMBERS_TABLE + " (" + ALLIANCE_KEY + " VARCHAR2(200)," + PLAYER_KEY + " VARCHAR2(200)," + PLAYER_NAME + " VARCHAR2(30)," + LATITUDE + " NUMBER, " + LONGITUDE + " NUMBER, " + UTM + " VARCHAR2(10)," + SUBUTM + " VARCHAR2(10)," + SPEED + " NUMBER," + ALTITUDE + " NUMBER)";
    private static final String CREATE_MESSAGES = "CREATE TABLE " + MESSAGE_TABLE + "(" + MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + MESSAGE_CONTENT + " VARCHAR2(300), " + MESSAGE_KEY + " VARCHAR2(200)," + MESSAGE_TYPE + " CHAR(1), " + MESSAGE_TIME + " INTEGER," + MY_MESSAGE + " CHAR(1)," + MESSAGE_AUTHOR + " VARCHAR2(200) )";
    private static final String CREATE_USER_IMAGES = "CREATE TABLE " + IMAGE_TABLE + "(" + USER_IMAGE_KEY + " VARCHAR2(200)," + USER_IMAGE + " BLOB)";

    //static db instance.
    private static DBHelper dbHelper = null;
    /*
     need to put other shit here.  bear in mind this will likely become a defacto class that is deliverable
     and also ported to iOS / Windows set up.  im sure they also use SQLite.
      */
    private MessageHandler messageHandler;


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }

        return dbHelper;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }


    /*
  due to locking issues etc and i dont want to pass the instance around but use standard methods.
   */
    public boolean hasPreLoad() {
        Cursor cursor = this.getReadableDatabase().rawQuery(
                "SELECT COUNT(1) as id FROM " + CONFIG_TABLE, null);
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.moveToLast();
        }

        cursor.close();

        return count != 0;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONFIG);
        db.execSQL(CREATE_ALLIANCES);
        db.execSQL(CREATE_ALLIANCE_MEMBERS);
        db.execSQL(CREATE_MESSAGES);
        db.execSQL(CREATE_USER_IMAGES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addBaseConfiguration() {
        Config config = new Config(Configuration.PORT, "8085", "Port", Config.BASE, false);
        addConfig(config);
        config = new Config(Configuration.URL, "86.30.69.23", "URL", Config.BASE, false);
        addConfig(config);
        config = new Config(Configuration.UUID_ALGORITHM, "MD5", "Unique Identifier Alogrithm", Config.BASE, true);
        addConfig(config);
        config = new Config(Configuration.SSL_ALGORITHM, "", "Encryption Alogrithm", Config.BASE, true);
        addConfig(config);
        config = new Config(Configuration.PLAYER_KEY, "", "Unique Identifier", Config.BASE, true);
        addConfig(config);
        config = new Config(Configuration.PLAYER_NAME, "", "Player Name", Config.BASE, true);
        addConfig(config);
        config = new Config(Configuration.CURRENT_UTM_LAT, "", "Universal Transverse Mercator LatCode", Config.BASE, true);
        addConfig(config);
        config = new Config(Configuration.CURRENT_SUBUTM_LAT, "", "Custom SubUTM grid LatCode", Config.BASE, true);
        addConfig(config);
        config = new Config(Configuration.CURRENT_UTM_LONG, "", "Universal Transverse Mercator LongCode", Config.BASE, true);
        addConfig(config);
        config = new Config(Configuration.CURRENT_SUBUTM_LONG, "", "Custom SubUTM grid LongCode", Config.BASE, true);
        addConfig(config);
        config = new Config(Configuration.GPS_UPDATE_INTERVAL, String.valueOf(ProjectCheActivity.TWO_MINUTES), "GPS Update Interval", Config.USER, true);
        addConfig(config);
        config = new Config(Configuration.SERVER_LOCATION_HIDE, "N", "Hide Me", Config.USER, true);
        addConfig(config);
        config = new Config(Configuration.CLEAR_BACKLOG, "N", "Clear Message Backlog", Config.SYSTEM, true);
        addConfig(config);
        config = new Config(Configuration.RESET_SOCKET, "N", "Reset Connections", Config.SYSTEM, true);
        addConfig(config);

    }

    public byte[] addUserImage(UserImage userImage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(USER_IMAGE_KEY, userImage.getUserImageKey());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        userImage.getUserImage().compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] array = bos.toByteArray();
        values.put(USER_IMAGE, array);


        db.insert(IMAGE_TABLE, null, values);

        return array;


    }

    public void addMessage(Message message) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MESSAGE_KEY, message.getMessageKey());
        values.put(MESSAGE_CONTENT, message.getMessage());
        values.put(MESSAGE_TYPE, message.getMessageType());
        values.put(MESSAGE_TIME, message.getTime());
        values.put(MESSAGE_AUTHOR, message.getAuthor());
        values.put(MY_MESSAGE, message.isMyMessage() ? "Y" : "N");

        db.insert(MESSAGE_TABLE, null, values);

        if (messageHandler != null) {
            messageHandler.handleChat(message.getMessageType());
        }
    }

    public void addConfig(Config config) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CONFIG_NAME, config.getName());
        values.put(CONFIG_VALUE, config.getValue());
        values.put(CONFIG_MARKUP, config.getMarkup());
        values.put(CONFIG_TYPE, config.getType());
        values.put(CONFIG_VISIBLE, config.isVisible() ? "Y" : "N");


        db.insert(CONFIG_TABLE, null, values);

    }


    public void addAlliance(Alliance alliance, boolean invite) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ALLIANCE_KEY, alliance.getKey());
        values.put(ALLIANCE_NAME, alliance.getName());

        db.insert(ALLIANCES_TABLE, null, values);

        if (messageHandler != null) {
            if (invite) {
                messageHandler.handleInvite(alliance.getKey(), alliance.getName());
            } else {
                messageHandler.handleList();
            }
        }


    }

    /*
    public void addAllianceMember(AllianceMember allianceMember) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ALLIANCE_KEY, allianceMember.getAlliance().getKey());
        values.put(PLAYER_KEY, allianceMember.getKey());
        values.put(PLAYER_NAME, allianceMember.getName());
        values.put(LATITUDE, allianceMember.getLatitude());
        values.put(LONGITUDE, allianceMember.getLongitude());
        values.put(SPEED, allianceMember.getSpeed());
        values.put(ALTITUDE, allianceMember.getAltitude());
        values.put(UTM, allianceMember.getUtm());
        values.put(SUBUTM, allianceMember.getSubUtm());

        db.insert(ALLIANCE_MEMBERS_TABLE, null, values);

        if (messageHandler != null) {
            messageHandler.handleAllianceMember(allianceMember, true);
        }
    } */



    /*
    delete methods..we dont delete configs?
     */

    public void deleteMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MESSAGE_TABLE, MESSAGE_ID + " = ?", new String[]{String.valueOf(message.getId())});

    }

    public void deleteMessages(String key) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MESSAGE_TABLE, MESSAGE_KEY + " = ?", new String[]{key});

    }


    public void deleteAlliance(Alliance alliance) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ALLIANCES_TABLE, ALLIANCE_KEY + " = ?", new String[]{alliance.getKey()});
    }

    public void deleteAllianceMember() {

        SQLiteDatabase db = this.getWritableDatabase();
        //todo
    }


    /*
    update methods...no point updating a grid.  most will simply updte user dfined names etc.
     */

    public byte[] updateUserImage(UserImage userImage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        userImage.getUserImage().compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] array = bos.toByteArray();
        values.put(USER_IMAGE, array);


        db.update(IMAGE_TABLE, values, USER_IMAGE_KEY + "=?", new String[]{userImage.getUserImageKey()});

        return array;


    }

    public boolean updateConfig(Config config) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        boolean changed = false;

        Config oldValues = getConfig(config.getName());

        if (!oldValues.getValue().equals(config.getValue())) {
            changed = true;
        }


        values.put(CONFIG_VALUE, config.getValue());

        db.update(CONFIG_TABLE, values, CONFIG_ID + " = ?", new String[]{String.valueOf(config.getId())});


        return changed;
    }

    public void handleNewPlayer(String key) {
        if (messageHandler != null) {
            messageHandler.handlePlayerKey(key);
        }
    }


    public void handleUTMChange(boolean utmChanged, boolean subUtmChanged, String utm, String subUtm) {

        if (messageHandler != null) {
            if (utmChanged) {
                messageHandler.handleUTMChange(utm);
            }
            if (subUtmChanged) {
                messageHandler.handleSubUTMChange(subUtm);
            }
        }
    }


    public void updateAlliance(Alliance alliance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ALLIANCE_NAME, alliance.getName());

        db.update(ALLIANCES_TABLE, values, ALLIANCE_KEY + " = ?", new String[]{alliance.getKey()});

    }

    /*

    public void updateAllianceMember(AllianceMember allianceMember) {

        //this will come from grids...
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PLAYER_NAME, allianceMember.getName());
        values.put(LATITUDE, allianceMember.getLatitude());
        values.put(LONGITUDE, allianceMember.getLongitude());
        values.put(SPEED, allianceMember.getSpeed());
        values.put(ALTITUDE, allianceMember.getAltitude());
        values.put(UTM, allianceMember.getUtm());
        values.put(SUBUTM, allianceMember.getSubUtm());

        db.update(ALLIANCE_MEMBERS_TABLE, values, PLAYER_KEY + "=?", new String[]{allianceMember.getKey()});

        if (messageHandler != null) {
            messageHandler.handleAllianceMember(allianceMember, false);
        }

    }
*/



    /*
    we need to get our display lists
     */

    public Cursor getMessages(String messageType, String messageKey) {
        return this.getReadableDatabase().rawQuery("SELECT " + MESSAGE_ID + " as _id," + MESSAGE_ID + "," + MESSAGE_CONTENT + "," + MESSAGE_TIME + "," + MY_MESSAGE + "," + MESSAGE_AUTHOR + " FROM " + MESSAGE_TABLE + " WHERE " + MESSAGE_TYPE + "=? AND " + MESSAGE_KEY + "=? ORDER BY " + MESSAGE_TIME + " ASC", new String[]{messageType, messageKey});
    }

    public Cursor getConfigs() {
        return this.getReadableDatabase().rawQuery("SELECT " + CONFIG_ID + " as _id," + CONFIG_ID + "," + CONFIG_NAME + "," + CONFIG_VALUE + "," + CONFIG_MARKUP + "," + CONFIG_VISIBLE + "," + CONFIG_TYPE + " FROM " + CONFIG_TABLE + " ORDER BY " + CONFIG_NAME + " ASC", null);
    }

    public Cursor getUserImages() {
        return this.getReadableDatabase().rawQuery("SELECT " + USER_IMAGE_KEY + " as _id," + USER_IMAGE_KEY + "," + USER_IMAGE + " FROM " + USER_IMAGE + " ORDER BY " + USER_IMAGE_KEY + " ASC", null);

    }

    public Cursor getConfigs(int type) {
        return this.getReadableDatabase().rawQuery("SELECT " + CONFIG_ID + " as _id," + CONFIG_ID + "," + CONFIG_NAME + "," + CONFIG_VALUE + "," + CONFIG_MARKUP + "," + CONFIG_VISIBLE + "," + CONFIG_TYPE + " FROM " + CONFIG_TABLE + " WHERE " + CONFIG_TYPE + " =? AND " + CONFIG_VISIBLE + " = 'Y' ORDER BY " + CONFIG_NAME + " ASC", new String[]{String.valueOf(type)});
    }


    public Cursor getAlliances() {
        return this.getReadableDatabase().rawQuery("SELECT " + ALLIANCE_KEY + " as _id," + ALLIANCE_KEY + "," + ALLIANCE_NAME + " FROM " + ALLIANCES_TABLE + " ORDER BY " + ALLIANCE_NAME + " ASC", null);
    }


    public Cursor getAllianceMembers() {
        return this.getReadableDatabase().rawQuery("SELECT " + PLAYER_KEY + " as _id," + PLAYER_KEY + "," + PLAYER_NAME + "," + SPEED + "," + ALTITUDE + "," + LATITUDE + "," + LONGITUDE + "," + UTM + "," + SUBUTM + " FROM " + ALLIANCE_MEMBERS_TABLE, null);
    }

    public Cursor getAllianceMembers(String allianceKey) {
        return this.getReadableDatabase().rawQuery("SELECT " + PLAYER_KEY + " as _id," + PLAYER_KEY + "," + PLAYER_NAME + "," + SPEED + "," + ALTITUDE + "," + LATITUDE + "," + LONGITUDE + "," + UTM + "," + SUBUTM + " FROM " + ALLIANCE_MEMBERS_TABLE + " WHERE " + ALLIANCE_KEY + " =?", new String[]{allianceKey});
    }


    /*
    get specific object.  using key.  probably to check it actually exists etc before writing a new
    entry etc in the database table.....unlikely to require other than to check something exists.
     */

    public Alliance getAlliance(String key) {

        Cursor alliance = this.getReadableDatabase().rawQuery("SELECT " + ALLIANCE_KEY + " as _id," + ALLIANCE_KEY + "," + ALLIANCE_NAME + " FROM " + ALLIANCES_TABLE + " WHERE " + ALLIANCE_KEY + " =? " + " ORDER BY " + ALLIANCE_NAME + " ASC", new String[]{key});

        Alliance returnAlliance = null;

        while (alliance.moveToNext()) {

            returnAlliance = new Alliance(alliance);

        }

        alliance.close();

        return returnAlliance;

    }


    public UserImage getUserImage(String key) {

        Cursor userImage = this.getReadableDatabase().rawQuery("SELECT " + USER_IMAGE_KEY + " as _id," + USER_IMAGE_KEY + "," + USER_IMAGE + " FROM " + IMAGE_TABLE + " WHERE " + USER_IMAGE_KEY + " = ?" + " ORDER BY " + USER_IMAGE_KEY + " ASC", new String[]{key});

        UserImage returnUserImage = null;

        while (userImage.moveToNext()) {
            returnUserImage = new UserImage(userImage);
        }
        userImage.close();

        return returnUserImage;
    }

    public Config getConfig(String key) {

        Cursor config = this.getReadableDatabase().rawQuery("SELECT " + CONFIG_ID + " as _id," + CONFIG_ID + "," + CONFIG_NAME + "," + CONFIG_VALUE + "," + CONFIG_MARKUP + "," + CONFIG_TYPE + "," + CONFIG_VISIBLE + " FROM " + CONFIG_TABLE + " WHERE " + CONFIG_NAME + " = ?" + " ORDER BY " + CONFIG_NAME + " ASC", new String[]{key});

        Config returnConfig = null;

        while (config.moveToNext()) {
            returnConfig = new Config(config);
        }
        config.close();

        return returnConfig;
    }


    //on shut down.
    public void close() {
        this.getWritableDatabase().close();
    }
}


