package com.kredatus.flockblockers.Handlers;

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
import box2dLight.PointLight;
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
            cityL1 = new Vector2(0               * bgw, 0.49826455843f * bgStackHeight),//cityLight1
            cityL2 = new Vector2(0.14990234375f  * bgw, 0.54454300039f * bgStackHeight),
            cityL3 = new Vector2(0.16015625f     * bgw, 0.5896644813f  * bgStackHeight),
            cityL4 = new Vector2(0.19384765625f     * bgw, 0.5942923255f  * bgStackHeight),
            cityL5 = new Vector2(0.21142578125f  * bgw, 0.42286926341f * bgStackHeight),
            cityL6 = new Vector2(0.21398305084f     * bgw, 0.54512148091f  * bgStackHeight),
            cityL7 = new Vector2(0.30322265625f  * bgw, 0.48283841111f * bgStackHeight),
            cityL8 = new Vector2(0.46533203125f  * bgw, 0.50308522947f * bgStackHeight),
            cityL9 = new Vector2(0.46949152542f     * bgw, 0.51870420363f  * bgStackHeight),
            cityL10 = new Vector2(0.53759765625f  * bgw, 0.62070960278f * bgStackHeight),
            sunPos = new Vector2( 0.59082f        * bgw,      0.482067f * bgStackHeight),
            cityL11 = new Vector2(0.51694915254f     * bgw, 0.59679907444f  * bgStackHeight),
            cityL13 = new Vector2(0.685546875f    * bgw, 0.50096413421f * bgStackHeight),
            cityL12 = new Vector2(0.70458984375f     * bgw, 0.59660624759f  * bgStackHeight),
            cityL14 = new Vector2(0.87744140625f  * bgw, 0.50057848053f * bgStackHeight),
            cityL15 = new Vector2(0.94091796875f  * bgw, 0.44369456229f * bgStackHeight),
                    cityL16 = new Vector2(0.955078125f     * bgw, 0.54512148091f * bgStackHeight),
            cityL17 = new Vector2(0.9853515625f  * bgw, 0.59602776707f * bgStackHeight);

    //PARAMETER CONFIG
    private static int cloudDia=3300, sunDia=2300, sDia=50,mDia=100,mlDia=200, LDia=300, xlDia=450, xxlDia=650, xxxlDia=1000;
    private static float cloudA=0.9f, sunA=0.95f,sA=0.5f,mA=0.7f,mlA=0.8f,lA=0.9f,xlA=0.95f, xxlA=1.0f;

    public LightHandler(BgHandler bgHandler) {

        //rayHandler.useDiffuseLight(true); //smoother but makes everywhere but light dark
        rayHandler.setAmbientLight(0.5f);   //light everywhere outside of our set lights
        rayHandler.setGammaCorrection(true);    //play with all the options to see what fits best
        newBgLighting(bgHandler.bgNumber);
        bgLights.add(newPointLight(255,255,255,cloudA,cloudDia,cloudBottomLeftPos)); System.out.println("cloud");
        bgLights.add(newPointLight(255,255,255,cloudA,cloudDia,cloudBottomRightPos));System.out.println("cloud");
        //bgLights.add(new DirectionalLight(rayHandler,30, new Color(233,33,33,0.5f), 90)); //directional light is like ambient light but with direction (for shadows)
    }

    private static CustomChainLight[] newBarLight(int r, int g, int b, float a, int lightDistance, int dirDeg, float[] chainVertices) {
        CustomChainLight newChainLightFront = new CustomChainLight(r, g, b, a, lightDistance, dirDeg,(int) (chainVertices.length/2f)*20, chainVertices );
        CustomChainLight newChainLightBack  = new CustomChainLight(r, g, b, a, lightDistance, -dirDeg, (int) (chainVertices.length/2f)*20, chainVertices );
        newChainLightBack.setSoft(false);         //no need for softness as not hitting obstacles
        newChainLightBack.setStaticLight(false);  //static lights dont interact with obstacles, redudes cpu load by 90%
        newChainLightBack.setXray(false);         //beams go through obstacles, reduces CPU burden of light about 70%
        newChainLightFront.setSoft(false);        //no need for softness as not hitting obstacles
        newChainLightFront.setStaticLight(false); //static lights dont interact with obstacles, redudes cpu load by 90%
        newChainLightFront.setXray(false);        //beams go through obstacles, reduces CPU burden of light about 70%
        return new CustomChainLight[]{newChainLightFront, newChainLightBack};
    }

    //(multiply width by bgh and height by *bgStackHeight respectively) positions of bg light sources are 0.5 x .154454 for clouds, .59082 x 0.517933 for first sun, .59082 x .791747 for second sun
    private static CustomPointLight newPointLight(int r, int g, int b, float a, int lightDistance, Vector2 origPos) {
        CustomPointLight newPointLight = new CustomPointLight(r, g, b, a, lightDistance, origPos);
        newPointLight.setSoft(false);
        newPointLight.setStaticLight(false);
        newPointLight.setXray(false);
        return newPointLight;
    }

    private static PointLight[] newPointLightMirrored(int r, int g, int b, float a, int lightDistance, Vector2 origPos) {   //input newPointLight in invertedBG and return a list with inverted and non inverted bg pointlights
        CustomPointLight invBgPointlight = new CustomPointLight(r, g, b, a, lightDistance, origPos);
        PointLight nonInvBgPointLight =  new CustomPointLight(r, g, b, a, lightDistance, new Vector2(origPos.x,origPos.y+((BgHandler.bgh-((origPos.y-BgHandler.separatorHeight)))*2)));
        return new PointLight[]{invBgPointlight, nonInvBgPointLight};
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
        if (bgNumber < 9) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    System.out.println("phoenix"); //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL1));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL2));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL17));
        } else if (bgNumber < 18) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    System.out.println("phoenix"); //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL1));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL2));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL17));
        } else if (bgNumber < 27) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    System.out.println("phoenix"); //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL1));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL2));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL17));
        } else if (bgNumber < 36) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    System.out.println("phoenix"); //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL1));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL2));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL17));
        } else if (bgNumber < 45) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    System.out.println("phoenix"); //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL1));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL2));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL17));
        } else if (bgNumber < 54) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    System.out.println("phoenix"); //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL1));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL2));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL17));
        } else if (bgNumber < 63) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    System.out.println("phoenix"); //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL1));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL2));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL17));
        } else if (bgNumber < 72) {
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sunA, sunDia, sunPos));    System.out.println("phoenix"); //puts in invertedBgSun and mirrored sun
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL1));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL2));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL3));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL4));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL5));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL6));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL7));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL8));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL9));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL10));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL11));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL12));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL13));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL14));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL15));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL16));
            Collections.addAll(bgLights, newPointLightMirrored(255, 237, 137, sA, sDia, cityL17));
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
