package mobile.che.com.oddymobstar.chemobile.model;

import android.database.Cursor;

import mobile.che.com.oddymobstar.chemobile.database.DBHelper;


/**
 * Created by root on 25/02/15.
 */
public class Config {

    public static final int BASE = 0;
    public static final int USER = 1;
    public static final int SYSTEM = 2;


    private int id;
    private String name;
    private String value;
    private String markup;
    private boolean visible;
    private int type;

    public Config(Cursor config) {
        setId(config.getInt(config.getColumnIndexOrThrow(DBHelper.CONFIG_ID)));
        setName(config.getString(config.getColumnIndexOrThrow(DBHelper.CONFIG_NAME)));
        setValue(config.getString(config.getColumnIndexOrThrow(DBHelper.CONFIG_VALUE)));
        setType(config.getInt(config.getColumnIndexOrThrow(DBHelper.CONFIG_TYPE)));
        setMarkup(config.getString(config.getColumnIndexOrThrow(DBHelper.CONFIG_MARKUP)));
        setVisible(config.getString(config.getColumnIndexOrThrow(DBHelper.CONFIG_VISIBLE)).equals("Y"));
    }

    public Config(String name, String value, String markup, int type, boolean visible) {
        this.name = name;
        this.value = value;
        this.markup = markup;
        this.type = type;
        this.visible = visible;
    }

    public Config(int id, String name, String value, String markup, int type, boolean visible) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.markup = markup;
        this.type = type;
        this.visible = visible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMarkup() {
        return markup;
    }

    public void setMarkup(String markup) {
        this.markup = markup;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


}
