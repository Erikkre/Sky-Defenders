package com.kredatus.flockblockers.CustomLights;

import com.badlogic.gdx.graphics.Color;
import com.kredatus.flockblockers.Handlers.LightHandler;

import box2dLight.ChainLight;

public class CustomChainLight extends ChainLight {
    public float[] origVerts;
    public CustomChainLight(int r, int g, int b, float a, int lightDistance, int dirDeg, float[] origVerts){
        super(LightHandler.rayHandler, 30, new Color(r/255f,g/255f,b/255f,a), lightDistance,  dirDeg, origVerts);
        this.origVerts=origVerts;
    }
}
