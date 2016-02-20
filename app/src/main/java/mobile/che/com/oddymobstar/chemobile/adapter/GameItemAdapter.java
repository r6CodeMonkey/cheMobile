package mobile.che.com.oddymobstar.chemobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
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
import mobile.che.com.oddymobstar.chemobile.activity.helper.GameHelper;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import util.GameObjectTypes;

/**
 * Created by timmytime on 11/02/16.
 */
public class GameItemAdapter extends CursorAdapter implements SectionIndexer {

    private final Context context;
    private final int layout = R.layout.game_list_item;
    private final int type;

    private SparseIntArray sectionMap = new SparseIntArray();
    private SparseIntArray positionMap = new SparseIntArray();


    public GameItemAdapter(Context context, Cursor c, boolean autoRequery, int type) {
        super(context, c, autoRequery);
        this.context = context;
        this.type = type;

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View v = inflator.inflate(layout, null);

        CardView cardView = (CardView) v.findViewById(R.id.game_card_view_inner);
        cardView.setCardBackgroundColor(context.getResources().getColor(GameHelper.getGameColor(type)));


        bindView(v, context, cursor);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view.findViewById(R.id.game_item_name);


        String detail = String.format("%s\nTotal:%s", GameObjectTypes.getTypeName(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))),
                cursor.getString(cursor.getColumnIndexOrThrow("type_total")));

        //in reality, its going to be the type name + key....
        tv.setText(detail);
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
                current = c.getString(c.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))
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

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionMap.indexOfKey(sectionIndex) != -1) {
            return sectionMap.get(sectionIndex);
        } else {
            return 0;
        }
    }

    @Override
    public int getSectionForPosition(int position) {
        if (positionMap.indexOfKey(position) != -1) {

            return positionMap.get(position);
        } else {
            return 0;
        }
    }
}
