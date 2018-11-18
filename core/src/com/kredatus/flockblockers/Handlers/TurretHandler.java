package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Projectile;
import com.kredatus.flockblockers.GameObjects.Turret;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Erik Kredatus on 11/8/2018.
 */

public class TurretHandler {

    public static ConcurrentLinkedQueue<Projectile> projectileList=new ConcurrentLinkedQueue<Projectile>();
    public static ArrayList<Turret> turretList=new ArrayList<Turret>(1);
    public static BirdAbstractClass targetBird;
    private float previousBirdHeight;
    public TurretHandler(float camWidth, float camHeight){
        turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*15),camWidth, camHeight));
        //turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*30),camWidth, camHeight));
        //turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*45),camWidth, camHeight));

        //turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*15),camWidth, camHeight));
        //turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*30),camWidth, camHeight));
        //turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*45),camWidth, camHeight));
    }

    public void update(float delta, float runTime) {
        for (Turret i : turretList){
            i.update(delta);
        }

//System.out.println(BirdHandler.activeBirdQueue);
        for (BirdAbstractClass i : BirdHandler.activeBirdQueue) {
            i.update(delta, runTime);
            if (i.isOffCam) {
                BirdHandler.activeBirdQueue.remove(i);
            } else if (!i.isAlive) {
                BirdHandler.deadBirdQueue.add(i);
                BirdHandler.activeBirdQueue.remove(i);
            } else {
                if (i.y >= previousBirdHeight) {
                    previousBirdHeight = i.y;
                    targetBird = i;
                }
            }
        }
        previousBirdHeight=0; //in case some birds are moved past lead bird before any bird dies, need to check top bird every time



        for (Projectile i : TurretHandler.projectileList){    //could have dead bird higher than alive bird, so need separate loader, alive, dead lists
            i.update(delta);
            if (i.isGone || i.pen==0){
                TurretHandler.projectileList.remove(i);
            }

            for (BirdAbstractClass j : BirdHandler.activeBirdQueue) {
                if (j.collides(i) && !j.hitBulletList.contains(i)) {  //if bird i is colliding with bullet j and was not already hit before
                    j.hit(i);
                    i.pen--;
                    j.hitBulletList.add(i);
                }
            }
        }



        for (BirdAbstractClass i : BirdHandler.deadBirdQueue){
            i.update(delta, runTime);
            if (i.isOffCam) {
                BirdHandler.deadBirdQueue.remove(i);
            }
        }


    }
}
