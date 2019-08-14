package com.kredatus.skydefenders.CustomLights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class CustomConeLight extends ConeLight {

    public Vector2 origPos;
    public CustomConeLight(RayHandler rayHandler,  int r, int g, int b, float a, int lightDistance, Vector2 origPos, float dirDeg, float coneDeg){
        super(rayHandler, 30, new Color(r/255f,g/255f,b/255f,a), lightDistance, origPos.x,origPos.y, dirDeg, coneDeg);
        this.origPos=origPos;
    }
}
