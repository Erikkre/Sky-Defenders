package com.kredatus.flockblockers.GlideOrDieHelpers;

import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

import java.util.ArrayList;

public class BirdHandler {

    private ArrayList<BirdAbstractClass> birdsList;

    public BirdHandler(float camwidth, float camheight){

    }

    public void update(float runTime, float delta) {
        for (BirdAbstractClass i : birdsList){
            if (i.isOffCam){
                birdsList.remove(i);
                //i=null; experiment performance putting this before, after or even removing it, or removing the .remove
            }
        }

    }

}
