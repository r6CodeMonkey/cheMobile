package mobile.che.com.oddymobstar.chemobile.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.util.map.SubUTM;
import mobile.che.com.oddymobstar.chemobile.util.map.UTM;

public class ProjectCheActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    public static final Long TWO_MINUTES = 120000l;
    private static Typeface font = null;
    public String googleAccountName = "";
    private ProjectCheController controller = new ProjectCheController(this);
    //will get removed.. lots of work to do.
    private GoogleMap mMap;

    public static Typeface getFont() {
        return font;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        AccountManager manager = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
        for (Account account : manager.getAccounts()) {
            if (account.type.equalsIgnoreCase("com.google")) {
                try {
                    googleAccountName = account.name.split("@")[0];
                } catch (Exception e) {
                    googleAccountName = account.name;
                }
            }
        }


        font = Typeface.createFromAsset(
                this.getAssets(), "fontawesome-webfont.ttf");


        UTM.createUTMRegions();
        SubUTM.createSubUtms();

        controller.onCreate();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        controller.onPostCreate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        controller.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        controller.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        controller.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        controller.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.onResume();
    }

    public void onPause() {
        super.onPause();
        controller.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
    }


    /*
      delete shit below....ok.....lets drink energy drink....fix this shit further then get more beers.

      yes
     */

    /*
     callbacks from before,,,need them to test
     */
    public void deleteMessages(View view) {
        controller.viewHandler.deleteMessages();
    }

    public void messageCoverage(View view) {
        controller.viewHandler.messageCoverage();
    }


    public void sendPost(View view) {
        controller.viewHandler.sendPost();
    }

    public void cancelPost(View view) {
        controller.viewHandler.cancelPost();
    }

    public void createButton(View view) {
        controller.viewHandler.createButton();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
