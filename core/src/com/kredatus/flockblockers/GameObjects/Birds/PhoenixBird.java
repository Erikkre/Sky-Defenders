package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class PhoenixBird extends BirdAbstractClass {
    public PhoenixBird( int sizeVariance, int health, OrthographicCamera cam){
        super(cam);
        this.diamonds=1;
        this.coins=7;
        this.health=100;
        this.width = width-sizeVariance+r.nextInt(sizeVariance*2);
        this.height = height-sizeVariance+r.nextInt(sizeVariance*2);
        super.load("sprites/phoenix.png");
    }

    public void intro(float delta, int camHeight){
        position=(new Vector2(0,-(camHeight/2) -height*2));
        velocity.y=1000;
        acceleration.y=-50;
        while (position.y!=camHeight/2){

        }
    }

    @Override
    public void fly(float delta) {

    }
    @Override
    public void update(float delta) {

    }

}
