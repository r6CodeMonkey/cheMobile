package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.Polygon;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.activity.helper.GameHelper;
import mobile.che.com.oddymobstar.chemobile.activity.helper.MaterialsHelper;
import mobile.che.com.oddymobstar.chemobile.activity.listener.MaterialsListener;
import mobile.che.com.oddymobstar.chemobile.fragment.AllianceGridFragment;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Config;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;


/**
 * Created by timmytime on 04/12/15.
 */
public class OnOptionsItemSelectedHandler {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    public OnOptionsItemSelectedHandler(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public boolean onOptionsItemSelected(MenuItem item) {


        if (controller.materialsHelper.navToggle.onOptionsItemSelected(item)) {
            return true;
        }

        controller.configuration = new Configuration(controller.dbHelper.getConfigs());
        //bind again if its down.
        if (controller.cheService == null) {
            main.bindService(controller.intent, controller.serviceConnection, main.BIND_AUTO_CREATE);
        }

        switch (item.getItemId()) {

            case android.R.id.home:
                controller.fragmentHandler.removeFragments(false);
                controller.materialsHelper.navDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.settings:
                controller.mapHandler.CURRENT_GRID_FAB_STATE = MapHandler.OTHER_STATE;
                controller.fragmentHandler.removeFragments(false);
                handleSettings();
                break;
            case R.id.alliances:
                controller.mapHandler.CURRENT_GRID_FAB_STATE = MapHandler.OTHER_STATE;
                controller.fragmentHandler.removeFragments(false);
                handleAlliance();
                break;
            case R.id.game_alerts:
                handleAlerts();
                break;
          /*  case R.id.bluetooth_receive:
                //same mechanism for discovery.
                controller.viewHandler.allianceInvite(true);
                break;
            case R.id.bluetooth_send:
                //same mechanism for discovery.
                controller.viewHandler.allianceInvite(false);
                break; */
            case R.id.game_land:
                handleGame(GameObjectGridFragment.LAND, main.getResources().getString(R.string.menu_land));
                break;
            case R.id.game_sea:
                handleGame(GameObjectGridFragment.SEA, main.getResources().getString(R.string.menu_sea));
                break;
            case R.id.game_airborne:
                handleGame(GameObjectGridFragment.AIR, main.getResources().getString(R.string.menu_airborne));
                break;
            case R.id.game_missiles:
                handleGame(GameObjectGridFragment.MISSILE, main.getResources().getString(R.string.menu_explosives));
                break;
            case R.id.game_infastructure:
                handleGame(GameObjectGridFragment.INFASTRUCTURE, main.getResources().getString(R.string.menu_infrastructure));
                break;
            case R.id.utm:
                controller.fragmentHandler.removeFragments(false);
                handleUTM();
                break;
            case R.id.sub_utm:
                controller.fragmentHandler.removeFragments(false);
                handleSubUTM();
                break;
            case R.id.encrypt:
                break;
            case R.id.invite:
                break;
        }


        return true;
    }

    private void handleUTM() {

        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.mapHandler.CURRENT_GRID_FAB_STATE = MapHandler.UTM_FAB_STATE;
        if (!controller.mapHandler.lastLocateUTMs.isEmpty()) {

            for (final Polygon polygon : controller.mapHandler.lastLocateUTMs.values()) {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        polygon.remove();
                    }
                });
            }
            controller.mapHandler.CLEAR_GRIDS = true;
        }

        controller.materialsHandler.handleNavToolbar(main.getResources().getColor(android.R.color.holo_purple), main.getResources().getString(R.string.menu_utm));
        controller.materialsHandler.handleFABChange(MaterialsHelper.UTM_COLOR, -1, View.VISIBLE);

        MaterialsListener.FAB_MODE = MaterialsListener.GRID_FAB;
        controller.mapHandler.setSelectedGrid();

        controller.mapHandler.animateToGrid(controller.mapHelper.getMyUTM(), MapHandler.UTM_ZOOM);
        controller.materialsHelper.navDrawer.closeDrawer(controller.materialsHelper.navigationView);

      /*  try {
            transaction.show(controller.fragmentHandler.gridViewFragment);
        } catch (Exception e) {

        } */

        transaction.commit();
    }

    private void handleSubUTM() {

        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.mapHandler.CURRENT_GRID_FAB_STATE = MapHandler.SUBUTM_FAB_STATE;
        if (controller.mapHandler.lastLocateSubUTM != null) {
            controller.mapHandler.lastLocateSubUTM.remove();
        }
        controller.mapHandler.lastLocateSubUTM = null;
        controller.materialsHandler.handleNavToolbar(main.getResources().getColor(android.R.color.holo_orange_dark), main.getResources().getString(R.string.menu_subutm));
        controller.materialsHandler.handleFABChange(MaterialsHelper.SUB_UTM_COLOR, -1, View.VISIBLE);

        MaterialsListener.FAB_MODE = MaterialsListener.GRID_FAB;

        controller.mapHandler.setSelectedGrid();

        controller.materialsHelper.navDrawer.closeDrawer(controller.materialsHelper.navigationView);
        controller.mapHandler.animateToGrid(controller.mapHelper.getMySubUTM(), MapHandler.SUB_UTM_ZOOM);


        transaction.commit();

    }

    private void handleAlliance() {

        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.materialsHelper.navDrawer.closeDrawer(controller.materialsHelper.navigationView);

        controller.materialsHandler.handleFABChange(MaterialsHelper.ALLIANCE_COLOR, R.drawable.ic_add_circle_white_24dp, View.VISIBLE);

        controller.materialsListener.FAB_MODE = MaterialsListener.ALLIANCE_FAB;
        controller.materialsHandler.handleNavToolbar(main.getResources().getColor(android.R.color.holo_red_dark), main.getResources().getString(R.string.menu_alliances));

        controller.fragmentHandler.gridFrag.init(AllianceGridFragment.MY_ALLIANCES, controller.viewListener.getAllianceListClickListener());
        transaction.replace(R.id.chat_fragment, controller.fragmentHandler.gridFrag);
        transaction.commit();

    }

    private void handleAlerts(){
        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.materialsHelper.navDrawer.closeDrawer(controller.materialsHelper.navigationView);

        controller.materialsHandler.handleNavToolbar(main.getResources().getColor(android.R.color.holo_green_dark), main.getResources().getString(R.string.menu_alerts));

       // controller.fragmentHandler.vidiPrintFragment.init(AllianceGridFragment.MY_ALLIANCES, controller.viewListener.getAllianceListClickListener());
        transaction.replace(R.id.chat_fragment, controller.fragmentHandler.vidiPrintFragment);
        transaction.commit();
    }

    private void handleGame(int type, String title) {

        controller.mapHandler.CURRENT_GRID_FAB_STATE = MapHandler.OTHER_STATE;
        controller.fragmentHandler.removeFragments(false);

        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.materialsHelper.navDrawer.closeDrawer(controller.materialsHelper.navigationView);

        controller.materialsHandler.handleFABChange(GameHelper.getGameColorFlag(type), R.drawable.ic_monetization_on_white_24dp, View.VISIBLE);

        controller.materialsListener.FAB_MODE = MaterialsListener.GAME_FAB;
        controller.materialsHandler.handleNavToolbar(main.getResources().getColor(GameHelper.getGameColor(type)), title);

        controller.fragmentHandler.gameFrag.init(type, controller.viewListener.getGameObjectTypesListClickListener());
        transaction.replace(R.id.chat_fragment, controller.fragmentHandler.gameFrag);
        transaction.addToBackStack(null); //this is an issue with backstack back press...but it fucks everything else up via nav drawer selection
        transaction.commit();
    }

    private void handleSettings() {

        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();

        controller.materialsHelper.navDrawer.closeDrawer(controller.materialsHelper.navigationView);
        controller.materialsHelper.navToolbar.setTitle(R.string.menu_settings);

        controller.fragmentHandler.confFrag.init(controller.configurationHandler, controller.dbHelper.getConfigs(Config.USER), controller.dbHelper.getConfigs(Config.SYSTEM));

        transaction.replace(R.id.chat_fragment, controller.fragmentHandler.confFrag);
        transaction.commit();

    }

}
