package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;

import org.w3c.dom.css.Rect;

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

    public static int lvl, engineTuning, armorLvl, sideThrustLvl;   //0-4
    public static TextureRegion balloonTexture, rackTexture, sideThrustTexture;    //balloonTexture is top part of hot air balloon, rack is bottom

    //positions 28,31    82,31  110-136 and 137-163

    public static ArrayList<Turret> turretList=new ArrayList<Turret>(13);
    public Polygon rackHitbox, balloonHitbox, prelimBoundPoly1, prelimBoundPoly2;

    public int tW=32, tH=33;

    public Airship(int camWidth, int camHeight) {
        armorLvl=0;
        lvl=2;
        sideThrustLvl=0;

        assignTextures(armorLvl,lvl);
        height=balloonHeight+rackHeight;
        pos=new Vector2(camWidth/2f, camHeight-balloonHeight-50);

        assignBounds();

        assignRackPositions(pos.x-rackWidth/2f, balloonHeight);
        //for (int i=0;i<positions.size()/2;i+=2){
            turretList.add(new Turret('f',positions.get(positions.size()/2)));
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
        float x =pos.x, y=pos.y-2, rB=balloonWidth/2f, hB=balloonHeight;

                prelimBoundPoly2= new Polygon(new float[]{x - balloonWidth/2f,y,   x - balloonWidth/2f,y+hB,    x + balloonWidth/2f,y+hB,    x + balloonWidth/2f,y});//left side
                prelimBoundPoly1= new Polygon(new float[]{x - rackWidth/2f,y,   x - rackWidth/2f,y - rackHeight,   x + rackWidth/2f,y - rackHeight,  x + rackWidth/2f,y});

                balloonHitbox = new Polygon(new float[]{
                        x, y +hB,     x - rB * 0.60f, y + hB * 0.92f,       x - rB * 0.98f, y + hB * 0.67f,          x - rB * 0.90f, y + hB * 0.37f,      x - rB * 0.40f, y,  //top to bottom left of burner
                        x + rB * 0.40f, y,        x + rB * 0.90f, y + hB * 0.37f,    x + rB * 0.98f, y + hB * 0.67f,          x + rB * 0.60f, y + hB * 0.92f //to top of balloon
                });

                if (lvl==0){
                rackHitbox = new Polygon(new float[]{
                        x - tW * 2, y ,         x - tW * 2, y - 1 * tH ,//bottom left rack
                        x + tW * 2, y - 1 * tH ,     x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==1){
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,//bottom left rack
                            x + tW * 2, y - 2 * tH ,   x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==2) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,     x - tW * 1.5f, y - 3 * tH ,//bottom left rack
                            x + tW * 1.5f, y - 3 * tH ,        x + tW * 2, y - 2 * tH ,    x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==3) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,     x - tW * 1.5f, y - 4 * tH ,//bottom left rack
                            x + tW * 1.5f, y - 4 * tH ,        x + tW * 2, y - 2 * tH ,    x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==4) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,     x - tW * 1.5f, y - 4 * tH ,      x - tW * 1f, y - 5 * tH ,//bottom left rack
                            x + tW * 1f, y - 5 * tH , x + tW * 1.5f, y - 4 * tH , x + tW * 2, y - 2 * tH , x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                }

        rackHitbox.setOrigin(x, y);
        rackHitbox.setRotation(rotation);
    }

    private void assignRackPositions(float leftXOfAirship, int balloonHeight) {
        for (int i=0;i<=lvl;i++) {
            if (i<=1) {
                for (int j=0;j<4;j++) {
                    positions.add(new Vector2(leftXOfAirship+j*tW+tW/2f,   pos.y-     i*tH - (tH/2)-1 ));
                }
            } else if (i<=3) {
                for (int j=0;j<3;j++) {
                    positions.add(new Vector2(leftXOfAirship+j*tW+(tW/2f)+tW/2f,pos.y-i*tH - (tH/2)-1 ));
                }
            } else if (i<=4) {
                for (int j=0;j<2;j++) {
                    positions.add(new Vector2(leftXOfAirship+j*tW+(tW)+tW/2f, pos.y-  i*tH - (tH/2)-1 ));
                }
            }
        }
    }

    private void assignTextures(int armorLvl, int lvl) {
        balloonTexture=AssetHandler.airshipBalloon;rackTexture=AssetHandler.airshipRack(armorLvl,lvl+1, tH);sideThrustTexture=AssetHandler.airshipSideThruster;
        balloonWidth=(int) ((balloonTexture.getRegionWidth())*(1+0.2f*lvl)); balloonHeight=balloonTexture.getRegionHeight();
        thrusterWidth=sideThrustTexture.getRegionWidth(); thrusterHeight=(int) (sideThrustTexture.getRegionHeight()*(1+0.2f*sideThrustLvl));
        rackWidth=rackTexture.getRegionWidth(); rackHeight=rackTexture.getRegionHeight();
    }

    public  void update(float delta) {
        //pos.add(vel.cpy().scl(delta));
        //rackHitbox.translate(vel.cpy().scl(delta).x,vel.cpy().scl(delta).y);
        //rackHitbox.setRotation(rotation);

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

        //for (int i=0;i<sideThrustLvl+1;i++){ //starting at bottom of balloon, draw different number of thrusters
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