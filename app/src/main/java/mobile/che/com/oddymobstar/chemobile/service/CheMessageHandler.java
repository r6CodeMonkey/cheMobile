package mobile.che.com.oddymobstar.chemobile.service;

import message.CheMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;

/**
 * Created by timmytime on 29/01/16.
 */
public class CheMessageHandler {

    private final DBHelper dbHelper;

    public CheMessageHandler(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }
    /*

     going to handler the message
     */

    public void handle(CheMessage cheMessage){

    }

}
