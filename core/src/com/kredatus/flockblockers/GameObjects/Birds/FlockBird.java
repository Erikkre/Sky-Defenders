package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class FlockBird extends BirdAbstractClass {
    public FlockBird(OrthographicCamera cam){
        super(cam);
        this.health=3;
        this.width = width-sizeVariance+r.nextInt(sizeVariance*2);
        this.height = height-sizeVariance+r.nextInt(sizeVariance*2);
        super.load("sprites/flock.png");
    }

    @Override
    public void fly(float delta) {

    }
    @Override
    public void update(float delta) {

    }
}
