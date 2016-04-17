package mobile.che.com.oddymobstar.chemobile.util;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.HashMap;
import java.util.Map;

import mobile.che.com.oddymobstar.chemobile.R;
import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import util.GameObjectTypes;

/**
 * Created by timmytime on 15/04/16.
 */
public class ImageFactory {

/*
need to return destination icons as map markers...not icons.  yes.
 */
    private static Map<Integer, BitmapDescriptor> imageMap = new HashMap<>();


    public static int getResource(int subType){
        switch(subType){

            case GameObjectTypes.AIRPORT:
                return R.drawable.airportlarge;
            case GameObjectTypes.FIGHTER:
                return R.drawable.fighterlarge;
            case GameObjectTypes.TANK:
                 return R.drawable.tanklarge;
            case GameObjectTypes.SATELLITE:
                return R.drawable.satellitelarge;
            case GameObjectTypes.OUTPOST:
                return R.drawable.outpostlarge;
            case GameObjectTypes.RV:
                return R.drawable.jeeplarge;
            case GameObjectTypes.MISSILE_LAUNCHER:
                return R.drawable.missilelauncherlarge;
            case GameObjectTypes.MINI_DRONE:
                return R.drawable.dronelarge;
            case GameObjectTypes.GARRISON:
                return R.drawable.fortlarge;


        }

        return R.drawable.drone;
    }

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
            case GameObjectTypes.GARRISON:
                imageMap.put(subtype, BitmapDescriptorFactory.fromResource(R.drawable.fort));
                break;
            default:
                imageMap.put(subtype, BitmapDescriptorFactory.defaultMarker());
                break;

        }

        return imageMap.get(subtype);
    }

}
