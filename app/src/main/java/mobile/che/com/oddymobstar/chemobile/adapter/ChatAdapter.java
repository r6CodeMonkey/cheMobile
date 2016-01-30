package mobile.che.com.oddymobstar.chemobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.util.widget.ChatBubbleLeft;
import mobile.che.com.oddymobstar.chemobile.util.widget.ChatBubbleRight;


/**
 * Created by root on 03/04/15.
 */
public class ChatAdapter extends CursorAdapter {

    private Context context;
    private int layout = R.layout.chat_row;

    private Map<Integer, View> viewMap = new HashMap<>();


    public ChatAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        final View v = inflator.inflate(layout, null);

        String myMessage = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MY_MESSAGE));

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String myMessage = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MY_MESSAGE));


        ChatBubbleRight bubbleRight;
        ChatBubbleLeft bubbleLeft;


        if (myMessage.equals("Y")) {
            bubbleRight = (ChatBubbleRight) view.findViewById(R.id.message_bubble);
            bubbleRight.setVisibility(View.INVISIBLE);
            bubbleLeft = (ChatBubbleLeft) view.findViewById(R.id.message_bubble_me);

            TextView tv = (TextView) bubbleLeft.findViewById(R.id.message);
            tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MESSAGE_CONTENT)));
            Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.MESSAGE_TIME)));
            tv = (TextView) bubbleLeft.findViewById(R.id.datetime);
            tv.setText(date.toString());
            tv = (TextView) bubbleLeft.findViewById(R.id.from);
            tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MESSAGE_AUTHOR)));

            bubbleLeft.setVisibility(View.VISIBLE);

        } else {
            bubbleLeft = (ChatBubbleLeft) view.findViewById(R.id.message_bubble_me);
            bubbleLeft.setVisibility(View.INVISIBLE);
            bubbleRight = (ChatBubbleRight) view.findViewById(R.id.message_bubble);

            TextView tv = (TextView) bubbleRight.findViewById(R.id.message);
            tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MESSAGE_CONTENT)));
            Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.MESSAGE_TIME)));
            tv = (TextView) bubbleRight.findViewById(R.id.datetime);
            tv.setText(date.toString());
            tv = (TextView) bubbleRight.findViewById(R.id.from);
            tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MESSAGE_AUTHOR)));

            bubbleRight.setVisibility(View.VISIBLE);

        }


    }
}
