package mobile.che.com.oddymobstar.chemobile.activity.listener;

import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.GameController;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.model.UTM;
import util.map.SubUTM;

/**
 * Created by timmytime on 17/02/16.
 */
public class MapListener {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    public MapListener(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public GoogleMap.OnMarkerClickListener getMarkerListener() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                if (controller.gameController.GAME_STATE == GameController.DEFAULT_STATE) {
                    if (!marker.getTitle().equals("Destination")) {
                        GameObject gameObject = controller.dbHelper.getGameObject(marker.getSnippet());
                        controller.gameController.gameHandler.actionsDialog(gameObject.getKey(), gameObject.getType());
                    } else {
                        marker.showInfoWindow();
                    }
                    return true;
                } else if (controller.gameController.GAME_STATE == GameController.GAME_OBJECT_FLY_TO_OTHER_BASE_STATE) {
                    if (controller.gameController.basesInRange.contains(marker.getSnippet())) {
                        //success...
                        final GameObject dest = controller.dbHelper.getGameObject(marker.getSnippet());

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        List<SubUTM> validators = new ArrayList<SubUTM>();
                                        validators.add(new SubUTM(dest.getSubUtmLat(), dest.getSubUtmLong())); //subUtm.getUtmLat() + subUtm.getUtmLong()

                                        try {
                                            controller.cheService.writeToSocket(controller.messageFactory.moveGameObject(controller.gameController.currentGameObject,
                                                    validators,
                                                    new LatLng(dest.getLatitude(), dest.getLongitude()), controller.locationListener.getCurrentLocation()));
                                        } catch (NoSuchAlgorithmException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                    } else {
                        Toast.makeText(main, "Base outside Maximum Range", Toast.LENGTH_SHORT).show();
                    }
                }
                {//could warn ie not allowed...as active..
                    return false;
                }


            }
        };
    }


}
