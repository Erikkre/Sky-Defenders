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
        turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*3),camWidth, camHeight));
        turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*15),camWidth, camHeight));
        turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*30),camWidth, camHeight));
        turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*45),camWidth, camHeight));
        turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*60),camWidth, camHeight));

        turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*3),camWidth, camHeight));
        turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*15),camWidth, camHeight));
        turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*30),camWidth, camHeight));
        turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*45),camWidth, camHeight));
        turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*60),camWidth, camHeight));
    }

    public void update(float delta, float runTime) {
        for (Turret i : turretList){
            i.update(delta);
        }

        for (Projectile j : TurretHandler.projectileList){
            j.update(delta);
            if (j.isGone) {
                TurretHandler.projectileList.remove(j);
            }
        }

        for (BirdAbstractClass i : BirdHandler.activeBirdQueue){    //could have dead bird higher than alive bird, so need separate loader, alive, dead lists
            i.update(delta, runTime);
            if (!i.isAlive){
                BirdHandler.deadBirdQueue.add(i);
                BirdHandler.activeBirdQueue.remove(i);
            } else {
                if (i.y >= previousBirdHeight) {
                    previousBirdHeight = i.y;
                    targetBird = i;
                }

                for (Projectile j : TurretHandler.projectileList) {
                    if (i.collides(j)) {
                        i.hit(j);
                        j.pen--;
                        if (j.pen == 0) {
                            TurretHandler.projectileList.remove(j);
                        }
                    }
                }
            }
        }
        previousBirdHeight=0;   //in case some birds are moved past lead bird before any bird dies, need to check top bird every time

        for (BirdAbstractClass i : BirdHandler.deadBirdQueue){
            i.update(delta, runTime);
            if (i.isOffCam) {
                BirdHandler.deadBirdQueue.remove(i);
            }
        }


    }
}
