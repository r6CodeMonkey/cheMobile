package mobile.che.com.oddymobstar.chemobile.service.handler;

import org.json.JSONException;

import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;

/**
 * Created by timmytime on 30/01/16.
 */
public class GameObjectHandler extends MessageHandler {

    private CheCallbackInterface cheCallback;

    public GameObjectHandler(DBHelper dbHelper) {
        super(dbHelper);
    }

    //probably not required anyway leave here.
    public void addCheCallback(CheCallbackInterface cheCallback) {
        this.cheCallback = cheCallback;
    }

    @Override
    public void handle(CheMessage cheMessage) throws JSONException {

    }
}
