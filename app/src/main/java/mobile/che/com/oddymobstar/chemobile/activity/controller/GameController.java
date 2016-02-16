package mobile.che.com.oddymobstar.chemobile.activity.controller;

import java.security.NoSuchAlgorithmException;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.helper.MaterialsHelper;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import util.GameObjectTypes;

/**
 * Created by timmytime on 16/02/16.
 */
public class GameController {

    private final ProjectCheController controller;
    private final ProjectCheActivity main;

    public GameController(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public static int getGameColor(int type){

        switch (type){
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

    public static int getGameColorFlag(int type){
        switch (type){
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

    private void infrastructureInit() {
        //1 garrison
        //2 sattelites
        //2 outposts
        controller.gameController.purchase(GameObjectGridFragment.INFASTRUCTURE, GameObjectTypes.GARRISON, 1);
        controller.gameController.purchase(GameObjectGridFragment.INFASTRUCTURE, GameObjectTypes.OUTPOST, 2);
        controller.gameController.purchase(GameObjectGridFragment.INFASTRUCTURE, GameObjectTypes.SATELLITE, 2);


    }

    private void landInit() {
        //2 tanks
        //2 rv
        controller.gameController.purchase(GameObjectGridFragment.LAND, GameObjectTypes.TANK, 2);
        controller.gameController.purchase(GameObjectGridFragment.LAND, GameObjectTypes.RV, 2);
    }

    private void seaInit() {
       //1 fac  .. probably should be none in reality.
    }

    private void airInit() {
       //2 mini drones
        controller.gameController.purchase(GameObjectGridFragment.AIR, GameObjectTypes.MINI_DRONE, 2);
    }

    private void missileInit() {
        //20 g2g
        //10 gta
        //5 landmines
        controller.gameController.purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.G2G, 20);
        controller.gameController.purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.GROUND_MINE, 5);
        controller.gameController.purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.G2A, 10);
    }


    public void handleFab() {

        Config config = null;

        switch (controller.fragmentHandler.gameFrag.getType()) {
            case GameObjectGridFragment.AIR:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_AIR);
                if (config.getValue().equals("N")) {
                    airInit();
                    controller.dbHelper.updateConfig(config);
                } else {

                }
                break;
            case GameObjectGridFragment.SEA:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_SEA);
                if (config.getValue().equals("N")) {
                    seaInit();
                } else {

                }
                break;
            case GameObjectGridFragment.LAND:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_LAND);
                if (config.getValue().equals("N")) {
                    landInit();
                } else {

                }
                break;
            case GameObjectGridFragment.INFASTRUCTURE:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_INFA);
                if (config.getValue().equals("N")) {
                    infrastructureInit();
                } else {

                }
                break;
            case GameObjectGridFragment.MISSILE:
                config = controller.dbHelper.getConfig(Configuration.START_PURCHASE_MISSILE);
                if (config.getValue().equals("N")) {
                    missileInit();
                } else {

                }
                break;
        }

        if(config.getValue().equals("N")){
            config.setValue("Y");
            controller.dbHelper.updateConfig(config);
        }

    }


}
