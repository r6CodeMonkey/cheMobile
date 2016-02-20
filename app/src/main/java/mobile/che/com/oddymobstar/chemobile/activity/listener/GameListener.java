package mobile.che.com.oddymobstar.chemobile.activity.listener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.GameController;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;

/**
 * Created by timmytime on 18/02/16.
 */
public class GameListener {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    public GameListener(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public GoogleMap.OnMapLongClickListener getGameLongClickListener() {

        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                //what is our mode...
                switch (controller.gameController.GAME_STATE) {
                    case GameController.GAME_OBJECT_MOVE_STATE:
                        controller.gameController.gameHandler.handleMoveDestination(latLng);
                        break;
                    case GameController.GAME_OBJECT_TARGET_STATE:
                        break;
                    case GameController.DEFAULT_STATE: //nothing to do...your too late...
                        break;
                }

            }
        };

    }


}
