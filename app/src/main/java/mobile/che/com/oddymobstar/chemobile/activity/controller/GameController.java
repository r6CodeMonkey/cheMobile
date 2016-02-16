package mobile.che.com.oddymobstar.chemobile.activity.controller;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.handler.GameHandler;
import mobile.che.com.oddymobstar.chemobile.activity.helper.GameHelper;

/**
 * Created by timmytime on 16/02/16.
 */
public class GameController {

    public final GameHandler gameHandler;
    public final GameHelper gameHelper;
    private final ProjectCheController controller;
    private final ProjectCheActivity main;

    public GameController(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
        gameHandler = new GameHandler(main, controller);
        gameHelper = new GameHelper(main, controller);
    }


}
