package mobile.che.com.oddymobstar.chemobile.activity.helper;

import android.support.v7.app.AlertDialog;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;

/**
 * Created by timmytime on 17/02/16.
 */
public class IntroductionHelper {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;


    public IntroductionHelper(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    /*
      basically just put our popups to help users..messages go in the strings resource file..
     */

    public AlertDialog getAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(main);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setCancelable(true);

        android.support.v7.app.AlertDialog dialog = builder.create();

        return dialog;

    }

}
