package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import oddymobstar.crazycourier.R;

/**
 * Created by root on 13/04/15.
 */
public class ChatBubbleLeft extends LinearLayout {


    public ChatBubbleLeft(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chat_bubble_left, this, true);
    }

    public ChatBubbleLeft(Context context) {
        super(context, null);
    }

}
