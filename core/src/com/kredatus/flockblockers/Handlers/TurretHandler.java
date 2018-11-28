package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameObjects.Turret;

import java.util.ArrayList;

/**
 * Created by Erik Kredatus on 11/24/2018.
 */

public class TurretHandler {
    public static ArrayList<Turret> turretList=new ArrayList<Turret>(10);

    public TurretHandler(float camWidth, float camHeight){
        turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*15),camWidth, camHeight));
        //turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*30),camWidth, camHeight));
        //turretList.add(new Turret('f',0,new Vector2(camWidth-(148/2),camHeight-(61/2)*45),camWidth, camHeight));

        //turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*15),camWidth, camHeight));
        //turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*30),camWidth, camHeight));
        //turretList.add(new Turret('f',0,new Vector2((148/2),camHeight-(61/2)*45),camWidth, camHeight));
    }

    public void update(){
        for (Turret i : turretList) {
            i.update();
        }
    }
}
