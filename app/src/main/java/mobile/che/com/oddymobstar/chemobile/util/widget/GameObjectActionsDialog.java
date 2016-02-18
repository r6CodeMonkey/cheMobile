package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;

/**
 * Created by timmytime on 17/02/16.
 */
public class GameObjectActionsDialog extends DialogFragment {

    private static DialogInterface.OnClickListener actionListener;
    private static DialogInterface.OnClickListener actionListener2;
    private static DialogInterface.OnClickListener actionListener3;
    private static DialogInterface.OnCancelListener dismissListener;


    private static int gameObjectType;
    private static String gameObjectKey;


    public static GameObjectActionsDialog newInstance(int type, String key, DialogInterface.OnClickListener listener,
                                                      DialogInterface.OnClickListener listener2, DialogInterface.OnClickListener listener3, DialogInterface.OnCancelListener dismiss) {
        actionListener = listener;
        actionListener2 = listener2;
        actionListener3 = listener3;
        dismissListener = dismiss;
        gameObjectType = type;
        gameObjectKey = key;
        return new GameObjectActionsDialog();
    }

    public String getGameObjectKey() {
        return gameObjectKey;
    }

    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Game Controls");
        builder.setMessage("What would you like to do?");

        String positiveAction = "";
        String negativeAction = "";
        String neutralAction = "";

        switch (gameObjectType) {
            case GameObjectGridFragment.AIR:
                positiveAction = "Take Off";
                negativeAction = "Land";
                neutralAction = "Target";
                break;
            case GameObjectGridFragment.LAND:
                positiveAction = "Move";
                negativeAction = "Stop";
                neutralAction = "Target";
                break;
            case GameObjectGridFragment.SEA:
                positiveAction = "Move";
                negativeAction = "Stop";
                neutralAction = "Target";
                break;
            case GameObjectGridFragment.INFASTRUCTURE:
                positiveAction = "Repair";
                break;


        }
        if (!positiveAction.trim().isEmpty()) {
            builder.setPositiveButton(positiveAction, actionListener);
        }
        if (!negativeAction.trim().isEmpty()) {
            builder.setNegativeButton(negativeAction, actionListener2);
        }
        if (!neutralAction.trim().isEmpty()) {
            builder.setNeutralButton(neutralAction, actionListener3);
        }


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
