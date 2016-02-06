package mobile.che.com.oddymobstar.chemobile.service.handler;

import android.util.Log;

import org.json.JSONException;

import message.CheMessage;
import message.UTMLocation;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import util.Tags;

/**
 * Created by timmytime on 30/01/16.
 */
public class GridHandler extends MessageHandler{


    public GridHandler(DBHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public void handle(CheMessage cheMessage) throws JSONException {

        UTMLocation utmLocation = (UTMLocation)cheMessage.getMessage(Tags.UTM_LOCATION);

        boolean utmLatChanged, utmLongChanged = false;
        boolean subUtmLatChanged, subUtmLongChanged = false;

        Config utmLat = dbHelper.getConfig(Configuration.CURRENT_UTM_LAT);
        utmLat.setValue(utmLocation.getUTM().getUTMLatGrid());
        Log.d("utm", utmLocation.getUTM().getUTMLatGrid());

        Config utmLong = dbHelper.getConfig(Configuration.CURRENT_UTM_LONG);
        utmLong.setValue(utmLocation.getUTM().getUTMLongGrid());
        Log.d("utm", utmLocation.getUTM().getUTMLongGrid());


        utmLongChanged = dbHelper.updateConfig(utmLong);
        utmLatChanged = dbHelper.updateConfig(utmLat);

        Config subUtmLat = dbHelper.getConfig(Configuration.CURRENT_SUBUTM_LAT);
        utmLat.setValue(utmLocation.getSubUTM().getUTMLatGrid());
        Log.d("utm", utmLocation.getSubUTM().getUTMLatGrid());

        Config subUtmLong = dbHelper.getConfig(Configuration.CURRENT_SUBUTM_LONG);
        utmLong.setValue(utmLocation.getSubUTM().getUTMLongGrid());
        Log.d("utm", utmLocation.getSubUTM().getUTMLongGrid());

        subUtmLatChanged =  dbHelper.updateConfig(subUtmLat);
        subUtmLongChanged = dbHelper.updateConfig(subUtmLong);

        dbHelper.handleUTMChange(utmLongChanged||utmLatChanged, subUtmLatChanged||subUtmLongChanged,
                utmLocation.getUTM().getUTMLatGrid()+utmLocation.getUTM().getUTMLongGrid(),
                utmLocation.getSubUTM().getUTMLatGrid()+utmLocation.getSubUTM().getUTMLongGrid() );

    }
}
