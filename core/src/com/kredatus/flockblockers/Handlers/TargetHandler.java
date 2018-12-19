package com.kredatus.flockblockers.Handlers;

import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Projectile;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Erik Kredatus on 11/8/2018.
 */

public class TargetHandler {
    public static ConcurrentLinkedQueue<Projectile> projectileList=new ConcurrentLinkedQueue<Projectile>();

    public static BirdAbstractClass targetBird;
    private int minTargetingHeight=0;
    private float previousBirdHeight=-minTargetingHeight;

  //  public TargetHandler(float camWidth, float camHeight){  }

    public void update(float delta, float runTime) {

        //System.out.println(BirdHandler.activeBirdQueue);
        for (BirdAbstractClass i : BirdHandler.activeBirdQueue) {
            i.update(delta, runTime);
            if (!i.isAlive) {
                BirdHandler.deadBirdQueue.add(i);
                BirdHandler.activeBirdQueue.remove(i);
            } else {
                if (i.y >= previousBirdHeight) {
                    previousBirdHeight = i.y;
                    targetBird = i;
                }
            }
        }
        previousBirdHeight=-minTargetingHeight; //in case some birds are moved past lead bird before any bird dies, need to check top bird every time



        for (Projectile i : projectileList){    //could have dead bird higher than alive bird, so need separate loader, alive, dead lists
            i.update(delta);
            if (i.isGone){
                TargetHandler.projectileList.remove(i);
            }
            for (BirdAbstractClass j : BirdHandler.activeBirdQueue) {
                if (j.collides(i) && !j.hitBulletList.contains(i)) {  //if bird i is colliding with bullet j and was not already hit before
                    j.hit(i);
                    i.pen--;
                    if (i.pen==0){
                        TargetHandler.projectileList.remove(i);
                    } else {
                        j.hitBulletList.add(i);
                    }

                }
            }
        }
        for (BirdAbstractClass i : BirdHandler.deadBirdQueue){
            i.update(delta, runTime);
            if (i.isOffCam&&i.coinList==null) {
                BirdHandler.deadBirdQueue.remove(i);
            }
        }
    }
}