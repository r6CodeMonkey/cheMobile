package mobile.che.com.oddymobstar.chemobile.util.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;


/**
 * Created by root on 14/04/15.
 */
public class CreateView extends LinearLayout {

    public CreateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.create_view, this, true);
    }

    public CreateView(Context context) {
        super(context, null);
    }


    public String getCreateText() {
        return ((EditText) findViewById(R.id.create_text)).getText().toString();
    }

    public void clear() {
        ((EditText) findViewById(R.id.create_text)).setText("");
    }

}
