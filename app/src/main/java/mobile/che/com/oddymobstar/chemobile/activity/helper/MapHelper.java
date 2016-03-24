package mobile.che.com.oddymobstar.chemobile.activity.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.activity.handler.SharedPreferencesHandler;
import mobile.che.com.oddymobstar.chemobile.activity.listener.LocationListener;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.map.UTMGridCreator;
import mobile.che.com.oddymobstar.chemobile.util.widget.GridDialog;
import util.map.SubUTM;
import util.map.UTM;


/**
 * Created by timmytime on 03/12/15.
 */
public class MapHelper {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;
    public float bearing, tilt, zoom = 0.0f;
    private GoogleMap map; // Might be null if Google Play services APK is not available.

    private Polygon myUTM;
    private Polygon mySubUTM;

    private PolygonOptions utmOptions;


    public MapHelper(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }


    public void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) main.getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();


            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }

        }
    }


    private void setUpMap() {
        map.getUiSettings().setZoomControlsEnabled(false);
        //now dd our last known location.
        controller.locationListener = new LocationListener(main, controller);
        controller.locationHelper.initLocationUpdates();

        SharedPreferences sharedPreferences = main.getPreferences(Context.MODE_PRIVATE);


        zoom = sharedPreferences.getFloat(SharedPreferencesHandler.ZOOM, 10.0f);
        tilt = sharedPreferences.getFloat(SharedPreferencesHandler.TILT, 0.0f);
        bearing = sharedPreferences.getFloat(SharedPreferencesHandler.BEARING, 0.0f);

        Config currentLat = controller.dbHelper.getConfig(Configuration.CURRENT_LATITUTDE);
        Config currentLong = controller.dbHelper.getConfig(Configuration.CURRENT_LONGITUDE);


        LatLng currentLatLng = new LatLng(Double.parseDouble(currentLat.getValue()),
                Double.parseDouble(currentLong.getValue()));

        //need to manage map markers too.  as per old code ie remove and re add.  do this now....joy
        controller.mapHandler.addUser(currentLatLng);
        controller.mapHandler.handleCamera(currentLatLng, tilt, bearing, zoom);

        if (myUTM != null) {
            myUTM.remove();
        }

        if (mySubUTM != null) {
            mySubUTM.remove();
        }

        if (!controller.configuration.getConfig(Configuration.CURRENT_UTM_LAT).getValue().trim().isEmpty()) {
            utmOptions = UTMGridCreator.getUTMGrid(new UTM(controller.configuration.getConfig(Configuration.CURRENT_UTM_LAT).getValue(), controller.configuration.getConfig(Configuration.CURRENT_UTM_LONG).getValue())).strokeColor(main.getResources().getColor(android.R.color.holo_purple));
            myUTM = map.addPolygon(utmOptions);

            PolygonOptions subUtmOptions = UTMGridCreator.getSubUTMGrid(new SubUTM(controller.configuration.getConfig(Configuration.CURRENT_SUBUTM_LAT).getValue(), controller.configuration.getConfig(Configuration.CURRENT_SUBUTM_LONG).getValue()), utmOptions).strokeColor(main.getResources().getColor(android.R.color.holo_orange_dark));
            mySubUTM = map.addPolygon(subUtmOptions);

        }
        controller.mapHandler.addOthers();
        addGameMarkers();
        addPaths();
        addTargets();

        map.setOnMarkerClickListener(controller.mapListener.getMarkerListener());
        map.setOnMapLongClickListener(controller.gameController.gameListener.getGameLongClickListener());
    }


    public GoogleMap getMap() {
        return map;
    }

    public void initLocationUpdates() {

        controller.progressDialog = new ProgressDialog(main);
        controller.progressDialog.setMessage("Locating...");
        controller.progressDialog.setIndeterminate(true);
        controller.progressDialog.show();

        controller.locationHelper.initLocationUpdates();
    }

    public PolygonOptions getUtmOptions() {
        return utmOptions;
    }

    public Polygon getMyUTM() {
        return myUTM;
    }

    public void setMyUTM(Polygon myUTM) {
        this.myUTM = myUTM;
    }

    public Polygon getMySubUTM() {
        return mySubUTM;
    }

    public void setMySubUTM(Polygon mySubUTM) {
        this.mySubUTM = mySubUTM;
    }

    public GridDialog createGridDialog(String selectedGrid) {
        controller.gridDialog =
                GridDialog.newInstance(selectedGrid, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        //the magic happens...but we cant deselect our selected item its not the pattern..
                        dialog.dismiss();
                        controller.mapHandler.handleLocateDialog(controller.gridDialog.getGrid(which));

                    }
                }, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        controller.materialsHelper.floatingActionButton.setVisibility(View.VISIBLE);
                    }
                });

        return controller.gridDialog;
    }

    private void addGameMarkers() {

        Cursor cursor = controller.dbHelper.getAddedGameObjects();

        while (cursor.moveToNext()) {
            controller.mapHandler.addGameObject(new GameObject(cursor), false);
        }
        cursor.close();

    }

    private void addPaths() {
        Cursor cursor = controller.dbHelper.getMovingGameObjects();

        while (cursor.moveToNext()) {
            controller.mapHandler.addPath(new GameObject(cursor));
        }
        cursor.close();
    }

    private void addTargets(){
        Cursor cursor = controller.dbHelper.getTargets();

        while(cursor.moveToNext()){

            GameObject gameObject = new GameObject(cursor);
            controller.mapHandler.addSphere(gameObject, gameObject.getImpactRadius(), true);
        }
    }

}
