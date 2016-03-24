package mobile.che.com.oddymobstar.chemobile.activity.controller;

import java.util.ArrayList;
import java.util.List;

import factory.GameObjectRulesFactory;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.handler.GameHandler;
import mobile.che.com.oddymobstar.chemobile.activity.helper.GameHelper;
import mobile.che.com.oddymobstar.chemobile.activity.listener.GameListener;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.util.game.GameTimer;
import mobile.che.com.oddymobstar.chemobile.util.game.MapExplosionTimer;
import mobile.che.com.oddymobstar.chemobile.util.widget.ArmDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.DeployDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.GameObjectActionsDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.MissileArmDialog;
import util.map.SubUTM;

/**
 * Created by timmytime on 16/02/16.
 */
public class GameController {

    public static final int DEFAULT_STATE = -1;
    public static final int GAME_OBJECT_MOVE_STATE = 0;
    public static final int GAME_OBJECT_TARGET_STATE = 1;

    public static int GAME_STATE = DEFAULT_STATE;


    public final GameHandler gameHandler;
    public final GameHelper gameHelper;
    public final GameListener gameListener;

    public final GameTimer gameTimer;
    public final MapExplosionTimer mapExplosionTimer;


    public GameObject currentGameObject; //tracked for states etc.
    public GameObject currentMissileObject;
    public List<SubUTM> currentValidators = new ArrayList<>(); //track whats valid too.

    public DeployDialog deployDialog;
    public ArmDialog armDialog;
    public GameObjectActionsDialog actionsDialog;
    public MissileArmDialog missileArmDialog;

    public GameController(ProjectCheActivity main, ProjectCheController controller) {
        gameHandler = new GameHandler(main, controller);
        gameHelper = new GameHelper(main, controller);
        gameListener = new GameListener(main, controller);
        gameTimer = new GameTimer(main, controller);
        mapExplosionTimer = new MapExplosionTimer(main, controller);
    }


}
