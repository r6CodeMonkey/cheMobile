package mobile.che.com.oddymobstar.chemobile.util;

import android.database.Cursor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.Config;

/**
 * Created by root on 27/02/15.
 */
public class Configuration implements Serializable {

    /*
    we hold the static configs here.  we hold them in database as there values can change
     */
    public final static String PORT = "PORT";
    public final static String URL = "URL";

    public final static String PLAYER_KEY = "PLAYER_KEY";
    public final static String PLAYER_NAME = "PLAYER_NAME";


    public final static String UUID_ALGORITHM = "UUID_ALGO";
    public final static String SSL_ALGORITHM = "SSL_ALGO";

    public final static String CURRENT_UTM_LONG = "CURRENT_UTM_LONG";
    public final static String CURRENT_SUBUTM_LONG = "CURRENT_SUBUTM_LONG";
    public final static String CURRENT_UTM_LAT = "CURRENT_UTM_LAT";
    public final static String CURRENT_SUBUTM_LAT = "CURRENT_SUBUTM_LAT";

    //need to actuall glamourize this so its in minutes, not milliseconds.
    public final static String GPS_UPDATE_INTERVAL = "GPS_UPDATE_INTERVAL";

    //technically we do not need these in the database...but we may add them.
    //also need mappers to control the values....some are checkboxes.  but we need to store settings
    public final static String SERVER_LOCATION_HIDE = "LOCATION_HIDE";

    public final static String RESET_SOCKET = "RESET_SOCKET";
    public final static String CLEAR_BACKLOG = "CLEAR_BACKLOG";


    //Need last server contact.   public final static String CURRENT_SUBUTM = "CURRENT_SUBUTM";
    /*
      we will also need other configs, ie security types...enough to get going tho.
     */
    private Map<String, Config> configs = new HashMap<String, Config>();

    public Configuration(Cursor cursor) {

        /*
          load whatever configs we have.  we need to add in some base ones on the create method.
         */
        while (cursor.moveToNext()) {
            Config config = new Config(cursor);
            configs.put(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_NAME)), config);
        }

        cursor.close();


    }

    public Map<String, Config> getConfigs() {
        return configs;
    }

    public Config getConfig(String key) {
        return configs.get(key);
    }
}
