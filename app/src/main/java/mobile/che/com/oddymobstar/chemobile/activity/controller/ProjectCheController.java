package mobile.che.com.oddymobstar.chemobile.activity.controller;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.widget.Toast;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.handler.ActivityResultHandler;
import mobile.che.com.oddymobstar.chemobile.activity.handler.ConfigurationHandler;
import mobile.che.com.oddymobstar.chemobile.activity.handler.FragmentHandler;
import mobile.che.com.oddymobstar.chemobile.activity.handler.MapHandler;
import mobile.che.com.oddymobstar.chemobile.activity.handler.MaterialsHandler;
import mobile.che.com.oddymobstar.chemobile.activity.handler.MessageHandler;
import mobile.che.com.oddymobstar.chemobile.activity.handler.OnOptionsItemSelectedHandler;
import mobile.che.com.oddymobstar.chemobile.activity.handler.SharedPreferencesHandler;
import mobile.che.com.oddymobstar.chemobile.activity.handler.ViewHandler;
import mobile.che.com.oddymobstar.chemobile.activity.helper.LocationHelper;
import mobile.che.com.oddymobstar.chemobile.activity.helper.MapHelper;
import mobile.che.com.oddymobstar.chemobile.activity.helper.MaterialsHelper;
import mobile.che.com.oddymobstar.chemobile.activity.listener.LocationListener;
import mobile.che.com.oddymobstar.chemobile.activity.listener.MaterialsListener;
import mobile.che.com.oddymobstar.chemobile.activity.listener.ViewListener;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.service.CheService;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.MessageFactory;
import mobile.che.com.oddymobstar.chemobile.util.UUIDGenerator;
import mobile.che.com.oddymobstar.chemobile.util.widget.GridDialog;

/**
 * Created by timmytime on 19/01/16.
 */
public class ProjectCheController {

    //statics etc
    public static final String BLUETOOTH_UUID = "39159dac-ead1-47ad-9975-ec8390df6f7d";
    public static final String MESSAGE_INTENT = "MESSAGE_INTENT";
    private final ProjectCheActivity main;
    //utils
    public UUIDGenerator uuidGenerator;
    public Intent intent;
    public Intent serviceIntent;
    public Configuration configuration;
    public DBHelper dbHelper;
    public CheService cheService;
    public ServiceConnection serviceConnection;
    public MessageFactory messageFactory;
    //helpers
    public MaterialsHelper materialsHelper;
    public MapHelper mapHelper;
    public LocationHelper locationHelper;
    //handlers
    public MessageHandler messageHandler;
    public MaterialsHandler materialsHandler;
    public MapHandler mapHandler;
    public ConfigurationHandler configurationHandler;
    public ViewHandler viewHandler;
    public ActivityResultHandler activityResultHandler;
    public OnOptionsItemSelectedHandler onOptionsItemSelectedHandler;
    public FragmentHandler fragmentHandler;
    //listeners
    public MaterialsListener materialsListener;
    public LocationListener locationListener;
    public ViewListener viewListener;
    //receivers
    public BroadcastReceiver bluetoothReceiver;
    public BroadcastReceiver messageReceiver;
    //managers
    public LocationManager locationManager;
    //fragments
    public GridDialog gridDialog;

    public ProjectCheController(ProjectCheActivity main) {
        this.main = main;
    }

    public void onCreate() {

        dbHelper = new DBHelper(main);
        messageFactory = new MessageFactory(dbHelper);


        if (!dbHelper.hasPreLoad()) {
            dbHelper.addBaseConfiguration();
            Config config = dbHelper.getConfig(Configuration.PLAYER_NAME);
            config.setValue(main.googleAccountName);
            dbHelper.updateConfig(config);
        }

        //really for testing...
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                Toast.makeText(main, message, Toast.LENGTH_SHORT).show();
            }
        };


        fragmentHandler = new FragmentHandler(main, this);
        configuration = new Configuration(dbHelper.getConfigs());
        messageHandler = new MessageHandler(main, this);
        uuidGenerator = new UUIDGenerator(configuration.getConfig(Configuration.UUID_ALGORITHM).getValue());
        configurationHandler = new ConfigurationHandler(this);
        viewHandler = new ViewHandler(main, this);
        viewListener = new ViewListener(main, this);
        activityResultHandler = new ActivityResultHandler(main, this);
        onOptionsItemSelectedHandler = new OnOptionsItemSelectedHandler(main, this);

        dbHelper.setMessageHandler(messageHandler);

        materialsHelper = new MaterialsHelper(main);
        materialsHandler = new MaterialsHandler(main, this);
        materialsListener = new MaterialsListener(main, this);

        materialsHelper.userImage = dbHelper.getUserImage(configuration.getConfig(Configuration.PLAYER_KEY).getValue());
        materialsHelper.playerKeyString = dbHelper.getConfig(Configuration.PLAYER_KEY).getValue();

        materialsHelper.setUpMaterials(
                materialsListener.getFABListener(),
                materialsListener.getImageListener());

        materialsHandler.setNavConfigValues();

        locationManager = (LocationManager) main.getSystemService(Context.LOCATION_SERVICE);
        mapHandler = new MapHandler(main, this);
        mapHelper = new MapHelper(main, this);
        locationHelper = new LocationHelper(this);


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                cheService = ((CheService.CheServiceBinder) service).getCheServiceInstance();
                cheService.setMessageHandler(messageHandler);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                cheService = null;
            }
        };

        intent = new Intent(main, CheService.class);
        serviceIntent = new Intent(main, CheService.class);

        LocalBroadcastManager.getInstance(main).registerReceiver(messageReceiver, new IntentFilter(MESSAGE_INTENT));

        main.startService(serviceIntent);
        main.bindService(intent, serviceConnection, main.BIND_AUTO_CREATE);

        mapHelper.setUpMapIfNeeded();

    }

    public void onPostCreate() {
        materialsHelper.navToggle.syncState();
    }

    public void onPause() {
        if (bluetoothReceiver != null) {
            try {
                main.unregisterReceiver(bluetoothReceiver);
            } catch (Exception e) {
                //its probably not registered..
            }
        }

        SharedPreferences sharedPreferences = main.getPreferences(Context.MODE_PRIVATE);
        SharedPreferencesHandler.handle(sharedPreferences, this);
    }

    public void onResume() {
        SharedPreferences sharedPreferences = main.getPreferences(Context.MODE_PRIVATE);

        if (locationListener.getCurrentLocation() == null) {
            locationListener.setCurrentLocation(new Location(sharedPreferences.getString(SharedPreferencesHandler.PROVIDER, "")));
        }

        locationListener.getCurrentLocation().setLatitude(Double.parseDouble(sharedPreferences.getString(SharedPreferencesHandler.LATITUTE, "0.0")));
        locationListener.getCurrentLocation().setLongitude(Double.parseDouble(sharedPreferences.getString(SharedPreferencesHandler.LONGITUDE, "0.0")));

        mapHelper.setUpMapIfNeeded();

        if (locationHelper.getLocationUpdates() == null) {
            mapHelper.initLocationUpdates();
        }
        //and we need to bind to it.
        if (cheService == null) {
            main.bindService(intent, serviceConnection, main.BIND_AUTO_CREATE);
        }
    }

    public void onDestroy() {
        //service = null;
        locationHelper.killLocationUpdates();

        main.unbindService(serviceConnection);

        SharedPreferences sharedPreferences = main.getPreferences(Context.MODE_PRIVATE);
        SharedPreferencesHandler.handle(sharedPreferences, this);

        LocalBroadcastManager.getInstance(main).unregisterReceiver(messageReceiver);

        //have removed all of this.
        if (bluetoothReceiver != null) {
            try {
                main.unregisterReceiver(bluetoothReceiver);
            } catch (Exception e) {
                //probably no longer registerd..
            }
        }


        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
    }


    public void onBackPressed() {
         fragmentHandler.removeFragments(true);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        materialsHelper.navToggle.onConfigurationChanged(newConfig);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        activityResultHandler.handleResult(requestCode, resultCode, data);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return onOptionsItemSelectedHandler.onOptionsItemSelected(item);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) main.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
