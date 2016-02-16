package mobile.che.com.oddymobstar.chemobile.activity.helper;

import android.os.Handler;

import java.security.NoSuchAlgorithmException;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.INFASTRUCTURE, GameObjectTypes.GARRISON, 1);
            }
        }, 1000);

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
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.LAND, GameObjectTypes.TANK, 2);
            }
        }, 1000);

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
        final Handler handler = new Handler();
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
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                purchase(GameObjectGridFragment.MISSILE, GameObjectTypes.G2A, 10);
            }
        }, 1000);
    }

}
