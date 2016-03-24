package mobile.che.com.oddymobstar.chemobile.util.game;

import android.os.CountDownTimer;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;

/**
 * Created by timmytime on 24/03/16.
 */
public class MapExplosionTimer {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    private CountDownTimer countDownTimer;

    private double radius;

    public MapExplosionTimer(ProjectCheActivity main, ProjectCheController controller){
        this.main = main;
        this.controller = controller;

    }


    public void startTimer(final GameObject gameObject, long duration) {

        radius = gameObject.getImpactRadius() / (duration /1000);

        final double factor = radius;


        countDownTimer = new CountDownTimer(duration, 1000) {

            @Override
            public void onTick(final long millisUntilFinished) {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        controller.mapHandler.circleList.clear();
                        controller.mapHandler.addSphere(gameObject, radius, true);

                        radius += factor;
                    }
                });
            }

            @Override
            public void onFinish() {
            /*    main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      controller.mapHandler.targets.get(gameObject.getKey()).remove();
                    }
                }); */
            }
        };

        countDownTimer.start();


    }

}
