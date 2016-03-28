package mobile.che.com.oddymobstar.chemobile.util.graphics;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import mobile.che.com.oddymobstar.chemobile.fragment.VidiPrintFragment;

/**
 * Created by timmytime on 22/02/16.
 */
public class VidiPrintView extends TextView{

    private CharSequence text;
    private int index;
    private long delay = 100;


    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");


    public VidiPrintView(Context context) {
        super(context);
    }

    public VidiPrintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            //need to handle line breaks too....but this means we end up committing the full row...
            setText(text.subSequence(0, index++));
            if(index <= text.length()) {
                mHandler.postDelayed(characterAdder, delay);
            }
        }
    };

    public void animateText(CharSequence text) {
        this.text = text;
        this.index = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, delay);
    }

    public void setCharacterDelay(long millis) {
        this.delay = millis;
    }
}