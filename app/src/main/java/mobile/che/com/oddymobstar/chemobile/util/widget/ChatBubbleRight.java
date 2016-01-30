package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import mobile.che.com.oddymobstar.chemobile.R;


/**
 * Created by root on 13/04/15.
 */
public class ChatBubbleRight extends LinearLayout {


    public ChatBubbleRight(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chat_bubble_right, this, true);
    }

    public ChatBubbleRight(Context context) {
        super(context, null);
    }

}
