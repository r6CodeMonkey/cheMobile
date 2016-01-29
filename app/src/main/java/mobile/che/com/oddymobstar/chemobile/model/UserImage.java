package mobile.che.com.oddymobstar.chemobile.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import mobile.che.com.oddymobstar.chemobile.database.DBHelper;


/**
 * Created by timmytime on 23/06/15.
 */
public class UserImage {

    private Bitmap userImage;
    private String userImageKey;

    public UserImage() {

    }

    public UserImage(Cursor cursor) {

        setUserImageKey(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USER_IMAGE_KEY)));
        setUserImage(cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.USER_IMAGE)));

    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        if (userImage != null) {
            if (userImage.length > 0) {
                this.userImage = BitmapFactory.decodeByteArray(userImage, 0, userImage.length);
            }
        }

    }

    public String getUserImageKey() {
        return userImageKey;
    }

    public void setUserImageKey(String userImageKey) {
        this.userImageKey = userImageKey;
    }
}
