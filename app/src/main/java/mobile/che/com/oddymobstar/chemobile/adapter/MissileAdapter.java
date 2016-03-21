package mobile.che.com.oddymobstar.chemobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import util.GameObjectTypes;

/**
 * Created by timmytime on 21/03/16.
 */
public class MissileAdapter extends CursorAdapter {

    private final int layout = android.R.layout.simple_list_item_checked;

    public MissileAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View v = inflator.inflate(layout, null);

        bindView(v, context, cursor);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(String.format("%s - %s",
                GameObjectTypes.getTypeName(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE)))
                , cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MISSILE_KEY))));

    }
}
