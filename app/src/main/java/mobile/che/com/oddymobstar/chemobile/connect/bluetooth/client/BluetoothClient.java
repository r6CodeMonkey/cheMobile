package mobile.che.com.oddymobstar.chemobile.connect.bluetooth.client;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

import oddymobstar.activity.handler.DeviceDiscoveryHandler;
import oddymobstar.connect.bluetooth.socket.BluetoothSocketThread;

/**
 * Created by root on 26/04/15.
 */
public class BluetoothClient extends Thread {

    private DeviceDiscoveryHandler deviceDiscovery;
    private BluetoothSocket socket;
    private BluetoothSocketThread bluetoothSocketThread;


    public BluetoothClient(DeviceDiscoveryHandler deviceDiscovery, String uuid) {
        this.deviceDiscovery = deviceDiscovery;


        try {
            //its always the first one...cant select more...well you can but its pointless
            socket = deviceDiscovery.getBluetoothManager().getSelectedDevices().get(0).createRfcommSocketToServiceRecord(UUID.fromString(uuid));
        } catch (IOException ioe) {

        }
    }

    public void run() {
        try {
            socket.connect();

            bluetoothSocketThread = new BluetoothSocketThread(deviceDiscovery, socket);
            bluetoothSocketThread.start();

        } catch (IOException ioe) {
            cancel();
        }
    }

    public void cancel() {
        try {
            bluetoothSocketThread.cancel();
        } catch (Exception e) {

        }

        try {
            socket.close();
        } catch (IOException ioe) {

        }
    }
}
