package mobile.che.com.oddymobstar.chemobile.activity.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import mobile.che.com.oddymobstar.chemobile.model.UserImage;
import mobile.che.com.oddymobstar.chemobile.util.graphics.RoundedImageView;


/**
 * Created by timmytime on 03/12/15.
 */
public class MaterialsHelper {

    //fuck sake spelling lol. to much reading american shit
    public static final int UTM_COLOR = 1;
    public static final int SUB_UTM_COLOR = 2;
    public static final int ALLIANCE_COLOR = 3;
    public static final int CHAT_COLOR = 4;
    public static final int INFRA_COLOR = 5;
    public static final int LAND_COLOR = 6;
    public static final int SEA_COLOR = 7;
    public static final int AIR_COLOR = 8;
    public static final int MISSILE_COLOR = 9;

    private final ProjectCheActivity main;
    private final ColorStateList subUtmColorList;
    private final ColorStateList utmColorList;
    private final ColorStateList allianceColorList;
    private final ColorStateList chatColorList;
    private final ColorStateList seaColorList;
    private final ColorStateList airColorList;
    private final ColorStateList landColorList;
    private final ColorStateList infraColorList;
    private final ColorStateList missileColorList;

    public DrawerLayout navDrawer;
    public ActionBarDrawerToggle navToggle;
    public NavigationView navigationView;
    public Toolbar navToolbar;
    public FloatingActionButton floatingActionButton;
    public RoundedImageView userImageView;
    public UserImage userImage;
    public TextView playerName;
    public TextView playerKey;
    public String playerKeyString = "";
    public TextView gameTimer;
    private Context context;



    public MaterialsHelper(ProjectCheActivity main) {
        this.context = main.getApplicationContext();
        this.main = main;

        subUtmColorList = createColorStateList(android.R.color.holo_orange_dark);
        utmColorList = createColorStateList(android.R.color.holo_purple);
        allianceColorList = createColorStateList(android.R.color.holo_red_dark);
        chatColorList = createColorStateList(android.R.color.holo_green_dark);
        seaColorList = createColorStateList(GameHelper.getGameColor(GameObjectGridFragment.SEA));
        airColorList = createColorStateList(GameHelper.getGameColor(GameObjectGridFragment.AIR));
        landColorList = createColorStateList(GameHelper.getGameColor(GameObjectGridFragment.LAND));
        infraColorList = createColorStateList(GameHelper.getGameColor(GameObjectGridFragment.INFASTRUCTURE));
        missileColorList = createColorStateList(GameHelper.getGameColor(GameObjectGridFragment.MISSILE));

    }

    public ColorStateList getColorStateList(int which) {
        switch (which) {

            case UTM_COLOR:
                return utmColorList;
            case SUB_UTM_COLOR:
                return subUtmColorList;
            case ALLIANCE_COLOR:
                return allianceColorList;
            case CHAT_COLOR:
                return chatColorList;
            case SEA_COLOR:
                return seaColorList;
            case AIR_COLOR:
                return airColorList;
            case LAND_COLOR:
                return landColorList;
            case INFRA_COLOR:
                return infraColorList;
            case MISSILE_COLOR:
                return missileColorList;
            default:
                return utmColorList;

        }
    }


    private ColorStateList createColorStateList(int color) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed}, //1
                        new int[]{android.R.attr.state_focused}, //2
                        new int[]{android.R.attr.state_focused, android.R.attr.state_pressed} //3
                },
                new int[]{
                        context.getResources().getColor(color), //1
                        context.getResources().getColor(color), //2
                        context.getResources().getColor(color) //3
                });
    }


    public void setUpMaterials(View.OnClickListener fabListener, View.OnTouchListener imageListener) {
        navDrawer = (DrawerLayout) main.findViewById(R.id.drawer_layout);
        // navDrawer.setElevation(16.0f);

        navToolbar = (Toolbar) main.findViewById(R.id.toolbar);
        main.setSupportActionBar(navToolbar);


        navToggle = new ActionBarDrawerToggle(
                main,
                navDrawer,
                navToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                main.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                main.invalidateOptionsMenu();
            }
        };
        navDrawer.setDrawerListener(navToggle);
        navToggle.syncState();


        main.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        main.getSupportActionBar().setHomeButtonEnabled(true);
        main.getSupportActionBar().setElevation(12.0f);


        navigationView = (NavigationView) main.findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                main.onOptionsItemSelected(menuItem);
                return true;
            }
        });

        floatingActionButton = (FloatingActionButton) main.findViewById(R.id.fab);

        floatingActionButton.setImageDrawable(context.getDrawable(R.drawable.ic_search_white_24dp));

        floatingActionButton.setBackgroundTintList(getColorStateList(SUB_UTM_COLOR));
        floatingActionButton.setVisibility(View.INVISIBLE);

        floatingActionButton.setOnClickListener(fabListener);

        playerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.player_name);
        playerKey = (TextView) navigationView.getHeaderView(0).findViewById(R.id.player_key);

        userImageView = (RoundedImageView) navigationView.getHeaderView(0).findViewById(R.id.user_image);
        userImageView.setOnTouchListener(imageListener);

        playerName.setText(main.googleAccountName);
        playerKey.setText(playerKeyString);


        if (userImage != null) {
            if (userImage.getUserImage() != null) {
                userImageView.setImageBitmap(userImage.getUserImage());
            }
        }

       gameTimer = (TextView)main.findViewById(R.id.nav_timer);


    }


}
