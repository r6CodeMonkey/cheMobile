package mobile.che.com.oddymobstar.chemobile.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.adapter.GameSubTypeAdapter;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;

/**
 * Created by timmytime on 16/02/16.
 */
public class GameSubTypeGridFragment extends Fragment {

    private int type, subType;

    private AdapterView.OnItemClickListener onClickListener = null;
    private CursorAdapter adapter = null;

    private DBHelper dbHelper;
    private GridView gridView;

    public GameSubTypeGridFragment() {
        setRetainInstance(true);
    }

    public void init(int type, int subType, AdapterView.OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.type = type;
        this.subType = subType;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.game_list_fragment, container, false);

        if (dbHelper == null) {
            dbHelper = DBHelper.getInstance(getActivity());
        }


        gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(onClickListener);

        gridView.setAdapter(adapter);

        new LoadCursors().execute("");

        return view;
    }

    private Cursor getCursor(int type, int subType) {
        return dbHelper.getGameObjects(type, subType);
    }

    private CursorAdapter getCursorAdapter(int type, int subType, Cursor cursor) {
        return new GameSubTypeAdapter(getActivity(), cursor, true, type, subType);
    }

    public void refreshAdapter() {
        if (adapter != null) {
            adapter.changeCursor(dbHelper.getGameObjects(type, subType));
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


    private class LoadCursors extends AsyncTask<String, Void, String> {

        private Cursor cursor;

        @Override
        protected String doInBackground(String... params) {

            if (dbHelper == null) {
                dbHelper = DBHelper.getInstance(getActivity());
            }

            cursor = getCursor(type, subType);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            adapter = getCursorAdapter(type, subType, cursor);
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
