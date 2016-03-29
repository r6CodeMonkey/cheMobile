package mobile.che.com.oddymobstar.chemobile.util.game;

import android.os.CountDownTimer;
import android.util.Log;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Polygon;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.GameController;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;

/**
 * Created by timmytime on 18/02/16.
 */
public class GameTimer {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    private CountDownTimer countDownTimer;

    public GameTimer(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public void stopTimer(boolean cancel) {

        if (cancel) {
            countDownTimer.cancel();
        }

        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("stop timer", "should stop timer");
                controller.materialsHelper.gameTimer.setText("");
                controller.gameController.GAME_STATE = GameController.DEFAULT_STATE;  //timed out basically.
                controller.gameController.currentValidators.clear();
                for (Polygon polygon : controller.gameController.gameHandler.validatorGrids) {
                    polygon.remove();
                }
                for (Circle circle : controller.mapHandler.circleList) {
                    circle.remove();
                }

                controller.gameController.gameHandler.validatorGrids.clear();
                controller.mapHandler.circleList.clear();

            }
        });

    }

    public void startTimer(long duration) {


        countDownTimer = new CountDownTimer(duration, 1000) {

            @Override
            public void onTick(final long millisUntilFinished) {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.materialsHelper.gameTimer.startAnimation(controller.materialsHelper.blinkAnimation);
                        controller.materialsHelper.gameTimer.setText(String.valueOf(millisUntilFinished / 1000));
                    }
                });
            }

            @Override
            public void onFinish() {
                stopTimer(false);
            }
        };

        countDownTimer.start();


    }

}
