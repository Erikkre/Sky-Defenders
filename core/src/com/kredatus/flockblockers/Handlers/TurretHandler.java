package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Projectile;
import com.kredatus.flockblockers.GameObjects.Turret;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Erik Kredatus on 11/8/2018.
 */

public class TurretHandler {

    public static ConcurrentLinkedQueue<Projectile> projectileList=new ConcurrentLinkedQueue<Projectile>();
    public static ArrayList<Turret> turretList=new ArrayList<Turret>(15);//maximal amount of turrets
    public static Iterator<BirdAbstractClass> iterator;
    public static BirdAbstractClass targetBird;

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
        iterator=BirdHandler.activeBirdQueue.iterator();
        for (Turret i : turretList){
            i.update(delta);
        }

        for (BirdAbstractClass i : BirdHandler.activeBirdQueue){
            i.update(delta, runTime);
            if (!i.isAlive){
                if (BirdHandler.activeBirdQueue.iterator().hasNext()){
                    System.out.println("***************************Next Target****************************");
                    targetBird=iterator.next();//next most recent bird is targeted. if that one is dead, go next etc etc
                } else {
                    targetBird=null;
                }
            } else if (targetBird==null && BirdHandler.activeBirdQueue.iterator().hasNext()){
                targetBird=iterator.next();
            }

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
