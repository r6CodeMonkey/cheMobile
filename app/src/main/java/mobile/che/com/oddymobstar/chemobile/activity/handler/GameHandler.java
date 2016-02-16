package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.content.DialogInterface;

import java.security.NoSuchAlgorithmException;

import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.util.widget.DeployDialog;

/**
 * Created by timmytime on 16/02/16.
 */
public class GameHandler {

    private final ProjectCheController controller;
    private final ProjectCheActivity main;

    public GameHandler(ProjectCheActivity main, ProjectCheController controller){
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


    public void deployDialog(String action, String title, String key){
        //add a popup to saying do you wish to deploy here.....
        controller.deployDialog =
                DeployDialog.newInstance(action, title, key,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                try {
                                    handleDeploy(controller.deployDialog.getGameObjectKey());
                                } catch (NoSuchAlgorithmException e) {

                                }
                            }
                        }, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                //
                                dialog.dismiss();
                            }
                        });
        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.deployDialog.show(transaction, "dialog");
    }


}
