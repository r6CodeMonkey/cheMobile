package mobile.che.com.oddymobstar.chemobile.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.adapter.CoreAdapter;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.util.widget.CreateView;


/**
 * Created by root on 14/04/15.
 */
public class GridFragment extends Fragment {


    /*
      can contain...Alliances or Topics.
     */
    public static final int MY_ALLIANCES = 0;


    private int type = MY_ALLIANCES;


    private AdapterView.OnItemClickListener onClickListener = null;
    private CursorAdapter adapter = null;
    private DBHelper dbHelper;
    private GridView gridView;

    private CreateView hiddenCreateView;


    public GridFragment() {
        setRetainInstance(true);
    }

    public int getType() {
        return type;
    }

    public void init(int type, AdapterView.OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.type = type;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_fragment, container, false);


        if (dbHelper == null) {
            dbHelper = DBHelper.getInstance(getActivity());
        }


        gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(onClickListener);

        //  gridView.setFastScrollEnabled(true);
        //  gridView.setFastScrollAlwaysVisible(true);

        gridView.setAdapter(adapter);

        hiddenCreateView = (CreateView) view.findViewById(R.id.create_alliance);
        hiddenCreateView.setVisibility(View.GONE);
        hiddenCreateView.setElevation(16);

        Button button = (Button) hiddenCreateView.findViewById(R.id.list_create);
        button.setTypeface(ProjectCheActivity.getFont());


        new LoadCursors().execute("");


        return view;
    }

    private Cursor getCursor(int type) {

        switch (type) {
            case MY_ALLIANCES:
                return dbHelper.getAlliances();
        }
        return null;
    }

    private CursorAdapter getCursorAdapter(int type, Cursor cursor) {

        return new CoreAdapter(getActivity(), cursor, true, type);

    }

    public void refreshAdapter() {
        if (adapter != null) {
            switch (type) {
                case MY_ALLIANCES:
                    adapter.changeCursor(dbHelper.getAlliances());
                    break;
            }


        }
    }

    public ListAdapter getListAdapter() {
        return gridView.getAdapter();
    }

    public void clearAdapter() {
        gridView.setAdapter(null);
        adapter = null;
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            adapter.getCursor().close();
        } catch (Exception e) {

        }
    }

    public CreateView getHiddenCreateView() {
        return hiddenCreateView;
    }

    private class LoadCursors extends AsyncTask<String, Void, String> {

        private Cursor cursor;

        @Override
        protected String doInBackground(String... params) {

            if (dbHelper == null) {
                dbHelper = DBHelper.getInstance(getActivity());
            }

            cursor = getCursor(type);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            adapter = getCursorAdapter(type, cursor);
            gridView.setAdapter(adapter);

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

}
