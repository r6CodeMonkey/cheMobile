package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

import message.Acknowledge;
import message.Alliance;
import message.CheMessage;
import message.Player;
import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.activity.helper.MaterialsHelper;
import mobile.che.com.oddymobstar.chemobile.activity.listener.MaterialsListener;
import mobile.che.com.oddymobstar.chemobile.fragment.GridFragment;
import mobile.che.com.oddymobstar.chemobile.model.Message;
import util.Tags;


/**
 * Created by timmytime on 06/12/15.
 */
public class ViewHandler {

    private final ProjectCheActivity main;
    private final ProjectCheController controller;

    public ViewHandler(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public void deleteMessages() {
        //grab the chat frag id...
        controller.dbHelper.deleteMessages(controller.fragmentHandler.chatFrag.getKey());
        controller.fragmentHandler.removeFragments(false);

    }

    public void messageCoverage() {

    }


    public void sendPost() {
        if (controller.fragmentHandler.chatFrag.getHiddenChatPost().isPostValid()) {
            //     try {

            CheMessage cheMessage = null;

            switch (controller.fragmentHandler.gridFrag.getType()) {


                case GridFragment.MY_ALLIANCES:

                    //create a message for the alliance....
                    //         coreMessage = new OutAllianceMessage(controller.locationListener.getCurrentLocation(), controller.configuration.getConfig(Configuration.PLAYER_KEY).getValue(), controller.uuidGenerator.generateAcknowledgeKey());
                    //         ((OutAllianceMessage) coreMessage).setAlliance(controller.dbHelper.getAlliance(controller.fragmentHandler.chatFrag.getKey()), OutCoreMessage.PUBLISH, OutCoreMessage.GLOBAL, controller.fragmentHandler.chatFrag.getHiddenChatPost().getPost());
                    break;

            }

            //need to animate...but
            controller.materialsHandler.handleChatFAB(controller.fragmentHandler.chatFrag, false);


            controller.cheService.writeToSocket(cheMessage);

       /*     } catch (NoSuchAlgorithmException nse) {

            } catch (JSONException jse) {
            } */

            cancelPost();
        } else {
            //find out if this every works! it was not cause of my bug
            controller.fragmentHandler.removeFragments(false);
        }
    }

    public void cancelPost() {
        controller.fragmentHandler.chatFrag.getHiddenChatPost().cancelPost();
    }

    public void createButton() {
        String createText = controller.fragmentHandler.gridFrag.getHiddenCreateView().getCreateText();

        switch (controller.fragmentHandler.gridFrag.getType()) {

            case GridFragment.MY_ALLIANCES:

                if (!createText.trim().isEmpty()) {
                    try {

                         controller.cheService.writeToSocket(controller.messageFactory.newAllianceMessage(createText, controller.locationListener.getCurrentLocation()));

                        //need to animate...but
                        controller.materialsHandler.handleAllianceFAB(controller.fragmentHandler.gridFrag, false);


                    } catch (NoSuchAlgorithmException e) {
                        Log.d("security exception", "security exception " + e.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;

        }

        controller.fragmentHandler.gridFrag.getHiddenCreateView().clear();

    }

    public void showChat(String title) {
        android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();


        //change the
        controller.materialsHandler.handleFABChange(MaterialsHelper.CHAT_COLOR, R.drawable.ic_chat_bubble_outline_white_24dp, View.VISIBLE);
        controller.materialsHelper.navToolbar.setTitle(title);

        MaterialsListener.FAB_MODE = MaterialsListener.CHAT_FAB;
        controller.materialsHelper.navToolbar.setBackgroundColor(main.getResources().getColor(android.R.color.holo_green_dark));
        controller.materialsHelper.navToolbar.setBackgroundColor(main.getResources().getColor(android.R.color.holo_green_dark));


        controller.fragmentHandler.chatFrag.setCursor(controller.dbHelper.getMessages(Message.ALLIANCE_MESSAGE, controller.fragmentHandler.chatFrag.getKey()), controller.fragmentHandler.chatFrag.getKey(), controller.fragmentHandler.chatFrag.getTitle());
        transaction.replace(R.id.chat_fragment, controller.fragmentHandler.chatFrag);
        transaction.addToBackStack(null);


        transaction.commit();

    }


}
