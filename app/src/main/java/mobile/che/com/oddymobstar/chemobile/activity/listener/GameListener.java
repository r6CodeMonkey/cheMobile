package mobile.che.com.oddymobstar.chemobile.activity.listener;

import android.content.DialogInterface;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.GameController;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;

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
                        controller.gameController.gameHandler.handleTarget(latLng);
                        break;
                    case GameController.DEFAULT_STATE: //nothing to do...your too late...
                        break;
                }

            }
        };

    }

    /*
     moving our game action listeners here.
     */

    public DialogInterface.OnClickListener getTargetListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                //neutral action
                dialog.dismiss();
                controller.gameController.gameHandler.handleGameObjectTarget(controller.gameController.actionsDialog.getGameObjectKey());
            }
        };
    }

    public DialogInterface.OnClickListener getCancelTargetListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                //neutral action
                dialog.dismiss();
                controller.gameController.gameHandler.handleCancelTarget(controller.gameController.actionsDialog.getGameObjectKey());
            }
        };
    }

    public DialogInterface.OnClickListener getLaunchListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                //neutral action
                dialog.dismiss();
                controller.gameController.gameHandler.handleLaunch(controller.gameController.actionsDialog.getGameObjectKey());
            }
        };
    }

    public DialogInterface.OnClickListener getRepairListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                //neutral action
                dialog.dismiss();
                controller.gameController.gameHandler.handleRepair(controller.gameController.actionsDialog.getGameObjectKey());
            }
        };
    }

    public DialogInterface.OnClickListener getLandingListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                //neutral action
                dialog.dismiss();
                controller.gameController.gameHandler.handleLanding(controller.gameController.actionsDialog.getGameObjectKey());
            }
        };
    }

    public DialogInterface.OnClickListener getStopListeningListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                //neutral action
                dialog.dismiss();
                controller.gameController.gameHandler.handleStopListening(controller.gameController.actionsDialog.getGameObjectKey());
            }
        };
    }

    public DialogInterface.OnClickListener getStartListeningListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                //neutral action
                dialog.dismiss();
                controller.gameController.gameHandler.handleStartListening(controller.gameController.actionsDialog.getGameObjectKey());
            }
        };
    }


    public DialogInterface.OnClickListener getMoveListener(final int type) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                dialog.dismiss();
                switch (type) {
                    case GameObjectGridFragment.AIR: //take off .. does it really need airport....ie cant deploy unless at a nase
                        controller.gameController.gameHandler.handleGameObjectMove(controller.gameController.actionsDialog.getGameObjectKey());
                        break;
                    case GameObjectGridFragment.SEA:  //same with sea.  only at certains places.
                        break;
                    case GameObjectGridFragment.LAND:
                        Log.d("actions", "actions handler");
                        controller.gameController.gameHandler.handleGameObjectMove(controller.gameController.actionsDialog.getGameObjectKey());
                        break;
                }
            }
        };
    }


}
