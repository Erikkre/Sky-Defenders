// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameObjects.Turret;

import java.util.ArrayList;

/**
 * Created by Erik Kredatus on 11/24/2018.
 */

public class TurretHandler {
    public ArrayList<Turret> turretList=new ArrayList<Turret>(13);

    public TurretHandler(float camWidth, float camHeight){
        float unit=camWidth/7;
        turretList.add(new Turret('f',new Vector2(unit*1,camHeight-(61/2)*2)));
        //turretList.add(new Turret('f',new Vector2(unit*2,camHeight-(61/2)*2)));
        //turretList.add(new Turret('f',new Vector2(unit*3,camHeight-(61/2)*2)));
        //turretList.add(new Turret('s',new Vector2(unit*4,camHeight-(61/2)*2)));
        //turretList.add(new Turret('f',new Vector2(unit*5,camHeight-(61/2)*2)));
        //turretList.add(new Turret('f',new Vector2(unit*6,camHeight-(61/2)*2)));

        /*turretList.add(new Turret('f',new Vector2(unit*1,camHeight-(61/2)*5)));
        turretList.add(new Turret('f',new Vector2(unit*2,camHeight-(61/2)*5)));
        turretList.add(new Turret('f',new Vector2(unit*3,camHeight-(61/2)*5)));
        turretList.add(new Turret('f',new Vector2(unit*4,camHeight-(61/2)*5)));
        turretList.add(new Turret('f',new Vector2(unit*5,camHeight-(61/2)*5)));
        turretList.add(new Turret('f',new Vector2(unit*6,camHeight-(61/2)*5)));*/

        /*
    int j=0;
        for (Turret i : turretList){
                for (int k=0;k<j;k++){
                    i.lvlUp();
                }
                j++;
            System.out.println(i.dmg);
            }
*/


    }


    public void update(){
        for (Turret i : turretList) {
            i.update();
        }
    }
}
