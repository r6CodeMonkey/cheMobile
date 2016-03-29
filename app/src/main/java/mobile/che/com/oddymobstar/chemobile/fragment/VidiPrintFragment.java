package mobile.che.com.oddymobstar.chemobile.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.adapter.VidiPrintAdapter;

/**
 * Created by timmytime on 20/02/16.
 */
public class VidiPrintFragment extends Fragment {

    private CursorAdapter adapter = null;
    private Cursor vidi;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vidiprint_layout, container, false);
        adapter = new VidiPrintAdapter(this.getActivity(), vidi, false);

        ListView lv = (ListView) view.findViewById(R.id.vidiprint_list);
        lv.setDivider(null);
        lv.setDividerHeight(0);

        lv.setAdapter(adapter);


        return view;
    }


    public void setVidi(Cursor vidi) {
        this.vidi = vidi;
    }


    public void refreshAdapter(Cursor cursor) {
        adapter.changeCursor(cursor);
    }

}
