package mobile.che.com.oddymobstar.chemobile.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.activity.handler.FragmentHandler;
import util.map.SubUTM;
import util.map.UTM;


public class ProjectCheActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final Long TWO_MINUTES = 120000l;
    private static Typeface font = null;
    public String googleAccountName = "";
    private ProjectCheController controller = new ProjectCheController(this);

    public static Typeface getFont() {
        return font;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        AccountManager manager = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
        for (Account account : manager.getAccounts()) {
            if (account.type.equalsIgnoreCase("com.google")) {
                try {
                    googleAccountName = account.name.split("@")[0];
                } catch (Exception e) {
                    googleAccountName = account.name;
                }
            }
        }


        font = Typeface.createFromAsset(
                this.getAssets(), "fontawesome-webfont.ttf");


        UTM.createUTMRegions();
        SubUTM.createSubUtms();

        controller.onCreate();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        controller.onPostCreate();
    }

    @Override
    public void onBackPressed() {
        controller.onBackPressed();
        if (!FragmentHandler.STOP_BACK_FRAGMENT) {
            super.onBackPressed();
        } else {
            FragmentHandler.STOP_BACK_FRAGMENT = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        controller.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        controller.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        controller.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.onResume();
    }

    public void onPause() {
        super.onPause();
        controller.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
    }


    /*
      delete shit below....ok.....lets drink energy drink....fix this shit further then get more beers.

      yes
     */

    /*
     callbacks from before,,,need them to test
     */
    public void deleteMessages(View view) {
        controller.viewHandler.deleteMessages();
    }

    public void messageCoverage(View view) {
        controller.viewHandler.messageCoverage();
    }


    public void sendPost(View view) {
        controller.viewHandler.sendPost();
    }

    public void cancelPost(View view) {
        controller.viewHandler.cancelPost();
    }

    public void createButton(View view) {
        controller.viewHandler.createButton();
    }


}
