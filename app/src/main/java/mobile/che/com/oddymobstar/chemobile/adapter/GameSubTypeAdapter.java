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
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import util.GameObjectTypes;

/**
 * Created by timmytime on 16/02/16.
 */
public class GameSubTypeAdapter extends CursorAdapter implements SectionIndexer {

    private final Context context;
    private final int layout = R.layout.game_subtype_list_item;
    private final int type, subType;

    private SparseIntArray sectionMap = new SparseIntArray();
    private SparseIntArray positionMap = new SparseIntArray();

    public GameSubTypeAdapter(Context context, Cursor c, boolean autoRequery, int type, int subType) {
        super(context, c, autoRequery);
        this.context = context;
        this.type = type;
        this.subType = subType;
    }

    public static boolean isDeployStatus(Cursor cursor) {
        String status = getStatus(cursor);

        switch (status) {
            case "Deploy":
                return true;
            case "Arm":
                return true;
            case "Drop":
                return true;
            case "Build":
                return true;
            default:
                return false;
        }

    }


    public static String getTertiaryAction(Cursor cursor) {
        switch (cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_TYPE))) {
            case GameObjectGridFragment.INFASTRUCTURE:
                return "";
            case GameObjectGridFragment.SEA:
                return "Navigate";
            case GameObjectGridFragment.AIR:
                return "Fly";
            case GameObjectGridFragment.LAND:
                return "";
            case GameObjectGridFragment.MISSILE:
                switch (cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))) {
                    case GameObjectTypes.GROUND_MINE:
                        return "";
                    case GameObjectTypes.WATER_MINE:
                        return "";
                    default:
                        return "Launch";

                }
        }

        return "";
    }

    public static String getSecondaryAction(Cursor cursor) {
        switch (cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_TYPE))) {
            case GameObjectGridFragment.INFASTRUCTURE:
                return "";
            case GameObjectGridFragment.SEA:
                return "Launch";
            case GameObjectGridFragment.AIR:
                return "Takeoff";
            case GameObjectGridFragment.LAND:
                return "Move";
            case GameObjectGridFragment.MISSILE:
                switch (cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))) {
                    case GameObjectTypes.GROUND_MINE:
                        return "Drop";
                    case GameObjectTypes.WATER_MINE:
                        return "Drop";
                    default:
                        return "Target";

                }
        }

        return "";
    }

    public static String getStatus(Cursor cursor) {
        switch (cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_TYPE))) {
            //need to beef this up....once have more tables and data.
            case GameObjectGridFragment.INFASTRUCTURE:
                return cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) == null ? "Build" : "Installed";
            case GameObjectGridFragment.SEA:
                return cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) == null ? "Deploy" : "Active";
            case GameObjectGridFragment.AIR:
                return cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) == null ? "Deploy" : "Active";
            case GameObjectGridFragment.LAND:
                return cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) == null ? "Deploy" : "Active";
            case GameObjectGridFragment.MISSILE:
                switch (cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))) {
                    case GameObjectTypes.GROUND_MINE:
                        return cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) == null ? "Arm" : "Armed";
                    case GameObjectTypes.WATER_MINE:
                        return cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) == null ? "Arm" : "Armed";
                    default:
                        return cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) == null ? "Arm" : "Armed";

                }

        }

        return "Unknown";
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View v = inflator.inflate(layout, null);

        CardView cardView = (CardView) v.findViewById(R.id.game_sub_type_view_inner);
        cardView.setCardBackgroundColor(context.getResources().getColor(GameHelper.getGameColor(type)));


        bindView(v, context, cursor);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view.findViewById(R.id.game_sub_type_key);
        if (cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_TYPE)) == GameObjectGridFragment.MISSILE) {
            String detail = String.format("%s\nUTM:%s\nSubUTM:%s\nStatus:%s", cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_KEY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) == null ? "" : cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LONG)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBUTM_LAT)) == null ? "" : cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBUTM_LAT)) + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBUTM_LONG)),
                    getStatus(cursor));
            tv.setText(detail);

        } else {
            String detail = String.format("%s\nUTM:%s\nSubUTM:%s\nStatus:%s\nExplosives:%s", cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_KEY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) == null ? "" : cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LAT)) + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_UTM_LONG)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBUTM_LAT)) == null ? "" : cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBUTM_LAT)) + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBUTM_LONG)),
                    getStatus(cursor),
                    cursor.getString(cursor.getColumnIndexOrThrow("explosives_count")));
            tv.setText(detail);
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
                current = c.getString(c.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_KEY))
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
