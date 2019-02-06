package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.kredatus.flockblockers.GameWorld.GameRenderer;

import java.util.concurrent.ConcurrentLinkedQueue;

import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;

public class LightHandler {
    private OrthographicCamera cam;
    private BgHandler bgHandler;
    public ConcurrentLinkedQueue<PositionalLight> bgLights=new ConcurrentLinkedQueue<PositionalLight>();

    private RayHandler rayHandler;

    public LightHandler(BgHandler bgHandler){

        World world = new World(new Vector2(0,0),false);//fake world to input to the rayhandler
        rayHandler = new RayHandler(world);

        //rayHandler.setAmbientLight(1f);   //light everywhere outside of our set lights
    }

    public void newBgLighting(TextureRegion texture){    //there will always be 2 light sources on the screen between the bg's and glowing cloud separators
        if (!bgLights.isEmpty()) bgLights.poll(); //get rid of last passed light unless is empty because just starting
        if (texture==AssetHandler.bgCloudSeparatorTexture)                                          bgLights.add(new PointLight(rayHandler,3, new Color(233,33,33,1), 1000,0,0));
        else if (texture==AssetHandler.bgPhoenixtexture||texture==AssetHandler.bgPhoenixtexture2)   bgLights.add(new PointLight(rayHandler,3, new Color(233,33,33,1), 1000,0,0));
        else if (texture==AssetHandler.bgThundertexture||texture==AssetHandler.bgThundertexture2)   bgLights.add(new PointLight(rayHandler,3, new Color(233,33,33,1), 1000,0,0));
        else if (texture==AssetHandler.bgWatertexture||texture==AssetHandler.bgWatertexture2)       bgLights.add(new PointLight(rayHandler,3, new Color(233,33,33,1), 1000,0,0));
        else if (texture==AssetHandler.bgFiretexture||texture==AssetHandler.bgFiretexture2)         bgLights.add(new PointLight(rayHandler,3, new Color(233,33,33,1), 1000,0,0));
        else if (texture==AssetHandler.bgAcidtexture||texture==AssetHandler.bgAcidtexture2)         bgLights.add(new PointLight(rayHandler,3, new Color(233,33,33,1), 1000,0,0));
        else if (texture==AssetHandler.bgNighttexture||texture==AssetHandler.bgNighttexture2)       bgLights.add(new PointLight(rayHandler,3, new Color(233,33,33,1), 1000,0,0));
        else if (texture==AssetHandler.bgLunartexture||texture==AssetHandler.bgLunartexture2)       bgLights.add(new PointLight(rayHandler,3, new Color(233,33,33,1), 1000,0,0));
        else if (texture==AssetHandler.bgGoldtexture||texture==AssetHandler.bgGoldtexture2)         bgLights.add(new PointLight(rayHandler,3, new Color(233,33,33,1), 1000,0,0));
    }

public void setCam(GameRenderer renderer){
    cam=renderer.cam;
    rayHandler.setCombinedMatrix(cam);
}

public void update(){
        rayHandler.updateAndRender();   //the render part might need to be put at the end of the render cycle in gameRenderer
}
}
