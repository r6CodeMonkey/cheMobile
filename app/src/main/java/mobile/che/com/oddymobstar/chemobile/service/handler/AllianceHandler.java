package mobile.che.com.oddymobstar.chemobile.service.handler;

import android.location.Location;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

import message.Alliance;
import message.CheMessage;
import message.Player;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.model.Message;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import util.Tags;

/**
 * Created by timmytime on 30/01/16.
 */
public class AllianceHandler extends MessageHandler {


    public AllianceHandler(DBHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public void handle(CheMessage cheMessage) throws JSONException {

        Alliance alliance = (Alliance) cheMessage.getMessage(Tags.ALLIANCE);

        switch (alliance.getState()) {
            case Tags.ALLIANCE_CREATE:
                //we can add our alliance.
                switch (alliance.getValue()) {
                    case Tags.SUCCESS:
                        mobile.che.com.oddymobstar.chemobile.model.Alliance newAlliance = new mobile.che.com.oddymobstar.chemobile.model.Alliance();
                        newAlliance.setKey(alliance.getKey());
                        newAlliance.setName(alliance.getName());
                        dbHelper.addAlliance(newAlliance, false);
                        break;
                    case Tags.ERROR:
                        break;
                }
                break;
            case Tags.ALLIANCE_INVITE:
                break;
            case Tags.ALLIANCE_JOIN:
                break;
            case Tags.ALLIANCE_LEAVE:
                break;
            case Tags.ALLIANCE_POST:
                Player player = (Player) cheMessage.getMessage(Tags.PLAYER);
                long time = cheMessage.getTime();

                Message message = new Message();
                message.setAuthor(player.getName());
                message.setMessageKey(alliance.getKey());
                message.setTime(time);
                message.setMessage(alliance.getValue());
                message.setMessageType(Message.ALLIANCE_MESSAGE); //sort of pointless
                message.setMyMessage(player.getKey().equals(dbHelper.getConfig(Configuration.PLAYER_KEY).getValue()) ? "Y" : "N");

                dbHelper.addMessage(message);
                break;
        }

        /*
         need to make this work...simples.
         */
    }


}
