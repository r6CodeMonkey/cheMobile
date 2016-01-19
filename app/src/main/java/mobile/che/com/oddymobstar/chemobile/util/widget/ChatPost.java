package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import oddymobstar.crazycourier.R;

/**
 * Created by root on 13/04/15.
 */
public class ChatPost extends LinearLayout {

    private EditText post;

    public ChatPost(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.chat_post, this, true);
        post = (EditText) findViewById(R.id.post_text);
    }

    public ChatPost(Context context) {
        super(context, null);
    }


    public boolean isPostValid() {
        return post != null;
    }

    public String getPost() {
        return post.getText().toString();
    }

    public void cancelPost() {
        post.setText("");
    }


}
