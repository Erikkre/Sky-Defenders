package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Airship {  //engines, sideThrusters, armors and health are organized as lvl1-lvl5
    private static float rotation;
    private Circle boundingCir;
    private static Vector2 pos, vel=new Vector2(), acc;
    public float gamexvel;
    public static int balloonWidth, balloonHeight, rackWidth, rackHeight, thrusterWidth, thrusterHeight, height; //x and y are at middle of textures, bottom of balloonTexture,top of rack
    protected boolean isScrolledDown;
    public float midpointY, midpointX, starty;
    private boolean isAlive;
    private ArrayList<Vector2> positions = new ArrayList<Vector2>(16);

    public int armor=100, health=100;

    public static int lvl, engineTuning, armorLvl, sideThrust;   //0-4
    public static TextureRegion balloonTexture, rackTexture, sideThrustTexture;    //balloonTexture is top part of hot air balloon, rack is bottom

    //positions 28,31    82,31  110-136 and 137-163

    public static ArrayList<Turret> turretList=new ArrayList<Turret>(13);
    public Polygon boundingPoly, wideBoundingPoly;
    Circle circle = new Circle();

    public int turretW=54, turretH=54;

    public Airship(int camWidth, int camHeight) {
        armorLvl=0;
        lvl=3;
        sideThrust=3;

        assignTextures(armorLvl,lvl);
        height=balloonHeight+rackHeight;
        pos=new Vector2(camWidth/2f, camHeight-balloonHeight);

        assignBounds();

        assignRackPositions(camWidth/2-rackWidth/2,camHeight, balloonHeight);
        //for (int i=0;i<positions.size()/2;i+=2){
            //turretList.add(new Turret('f',positions.get(6)));
        //}

        /*
        int j=0;
        for (Turret i : turretList){
                for (int k=0;k<j;k++){
                    i.lvlUp();
                }
                j++;
            System.out.println(i.dmg);
            }
*/
    }

    private void assignBounds(){
        float x =pos.x, y=pos.y;
                if (lvl==0){
                boundingPoly = new Polygon(new float[]{x, y +balloonHeight + 20, x - (balloonWidth / 2f) * 0.5f*(1+0.2f*lvl), y + balloonHeight * 0.95f, x - (balloonWidth / 2f)*(1+0.2f*lvl) * 0.9f, y + balloonHeight * 0.75f, x - (balloonWidth / 2f)*(1+0.2f*lvl), y + balloonHeight * 0.55f, x - (balloonWidth / 2f) * 0.95f*(1+0.2f*lvl), y + balloonHeight * 0.40f, x - (balloonWidth / 2f) * 0.40f*(1+0.2f*lvl), y,  //top to bottom left of burner
                        x - turretW * 2, y - 5,     x - turretW * 2, y - 1 * turretH - 5,//bottom left rack

                        x + turretW * 2, y - 1 * turretH - 5,   x + turretW * 2, y - 5,     //bottom right of burner
                        x + (balloonWidth / 2f)*(1+0.2f*lvl) * 0.40f, y, x + (balloonWidth / 2f)*(1+0.2f*lvl) * 0.95f, y + balloonHeight * 0.40f, x + (balloonWidth / 2f)*(1+0.2f*lvl), y + balloonHeight * 0.55f, x + (balloonWidth / 2f)*(1+0.2f*lvl) * 0.9f, y + balloonHeight * 0.75f, x + (balloonWidth / 2f) * 0.5f*(1+0.2f*lvl), y + balloonHeight * 0.95f //to top of balloon

                        }
                    );
                } else if (lvl==1){
                    boundingPoly = new Polygon(new float[]{x, y + balloonHeight + 20, x - (balloonWidth / 2f) * 0.5f, y + balloonHeight * 0.95f, x - (balloonWidth / 2f) * 0.9f, y + balloonHeight * 0.75f, x - (balloonWidth / 2f), y + balloonHeight * 0.55f, x - (balloonWidth / 2f) * 0.95f, y + balloonHeight * 0.40f, x - (balloonWidth / 2f) * 0.40f, y,  //top to bottom left of burner
                            x - turretW * 2, y - 5,     x - turretW * 2, y - 2 * turretH - 5,//bottom left rack

                            x + turretW * 2, y - 2 * turretH - 5,   x + turretW * 2, y - 5,     //bottom right of burner
                            x + (balloonWidth / 2f) * 0.40f, y, x + (balloonWidth / 2f) * 0.95f, y + balloonHeight * 0.40f, x + (balloonWidth / 2f), y + balloonHeight * 0.55f, x + (balloonWidth / 2f) * 0.9f, y + balloonHeight * 0.75f, x + (balloonWidth / 2f) * 0.5f, y + balloonHeight * 0.95f //to top of balloon
                        }
                    );
                } else if (lvl==2) {
                    boundingPoly = new Polygon(new float[]{x, y + balloonHeight + 20, x - (balloonWidth / 2f) * 0.5f, y + balloonHeight * 0.95f, x - (balloonWidth / 2f) * 0.9f, y + balloonHeight * 0.75f, x - (balloonWidth / 2f), y + balloonHeight * 0.55f, x - (balloonWidth / 2f) * 0.95f, y + balloonHeight * 0.40f, x - (balloonWidth / 2f) * 0.40f, y,  //top to bottom left of burner
                            x - turretW * 2, y - 5,    x - turretW * 2, y - 2 * turretH - 5,     x - turretW * 1.5f, y - 3 * turretH - 5,//bottom left rack

                            x + turretW * 1.5f, y - 3 * turretH - 5,        x + turretW * 2, y - 2 * turretH - 5,    x + turretW * 2, y - 5,     //bottom right of burner
                            x + (balloonWidth / 2f) * 0.40f, y, x + (balloonWidth / 2f) * 0.95f, y + balloonHeight * 0.40f, x + (balloonWidth / 2f), y + balloonHeight * 0.55f, x + (balloonWidth / 2f) * 0.9f, y + balloonHeight * 0.75f, x + (balloonWidth / 2f) * 0.5f, y + balloonHeight * 0.95f //to top of balloon
                    }
                    );
                } else if (lvl==3) {
                    boundingPoly = new Polygon(new float[]{x, y + balloonHeight + 20, x - (balloonWidth / 2f) * 0.5f, y + balloonHeight * 0.95f, x - (balloonWidth / 2f) * 0.9f, y + balloonHeight * 0.75f, x - (balloonWidth / 2f), y + balloonHeight * 0.55f, x - (balloonWidth / 2f) * 0.95f, y + balloonHeight * 0.40f, x - (balloonWidth / 2f) * 0.40f, y,  //top to bottom left of burner
                            x - turretW * 2, y - 5,    x - turretW * 2, y - 2 * turretH - 5,     x - turretW * 1.5f, y - 4 * turretH - 5,//bottom left rack

                            x + turretW * 1.5f, y - 4 * turretH - 5,        x + turretW * 2, y - 2 * turretH - 5,    x + turretW * 2, y - 5,     //bottom right of burner
                            x + (balloonWidth / 2f) * 0.40f, y, x + (balloonWidth / 2f) * 0.95f, y + balloonHeight * 0.40f, x + (balloonWidth / 2f), y + balloonHeight * 0.55f, x + (balloonWidth / 2f) * 0.9f, y + balloonHeight * 0.75f, x + (balloonWidth / 2f) * 0.5f, y + balloonHeight * 0.95f //to top of balloon
                    }
                    );
                } else if (lvl==4) {
                    boundingPoly = new Polygon(new float[]{x, y + balloonHeight + 20, x - (balloonWidth / 2f) * 0.5f, y + balloonHeight * 0.95f, x - (balloonWidth / 2f) * 0.9f, y + balloonHeight * 0.75f, x - (balloonWidth / 2f), y + balloonHeight * 0.55f, x - (balloonWidth / 2f) * 0.95f, y + balloonHeight * 0.40f, x - (balloonWidth / 2f) * 0.40f, y,  //top to bottom left of burner
                            x - turretW * 2, y - 5, x - turretW * 2, y - 2 * turretH - 5, x - turretW * 1.5f, y - 4 * turretH - 5, x - turretW * 1f, y - 5 * turretH - 15,//bottom left rack

                            x + turretW * 1f, y - 5 * turretH - 15, x + turretW * 1.5f, y - 4 * turretH - 5, x + turretW * 2, y - 2 * turretH - 5, x + turretW * 2, y - 5,     //bottom right of burner
                            x + (balloonWidth / 2f) * 0.40f, y, x + (balloonWidth / 2f) * 0.95f, y + balloonHeight * 0.40f, x + (balloonWidth / 2f), y + balloonHeight * 0.55f, x + (balloonWidth / 2f) * 0.9f, y + balloonHeight * 0.75f, x + (balloonWidth / 2f) * 0.5f, y + balloonHeight * 0.95f //to top of balloon
                    }
                    );
                }

        boundingPoly.setOrigin(x, y);
        boundingPoly.setRotation(rotation);
    }

    private void assignRackPositions(int leftXOfAirship, int camHeight, int balloonHeight) {
        for (int i=0;i<=lvl;i++) {
            if (i<=1) {
                for (int j=0;j<4;j++) {
                    positions.add(new Vector2(leftXOfAirship+j*55+Turret.width/2,   camHeight-balloonHeight-i*57 - 33));
                }
            } else if (i<=3) {
                for (int j=0;j<3;j++) {
                    positions.add(new Vector2(leftXOfAirship+j*55+29+Turret.width/2,camHeight-balloonHeight-i*57 - 33));
                }
            } else if (i<=4) {
                for (int j=0;j<2;j++) {
                    positions.add(new Vector2(leftXOfAirship+j*55+56+Turret.width/2, camHeight-balloonHeight-i*57 - 33));
                }
            }
        }
    }

    private void assignTextures(int armorLvl, int lvl) {
        balloonTexture=AssetHandler.airshipBalloon;rackTexture=AssetHandler.airshipRack(armorLvl,lvl);sideThrustTexture=AssetHandler.airshipSideThruster;
        balloonWidth=(int) ((balloonTexture.getRegionWidth()/2f)*(1+0.2f*lvl)); balloonHeight=balloonTexture.getRegionHeight()/2;
        thrusterWidth=sideThrustTexture.getRegionWidth()/2; thrusterHeight=sideThrustTexture.getRegionHeight()/2;
        rackWidth=rackTexture.getRegionWidth(); rackHeight=rackTexture.getRegionHeight();
    }

    public  void update(float delta) {
        pos.add(vel.cpy().scl(delta));
        boundingPoly.translate(vel.cpy().scl(delta).x,vel.cpy().scl(delta).y);
        boundingPoly.setRotation(rotation);

        if (Gdx.input.isTouched(0)){

        }
        for (Turret i : turretList){
            i.position.add(vel.cpy().scl(delta));
            i.update();
        }
        if (vel.x>0)vel.x-=1; //slowdown
        else if (vel.x<0)vel.x+=1;

        if (vel.y>0)vel.y-=1;
        else if (vel.y<0)vel.y+=1;
    }

    public static void draw(SpriteBatch batcher) {
        //System.out.println(1+0.2f*lvl);
        batcher.draw(balloonTexture, pos.x-(balloonWidth)/2f, pos.y,
                balloonWidth/2f, balloonHeight/2f, balloonWidth, balloonHeight, 1, 1, rotation);

        //for (int i=0;i<sideThrust+1;i++){ //starting at bottom of balloon, draw different number of thrusters
            batcher.draw(sideThrustTexture, pos.x-thrusterWidth/2f, pos.y+ 0.18f*balloonHeight ,//+ (thrusterHeight)*i
                    thrusterWidth/2f, thrusterHeight/2f, thrusterWidth, thrusterHeight, 1, 1, rotation);
        //}

        batcher.draw(rackTexture, pos.x-rackWidth/2f, pos.y-rackHeight,
                rackWidth/2f, rackHeight/2f, rackWidth, rackHeight,1,1,rotation);

        for (Turret i : turretList) {
            batcher.draw(i.texture, i.position.x-i.width/2f, i.position.y-i.height/2f,
                    i.width/2f, i.height/2f, i.width, i.height, 1f, 1f, i.getRotation());
        }
    }

    public void hit(int origBirdHealth){
        health-=origBirdHealth;
    }
}