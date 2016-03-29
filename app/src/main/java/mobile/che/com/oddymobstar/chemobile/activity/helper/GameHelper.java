package mobile.che.com.oddymobstar.chemobile.activity.helper;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.google.android.gms.maps.model.LatLng;

import java.security.NoSuchAlgorithmException;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.GameController;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import util.GameObjectTypes;

/**
 * Created by timmytime on 16/02/16.
 */
public class GameHelper {

    private final ProjectCheController controller;
    private final ProjectCheActivity main;

    public GameHelper(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public static int getGameColor(int type) {

        switch (type) {
            case GameObjectGridFragment.MISSILE:
                return android.R.color.holo_purple;
            case GameObjectGridFragment.AIR:
                return android.R.color.holo_blue_bright;
            case GameObjectGridFragment.SEA:
                return android.R.color.holo_blue_dark;
            case GameObjectGridFragment.LAND:
                return android.R.color.holo_green_dark;
            case GameObjectGridFragment.INFASTRUCTURE:
                return android.R.color.holo_green_light;

        }

        return MaterialsHelper.MISSILE_COLOR;
    }

    public static int getGameColorFlag(int type) {
        switch (type) {
            case GameObjectGridFragment.MISSILE:
                return MaterialsHelper.MISSILE_COLOR;
            case GameObjectGridFragment.AIR:
                return MaterialsHelper.AIR_COLOR;
            case GameObjectGridFragment.SEA:
                return MaterialsHelper.SEA_COLOR;
            case GameObjectGridFragment.LAND:
                return MaterialsHelper.LAND_COLOR;
            case GameObjectGridFragment.INFASTRUCTURE:
                return MaterialsHelper.INFRA_COLOR;

        }

        return MaterialsHelper.MISSILE_COLOR;
    }

    public void purchase(int type, int subType, final int quantity) {
        final mobile.che.com.oddymobstar.chemobile.model.GameObject gameObject = new mobile.che.com.oddymobstar.chemobile.model.GameObject();
        gameObject.setType(type);
        gameObject.setSubType(subType);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject, controller.locationListener.getCurrentLocation(), quantity));
                } catch (NoSuchAlgorithmException e) {

                }
            }
        }).start();
    }

    /*

     */

    public void infrastructureInit() {
        //1 garrison
        //2 sattelites
        //2 outposts

        purchase(GameObjectGridFragment.INFASTRUCTURE, GameObjectTypes.GARRISON, 1);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.INFASTRUCTURE, GameObjectTypes.OUTPOST, 2);
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.INFASTRUCTURE, GameObjectTypes.SATELLITE, 2);
            }
        }, 1000);


    }

    public void landInit() {
        //2 tanks
        //2 rv
        purchase(GameObjectGridFragment.LAND, GameObjectTypes.TANK, 2);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.LAND, GameObjectTypes.RV, 2);
            }
        }, 1000);

    }

    public void seaInit() {
        //1 fac  .. probably should be none in reality.
    }

    public void airInit() {
        //2 mini drones
        purchase(GameObjectGridFragment.AIR, GameObjectTypes.MINI_DRONE, 2);
    }

    public void missileInit() {
        //20 g2g
        //10 gta
        //5 landmines
        purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.G2G, 20);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.GROUND_MINE, 5);
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.G2A, 10);
            }
        }, 1000);
    }

    public AlertDialog getTargetDialog(DialogInterface.OnClickListener targetListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(main);


        builder.setTitle("Target Confirmation");
        builder.setMessage("Target set for " + GameObjectTypes.getTypeName(controller.gameController.currentGameObject.getSubType()) + " - " + controller.gameController.currentGameObject.getKey());
        builder.setCancelable(true);

        builder.setPositiveButton("Take Aim", targetListener);


        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();

        return dialog;
    }

    public AlertDialog getDestinationDialog(DialogInterface.OnClickListener moveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(main);


        builder.setTitle("Destination Confirmation");
        builder.setMessage("Move " + GameObjectTypes.getTypeName(controller.gameController.currentGameObject.getSubType()) + " - " + controller.gameController.currentGameObject.getKey());
        builder.setCancelable(true);

        builder.setPositiveButton("Move It", moveListener);


        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();

        return dialog;
    }


    public AlertDialog getGameMoveDialog(final GameObject gameObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(main);

        controller.gameController.currentGameObject = gameObject;

        builder.setTitle("Game Rules");
        builder.setMessage("Move within the orange grids\nPress on the map to set destination\nYou have 20 seconds to complete it!");
        builder.setCancelable(false);

        builder.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                controller.mapHandler.handleCamera(new LatLng(gameObject.getLatitude(), gameObject.getLongitude()), 45, 0, 20);
                controller.gameController.GAME_STATE = GameController.GAME_OBJECT_MOVE_STATE;
                controller.gameController.gameTimer.startTimer(1000 * 20);
            }
        });


        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();

        return dialog;
    }

    public AlertDialog getMissileTargetDialog(final GameObject gameObject, final GameObject missileObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(main);

        controller.gameController.currentGameObject = gameObject;
        controller.gameController.currentMissileObject = missileObject;

        builder.setTitle("Missile Range");
        builder.setMessage("The range is within the red circle\nPress on the map to set target\nYou have 20 seconds to complete it!");
        builder.setCancelable(false);

        builder.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                controller.mapHandler.handleCamera(new LatLng(gameObject.getLatitude(), gameObject.getLongitude()), 45, 0, 14);
                controller.gameController.GAME_STATE = GameController.GAME_OBJECT_TARGET_STATE;
                controller.gameController.gameTimer.startTimer(1000 * 20);
            }
        });


        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();

        return dialog;
    }

}
