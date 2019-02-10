package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.kredatus.flockblockers.CustomLights.CustomChainLight;
import com.kredatus.flockblockers.CustomLights.CustomConeLight;
import com.kredatus.flockblockers.CustomLights.CustomPointLight;
import com.kredatus.flockblockers.GameWorld.GameRenderer;

import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

import box2dLight.ChainLight;
import box2dLight.Light;
import box2dLight.RayHandler;

public class LightHandler { //consider making barlight and mirroring on each side of city
    private OrthographicCamera cam;
    public static ConcurrentLinkedQueue<Light> bgLights = new ConcurrentLinkedQueue<Light>();   //max number of lights probably
    public static World world = new World(new Vector2(0, 0), false);   //fake world to input to the rayhandler
    public static RayHandler rayHandler = new RayHandler(world);

    private static float bgw = BgHandler.bgw, bgStackHeight= BgHandler.bgStackHeight, cloudBottomY= 0.154454f * bgStackHeight, cloudTopY=cloudBottomY+bgStackHeight;
    private static final Vector2        //light positions in Vector2
            cloudBottomLeftPos  = new Vector2(bgw/4, cloudBottomY),
            cloudBottomRightPos = new Vector2(3*bgw/4, cloudBottomY),
            cloudTopLeftPos  = new Vector2(  bgw/4, cloudTopY),
            cloudTopRightPos = new Vector2(3*bgw/4, cloudTopY),
//2 3 and 4 wrong pos vert and horiz
            cityP1 = new Vector2(0               * bgw, 0.49922869263f * bgStackHeight),//cityPight1
            cityC2 = new Vector2(0.15380859375f  * bgw, 0.54454300039f * bgStackHeight),//coneLight
            cityP3 = new Vector2(0.16064453125f  * bgw, 0.58927882761f  * bgStackHeight),
            cityP4 = new Vector2(0.193359375f    * bgw, 0.59429232549f  * bgStackHeight),
            cityP5 = new Vector2(0.21142578125f  * bgw, 0.42267643656f * bgStackHeight),
            cityP6 = new Vector2(0.24609375f     * bgw, 0.54512148091f  * bgStackHeight),
            cityP7 = new Vector2(0.30322265625f  * bgw, 0.48438102583f * bgStackHeight),
            cityP8 = new Vector2(0.46533203125f  * bgw, 0.50617045892f * bgStackHeight),
            cityP9 = new Vector2(0.53515625f     * bgw, 0.62032394909f  * bgStackHeight),
            cityP10 =new Vector2(0.54052734375f  * bgw, 0.51851137678f * bgStackHeight),
            sunPos = new Vector2(0.59082f       * bgw, 0.482067f * bgStackHeight),
            cityP11 = new Vector2(0.595703125f   * bgw, 0.59679907443f  * bgStackHeight),
            cityP13 = new Vector2(0.685546875f   * bgw, 0.50096413421f * bgStackHeight),
            cityP12 = new Vector2(0.70458984375f * bgw, 0.59660624759f  * bgStackHeight),
            cityP14 = new Vector2(0.87744140625f * bgw, 0.50057848053f * bgStackHeight),
            cityP15 = new Vector2(0.94091796875f * bgw, 0.44369456229f * bgStackHeight),
            cityP16 = new Vector2(0.955078125f   * bgw, 0.54512148091f * bgStackHeight),
            cityP17 = new Vector2(0.9853515625f  * bgw, 0.59602776707f * bgStackHeight);

    //PARAMETER CONFIG
    private static int cloudDia=3300, sunDia=2300, sDia=50,mDia=100,mlDia=200, lDia=300, xlDia=450, xxlDia=650, xxxlDia=1000;
    private static float cloudA=0.9f, sunA=0.95f,sA=0.5f,mA=0.7f,mlA=0.8f,lA=0.9f,xlA=0.95f, xxlA=1.0f;

    public LightHandler(BgHandler bgHandler) {
        //rayHandler.useDiffuseLight(true); //smoother but makes everywhere but light dark
        rayHandler.setAmbientLight(0.45f);   //light everywhere outside of our set lights
        rayHandler.setGammaCorrection(true);    //play with all the options to see what fits best
        newBgLighting(bgHandler.bgNumber);
        bgLights.add(newPointLight(255,255,255,cloudA,cloudDia,cloudBottomLeftPos)); System.out.println("cloud");
        bgLights.add(newPointLight(255,255,255,cloudA,cloudDia,cloudBottomRightPos));System.out.println("cloud");
        //bgLights.add(new DirectionalLight(rayHandler,30, new Color(233,33,33,0.5f), 90)); //directional light is like ambient light but with direction (for shadows)
    }

    /*private static CustomChainLight[] newBarLight(int r, int g, int b, float a, int lightDistance, int dirDeg, float[] chainVertices) {
        CustomChainLight newChainLightFront = new CustomChainLight(r, g, b, a, lightDistance, dirDeg,(int) (chainVertices.length/2f)*20, chainVertices );
        CustomChainLight newChainLightBack  = new CustomChainLight(r, g, b, a, lightDistance, -dirDeg, (int) (chainVertices.length/2f)*20, chainVertices );
        newChainLightBack.setSoft(false);         //no need for softness as not hitting obstacles
        newChainLightBack.setStaticLight(false);  //static lights dont interact with obstacles, redudes cpu load by 90%
        newChainLightBack.setXray(false);         //beams go through obstacles, reduces CPU burden of light about 70%
        newChainLightFront.setSoft(false);        //no need for softness as not hitting obstacles
        newChainLightFront.setStaticLight(false); //static lights dont interact with obstacles, redudes cpu load by 90%
        newChainLightFront.setXray(false);        //beams go through obstacles, reduces CPU burden of light about 70%
        return new CustomChainLight[]{newChainLightFront, newChainLightBack};
    }*/

    //(multiply width by bgh and height by *bgStackHeight respectively) positions of bg light sources are 0.5 x .154454 for clouds, .59082 x 0.517933 for first sun, .59082 x .791747 for second sun
    private static CustomPointLight newPointLight(int r, int g, int b, float a, int lightDistance, Vector2 origPos) {
        CustomPointLight newPointLight = new CustomPointLight(r, g, b, a, lightDistance, origPos);
        newPointLight.setSoft(false);
        newPointLight.setStaticLight(false);
        newPointLight.setXray(false);
        return newPointLight;
    }

    private static CustomPointLight[] newPointLightMirrored(int r, int g, int b, float a, int lightDistance, Vector2 origPos) {   //input newPointLight in invertedBG and return a list with inverted and non inverted bg pointlights
        CustomPointLight invBgPointlight = new CustomPointLight(r, g, b, a, lightDistance, origPos);
        CustomPointLight nonInvBgPointLight =  new CustomPointLight(r, g, b, a, lightDistance, new Vector2(origPos.x,origPos.y+((BgHandler.bgh-((origPos.y-BgHandler.separatorHeight)))*2)));
        return new CustomPointLight[]{invBgPointlight, nonInvBgPointLight};
    }

    private static CustomPointLight[] newPointLightMirrored(Color color, int lightDistance, Vector2 origPos) {   //input newPointLight in invertedBG and return a list with inverted and non inverted bg pointlights
        CustomPointLight invBgPointlight = new CustomPointLight(color, lightDistance, origPos);
        CustomPointLight nonInvBgPointLight =  new CustomPointLight(color, lightDistance, new Vector2(origPos.x,origPos.y+((BgHandler.bgh-((origPos.y-BgHandler.separatorHeight)))*2)));
        return new CustomPointLight[]{invBgPointlight, nonInvBgPointLight};
    }
    private static CustomConeLight[] newConeLightMirrored(int r, int g, int b, float a, int lightDistance, Vector2 origPos) {   //input newPointLight in invertedBG and return a list with inverted and non inverted bg pointlights
        CustomConeLight invBgCoinlight = new CustomConeLight(r, g, b, a, lightDistance, origPos, 270, 90);
        CustomConeLight noninvBgCoinlight =  new CustomConeLight(r, g, b, a, lightDistance, new Vector2(origPos.x,origPos.y+((BgHandler.bgh-((origPos.y-BgHandler.separatorHeight)))*2)), 90, 180);
        noninvBgCoinlight.setSoft(true);
        return new CustomConeLight[]{invBgCoinlight, noninvBgCoinlight};
    }

    public static void newBgLighting(int bgNumber) {
        for (Light i : bgLights) { //if bg is reset we have to add - height of bg back to lights that are still in play when vert resets
            if (i instanceof CustomPointLight)      { System.out.println(((CustomPointLight) i).origPos.y+" set to "+(((CustomPointLight) i).origPos.y-bgStackHeight)); ((CustomPointLight) i).origPos= new Vector2(((CustomPointLight) i).origPos.x, ((CustomPointLight) i).origPos.y  -bgStackHeight);}

            else if (i instanceof CustomChainLight) ((CustomChainLight) i).origVerts = new float[]{((CustomChainLight) i).origVerts[0],((CustomChainLight) i).origVerts[1]-bgStackHeight,
                    ((CustomChainLight) i).origVerts[2],((CustomChainLight) i).origVerts[3]-bgStackHeight,((CustomChainLight) i).origVerts[4],((CustomChainLight) i).origVerts[5]-bgStackHeight};

            else                                    ((CustomConeLight) i).origPos = new Vector2(((CustomConeLight) i).origPos.x, ((CustomConeLight) i).origPos.y - bgStackHeight);
        }

        bgLights.add(newPointLight(255, 255, 255, cloudA, cloudDia, cloudTopLeftPos));  System.out.println("cloud"); //add cloudLights no matter what
        bgLights.add(newPointLight(255, 255, 255, cloudA, cloudDia, cloudTopRightPos)); System.out.println("cloud");
        if (bgNumber < 9) {//phoenix
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP1));
            Collections.addAll(bgLights, newConeLightMirrored( 255, 237, 137, lA, xlDia, cityC2));//cone
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xlDia, cityP3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, lDia, cityP5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, mDia, cityP7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mlA,xxlDia, cityP9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityP15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xxxlDia, cityP17));
        } else if (bgNumber < 18) {//thunder
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP1));
            Collections.addAll(bgLights, newConeLightMirrored( 255, 237, 137, lA, xlDia, cityC2));//cone
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xlDia, cityP3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, lDia, cityP5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, mDia, cityP7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, xlDia, cityP9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityP15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xxxlDia, cityP17));
        } else if (bgNumber < 27) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP1));
            Collections.addAll(bgLights, newConeLightMirrored( 255, 237, 137, lA, xlDia, cityC2));//cone
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xlDia, cityP3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, lDia, cityP5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, mDia, cityP7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, xlDia, cityP9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityP15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xxxlDia, cityP17));
        } else if (bgNumber < 36) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP1));
            Collections.addAll(bgLights, newConeLightMirrored( 255, 237, 137, lA, xlDia, cityC2));//cone
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xlDia, cityP3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, lDia, cityP5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, mDia, cityP7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, xlDia, cityP9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityP15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xxxlDia, cityP17));
        } else if (bgNumber < 45) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP1));
            Collections.addAll(bgLights, newConeLightMirrored( 255, 237, 137, lA, xlDia, cityC2));//cone
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xlDia, cityP3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, lDia, cityP5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, mDia, cityP7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, xlDia, cityP9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityP15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xxxlDia, cityP17));
        } else if (bgNumber < 54) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP1));
            Collections.addAll(bgLights, newConeLightMirrored( 255, 237, 137, lA, xlDia, cityC2));//cone
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xlDia, cityP3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, lDia, cityP5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, mDia, cityP7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, xlDia, cityP9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityP15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xxxlDia, cityP17));
        } else if (bgNumber < 63) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP1));
            Collections.addAll(bgLights, newConeLightMirrored( 255, 237, 137, lA, xlDia, cityC2));//cone
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xlDia, cityP3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, lDia, cityP5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, mDia, cityP7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, xlDia, cityP9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityP15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xxxlDia, cityP17));
        } else if (bgNumber < 72) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP1));
            Collections.addAll(bgLights, newConeLightMirrored( 255, 237, 137, lA, xlDia, cityC2));//cone
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xlDia, cityP3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, lDia, cityP5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, mDia, cityP7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, xlDia, cityP9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, sDia, cityP10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, lA, mDia, cityP12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityP15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, mDia, cityP16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, mA, xxxlDia, cityP17));
        }
        System.out.println("Size of bgLights list: "+bgLights.size());
    }

    public void setCam(GameRenderer renderer) {
        cam = renderer.cam;
        rayHandler.setCombinedMatrix(cam);
    }

    public boolean isOffCam(Light l) {
        return l.getY() + l.getDistance()/2 < 0; //if light yPos+radius*0.66 is lower than 0, delete it
    }

    public void update() {
        float bgVert = BgHandler.vert.getValue();
        float bgHoriz = BgHandler.horiz.getValue();
        for (Light i : bgLights) {
            if (isOffCam(i)){ bgLights.remove(i);i.remove(); System.out.println("remove");}
            else if (i instanceof CustomPointLight)  i.setPosition(((CustomPointLight) i).origPos.x + bgHoriz, ((CustomPointLight) i).origPos.y + bgVert);
            else if (i instanceof ChainLight)        {i.setPosition(((CustomChainLight) i).origVerts[0] + bgHoriz, ((CustomChainLight) i).origVerts[1] + bgVert);}
            else                                     i.setPosition(((CustomConeLight) i).origPos.x + bgHoriz, ((CustomConeLight) i).origPos.y + bgVert);
        }
        rayHandler.update();   //the render part might need to be put at the end of the render cycle in gameRenderer
    }

    public static void render() {
        rayHandler.render();
    }
}
