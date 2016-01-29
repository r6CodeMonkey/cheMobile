package mobile.che.com.oddymobstar.chemobile.activity.listener;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
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


    @Override
    public void onLocationChanged(Location location) {

        currentLocation = location;

        Log.d("location changed", "location changed");
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        if (controller.mapHandler.getMarkerMap().containsKey("Me")) {
            controller.mapHandler.getMarkerMap().get("Me").remove();
        }

        controller.mapHandler.addUser(currentLatLng);

        controller.mapHandler.handleCamera(currentLatLng,
                controller.mapHelper.getMap().getCameraPosition().tilt,
                controller.mapHelper.getMap().getCameraPosition().bearing,
                controller.mapHelper.getMap().getCameraPosition().zoom);
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


