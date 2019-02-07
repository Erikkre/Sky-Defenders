package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.kredatus.flockblockers.CustomLights.CustomChainLight;
import com.kredatus.flockblockers.CustomLights.CustomConeLight;
import com.kredatus.flockblockers.CustomLights.CustomPointLight;
import com.kredatus.flockblockers.GameWorld.GameRenderer;


import java.util.concurrent.ConcurrentLinkedQueue;

import box2dLight.ChainLight;
import box2dLight.Light;
import box2dLight.RayHandler;

public class LightHandler {
    private OrthographicCamera cam;
    public static ConcurrentLinkedQueue<Light> bgLights = new ConcurrentLinkedQueue<Light>();   //max number of lights probably
    public static World world = new World(new Vector2(0, 0), false);//fake world to input to the rayhandler
    public static RayHandler rayHandler = new RayHandler(world);

    private static final float cloudYPos=0.154454f * BgHandler.bgStackHeight, cloudAboveStackYPos=cloudYPos+BgHandler.bgStackHeight, bgw = BgHandler.bgw;
    private static final Vector2
            sun1LightPos = new Vector2(0.59082f * bgw, 0.517933f * BgHandler.bgStackHeight),
            sun2LightPos = new Vector2(0.59082f * bgw, .791747f * BgHandler.bgStackHeight);
    private static final float[] firstCloudLightVerts  = {0.25f*bgw,cloudYPos, 0.50f*bgw,cloudYPos, 0.75f*bgw,cloudYPos}, cloudLightVerts = {0.25f*bgw,cloudAboveStackYPos, 0.50f*bgw,cloudAboveStackYPos, 0.75f*bgw,cloudAboveStackYPos};

    public LightHandler() {
        bgLights.add(newChainLight(255, 255, 255, 0.85f, 2500, 1, firstCloudLightVerts));
        System.out.println("Start out in a cloud light always, then do all rest of lights +1 more cloud light whenever you add");

        rayHandler.setAmbientLight(1f);   //light everywhere outside of our set lights

        //bgLights.add(newPointLight());
        //bgLights.add(new DirectionalLight(rayHandler,30, new Color(233,33,33,0.5f), 90));
    }

    private static CustomChainLight newChainLight(int r, int g, int b, float a, int lightDistance, int dirDeg, float[] chainVertices) {
        CustomChainLight newChainLight = new CustomChainLight(r, g, b, a, lightDistance, dirDeg, chainVertices );
        newChainLight.setSoft(false);   //no need for softness as not hitting obstacles
        newChainLight.setStaticLight(false); //static lights dont interact with obstacles, redudes cpu load by 90%
        newChainLight.setXray(false);    //beams go through obstacles, reduces CPU burden of light about 70%
        return newChainLight;
    }

    //(multiply width by bgh and height by *bgStackHeight respectively) positions of bg light sources are 0.5 x .154454 for clouds, .59082 x 0.517933 for first sun, .59082 x .791747 for second sun
    private static CustomPointLight newPointLight(int r, int g, int b, float a, int lightDistance, Vector2 origPos) {
        CustomPointLight newPointLight = new CustomPointLight(r, g, b, a, lightDistance, origPos);
        newPointLight.setSoft(false);   //no need for softness as not hitting obstacles
        newPointLight.setStaticLight(false); //static lights dont interact with obstacles, redudes cpu load by 90%
        newPointLight.setXray(false);    //beams go through obstacles, reduces CPU burden of light about 70%
        return newPointLight;
    }

    public static void newBgLighting(int bgNumber) {
        for (Light i : bgLights) { //if bg is reset we have to add - height of bg back to lights that are still in play when vert resets
            if (i instanceof CustomPointLight)      ((CustomPointLight) i).origPos.set(((CustomPointLight) i).origPos.x, ((CustomPointLight) i).origPos.y  -BgHandler.bgStackHeight);

            else if (i instanceof CustomChainLight) ((CustomChainLight) i).origVerts = new float[]{((CustomChainLight) i).origVerts[0],((CustomChainLight) i).origVerts[1]-BgHandler.bgStackHeight,
                    ((CustomChainLight) i).origVerts[2],((CustomChainLight) i).origVerts[3]-BgHandler.bgStackHeight,((CustomChainLight) i).origVerts[4],((CustomChainLight) i).origVerts[5]-BgHandler.bgStackHeight};

            else                                    ((CustomConeLight) i).origPos.set(((CustomConeLight) i).origPos.x, ((CustomConeLight) i).origPos.y - BgHandler.bgStackHeight);
        }
        bgLights.add(newChainLight(255, 255, 255, 0.85f, 2500, 1, cloudLightVerts));   //add cloudLights no matter what
        if (bgNumber < 9) {
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun1LightPos));
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun2LightPos));
        } else if (bgNumber < 18) {
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun1LightPos));
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun2LightPos));
        } else if (bgNumber < 27) {
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun1LightPos));
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun2LightPos));
        } else if (bgNumber < 36) {
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun1LightPos));
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun2LightPos));
        } else if (bgNumber < 45) {
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun1LightPos));
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun2LightPos));
        } else if (bgNumber < 54) {
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun1LightPos));
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun2LightPos));
        } else if (bgNumber < 63) {
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun1LightPos));
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun2LightPos));
        } else if (bgNumber < 72) {
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun1LightPos));
            bgLights.add(newPointLight(255, 237, 137, 0.65f, 2500, sun2LightPos));
        }
    }

    public void setCam(GameRenderer renderer) {
        cam = renderer.cam;
        rayHandler.setCombinedMatrix(cam);
    }

    public boolean isOffCam(Light l) {
        return l.getY() + l.getDistance()/2 < 0; //if light yPos+radius is lower than 0, delete it
    }

    public void update() {
        float bgVert = BgHandler.vert.getValue();
        float bgHoriz = BgHandler.horiz.getValue();
        for (Light i : bgLights) {
            if (isOffCam(i)){ bgLights.remove(i);}
            else if (i instanceof CustomPointLight)  i.setPosition(((CustomPointLight) i).origPos.x + bgHoriz, ((CustomPointLight) i).origPos.y + bgVert);
            else if (i instanceof ChainLight)        {i.setPosition(((CustomChainLight) i).origVerts[0] + bgHoriz, ((CustomChainLight) i).origVerts[1] + bgVert);System.out.println(i.getPosition());}
            else                                     i.setPosition(((CustomConeLight) i).origPos.x + bgHoriz, ((CustomConeLight) i).origPos.y + bgVert);
        }
        rayHandler.update();   //the render part might need to be put at the end of the render cycle in gameRenderer
    }

    public static void render() {
        rayHandler.render();
    }
}
