package mobile.che.com.oddymobstar.chemobile.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 25/02/15.
 */
public class UTM {

    /*
     need some user friendly mappings for UTM by region groups.
     */
    private static final List<String> southAmericaUTMs = new ArrayList<>();
    private static final List<String> centralAmericaUTMs = new ArrayList<>();
    private static final List<String> northAmericaUTMs = new ArrayList<>();
    private static final List<String> oceaniaUTMs = new ArrayList<>();
    private static final List<String> europeanUTMs = new ArrayList<>();
    private static final List<String> africanUTMs = new ArrayList<>();
    private static final List<String> centralAsianUTMs = new ArrayList<>();
    private static final List<String> asianUTMs = new ArrayList<>();
    private static final List<String> southAsianUTMs = new ArrayList<>();
    private static final List<String> middleEastUTMs = new ArrayList<>();

    private static final Map<String, List> utmRegions = new HashMap<>();
    private static List<String> utmList = new ArrayList<>();

    private static Map<String, String> regionCentre = new HashMap<>();


    private int utmLong;
    private String utmLat = "";

    public UTM(String utm) {

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(utm);

        while (matcher.find()) {
            utmLong = Integer.valueOf(matcher.group());
        }

        pattern = Pattern.compile("[A-Z]");
        matcher = pattern.matcher(utm);

        while (matcher.find()) {
            utmLat = matcher.group();
        }


    }

    private static void populateRegion(List<String> values, String startLetter, String endLetter, int start, int end) {


        List<String> prefix = UTMGridCreator.latValues.subList(UTMGridCreator.latValues.indexOf(startLetter), UTMGridCreator.latValues.indexOf(endLetter) + 1);
        for (String val : prefix) {
            for (int i = start; i <= end; i++) {
                values.add(val + i);
            }
        }


    }

    public static void createUTMRegions() {


        /*
          now go look up the various codes.  will take some time.  go shopping now

          tehnically could use the long and lat, and calc on fly, but basically its as easy to hard code some values
          as they will never actually change.
         */

        /*
        south america is F - N, 18 - 24 */
        populateRegion(southAmericaUTMs, "F", "N", 18, 24);
        regionCentre.put("South America", "K21");
        /*central america is P - R, 11 - 21  */
        populateRegion(centralAmericaUTMs, "P", "R", 11, 21);
        regionCentre.put("Central America", "Q16");
         /*north america is S - V, 9 - 21 */
        populateRegion(northAmericaUTMs, "S", "V", 9, 21);
        regionCentre.put("North America", "T14");
        /*europe is S- V, 29 - 38 */
        populateRegion(europeanUTMs, "S", "V", 29, 38);
        regionCentre.put("Europe", "U32");
        /*africa is (should break it up) J- R, 28 - 38 */
        populateRegion(africanUTMs, "J", "R", 28, 38);
        regionCentre.put("Africa", "N34");
        /*middle east is Q- S, 36 - 40 */
        populateRegion(middleEastUTMs, "Q", "S", 36, 40);
        regionCentre.put("Middle East", "R38");
        /*central asia is P - U, 41 - 46 */
        populateRegion(centralAsianUTMs, "P", "U", 41, 46);
        regionCentre.put("Central Asia", "R42");
        /*south asia is M - P, 47 55 */
        populateRegion(southAsianUTMs, "M", "P", 47, 55);
        regionCentre.put("South Asia", "N48");
        /*asia is Q - U, 47 - 54 */
        populateRegion(asianUTMs, "Q", "U", 47, 54);
        regionCentre.put("Asia", "S50");
        /*oceania is G - L, 50 - 60 */
        populateRegion(oceaniaUTMs, "G", "L", 50, 60);
        regionCentre.put("Oceania", "J54");


        utmRegions.put("South America", southAmericaUTMs);
        utmRegions.put("Central America", centralAmericaUTMs);
        utmRegions.put("North America", northAmericaUTMs);
        utmRegions.put("Europe", europeanUTMs);
        utmRegions.put("Africa", africanUTMs);
        utmRegions.put("Asia", asianUTMs);
        utmRegions.put("Central Asia", centralAsianUTMs);
        utmRegions.put("South Asia", southAsianUTMs);
        utmRegions.put("Oceania", oceaniaUTMs);
        utmRegions.put("Middle East", middleEastUTMs);

        //what would work with this amount...is that we render them all for a region..
        //then we filter by region in utm mode.
        for (String name : utmRegions.keySet()) {
            utmList.add(name);
        }

        //now create the other UTM not in any of the above.
        for (String val : UTMGridCreator.latValues) {
            for (int i = 1; i <= 60; i++) {

                if (!isInRegions(val + i)) {
                    utmList.add(val + i);
                }
            }
        }

    }

    private static boolean isInRegions(String utm) {

        for (List regions : utmRegions.values()) {
            if (regions.contains(utm)) {
                return true;
            }
        }

        return false;
    }

    public static List<String> getUtmRegion(String region) {
        return utmRegions.get(region);
    }

    public static boolean isUTMRegion(String key) {
        return utmRegions.containsKey(key);
    }

    public static String getUTMRegion(String utm) {
        for (String key : utmRegions.keySet()) {

            if (utmRegions.get(key).contains(utm)) {
                return key;
            }
        }

        return "";
    }

    public static List<String> getUtmList() {
        return utmList;
    }

    public static String getRegionCentre(String region) {
        return regionCentre.get(region);
    }

    public static void main(String[] args) {

        UTM utm = new UTM("E31");

        System.out.println(utm.getUtmLat());
        System.out.println(utm.getUtmLong());


    }

    public int getUtmLong() {
        return utmLong;
    }

    public String getUtmLat() {
        return utmLat;
    }


}
