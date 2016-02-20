package mobile.che.com.oddymobstar.chemobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import mobile.che.com.oddymobstar.chemobile.database.DBHelper;

/**
 * Created by timmytime on 20/02/16.
 */
public class VidiPrintAdapter extends CursorAdapter {

    private final int layout = android.R.layout.simple_list_item_1;

    public VidiPrintAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        final View v = inflator.inflate(layout, null);

        bindView(v, context, cursor);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setTextColor(Color.GREEN);

        textView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MESSAGE_CONTENT)));

    }


}

