package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.kredatus.flockblockers.GameWorld.GameRenderer;

import java.util.concurrent.ConcurrentLinkedQueue;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightHandler {
    private OrthographicCamera cam;
    private BgHandler bgHandler;
    public static ConcurrentLinkedQueue<Light> bgLights=new ConcurrentLinkedQueue<Light>();
    public static World world = new World(new Vector2(0,0),false);//fake world to input to the rayhandler
    private static RayHandler rayHandler=new RayHandler(world);;
    private static final Vector2 cloudLightPos= new Vector2(0.5f*BgHandler.bgw,.154454f*BgHandler.bgStackHeight),
            sun1LightPos= new Vector2(0.59082f*BgHandler.bgw,0.517933f*BgHandler.bgStackHeight),
            sun2LightPos=new Vector2(0.59082f*BgHandler.bgw,.791747f*BgHandler.bgStackHeight);

    public LightHandler(BgHandler bgHandler){


        rayHandler.setAmbientLight(1f);   //light everywhere outside of our set lights

        //bgLights.add(newPointLight());
        //bgLights.add(new DirectionalLight(rayHandler,30, new Color(233,33,33,0.5f), 90));
    }

    //(multiply width by bgh and height by *bgStackHeight respectively) positions of bg light sources are 0.5 x .154454 for clouds, .59082 x 0.517933 for first sun, .59082 x .791747 for second sun
    private static PointLight newPointLight(Color color, float lightDistance){
        PointLight newPointLight = new PointLight(rayHandler,30, color, lightDistance, 0,0);
        newPointLight.setSoft(false);   //no need for softness as not hitting obstacles
        newPointLight.setStaticLight(false); //static lights dont interact with obstacles, redudes cpu load by 90%
        newPointLight.setXray(false);    //beams go through obstacles, reduces CPU burden of light about 70%
        return newPointLight;
    }
    public static void newBgLighting(TextureRegion texture){    //there will always be 2 light sources on the screen between the bg's and glowing cloud separators
        System.out.println("new bg light");
        if (bgLights.size()==3) {bgLights.poll(); System.out.println("take out light");} //get rid of last passed light unless is empty because just starting

        if (texture==AssetHandler.bgCloudSeparatorTexture) {bgLights.add(newPointLight(new Color(255f/255f,255f/255f,255f/255f, 0.85f),2500));System.out.println("Add cloudLight");}
        else if (texture==AssetHandler.bgPhoenixtexture)   {bgLights.add(newPointLight(new Color(255f/255f,237f/255f,137f/255f, .65f),2501));System.out.println("Add 1st phoenixLight");}
        else if (texture==AssetHandler.bgPhoenixtexture2)  {bgLights.add(newPointLight(new Color(255f/255f,237f/255f,137f/255f, .65f),2502));System.out.println("Add 2nd phoenixLight");}
        else if (texture==AssetHandler.bgThundertexture)    bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 0.1f),2001));
        else if (texture==AssetHandler.bgThundertexture2)   bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2002));
        else if (texture==AssetHandler.bgWatertexture)      bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2001));
        else if (texture==AssetHandler.bgWatertexture2)     bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2002));
        else if (texture==AssetHandler.bgFiretexture)       bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2001));
        else if (texture==AssetHandler.bgFiretexture2)      bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2002));
        else if (texture==AssetHandler.bgAcidtexture)       bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2001));
        else if (texture==AssetHandler.bgAcidtexture2)      bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2002));
        else if (texture==AssetHandler.bgNighttexture)      bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2001));
        else if (texture==AssetHandler.bgNighttexture2)     bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2002));
        else if (texture==AssetHandler.bgLunartexture)      bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2001));
        else if (texture==AssetHandler.bgLunartexture2)     bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2002));
        else if (texture==AssetHandler.bgGoldtexture)       bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2001));
        else if (texture==AssetHandler.bgGoldtexture2)      bgLights.add(newPointLight(new Color(255f/255f,33f/255f,33f/255f, 1f),2002));
    }

    public void setCam(GameRenderer renderer){
        cam=renderer.cam;
        rayHandler.setCombinedMatrix(cam);
    }

    public void  update(){
        float bgVert=BgHandler.vert.getValue();
        float bgHoriz=BgHandler.horiz.getValue();
        for (Light i : bgLights){
            if (i.getDistance()==2500) {i.setPosition(cloudLightPos.x+bgHoriz, cloudLightPos.x+bgVert);}//System.out.println("update clouds");}
            else if (i.getDistance()==2501){ i.setPosition(sun1LightPos.x+bgHoriz,sun1LightPos.y+bgVert);}//System.out.println("update sun1");}
            else if (i.getDistance()==2502){ i.setPosition(sun2LightPos.x+bgHoriz,sun2LightPos.y+bgVert);} //System.out.println("update sun2");}
        }
        rayHandler.update();   //the render part might need to be put at the end of the render cycle in gameRenderer
    }
    public static void  render(){
        rayHandler.render();
    }
}
