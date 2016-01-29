package mobile.che.com.oddymobstar.chemobile.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import mobile.che.com.oddymobstar.chemobile.activity.handler.ConfigurationHandler;
import mobile.che.com.oddymobstar.chemobile.adapter.ConfigurationAdapter;


/**
 * Created by root on 24/04/15.
 */
public class ConfigurationFragment extends Fragment {


    private Cursor userConfigs;
    private Cursor systemConfigs;

    private ConfigurationHandler configurationHandler;

    public ConfigurationFragment() {

    }

    public void init(ConfigurationHandler configurationHandler, Cursor userConfigs, Cursor systemConfigs) {
        this.configurationHandler = configurationHandler;
        this.userConfigs = userConfigs;
        this.systemConfigs = systemConfigs;

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.configuration_fragment, container, false);


        final ExpandableListView lv = (ExpandableListView) view
                .findViewById(R.id.expandableListView1);

        lv.setAdapter(new ConfigurationAdapter(getActivity(), configurationHandler, userConfigs, systemConfigs));

        //shouldnt hard code but not adding more yet so who cares. it static at mo.
        lv.expandGroup(0);
        lv.expandGroup(1);

        //dont think this is working....needs to be card not fragment?  need to review card perhaps
        //card can be loaded via transaction manager.  i doubt it.
        view.setElevation(12);


        return view;
    }


}
