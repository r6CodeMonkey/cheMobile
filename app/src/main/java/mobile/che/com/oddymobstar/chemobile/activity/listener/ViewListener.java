package mobile.che.com.oddymobstar.chemobile.activity.listener;

import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.fragment.AllianceGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Message;


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

    public AdapterView.OnItemClickListener getListClickListener() {

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
