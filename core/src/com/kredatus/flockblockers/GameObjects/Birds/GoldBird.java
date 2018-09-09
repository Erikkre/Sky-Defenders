package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */
public class GoldBird extends BirdAbstractClass {

    public GoldBird(int width, int height, OrthographicCamera cam, int camwidth, int camheight, int sizeVariance, int health){
        super( width,  height,  cam,  camwidth, camheight);
        this.width = width-sizeVariance+r.nextInt(sizeVariance*2);
        this.height = height-sizeVariance+r.nextInt(sizeVariance*2);
    }


    @Override
    public void update(float delta) {

    }
}
