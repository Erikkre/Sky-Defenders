package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Random;

public class TinyBird {
    public float width, height, x, y;
    protected float camWidth, camHeight, edge, sizeRatio, finalSizeRatio;
    private int sizeVariance;
    public boolean isOffCam;
    Animation animation;
    private Random r = new Random();
    public TinyBird(Animation animation){
        this.animation=animation;
        height=((TextureRegion)animation.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)animation.getKeyFrames()[0]).getRegionWidth();

        sizeVariance=20;    //its 70 wide so 50-90 will be its size
        sizeRatio=0.9f;

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio= ((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height*=finalSizeRatio;
        //edge = (camWidth)-width/3;
        //System.out.println("Height after: " + height+ " width: " + width);



        //x=(width/3 + r.nextInt((int)(edge-(2*width)/3)));
    }
}
