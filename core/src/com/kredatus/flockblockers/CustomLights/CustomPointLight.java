package com.kredatus.flockblockers.CustomLights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.LightHandler;

import box2dLight.PointLight;


public class CustomPointLight extends PointLight{
    public Vector2 origPos;
    public CustomPointLight(int r, int g, int b, float a, int lightDistance, Vector2 origPos){
        super(LightHandler.rayHandler, 25, new Color(r/255f,g/255f,b/255f,a), lightDistance, origPos.x,origPos.y);
        this.origPos=origPos;
    }
}
