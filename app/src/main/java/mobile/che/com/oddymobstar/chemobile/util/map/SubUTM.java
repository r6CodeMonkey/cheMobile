package mobile.che.com.oddymobstar.chemobile.util.map;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by timmytime on 05/05/15.
 */
public class SubUTM {

    private static List<String> subUtmList = new ArrayList<>();
    private String subUtmLat = "";
    private String subLatString = "";
    private int subLatInt;
    private int subUtmLong;


    public SubUTM(String subUtm) {


        //its in form....lat => Number + Letter,,,
        //               long => Number
        //no regex this time...find occurence of letter..
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(subUtm);


        while (matcher.find()) {
            subLatString = matcher.group();
        }

        //now find its index.
        int index = subUtm.indexOf(subLatString);

        //there is an issue here sometimes...we need test cases to find out why.  i think its a timing glitch ie we have an empty string.
        subLatInt = Integer.valueOf(subUtm.substring(0, index));
        subUtmLat = subUtm.substring(0, index + 1);
        subUtmLong = Integer.valueOf(subUtm.substring(index + 1));

    }

    public static void createSubUtms() {
        for (int indexCounter = 1; indexCounter <= 8; indexCounter++) {
            for (String val : UTMGridCreator.latValues) {
                for (int i = 0; i < 60; i++) {
                    subUtmList.add(indexCounter + val + i);
                }
            }
        }

    }

    public static List<String> getSubUtmList() {
        return subUtmList;
    }

    public static void main(String[] args) {

        SubUTM subUTM = new SubUTM("19V20");

        System.out.println(subUTM.getSubUtmLat());
        System.out.println(subUTM.getSubUtmLong());
        System.out.println(subUTM.getSubLatInt());
        System.out.println(subUTM.getSubLatString());


    }

    public String getSubUtmLat() {
        return subUtmLat;
    }

    public int getSubUtmLong() {
        return subUtmLong;
    }

    public int getSubLatInt() {
        return subLatInt;
    }

    public String getSubLatString() {
        return subLatString;
    }
}
