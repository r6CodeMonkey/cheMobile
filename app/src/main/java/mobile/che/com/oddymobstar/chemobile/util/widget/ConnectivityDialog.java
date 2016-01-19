package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import oddymobstar.connect.ConnectivityHandler;


/**
 * Created by root on 25/04/15.
 */
public class ConnectivityDialog extends DialogFragment {

    public static final String NFC = "NFC";
    public static final String WIFI = "Wifi P2P";
    public static final String BLUETOOTH = "Bluetooth";

    private static ConnectivityHandler connectivityHandler;
    private static boolean isClient = false;

    public ConnectivityDialog() {

    }


    public static ConnectivityDialog newInstance(ConnectivityHandler handler, boolean client) {
        connectivityHandler = handler;
        isClient = client;
        return new ConnectivityDialog();
    }

    public static boolean isClient() {
        return isClient;
    }

    public Dialog onCreateDialog(Bundle savedInstance) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final List<CharSequence> connAr = new ArrayList<>();

        if (connectivityHandler.hasBluetooth()) {
            connAr.add(BLUETOOTH);
        }

        if (connectivityHandler.hasNFC()) {
            connAr.add(NFC);
        }

        if (connectivityHandler.hasWifiP2P()) {
            connAr.add(WIFI);
        }


        builder.setTitle("Select Connectivity");
        builder.setSingleChoiceItems(connAr.toArray(new CharSequence[0]), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                connectivityHandler.handle(connAr.get(which).toString());
                dialog.dismiss();

            }
        });

        return builder.create();
    }

}
