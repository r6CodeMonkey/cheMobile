package mobile.che.com.oddymobstar.chemobile.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


/**
 * Created by root on 25/04/15.
 */
public class DeviceFragment extends DialogFragment {

    private static ArrayAdapter<String> adapter;


    private DialogInterface.OnClickListener sendInvite;
    private DialogInterface.OnClickListener receiveInvite;

    private boolean isClient = false;

    private AdapterView.OnItemClickListener listener;


    public DeviceFragment() {

    }

    public static DeviceFragment newInstance(List<String> devices, Context context, boolean isClient, DialogInterface.OnClickListener sendInvite, DialogInterface.OnClickListener receiveInvite, AdapterView.OnItemClickListener listener) {
        DeviceFragment deviceFragment = new DeviceFragment();
        deviceFragment.init(devices, context, isClient, sendInvite, receiveInvite, listener);
        return deviceFragment;
    }

    public void init(List<String> devices, Context context, boolean isClient, DialogInterface.OnClickListener sendInvite, DialogInterface.OnClickListener receiveInvite, AdapterView.OnItemClickListener listener) {
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, devices);


        this.sendInvite = sendInvite;
        this.receiveInvite = receiveInvite;
        this.listener = listener;
        this.isClient = isClient;
    }

    public Dialog onCreateDialog(Bundle savedInstance) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.device_fragment, null);

        ListView lv = (ListView) v.findViewById(android.R.id.list);
        lv.setAdapter(adapter);

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lv.setOnItemClickListener(listener);


        builder.setView(v);
        builder.setTitle("Devices Discovered");

        if (!isClient) {
            builder.setPositiveButton("Send Invite", sendInvite);
        } else {
            builder.setNegativeButton("Receive Invite", receiveInvite);
        }

        return builder.create();

    }

    public void refreshAdapter(String device) {
        if (adapter != null) {
            adapter.add(device);
        }
    }


}
