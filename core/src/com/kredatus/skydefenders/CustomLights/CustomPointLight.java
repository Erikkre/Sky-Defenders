package com.kredatus.skydefenders.CustomLights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import box2dLight.PointLight;
import box2dLight.RayHandler;


public class CustomPointLight extends PointLight{
    public Vector2 origPos,distanceFromAirship;
    public CustomPointLight(RayHandler rayHandler,  int r, int g, int b, float a, int lightDistance, Vector2 origPos){
        super(rayHandler, 25, new Color(r/255f,g/255f,b/255f,a), lightDistance, origPos.x,origPos.y);
        this.origPos=origPos;
    }
    public CustomPointLight(Vector2 distanceFromAirship, RayHandler rayHandler,  int r, int g, int b, float a, int lightDistance,Vector2 airshipPos){
        super(rayHandler, 25, new Color(r/255f,g/255f,b/255f,a), lightDistance, airshipPos.x+distanceFromAirship.x,airshipPos.y+distanceFromAirship.y);
        this.origPos=super.getPosition();
        this.distanceFromAirship=distanceFromAirship;
    }
    public CustomPointLight(RayHandler rayHandler, Color color, int lightDistance, Vector2 origPos){
        super(rayHandler, 25, color, lightDistance, origPos.x,origPos.y);
        this.origPos=origPos;
    }
}
