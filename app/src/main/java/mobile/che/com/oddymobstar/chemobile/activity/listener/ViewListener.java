package mobile.che.com.oddymobstar.chemobile.activity.listener;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.maps.model.LatLng;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.GameController;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.adapter.GameSubTypeAdapter;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.fragment.AllianceGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Message;
import mobile.che.com.oddymobstar.chemobile.util.widget.DeployDialog;
import mobile.che.com.oddymobstar.chemobile.util.widget.GridDialog;
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

    public AdapterView.OnItemClickListener gameObjectSubTypeListClickListener(){
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position,
                                    long id) {

                Cursor cursor = (Cursor) controller.fragmentHandler.gameSubTypeFrag.getListAdapter().getItem(position);
                controller.fragmentHandler.removeFragments(false);

                boolean deploy = GameSubTypeAdapter.isDeployStatus(cursor);

                LatLng latLng = null;

                if(deploy){
                  latLng = controller.locationListener.getCurrentLocationLatLng();
                  //also need to ensure you can deploy in event of sea game object etc.  need to do something to ensure this is ok...also you need a port....
                }else{
                   latLng = new LatLng(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_LAT)), cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_LONG)));
                }

                //zoom to wherever we are headed.
                controller.mapHandler.handleCamera(latLng, 45, 0, 20);

                //ideally need to wait a little bit.
                if(deploy){

                    final String title = GameObjectTypes.getTypeName(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))).replace("\n", " ") + "\nKey: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_KEY));
                    final String action = GameSubTypeAdapter.getStatus(cursor);
                    final String key = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_KEY));


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            controller.gameController.gameHandler.deployDialog(action, title, key);   }
                    }, 3000);
                }

            }
        };
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
                controller.fragmentHandler.gameSubTypeFrag.init(controller.fragmentHandler.gameFrag.getType(), cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE)), gameObjectSubTypeListClickListener());
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
