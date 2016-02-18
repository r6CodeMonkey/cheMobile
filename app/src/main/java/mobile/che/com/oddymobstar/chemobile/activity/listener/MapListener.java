package mobile.che.com.oddymobstar.chemobile.activity.listener;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;

/**
 * Created by timmytime on 17/02/16.
 */
public class MapListener {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    public MapListener(ProjectCheActivity main, ProjectCheController controller){
        this.main = main;
        this.controller = controller;
    }

    public GoogleMap.OnMarkerClickListener getMarkerListener(){
       return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                GameObject gameObject = controller.dbHelper.getGameObject(marker.getSnippet());
                controller.gameController.gameHandler.actionsDialog(gameObject.getKey(), gameObject.getType());
                return true;
            }
        };
    }


}
