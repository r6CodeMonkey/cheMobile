package mobile.che.com.oddymobstar.chemobile.activity.listener;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;

import com.google.android.gms.maps.model.LatLng;

import java.security.NoSuchAlgorithmException;

import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.model.Message;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;


/**
 * Created by timmytime on 03/12/15.
 */
public class LocationListener implements android.location.LocationListener {


    private final ProjectCheController controller;
    private Location currentLocation;

    public LocationListener(ProjectCheController controller) {
        this.controller = controller;
    }


    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public LatLng getCurrentLocationLatLng() {

        if(currentLocation != null) {
            return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }else{
            Config currentLat = controller.dbHelper.getConfig(Configuration.CURRENT_LATITUTDE);
            Config currentLong = controller.dbHelper.getConfig(Configuration.CURRENT_LONGITUDE);

            return new LatLng(Double.parseDouble(currentLat.getValue()), Double.parseDouble(currentLong.getValue()));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //this is our only location listener now...
        currentLocation = location;

        if(controller != null) {
            Config currentLat = controller.dbHelper.getConfig(Configuration.CURRENT_LATITUTDE);
            Config currentLong = controller.dbHelper.getConfig(Configuration.CURRENT_LONGITUDE);

            currentLat.setValue(String.valueOf(location.getLatitude()));
            currentLong.setValue(String.valueOf(location.getLongitude()));

            controller.dbHelper.updateConfig(currentLat);
            controller.dbHelper.updateConfig(currentLong);

            Message message = new Message();
            message.setMessage("this is a test on location change and now we force a line break");
            message.setTime(System.currentTimeMillis());
            controller.dbHelper.addVidiNews(message);

            if (controller.progressDialog != null) {
                controller.progressDialog.dismiss();

                //its enough for time being.
                if (controller.runNewUserAnimation) {
                    controller.runNewUserAnimation = false;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            controller.materialsHelper.navDrawer.openDrawer(Gravity.LEFT);
                        }
                    }, 1500);

                }
            }

            Log.d("location changed", "location changed");
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        controller.cheService.writeToSocket(controller.messageFactory.locationChangedMessage(currentLocation), currentLocation);
                    } catch (NoSuchAlgorithmException e) {
                        Log.d("security exception", "security exception " + e.toString());
                    }
                }
            }).start();


            if (controller.mapHandler.getMarkerMap().containsKey("Me")) {
                controller.mapHandler.getMarkerMap().get("Me").remove();
            }

            controller.mapHandler.addUser(currentLatLng);

            controller.mapHandler.handleCamera(currentLatLng,
                    controller.mapHelper.getMap().getCameraPosition().tilt,
                    controller.mapHelper.getMap().getCameraPosition().bearing,
                    controller.mapHelper.getMap().getCameraPosition().zoom);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        controller.locationManager.removeUpdates(this);
    }

    @Override
    public void onProviderEnabled(String provider) {
        controller.locationManager.requestLocationUpdates(provider, Long.parseLong(controller.configuration.getConfig(Configuration.GPS_UPDATE_INTERVAL).getValue()), 0, this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}


