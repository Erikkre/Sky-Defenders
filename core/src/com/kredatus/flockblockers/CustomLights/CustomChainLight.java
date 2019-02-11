// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.CustomLights;

import com.badlogic.gdx.graphics.Color;


import box2dLight.ChainLight;
import box2dLight.RayHandler;

public class CustomChainLight extends ChainLight {
    public float[] origVerts;
    public CustomChainLight(RayHandler rayHandler, int r, int g, int b, float a, int lightDistance, int dirDeg, int rayNumber, float[] origVerts){
        super(rayHandler, rayNumber, new Color(r/255f,g/255f,b/255f,a), lightDistance,  dirDeg, origVerts);
        this.origVerts=origVerts;
    }
}
