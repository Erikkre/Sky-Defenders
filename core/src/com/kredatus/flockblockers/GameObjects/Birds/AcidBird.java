package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

public class AcidBird extends BirdAbstractClass{
    public AcidBird(OrthographicCamera cam){
        super(cam);
        this.health=7;
        this.width = width-sizeVariance+r.nextInt(sizeVariance*2);
        this.height = height-sizeVariance+r.nextInt(sizeVariance*2);
        super.load("sprites/acid.png");
    }

    @Override
    public void fly(float delta) {

    }
    @Override
    public void update(float delta) {

    }
}
