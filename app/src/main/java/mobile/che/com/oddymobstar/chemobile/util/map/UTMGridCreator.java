package mobile.che.com.oddymobstar.chemobile.util.map;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import mobile.che.com.oddymobstar.chemobile.fragment.GameObjectGridFragment;
import util.map.SubUTM;
import util.map.UTM;
import util.map.UTMConvert;

/**
 * Created by timmytime on 05/05/15.
 */
public class UTMGridCreator {

    public static final int LAT_DEGREES = 8;
    public static final int LONG_DEGREES = 6;

    public static final int LAT_OFFSET = 80;
    public static final int LONG_OFFSET = 180;

    public static final double LAT_SUB_DEG = 0.05;
    public static final double LONG_SUB_DEG = 0.1;






    /*
      slighty different to our server side code....therefore we need server to tell us the dimensions...
      as a config message long term.  for now we can hard code.

      maybe not config no point messing with scales...to much work,


     given each UTM (and we then repeat for sub utm hence config)

     we need to calculate the 4 latlng points.

     */

    public UTMGridCreator() {

    }

    public static PolygonOptions getUTMGrid(UTM utm) {

        PolygonOptions options = new PolygonOptions();


        double long1 = ((Integer.valueOf(utm.getUtmLong()) * LONG_DEGREES) - LONG_DEGREES) - LONG_OFFSET;
        double long2 = (Integer.valueOf(utm.getUtmLong()) * LONG_DEGREES) - LONG_OFFSET;
        double lat1 = (((UTMConvert.latValues.indexOf(utm.getUtmLat()) + 1) * LAT_DEGREES) - LAT_DEGREES) - LAT_OFFSET;
        double lat2 = ((UTMConvert.latValues.indexOf(utm.getUtmLat()) + 1) * LAT_DEGREES) - LAT_OFFSET;

        LatLng point1 = new LatLng(lat1, long1);  //lower left hand corner
        LatLng point2 = new LatLng(lat1, long2);  //lower right hand corner
        LatLng point3 = new LatLng(lat2, long1);  //uper left hand corner
        LatLng point4 = new LatLng(lat2, long2);  //upper right hand corner

        options.add(point1);
        options.add(point2);
        options.add(point4);
        options.add(point3);


        // options.strokeColor(Color.CYAN);


        return options;
    }

    public static PolygonOptions getSubUTMGrid(SubUTM subUTM, PolygonOptions utm) {

        PolygonOptions options = new PolygonOptions();

        LatLng latLng1 = utm.getPoints().get(0);


        double utmlong = Integer.valueOf(subUTM.getSubUtmLong()) * LONG_SUB_DEG;
        double utmLat = ((UTMConvert.latValues.indexOf(subUTM.getSubLatString())) + ((subUTM.getSubLatInt() - 1) * UTMConvert.latValues.size())) * LAT_SUB_DEG;


        double long1 = latLng1.longitude + utmlong;
        double long2 = latLng1.longitude + utmlong + LONG_SUB_DEG;
        double lat1 = latLng1.latitude + utmLat;
        double lat2 = latLng1.latitude + utmLat + LAT_SUB_DEG;
        //this is a bit harder...well it isnt but we do need to know our utm which we have..

        LatLng point1 = new LatLng(lat1, long1);  //lower left hand corner
        LatLng point2 = new LatLng(lat1, long2);  //lower right hand corner
        LatLng point3 = new LatLng(lat2, long1);  //uper left hand corner
        LatLng point4 = new LatLng(lat2, long2);  //upper right hand corner

        options.add(point1);
        options.add(point2);
        options.add(point4);
        options.add(point3);


        return options;
    }


    public static LatLng getCentreUTM(List<LatLng> options) {
        //given above.  we need point1 lat + 1/2 lat degrees + point1 long + 1/2 long degrees

        return new LatLng(options.get(0).latitude + (LAT_DEGREES / 2), options.get(0).longitude + (LONG_DEGREES / 2));
    }

    public static LatLng getCentreSubUTM(List<LatLng> options) {
        //given above.  we need point1 lat + 1/2 lat degrees + point1 long + 1/2 long degrees

        return new LatLng(options.get(0).latitude + (LAT_SUB_DEG / 2), options.get(0).longitude + (LONG_SUB_DEG / 2));
    }


    public static PolylineOptions createPath(LatLng start, LatLng destination, int type) {
        PolylineOptions options = new PolylineOptions();

        options.add(start);
        options.add(destination);
        options.width(50);
        switch (type) {
            case GameObjectGridFragment.AIR:
                options.color(Color.CYAN);
                break;
            case GameObjectGridFragment.LAND:
                options.color(Color.GREEN);
                break;
            case GameObjectGridFragment.SEA:
                options.color(Color.BLUE);
                break;

        }

        return options;
    }


}
