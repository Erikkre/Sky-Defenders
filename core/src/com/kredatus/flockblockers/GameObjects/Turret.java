package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.TurretHandler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Erik Kredatus on 9/9/2018.
 */

public class Turret {

    private boolean isScored = false, ai, firing;
    protected int width, height ;
    private boolean isGone;
    private Vector2 position;
    private float camWidth, camHeight, rotation;
    public float dmg, pen, spr, rof;
    public Projectile projectile;
    Timer timer;
    TimerTask timerTask;

public Turret(char turretType, int lvl, Vector2 position, int width, int height, float camWidth, float camHeight){
    this.width    = width    ;
    this.height   = height   ;
    this.position = position ;

    this.camWidth = camWidth ;
    this.camHeight= camHeight;

    timer=new Timer();
    firing=false;
    ai=true;

    turretSetup(turretType, lvl);
}

public void turretSetup(char turretType, int lvl){
    switch (turretType) {
        case ('f'): //fast firing
            dmg = 2;
            pen = 1;
            spr = 1;
            rof = 1;
        case ('s'):
            dmg = 1;
            pen = 1;
            spr = 3;
            rof = 0.5f;
        case ('d'):
            dmg = 4;
            pen = 2;
            spr = 1;
            rof = 0.1f;
    }

    for (int i=1;i<=lvl;i++){
        dmg*=2;
        rof*=1.5f;
        pen*=1.4f;
        if (turretType=='s'){
            spr+=1;
        }
    }
}

    public void configureFirerate() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                TurretHandler.projectileList.add(new Projectile(dmg,rof,pen, position, width, height, camWidth, camHeight));
            }
        };//set task to run later using timer.schedule
}

}
