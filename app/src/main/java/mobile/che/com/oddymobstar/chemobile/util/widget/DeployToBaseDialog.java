package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import mobile.che.com.oddymobstar.chemobile.adapter.DeployBaseAdapter;


/**
 * Created by timmytime on 09/04/16.
 */
public class DeployToBaseDialog extends DialogFragment {

    private static DialogInterface.OnClickListener deployListener;
    private static DialogInterface.OnCancelListener cancelDeployListener;

    private Cursor selectedObject;

    private static DeployBaseAdapter deployBaseAdapter;


    public static DeployToBaseDialog newInstance(DeployBaseAdapter adapter, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener dismiss) {
        deployBaseAdapter = adapter;
        deployListener = listener;
        cancelDeployListener = dismiss;
        return new DeployToBaseDialog();
    }


    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Base");

        selectedObject = (Cursor) deployBaseAdapter.getItem(0);

        builder.setSingleChoiceItems(deployBaseAdapter, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedObject = (Cursor) deployBaseAdapter.getItem(which);
            }
        });

        builder.setPositiveButton("Deploy", deployListener);


        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    cancelDeployListener.onCancel(dialog);
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
