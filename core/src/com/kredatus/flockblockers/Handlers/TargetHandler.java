// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Projectile;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.GameWorld.GameWorld;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Erik Kredatus on 11/8/2018.
 */

public class TargetHandler {
    public static ConcurrentLinkedQueue<Projectile> projectileList=new ConcurrentLinkedQueue<Projectile>();

    public static BirdAbstractClass targetBird;
    public static int minTargetingHeight=0;
    private float previousBirdHeight=minTargetingHeight;
    private Airship airship;
    public TargetHandler(Airship airship){
        this.airship=airship;
    }

    public void update(float delta, float runTime) {

        //System.out.println(BirdHandler.activeBirdQueue);
        for (BirdAbstractClass i : BirdHandler.activeBirdQueue) {
            i.update(delta, runTime);

            if (i.collides(airship.boundingPoly)) {
                System.out.println(airship.boundingPoly + " "+i.collides(airship.boundingPoly));
                airship.hit(i.origHealth);
                i.hit(9000);    //lol
                i.die();                   //just in case bird health gets crazy high
            } else if (!i.isAlive){
                BirdHandler.deadBirdQueue.add(i);
                BirdHandler.activeBirdQueue.remove(i);
            } else {
                if (i.y >= previousBirdHeight) {
                    previousBirdHeight = i.y;
                    targetBird = i;
                }
            }
        }

        previousBirdHeight=minTargetingHeight; //in case some birds are moved past lead bird before any bird dies, need to check top bird every time

        for (Projectile i : projectileList){    //could have dead bird higher than alive bird, so need separate loader, alive, dead lists
            i.update(delta);
            if (i.isGone){
                projectileList.remove(i);
            }
            for (BirdAbstractClass j : BirdHandler.activeBirdQueue) {
                if (i.pen>0 && !j.hitBulletList.contains(i) && j.y>minTargetingHeight && j.collides(i.boundingRect) && j.health>0) {  //if bird i is colliding with bullet j and was not already hit before
                    j.hit(i.dmg);
                    //System.out.println("Bullet --, pen was "+i.pen);
                    i.pen--;

                    if (i.pen<1){
                        //System.out.println("Bullet exhausted");
                        projectileList.remove(i);
                    } else {

                        j.hitBulletList.add(i);
                    }

                }
            }
        }
        for (BirdAbstractClass i : BirdHandler.deadBirdQueue){
            i.update(delta, runTime);
            if (i.isOffCam()&&i.coinList==null) {
                BirdHandler.deadBirdQueue.remove(i);
            }
        }
    }
}