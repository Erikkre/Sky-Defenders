package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class FireBird extends BirdAbstractClass {
    FireBird(OrthographicCamera cam){
        super(cam);
        this.health=7;
        this.sizeVariance=15;
        int variance = r.nextInt(sizeVariance*2);
        this.width = width-sizeVariance+variance;
        this.height = height-sizeVariance+variance;
        super.load("sprites/fire.png");
    }

    @Override
    public void fly(float delta) {

    }
}
