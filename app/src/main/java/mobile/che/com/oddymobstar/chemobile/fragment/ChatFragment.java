package mobile.che.com.oddymobstar.chemobile.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.adapter.ChatAdapter;
import mobile.che.com.oddymobstar.chemobile.util.widget.ChatPost;


/**
 * Created by root on 04/04/15.
 */
public class ChatFragment extends Fragment {

    private Cursor chat;
    private String key;
    private String title;

    private ChatPost hiddenChatPost;


    private CursorAdapter adapter = null;


    public ChatFragment() {
        setRetainInstance(true);
    }

    public String getKey() {
        return key;
    }

    public void setCursor(Cursor chat, String key, String title) {
        this.key = key;
        this.chat = chat;
        this.title = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chat_fragment, container, false);


        //need to sort out the list shit too....
        adapter = new ChatAdapter(this.getActivity(), chat, false);

        ListView lv = (ListView) view.findViewById(R.id.list);

        hiddenChatPost = (ChatPost) view.findViewById(R.id.chat_post);

        hiddenChatPost.setVisibility(View.GONE);
        hiddenChatPost.setElevation(16);

        Button button = (Button) hiddenChatPost.findViewById(R.id.post);
        button.setTypeface(ProjectCheActivity.getFont());

        button = (Button) hiddenChatPost.findViewById(R.id.cancel);
        button.setTypeface(ProjectCheActivity.getFont());


        lv.setDivider(null);
        lv.setDividerHeight(0);

        lv.setAdapter(adapter);


        return view;

    }

    public void refreshAdapter(Cursor cursor) {

        adapter.changeCursor(cursor);
    }


    public String getTitle() {
        return title;
    }

    public ChatPost getHiddenChatPost() {
        return hiddenChatPost;
    }


}
