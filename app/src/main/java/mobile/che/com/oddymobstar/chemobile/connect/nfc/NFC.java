package mobile.che.com.oddymobstar.chemobile.connect.nfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcManager;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import oddymobstar.activity.DemoActivity;
import oddymobstar.connect.ConnectivityInterface;
import oddymobstar.database.DBHelper;
import oddymobstar.message.in.InAllianceMessage;
import oddymobstar.message.in.InCoreMessage;
import oddymobstar.message.out.OutAllianceMessage;
import oddymobstar.message.out.OutCoreMessage;
import oddymobstar.model.Alliance;
import oddymobstar.service.handler.CheService;

/**
 * Created by root on 25/04/15.
 */
public class NFC implements ConnectivityInterface, NfcAdapter.CreateNdefMessageCallback {


    public static final String BYTE_ARR_ALLIANCE_ID = "allianceID";
    /*
    using beam its all very easy.  there is no device discovery.
    its tap and go basically.  assuming we have the option.

    Therefore user selects NFC (enables if not on)
    same with other user, then it happens.
     */
    private NfcAdapter nfcAdapter;

    private FragmentActivity activity;

    private byte[] message;


    public NFC(FragmentActivity activity) {

        this.activity = activity;

        nfcAdapter = ((NfcManager) activity.getSystemService(Context.NFC_SERVICE)).getDefaultAdapter();

    }

    //a fucking joke is NFC beam.  killing objects
    public static void handleInvite(Intent data, Location location, CheService cheService, DBHelper dbHelper, String playerKey, String ackId) {
        //
        Parcelable[] rawMessage = data.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        //its only 1 message, as a JSON
        NdefMessage msg = (NdefMessage) rawMessage[0];

        try {
            JSONObject jsonObject = new JSONObject(new String(msg.getRecords()[0].getPayload()));

            InAllianceMessage inAllianceMessage = new InAllianceMessage(jsonObject.getJSONObject(InCoreMessage.CORE).getJSONObject(InCoreMessage.ALLIANCE));


            OutAllianceMessage allianceMessage = new OutAllianceMessage(location, playerKey, ackId);
            Alliance alliance = new Alliance();
            alliance.setKey(inAllianceMessage.getAid());
            alliance.setName(inAllianceMessage.getName());

            allianceMessage.setAlliance(alliance, OutCoreMessage.JOIN, OutCoreMessage.GLOBAL, "Joining");

            Log.d("message is", "message" + allianceMessage.getMessage().toString());

            cheService.writeToSocket(allianceMessage);

            dbHelper.addAlliance(alliance, true);


        } catch (JSONException jse) {

        }


    }

    @Override
    public void handle(int requestCode, int resultCode, Intent data) {


    }

    @Override
    public void enable() {
        if (nfcAdapter != null) {
            nfcAdapter.setNdefPushMessageCallback(this, activity);
        }

        if (!nfcAdapter.isEnabled()) {
            Intent nfcSettings = new Intent(
                    Settings.ACTION_NFC_SETTINGS);
            activity.startActivity(nfcSettings);
        }

        ((DemoActivity) activity).onActivityResult(Activity.RESULT_OK, Activity.RESULT_OK, null);

    }

    @Override
    public void disable() {

    }

    @Override
    public void setMessage(byte[] message) {
        this.message = message;
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage msg = null;

        Log.d("received something", "creating the message " + message.toString());

        msg = new NdefMessage(NdefRecord.createMime("application/com.oddymobstar.android.beam", message));


        return msg;
    }


}
