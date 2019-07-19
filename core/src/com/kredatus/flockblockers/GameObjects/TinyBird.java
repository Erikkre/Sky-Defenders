package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.NonGameHandlerScreens.Loader;

import java.util.ArrayList;
import java.util.Random;

import static com.kredatus.flockblockers.Handlers.BgHandler.bgh;
import static com.kredatus.flockblockers.Handlers.BgHandler.bgw;
import static com.kredatus.flockblockers.Handlers.BgHandler.separatorHeight;

public class TinyBird {
    public float width, height, camWidth, camHeight, addedY;
    protected float sizeRatio, finalSizeRatio;
    private int sizeVariance;
    public Animation<TextureRegion> animation;
    public float flapRandomFactor;

    public Vector2 pos = new Vector2(), vel=new Vector2(), posChange;
    public float  rotation;

    public boolean inCamView;

    private Random r = new Random();
    public TinyBird(ArrayList<Float> flapSpeedIntervals, float camWidth, float camHeight){
        flapRandomFactor=r.nextFloat()*0.5f;

        this.camHeight=camHeight;
        this.camWidth=camWidth;

        float minSpeed= -1.6f, speedRange = 3.2f;
        vel.x=-0.5f+r.nextFloat();
        vel.y=minSpeed+r.nextFloat()*speedRange;

        if (vel.y>=0)rotation = -(float) Math.toDegrees(Math.atan(vel.y / (vel.x)*10))*0.05f ;
        else rotation = -(float) Math.toDegrees(Math.atan(vel.y / (vel.x)*10))*0.05f;

        sizeVariance=15;    //its 37 wide so 22-52 will be its size before sizeRatio shrinks it
        sizeRatio=0.90f;

        //edge = (camWidth)-width/3;
        //System.out.println("Height after: " + height+ " width: " + width);

        pos.x= bgw/2 - bgw/4 + r.nextInt((bgw/2));
        pos.y=  r.nextFloat()*(bgh*2 + separatorHeight)  + (camHeight);   //spawn all tiny birds all over next part of wave               * (BgHandler.bgh / 6)

        //if yvel>0 set to anywhere between 100ms per frame to 30ms by multiplying by yVel/(possible y vel:range-min), so as to use percentage of maximum speed to get flap speed relative to yMovement speed

        float frameDuration=flapSpeedIntervals.get( (int) (((vel.y-minSpeed)/(speedRange))*flapSpeedIntervals.size()));
        if      ((pos.y>separatorHeight&&pos.y<(bgh/2) +separatorHeight) || (pos.y>separatorHeight+bgh*(1.5f)&&pos.y<separatorHeight+(bgh*2))) animation = new Animation<TextureRegion>(frameDuration,(TextureRegion[]) Loader.tinyAnims[4+r.nextInt(5)].getKeyFrames());
        else if ( pos.y>separatorHeight+(bgh/2)&&pos.y<separatorHeight+(bgh*1.5f)) animation = new Animation<TextureRegion>(frameDuration,(TextureRegion[]) Loader.tinyAnims[6+r.nextInt(4)].getKeyFrames());
        else if ((pos.y>separatorHeight+(bgh*2)&&pos.y<separatorHeight+((bgh*2)+separatorHeight)) || (pos.y>camHeight&&pos.y<separatorHeight)) animation = new Animation<TextureRegion>(frameDuration,(TextureRegion[]) Loader.tinyAnims[r.nextInt(6)].getKeyFrames());

        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);


        height=(animation.getKeyFrames()[0]).getRegionHeight();
        width=(animation.getKeyFrames()[0]).getRegionWidth();
        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height*=finalSizeRatio;


        //System.out.println("vel.y: "+vel.y+", vel.y-minSpeed: "+(vel.y-minSpeed)+", flapSpeed: "+flapSpeedIntervals.get( (int) (((vel.y-minSpeed)/(speedRange))*flapSpeedIntervals.size())));
        posChange=pos.cpy();
    }

    public boolean isGone() {
        return pos.y < 0;
    }

    public void update(float delta){
        if (!inCamView&&pos.y +  height/2 < camHeight) inCamView=true;
        pos.set(BgHandler.horiz.get()+posChange.cpy().x, BgHandler.vert.get()+posChange.cpy().y+addedY);
        if (inCamView)posChange.add(vel.cpy());
    }
}
