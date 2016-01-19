package mobile.che.com.oddymobstar.chemobile.connect.bluetooth.manager;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oddymobstar.activity.controller.DemoActivityController;
import oddymobstar.fragment.DeviceFragment;
import oddymobstar.message.in.InAllianceMessage;
import oddymobstar.message.out.OutAllianceMessage;
import oddymobstar.message.out.OutCoreMessage;
import oddymobstar.model.Alliance;
import oddymobstar.util.Configuration;

/**
 * Created by root on 27/04/15.
 */
public class BluetoothManager {

    private List<BluetoothDevice> devices;
    private Map<String, Boolean> selectedControl = new HashMap<>();
    private boolean isRunning = false;
    private List<BluetoothDevice> selectedDevices = new ArrayList<>();
    private String deviceName;


    private String key;
    private Location location;  //i dont need this perse....but message does
    private Context context;
    private DemoActivityController controller;


    private DialogInterface.OnClickListener inviteListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {


            //we need to alliance message...
            OutAllianceMessage allianceMessage = null;


            try {
                allianceMessage = new OutAllianceMessage(location, controller.configuration.getConfig(Configuration.PLAYER_KEY).getValue(), controller.uuidGenerator.generateAcknowledgeKey());
                allianceMessage.setAlliance(controller.dbHelper.getAlliance(key), OutCoreMessage.INVITE, OutCoreMessage.GLOBAL, "Invitation to Join");
                controller.connectivityHandler.getBluetooth().setMessage(allianceMessage.getMessage().toString().getBytes());
            } catch (JSONException jse) {

            } catch (NoSuchAlgorithmException nsae) {

            }
            controller.connectivityHandler.getBluetooth().createServer();

            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener requestListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (!selectedDevices.isEmpty()) {
                controller.connectivityHandler.getBluetooth().createClient();
                dialog.dismiss();
            }
        }
    };

    private AdapterView.OnItemClickListener selectionListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            BluetoothDevice temp = devices.get(position);

            if (selectedControl.get(temp.getName())) {
                selectedControl.put(temp.getName(), Boolean.FALSE);
            } else {
                selectedControl.put(temp.getName(), Boolean.TRUE);
            }

            if (selectedControl.get(temp.getName())) {
                selectedDevices.add(devices.get(position));
            } else {
                int counter = 0;
                int pos = 0;
                for (BluetoothDevice d : selectedDevices) {
                    if (d.getName().equals(temp.getName())) {
                        pos = counter;
                    }
                    counter++;
                }
                selectedDevices.remove(pos);
            }
        }
    };


    public BluetoothManager(Context context, DemoActivityController controller, String key) {
        this.context = context;
        this.controller = controller;
        this.key = key;
        this.location = controller.locationListener.getCurrentLocation();

    }


    public void init(List<BluetoothDevice> devices, String deviceName) {
        this.deviceName = deviceName;
        this.devices = devices;
        //now add all our devices in this list as unselected
        for (BluetoothDevice device : devices) {
            selectedControl.put(device.getName(), Boolean.FALSE);
        }
    }

    public List<BluetoothDevice> getSelectedDevices() {
        return selectedDevices;
    }


    public String getDeviceName() {
        return deviceName;
    }


    public boolean isRunning() {
        return isRunning;
    }

    public boolean addDevice(BluetoothDevice device) {
        boolean add = true;
        for (BluetoothDevice d : devices) {
            if (d.getAddress().equals(device.getAddress())) {
                add = false;
            }
        }
        if (add) {
            devices.add(device);
            selectedControl.put(device.getName(), Boolean.FALSE);

        }

        return add;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public DeviceFragment onDiscover(boolean isClient) {

        List<String> deviceKeys = new ArrayList<>();
        for (BluetoothDevice device : devices) {
            deviceKeys.add(device.getName());
        }

        return DeviceFragment.newInstance(deviceKeys, context, isClient, inviteListener, requestListener, selectionListener);

    }


    public void handleMessage(InAllianceMessage message) {


        //this is where we contact che service with message...
        try {
            OutAllianceMessage allianceMessage = new OutAllianceMessage(location, controller.configuration.getConfig(Configuration.PLAYER_KEY).getValue(), controller.uuidGenerator.generateAcknowledgeKey());
            Alliance alliance = new Alliance();
            alliance.setKey(message.getAid());
            alliance.setName(message.getName());
            allianceMessage.setAlliance(alliance, OutCoreMessage.JOIN, OutCoreMessage.GLOBAL, "Joining");

            controller.cheService.writeToSocket(allianceMessage);
            controller.dbHelper.addAlliance(alliance, true);
            controller.connectivityHandler.getBluetooth().disable();

        } catch (JSONException jse) {

        } catch (NoSuchAlgorithmException nsae) {

        }
    }


    public void unpair(String deviceName) {
        int counter = 0;
        int pos = 0;
        for (BluetoothDevice device : selectedDevices) {
            if (device.getName().equals(deviceName)) {
                controller.connectivityHandler.getBluetooth().unpair(device);
                pos = counter;
            }
            counter++;
        }

        selectedDevices.remove(pos);
        //and remove
        if (selectedDevices.isEmpty()) {
            controller.connectivityHandler.getBluetooth().disable();
        }

    }


}
