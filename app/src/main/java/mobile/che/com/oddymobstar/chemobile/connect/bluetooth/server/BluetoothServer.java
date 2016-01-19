package mobile.che.com.oddymobstar.chemobile.connect.bluetooth.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

import oddymobstar.activity.handler.DeviceDiscoveryHandler;
import oddymobstar.connect.bluetooth.socket.BluetoothSocketThread;

/**
 * Created by root on 26/04/15.
 */
public class BluetoothServer extends Thread {

    private final static String BT_SERVER_NAME = "ALLIANCE INVITE";

    private DeviceDiscoveryHandler deviceDiscovery;
    private BluetoothServerSocket serverSocket;
    private BluetoothSocketThread bluetoothSocketThread;
    private byte[] message;

    public BluetoothServer(DeviceDiscoveryHandler deviceDiscovery, BluetoothAdapter bluetoothAdapter, String uuid, byte[] message) {

        this.deviceDiscovery = deviceDiscovery;
        this.message = message;

        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(BT_SERVER_NAME, UUID.fromString(uuid));
        } catch (IOException ioe) {

        }
    }


    public void run() {

        BluetoothSocket socket = null;

        boolean state = true;

        while (state) {

            try {
                socket = serverSocket.accept();


            } catch (IOException ioe) {
                state = false;
            }

            if (socket != null) {

                bluetoothSocketThread = new BluetoothSocketThread(deviceDiscovery, socket);

                bluetoothSocketThread.start();
                bluetoothSocketThread.write(message);

                state = false;
            }

        }

    }

    public void cancel() {
        try {
            bluetoothSocketThread.cancel();

        } catch (Exception ioe) {

        }

        try {
            serverSocket.close();

        } catch (IOException ioe) {

        }
    }

}
