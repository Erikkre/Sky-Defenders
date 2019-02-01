package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.BgHandler;

import java.util.ArrayList;
import java.util.Random;

public class TinyBird {
    public float width, height, camWidth, camHeight;
    protected float sizeRatio, finalSizeRatio;
    private int sizeVariance;
    public Animation animation;
    public float flapRandomFactor;

    public Vector2 pos = new Vector2(), vel=new Vector2(), posChange = new Vector2();
    public float  rotation;

    public boolean inCamView;

    private Random r = new Random();
    public TinyBird(Animation animation, ArrayList<Float> flapSpeedIntervals, float camWidth, float camHeight){
        flapRandomFactor=r.nextFloat()*0.5f;

        this.camHeight=camHeight;
        this.camWidth=camWidth;

        this.animation=animation;
        height=((TextureRegion)animation.getKeyFrames()[0]).getRegionHeight();
        width=((TextureRegion)animation.getKeyFrames()[0]).getRegionWidth();

        float minSpeed= -1.6f, speedRange = 3.2f;
        vel.x=-0.5f+r.nextFloat();
        vel.y=minSpeed+r.nextFloat()*speedRange;

        if (vel.y>=0)rotation = -(float) Math.toDegrees(Math.atan(vel.y / (vel.x)*10))*0.05f ;
        else rotation = -(float) Math.toDegrees(Math.atan(vel.y / (vel.x)*10))*0.05f;

        sizeVariance=15;    //its 37 wide so 22-52 will be its size before sizeRatio shrinks it
        sizeRatio=0.90f;

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height*=finalSizeRatio;
        //edge = (camWidth)-width/3;
        //System.out.println("Height after: " + height+ " width: " + width);

        pos.x=BgHandler.bgw/2 - BgHandler.bgw/4 + r.nextInt((BgHandler.bgw/2));
        pos.y=  r.nextFloat()*(BgHandler.bgh*2+BgHandler.separatorHeight)  + (camHeight);   //spawn all tiny birds all over next part of wave               * (BgHandler.bgh / 6)

        //if yvel>0 set to anywhere between 100ms per frame to 30ms by multiplying by yVel/(possible y vel:range-min), so as to use percentage of maximum speed to get flap speed relative to yMovement speed

        animation.setFrameDuration(flapSpeedIntervals.get( (int) (((vel.y-minSpeed)/(speedRange))*flapSpeedIntervals.size())));
        System.out.println(flapSpeedIntervals.get((int) (((vel.y-minSpeed)/(speedRange))*flapSpeedIntervals.size())));

        posChange=pos.cpy();
    }


    public boolean isGone() {
        return pos.y -  height/2 < -camWidth/2;
    }

    public void update(float delta){
        if (!inCamView&&pos.y +  height/2 < camHeight) inCamView=true;
        pos.set(BgHandler.horiz.getValue()+posChange.cpy().x, BgHandler.vert.getValue()+posChange.cpy().y);
        if (inCamView)posChange.add(vel.cpy());
    }
}
