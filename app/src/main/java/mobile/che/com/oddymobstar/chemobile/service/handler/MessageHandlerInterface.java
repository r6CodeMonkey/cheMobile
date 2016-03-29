package mobile.che.com.oddymobstar.chemobile.service.handler;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

import message.CheMessage;

/**
 * Created by timmytime on 30/01/16.
 */
public interface MessageHandlerInterface {

    void handle(CheMessage cheMessage) throws JSONException, NoSuchAlgorithmException;

}
