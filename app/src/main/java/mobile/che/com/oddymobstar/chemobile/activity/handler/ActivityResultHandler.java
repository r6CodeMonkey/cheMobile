package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.activity.listener.MaterialsListener;
import mobile.che.com.oddymobstar.chemobile.connect.bluetooth.handler.Bluetooth;
import mobile.che.com.oddymobstar.chemobile.model.UserImage;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.widget.ConnectivityDialog;

/**
 * Created by timmytime on 04/12/15.
 */
public class ActivityResultHandler {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    public ActivityResultHandler(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case MaterialsListener.USER_IMAGE_RESULT_CODE:

                if (resultCode == main.RESULT_OK) {
                    handleImage(data);
                }
                return;

            case Bluetooth.REQUEST_ENABLE_BT:
                handleBluetooth(requestCode, resultCode, data);
                return;
        }
    }

    private void handleBluetooth(int requestCode, int resultCode, Intent data) {

        switch (controller.connectivityHandler.getMode()) {
            case ConnectivityDialog.BLUETOOTH:

                controller.bluetoothReceiver = controller.connectivityHandler.getBluetooth().getReceiver();
                main.registerReceiver(controller.bluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

                controller.connectivityHandler.getBluetooth().handle(requestCode, resultCode, data);
                if (resultCode == controller.connectivityHandler.getBluetooth().DISCOVERABLE_SECONDS) {
                    controller.deviceDiscoveryHandler.init();
                    controller.connectivityHandler.getBluetooth().getProgress(controller.deviceDiscoveryHandler).show();
                }
                break;

        }
    }

    private void handleImage(Intent data) {
        try {
            // We need to recyle unused bitmaps

            InputStream stream = main.getContentResolver().openInputStream(
                    data.getData());
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            bitmap = Bitmap.createScaledBitmap(bitmap, 236, 354, false);
            Log.d("bitmap size is", "size " + bitmap.getRowBytes() * bitmap.getHeight());
            controller.materialsHelper.userImageView.setImageBitmap(bitmap);
            byte[] imageArray;
            if (controller.materialsHelper.userImage == null) {
                controller.materialsHelper.userImage = new UserImage();
                controller.materialsHelper.userImage.setUserImageKey(controller.configuration.getConfig(Configuration.PLAYER_KEY).getValue());
                controller.materialsHelper.userImage.setUserImage(bitmap);
                imageArray = controller.dbHelper.addUserImage(controller.materialsHelper.userImage);
            } else {
                controller.materialsHelper.userImage.setUserImage(bitmap);
                imageArray = controller.dbHelper.updateUserImage(controller.materialsHelper.userImage);
            }

            try {
                final OutImageMessage outImageMessage = new OutImageMessage(controller.locationListener.getCurrentLocation(), controller.configuration.getConfig(Configuration.PLAYER_KEY).getValue(), controller.uuidGenerator.generateAcknowledgeKey());
                outImageMessage.setImage(Base64.encodeToString(imageArray, Base64.DEFAULT));

                new Thread((new Runnable() {
                    @Override
                    public void run() {
                        controller.cheService.writeToSocket(outImageMessage);
                    }
                })).start();


            } catch (JSONException jse) {

            } catch (NoSuchAlgorithmException nsae) {

            }

            //need to review this too.! bitmap.recycle();


        } catch (FileNotFoundException fe) {
        } catch (IOException e) {

        }
    }

}
