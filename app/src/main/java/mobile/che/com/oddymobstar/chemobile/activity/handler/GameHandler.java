package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.adapter.ArmExplosiveAdapter;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.map.GameMoveGridCreator;
import mobile.che.com.oddymobstar.chemobile.util.map.SubUTM;
import mobile.che.com.oddymobstar.chemobile.util.map.UTM;
import mobile.che.com.oddymobstar.chemobile.util.map.UTMGridCreator;
import mobile.che.com.oddymobstar.chemobile.util.widget.ArmDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.DeployDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.GameObjectActionsDialog;
import util.GameObjectTypes;

/**
 * Created by timmytime on 16/02/16.
 */
public class GameHandler {

    private final ProjectCheController controller;
    private final ProjectCheActivity main;

    public GameHandler(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public void handleDeploy(String gameObjectKey) throws NoSuchAlgorithmException {

        final CheMessage cheMessage = controller.messageFactory.createDeploy(controller.dbHelper.getGameObject(gameObjectKey), controller.locationListener.getCurrentLocation());

        new Thread(new Runnable() {
            @Override
            public void run() {
                controller.cheService.writeToSocket(cheMessage);
            }
        }).start();

    }

    public void handleArm(String gameObjectKey, Cursor gameObject) throws NoSuchAlgorithmException {

        final CheMessage cheMessage = controller.messageFactory.armExplosive(new GameObject(gameObject), controller.dbHelper.getGameObject(gameObjectKey), controller.locationListener.getCurrentLocation());

        new Thread(new Runnable() {
            @Override
            public void run() {
                controller.cheService.writeToSocket(cheMessage);
            }
        }).start();

    }

    public void armDialog(String object, String key, ArmExplosiveAdapter adapter){
        controller.gameController.armDialog =
                ArmDialog.newInstance(object, key, adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                try {
                                    handleArm(controller.gameController.armDialog.getGameObjectKey(), controller.gameController.armDialog.getSelectItem());
                                } catch (NoSuchAlgorithmException e) {

                                }
                            }
                        }
                        , new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                //
                                dialog.dismiss();
                            }
                        });
        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.gameController.armDialog.show(transaction, "dialog");
    }

    public void actionsDialog(String key, final int type){
        controller.gameController.actionsDialog =
                GameObjectActionsDialog.newInstance(type, key,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                              //positive action
                                dialog.dismiss();
                                switch (type){
                                    case GameObjectGridFragment.INFASTRUCTURE: //repair
                                        handleRepair(controller.gameController.actionsDialog.getGameObjectKey());
                                        break;
                                    case GameObjectGridFragment.AIR: //take off
                                        handleTakeOff(controller.gameController.actionsDialog.getGameObjectKey());
                                        break;
                                    default: //land and sea move
                                        handleGameObjectMove(controller.gameController.actionsDialog.getGameObjectKey());
                                        break;
                                }
                            }
                        }
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                //negative action
                                dialog.dismiss();
                                switch (type) {
                                    case GameObjectGridFragment.AIR:  //its land.
                                        handleLanding(controller.gameController.actionsDialog.getGameObjectKey());
                                        break;
                                    default: //land and sea stop
                                        handleStop(controller.gameController.actionsDialog.getGameObjectKey());
                                        break;
                                }
                            }
                        }
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                //neutral action
                                dialog.dismiss();
                                handleGameObjectTarget(controller.gameController.actionsDialog.getGameObjectKey());
                           }
                        }
                        , new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                //
                                dialog.dismiss();
                            }
                        });
        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.gameController.actionsDialog.show(transaction, "dialog");
    }


    public void deployDialog(String action, String title, String key) {
        //add a popup to saying do you wish to deploy here.....
        controller.gameController.deployDialog =
                DeployDialog.newInstance(action, title, key,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                try {
                                    handleDeploy(controller.gameController.deployDialog.getGameObjectKey());
                                } catch (NoSuchAlgorithmException e) {

                                }
                            }
                        }
                        , new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                //
                                dialog.dismiss();
                            }
                        });
        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.gameController.deployDialog.show(transaction, "dialog");
    }

    public void handleGameObjectMove(final String key){

        final GameObject gameObject = controller.dbHelper.getGameObject(key);

        Map<UTM, List<SubUTM>> mapGridInfo = GameMoveGridCreator.get3by3Grid(gameObject);

        //we need to grab out each UTM, and its children.
        Set<UTM> keys = mapGridInfo.keySet();

        for(UTM utm : keys){
            //grab our sub utm list...
            List<SubUTM> subUTMs = mapGridInfo.get(utm);

            PolygonOptions utmOptions = UTMGridCreator.getUTMGrid(utm).strokeColor(main.getResources().getColor(android.R.color.holo_purple));

            for(SubUTM subUTM : subUTMs) {
                //
                PolygonOptions subUtmOptions = UTMGridCreator.getSubUTMGrid(subUTM, utmOptions).strokeColor(main.getResources().getColor(android.R.color.holo_orange_dark));
                controller.mapHelper.getMap().addPolygon(subUtmOptions);
            }

        }

        controller.mapHandler.handleCamera(new LatLng(gameObject.getLatitude(), gameObject.getLongitude()), 45, 0, 10);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                controller.gameController.gameHelper.getGameMoveDialog(gameObject).show();
            }
        }, 3000);

    }

    public void handleGameObjectTarget(String key){

    }

    public void handleRepair(String key){

    }

    public void handleTakeOff(String key){

    }

    public void handleLanding(String key){

    }

    public void handleStop(String key){

    }


    public void handleFab() {

        Config config = null;

        switch (controller.fragmentHandler.gameFrag.getType()) {
            case GameObjectGridFragment.AIR:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_AIR);
                if (config.getValue().equals("N")) {
                    controller.gameController.gameHelper.airInit();
                    controller.dbHelper.updateConfig(config);
                } else {

                }
                break;
            case GameObjectGridFragment.SEA:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_SEA);
                if (config.getValue().equals("N")) {
                    controller.gameController.gameHelper.seaInit();
                } else {

                }
                break;
            case GameObjectGridFragment.LAND:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_LAND);
                if (config.getValue().equals("N")) {
                    controller.gameController.gameHelper.landInit();
                } else {

                }
                break;
            case GameObjectGridFragment.INFASTRUCTURE:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_INFA);
                if (config.getValue().equals("N")) {
                    controller.gameController.gameHelper.infrastructureInit();
                } else {

                }
                break;
            case GameObjectGridFragment.MISSILE:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_MISSILE);
                if (config.getValue().equals("N")) {
                    controller.gameController.gameHelper.missileInit();
                } else {

                }
                break;
        }

        if (config.getValue().equals("N")) {
            config.setValue("Y");
            controller.dbHelper.updateConfig(config);
        }

    }


}
