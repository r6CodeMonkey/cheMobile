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
import android.widget.SeekBar;
import android.widget.TextView;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.handler.ConfigurationHandler;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;


/**
 * Created by timmytime on 18/05/15.
 */
public class UserConfigurationAdapter extends CursorAdapter {

    private static int SEEK_BAR_STEP = 60;
    private View seekBarView;
    private ConfigurationHandler configurationHandler;


    public UserConfigurationAdapter(Context context, Cursor cursor, boolean autoRequery, ConfigurationHandler configurationHandler) {
        super(context, cursor, autoRequery);

        this.configurationHandler = configurationHandler;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View v;
        if (cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_NAME)).equals(Configuration.SERVER_LOCATION_HIDE)) {
            v = inflator.inflate(R.layout.config_checkbox_list_item, null);

            CheckBox cb = (CheckBox) v.findViewById(R.id.core_item_name);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    configurationHandler.handleHideUser(isChecked);
                }
            });

        } else {
            v = inflator.inflate(R.layout.config_slider_list_item, null);

            seekBarView = v;

            SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekBar);
            seekBar.setMax(20);
            //every seek interval is 1 minute ie 60 seconds.  so 0 is actually 1...fun. could be 0 is off but that
            //isnt suitable for task.  needs single state ie control of..

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                private int progress = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    this.progress = (progress * SEEK_BAR_STEP) + SEEK_BAR_STEP;

                    TextView tv = (TextView) seekBarView.findViewById(R.id.core_item_name);
                    tv.setText(tv.getText().toString().split("-")[0].trim() + " - " + this.progress + " Seconds");
                    //so just need to make this work....
                    //      Log.d("on progress changed", "progress has changed "+progress+" "+this.progress);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    //on stop we update the configuration.  dont be too clever.
                    //then when we use config to restart sockets etc
                    configurationHandler.handleGPSInterval(progress);
                }
            });
        }


        bindView(v, context, cursor);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if (cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_NAME)).equals(Configuration.SERVER_LOCATION_HIDE)) {

            CheckBox cb = (CheckBox) view.findViewById(R.id.core_item_name);

            cb.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_MARKUP)));
            cb.setChecked(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_VALUE)).equals("Y"));
        } else {
            TextView tv = (TextView) view.findViewById(R.id.core_item_name);

            //convert to time value...ie
            int millseconds = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_VALUE)));
            tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CONFIG_MARKUP)) + " - " + millseconds / 1000 + " Seconds");

            SeekBar pg = (SeekBar) view.findViewById(R.id.seekBar);
            pg.setProgress(((millseconds / 1000) / 60) - 1);

        }

    }
}
