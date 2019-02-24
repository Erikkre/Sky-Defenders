package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;

import java.util.ArrayList;

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

    public Airship(int camWidth, int camHeight) {
        armorLvl=0;
        lvl=0;
        sideThrust=3;

        assignTextures(armorLvl,lvl);
        height=balloonHeight+rackHeight;
        pos=new Vector2(camWidth/2, camHeight-balloonHeight);
        assignRackPositions(camWidth/2-rackWidth/2,camHeight, balloonHeight);
        for (int i=0;i<positions.size();i++){
            turretList.add(new Turret('f',positions.get(i)));
        }
        System.out.println(turretList.size());

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
        balloonWidth=balloonTexture.getRegionWidth()/2; balloonHeight=balloonTexture.getRegionHeight()/2;
        thrusterWidth=sideThrustTexture.getRegionWidth()/2; thrusterHeight=sideThrustTexture.getRegionHeight()/2;
        rackWidth=rackTexture.getRegionWidth(); rackHeight=rackTexture.getRegionHeight();
    }

    public  void update(float delta) {
        pos.add(vel.cpy().scl(delta));

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
        batcher.draw(balloonTexture, pos.x-(balloonWidth*(1+0.2f*lvl))/2f, pos.y,
                balloonWidth/2, balloonHeight/2, balloonWidth*(1+0.2f*lvl), balloonHeight, 1, 1, rotation);  //origin might be x, y or 0, 0

        for (int i=0;i<sideThrust+1;i++){ //starting at bottom of balloon, draw different number of thrusters
            batcher.draw(sideThrustTexture, pos.x-thrusterWidth/2f, pos.y+ 0.22693f*balloonHeight + (i*thrusterHeight),
                    thrusterWidth/2, thrusterHeight/2, thrusterWidth, thrusterHeight, 1, 1, rotation);  //origin might be x, y or 0, 0
        }

        batcher.draw(rackTexture, pos.x-rackWidth/2f, pos.y-rackHeight,
                rackWidth/2, rackHeight/2, rackWidth, rackHeight,1,1,rotation);

        for (Turret i : turretList) {
            batcher.draw(i.texture, i.position.x-i.width/2, i.position.y-i.height/2,
                    i.width/2, i.height/2, i.width, i.height, 1f, 1f, i.getRotation());
        }
    }
}