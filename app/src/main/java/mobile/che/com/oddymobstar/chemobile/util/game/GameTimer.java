package mobile.che.com.oddymobstar.chemobile.util.game;

import android.os.CountDownTimer;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.GameController;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;

/**
 * Created by timmytime on 18/02/16.
 */
public class GameTimer {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    public GameTimer(ProjectCheActivity main, ProjectCheController controller){
        this.main = main;
        this.controller = controller;
    }

    public void stopTimer(){
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controller.materialsHelper.gameTimer.setText("");
                controller.gameController.GAME_STATE = GameController.DEFAULT_STATE;  //timed out basically.
                controller.gameController.currentValidators.clear();
            }
        });

    }

    public void startTimer(long duration){


        new CountDownTimer(duration, 1000) {

            @Override
            public void onTick(final long millisUntilFinished) {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.materialsHelper.gameTimer.setText(String.valueOf(millisUntilFinished/1000));
                    }
                });
            }

            @Override
            public void onFinish() {
               stopTimer();
            }
        }.start();




    }

}
