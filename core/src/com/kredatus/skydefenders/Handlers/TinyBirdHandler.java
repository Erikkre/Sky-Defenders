package com.kredatus.skydefenders.Handlers;

import com.kredatus.skydefenders.GameObjects.TinyBird;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TinyBirdHandler {
    public  ConcurrentLinkedQueue<TinyBird> tinyBirdQueue=new ConcurrentLinkedQueue<TinyBird>();
    public static final int birdAmount = 200;
    private static Random r = new Random();
    private static ArrayList<Float> flapSpeedIntervals=new ArrayList<Float>();

    public TinyBirdHandler(){
        flapSpeedIntervals();
    }

    public  void addTinyBirdsNextCity(float camWidth, float camHeight){
        for (int i=0;i<birdAmount;i++){   //100-250 tinyBirds
            tinyBirdQueue.add(new TinyBird(flapSpeedIntervals, camWidth, camHeight));
        }
    }

    protected void flapSpeedIntervals(){
        float decInterval =0.010f;
        for (float i=0.110f; i>=0.020;i-=decInterval){
             //slow to fast (number is ms/frame)
             flapSpeedIntervals.add(i);
        }
        //System.out.println(flapSpeedIntervals.toString());
    }

    public  void addVertValueToBirdsSurvivingWavePart(float vertValue){
        for (TinyBird i : tinyBirdQueue){
            i.addedY=vertValue;
        }
    }

    public void update(float delta){
        for (TinyBird i : tinyBirdQueue){
            i.update(delta);
            if (i.isGone()) tinyBirdQueue.remove(i);
        }
    }
}
