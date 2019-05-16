// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
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
    public static RayHandler foreRayHandler = new RayHandler(null);
    public static RayHandler backRayHandler = new RayHandler(null);
    private static float bgw = BgHandler.bgw, bgStackHeight= BgHandler.bgStackHeight, cloudBottomY= 0.154454f * bgStackHeight, cloudTopY=cloudBottomY+bgStackHeight;
    private static final Vector2        //light positions in Vector2
            cloudBottomLeftPos  = new Vector2(0,   cloudBottomY),
            cloudBottomMidPos   = new Vector2(0.95f*bgw/2, cloudBottomY),
            cloudBottomRightPos = new Vector2(bgw, cloudBottomY),
            cloudTopLeftPos  = new Vector2(   0,   cloudTopY),
            cloudTopMidPos   = new Vector2(   1*bgw/2, cloudTopY),
            cloudTopRightPos = new Vector2(   bgw, cloudTopY),
    //2 3 and 4 wrong pos vert and horiz
            cityP1 =  new Vector2(0               * bgw, 0.49922869263f * bgStackHeight),//cityPight1
            cityC2 =  new Vector2(0.15380859375f  * bgw, 0.54454300039f * bgStackHeight),//coneLight
            cityP3 =  new Vector2(0.16064453125f  * bgw, 0.58927882761f * bgStackHeight),
            cityP4 =  new Vector2(0.193359375f    * bgw, 0.59429232549f * bgStackHeight),
            cityP5 =  new Vector2(0.21142578125f  * bgw, 0.42267643656f * bgStackHeight),
            cityP6 =  new Vector2(0.24609375f     * bgw, 0.54512148091f * bgStackHeight),
            cityP7 =  new Vector2(0.30322265625f  * bgw, 0.48438102583f * bgStackHeight),
            cityP8 =  new Vector2(0.46533203125f  * bgw, 0.50617045892f * bgStackHeight),
            cityP9 =  new Vector2(0.54515625f     * bgw, 0.62032394909f * bgStackHeight),
            cityP10 = new Vector2(0.54052734375f  * bgw, 0.51851137678f * bgStackHeight),
            sunPos  = new Vector2(0.59282f        * bgw, 0.525f         * bgStackHeight),
            cityP11 = new Vector2(0.595703125f    * bgw, 0.59679907443f * bgStackHeight),
            cityP12 = new Vector2(0.685546875f    * bgw, 0.50096413421f * bgStackHeight),
            cityP13 = new Vector2(0.7041015625f   * bgw, 0.59660624759f * bgStackHeight),
            cityP14 = new Vector2(0.87744140625f  * bgw, 0.50057848053f * bgStackHeight),
            cityP15 = new Vector2(0.94091796875f  * bgw, 0.44369456229f * bgStackHeight),
            cityP16 = new Vector2(0.955078125f    * bgw, 0.54512148091f * bgStackHeight),
            cityP17 = new Vector2(0.9953515625f   * bgw, 0.60302776707f * bgStackHeight);

    //PARAMETER CONFIG
    private static int cloudDia=1700, sunDia=1000, xxxxsDia=30, xxxsDia=50, xxsDia=80,xsDia=130, sDia=180, smDia=230, mDia=280, mlDia=350, lDia=450, xlDia=700, xxlDia=1000, xxxlDia=1300, xxxxlDia=1600;
    private static float cloudA=1.00f, sunA=0.85f,  xxsA=0.23f, xsA=0.35f, sA=0.46f,smA=0.56f,mA=0.65f,mlA=0.73f, lA =0.80f,xlA=0.86f, xxlA=0.91f, xxxlA=0.95f, xxxxlA=1.00f;

    public static float rayHandlerAmbLightLvl=0.75f;
    public LightHandler(BgHandler bgHandler) {
        //foreRayHandler.useDiffuseLight(true); //smoother but makes everywhere but light dark

        foreRayHandler.setAmbientLight(0.70f);  //++ makes backhandler lights brighter, -- makes birds darker outside of forehandler lights and forehandler lights brighter
        backRayHandler.setAmbientLight(0.75f);  //-- makes backhandler lights darker and background much darker

        //backRayHandler.setGammaCorrection(false);
        //foreRayHandler.setGammaCorrection(false);    //play with all the options to see what fits best

        newBgLighting(bgHandler.bgNumber);
        bgLights.add(newPointLight(foreRayHandler, 255,255,255,cloudA,cloudDia,cloudBottomLeftPos));
        bgLights.add(newPointLight(foreRayHandler, 255,255,255,cloudA-.2f,cloudDia,cloudBottomMidPos));
        bgLights.add(newPointLight(foreRayHandler, 255,255,255,cloudA,cloudDia,cloudBottomRightPos));
        //bgLights.add(new DirectionalLight(foreRayHandler,30, new Color(233,33,33,0.5f), 90)); //directional light is like ambient light but with direction (for shadows)
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
    public static CustomPointLight newPointLight(RayHandler rayHandler, int r, int g, int b, float a, int lightDistance, Vector2 origPos) {
        CustomPointLight newPointLight = new CustomPointLight(rayHandler, r, g, b, a, lightDistance, origPos);
        newPointLight.setSoft(false);
        newPointLight.setStaticLight(false);
        newPointLight.setXray(false);
        return newPointLight;
    }
    public static CustomConeLight newConeLight(RayHandler rayHandler, int r, int g, int b, float a, int lightDistance, Vector2 origPos, float dirDeg, float coneDegHalf) {
        CustomConeLight newConeLight = new CustomConeLight(rayHandler, r, g, b, a, lightDistance, origPos,dirDeg,coneDegHalf);
        newConeLight.setSoft(false);
        newConeLight.setStaticLight(false);
        newConeLight.setXray(false);
        return newConeLight;
    }

    public static CustomPointLight[] newPointLightMirrored(RayHandler rayHandler, int r, int g, int b, float a, int lightDistance, Vector2 origPos) {   //input newPointLight in invertedBG and return a list with inverted and non inverted bg pointlights
        CustomPointLight invBgPointlight = new CustomPointLight(rayHandler, r, g, b, a, lightDistance, origPos);
        CustomPointLight nonInvBgPointLight =  new CustomPointLight(rayHandler, r, g, b, a, lightDistance, new Vector2(origPos.x,origPos.y+((BgHandler.bgh-((origPos.y-BgHandler.separatorHeight)))*2)));
        return new CustomPointLight[]{invBgPointlight, nonInvBgPointLight};
    }

    private static CustomConeLight[] newConeLightMirrored(RayHandler rayHandler, int r, int g, int b, float a, int lightDistance, Vector2 origPos, float dirDeg, float coneDegHalf) {   //input newPointLight in invertedBG and return a list with inverted and non inverted bg pointlights
        CustomConeLight invBgCoinlight = new CustomConeLight(rayHandler, r, g, b, a, lightDistance, origPos, dirDeg+180, coneDegHalf);//lower light facing downwards
        CustomConeLight noninvBgCoinlight =  new CustomConeLight(rayHandler, r, g, b, a, lightDistance, new Vector2(origPos.x,origPos.y+((BgHandler.bgh-((origPos.y-BgHandler.separatorHeight)))*2)), dirDeg, coneDegHalf);//upper light facing upwards
        //noninvBgCoinlight.setSoft(true);
        return new CustomConeLight[]{invBgCoinlight, noninvBgCoinlight};
    }

    public static void newBgLighting(int bgNumber) {
        for (Light i : bgLights) { //if bg is reset we have to add - height of bg back to lights that are still in play when vert resets
            if (i instanceof CustomPointLight)      {((CustomPointLight) i).origPos= new Vector2(((CustomPointLight) i).origPos.x, ((CustomPointLight) i).origPos.y  -bgStackHeight);}

            else if (i instanceof CustomChainLight) ((CustomChainLight) i).origVerts = new float[]{((CustomChainLight) i).origVerts[0],((CustomChainLight) i).origVerts[1]-bgStackHeight,
                    ((CustomChainLight) i).origVerts[2],((CustomChainLight) i).origVerts[3]-bgStackHeight,((CustomChainLight) i).origVerts[4],((CustomChainLight) i).origVerts[5]-bgStackHeight};

            else                                    ((CustomConeLight) i).origPos = new Vector2(((CustomConeLight) i).origPos.x, ((CustomConeLight) i).origPos.y - bgStackHeight);
        }

        bgLights.add(newPointLight(foreRayHandler, 255, 255, 255, cloudA, cloudDia, cloudTopLeftPos));   //add cloudLights no matter what
        bgLights.add(newPointLight(foreRayHandler, 255, 255, 255, cloudA-.2f, cloudDia, cloudTopMidPos));
        bgLights.add(newPointLight(foreRayHandler, 255, 255, 255, cloudA, cloudDia, cloudTopRightPos));
        if (bgNumber < 9) {         //phoenix
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 237, 137, sunA, sunDia, sunPos));       //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 237, 137, xsA, mlDia, cityP1));         //skyScraper tip
            Collections.addAll(bgLights, newConeLightMirrored(foreRayHandler,  255, 237, 137, sA, xlDia, cityC2,90,90));       //cone
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 237, 137, sA, xxlDia, cityP3));        //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 0, 0,     xxlA, xsDia, cityP4));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 237, 137, smA, xxlDia, cityP5));        //skyScraper tip***************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 0, 0,     xxxlA, xxsDia, cityP6));    //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 0, 0,     sA, xsDia, cityP7));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 0, 0,     mlA, xsDia, cityP8));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 237, 137, xsA, lDia, cityP9));          //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 0, 0,     xxxxlA, xxxxsDia, cityP10));  //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 0, 0,     xxxxlA, xsDia, cityP11));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 0, 0,     mlA, xxsDia, cityP12));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 0, 0,     xxxxlA, sDia, cityP13));      //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 0, 0,     xxxlA, xxxsDia, cityP14));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 237, 137, mA, xxlDia, cityP15));        //skyScraper tip**********************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 237, 137, lA, mDia, cityP16));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 237, 137, lA, xlDia, cityP17));         //ambient lightspot
        } else if (bgNumber < 18) { //thunder
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 170, 140, sunA, sunDia, sunPos));       //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 170, 140, xsA, mlDia, cityP1));         //skyScraper tip
            Collections.addAll(bgLights, newConeLightMirrored(foreRayHandler,  255, 170, 140, sA, xlDia, cityC2,90,90));       //cone));       //cone
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 170, 140, sA, xxlDia, cityP3));        //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 1, 55,     xxlA, xsDia, cityP4));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 170, 140, smA, xxlDia, cityP5));        //skyScraper tip***************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 1, 55,     xxxlA, xxsDia, cityP6));    //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 1, 55,     sA, xsDia, cityP7));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 1, 55,     mlA, xsDia, cityP8));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 170, 140, xsA, lDia, cityP9));          //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 1, 55,     xxxxlA, xxxxsDia, cityP10));  //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 1, 55,     xxxxlA, xsDia, cityP11));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 1, 55,     mlA, xxsDia, cityP12));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 1, 55,     xxxxlA, sDia, cityP13));      //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 1, 55,     xxxlA, xxxsDia, cityP14));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 170, 140, mA, xxlDia, cityP15));        //skyScraper tip**********************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 170, 140, lA, mDia, cityP16));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 170, 140, lA, xlDia, cityP17));         //ambient lightspot
        } else if (bgNumber < 27) {//water
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 43, 158, 238, sunA, sunDia, sunPos));       //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 43, 158, 238, xsA, mlDia, cityP1));         //skyScraper tip
            Collections.addAll(bgLights, newConeLightMirrored(foreRayHandler,  43, 158, 238, sA, xlDia, cityC2,90,90));       //cone));       //cone
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 43, 158, 238, sA, xxlDia, cityP3));        //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 1, 132, 222,     xxlA, xsDia, cityP4));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 43, 158, 238, smA, xxlDia, cityP5));        //skyScraper tip***************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 1, 132, 222,     xxxlA, xxsDia, cityP6));    //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 1, 132, 222,     sA, xsDia, cityP7));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 1, 132, 222,     mlA, xsDia, cityP8));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 43, 158, 238, xsA, lDia, cityP9));          //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 1, 132, 222,     xxxxlA, xxxxsDia, cityP10));  //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 1, 132, 222,     xxxxlA, xsDia, cityP11));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 1, 132, 222,     mlA, xxsDia, cityP12));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 1, 132, 222,     xxxxlA, sDia, cityP13));      //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 1, 132, 222,     xxxlA, xxxsDia, cityP14));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 43, 158, 238, mA, xxlDia, cityP15));        //skyScraper tip**********************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 43, 158, 238, lA, mDia, cityP16));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 43, 158, 238, lA, xlDia, cityP17));         //ambient lightspot
        } else if (bgNumber < 36) {//fire
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 227, 133, 37, sunA, sunDia, sunPos));       //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 227, 133, 37, xsA, mlDia, cityP1));         //skyScraper tip
            Collections.addAll(bgLights, newConeLightMirrored(foreRayHandler,  227, 133, 37, sA, xlDia, cityC2,90,90));       //cone));       //cone
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 227, 133, 37, sA, xxlDia, cityP3));        //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 219, 36, 118,     xxlA, xsDia, cityP4));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 227, 133, 37, smA, xxlDia, cityP5));        //skyScraper tip***************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 219, 36, 118,     xxxlA, xxsDia, cityP6));    //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 219, 36, 118,     sA, xsDia, cityP7));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 219, 36, 118,     mlA, xsDia, cityP8));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 227, 133, 37, xsA, lDia, cityP9));          //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 219, 36, 118,     xxxxlA, xxxxsDia, cityP10));  //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 219, 36, 118,     xxxxlA, xsDia, cityP11));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 219, 36, 118,     mlA, xxsDia, cityP12));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 219, 36, 118,     xxxxlA, sDia, cityP13));      //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 219, 36, 118,     xxxlA, xxxsDia, cityP14));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 227, 133, 37, mA, xxlDia, cityP15));        //skyScraper tip**********************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 227, 133, 37, lA, mDia, cityP16));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 227, 133, 37, lA, xlDia, cityP17));         //ambient lightspot
        } else if (bgNumber < 45) {//acid
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 94, 252, 177, sunA, sunDia, sunPos));       //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 94, 252, 177, xsA, mlDia, cityP1));         //skyScraper tip
            Collections.addAll(bgLights, newConeLightMirrored(foreRayHandler,  94, 252, 177, sA, xlDia, cityC2,90,90));       //cone));       //cone
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 94, 252, 177, sA, xxlDia, cityP3));        //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 85, 236, 0,     xxlA, xsDia, cityP4));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 94, 252, 177, smA, xxlDia, cityP5));        //skyScraper tip***************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 85, 236, 0,     xxxlA, xxsDia, cityP6));    //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 85, 236, 0,     sA, xsDia, cityP7));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 85, 236, 0,     mlA, xsDia, cityP8));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 94, 252, 177, xsA, lDia, cityP9));          //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 85, 236, 0,     xxxxlA, xxxxsDia, cityP10));  //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 85, 236, 0,     xxxxlA, xsDia, cityP11));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 85, 236, 0,     mlA, xxsDia, cityP12));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 85, 236, 0,     xxxxlA, sDia, cityP13));      //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 85, 236, 0,     xxxlA, xxxsDia, cityP14));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 94, 252, 177, mA, xxlDia, cityP15));        //skyScraper tip**********************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 94, 252, 177, lA, mDia, cityP16));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 94, 252, 177, lA, xlDia, cityP17));         //ambient lightspot
        } else if (bgNumber < 54) {//night
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 220, 221, 226, sunA, sunDia, sunPos));       //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 220, 221, 226, xsA, mlDia, cityP1));         //skyScraper tip
            Collections.addAll(bgLights, newConeLightMirrored(foreRayHandler,  220, 221, 226, sA, xlDia, cityC2,90,90));       //cone));       //cone
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 220, 221, 226, sA, xxlDia, cityP3));        //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 32, 33, 37,     xxlA, xsDia, cityP4));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 220, 221, 226, smA, xxlDia, cityP5));        //skyScraper tip***************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 32, 33, 37,     xxxlA, xxsDia, cityP6));    //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 32, 33, 37,     sA, xsDia, cityP7));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 32, 33, 37,     mlA, xsDia, cityP8));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 220, 221, 226, xsA, lDia, cityP9));          //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 32, 33, 37,     xxxxlA, xxxxsDia, cityP10));  //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 32, 33, 37,     xxxxlA, xsDia, cityP11));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 32, 33, 37,     mlA, xxsDia, cityP12));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 32, 33, 37,     xxxxlA, sDia, cityP13));      //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 32, 33, 37,     xxxlA, xxxsDia, cityP14));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 220, 221, 226, mA, xxlDia, cityP15));        //skyScraper tip**********************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 220, 221, 226, lA, mDia, cityP16));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 220, 221, 226, lA, xlDia, cityP17));         //ambient lightspot
        } else if (bgNumber < 63) {//lunar
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 230, 49, 252, sunA, sunDia, sunPos));       //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 230, 49, 252, xsA, mlDia, cityP1));         //skyScraper tip
            Collections.addAll(bgLights, newConeLightMirrored(foreRayHandler,  230, 49, 252, sA, xlDia, cityC2,90,90));       //cone));       //cone
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 230, 49, 252, sA, xxlDia, cityP3));        //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 3, 4, 206,     xxlA, xsDia, cityP4));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 230, 49, 252, smA, xxlDia, cityP5));        //skyScraper tip***************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 3, 4, 206,     xxxlA, xxsDia, cityP6));    //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 3, 4, 206,     sA, xsDia, cityP7));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 3, 4, 206,     mlA, xsDia, cityP8));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 230, 49, 252, xsA, lDia, cityP9));          //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 3, 4, 206,     xxxxlA, xxxxsDia, cityP10));  //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 3, 4, 206,     xxxxlA, xsDia, cityP11));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 3, 4, 206,     mlA, xxsDia, cityP12));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 3, 4, 206,     xxxxlA, sDia, cityP13));      //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 3, 4, 206,     xxxlA, xxxsDia, cityP14));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 230, 49, 252, mA, xxlDia, cityP15));        //skyScraper tip**********************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 230, 49, 252, lA, mDia, cityP16));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 230, 49, 252, lA, xlDia, cityP17));         //ambient lightspot
        } else if (bgNumber < 72) {//gold
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 255, 67, sunA, sunDia, sunPos));       //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 255, 67, xsA, mlDia, cityP1));         //skyScraper tip
            Collections.addAll(bgLights, newConeLightMirrored(foreRayHandler,  255, 255, 67, sA, xlDia, cityC2,90,90));       //cone));       //cone
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 255, 67, sA, xxlDia, cityP3));        //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 251, 2, 0,     xxlA, xsDia, cityP4));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 255, 67, smA, xxlDia, cityP5));        //skyScraper tip***************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 251, 2, 0,     xxxlA, xxsDia, cityP6));    //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 251, 2, 0,     sA, xsDia, cityP7));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 251, 2, 0,     mlA, xsDia, cityP8));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 255, 67, xsA, lDia, cityP9));          //ambient lightspot
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 251, 2, 0,     xxxxlA, xxxxsDia, cityP10));  //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 251, 2, 0,     xxxxlA, xsDia, cityP11));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 251, 2, 0,     mlA, xxsDia, cityP12));        //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 251, 2, 0,     xxxxlA, sDia, cityP13));      //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 251, 2, 0,     xxxlA, xxxsDia, cityP14));     //red signal
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 255, 67, mA, xxlDia, cityP15));        //skyScraper tip**********************
            Collections.addAll(bgLights, newPointLightMirrored(backRayHandler, 255, 255, 67, lA, mDia, cityP16));          //red signal
            Collections.addAll(bgLights, newPointLightMirrored(foreRayHandler, 255, 255, 67, lA, xlDia, cityP17));         //ambient lightspot
        }
    }

    public void setCam(GameRenderer renderer) {
        cam = renderer.cam;
        foreRayHandler.setCombinedMatrix(cam);
        backRayHandler.setCombinedMatrix(cam);
    }

    public boolean isOffCam(Light l) {
        return l.getY() + l.getDistance() < 0; //if light yPos+radius*0.66 is lower than 0, delete it
    }

/*if (rayHandlerAmbLightLvl<=0.75) {
            rayHandlerAmbLightLvl+=0.001f;
            if (rayHandlerAmbLightLvl <= 0.70)
                foreRayHandler.setAmbientLight(rayHandlerAmbLightLvl);
            if (rayHandlerAmbLightLvl <= 0.75)
                backRayHandler.setAmbientLight(rayHandlerAmbLightLvl);
        }*/
    public void update() {
        if (rayHandlerAmbLightLvl<=0.75&&BgHandler.lightsBrightening) {
            rayHandlerAmbLightLvl+=Math.abs(BgHandler.yVel/17000f);
            //System.out.println("+ "+BgHandler.yVel/7000f);
                foreRayHandler.setAmbientLight(rayHandlerAmbLightLvl-0.05f);
                backRayHandler.setAmbientLight(rayHandlerAmbLightLvl);

        } else if (rayHandlerAmbLightLvl>=0.40&&!BgHandler.lightsBrightening) {
            rayHandlerAmbLightLvl-=Math.abs(BgHandler.yVel/13000f);
            //System.out.println("- "+BgHandler.yVel/7000f);
            foreRayHandler.setAmbientLight(rayHandlerAmbLightLvl-0.05f);
            backRayHandler.setAmbientLight(rayHandlerAmbLightLvl);
        }
        System.out.println(rayHandlerAmbLightLvl);

        //System.out.println(Math.abs(BgHandler.vert.get()/BgHandler.bgStackHeight));
        float bgVert = BgHandler.vert.get();
        float bgHoriz = BgHandler.horiz.get();
        for (Light i : bgLights) {
            if (isOffCam(i)){ bgLights.remove(i);i.remove(); }
            else if (i instanceof CustomPointLight)  i.setPosition(((CustomPointLight) i).origPos.x + bgHoriz, ((CustomPointLight) i).origPos.y + bgVert);
            else if (i instanceof ChainLight)        {i.setPosition(((CustomChainLight) i).origVerts[0] + bgHoriz, ((CustomChainLight) i).origVerts[1] + bgVert);}
            else                                     i.setPosition(((CustomConeLight) i).origPos.x + bgHoriz, ((CustomConeLight) i).origPos.y + bgVert);
        }
    }

    public static void renderFront() {
        foreRayHandler.updateAndRender();
    }
    public static void renderBack() {
        backRayHandler.updateAndRender();
    }
}
