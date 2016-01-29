package mobile.che.com.oddymobstar.chemobile.connect;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.nfc.NfcManager;
import android.support.v4.app.FragmentActivity;

import mobile.che.com.oddymobstar.chemobile.connect.bluetooth.handler.Bluetooth;
import mobile.che.com.oddymobstar.chemobile.connect.nfc.NFC;
import mobile.che.com.oddymobstar.chemobile.util.widget.ConnectivityDialog;


/**
 * Created by root on 25/04/15.
 */
public class ConnectivityHandler {

    private FragmentActivity activity;

    private Bluetooth bluetooth;
    private NFC nfc;

    private String mode;

    private String uuid;

    public ConnectivityHandler(FragmentActivity activity, String uuid) {
        this.activity = activity;
        this.uuid = uuid;

    }


    public void handle(String mode) {

        this.mode = mode;

        switch (mode) {
            case ConnectivityDialog.BLUETOOTH:
                bluetooth = new Bluetooth(activity, uuid);
                bluetooth.enable();
                break;
            case ConnectivityDialog.NFC:
                nfc = new NFC(activity);
                nfc.enable();
                break;
            case ConnectivityDialog.WIFI:
                break;
        }


    }

    public String getMode() {
        return mode;
    }

    public NFC getNfc() {
        return nfc;
    }

    public Bluetooth getBluetooth() {
        return bluetooth;
    }


    public boolean hasBluetooth() {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null;
    }

    public boolean hasNFC() {

        NfcManager nfcManager = (NfcManager) activity.getSystemService(Context.NFC_SERVICE);
        return nfcManager != null;
    }

    public boolean hasWifiP2P() {
        //is slightly different.  leave for time being.
        return true;
    }


}
