package mobile.che.com.oddymobstar.chemobile.util.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import util.map.SubUTM;
import util.map.UTM;
import util.map.UTMConvert;

/**
 * Created by timmytime on 18/02/16.
 */
public class GameMoveGridCreator {


    /*
      we need to return a list of utm,sub utm pairs.....
     */

    public static Map<UTM, List<SubUTM>> get3by3Grid(GameObject gameObject) {

        Map<UTM, List<SubUTM>> grids = new HashMap<>();

        String x = gameObject.getSubUtmLat();
        String y = gameObject.getSubUtmLong();

        String alpha = gameObject.getSubUtmLat().substring(gameObject.getSubUtmLat().length() - 1);
        int alphaIndex = UTMConvert.latValues.indexOf(alpha);
        int latInt = Integer.valueOf(gameObject.getSubUtmLat().substring(0, 1));
        int longInt = Integer.valueOf(gameObject.getSubUtmLong());

        List<SubUTM> currentUtmList = new ArrayList<>();

        currentUtmList.add(new SubUTM(x, y));

        //simple case...test if our centre is within a UTM...
        //UTM runs 1C0 to 1C59, and to 8X0 to 8X59
        if (!alpha.equals("C") && latInt != 1 && !alpha.equals("X") && latInt != 8
                && longInt != 0 && longInt != 59) {
            //we are in the grid so its straight forward.
            currentUtmList.add(new SubUTM(latInt + alpha, String.valueOf(longInt + 1)));  //east
            currentUtmList.add(new SubUTM(latInt + alpha, String.valueOf(longInt - 1))); //west

            currentUtmList.add(new SubUTM(alpha.equals("X") ? latInt + 1 + "C" : latInt + UTMConvert.latValues.get(alphaIndex + 1), String.valueOf(longInt)));  //north
            currentUtmList.add(new SubUTM(alpha.equals("C") ? latInt - 1 + "X" : latInt + UTMConvert.latValues.get(alphaIndex - 1), String.valueOf(longInt))); //south

            //north east
            currentUtmList.add(new SubUTM(alpha.equals("X") ? latInt + 1 + "C" : latInt + UTMConvert.latValues.get(alphaIndex + 1), String.valueOf(longInt + 1)));
            //north west
            currentUtmList.add(new SubUTM(alpha.equals("X") ? latInt + 1 + "C" : latInt + UTMConvert.latValues.get(alphaIndex + 1), String.valueOf(longInt - 1)));
            //south east
            currentUtmList.add(new SubUTM(alpha.equals("C") ? latInt - 1 + "X" : latInt + UTMConvert.latValues.get(alphaIndex - 1), String.valueOf(longInt + 1)));
            //south west
            currentUtmList.add(new SubUTM(alpha.equals("C") ? latInt - 1 + "X" : latInt + UTMConvert.latValues.get(alphaIndex - 1), String.valueOf(longInt - 1)));

            grids.put(new UTM(gameObject.getUtmLat(), gameObject.getUtmLong()), currentUtmList);

        } else {
            currentUtmList.addAll(getCurrentUTMSubGrids(gameObject));
            grids.put(new UTM(gameObject.getUtmLat(), gameObject.getUtmLong()), currentUtmList);

            UTM north = null;
            UTM south = null;
            UTM east;
            UTM west;
            UTM southEast = null;
            UTM southWest = null;
            UTM northEast = null;
            UTM northWest = null;


            alphaIndex = UTMConvert.latValues.indexOf(gameObject.getUtmLat());

            if (!gameObject.getUtmLat().equals("X")) {
                north = new UTM(UTMConvert.latValues.get(alphaIndex + 1), gameObject.getUtmLong());
                if (gameObject.getUtmLong().equals("60")) {
                    northEast = new UTM(UTMConvert.latValues.get(alphaIndex + 1), "1");
                    northWest = new UTM(UTMConvert.latValues.get(alphaIndex + 1), "1");
                } else if (gameObject.getUtmLong().equals("1")) {
                    northEast = new UTM(UTMConvert.latValues.get(alphaIndex + 1), "60");
                    northWest = new UTM(UTMConvert.latValues.get(alphaIndex + 1), "60");
                } else {
                    northEast = new UTM(UTMConvert.latValues.get(alphaIndex + 1), String.valueOf(Integer.valueOf(gameObject.getUtmLong()) + 1));
                    northWest = new UTM(UTMConvert.latValues.get(alphaIndex + 1), String.valueOf(Integer.valueOf(gameObject.getUtmLong()) - 1));
                }
            }
            if (!gameObject.getUtmLat().equals("C")) {
                south = new UTM(UTMConvert.latValues.get(alphaIndex - 1), gameObject.getUtmLong());
                if (gameObject.getUtmLong().equals("60")) {
                    southEast = new UTM(UTMConvert.latValues.get(alphaIndex - 1), "1");
                    southWest = new UTM(UTMConvert.latValues.get(alphaIndex - 1), "1");
                } else if (gameObject.getUtmLong().equals("1")) {
                    southEast = new UTM(UTMConvert.latValues.get(alphaIndex - 1), "60");
                    southWest = new UTM(UTMConvert.latValues.get(alphaIndex - 1), "60");
                } else {
                    southEast = new UTM(UTMConvert.latValues.get(alphaIndex - 1), String.valueOf(Integer.valueOf(gameObject.getUtmLong()) + 1));
                    southWest = new UTM(UTMConvert.latValues.get(alphaIndex - 1), String.valueOf(Integer.valueOf(gameObject.getUtmLong()) - 1));
                }
            }


            if (gameObject.getUtmLong().equals("60")) {
                east = new UTM(gameObject.getUtmLat(), "1");
                west = new UTM(gameObject.getUtmLat(), "59");
            } else if (gameObject.getUtmLong().equals("1")) {
                east = new UTM(gameObject.getUtmLat(), "60");
                west = new UTM(gameObject.getUtmLat(), "2");

            } else {
                east = new UTM(gameObject.getUtmLat(), String.valueOf(Integer.valueOf(gameObject.getUtmLong()) + 1));
                west = new UTM(gameObject.getUtmLat(), String.valueOf(Integer.valueOf(gameObject.getUtmLong()) - 1));
            }

            //1 is north available and does it have any values?
            if (north != null) {
                List<SubUTM> temp = getOtherUTMSubGrids(gameObject, "North");
                if (temp.size() > 0) {
                    grids.put(north, temp);
                }
            }

            if (south != null) {
                List<SubUTM> temp = getOtherUTMSubGrids(gameObject, "South");
                if (temp.size() > 0) {
                    grids.put(south, temp);
                }

            }

            List<SubUTM> temp2 = getOtherUTMSubGrids(gameObject, "East");
            if (temp2.size() > 0) {
                grids.put(east, temp2);
            }

            temp2 = getOtherUTMSubGrids(gameObject, "West");
            if (temp2.size() > 0) {
                grids.put(west, temp2);
            }

            temp2 = getOtherUTMSubGrids(gameObject, "NorthEeast");
            if (temp2.size() > 0) {
                grids.put(northEast, temp2);
            }

            temp2 = getOtherUTMSubGrids(gameObject, "NorthWest");
            if (temp2.size() > 0) {
                grids.put(northWest, temp2);
            }

            temp2 = getOtherUTMSubGrids(gameObject, "SouthEast");
            if (temp2.size() > 0) {
                grids.put(southEast, temp2);
            }

            temp2 = getOtherUTMSubGrids(gameObject, "SouthWest");
            if (temp2.size() > 0) {
                grids.put(southWest, temp2);
            }


        }


        return grids;
    }

    public static List<SubUTM> getOtherUTMSubGrids(GameObject gameObject, String direction) {

        String x = gameObject.getSubUtmLat();
        String y = gameObject.getSubUtmLong();

        String alpha = gameObject.getSubUtmLat().substring(gameObject.getSubUtmLat().length() - 1);
        int alphaIndex = UTMConvert.latValues.indexOf(alpha);
        int latInt = Integer.valueOf(gameObject.getSubUtmLat().substring(0, 1));
        int longInt = Integer.valueOf(gameObject.getSubUtmLong());

        List<SubUTM> grids = new ArrayList<>();


        switch (direction) {
            case "North":
                //only true if lat alpha = X. and int = 8
                if (alpha.equals("X") && latInt == 8) {
                    //max 3..else 2...
                    grids.add(new SubUTM("1C", String.valueOf(longInt)));
                    if (longInt > 0 && longInt < 59) {
                        //add in 3 grids....
                        grids.add(new SubUTM("1C", String.valueOf(longInt + 1)));
                        grids.add(new SubUTM("1C", String.valueOf(longInt - 1)));
                    } else if (longInt == 0) {
                        grids.add(new SubUTM("1C", String.valueOf(longInt + 1)));

                    } else if (longInt == 59) {
                        grids.add(new SubUTM("1C", String.valueOf(longInt - 1)));
                    }
                }
                break;
            case "South":
                //only true if lat aplha = c and int = 1
                if (alpha.equals("C") && latInt == 1) {
                    grids.add(new SubUTM("8X", String.valueOf(longInt)));
                    if (longInt > 0 && longInt < 59) {
                        //add in 3 grids....
                        grids.add(new SubUTM("8X", String.valueOf(longInt + 1)));
                        grids.add(new SubUTM("8X", String.valueOf(longInt - 1)));
                    } else if (longInt == 0) {
                        grids.add(new SubUTM("8X", String.valueOf(longInt + 1)));

                    } else if (longInt == 59) {
                        grids.add(new SubUTM("8X", String.valueOf(longInt - 1)));
                    }
                }
                break;
            case "East":
                //only true if we have long of 0
                if (longInt == 0) {
                    grids.add(new SubUTM(latInt + alpha, "59"));

                    if (!(alpha.equals("C") && latInt == 1) && !(alpha.equals("X") && latInt == 8)) {
                        grids.add(new SubUTM(alpha.equals("X") ? latInt + 1 + "C" : latInt + UTMConvert.latValues.get(alphaIndex + 1), "59"));
                        grids.add(new SubUTM(alpha.equals("C") ? latInt - 1 + "X" : latInt + UTMConvert.latValues.get(alphaIndex - 1), "59"));

                    } else if (alpha.equals("C") && latInt == 1) {
                        grids.add(new SubUTM(alpha.equals("X") ? latInt + 1 + "C" : latInt + UTMConvert.latValues.get(alphaIndex + 1), "59"));

                    } else if (alpha.equals("X") && latInt == 8) {
                        grids.add(new SubUTM(alpha.equals("C") ? latInt - 1 + "X" : latInt + UTMConvert.latValues.get(alphaIndex - 1), "59"));
                    }

                }
                break;
            case "West":
                //only true if long of 59
                if (longInt == 59) {
                    grids.add(new SubUTM(latInt + alpha, "1"));

                    if (!(alpha.equals("C") && latInt == 1) && !(alpha.equals("X") && latInt == 8)) {
                        grids.add(new SubUTM(alpha.equals("X") ? latInt + 1 + "C" : latInt + UTMConvert.latValues.get(alphaIndex + 1), "1"));
                        grids.add(new SubUTM(alpha.equals("C") ? latInt - 1 + "X" : latInt + UTMConvert.latValues.get(alphaIndex - 1), "1"));

                    } else if (alpha.equals("C") && latInt == 1) {
                        grids.add(new SubUTM(alpha.equals("X") ? latInt + 1 + "C" : latInt + UTMConvert.latValues.get(alphaIndex + 1), "1"));

                    } else if (alpha.equals("X") && latInt == 8) {
                        grids.add(new SubUTM(alpha.equals("C") ? latInt - 1 + "X" : latInt + UTMConvert.latValues.get(alphaIndex - 1), "1"));
                    }

                }
                break;
            case "SouthWest": //very rare....only 1 case...
                if (alpha.equals("C") && latInt == 1 && longInt == 0) {
                    grids.add(new SubUTM("8X", "59"));
                }
                break;
            case "SouthEast":
                if (alpha.equals("C") && latInt == 1 && longInt == 59) {
                    grids.add(new SubUTM("8X", "0"));
                }
                break;
            case "NorthEast":
                if (alpha.equals("X") && latInt == 8 && longInt == 0) {
                    grids.add(new SubUTM("1C", "0"));
                }
                break;
            case "NorthWest":
                if (alpha.equals("X") && latInt == 8 && longInt == 59) {
                    grids.add(new SubUTM("1C", "59"));
                }
                break;

        }

        return grids;
    }


    public static List<SubUTM> getCurrentUTMSubGrids(GameObject gameObject) {

        String x = gameObject.getSubUtmLat();
        String y = gameObject.getSubUtmLong();

        String alpha = gameObject.getSubUtmLat().substring(gameObject.getSubUtmLat().length() - 1);
        int alphaIndex = UTMConvert.latValues.indexOf(alpha);
        int latInt = Integer.valueOf(gameObject.getSubUtmLat().substring(0, 1));
        int longInt = Integer.valueOf(gameObject.getSubUtmLong());

        SubUTM south, north, east, west, southEast, southWest, northEast, northWest;
        try {
            south = new SubUTM(latInt + UTMConvert.latValues.get(alphaIndex - 1), String.valueOf(longInt));
        } catch (Exception e) {
            south = null;
        }
        try {
            north = new SubUTM(latInt + UTMConvert.latValues.get(alphaIndex + 1), String.valueOf(longInt));
        } catch (Exception e) {
            north = null;
        }
        try {
            west = new SubUTM(latInt + alpha, String.valueOf(longInt - 1));
        } catch (Exception e) {
            west = null;

        }
        try {
            east = new SubUTM(latInt + alpha, String.valueOf(longInt + 1));
        } catch (Exception e) {
            east = null;

        }
        try {
            southEast = new SubUTM(latInt + UTMConvert.latValues.get(alphaIndex - 1), String.valueOf(longInt + 1));
        } catch (Exception e) {
            southEast = null;

        }
        try {
            southWest = new SubUTM(latInt + UTMConvert.latValues.get(alphaIndex - 1), String.valueOf(longInt - 1));
        } catch (Exception e) {
            southWest = null;

        }
        try {
            northEast = new SubUTM(latInt + UTMConvert.latValues.get(alphaIndex + 1), String.valueOf(longInt + 1));
        } catch (Exception e) {
            northEast = null;

        }
        try {
            northWest = new SubUTM(latInt + UTMConvert.latValues.get(alphaIndex + 1), String.valueOf(longInt - 1));
        } catch (Exception e) {
            northWest = null;
        }


        List<SubUTM> grids = new ArrayList<>();

        //we are at the top
        if (alpha.equals("X") && latInt == 8) {
            //simple rules..if we are at top, and not sides, we can add east, west, south, south east, south west.
            //if we are a top and left side, we can add west, south, south west
            //if we are at top and right side we can add east, south, south east

            //at top we can always add....south.
            grids.add(south);

            if (longInt > 0 && longInt < 59) {
                grids.add(southEast);  //south east
                grids.add(southWest);  //south west
                grids.add(east);  // east
                grids.add(west); //west
            } else if (longInt == 0) {
                grids.add(east);  // east
                grids.add(south);  //south east

            } else if (longInt == 59) {
                grids.add(west); //west
                grids.add(southWest);  //south west
            }

        }

        //we are at the bottom
        else if (alpha.equals("C") && latInt == 1) {

            grids.add(north);
            //if we are bottom and not sides we can add north, east, west, north east, north west
            //if we are at bottom and left, we can add north, north west, west
            //if we are at bottom and right, we can add north, north east, east
            if (longInt > 0 && longInt < 59) {
                grids.add(northEast);  //south east
                grids.add(northWest);  //south west
                grids.add(east);  // east
                grids.add(west); //west
            } else if (longInt == 0) {
                grids.add(east);  // east
                grids.add(northEast);  //south east

            } else if (longInt == 59) {
                grids.add(west); //west
                grids.add(northWest);  //south west
            }

        } else if (longInt == 0) {  //we are left.
            //if we are at left (not top or bottom_ we can add north, south, east, north east, south east
            grids.add(north);
            grids.add(south);
            grids.add(east);
            grids.add(northEast);
            grids.add(southEast);

        } else if (longInt == 59) { //we are right.
            //if we are at right (not top or bottom) we can add north south, west, north west, south west...
            grids.add(north);
            grids.add(south);
            grids.add(west);
            grids.add(northWest);
            grids.add(northEast);

        }


        return grids;
    }


    public static void main(String[] args) {
        //normal...these will go intests eventually...once ive fixed it!
        System.out.println("normal");
        GameObject gameObject = new GameObject();
        gameObject.setUtmLong(("31"));
        gameObject.setUtmLat("U");
        gameObject.setSubUtmLat("4V");
        gameObject.setSubUtmLong("9");
        Map<UTM, List<SubUTM>> grids = get3by3Grid(gameObject);

        for (List<SubUTM> list : grids.values()) {
            for (SubUTM subUTM : list) {
                System.out.println("we have a grid " + subUTM.getSubUtmLat() + subUTM.getSubUtmLong());
            }
        }

        //bottom left
        System.out.println("bottom left");
        gameObject = new GameObject();
        gameObject.setUtmLong(("31"));
        gameObject.setUtmLat("U");
        gameObject.setSubUtmLat("1C");
        gameObject.setSubUtmLong("0");
        grids = get3by3Grid(gameObject);

        for (List<SubUTM> list : grids.values()) {
            for (SubUTM subUTM : list) {
                System.out.println("we have a grid " + subUTM.getSubUtmLat() + subUTM.getSubUtmLong());
            }
        }

        //bottom left
        System.out.println("bottom right");
        gameObject = new GameObject();
        gameObject.setUtmLong(("31"));
        gameObject.setUtmLat("U");
        gameObject.setSubUtmLat("1C");
        gameObject.setSubUtmLong("59");
        grids = get3by3Grid(gameObject);

        for (List<SubUTM> list : grids.values()) {
            for (SubUTM subUTM : list) {
                System.out.println("we have a grid " + subUTM.getSubUtmLat() + subUTM.getSubUtmLong());
            }
        }

        //bottom left
        System.out.println("top left");
        gameObject = new GameObject();
        gameObject.setUtmLong(("31"));
        gameObject.setUtmLat("U");
        gameObject.setSubUtmLat("8X");
        gameObject.setSubUtmLong("0");
        grids = get3by3Grid(gameObject);

        for (List<SubUTM> list : grids.values()) {
            for (SubUTM subUTM : list) {
                System.out.println("we have a grid " + subUTM.getSubUtmLat() + subUTM.getSubUtmLong());
            }
        }

        //bottom left
        System.out.println("top right");
        gameObject = new GameObject();
        gameObject.setUtmLong(("31"));
        gameObject.setUtmLat("U");
        gameObject.setSubUtmLat("8X");
        gameObject.setSubUtmLong("59");
        grids = get3by3Grid(gameObject);

        for (List<SubUTM> list : grids.values()) {
            for (SubUTM subUTM : list) {
                System.out.println("we have a grid " + subUTM.getSubUtmLat() + subUTM.getSubUtmLong());
            }
        }

        //bottom left
        System.out.println("top ");
        gameObject = new GameObject();
        gameObject.setUtmLong(("31"));
        gameObject.setUtmLat("U");
        gameObject.setSubUtmLat("8X");
        gameObject.setSubUtmLong("10");
        grids = get3by3Grid(gameObject);

        for (List<SubUTM> list : grids.values()) {
            for (SubUTM subUTM : list) {
                System.out.println("we have a grid " + subUTM.getSubUtmLat() + subUTM.getSubUtmLong());
            }
        }

        //bottom left
        System.out.println("bottom");
        gameObject = new GameObject();
        gameObject.setUtmLong(("31"));
        gameObject.setUtmLat("U");
        gameObject.setSubUtmLat("1C");
        gameObject.setSubUtmLong("10");
        grids = get3by3Grid(gameObject);

        for (List<SubUTM> list : grids.values()) {
            for (SubUTM subUTM : list) {
                System.out.println("we have a grid " + subUTM.getSubUtmLat() + subUTM.getSubUtmLong());
            }
        }
    }
}
