package mobile.che.com.oddymobstar.chemobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Date;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.util.graphics.VidiPrintView;

/**
 * Created by timmytime on 20/02/16.
 */
public class VidiPrintAdapter extends CursorAdapter {

    private final int layout = R.layout.vidi_print_row;

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

        final TextView textView = (TextView) view.findViewById(R.id.vidi_print_item);

        Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.MESSAGE_TIME)));

        //need to add date time formatted:
        String formatter = String.format("%s: %s",VidiPrintView.sdf.format(date),  cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MESSAGE_CONTENT)));

        textView.setText(formatter);

    }
    /*
     wrong...basically we want it on a new view only.
     */

}

