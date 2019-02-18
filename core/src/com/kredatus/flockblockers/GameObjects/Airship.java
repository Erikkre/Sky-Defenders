package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;

public class Airship {  //engines, sideThrusters, armors and health are organized as lvl1-lvl5
    private static float rotation;
    private Circle boundingCir;
    private static Vector2 pos, vel=new Vector2(), acc;
    public float gamexvel;
    public static int balloonTextureWidth, balloonTextureHeight; //x and y are at middle of textures, bottom of balloonTexture,top of turretRack
    protected boolean isScrolledDown;
    public float midpointY, midpointX, starty;
    private boolean isAlive;

    public int armor=100, health=100;
//151, 351
    public static int lvl, engineTuning, armorLvl, sideThrust;   //0-4
    public static TextureRegion balloonTexture, turretRackTexture, sideThrustTexture;    //balloonTexture is top part of hot air balloon, turretRack is bottom

    public Airship(int camWidth, int camHeight){
        assignTextures();
        pos=new Vector2(camWidth/2f, camHeight-balloonTextureHeight);
        balloonTextureHeight=balloonTexture.getRegionHeight();balloonTextureWidth=balloonTexture.getRegionWidth();
    }

    public void assignTextures(){
        balloonTexture=AssetHandler.airshipBalloon; turretRackTexture=AssetHandler.airshipturretRack(armorLvl); sideThrustTexture=AssetHandler.airshipSideThruster;
    }

    public static void update(float delta){
        pos.add(vel.cpy().scl(delta));
        if (vel.x>0)vel.x-=1; //slowdown
        else if (vel.x<0)vel.x+=1;

        if (vel.y>0)vel.y-=1;
        else if (vel.y<0)vel.y+=1;
    }

    public static void draw(SpriteBatch batcher){
        batcher.draw(balloonTexture, pos.x-balloonTextureWidth/2f, pos.y,
                pos.x, pos.y, balloonTextureWidth*(1+0.2f*lvl), balloonTextureHeight/2f, 1f, 1f, rotation);  //origin might be x, y or 0, 0

        for (int i=0;i<sideThrust;i++){ //starting at bottom of balloon, draw different number of thrusters
            batcher.draw(sideThrustTexture, pos.x-sideThrustTexture.getRegionWidth()/2f, pos.y+ 0.22693f*balloonTextureHeight + (i*sideThrustTexture.getRegionHeight()),
                    pos.x, pos.y, balloonTextureWidth, balloonTextureHeight, 1, 1, rotation);  //origin might be x, y or 0, 0
        }

        batcher.draw(turretRackTexture, pos.x-turretRackTexture.getRegionWidth()/2f, pos.y-turretRackTexture.getRegionHeight(),
                pos.x, pos.y, turretRackTexture.getRegionWidth(), turretRackTexture.getRegionHeight(),1,1,rotation);
    }
}
