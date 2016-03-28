package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import mobile.che.com.oddymobstar.chemobile.adapter.ArmExplosiveAdapter;

/**
 * Created by timmytime on 17/02/16.
 */
public class ArmDialog extends DialogFragment {

    private static DialogInterface.OnClickListener armListener;
    private static DialogInterface.OnCancelListener dismissListener;

    private static String gameObjectName = "";

    private static String gameObjectKey = "";
    private static ArmExplosiveAdapter gameObjects;

    private Cursor selectedObject;

    public static ArmDialog newInstance(String gameObject, String key, ArmExplosiveAdapter adapter,
                                        DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener dismiss) {
        armListener = listener;
        dismissListener = dismiss;
        gameObjectName = gameObject;
        gameObjectKey = key;
        gameObjects = adapter;


        return new ArmDialog();

    }

    public String getGameObjectKey() {
        return gameObjectKey;
    }

    public Cursor getSelectItem() {
        return selectedObject;
    }

    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Arm " + gameObjectName);

        builder.setSingleChoiceItems(gameObjects, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedObject = (Cursor) gameObjects.getItem(which);
            }
        });

        builder.setPositiveButton("Arm", armListener);

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

        dialog.getListView().setFastScrollEnabled(true);

        return dialog;
    }

}
