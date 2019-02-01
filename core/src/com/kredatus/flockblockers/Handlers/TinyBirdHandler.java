package com.kredatus.flockblockers.Handlers;


import com.kredatus.flockblockers.GameObjects.TinyBird;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TinyBirdHandler {
    public static ConcurrentLinkedQueue<TinyBird> tinyBirdQueue=new ConcurrentLinkedQueue<TinyBird>();
    public static final int birdAmount =100;
    private static Random r = new Random();
    private static ArrayList<Float> flapSpeedIntervals=new ArrayList<Float>();
    public TinyBirdHandler(){
        flapSpeedIntervals();

    }

    public static void addTinyBirdsNextCity(float camWidth, float camHeight){
        for (int i=0;i<birdAmount+r.nextInt(15);i++){   //10-25 tinyBirds
            tinyBirdQueue.add(new TinyBird(AssetHandler.tinyAnims[r.nextInt(AssetHandler.tinyAnims.length)], flapSpeedIntervals, camWidth, camHeight));
        }

    }
    protected void flapSpeedIntervals(){
        for (float i=0.100f; i>=0.040f;i-=0.010f){
            flapSpeedIntervals.add(i); //slow to fast (number is ms/frame)
        }
        //System.out.println(flapSpeedIntervals.toString());
    }

    public void update(float delta){
        for (TinyBird i : tinyBirdQueue){
            i.update(delta);
            if (i.isGone()) tinyBirdQueue.remove(i);
        }
    }
}
