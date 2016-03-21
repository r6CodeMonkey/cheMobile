package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import mobile.che.com.oddymobstar.chemobile.adapter.MissileAdapter;

/**
 * Created by timmytime on 21/03/16.
 */
public class MissileArmDialog extends DialogFragment {

    private static DialogInterface.OnClickListener missileListener;
    private static DialogInterface.OnCancelListener dismissListener;

    private static MissileAdapter missiles;

    private Cursor selectedObject;



    public static MissileArmDialog newInstance(MissileAdapter adapter, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener dismiss){
        missiles = adapter;
        missileListener = listener;
        dismissListener = dismiss;
        return new MissileArmDialog();
    }

     public Cursor getSelectItem() {
        return selectedObject;
    }



    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Missile");

        builder.setSingleChoiceItems(missiles, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedObject = (Cursor) missiles.getItem(which);
            }
        });

        builder.setPositiveButton("Load", missileListener);

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
