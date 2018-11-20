package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.math.Vector2;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

public class Coin {
    public Vector2 position, velocity=new Vector2();
    private Vector2 dest=new Vector2(500,500);
    private Tween xMotion0, yMotion0, xMotion1, yMotion1;
    public float y1, x1;
    public Coin(int camWidth, int camheight, Vector2 position, float rotation){
        this.position=position;
        x1=-(float)(Math.cos(Math.toRadians(rotation)));
        y1=-(float)(Math.sin(Math.toRadians(rotation)));
        xMotion0 = Tween.to(position.x, 1, 1).target(x1).ease(TweenEquations.easeOutSine).repeatYoyo(Tween.INFINITY,0);
        yMotion0 = Tween.to(position.y, 1, 1).target(y1).ease(TweenEquations.easeOutSine).repeatYoyo(Tween.INFINITY,0);
        xMotion1 = Tween.to(position.x, 1, 2).target(dest.x).ease(TweenEquations.easeInQuint).repeatYoyo(Tween.INFINITY,0).delay(2);
        yMotion1 = Tween.to(position.y, 1, 2).target(dest.y).ease(TweenEquations.easeInQuint).repeatYoyo(Tween.INFINITY,0);
    }
}
