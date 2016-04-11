package mobile.che.com.oddymobstar.chemobile.activity.handler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.security.NoSuchAlgorithmException;

import mobile.che.com.oddymobstar.chemobile.activity.ProjectCheActivity;
import mobile.che.com.oddymobstar.chemobile.activity.controller.ProjectCheController;
import mobile.che.com.oddymobstar.chemobile.model.GameObject;
import mobile.che.com.oddymobstar.chemobile.util.Configuration;
import mobile.che.com.oddymobstar.chemobile.util.map.UTMGridCreator;
import util.Tags;
import util.map.SubUTM;
import util.map.UTM;


/**
 * Created by timmytime on 06/12/15.
 */
public class MessageHandler extends Handler {

    private final ProjectCheController controller;
    private final ProjectCheActivity main;

    public MessageHandler(ProjectCheActivity main, ProjectCheController controller) {
        this.main = main;
        this.controller = controller;
    }

    public void handleGameObject() {
        if (controller != null) {

            if (controller.fragmentHandler.gameFrag != null) {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.fragmentHandler.gameFrag.refreshAdapter();
                    }
                });

            }
        }
    }


    public void handleAlliance() {
        if (controller != null) {
            if (controller.fragmentHandler.gridFrag != null) {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.fragmentHandler.gridFrag.refreshAdapter();
                    }
                });

            }
        }
    }

    public void handleVidiNews(final String message) {
        if (controller != null) {
            if (controller.fragmentHandler.vidiPrintFragment != null) {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.materialsHelper.vidiPrintView.animateText(message);
                        // controller.fragmentHandler.vidiPrintFragment.refreshAdapter(controller.dbHelper.getVidiNews());
                    }
                });

            }
        }
    }

    public void handlePlayerKey(String key) {

        if (controller != null) {
            controller.materialsHandler.handlePlayerKey(key);
            if (controller.progressDialog != null) {

                Log.d("dismiss progress", "dismiss");

                controller.progressDialog.dismiss();



                final Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            controller.progressDialog = new ProgressDialog(main);
                            controller.progressDialog.setMessage("Locating...");
                            controller.progressDialog.setIndeterminate(true);
                            controller.progressDialog.show();
                        }
                    }, 100);
                }

        }

    }

    public void addGameObject(final GameObject gameObject, boolean hasStopped) {


        if (controller != null) {
            controller.mapHandler.addGameObject(gameObject, false);

            if (hasStopped) {

                Log.d("add game object", "object has stopped");
                controller.mapHandler.removeDestination(gameObject);

            }
        }

    }

    public void handleGameObjectDestroyed(final GameObject gameObject) {
        if (controller != null) {

            Log.d("object hit", "object has been destroyed and controller ok");


            //1 navigate to our
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controller.mapHandler.handleCamera(new LatLng(gameObject.getLatitude(), gameObject.getLongitude()), 45, 0, 17);

                    if (controller.mapHandler.getMarkerMap().containsKey(gameObject.getKey())) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                controller.mapHandler.getMarkerMap().get(gameObject.getKey()).remove();
                            }
                        }, 1000);
                    }

                }
            });
            //need to add an impact to it and confirm points lost...actually remove from map as well, and then send confirmation to server to say we are dead.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        controller.cheService.writeToSocket(controller.messageFactory.getMissileHit(gameObject, controller.locationListener.getCurrentLocation(), Tags.GAME_OBJECT_DESTROYED));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void gameObjectHit(final GameObject gameObject) {
        if (controller != null) {

            Log.d("object hit", "object has been hit and controller ok");

            //1 navigate to our
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controller.mapHandler.handleCamera(new LatLng(gameObject.getLatitude(), gameObject.getLongitude()), 45, 0, 17);
                }
            });
            //need to add an impact to it and confirm points lost...
            //send confirmation to server of new points total for player.

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        controller.cheService.writeToSocket(controller.messageFactory.getMissileHit(gameObject, controller.locationListener.getCurrentLocation(), Tags.GAME_OBJECT_HIT));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void moveGameObject(GameObject gameObject) {
        if (controller != null) {
            controller.mapHandler.addPath(gameObject);
            controller.gameController.gameTimer.stopTimer(true);
        }

    }

    public void confirmTarget(GameObject gameObject) {
        if (controller != null) {
            //simples...we add a target circle on the map showing kill radius...yes.
            controller.mapHandler.addSphere(gameObject, gameObject.getImpactRadius(), true);
            controller.gameController.gameTimer.stopTimer(true);

            Log.d("have confirmed target", "so timer should of stopped!");
        }
    }

    public void missileTargetReached(final GameObject gameObject) {


        if (controller != null) {

            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("target reached", "target has been reached by missile " + gameObject.getKey());

            /*
               so 1: we need to animate from our parent destination (which we probably dont have?  yes we do).
             */
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            controller.mapHandler.handleCamera(new LatLng(gameObject.getLatitude(), gameObject.getLongitude()), 45, 0, 19);
                        }
                    }, 3000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            controller.mapHandler.handleCamera(new LatLng(gameObject.getDestLatitude(), gameObject.getDestLongitude()), 45, 0, 17);
                            controller.gameController.mapExplosionTimer.startTimer(gameObject, 4000);
                        }
                    }, 6000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) main.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(3000);
                        }
                    }, 7000);
                }


            });
        }


    }


    public void handleUTMChange(String utm) {

        if (controller != null) {


            controller.materialsHandler.setNavConfigValues();


            final PolygonOptions options = UTMGridCreator.getUTMGrid(new UTM(utm)).strokeColor(main.getResources().getColor(android.R.color.holo_purple));
            main.runOnUiThread(new Runnable() {


                @Override
                public void run() {

                    if (controller.mapHelper.getMyUTM() != null) {
                        controller.mapHelper.getMyUTM().remove();
                    }

                    controller.mapHelper.setMyUTM(controller.mapHelper.getMap().addPolygon(options));

                }
            });
        }
    }

    public void handleSubUTMChange(String subUtm) {

        if (controller != null) {

            controller.materialsHandler.setNavConfigValues();

            controller.configuration = new Configuration(controller.dbHelper.getConfigs());

            //timing can cause this to fail...its no biggy its not likely required in end model.
            UTM utm = null;
            SubUTM subUTM = null;

            try {
                utm = new UTM(controller.configuration.getConfig(Configuration.CURRENT_UTM_LAT).getValue(), controller.configuration.getConfig(Configuration.CURRENT_UTM_LONG).getValue());
                subUTM = new SubUTM(controller.configuration.getConfig(Configuration.CURRENT_SUBUTM_LAT).getValue(), controller.configuration.getConfig(Configuration.CURRENT_SUBUTM_LONG).getValue());
            } catch (Exception e) {
                Log.d("error on utm", "error " + e.getMessage());
            }

            if (utm != null && subUTM != null) {
                PolygonOptions utmOption = UTMGridCreator.getUTMGrid(utm);
                final PolygonOptions options = UTMGridCreator.getSubUTMGrid(subUTM, utmOption).strokeColor(main.getResources().getColor(android.R.color.holo_orange_dark));
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (controller.mapHelper.getMySubUTM() != null) {
                            controller.mapHelper.getMySubUTM().remove();
                        }
                        controller.mapHelper.setMySubUTM(controller.mapHelper.getMap().addPolygon(options));
                    }
                });
            }
        }

    }

    public void handleChat(final String type) {
        if (controller != null) {
            if (controller.fragmentHandler.chatFrag != null && controller.fragmentHandler.chatFrag.isVisible()) {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.fragmentHandler.chatFrag.refreshAdapter(controller.dbHelper.getMessages(type, controller.fragmentHandler.chatFrag.getKey()));
                    }
                });
            }
        }

    }

    public void handleInvite(final String key, final String title) {

     /*   if (controller != null) {
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    android.support.v4.app.FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();


                    controller.fragmentHandler.chatFrag.setCursor(controller.dbHelper.getMessages(Message.ALLIANCE_MESSAGE, key), key, title);

                    transaction.replace(R.id.chat_fragment, controller.fragmentHandler.chatFrag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        } */

    }
/*
    public void handleAllianceMember(final AllianceMember allianceMember, final boolean zoomTo) {

        if (controller != null) {

            Log.d("adding marker", "marker " + allianceMember.getKey() + " lat long is " + allianceMember.getLatLng().toString());


            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (controller.mapHandler.getMarkerMap().containsKey(allianceMember.getKey())) {
                        controller.mapHandler.getMarkerMap().get(allianceMember.getKey()).remove();
                        Log.d("adding marker", "removing marker ");
                    }

                    Marker marker = controller.mapHelper.getMap().addMarker(new MarkerOptions().position(allianceMember.getLatLng()).title(allianceMember.getKey()));

                    if (zoomTo) {
                        controller.mapHandler.handleCamera(allianceMember.getLatLng(),
                                controller.mapHelper.getMap().getCameraPosition().tilt,
                                controller.mapHelper.getMap().getCameraPosition().bearing,
                                controller.mapHelper.getMap().getCameraPosition().zoom);
                    }

                    if (marker != null) {
                        controller.mapHandler.getMarkerMap().put(allianceMember.getKey(), marker);
                    }

                }

            });
        }

    } */
}
