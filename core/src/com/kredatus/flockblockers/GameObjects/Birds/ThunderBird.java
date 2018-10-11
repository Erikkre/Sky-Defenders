package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class ThunderBird extends BirdAbstractClass {
    public ThunderBird(int width, int height, OrthographicCamera cam, int camwidth, int camheight, int sizeVariance, int health){
        super(cam);
        this.coins=15;
        this.health=7;
        this.width = width-sizeVariance+r.nextInt(sizeVariance*2);
        this.height = height-sizeVariance+r.nextInt(sizeVariance*2);
        super.load("sprites/thunder.png");
    }

    @Override
    public void fly(float delta) {

    }
    @Override
    public void update(float delta) {

    }
}
