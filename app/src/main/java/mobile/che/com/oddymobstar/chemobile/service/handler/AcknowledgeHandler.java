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

    private final MessageFactory messageFactory;
    private CheCallbackInterface cheCallback;

    public AcknowledgeHandler(DBHelper dbHelper, MessageFactory messageFactory) {
        super(dbHelper);
        this.messageFactory = messageFactory;
    }

    public void addCheCallback(CheCallbackInterface cheCallback) {
        this.cheCallback = cheCallback;
    }

    @Override
    public void handle(CheMessage cheMessage) throws JSONException, NoSuchAlgorithmException {

        CheMessage temp = messageFactory.createCheMessage();

        temp.setMessage(Tags.CHE_ACKNOWLEDGE, messageFactory.createCheAcknowledge(cheMessage.getMessage(Tags.CHE_ACKNOWLEDGE).getString(Tags.CHE_ACK_ID)).getContents());

        temp.setMessage(Tags.ACKNOWLEDGE, messageFactory.createAcknowledge());
        temp.setMessage(Tags.PLAYER, messageFactory.createPlayer());


        cheCallback.send(temp);
    }


}
