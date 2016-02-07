package mobile.che.com.oddymobstar.chemobile.service.handler;

import org.json.JSONException;

import message.Alliance;
import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
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

        Alliance alliance = (Alliance)cheMessage.getMessage(Tags.ALLIANCE);

        switch (alliance.getState()){
            case Tags.ALLIANCE_CREATE:
                //we can add our alliance.
               switch(alliance.getValue()){
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
                break;
        }

        /*
         need to make this work...simples.
         */
    }
}
