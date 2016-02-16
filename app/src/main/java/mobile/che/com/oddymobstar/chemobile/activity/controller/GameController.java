package mobile.che.com.oddymobstar.chemobile.activity.controller;

import java.security.NoSuchAlgorithmException;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;

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

    public void purchase(int type, int subType, int quantity) throws NoSuchAlgorithmException {
        mobile.che.com.oddymobstar.chemobile.model.GameObject gameObject = new mobile.che.com.oddymobstar.chemobile.model.GameObject();
        gameObject.setType(type);
        gameObject.setSubType(subType);
        controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject, controller.locationListener.getCurrentLocation(), quantity));
    }

  /*  public void purchaseGarrison(int quantity) throws NoSuchAlgorithmException {

     //  for (int i = 0; i < 2; i++) {
          gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.LAND);
            gameObject.setSubType(GameObjectTypes.SATELLITE);
         //   controller.messageFactory.purchaseGameObject(gameObject);
            controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject));
            gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.LAND);
            gameObject.setSubType(GameObjectTypes.OUTPOST);
       //     controller.messageFactory.purchaseGameObject(gameObject);
            controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject));

     //   }
    }

    public void purchase() throws NoSuchAlgorithmException {
        for (int i = 0; i < 10; i++) {
            GameObject gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.MISSILE);
            gameObject.setSubType(GameObjectTypes.GROUND_MINE);
            controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject));
        }

        for (int i = 0; i < 50; i++) {
            GameObject gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.MISSILE);
            gameObject.setSubType(GameObjectTypes.G2G);
            controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject));
            gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.MISSILE);
            gameObject.setSubType(GameObjectTypes.G2A);
            controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject));
        }

    }

    public void initLand() throws NoSuchAlgorithmException {
        for (int i = 0; i < 2; i++) {
            GameObject gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.LAND);
            gameObject.setSubType(GameObjectTypes.TANK);
            controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject));
            gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.LAND);
            gameObject.setSubType(GameObjectTypes.SATELLITE);
            controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject));
            gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.LAND);
            gameObject.setSubType(GameObjectTypes.RV);
            controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject));

        }
    }

    public void initAir() throws NoSuchAlgorithmException {

        for(int i=0;i<2;i++){
            GameObject gameObject = new GameObject();
            gameObject.setType(GameObjectGridFragment.AIR);
            gameObject.setSubType(GameObjectTypes.MINI_DRONE);
            controller.cheService.writeToSocket(controller.messageFactory.purchaseGameObject(gameObject));

        }
    }

    public void initSea(){

    }


    public void initGame() throws NoSuchAlgorithmException {

    } */
}
