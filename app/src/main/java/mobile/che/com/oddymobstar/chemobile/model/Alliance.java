package mobile.che.com.oddymobstar.chemobile.model;

import android.database.Cursor;

import message.CoreMessage;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;


/**
 * Created by root on 25/02/15.
 */
public class Alliance implements CheModelInterface{

    private String key = "";
    private String name = "";

    public Alliance() {

    }

    public Alliance(Cursor alliance) {
        setKey(alliance.getString(alliance.getColumnIndexOrThrow(DBHelper.ALLIANCE_KEY)));
        setName(alliance.getString(alliance.getColumnIndexOrThrow(DBHelper.ALLIANCE_NAME)));

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void create(CoreMessage coreMessage) {

    }

    @Override
    public CoreMessage getMessage() {
        return null;
    }
}
