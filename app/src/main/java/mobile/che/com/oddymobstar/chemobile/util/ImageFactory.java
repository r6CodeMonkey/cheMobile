package mobile.che.com.oddymobstar.chemobile.util;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.HashMap;
import java.util.Map;

import mobile.che.com.oddymobstar.chemobile.R;
import util.GameObjectTypes;

/**
 * Created by timmytime on 15/04/16.
 */
public class ImageFactory {

/*
need to return destination icons as map markers...not icons.  yes.
 */
    private static Map<Integer, BitmapDescriptor> imageMap = new HashMap<>();

    public static BitmapDescriptor getImage(int subtype, boolean destination){

        if(destination){
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }

        if(imageMap.containsKey(subtype)){
            return imageMap.get(subtype);
        }

        switch(subtype){

            case GameObjectTypes.AIRPORT:
                imageMap.put(subtype,BitmapDescriptorFactory.fromResource(R.drawable.airport));
                break;
            case GameObjectTypes.FIGHTER:
                imageMap.put(subtype, BitmapDescriptorFactory.fromResource(R.drawable.fighter));
                break;
            case GameObjectTypes.TANK:
                imageMap.put(subtype, BitmapDescriptorFactory.fromResource(R.drawable.tank));
                break;
            case GameObjectTypes.SATELLITE:
                imageMap.put(subtype, BitmapDescriptorFactory.fromResource(R.drawable.satellite));
                break;
            case GameObjectTypes.OUTPOST:
                imageMap.put(subtype, BitmapDescriptorFactory.fromResource(R.drawable.outpost));
                break;
            case GameObjectTypes.RV:
                imageMap.put(subtype, BitmapDescriptorFactory.fromResource(R.drawable.atv));
                break;
            case GameObjectTypes.MISSILE_LAUNCHER:
                imageMap.put(subtype, BitmapDescriptorFactory.fromResource(R.drawable.missilelauncher));
                break;
            case GameObjectTypes.MINI_DRONE:
                imageMap.put(subtype, BitmapDescriptorFactory.fromResource(R.drawable.drone));
                break;
            default:
                imageMap.put(subtype, BitmapDescriptorFactory.defaultMarker());
                break;

        }

        return imageMap.get(subtype);
    }

}
