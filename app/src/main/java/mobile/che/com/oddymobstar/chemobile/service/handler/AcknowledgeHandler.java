package mobile.che.com.oddymobstar.chemobile.service.handler;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.util.MessageFactory;
import util.Tags;

/**
 * Created by timmytime on 30/01/16.
 */
public class AcknowledgeHandler extends MessageHandler {

    public interface CheCallbackInterface {
        void send(CheMessage cheMessage);
    }

    private CheCallbackInterface cheCallback;
    private final MessageFactory messageFactory;

    public AcknowledgeHandler(DBHelper dbHelper, MessageFactory messageFactory) {
        super(dbHelper);
        this.messageFactory = messageFactory;
    }

    public void addCheCallback(CheCallbackInterface cheCallback){
        this.cheCallback = cheCallback;
    }

    @Override
    public void handle(CheMessage cheMessage) throws JSONException, NoSuchAlgorithmException {
        cheMessage.setMessage(Tags.ACKNOWLEDGE, messageFactory.createAcknowledge());
        cheMessage.setMessage(Tags.PLAYER, messageFactory.createPlayer());

        cheCallback.send(cheMessage);
    }



}
