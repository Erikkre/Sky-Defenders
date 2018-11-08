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
    private Vector2 position, velocity;
    private float camWidth, camHeight, rotation;
    public float damage, penetration, spread, firerate;
    public Trajectile trajectile;
    Timer timer;
    TimerTask timerTask;
public Turret(char turretType, int level, Vector2 position, Vector2 velocity, int width, int height, float camWidth, float camHeight){
    this.width    = width    ;
    this.height   = height   ;
    this.position = position ;
    this.velocity = velocity ;
    this.camWidth = camWidth ;
    this.camHeight= camHeight;

    timer=new Timer();
    firing=false;
    ai=true;
    switch (turretType){
        case ('f'): //fast firing
            switch (level){
                case (1):
                    damage=5;
                    penetration=5;
                    spread=1;
                    firerate=5;
                case (2):
                case (3):
                case (4):
                case (5):
                case (6):
                case (7):
                case (8):
                case (9):
                case (10):
            }
        case ('w'): //wide spread
            switch (level){
                case (1):
                case (2):
                case (3):
                case (4):
                case (5):
                case (6):
                case (7):
                case (8):
                case (9):
                case (10):
            }
        case ('d'): //damage penetration
            switch (level){
                case (1):
                case (2):
                case (3):
                case (4):
                case (5):
                case (6):
                case (7):
                case (8):
                case (9):
                case (10):
            }
    }
}

    public void configureFirerate() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                TurretHandler.bulletlist.add(Trajectile(damage, penetration, angle, velocity etc));
            }
        };//set task to run later using timer.schedule
}

}
