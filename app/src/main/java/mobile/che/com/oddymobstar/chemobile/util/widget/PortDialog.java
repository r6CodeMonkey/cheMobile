package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import mobile.che.com.oddymobstar.chemobile.adapter.PortAdapter;

/**
 * Created by timmytime on 09/04/16.
 */
public class PortDialog extends DialogFragment {

    private static DialogInterface.OnClickListener selectListener;
    private static DialogInterface.OnCancelListener cancelListener;

    private Cursor selectedObject;

    private static String actionTitle = "";

    private static PortAdapter portAdapter;

    public static PortDialog newInstance(String title,PortAdapter adapter, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener dismiss) {
        portAdapter = adapter;
        selectListener = listener;
        cancelListener = dismiss;
        actionTitle = title;
        return new PortDialog();
    }


    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Item");

        selectedObject = (Cursor) portAdapter.getItem(0);

        builder.setSingleChoiceItems(portAdapter, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedObject = (Cursor) portAdapter.getItem(which);
            }
        });

        builder.setPositiveButton(actionTitle, selectListener);


        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    cancelListener.onCancel(dialog);
                    dialog.dismiss();
                }
                return true;
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();

        dialog.getListView().setFastScrollEnabled(true);

        return dialog;
    }

    public Cursor getSelectedObject(){
        return selectedObject;
    }


}
