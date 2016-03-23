package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.content.SharedPreferences;

import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;


/**
 * Created by timmytime on 06/12/15.
 */
public class SharedPreferencesHandler {

    public static final String ZOOM = "zoom";
    public static final String BEARING = "bearing";
    public static final String TILT = "tilt";
    public static final String PROVIDER = "provider";


    public static void handle(SharedPreferences sharedPreferences, ProjectCheController controller) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat(ZOOM, controller.mapHelper.getMap().getCameraPosition().zoom);
        editor.putFloat(BEARING, controller.mapHelper.getMap().getCameraPosition().bearing);
        editor.putFloat(TILT, controller.mapHelper.getMap().getCameraPosition().tilt);
        editor.putString(PROVIDER, controller.locationListener.getCurrentLocation().getProvider());

        editor.commit();
    }
}
