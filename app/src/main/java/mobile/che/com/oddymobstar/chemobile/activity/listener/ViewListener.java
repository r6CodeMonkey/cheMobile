package mobile.che.com.oddymobstar.chemobile.activity.listener;

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.GameController;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.fragment.AllianceGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Message;
import util.GameObjectTypes;


/**
 * Created by timmytime on 06/12/15.
 */
public class ViewListener {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    public ViewListener(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public AdapterView.OnItemClickListener getGameObjectTypesListClickListener(){
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position,
                                    long id) {



                android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

                //what are we,,,,
                Cursor cursor = (Cursor) controller.fragmentHandler.gameFrag.getListAdapter().getItem(position);
                controller.fragmentHandler.removeFragments(false);

                //so now we simply need to load up the next phase...based on our type and subtype.
                controller.fragmentHandler.gameSubTypeFrag.init(controller.fragmentHandler.gameFrag.getType(), cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE)), null);
                controller.materialsHandler.handleNavToolbar(main.getResources().getColor(GameController.getGameColor(controller.fragmentHandler.gameFrag.getType())), GameObjectTypes.getTypeName(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))));
                transaction.replace(R.id.chat_fragment, controller.fragmentHandler.gameSubTypeFrag);
                transaction.addToBackStack(null);

                transaction.commit();

            }
        };
    }

    public AdapterView.OnItemClickListener getAllianceListClickListener() {

        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position,
                                    long id) {

            /*
            basically if they select an item we launch chat frag with an ID...
             */
                Cursor cursor = (Cursor) controller.fragmentHandler.gridFrag.getListAdapter().getItem(position);
                controller.fragmentHandler.removeFragments(false);

                android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

                String key = "";
                switch (controller.fragmentHandler.gridFrag.getType()) {
                    case AllianceGridFragment.MY_ALLIANCES:
                        key = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ALLIANCE_KEY));
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ALLIANCE_NAME));
                        controller.fragmentHandler.chatFrag.setCursor(controller.dbHelper.getMessages(Message.ALLIANCE_MESSAGE, key), key, title);

                        //and show
                        controller.viewHandler.showChat(title);

                        break;

                }


                transaction.commit();


            }
        };
    }
}
