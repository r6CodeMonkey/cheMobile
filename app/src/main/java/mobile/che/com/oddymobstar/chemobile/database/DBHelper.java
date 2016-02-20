package mobile.che.com.oddymobstar.chemobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.handler.MessageHandler;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Alliance;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.model.Message;
import mobile.che.com.oddymobstar.chemobile.model.UserImage;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import util.GameObjectTypes;
import util.Tags;

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
    public static final String GAME_OBJECTS_TABLE = "GAME_OBJECTS";
    public static final String MISSILES_BY_GAME_OBJECT = "MISSILES_BY_GAME_OBJECT";
    public static final String VIDIPRINT_TABLE = "VIDIPRINT_TABLE";

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
    public static final String GAME_OBJECT_KEY = "game_object_key"; //testing hardcode, ultimately they come from server.
    public static final String GAME_OBJECT_TYPE = "game_object_type";
    public static final String GAME_OBJECT_SUBTYPE = "game_object_subtype";
    public static final String GAME_OBJECT_LAT = "game_object_lat";
    public static final String GAME_OBJECT_LONG = "game_object_long";
    public static final String GAME_OBJECT_UTM_LAT = "game_object_utm_lat";
    public static final String GAME_OBJECT_UTM_LONG = "game_object_utm_long";
    public static final String GAME_OBJECT_SUBUTM_LAT = "game_object_subutm_lat";
    public static final String GAME_OBJECT_SUBUTM_LONG = "game_object_subutm_long";
    public static final String GAME_OBJECT_STATUS = "game_object_status";
    public static final String GAME_OBJECT_DEST_LAT = "game_object_dest_lat";
    public static final String GAME_OBJECT_DEST_LONG = "game_object_dest_long";

    public static final String MISSILE_KEY = "missile_key";


    //going to also need a player points table, well on server too...not now.


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PROJECTCHE";
    //table creates
    private static final String CREATE_CONFIG = "CREATE TABLE " + CONFIG_TABLE + " (" + CONFIG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + CONFIG_NAME + " VARCHAR2(30)," + CONFIG_VALUE + " VARCHAR2(30), " + CONFIG_MARKUP + " VARCHAR2(50)," + CONFIG_TYPE + " CHAR(1), " + CONFIG_VISIBLE + " CHAR(1))";
    private static final String CREATE_ALLIANCES = "CREATE TABLE " + ALLIANCES_TABLE + " (" + ALLIANCE_KEY + " VARCHAR2(200) UNIQUE NOT NULL," + ALLIANCE_NAME + " VARCHAR2(30))";
    private static final String CREATE_ALLIANCE_MEMBERS = "CREATE TABLE " + ALLIANCE_MEMBERS_TABLE + " (" + ALLIANCE_KEY + " VARCHAR2(200)," + PLAYER_KEY + " VARCHAR2(200)," + PLAYER_NAME + " VARCHAR2(30)," + LATITUDE + " NUMBER, " + LONGITUDE + " NUMBER, " + UTM + " VARCHAR2(10)," + SUBUTM + " VARCHAR2(10)," + SPEED + " NUMBER," + ALTITUDE + " NUMBER)";
    private static final String CREATE_MESSAGES = "CREATE TABLE " + MESSAGE_TABLE + "(" + MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + MESSAGE_CONTENT + " VARCHAR2(300), " + MESSAGE_KEY + " VARCHAR2(200)," + MESSAGE_TYPE + " CHAR(1), " + MESSAGE_TIME + " INTEGER," + MY_MESSAGE + " CHAR(1)," + MESSAGE_AUTHOR + " VARCHAR2(200) )";
    private static final String CREATE_USER_IMAGES = "CREATE TABLE " + IMAGE_TABLE + "(" + USER_IMAGE_KEY + " VARCHAR2(200)," + USER_IMAGE + " BLOB)";
    private static final String CREATE_GAME_OBJECTS = "CREATE TABLE " + GAME_OBJECTS_TABLE + "(" + GAME_OBJECT_KEY + " VARCHAR2(200) UNIQUE NOT NULL," + GAME_OBJECT_TYPE + " INTEGER, " + GAME_OBJECT_SUBTYPE + " INTEGER," + GAME_OBJECT_LAT + " NUMBER, " + GAME_OBJECT_LONG + " NUMBER, " + GAME_OBJECT_DEST_LAT + " NUMBER," + GAME_OBJECT_DEST_LONG + " NUMBER," + GAME_OBJECT_UTM_LAT + " VARCHAR2(10), " + GAME_OBJECT_UTM_LONG + " VARCHAR2(10), " + GAME_OBJECT_SUBUTM_LAT + " VARCHAR2(10), " + GAME_OBJECT_SUBUTM_LONG + " VARCHAR2(10), " + GAME_OBJECT_STATUS + " VARCHAR2(20))";
    private static final String CREATE_MISSILES_BY_GAME_OBJECT = "CREATE TABLE " + MISSILES_BY_GAME_OBJECT + "( " + GAME_OBJECT_KEY + " VARCHAR2(200), " + MISSILE_KEY + " VARCHAR2(200))";
    private static final String CREATE_VIDIPRINT_TABLE = "CREATE TABLE " + VIDIPRINT_TABLE + "(" + MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + MESSAGE_CONTENT + " VARCHAR2(300), " + MESSAGE_TIME + " INTEGER)";

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
        db.execSQL(CREATE_GAME_OBJECTS);
        db.execSQL(CREATE_MISSILES_BY_GAME_OBJECT);
        db.execSQL(CREATE_VIDIPRINT_TABLE);


    }

    /*
    to update without updating.
     */
    public void developStub() {
      /*  this.getWritableDatabase().execSQL("DROP TABLE " + GAME_OBJECTS_TABLE);
        this.getWritableDatabase().execSQL("DROP TABLE " + MISSILES_BY_GAME_OBJECT);
        this.getWritableDatabase().execSQL("DROP TABLE " + ALLIANCE_MEMBERS_TABLE);
        this.getWritableDatabase().execSQL("DROP TABLE " + ALLIANCES_TABLE);
        this.getWritableDatabase().execSQL("DROP TABLE " + CONFIG_TABLE);
        this.getWritableDatabase().execSQL("DROP TABLE " + MESSAGE_TABLE); */
        this.getWritableDatabase().execSQL(CREATE_VIDIPRINT_TABLE);
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
        config = new Config(Configuration.START_PURCHASE_INFA, "N", "Start Purchase Infrastructure", Config.SYSTEM, false);
        addConfig(config);
        config = new Config(Configuration.START_PURCHASE_LAND, "N", "Start Purchase Land", Config.SYSTEM, false);
        addConfig(config);
        config = new Config(Configuration.START_PURCHASE_AIR, "N", "Start Purchase Air", Config.SYSTEM, false);
        addConfig(config);
        config = new Config(Configuration.START_PURCHASE_MISSILE, "N", "Start Purchase Missiles", Config.SYSTEM, false);
        addConfig(config);
        config = new Config(Configuration.START_PURCHASE_SEA, "N", "Start Purchase Sea", Config.SYSTEM, false);
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

    public void addMissileToGameObject(String gameObjectKey, String missileKey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GAME_OBJECT_KEY, gameObjectKey);
        values.put(MISSILE_KEY, missileKey);

        db.insert(MISSILES_BY_GAME_OBJECT, null, values);

    }

    public void addVidiNews(Message message) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MESSAGE_CONTENT, message.getMessage());
        values.put(MESSAGE_TIME, message.getTime());

        db.insert(VIDIPRINT_TABLE, null, values);

        if (messageHandler != null) {
            messageHandler.handleVidiNews();
        }

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
                messageHandler.handleAlliance();
            }
        }


    }

    public void addGameObject(GameObject gameObject) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GAME_OBJECT_KEY, gameObject.getKey());
        values.put(GAME_OBJECT_TYPE, gameObject.getType());
        values.put(GAME_OBJECT_SUBTYPE, gameObject.getSubType());
        values.put(GAME_OBJECT_LAT, gameObject.getLatitude());
        values.put(GAME_OBJECT_LONG, gameObject.getLongitude());
        values.put(GAME_OBJECT_UTM_LAT, gameObject.getUtmLat());
        values.put(GAME_OBJECT_UTM_LONG, gameObject.getUtmLong());
        values.put(GAME_OBJECT_SUBUTM_LAT, gameObject.getSubUtmLat());
        values.put(GAME_OBJECT_SUBUTM_LONG, gameObject.getSubUtmLong());
        values.put(GAME_OBJECT_STATUS, gameObject.getStatus());
        values.put(GAME_OBJECT_DEST_LAT, gameObject.getDestLatitude());
        values.put(GAME_OBJECT_DEST_LONG, gameObject.getDestLongitude());

        db.insert(GAME_OBJECTS_TABLE, null, values);


        if (messageHandler != null) {
            messageHandler.handleGameObject();
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

    public void deleteVidiNews(long time) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(VIDIPRINT_TABLE, MESSAGE_TIME + " < ?", new String[]{String.valueOf(time)});

    }

    public void deleteMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MESSAGE_TABLE, MESSAGE_ID + " = ?", new String[]{String.valueOf(message.getId())});

    }

    public void deleteMissileFromGameObject(String gameObjectKey, String missileKey) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MISSILES_BY_GAME_OBJECT, GAME_OBJECT_KEY + " = ? AND " + MISSILE_KEY + " =?", new String[]{gameObjectKey, missileKey});

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

    public void deleteGameObject(GameObject gameObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(GAME_OBJECTS_TABLE, GAME_OBJECT_KEY + " = ?", new String[]{gameObject.getKey()});

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

    public void updateAlliance(Alliance alliance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ALLIANCE_NAME, alliance.getName());

        db.update(ALLIANCES_TABLE, values, ALLIANCE_KEY + " = ?", new String[]{alliance.getKey()});

    }


    public void updateGameObject(GameObject gameObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GAME_OBJECT_LAT, gameObject.getLatitude());
        values.put(GAME_OBJECT_LONG, gameObject.getLongitude());
        values.put(GAME_OBJECT_UTM_LAT, gameObject.getUtmLat());
        values.put(GAME_OBJECT_UTM_LONG, gameObject.getUtmLong());
        values.put(GAME_OBJECT_SUBUTM_LAT, gameObject.getSubUtmLat());
        values.put(GAME_OBJECT_SUBUTM_LONG, gameObject.getSubUtmLong());
        values.put(GAME_OBJECT_STATUS, gameObject.getStatus());
        values.put(GAME_OBJECT_DEST_LAT, gameObject.getDestLatitude());
        values.put(GAME_OBJECT_DEST_LONG, gameObject.getDestLongitude());


        db.update(GAME_OBJECTS_TABLE, values, GAME_OBJECT_KEY + " = ?", new String[]{gameObject.getKey()});
        if (gameObject.getType() != GameObjectGridFragment.MISSILE) {  //we dont add missiles at this point.
            handleGameObjectAdded(gameObject);
        }

    }

    public void handleGameObjectAdded(GameObject gameObject) {
        if (messageHandler != null) {
            if (gameObject.getStatus().equals(Tags.GAME_OBJECT_IS_FIXED) || gameObject.getStatus().trim().isEmpty()) {
                messageHandler.addGameObject(gameObject);
            } else if (gameObject.getStatus().equals(Tags.GAME_OBJECT_IS_MOVING)) {
                messageHandler.moveGameObject(gameObject);
            }
        }
    }


    public void handleNewPlayer(String key) {
        Log.d("handle new player", "new player");
        if (messageHandler != null) {
            Log.d("handle new player", "new player not null handler");
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

    /*
     game objects need to be grouped by type...so distinct sub type, + count number of to display each individual
     */
    public Cursor getGameObjects(int type, int subType) {
        return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + "(SELECT COUNT(1) FROM " + MISSILES_BY_GAME_OBJECT + " WHERE " + GAME_OBJECT_KEY + " = " + GAME_OBJECTS_TABLE + "." + GAME_OBJECT_KEY + ") as explosives_count," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + "=? AND " + GAME_OBJECT_SUBTYPE + "=? ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", new String[]{String.valueOf(type), String.valueOf(subType)});
    }

    public Cursor getAddedGameObjects() {
        return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_UTM_LAT + " IS NOT NULL AND " + GAME_OBJECT_TYPE + " != " + GameObjectGridFragment.MISSILE + " AND " + GAME_OBJECT_STATUS + "='" + Tags.GAME_OBJECT_IS_FIXED + "' ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
    }

    public Cursor getMovingGameObjects() {
        return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_UTM_LAT + " IS NOT NULL AND " + GAME_OBJECT_TYPE + " != " + GameObjectGridFragment.MISSILE + " AND " + GAME_OBJECT_STATUS + "='" + Tags.GAME_OBJECT_IS_MOVING + "' ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
    }

    public Cursor getAvailableObjectsToArm(int type) {

        switch (type) {
            case GameObjectTypes.G2G:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.LAND + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.G2A:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.LAND + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.G2W:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.LAND + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.A2A:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.AIR + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.A2G:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.AIR + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.A2W:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.AIR + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.W2A:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.SEA + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.W2G:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.SEA + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.W2W:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.SEA + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.WATER_MINE:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.SEA + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            case GameObjectTypes.GROUND_MINE:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.LAND + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
            default:
                return this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " IN (" + GameObjectGridFragment.LAND + "," + GameObjectGridFragment.AIR + "," + GameObjectGridFragment.SEA + ") AND TRIM(" + GAME_OBJECT_UTM_LAT + ") IS NOT NULL ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", null);
        }

    }

    public Cursor getGameObjectTypes(int type) {
        return this.getReadableDatabase().rawQuery("SELECT DISTINCT types." + GAME_OBJECT_SUBTYPE + " as _id, types." + GAME_OBJECT_SUBTYPE + ", (SELECT COUNT(" + GAME_OBJECT_SUBTYPE + ") FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_TYPE + " = ? AND " + GAME_OBJECT_SUBTYPE + " = types." + GAME_OBJECT_SUBTYPE + ") as type_total FROM " + GAME_OBJECTS_TABLE + " types WHERE " + GAME_OBJECT_TYPE + "=? ORDER BY types." + GAME_OBJECT_SUBTYPE + " ASC", new String[]{String.valueOf(type), String.valueOf(type)});
    }

    public Cursor getVidiNews() {
        return this.getReadableDatabase().rawQuery("SELECT " + MESSAGE_ID + " as _id, " + MESSAGE_CONTENT + " FROM " + VIDIPRINT_TABLE + " ORDER BY " + MESSAGE_TIME + " ASC", null);
    }

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

    public GameObject getGameObject(String key) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT " + GAME_OBJECT_KEY + " as _id," + GAME_OBJECT_KEY + "," + GAME_OBJECT_STATUS + "," + GAME_OBJECT_TYPE + "," + GAME_OBJECT_SUBTYPE + "," + GAME_OBJECT_LAT + "," + GAME_OBJECT_LONG + "," + GAME_OBJECT_DEST_LAT + "," + GAME_OBJECT_DEST_LONG + "," + GAME_OBJECT_UTM_LAT + "," + GAME_OBJECT_UTM_LONG + "," + GAME_OBJECT_SUBUTM_LAT + "," + GAME_OBJECT_SUBUTM_LONG + " FROM " + GAME_OBJECTS_TABLE + " WHERE " + GAME_OBJECT_KEY + "=? ORDER BY " + GAME_OBJECT_SUBTYPE + " ASC", new String[]{key});
        ;
        GameObject gameObject = null;

        while (cursor.moveToNext()) {
            gameObject = new GameObject(cursor);
        }
        cursor.close();

        return gameObject;
    }


    //on shut down.
    public void close() {
        this.getWritableDatabase().close();
    }
}


