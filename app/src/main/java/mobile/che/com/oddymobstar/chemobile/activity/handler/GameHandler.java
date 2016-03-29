package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.adapter.ArmExplosiveAdapter;
import mobile.che.com.oddymobstar.chemobile.adapter.MissileAdapter;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.map.UTMGridCreator;
import mobile.che.com.oddymobstar.chemobile.util.widget.ArmDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.DeployDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.GameObjectActionsDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.MissileArmDialog;
import util.GameObjectTypes;
import util.Tags;
import util.map.GridCreator;
import util.map.SubUTM;
import util.map.UTM;


/**
 * Created by timmytime on 16/02/16.
 */
public class GameHandler {

    private final ProjectCheController controller;
    private final ProjectCheActivity main;

    //need to keep track of our game object added grids, and remove them once move carried out.
    public final List<Polygon> validatorGrids = new ArrayList<>();

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

    public void armDialog(String object, String key, ArmExplosiveAdapter adapter) {

        if(adapter.getCount() > 0) {

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
        }else{
            Toast.makeText(main, "No Hosts Deployed", Toast.LENGTH_SHORT).show();
        }
    }

    public void actionsDialog(String key, final int type) {

        GameObject gameObject = controller.dbHelper.getGameObject(key);

        boolean targetSet = controller.dbHelper.hasTargetSet(key);



        String title = "";
        String title2 = "";
        String title3 = "";

        DialogInterface.OnClickListener listener = null;
        DialogInterface.OnClickListener listener2 = null;
        DialogInterface.OnClickListener listener3 = null;

        switch (type){

            case GameObjectGridFragment.INFASTRUCTURE:
                 if(!gameObject.getStatus().equals(Tags.GAME_OBJECT_REPAIR) && gameObject.getStrength() < gameObject.getMaxStrength()) {
                    title3 = GameObjectActionsDialog.REPAIR;  //could have a reinforce too.  same thing i guess.
                    listener3 = controller.gameController.gameListener.getRepairListener();
                }
                //as long as we are not a satellite
                if(gameObject.getSubType() != GameObjectTypes.SATELLITE) {
                    if (targetSet) {
                        title = GameObjectActionsDialog.LAUNCH;
                        listener = controller.gameController.gameListener.getLaunchListener();
                        title2 = GameObjectActionsDialog.CANCEL;
                        listener2 = controller.gameController.gameListener.getCancelTargetListener();
                    } else {
                        title = GameObjectActionsDialog.TARGET;
                        listener = controller.gameController.gameListener.getTargetListener();
                    }
                }
                break;
            case GameObjectGridFragment.LAND:
                //rules.  what is our status?
                switch (gameObject.getStatus()){
                    case Tags.GAME_OBJECT_IS_MOVING:
                        if(targetSet){
                            title = GameObjectActionsDialog.LAUNCH;
                            listener = controller.gameController.gameListener.getLaunchListener();
                            title2 = GameObjectActionsDialog.CANCEL;
                            listener2 = controller.gameController.gameListener.getCancelTargetListener();
                        }else{
                            title = GameObjectActionsDialog.TARGET;
                            listener = controller.gameController.gameListener.getTargetListener();
                        }
                        break;
                    case Tags.GAME_OBJECT_IS_FIXED:
                        title3 = GameObjectActionsDialog.MOVE;
                        listener3 = controller.gameController.gameListener.getMoveListener(type);
                        if(targetSet){
                            title = GameObjectActionsDialog.LAUNCH;
                            listener = controller.gameController.gameListener.getLaunchListener();
                            title2 = GameObjectActionsDialog.CANCEL;
                            listener2 = controller.gameController.gameListener.getCancelTargetListener();
                        }else{
                            title = GameObjectActionsDialog.TARGET;
                            listener = controller.gameController.gameListener.getTargetListener();
                        }
                        break;
                }
                break;
            case GameObjectGridFragment.SEA:
                switch (gameObject.getStatus()){
                    case Tags.GAME_OBJECT_IS_MOVING:

                        if(targetSet){
                            title = GameObjectActionsDialog.LAUNCH;
                            listener = controller.gameController.gameListener.getLaunchListener();
                            title2 = GameObjectActionsDialog.CANCEL;
                            listener2 = controller.gameController.gameListener.getCancelTargetListener();
                        }else{
                            title = GameObjectActionsDialog.TARGET;
                            listener = controller.gameController.gameListener.getTargetListener();
                        }

                        break;
                    case Tags.GAME_OBJECT_IS_FIXED:
                        title3 = GameObjectActionsDialog.MOVE;
                        listener3 = controller.gameController.gameListener.getMoveListener(type);
                        if(targetSet){
                            title = GameObjectActionsDialog.LAUNCH;
                            listener = controller.gameController.gameListener.getLaunchListener();
                            title2 = GameObjectActionsDialog.CANCEL;
                            listener2 = controller.gameController.gameListener.getCancelTargetListener();
                        }else{
                            title = GameObjectActionsDialog.TARGET;
                            listener = controller.gameController.gameListener.getTargetListener();
                        }
                    break;
                }
                break;
            case GameObjectGridFragment.AIR:  //really need a takeoff too.  ie move, take off, land etc....anyway.  this can go later.
                switch (gameObject.getStatus()){
                    case Tags.GAME_OBJECT_IS_MOVING:
                        title3 = GameObjectActionsDialog.LAND;
                        listener3 = controller.gameController.gameListener.getLandingListener();
                        if(targetSet){
                            title = GameObjectActionsDialog.LAUNCH;
                            listener = controller.gameController.gameListener.getLaunchListener();
                            title2 = GameObjectActionsDialog.CANCEL;
                            listener2 = controller.gameController.gameListener.getCancelTargetListener();
                        }else{
                            title = GameObjectActionsDialog.TARGET;
                            listener = controller.gameController.gameListener.getTargetListener();
                        }
                        break;
                    case Tags.GAME_OBJECT_IS_FIXED:
                        title3 = GameObjectActionsDialog.MOVE;
                        listener3 = controller.gameController.gameListener.getMoveListener(type);
                        if(targetSet){
                            title = GameObjectActionsDialog.LAUNCH;
                            listener = controller.gameController.gameListener.getLaunchListener();
                            title2 = GameObjectActionsDialog.CANCEL;
                            listener2 = controller.gameController.gameListener.getCancelTargetListener();
                        }else{
                            title = GameObjectActionsDialog.TARGET;
                            listener = controller.gameController.gameListener.getTargetListener();
                        }
                        break;
                }
                break;

        }


        controller.gameController.actionsDialog =
                GameObjectActionsDialog.newInstance(key,title, title2, title3, listener, listener2, listener3

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

    public void handleGameObjectMove(final String key) {

        final GameObject gameObject = controller.dbHelper.getGameObject(key);

        Map<UTM, List<SubUTM>> mapGridInfo = new GridCreator().getAndroidGrids(3, gameObject.getLatitude(), gameObject.getLongitude());

        //we need to grab out each UTM, and its children.
        Set<UTM> keys = mapGridInfo.keySet();

        for (UTM utm : keys) {
            //grab our sub utm list...
            List<SubUTM> subUTMs = mapGridInfo.get(utm);

            PolygonOptions utmOptions = UTMGridCreator.getUTMGrid(utm).strokeColor(main.getResources().getColor(android.R.color.holo_purple));

            for (SubUTM subUTM : subUTMs) {
                controller.gameController.currentValidators.add(subUTM);
                //
                PolygonOptions subUtmOptions = UTMGridCreator.getSubUTMGrid(subUTM, utmOptions).strokeColor(main.getResources().getColor(android.R.color.holo_orange_dark));
                validatorGrids.add(controller.mapHelper.getMap().addPolygon(subUtmOptions));
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

    public void handleGameObjectTarget(final String key) {
        //if no missiles, basically cant do anything.  can review if we need to capture it in future.
        Cursor availableMissiles = controller.dbHelper.getAvailableMissiles(key);
        if(availableMissiles.getCount() > 0) {
            controller.gameController.missileArmDialog =
                    MissileArmDialog.newInstance(new MissileAdapter(main, availableMissiles, true),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, final int which) {
                                        handleMissileLoad(key, controller.gameController.missileArmDialog.getSelectItem());
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

            controller.gameController.missileArmDialog.show(transaction, "dialog");
        }else{
            Toast.makeText(main, "No Missiles Available", Toast.LENGTH_SHORT).show();
        }

    }

    public void handleMissileLoad(String key, Cursor missile){

        final GameObject gameObject = controller.dbHelper.getGameObject(key);
        final GameObject missileObject = controller.dbHelper.getGameObject(missile.getString(missile.getColumnIndexOrThrow(DBHelper.MISSILE_KEY)));
        controller.mapHandler.addSphere(gameObject, missileObject.getRange(), false);

        //scale this.  ie if missile range < 5000 needs to be more like 11....ie  .. ie per metre 10 works better.  so

        controller.mapHandler.handleCamera(new LatLng(gameObject.getLatitude(), gameObject.getLongitude()), 45, 0, (float)(missileObject.getRange() / 500.0f));

        //now we need to delay slightly, and then start timer and dialog.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                controller.gameController.gameHelper.getMissileTargetDialog(gameObject, missileObject).show();
            }
        }, 3000);
    }

    public void handleRepair(final String key) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.cheService.writeToSocket(controller.messageFactory.createRepairMessage(controller.dbHelper.getGameObject(key), controller.locationListener.getCurrentLocation()));
                } catch (NoSuchAlgorithmException e) {

                }
            }
        }).start();

    }

    //think there is nothing to do here
    public void handleTakeoff(String key) {

    }

    public void handleLanding(String key) {

    }

    public void handleStop(final String key) {

        //send a message to the server to tell it to stop.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.cheService.writeToSocket(controller.messageFactory.stopGameObject(controller.dbHelper.getGameObject(key), controller.locationListener.getCurrentLocation()));
                } catch (NoSuchAlgorithmException e) {

                }
            }
        }).start();

    }

    public void handleCancelTarget(final String key){

        //to do....lol.  yes need to fix this as well.

    }

    //we are assuming we can only target one thing at a time.  makes sense at present.
    public void handleLaunch(final String key){

        GameObject gameObject = null;  //really we want to have more than one target set....well means changing buttons so leave at present.  enough other things to do.

        Cursor missiles = controller.dbHelper.getLaunchMissiles(key);

        while (missiles.moveToNext()){
            gameObject = controller.dbHelper.getGameObject(missiles.getString(missiles.getColumnIndexOrThrow(DBHelper.MISSILE_KEY)));
        }

        missiles.close();

        controller.mapHandler.removeSphere(gameObject);

        final GameObject missile = gameObject;
        //we need to send a missile message now.  do we allow multiple targets?  we could.   if so, we need a listener.  for moment launch 1.  then refactor.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.cheService.writeToSocket(controller.messageFactory.getMissileLaunch(missile, controller.locationListener.getCurrentLocation()));

                } catch (NoSuchAlgorithmException e) {

                }
            }
        }).start();

    }

    public void handleTarget(final LatLng latLng){

        final GameObject gameObject = controller.gameController.currentGameObject;
        final GameObject explosive = controller.gameController.currentMissileObject;
        //validate it here.

        Location start = new Location("");
        start.setLatitude(explosive.getLatitude());
        start.setLongitude(explosive.getLongitude());
        Location target = new Location("");
        target.setLatitude(latLng.latitude);
        target.setLongitude(latLng.longitude);


        if(start.distanceTo(target) > explosive.getRange()){
            Toast.makeText(main, "Target Outside Range!", Toast.LENGTH_SHORT).show();
        }else {

            //as the other.  set a target Dialog
            controller.gameController.gameHelper.getTargetDialog(
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            ;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        controller.cheService.writeToSocket(controller.messageFactory.setTarget(gameObject, explosive, latLng, controller.locationListener.getCurrentLocation()));

                                    } catch (NoSuchAlgorithmException e) {

                                    }
                                }
                            }).start();

                        }
                    }
            ).show();
        }
    }


    public void handleMoveDestination(final LatLng latLng) {
        //1: popup to say this is the chosen location....then send message.
        controller.gameController.gameHelper.getDestinationDialog(
                 new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        final List<SubUTM> validators = controller.gameController.currentValidators;
                        final GameObject gameObject = controller.gameController.currentGameObject;

                        controller.mapHandler.handleCamera(new LatLng(controller.gameController.currentGameObject.getLatitude(), controller.gameController.currentGameObject.getLongitude()), 45, 0, 20);

                        //so new message plus tag.  ie we are moving game object.  so make ticket, + set the validators for it.  simples.
                        //then if its success, we remove map marker as its active...and change the flag, else we say, sorry, its not a valid move...
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    controller.cheService.writeToSocket(controller.messageFactory.moveGameObject(gameObject, validators, latLng, controller.locationListener.getCurrentLocation()));

                                } catch (NoSuchAlgorithmException e) {

                                }
                            }
                        }).start();


                    }
                }
        ).show();
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
