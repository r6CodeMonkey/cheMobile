package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.connect.bluetooth.manager.BluetoothManager;


/**
 * Created by timmytime on 06/12/15.
 */
public class DeviceDiscoveryHandler {


    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    private BluetoothManager bluetoothManager;

    public DeviceDiscoveryHandler(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;

    }

    public void init() {
        bluetoothManager = new BluetoothManager(main.getApplicationContext(), controller, controller.fragmentHandler.chatFrag.getKey());
    }


    public void addDevice(BluetoothDevice device) {

        Log.d("adding device", "adding a device");
        if (bluetoothManager.addDevice(device)) {
            controller.fragmentHandler.deviceFragment.refreshAdapter(device.getName());
        }
    }

    public BluetoothManager getBluetoothManager() {
        return bluetoothManager;
    }


    public void onDiscover(boolean isClient) {
        bluetoothManager.setIsRunning(true);
        //we launch..
        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();
        controller.fragmentHandler.deviceFragment = bluetoothManager.onDiscover(isClient);
        controller.fragmentHandler.deviceFragment.show(transaction, "dialog");
    }


}
