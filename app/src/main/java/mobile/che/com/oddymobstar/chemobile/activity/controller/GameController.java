package mobile.che.com.oddymobstar.chemobile.activity.controller;

import java.util.ArrayList;
import java.util.List;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.handler.GameHandler;
import mobile.che.com.oddymobstar.chemobile.activity.helper.GameHelper;
import mobile.che.com.oddymobstar.chemobile.activity.listener.GameListener;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.util.game.GameTimer;
import mobile.che.com.oddymobstar.chemobile.util.map.SubUTM;
import mobile.che.com.oddymobstar.chemobile.util.widget.ArmDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.DeployDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.GameObjectActionsDialog;

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

    private final ProjectCheController controller;
    private final ProjectCheActivity main;

    public GameObject currentGameObject; //tracked for states etc.
    public List<SubUTM> currentValidators = new ArrayList<>(); //track whats valid too.

    public DeployDialog deployDialog;
    public ArmDialog armDialog;
    public GameObjectActionsDialog actionsDialog;

    public GameController(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
        gameHandler = new GameHandler(main, controller);
        gameHelper = new GameHelper(main, controller);
        gameListener = new GameListener(main, controller);
        gameTimer = new GameTimer(main, controller);
    }


}
