package mobile.che.com.oddymobstar.chemobile.activity.listener;

import android.database.Cursor;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.activity.handler.FragmentHandler;
import mobile.che.com.oddymobstar.chemobile.activity.helper.GameHelper;
import mobile.che.com.oddymobstar.chemobile.adapter.ArmExplosiveAdapter;
import mobile.che.com.oddymobstar.chemobile.adapter.GameSubTypeAdapter;
import mobile.che.com.oddymobstar.chemobile.database.DBHelper;
import mobile.che.com.oddymobstar.chemobile.fragment.AllianceGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
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

    public AdapterView.OnItemClickListener gameObjectSubTypeListClickListener() {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position,
                                    long id) {

                Cursor cursor = (Cursor) controller.fragmentHandler.gameSubTypeFrag.getListAdapter().getItem(position);
                controller.fragmentHandler.removeFragments(false);

                boolean deploy = GameSubTypeAdapter.isDeployStatus(cursor);

                LatLng latLng = null;

                if (deploy) {
                    latLng = controller.locationListener.getCurrentLocationLatLng();
                    //also need to ensure you can deploy in event of sea game object etc.  need to do something to ensure this is ok...also you need a port....
                } else {
                    latLng = new LatLng(cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_LAT)), cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_LONG)));
                }

                final String action = GameSubTypeAdapter.getStatus(cursor);
                final String title = GameObjectTypes.getTypeName(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))).replace("\n", " ") + "\nKey: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_KEY));
                final String key = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_KEY));
                final int subType = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE));
                final int type = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_TYPE));


                if (action.equals("Arm")) {
                    controller.gameController.gameHandler.armDialog(title, key,
                            new ArmExplosiveAdapter(main, controller.dbHelper.getAvailableObjectsToArm(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))), true));
                } else {

                    //zoom to wherever we are headed.
                    controller.mapHandler.handleCamera(latLng, 45, 0, 19);
                    Marker marker = controller.mapHandler.getMarkerMap().get(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_KEY)));

                    if (marker != null) {
                        marker.showInfoWindow();
                    }

                    //if we are locked..we delay a camera animation to target and then back
                    if (action.equals("Locked")) {

                        final LatLng host = latLng;
                        final GameObject missile = controller.dbHelper.getGameObject(key);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                controller.mapHandler.handleCamera(new LatLng(missile.getDestLatitude(), missile.getDestLongitude()), 45, 0, 17);
                            }
                        }, 3000);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                controller.mapHandler.handleCamera(host, 45, 0, 19);
                            }
                        }, 6000);
                    }

                    //ideally need to wait a little bit.
                    if (deploy) {

                        //if we are an aircraft or boats we need to deploy at port / airport
                        if(subType == GameObjectTypes.CARRIER || subType == GameObjectTypes.FIGHTER || subType == GameObjectTypes.BOMBER ||
                                subType == GameObjectTypes.SUB || subType == GameObjectTypes.FAC || subType == GameObjectTypes.DESTROYER){
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    controller.gameController.gameHandler.deployToBaseDialog(key, type);
                                }
                            }, 3000);

                        }else {

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    controller.gameController.gameHandler.deployDialog(action, title, key);
                                }
                            }, 3000);
                        }
                    }

                }
            }
        };
    }

    public AdapterView.OnItemClickListener getGameObjectTypesListClickListener() {
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
                controller.materialsHandler.handleNavToolbar(main.getResources().getColor(GameHelper.getGameColor(controller.fragmentHandler.gameFrag.getType())), GameObjectTypes.getTypeName(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.GAME_OBJECT_SUBTYPE))));
                transaction.replace(R.id.chat_fragment, controller.fragmentHandler.gameSubTypeFrag);

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
                FragmentHandler.STOP_BACK_FRAGMENT = false;

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
