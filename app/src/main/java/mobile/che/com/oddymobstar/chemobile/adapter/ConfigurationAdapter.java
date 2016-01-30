package mobile.che.com.oddymobstar.chemobile.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.handler.ConfigurationHandler;


/**
 * Created by root on 24/04/15.
 */
public class ConfigurationAdapter extends BaseExpandableListAdapter {

    public static final int USER_CONFIGS = 0;
    public static final int SYS_CONFIGS = 1;
    private Context context;
    private List<String> groups = new ArrayList<>();
    private Cursor userConfigs;
    private Cursor systemConfigs;

    private UserConfigurationAdapter userConfigurationAdapter;
    private SystemConfigurationAdapter systemConfigurationAdapter;

    public ConfigurationAdapter(Context context, ConfigurationHandler configurationHandler, Cursor userConfigs, Cursor systemConfigs) {
        this.context = context;

        this.userConfigs = userConfigs;
        this.systemConfigs = systemConfigs;

        this.userConfigurationAdapter = new UserConfigurationAdapter(context, userConfigs, true, configurationHandler);
        this.systemConfigurationAdapter = new SystemConfigurationAdapter(context, systemConfigs, true, configurationHandler);

        groups.add("User Configuration");
        groups.add("System Configuration");

    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        switch (groupPosition) {
            case USER_CONFIGS:
                return userConfigs.getCount();
            case SYS_CONFIGS:
                return systemConfigs.getCount();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.group_list_item, null);
        }
        TextView groupText = (TextView) convertView.findViewById(R.id.item_title);
        groupText.setText(groups.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        switch (groupPosition) {
            case USER_CONFIGS:
                userConfigs.moveToPosition(childPosition);
                convertView = userConfigurationAdapter.newView(context, userConfigs, parent);
                break;
            case SYS_CONFIGS:
                systemConfigs.moveToPosition(childPosition);
                convertView = systemConfigurationAdapter.newView(context, systemConfigs, parent);
                break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
