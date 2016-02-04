package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.fragment.ChatFragment;
import mobile.che.com.oddymobstar.chemobile.fragment.GridFragment;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.widget.ChatPost;
import mobile.che.com.oddymobstar.chemobile.util.widget.CreateView;


/**
 * Created by timmytime on 03/12/15.
 */
public class MaterialsHandler {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    public MaterialsHandler(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    private Animator getAnimatorIn(LinearLayout view, boolean hide) {


        int cxIn = (view.getLeft() + view.getRight()) / 2;
        int cyIn = (view.getTop() + view.getBottom()) / 2;

        int radiusIn = Math.max(view.getWidth(), view.getHeight());

        int cxOut = (controller.materialsHelper.floatingActionButton.getLeft() + controller.materialsHelper.floatingActionButton.getRight()) / 2;
        int cyOut = (controller.materialsHelper.floatingActionButton.getTop() + controller.materialsHelper.floatingActionButton.getBottom()) / 2;

        int radiusOut = controller.materialsHelper.floatingActionButton.getWidth();

        Animator animatorIn = null;

        if (hide) {
            animatorIn = ViewAnimationUtils.createCircularReveal(view, cxIn, cyIn, 0, radiusIn);
        } else {
            animatorIn = ViewAnimationUtils.createCircularReveal(controller.materialsHelper.floatingActionButton, cxOut, cyOut, 0, radiusOut);
        }
        //   animatorIn.setDuration(500);
        animatorIn.setInterpolator(new AccelerateInterpolator());

        return animatorIn;

    }

    private Animator getAnimatorOut(LinearLayout view, boolean hide) {

        int cxIn = (view.getLeft() + view.getRight()) / 2;
        int cyIn = (view.getTop() + view.getBottom()) / 2;

        int radiusIn = Math.max(view.getWidth(), view.getHeight());

        int cxOut = (controller.materialsHelper.floatingActionButton.getLeft() + controller.materialsHelper.floatingActionButton.getRight()) / 2;
        int cyOut = (controller.materialsHelper.floatingActionButton.getTop() + controller.materialsHelper.floatingActionButton.getBottom()) / 2;

        int radiusOut = controller.materialsHelper.floatingActionButton.getWidth();

        Animator animatorOut = null;

        if (hide) {
            animatorOut = ViewAnimationUtils.createCircularReveal(controller.materialsHelper.floatingActionButton, cxOut, cyOut, radiusOut, 0);
        } else {
            animatorOut = ViewAnimationUtils.createCircularReveal(view, cxIn, cyIn, radiusIn, 0);
        }

        animatorOut.setInterpolator(new AccelerateInterpolator());

        return animatorOut;
    }


    public void handleChatFAB(ChatFragment chatFrag, final boolean hide) {

        final ChatPost hiddenChatPost = chatFrag.getHiddenChatPost();


        Animator animatorIn = getAnimatorIn(hiddenChatPost, hide);
        Animator animatorOut = getAnimatorOut(hiddenChatPost, hide);


        animatorOut.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (hide) {
                    controller.materialsHelper.floatingActionButton.setVisibility(View.INVISIBLE);

                    InputMethodManager imm = (InputMethodManager) main.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    hiddenChatPost.requestFocus();

                } else {
                    hiddenChatPost.setVisibility(View.GONE);

                    InputMethodManager imm = (InputMethodManager) main.getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(hiddenChatPost.getWindowToken(), 0);


                }

            }
        });

        if (hide) {
            hiddenChatPost.setVisibility(View.VISIBLE);
        } else {
            controller.materialsHelper.floatingActionButton.setVisibility(View.VISIBLE);
        }


        animatorOut.start();
        animatorIn.start();

    }


    public void handleAllianceFAB(GridFragment gridFrag, final boolean hide) {

        final CreateView hiddenCreateView = gridFrag.getHiddenCreateView();

        Animator animatorIn = getAnimatorIn(hiddenCreateView, hide);
        Animator animatorOut = getAnimatorOut(hiddenCreateView, hide);

        animatorOut.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);


                if (hide) {
                    controller.materialsHelper.floatingActionButton.setVisibility(View.INVISIBLE);

                    InputMethodManager imm = (InputMethodManager) main.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    hiddenCreateView.requestFocus();

                } else {
                    hiddenCreateView.setVisibility(View.GONE);

                    InputMethodManager imm = (InputMethodManager) main.getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(hiddenCreateView.getWindowToken(), 0);


                }


            }
        });

        if (hide) {
            hiddenCreateView.setVisibility(View.VISIBLE);
        } else {
            controller.materialsHelper.floatingActionButton.setVisibility(View.VISIBLE);
        }


        animatorOut.start();
        animatorIn.start();


    }


    public void handleSearchFab() {


        int cxOut = (controller.materialsHelper.floatingActionButton.getLeft() + controller.materialsHelper.floatingActionButton.getRight()) / 2;
        int cyOut = (controller.materialsHelper.floatingActionButton.getTop() + controller.materialsHelper.floatingActionButton.getBottom()) / 2;

        int radiusOut = controller.materialsHelper.floatingActionButton.getWidth();


        Animator animatorOut = ViewAnimationUtils.createCircularReveal(controller.materialsHelper.floatingActionButton, cxOut, cyOut, radiusOut, 0);

        //animatorOut.setDuration(300);
        animatorOut.setInterpolator(new AccelerateInterpolator());

        animatorOut.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                controller.materialsHelper.floatingActionButton.setVisibility(View.INVISIBLE);

                FragmentTransaction transaction = main.getSupportFragmentManager()
                        .beginTransaction();

                controller.mapHelper.createGridDialog(controller.mapHandler.getSelectedGrid()).show(transaction, "dialog");

            }
        });

        animatorOut.start();


    }

    public void handleFABChange(int color, int image, int visible) {
        if (color != -1) {
            controller.materialsHelper.floatingActionButton.setBackgroundTintList(controller.materialsHelper.getColorStateList(color));
        }
        controller.materialsHelper.floatingActionButton.setVisibility(visible);
        if (image != -1) {
            controller.materialsHelper.floatingActionButton.setImageDrawable(main.getDrawable(image));
        }

    }


    public void setNavConfigValues() {


        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                MenuItem item = controller.materialsHelper.navigationView.getMenu().findItem(R.id.utm);
                item.setTitle(main.getResources().getString(R.string.menu_utm) + " - " + controller.configuration.getConfig(Configuration.CURRENT_UTM).getValue());

                item = controller.materialsHelper.navigationView.getMenu().findItem(R.id.sub_utm);
                item.setTitle(main.getResources().getString(R.string.menu_subutm) + " - " + controller.configuration.getConfig(Configuration.CURRENT_SUBUTM).getValue());

                item = controller.materialsHelper.navigationView.getMenu().findItem(R.id.encrypt);
                item.setTitle(main.getResources().getString(R.string.menu_encryption) + " - " + controller.configuration.getConfig(Configuration.SSL_ALGORITHM).getValue());

                TextView textView = (TextView) controller.materialsHelper.navigationView.findViewById(R.id.nav_header);
//                textView.setText(controller.configuration.getConfig(Configuration.PLAYER_KEY).getValue());

                if (controller.materialsHelper.userImage != null) {
                    if (controller.materialsHelper.userImage.getUserImage() != null) {
                        controller.materialsHelper.userImageView.setImageBitmap(controller.materialsHelper.userImage.getUserImage());
                    }
                }

                //  gridFrag.refreshAdapter();  //dont really need this...to check its for alliances..
            }
        });


    }

    public void handleNavToolbar(int color, String title) {
        controller.materialsHelper.navToolbar.setBackgroundColor(color);
        controller.materialsHelper.navToolbar.setTitle(title);

    }


}
