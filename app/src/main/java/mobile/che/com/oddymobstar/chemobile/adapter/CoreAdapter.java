package mobile.che.com.oddymobstar.chemobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.fragment.GridFragment;


/**
 * Created by root on 27/02/15.
 */
public class CoreAdapter extends CursorAdapter implements SectionIndexer {

    private Context context;
    private int layout = R.layout.alliance_list_item;
    private int source;

    private SparseIntArray sectionMap = new SparseIntArray();
    private SparseIntArray positionMap = new SparseIntArray();


    public CoreAdapter(Context context, Cursor cursor, boolean autoRequery, int source) {
        super(context, cursor, autoRequery);

        this.source = source;
        this.context = context;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View v = inflator.inflate(layout, null);

      /*  CardView cardView = (CardView)v.findViewById(R.id.card_view);
        cardView.setRadius(4);
        cardView.setMaxCardElevation(16);
*/


        bindView(v, context, cursor);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tv = (TextView) view.findViewById(R.id.alliance_item_name);

        switch (source) {

            case GridFragment.MY_ALLIANCES:
                tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ALLIANCE_NAME)));
                break;
        }


    }

    /*
      fast index scroller
     */

    @Override
    public int getPositionForSection(int sectionIndex) {
        // TODO Auto-generated method stub
        if (sectionMap.indexOfKey(sectionIndex) != -1) {
            return sectionMap.get(sectionIndex);
        } else {
            return 0;
        }

    }

    @Override
    public int getSectionForPosition(int position) {
        // TODO Auto-generated method stub
        if (positionMap.indexOfKey(position) != -1) {

            return positionMap.get(position);
        } else {
            return 0;
        }
    }

    @Override
    public Object[] getSections() {

        Cursor c = this.getCursor();

        List<String> sections = new ArrayList<>();

        int sectionCounter = 0;

        String previous = "";
        String current = "";
        int startPosition = 0;
        c.moveToFirst();
        while (c.moveToNext()) {

            try {
                current = c.getString(c.getColumnIndexOrThrow(DBHelper.ALLIANCE_NAME))
                        .substring(0, 1);
            } catch (Exception e) {
            }

            positionMap.put(c.getPosition(), sectionCounter);

            if (!previous.trim().isEmpty() && !current.trim().isEmpty()
                    && !previous.equals(current)) {
                sections.add(previous);
                sectionMap.put(sectionCounter, startPosition);
                startPosition = c.getPosition();
                sectionCounter++;
            }

            previous = current;
        }
        c.moveToFirst();
        String[] t = new String[sections.size()];
        // TODO Auto-generated method stub
        return sections.toArray(t);
    }


}
