package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.math.Vector2;
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
        turretList.add(new Turret('f',0,new Vector2(500,500),camWidth, camHeight));
    }

    public void update(float delta) {
        for (Turret i : turretList){
            i.update(delta);
        }
    }
}
