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
        if (BirdHandler.activeBirdQueue.peek() != null) {
            System.out.println(BirdHandler.activeBirdQueue.peek().x);
        }
        for (Turret i : turretList){
            i.update(delta);
        }

        for (BirdAbstractClass i : BirdHandler.activeBirdQueue){
            i.update(delta, runTime);
            if (i.isOffCam){
                BirdHandler.activeBirdQueue.remove(i);
            }



            for (Projectile j : TurretHandler.projectileList){
                if (i.collides(j)){
                    i.hit(j);
                    j.pen--;
                    if (j.pen==0 ){
                        TurretHandler.projectileList.remove(j);
                    }
                }
            }
        }

        for (Projectile j : TurretHandler.projectileList){
            j.update(delta);
            if (j.isGone) {
                TurretHandler.projectileList.remove(j);
            }
        }

    }
}
