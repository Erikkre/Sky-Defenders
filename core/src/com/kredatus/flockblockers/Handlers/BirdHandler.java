package com.kredatus.flockblockers.Handlers;

import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird;
import com.kredatus.flockblockers.Helpers.BirdAccessor;

import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;

public class BirdHandler {

    public static ArrayList<BirdAbstractClass> birdsList = new ArrayList<BirdAbstractClass>();

    public BirdHandler(float camWidth, float camHeight){

        Tween.registerAccessor(BirdAbstractClass.class, new BirdAccessor());

        PhoenixBird bird = new PhoenixBird( camHeight,camWidth);
        birdsList.add(bird);
    }

    public void update(float runTime, float delta) {
        for (int i =0 ; i < birdsList.size(); i++){
            //System.out.println(birdsList.size());
            birdsList.get(i).update(delta);
            if (birdsList.get(i).isOffCam){
                birdsList.remove(i);
                //i=null; experiment performance putting this before, after or even removing it, or removing the .remove
            }

        }

    }

}
