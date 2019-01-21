package com.kredatus.flockblockers.Handlers;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.kredatus.flockblockers.GameObjects.TinyBird;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TinyBirdHandler {
    public static ConcurrentLinkedQueue<TinyBird> tinyBirdQueue=new ConcurrentLinkedQueue<TinyBird>();
    private static Animation[] textureList = new Animation[]{AssetHandler.tinyAnim1,AssetHandler.tinyAnim2,AssetHandler.tinyAnim3,
            AssetHandler.tinyAnim4,AssetHandler.tinyAnim5,AssetHandler.tinyAnim6,AssetHandler.tinyAnim7,AssetHandler.tinyAnim8};
    private Random r = new Random();

    public TinyBirdHandler(){
        for (int i=0;i<10;i++){
            tinyBirdQueue.add(new TinyBird(textureList[r.nextInt(textureList.length)]));
        }
    }
}
