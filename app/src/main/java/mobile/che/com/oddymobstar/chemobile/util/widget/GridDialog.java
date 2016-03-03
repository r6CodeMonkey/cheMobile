package mobile.che.com.oddymobstar.chemobile.util.widget;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import java.util.List;

import mobile.che.com.oddymobstar.chemobile.activity.handler.MapHandler;
import util.map.SubUTM;
import util.map.UTM;


/**
 * Created by timmytime on 18/06/15.
 */
public class GridDialog extends DialogFragment {

    private static List<String> utmList = UTM.getUtmList();
    private static List<String> subUtmList = SubUTM.getSubUtmList();
    private static DialogInterface.OnClickListener locateListener;
    private static DialogInterface.OnCancelListener dismissListener;
    private static String lastSelectedGrid = "";

    public static GridDialog newInstance(String selectedGrid, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener dismiss) {
        locateListener = listener;
        dismissListener = dismiss;

        if (!selectedGrid.trim().isEmpty()) {
            lastSelectedGrid = selectedGrid;
        }

        return new GridDialog();
    }

    public Dialog onCreateDialog(Bundle savedInstance) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        //dont need fast indexer letters get in way.  makes user interpret values as its part of logic to gaming.  a challenge.

        builder.setTitle(MapHandler.CURRENT_GRID_FAB_STATE == MapHandler.UTM_FAB_STATE ? "Select UTM/Region" : MapHandler.CURRENT_GRID_FAB_STATE == MapHandler.SUBUTM_FAB_STATE ? "Select SubUTM" : "Select UTM");


        //ok it runs like a dog on map...so i click.  and filter lists by where are friends are etc....tbf this is a bit
        //OTT it shows nothing of much interest.  im just playing with materials....so stuff it.
        builder.setSingleChoiceItems(MapHandler.CURRENT_GRID_FAB_STATE == MapHandler.UTM_FAB_STATE ?
                        utmList.toArray(new CharSequence[0]) : MapHandler.CURRENT_GRID_FAB_STATE == MapHandler.SUBUTM_FAB_STATE ?
                        subUtmList.toArray(new CharSequence[0]) : UTM.getUtmRegion(MapHandler.UTM_REGION).toArray(new CharSequence[0]),
                MapHandler.CURRENT_GRID_FAB_STATE == MapHandler.UTM_FAB_STATE ?
                        utmList.indexOf(lastSelectedGrid) : MapHandler.CURRENT_GRID_FAB_STATE == MapHandler.SUBUTM_FAB_STATE ?
                        subUtmList.indexOf(lastSelectedGrid) : UTM.getUtmRegion(MapHandler.UTM_REGION).indexOf(lastSelectedGrid), locateListener);


        //not pretty but it works as i want it to.
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

    public String getGrid(int which) {
        return lastSelectedGrid = MapHandler.CURRENT_GRID_FAB_STATE == MapHandler.UTM_FAB_STATE ?
                utmList.get(which) : MapHandler.CURRENT_GRID_FAB_STATE == MapHandler.SUBUTM_FAB_STATE ?
                subUtmList.get(which) : UTM.getUtmRegion(MapHandler.UTM_REGION).get(which);
    }


}
