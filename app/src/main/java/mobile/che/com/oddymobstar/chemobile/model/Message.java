package mobile.che.com.oddymobstar.chemobile.model;

import android.database.Cursor;

import java.sql.Timestamp;

import mobile.che.com.oddymobstar.chemobile.database.DBHelper;


/**
 * Created by root on 08/04/15.
 */
public class Message {

    public static final String ALLIANCE_MESSAGE = "A";


    private long id;
    private long time;
    private String message;
    private String messageType;
    private String messageKey;
    private String myMessage = "N";
    private String author;

    public Message() {

    }

    public Message(Cursor message) {
        setId(message.getInt(message.getColumnIndexOrThrow(DBHelper.MESSAGE_ID)));
        setTime(message.getLong(message.getColumnIndexOrThrow(DBHelper.MESSAGE_TIME)));
        setMessage(message.getString(message.getColumnIndexOrThrow(DBHelper.MESSAGE_CONTENT)));
        setMessageKey(message.getString(message.getColumnIndexOrThrow(DBHelper.MESSAGE_KEY)));
        setMyMessage(message.getString(message.getColumnIndexOrThrow(DBHelper.MY_MESSAGE)));
        setAuthor(message.getString(message.getColumnIndexOrThrow(DBHelper.MESSAGE_AUTHOR)));
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isMyMessage() {
        return myMessage.equals("Y");
    }

    public void setMyMessage(String myMessage) {
        this.myMessage = myMessage;
    }

    public String getTimeStamp() {
        Timestamp ts = new Timestamp(time);

        return ts.toString();
    }

}
