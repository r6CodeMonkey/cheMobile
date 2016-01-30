package mobile.che.com.oddymobstar.chemobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.handler.ConfigurationHandler;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;


/**
 * Created by timmytime on 18/05/15.
 */
public class SystemConfigurationAdapter extends CursorAdapter {

    private ConfigurationHandler configurationHandler;

    public SystemConfigurationAdapter(Context context, Cursor cursor, boolean autoRequery, ConfigurationHandler configurationHandler) {
        super(context, cursor, autoRequery);

        this.configurationHandler = configurationHandler;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View v = inflator.inflate(R.layout.config_checkbox_list_item, null);

        CheckBox cb = (CheckBox) v.findViewById(R.id.core_item_name);

        if (cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_NAME)).equals(Configuration.CLEAR_BACKLOG)) {
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //need to call our handler.
                        configurationHandler.handleClearBacklog();
                    }

                }
            });

        } else if (cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_NAME)).equals(Configuration.RESET_SOCKET)) {
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        configurationHandler.handleResetConnection();
                    }

                }
            });

        }


        //need to set state listener...then run actions. dont ust hack the code.

        bindView(v, context, cursor);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        CheckBox cb = (CheckBox) view.findViewById(R.id.core_item_name);


        cb.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_MARKUP)));
        cb.setChecked(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_VALUE)).equals("Y"));


    }
}
