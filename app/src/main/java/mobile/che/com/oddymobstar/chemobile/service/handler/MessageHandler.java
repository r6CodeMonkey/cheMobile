package mobile.che.com.oddymobstar.chemobile.service.handler;

import mobile.che.com.oddymobstar.chemobile.database.DBHelper;

/**
 * Created by timmytime on 30/01/16.
 */
public abstract class MessageHandler implements MessageHandlerInterface {

    protected final DBHelper dbHelper;

    public MessageHandler(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }
}
