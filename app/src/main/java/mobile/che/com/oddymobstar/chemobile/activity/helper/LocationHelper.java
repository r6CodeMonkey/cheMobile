package mobile.che.com.oddymobstar.chemobile.activity.helper;

import android.location.LocationManager;

import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;


/**
 * Created by timmytime on 03/12/15.
 */
public class LocationHelper {

    private final ProjectCheController controller;
    private android.os.Handler handler = new android.os.Handler();
    private Thread locationUpdates;

    public LocationHelper(ProjectCheController controller) {
        this.controller = controller;
    }

    public void initLocationUpdates() {


        locationUpdates = new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        controller.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Long.parseLong(controller.configuration.getConfig(Configuration.GPS_UPDATE_INTERVAL).getValue()), 0, controller.locationListener);
                    }
                });

            }
        });
        locationUpdates.start();
    }

    public Thread getLocationUpdates() {
        return locationUpdates;
    }

    public void killLocationUpdates() {
        locationUpdates = null;
    }

}
