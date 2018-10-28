package com.kredatus.flockblockers.Handlers;

import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird;


import java.util.ArrayList;


public class BirdHandler {

    public static ArrayList<BirdAbstractClass> birdsList = new ArrayList<BirdAbstractClass>();
    public static String[] birdOrderList={"pB","wB","nB","aB","fB","tB","lB","gB"};
    public static int[] birdNumberList=  { 1,   40,  30,  20,  20,  20,  10,  5  };
    public BirdHandler(BgHandler bgHandler, float camWidth, float camHeight){

        if (bgHandler.getBackground().texture==AssetHandler.bgPhoenixtexture2||bgHandler.getBackground2().texture==AssetHandler.bgPhoenixtexture2){

        }
        PhoenixBird bird = new PhoenixBird( camHeight,camWidth);
        birdsList.add(bird);
    }

    public void update(float runTime, float delta) {
        for (int i =0 ; i < birdsList.size(); i++){
            //System.out.println(birdsList.size());
            birdsList.get(i).update(delta, runTime);
            if (birdsList.get(i).isOffCam){
                birdsList.remove(i);
                //i=null; experiment performance putting this before, after or even removing it, or removing the .remove
            }

        }

    }

}
