package mobile.che.com.oddymobstar.chemobile.activity.helper;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

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
        }, 2500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.INFASTRUCTURE, GameObjectTypes.AIRPORT, 2);
            }
        }, 3500);

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
        purchase(GameObjectGridFragment.SEA, GameObjectTypes.CARRIER, 1);
    }

    public void airInit() {
        //2 mini drones
        purchase(GameObjectGridFragment.AIR, GameObjectTypes.MINI_DRONE, 2);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.AIR, GameObjectTypes.FIGHTER, 1);
            }
        }, 1000);
    }

    public void missileInit() {
        //20 g2g
        //10 gta
        final Handler handler = new Handler();
        //5 landmines
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.G2G, 20);
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.GROUND_MINE, 5);
            }
        }, 3500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.G2A, 10);
            }
        }, 6000);
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

    public AlertDialog getSatelliteListenerDialog(final GameObject gameObject, boolean stop){
        AlertDialog.Builder builder = new AlertDialog.Builder(main);

        controller.gameController.currentGameObject = gameObject;

        builder.setTitle("Satellite Range");
        builder.setMessage(stop ? "Unregistering from purple grids" : "Will be registered in each purple grid.\nCan identify objects moving within");
        builder.setCancelable(false);

        builder.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.gameController.currentValidators.clear();
                        for (Polygon polygon : controller.gameController.gameHandler.validatorGrids) {
                            polygon.remove();
                        }

                        controller.gameController.gameHandler.validatorGrids.clear();
                    }
                });

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

    public AlertDialog getTakeOffDialog(final GameObject gameObject){
        AlertDialog.Builder builder = new AlertDialog.Builder(main);

        controller.gameController.currentGameObject = gameObject;

        builder.setTitle("Flight Range");
        builder.setMessage("The range is within the red circle\nSelect a point to fly out to\nYou have 20 seconds to complete it!");
        builder.setCancelable(false);

        builder.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                controller.gameController.GAME_STATE = GameController.GAME_OBJECT_FLYING_STATE;
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


    public AlertDialog getSelectBaseDestinationListener(){
        AlertDialog.Builder builder = new AlertDialog.Builder(main);

        builder.setTitle("Flight Instructions");
        builder.setMessage("Select an existing base to fly to.\nYou have 20 seconds to complete it!");

        builder.setCancelable(false);

        builder.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        controller.gameController.GAME_STATE = GameController.GAME_OBJECT_FLY_TO_OTHER_BASE_STATE;
                        controller.gameController.gameTimer.startTimer(1000 * 20);
                    }
                });

                android.support.v7.app.AlertDialog dialog = builder.create();

        return dialog;

    };


    public AlertDialog getFlightTypeDialog(DialogInterface.OnClickListener roundTripListener, DialogInterface.OnClickListener transferListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(main);

        builder.setTitle("Flight Type");
        builder.setMessage("You can fly to another base within your maximum range, or carry out a round trip and attack targets along the way");

        builder.setCancelable(false);

        builder.setPositiveButton("Transfer Flight", transferListener);
        builder.setNeutralButton("Round Trip", roundTripListener);

        android.support.v7.app.AlertDialog dialog = builder.create();

        return dialog;

    };

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
