package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class LunarBird extends BirdAbstractClass {
    public LunarBird(int width, int height, OrthographicCamera cam, int camwidth, int camheight, int sizeVariance, int health){
        super( width,  height,  cam,  camwidth, camheight, health);
        this.width = width-sizeVariance+r.nextInt(sizeVariance*2);
        this.height = height-sizeVariance+r.nextInt(sizeVariance*2);
        super.load("sprites/lunar.png");
    }

    @Override
    public void fly(float delta) {

    }

}
