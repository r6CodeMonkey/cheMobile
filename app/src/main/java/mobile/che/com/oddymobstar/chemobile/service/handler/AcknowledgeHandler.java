package mobile.che.com.oddymobstar.chemobile.service.handler;

import org.json.JSONException;

import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;

/**
 * Created by timmytime on 30/01/16.
 */
public class AcknowledgeHandler extends MessageHandler {

    public interface CheCallbackInterface {
        void send(CheMessage cheMessage);
    }

    private CheCallbackInterface cheCallback;

    public AcknowledgeHandler(DBHelper dbHelper) {
        super(dbHelper);
    }

    public void addCheCallback(CheCallbackInterface cheCallback){
        this.cheCallback = cheCallback;
    }

    @Override
    public void handle(CheMessage cheMessage) throws JSONException {

    }



}
