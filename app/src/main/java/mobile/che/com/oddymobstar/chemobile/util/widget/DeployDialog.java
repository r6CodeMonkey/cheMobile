package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

/**
 * Created by timmytime on 16/02/16.
 */
public class DeployDialog extends DialogFragment {

    private static DialogInterface.OnClickListener deployListener;
    private static DialogInterface.OnCancelListener dismissListener;

    private static String gameObjectName = "";
    private static String deployAction = "";


    private static String gameObjectKey = "";


    public static DeployDialog newInstance(String action, String gameObject, String key,
                                           DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener dismiss) {
        deployListener = listener;
        dismissListener = dismiss;
        gameObjectName = gameObject;
        deployAction = action;
        gameObjectKey = key;

        return new DeployDialog();
    }

    public String getGameObjectKey() {
        return gameObjectKey;
    }


    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(gameObjectName);

        builder.setPositiveButton(deployAction, deployListener);


        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissListener.onCancel(dialog);
                    dialog.dismiss();
                }
                return true;
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();

        return dialog;
    }

}
