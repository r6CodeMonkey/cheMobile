package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.os.Handler;

import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;


/**
 * Created by timmytime on 06/12/15.
 */

public class ConfigurationHandler extends Handler {

    private final ProjectCheController controller;

    public ConfigurationHandler(ProjectCheController controller) {
        this.controller = controller;
    }

    public void handleClearBacklog() {
        //calls the service to handle.  add service method
        controller.cheService.clearBacklog();

    }


    public void handleResetConnection() {
        controller.cheService.resetConnection();
    }

    public void handleGPSInterval(int progress) {
        //
        Config config = controller.configuration.getConfig(Configuration.GPS_UPDATE_INTERVAL);
        config.setValue(String.valueOf(progress * 1000));

        controller.dbHelper.updateConfig(config);

        controller.configuration = new Configuration(controller.dbHelper.getConfigs());

        if (controller.locationHelper.getLocationUpdates().isAlive()) {
            controller.locationHelper.getLocationUpdates().interrupt();
            controller.locationHelper.killLocationUpdates();
        }

        controller.mapHelper.initLocationUpdates();
        controller.cheService.resetLocationUpdates();
    }

    public void handleHideUser(boolean hide) {
        //updates config.
        Config config = controller.configuration.getConfig(Configuration.SERVER_LOCATION_HIDE);
        config.setValue(hide ? "Y" : "N");

        controller.dbHelper.updateConfig(config);

        controller.configuration = new Configuration(controller.dbHelper.getConfigs());
    }

}


