package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kredatus.flockblockers.CustomLights.CustomConeLight;
import com.kredatus.flockblockers.CustomLights.CustomPointLight;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.LightHandler;
import com.kredatus.flockblockers.TweenAccessors.Value;

import java.util.ArrayList;
import java.util.Arrays;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import box2dLight.Light;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;


public class Airship {  //engines, sideThrusters, armors and health are organized as lvl1-lvl5
    private Circle boundingCir;
    public static Vector2 pos, vel=new Vector2(), thrusterOrigPos; //vel is only used for monitoring not changing pos, lastTouchVel=new Vector2(), acc, dest, lastDest, differenceVector;
    //public boolean was

    public static int balloonWidth, balloonHeight, rackWidth, rackHeight, thrusterWidth, thrusterHeight, armorWidth, armorHeight, height; //x and y are at middle of textures, bottom of balloonTexture,top of rack
    protected boolean isScrolledDown;
    public float midpointY, midpointX, startY,startX;
    private boolean isAlive;
    private ArrayList<Vector2> positions = new ArrayList<Vector2>(16);

    public static int armor=100, health=100, origHealth=health; //slowdownSpeed;

    public static int lvl, engineTuning, armorLvl, sideThrustLvl;   //0-4
    public static TextureRegion balloonTexture, rackTexture, sideThrustTexture, armorTexture;    //balloonTexture is top part of hot air balloon, rack is bottom

    //positions 28,31    82,31  110-136 and 137-163

    public ArrayList<Turret> turretList=new ArrayList<Turret>(13);
    public Polygon rackHitbox, balloonHitbox, prelimBoundPoly1, prelimBoundPoly2;

    public int tW=32, tH=33;
    public static int airshipTouchPointer=-1, camWidth, camHeight;
    public float fingerAirshipXDiff, fingerAirshipYDiff;
    //public static boolean airshipTouched;

    public float currentFlashLength;// dragSpeed;
    public boolean isFlashing;
    public Value flashOpacityValue = new Value(), rotation = new Value();
    public Tween flashTween;
    public TweenCallback endFlashing;
    protected ArrayList<Float> flashLengths=new ArrayList<Float>();

    public Tween tween, burnerLightTween, rightThrusterLightTween, leftThrusterLightTween, rotationTween;
    float inputX, inputY, speedDivisor;
    public static boolean airshipTouched;

    public static float preX, preY, maxInputX;
    float xOffsetFromRotation,yOffsetFromRotation;

    //public static PooledEffect burnerFire, thrusterFireLeft, thrusterFireUp;
    public static Array<PooledEffect> additiveEffects = new Array<PooledEffect>();
    public static Array<ParticleEmitter> emitters = new Array<ParticleEmitter>();

    //public boolean isMovingLeftAndSlowing, isMovingRightAndSlowing;
    public static Array<Light> flameLights = new Array<Light>();
    public float burnerOrigAlpha=0.70f, thrusterOrigAlpha=0.70f;
    public int burnerOrigDist=40, thrusterOrigDist=70;

    public static float[] airshipTint, airShipCloudTint;
    public boolean hitMaxBrightnessCloudBrightening=false;
    public static int[] healthValues=new int[]{100, 200, 350, 550, 800, 1100, 1450,1850,2300,2800}, armorValues={100, 250, 500, 850, 1300, 1850};
    public static TextureRegion[] armorTextures, rackTextures;

    public Airship(int camWidth, int camHeight, int birdType) {
        this.camWidth=camWidth;
        this.camHeight=camHeight;
        armorLvl=0;
        lvl=0;
        sideThrustLvl=0;

        assignTextures(armorLvl,lvl);//also assigns rack bounds
        assignBalloonBounds();

        height=balloonHeight+rackHeight+armorHeight;
        startY=camHeight/2f; //-height;
        startX=0; //-balloonWidth;
        pos=new Vector2(startX, startY);
        thrusterOrigPos=new Vector2(pos.x, pos.y+ 0.18f*balloonHeight);


        tween=Tween.to(pos,0,4).target(camWidth-balloonWidth,camHeight-height).ease(TweenEquations.easeOutCirc).delay(1).start();
        rotationTween=Tween.to(rotation,0,2).waypoint((pos.x-(camWidth-balloonWidth))/25f).target(0).ease(TweenEquations.easeOutCirc).delay(1).start();
        assignRackPositions(pos.x-rackWidth/2f);
        for (int i=0;i<positions.size();i++){
            turretList.add(new Turret('f',positions.get(i)));
            turretList.get(i).rofUp();turretList.get(i).rofUp();turretList.get(i).rofUp();turretList.get(i).rofUp();turretList.get(i).rofUp();turretList.get(i).rofUp();turretList.get(i).rofUp();turretList.get(i).rofUp();turretList.get(i).rofUp();turretList.get(i).rofUp();
            turretList.get(i).penUp();turretList.get(i).penUp();turretList.get(i).penUp();
        }

        /*
        int j=0;
        for (Turret i : turretList){
                for (int k=0;k<j;k++){
                    i.lvlUp();
                }
                j++;
            System.out.println(i.dmg);
            }*/

        endFlashing = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                isFlashing = false;
                flashTween = null;
            }
        };
        for (double i = 0.4f; i <= 6; i += 0.1f) { //  5.6/0.05=66 poss, maxes out at a 13 second flash
            flashLengths.add((float) (Math.pow(flashLengths.size(), 0.7) / 3f + 0.3f));//desmos:y=\left(x^{0.5}+0.3\right)
            //else flashLengths.add(   (float) (                (-(Math.pow(-(flashLengths.size()-25),1.32))/50) +1.8f    ));
        }
        //slowdownSpeed=20;
        //dragSpeed=10f;

        speedDivisor=50f;//50, 65, 80, 95, 110  higher the faster

        loadEffects();

        //also done in BirdHandler class every time background changes
        setupLights();
        setFireColor(birdType);
        airshipTint=chooseColorBasedOnWave(birdType, true);
        airShipCloudTint=airshipTint.clone();
    }

    private void setupLights(){
        //thrusterOrigPos=new Vector2(pos.x);
        flameLights.add(LightHandler.newPointLight(LightHandler.foreRayHandler, 255,255,255,thrusterOrigAlpha,0, new Vector2(thrusterOrigPos.x-thrusterWidth*1.1f, thrusterOrigPos.y+thrusterHeight/1.9f)));
        flameLights.add(LightHandler.newPointLight(LightHandler.foreRayHandler, 255,255,255,thrusterOrigAlpha,0, new Vector2(thrusterOrigPos.x+thrusterWidth*1.1f, thrusterOrigPos.y+thrusterHeight/1.9f)));
        flameLights.add(LightHandler.newPointLight(LightHandler.foreRayHandler, 255,255,255,burnerOrigAlpha,0, new Vector2(pos.x,pos.y+8)));//position same as bottom of burner
        //flameLights.add(LightHandler.newPointLight(LightHandler.foreRayHandler, 255,255,255,burnerOrigAlpha,0, new Vector2(pos.x+15,pos.y+15)));
         //leftThrusterLightTween=Tween.to(flameLights.get(0), 1, 1f).target(thrusterOrigDist).repeatYoyo(2,0);
        //rightThrusterLightTween=Tween.to(flameLights.get(0), 1, 1f).target(thrusterOrigDist).repeatYoyo(2,0);
        burnerLightTween=Tween.to(flameLights.get(2), 1, 1f).target(burnerOrigDist).start();
    }

    private void assignBalloonBounds() {
        float x = pos.x, y = pos.y, rB = balloonWidth / 2f, hB = balloonHeight;

        prelimBoundPoly2 = new Polygon(new float[]{x - balloonWidth / 2f, y, x - balloonWidth / 2f, y + hB, x + balloonWidth / 2f, y + hB, x + balloonWidth / 2f, y});//left side
        prelimBoundPoly1 = new Polygon(new float[]{x - rackWidth / 2f, y, x - rackWidth / 2f, y - rackHeight, x + rackWidth / 2f, y - rackHeight, x + rackWidth / 2f, y});

        balloonHitbox = new Polygon(new float[]{
                x, y + hB, x - rB * 0.60f, y + hB * 0.92f, x - rB * 0.98f, y + hB * 0.67f, x - rB * 0.90f, y + hB * 0.37f, x - rB * 0.40f, y,  //top to bottom left of burner
                x + rB * 0.40f, y, x + rB * 0.90f, y + hB * 0.37f, x + rB * 0.98f, y + hB * 0.67f, x + rB * 0.60f, y + hB * 0.92f //to top of balloon
        });
    }

    private void changeRackBounds(){
        float x = pos.x, y = pos.y;
                if (lvl==0){
                rackHitbox = new Polygon(new float[] {
                        x - tW * 2, y ,         x - tW * 2, y - 1 * tH , //bottom left rack
                        x, y - (1 * tH) - armorHeight,  //tip of bottom of armor
                        x + tW * 2, y - 1 * tH ,     x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==1) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,//bottom left rack
                            x, y - (2 * tH) - armorHeight,  //tip of bottom f armor
                            x + tW * 2, y - 2 * tH ,   x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==2) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,     x - tW * 1.5f, y - 3 * tH ,//bottom left rack
                            x, y - (3 * tH) - armorHeight,  //tip of bottom of armor
                            x + tW * 1.5f, y - 3 * tH ,        x + tW * 2, y - 2 * tH ,    x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==3) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,     x - tW * 1.5f, y - 4 * tH ,//bottom left rack
                            x, y - (4 * tH) - armorHeight,  //tip of bottom of armor
                            x + tW * 1.5f, y - 4 * tH ,        x + tW * 2, y - 2 * tH ,    x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==4) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,     x - tW * 1.5f, y - 4 * tH ,      x - tW * 1f, y - 5 * tH ,//bottom left rack
                            x, y - (5 * tH) - armorHeight,  //tip of bottom of armor
                            x + tW * 1f, y - 5 * tH , x + tW * 1.5f, y - 4 * tH , x + tW * 2, y - 2 * tH , x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                }
        //rackHitbox.setRotation(rotation.get());
        //balloonHitbox.setRotation(rotation.get());
    }

    private void assignRackPositions(float leftXOfAirship) {
        for (int i=0;i<=lvl;i++) {
            if (i<=1) {
                for (int j=0;j<4;j++) {
                    positions.add(new Vector2(leftXOfAirship+j*tW+tW/2f,        pos.y-i*tH - (tH/2)-1 ));
                }
            } else if (i<=3) {
                for (int j=0;j<3;j++) {
                    positions.add(new Vector2(leftXOfAirship+j*tW+(tW/2f)+tW/2f,pos.y-i*tH - (tH/2)-1 ));
                }
            } else if (i<=4) {
                for (int j=0;j<2;j++) {
                    positions.add(new Vector2(leftXOfAirship+j*tW+(tW)+tW/2f,   pos.y-i*tH - (tH/2)-1 ));
                }
            }
        }
    }

    private void assignTextures(int armorLvl, int lvl) {
        balloonTexture=AssetHandler.airshipBalloon;
        sideThrustTexture=AssetHandler.airshipSideThruster;
        balloonWidth=(int) ((balloonTexture.getRegionWidth())*(1+0.2f*lvl)); balloonHeight=balloonTexture.getRegionHeight();
        thrusterWidth=sideThrustTexture.getRegionWidth(); thrusterHeight=(int) (sideThrustTexture.getRegionHeight()*(1+0.2f*sideThrustLvl));
        rackWidth=rackTexture.getRegionWidth(); rackHeight=rackTexture.getRegionHeight();

        armorHeight=armorTexture.getRegionHeight();armorWidth=armorTexture.getRegionWidth();
        for (int i=0;i<5;i++){
            armorTextures[i]=AssetHandler.armor(i);
            rackTextures[i]=AssetHandler.airshipRack(i);
        }
        assignLevelAndArmor(armorLvl,lvl);
    }

    private void assignLevelAndArmor(int armorLvl, int lvl){
        rackTexture=rackTextures[armorLvl];
        rackTexture.setRegion(0,0,rackTextures[armorLvl].getRegionWidth(),tH*lvl + 3);
        armorTexture=armorTextures[armorLvl];

        health=healthValues[lvl];
        armor=armorValues[armorLvl];
        changeRackBounds();
    }

    public boolean pointerOnAirship(int pointer){
        //System.out.println("NEW TOUCH ON AIRSHIP as y: "+y+", posY: "+pos.y+", x: "+x+", posX: "+x);
        float y = -(InputHandler.scaleY(Gdx.input.getY(pointer))-camHeight), x = InputHandler.scaleX(Gdx.input.getX(pointer));
        return y < pos.y + balloonHeight && y > pos.y - rackHeight && x < pos.x + ((balloonWidth + rackWidth) / 4f) && x > pos.x - ((balloonWidth + rackWidth) / 4f);//average width of airship between balloon and rack
    }

    public boolean isOnCam(float x, float y){
        return x > 0 && x < camWidth && y < camHeight && y > 0;
    }

    public void checkBordersAndSlowdown(){
        /*if (vel.x>0)vel.x-=slowdownSpeed; //slowdown
        else if (vel.x<0)vel.x+=slowdownSpeed;

        if (vel.y>0)vel.y-=slowdownSpeed;
        else if (vel.y<0)vel.y+=slowdownSpeed;

        if (pos.x < balloonWidth/2f&&vel.x<0   ||   pos.x>camWidth-balloonWidth/2f&&vel.x>0){
            if (pos.x < balloonWidth/3f   ||   pos.x>camWidth-balloonWidth/3f){
                if (Math.abs(vel.x)>30f){vel.x=Math.signum(vel.x)*30f;}vel.x=-vel.x;//max speed for bounceback
            } else {vel.x*=0.5f;}
        }
        if (pos.y < rackHeight&&vel.y<0   ||   pos.y>camHeight-balloonHeight&&vel.y>0){
            if (pos.y < rackHeight/3f  ||   pos.y>camHeight-balloonHeight/4f){
                if (Math.abs(vel.y)>30f){vel.y=Math.signum(vel.y)*30f;}vel.y=-vel.y;//max speed for bounceback
            } else {vel.y*=0.5f;}
        }*/
    }

    private int getAirshipTouchPointer() {
        for (int i = 0; i < 2; i++) {
            if (pointerOnAirship(i)) {
                return i;
            }
        }
        return -1;
    }

    private void setDestAirship(){
        if (airshipTouchPointer==-1 && Gdx.input.justTouched()) { //if new press and not pressed before
            //System.out.println(InputHandler.scaleX(Gdx.input.getX())+ " *** "+  InputHandler.scaleY(Gdx.input.getY()) );
            airshipTouchPointer=getAirshipTouchPointer();
            if (airshipTouchPointer>=0) {
                //System.out.println("AIRSHIP POINTER TOUCHED");
                airshipTouched=true;
                inputX=InputHandler.scaleX(Gdx.input.getX(airshipTouchPointer));inputY=-(InputHandler.scaleY(Gdx.input.getY(airshipTouchPointer))-camHeight);
                fingerAirshipXDiff=inputX-pos.x;fingerAirshipYDiff=inputY-pos.y;//fingerAirshipDiff doesnt change while finger is pressed which is why we get it once here
            }

        } else if (airshipTouchPointer>=0 && Gdx.input.isTouched(airshipTouchPointer) &&
                (     Math.abs((inputX+fingerAirshipXDiff)-InputHandler.scaleX(Gdx.input.getX(airshipTouchPointer))  ) >0
                    ||Math.abs((inputY+fingerAirshipYDiff)+(InputHandler.scaleY(Gdx.input.getY(airshipTouchPointer))-camHeight)  ) >0     )     ) { //if (after first press) and (airship was pressed) and (airship currently pressed)
            //System.out.println("AIRSHIP POINTER MOVED");
            inputX = InputHandler.scaleX(Gdx.input.getX(airshipTouchPointer)) - fingerAirshipXDiff ;//input with finger touch difference
            inputY = -(InputHandler.scaleY(Gdx.input.getY(airshipTouchPointer))-camHeight) - fingerAirshipYDiff ;

            if (isOnCam(inputX, inputY)) {
                double distance =(Math.sqrt(Math.pow(Math.abs(pos.x-inputX),2)+Math.pow(Math.abs(pos.y-inputY),2)))/speedDivisor;
                //if (distance/speedDivisor<1.5f){//if distance is so small it takes under 1.5s to get there, take 1.5s anyways
                //    tween = Tween.to(pos, 0, 1.5f).target(inputX, inputY).ease(TweenEquations.easeOutQuint).start();
                //} else {

                tween = Tween.to(pos, 0, (float) distance ).target(inputX, inputY).ease(TweenEquations.easeOutQuint).start();
                if (distance>10) distance = 10; //limit rotation
                rotationTween = Tween.to(rotation, 0, 1.5f).waypoint((pos.x-inputX)/25f).target(0).ease(TweenEquations.easeOutCirc).start();
                //rotate to waypoint based on x distance, then back to itself
            }

            if (Gdx.input.getDeltaX(airshipTouchPointer)<-3) {
                fireThruster(2);
                //System.out.println("Thrust Right");
            } else if (Gdx.input.getDeltaX(airshipTouchPointer)>3) {
                fireThruster(1);
               // System.out.println("Thrust Left");
            }

            //if (inputX > balloonWidth/3f   &&  inputX<camWidth-balloonWidth/3f)pos.x=inputX;
            //if (inputY > rackHeight/3f  &&   inputY<camHeight-balloonHeight/4f) pos.y=inputY;
            //} else if (airshipTouchPointer>=0) {//if (airship pointer not pressed and pointer not reset)
        } else if (airshipTouchPointer>=0&&!Gdx.input.isTouched(airshipTouchPointer)) {
            //System.out.println("AIRSHIP POINTER UNTOUCHED");
            //vel.set(Gdx.input.getDeltaX(airshipTouchPointer)*30,-(Gdx.input.getDeltaY(airshipTouchPointer))*30);
            airshipTouchPointer=-1;
            if (airshipTouched)airshipTouched=false;
        }
    }

    private void loadEffects () {
        //burnerFire=AssetHandler.burnerFirePool.obtain(); thrusterFireLeft=AssetHandler.thrusterFireLeftPool.obtain(); thrusterFireUp=AssetHandler.thrusterFireUpPool.obtain();
        additiveEffects = AssetHandler.additiveEffects;
        additiveEffects.get(0).scaleEffect(0.20f);
        additiveEffects.get(1).scaleEffect(0.30f);
        additiveEffects.get(2).scaleEffect(0.30f);
        emitters=AssetHandler.emitters;
        //burnerFire.scaleEffect(0.3f);
        //burnerFire.start();
    }

    public static float[] chooseColorBasedOnWave (int waveTypeCnt, boolean isBalloon) {
        if (!isBalloon){
            if (waveTypeCnt==0) return new float[]{178/255f, 166/255f, 96/255f };
            else if (waveTypeCnt==1) return new float[]{178/255f, 119/255f, 98/255f };
            else if (waveTypeCnt==2) return new float[]{43/255f,  158/255f, 238/255f};
            else if (waveTypeCnt==3) return new float[]{227/255f, 133/255f, 37/255f };
            else if (waveTypeCnt==4) return new float[]{75/255f,  201/255f, 142/255f};
            else if (waveTypeCnt==5) return new float[]{154/255f, 155/255f, 158/255f};
            else if (waveTypeCnt==6) return new float[]{230/255f, 49/255f,  252/255f};
            else if (waveTypeCnt==7) return new float[]{178/255f, 178/255f, 47/255f };
        } else {
                if (waveTypeCnt==0)  return new float[]{255, 180, 148};
            else if (waveTypeCnt==1) return new float[]{249, 50,  109};
            else if (waveTypeCnt==2) return new float[]{43,  158, 238};
            else if (waveTypeCnt==3) return new float[]{227, 133, 37 };
            else if (waveTypeCnt==4) return new float[]{75,  201, 142};
            else if (waveTypeCnt==5) return new float[]{154, 155, 158};
            else if (waveTypeCnt==6) return new float[]{230, 49,  252};
            else if (waveTypeCnt==7) return new float[]{178, 178, 47 };
        }
        return null;
    }

    public static void setFireColor(int waveTypeCnt){
        //System.out.println("Was "+emitters.get(i).getTint().getColors()[0]+", "+emitters.get(i).getTint().getColors()[1]+", "+emitters.get(i).getTint().getColors()[2]);
        //{"pB","tB","wB","fB","aB","nB","lB","gB"};
        //  0    1    2    3    4    5    6    7

        float[] color =null;
        try {
            color = chooseColorBasedOnWave(waveTypeCnt, false);
            assert color!=null;
        } catch (Exception e){
            e.printStackTrace();
        }

        for (int i=0;i<Airship.emitters.size;i++){
            emitters.get(i).getTint().setColors(color);
        }
        for (Light j : flameLights) {
            j.setColor(color[0],color[1],color[2],j.getColor().a);
        }
        //System.out.println("Was "+emitters.get(i).getTint().getColors()[0]+", "+emitters.get(i).getTint().getColors()[1]+", "+emitters.get(i).getTint().getColors()[2]);
    }

    public void fireThruster(int i){
        emitters.get(i).allowCompletion();
        if (i==1) {
            setEmitterVal(emitters.get(i).getAngle(), 180 + rotation.get(), true, true);
            leftThrusterLightTween=Tween.to(flameLights.get(0), 1, 1.5f).waypoint(thrusterOrigDist).target(0).repeatYoyo(0,0).ease(TweenEquations.easeOutQuint).start();
        }
        else if(i==2) {
            setEmitterVal(emitters.get(i).getAngle(), 0 + rotation.get(), true, true);//thrust right
            rightThrusterLightTween=Tween.to(flameLights.get(1), 1, 1.5f).target(0).waypoint(thrusterOrigDist).repeatYoyo(0,0).ease(TweenEquations.easeOutQuint).start();
        }
        additiveEffects.get(i).start();
    }

    public void setBurnerLightTarget(float target, TweenEquation eq) {
        burnerLightTween=Tween.to(flameLights.get(2), 1, 1f).target(target).ease(eq).start();
    }

    public void fastBurner( ) {
        //System.out.println("7, "+emitters.get(0).getEmission().getHighMax());
        System.out.println("8, "+emitters.get(0).getEmission().getHighMax());
        if (emitters.get(0).getEmission().getHighMax() < 2000) {

            setEmitterVal(emitters.get(0).getEmission(), 2000, false, false);
            emitters.get(0).start();
            setBurnerLightTarget(burnerOrigDist*5,TweenEquations.easeOutElastic);
        }
        setEmitterVal(emitters.get(0).getAngle(), 90 - rotation.get() * 10, true, true);
    }

    public void burnerOnOff() {
            if (vel.y >= 0) {   //if moving up
                //System.out.println("1, "+emitters.get(0).getEmission().getHighMax());
                setEmitterVal(emitters.get(0).getAngle(), 90 - rotation.get() * 10, true, true);//always change angle based on arship rot
                setEmitterVal(emitters.get(0).getVelocity(), 80 + vel.y * 15, true, false);//always change vel based on airship vel

                if (vel.y > 1 ) {   //if moving up fastish and burner set to low (might want to leave out last condition)
                    //System.out.println("2, "+emitters.get(0).getEmission().getHighMax());
                    setEmitterVal(emitters.get(0).getEmission(), 300 + vel.y * 750, false, false);
                    setBurnerLightTarget(vel.y*(burnerOrigDist/2f)+burnerOrigDist, TweenEquations.easeOutElastic);
                    emitters.get(0).start();
                } else if (vel.y < 1 && emitters.get(0).getEmission().getHighMax() != 300) {    //if moving slow and burner not set to low, reset
                    setEmitterVal(emitters.get(0).getEmission(), 300, false, false);
                    emitters.get(0).start();
                    setBurnerLightTarget( burnerOrigDist, TweenEquations.easeOutElastic);
                    //System.out.println("3, "+emitters.get(0).getEmission().getHighMax());
                }

            } else if (vel.y < -2.5 && !emitters.get(0).isComplete()) { //if descending let current burner anim finish then turn it off
                emitters.get(0).allowCompletion();
                if (getLightDist("burner")!=0){
                    setBurnerLightTarget( 0, TweenEquations.easeOutCirc);
                }
                //System.out.println("4, "+emitters.get(0).getEmission().getHighMax());
            } else if (vel.y >= -2.5f ) {//if stopped falling go back to flame
                //System.out.println("5, "+emitters.get(0).getEmission().getHighMax());
                if (emitters.get(0).getEmission().getHighMax() != 300)
                    //System.out.println("6, "+emitters.get(0).getEmission().getHighMax());
                    setEmitterVal(emitters.get(0).getEmission(), 300, false, false);
                    emitters.get(0).start();
                    setBurnerLightTarget(burnerOrigDist, TweenEquations.easeOutElastic);
            }
    }

    public int getLightDist(String burnerOrleftThrustOrRightThrust){
        if (burnerOrleftThrustOrRightThrust.equals("burner")){
            return (int) flameLights.get(2).getDistance();
        } else if (burnerOrleftThrustOrRightThrust.equals("leftThrust")) {
            return (int) flameLights.get(0).getDistance();
        } else if (burnerOrleftThrustOrRightThrust.equals("rightThrust")) {
            return (int) flameLights.get(1).getDistance();
        }
        return 0;
    }

    /*public void setLightDist(String burnerOrThrusterLeftOrThrusterRight, int newDist){
        if (burnerOrThrusterLeftOrThrusterRight.equals("burner")){
            if (!BgHandler.isbgVertFast&&newDist>burnerOrigDist*2){
                newDist=burnerOrigDist*2;
            }
            flameLights.get(2).setDistance(newDist); flameLights.get(3).setDistance(newDist);
        } else if (burnerOrThrusterLeftOrThrusterRight.equals("thrusterLeft")) {
            flameLights.get(0).setDistance(newDist);
        } else if (burnerOrThrusterLeftOrThrusterRight.equals("thrusterRight")) {
            flameLights.get(1).setDistance(newDist);
        }
    }*/

    /*public void changeLightAlpha(String burnerOrThruster, float newAlpha){
        Light i=null, j=null;
        if (burnerOrThruster.equals("burner")){
            i = flameLights.get(2); j=flameLights.get(3);
        } else if (burnerOrThruster.equals("thruster")) {
            i = flameLights.get(0); j = flameLights.get(1);
        }
            i.setColor(i.getColor().r,i.getColor().g,i.getColor().b,newAlpha);
            j.setColor(j.getColor().r,j.getColor().g,j.getColor().b,newAlpha);
    }*/

    public void setEmitterVal(ParticleEmitter.ScaledNumericValue val, float newVal, boolean retainHighMinMax, boolean changeLowToo) {
            if (retainHighMinMax) {
                float amplitude = (val.getHighMax() - val.getHighMin()) / 2f;
                float h1 = newVal + amplitude;
                float h2 = newVal - amplitude;
                val.setHigh(h1, h2);
            } else {
                val.setHigh(newVal);
            }
            if (changeLowToo) val.setLow(newVal);
    }

    public void setHitboxRotation(Polygon poly, float rotation){
        //use draw method

    }

    public void update(float delta) {
        //System.out.println("isMovingRightAndSlowing: "+isMovingRightAndSlowing+", velX: "+vel.x);
        setDestAirship();
        //System.out.print(pos.toString());
        setHitboxRotation(rackHitbox,25);
        setHitboxRotation(balloonHitbox,25);
        //System.out.print(BgHandler.isbgVertFast);
        if (BgHandler.isbgVertFast||BgHandler.endWaveBgMotion) {
            fastBurner();
            //System.out.println("very fast");
        } else if (emitters.get(0).getEmission().getHighMax() == 2000){ //if past fastBurning stage, change emission to 300
            setEmitterVal(emitters.get(0).getEmission(), 300, false, false);
            setBurnerLightTarget( burnerOrigDist, TweenEquations.easeOutElastic);
            //System.out.println("not very fast and reset");
        }

        if (burnerLightTween.isStarted()) burnerLightTween.update(delta);
        if (leftThrusterLightTween!=null && !leftThrusterLightTween.isFinished()) leftThrusterLightTween.update(delta);
        if (rightThrusterLightTween!=null && !rightThrusterLightTween.isFinished()) rightThrusterLightTween.update(delta);

        //0 is burner, 1 is thrustLeft, 2 is thrustRight
        if (!tween.isFinished()) { //if moving
            if (!BgHandler.isbgVertFast&&!BgHandler.endWaveBgMotion) {
                burnerOnOff();//if not moving quickly
                //system.out.println("burner change");
            }

            //if (vel.y>-2&&emitters.get(0).isComplete()) emitters.get(0).reset();
            additiveEffects.get(0).setPosition(pos.x, pos.y + 8);

            additiveEffects.get(1).setPosition(pos.x-thrusterWidth/2f+2, pos.y + 0.18f*balloonHeight+thrusterHeight/2f + vel.y);//adding a bit of vel for straying thrusters
            additiveEffects.get(2).setPosition(pos.x+thrusterWidth/2f-2, pos.y + 0.18f*balloonHeight+thrusterHeight/2f + vel.y);

            preX=pos.x;
            preY=pos.y;
            tween.update(delta);
            rotationTween.update(delta);
            vel.set(pos.x-preX, pos.y-preY);

            /*if ((vel.x<=0&&tween.getTargetValues()[0]-pos.x<0)||(vel.x>=0&&tween.getTargetValues()[0]-pos.x>0)){
                rotation.get() += -Math.signum(vel.x)*((Math.abs(vel.x*2)-Math.abs(rotation.get()))/1.5f);
            }*/

            //System.out.println("Velocity change, vel: "+vel.x+", preVel: "+(tween.getTargetValues()[0]-pos.x));
            float temp = vel.x/(2f*(speedDivisor/60f));
            //-Math.signum(vel.x)*(temp*temp); //exponent of 2 //(float) (-Math.signum(xVel)*Math.pow(Math.abs(xVel),1.5));//-xVel*2f;


            for (Turret i : turretList) {
                i.update();
                i.position.set(pos.x - (startX - i.origPosition.x), pos.y - (startY - i.origPosition.y));
            }
            for (Light i: flameLights){
                if (i instanceof CustomPointLight)  i.setPosition(pos.x - (startX - ((CustomPointLight) i).origPos.x), pos.y - (startY - ((CustomPointLight) i).origPos.y));
                //else if (i instanceof ChainLight)        {i.setPosition(pos.x - (startX - ((CustomChainLight) i).origPos.x), pos.x - (startX - ((CustomChainLight) i).origPos.y))}
                else                                     i.setPosition(pos.x - (startX - ((CustomConeLight) i).origPos.x), pos.y - (startY - ((CustomConeLight) i).origPos.y));
            }

            //System.out.println("posLeft: "+flameLights.get(0).getDistance()+"posRight: "+flameLights.get(1).getDistance());

            rackHitbox.setPosition(pos.x - startX, pos.y - startY);
            balloonHitbox.setPosition(pos.x - startX, pos.y - startY);
            //checkBordersAndSlowdown(); not using velocity
        } else {
            for (Turret i : turretList) {
                i.update();
            }
        }
        /*if (!isTouched) {
            //System.out.println("not touched");
            checkBordersAndSlowdown();
            pos.add(vel.cpy().scl(delta));
            rackHitbox.translate(vel.cpy().scl(delta).x, vel.cpy().scl(delta).y);
            balloonHitbox.translate(vel.cpy().scl(delta).x, vel.cpy().scl(delta).y);
            for (Turret i : turretList){
                i.position.add(vel.cpy().scl(delta));
                i.update();
            }
        } else {
            for (Turret i : turretList) {
                i.position.set(pos.x - (startX - i.origPosition.x), pos.y - (startY - i.origPosition.y));
                i.update();
            }

            rackHitbox.setPosition(pos.x - startX, pos.y - startY);
            balloonHitbox.setPosition(pos.x - startX, pos.y - startY);
            checkBordersAndSlowdown();
        }*/
    }

    public void draw(SpriteBatch batcher, float delta) {
        //Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        //burnerFire.setEmittersCleanUpBlendFunction(false);//can use this to make tall textures ghostly, see what blending function actually enables that

        additiveEffects.get(0).draw(batcher, delta);
/*if (!hitMaxBrightnessCloudBrightening) { //if still getting brighter
                    System.out.println("Getting brighter");
                    airShipCloudTint[0] += (255 - airShipCloudTint[0]) / 10f;
                    airShipCloudTint[1] += (255 - airShipCloudTint[1]) / 10f;
                    airShipCloudTint[2] += (255 - airShipCloudTint[2]) / 10f;
                } else if (airShipCloudTint[0] > airshipTint[0]) {   //if past max point and getting darker and brighter than original
                    System.out.println("Getting darker");
                    airShipCloudTint[0] -= (airShipCloudTint[0]-airshipTint[0]) / 10f;
                    airShipCloudTint[1] -= (airShipCloudTint[1]-airshipTint[1]) / 10f;
                    airShipCloudTint[2] -= (airShipCloudTint[2]-airshipTint[2]) / 10f;
                }
                */

        if (BgHandler.isbgVertFast) {
            if (!BgHandler.isMiddleOfCloud) {
                if (!hitMaxBrightnessCloudBrightening) { //if still getting brighter
                    System.out.println("Getting brighter");
                    airShipCloudTint[0] += (255 - airShipCloudTint[0]) / 250f;
                    airShipCloudTint[1] += (255 - airShipCloudTint[1]) / 250f;
                    airShipCloudTint[2] += (255 - airShipCloudTint[2]) / 250f;
                } else if (airShipCloudTint[0] > airshipTint[0]) {   //if past max point and getting darker and brighter than original
                    System.out.println("Getting darker");
                    airShipCloudTint[0] -= (airShipCloudTint[0]-airshipTint[0]) / 7f;
                    airShipCloudTint[1] -= (airShipCloudTint[1]-airshipTint[1]) / 7f;
                    airShipCloudTint[2] -= (airShipCloudTint[2]-airshipTint[2]) / 7f;
                }
                //if (airShipCloudTint[0] > 255)
                batcher.setColor(airShipCloudTint[0] / 255f, airShipCloudTint[1] / 255f, airShipCloudTint[2] / 255f, 1);
            } else {    //if is moving fast and in the middle of the cloud
                if (!hitMaxBrightnessCloudBrightening) {//so we only do this block once
                    hitMaxBrightnessCloudBrightening=true;
                    System.out.println("hit middle of cloud");
                    if ((BgHandler.bgNumber-1)%9==0) {    //if changing waves, change colors
                        System.out.println("Change colors");
                        //System.out.println("Bgnumber: "+(bgNumber-2)+", ");
                        setFireColor((BgHandler.bgNumber-1)/9);
                        airshipTint=Airship.chooseColorBasedOnWave((BgHandler.bgNumber-1)/9, true);
                    }
                }
            }
        } else {
            if (hitMaxBrightnessCloudBrightening){ //if stopped going fast and had hit max brightness
                //System.out.println("Not going fast, isFast= "+BgHandler.isbgVertFast);
                hitMaxBrightnessCloudBrightening=false;
            }
            if (airShipCloudTint[0]!=airshipTint[0]){airShipCloudTint=airshipTint.clone();
            System.out.println("airship cloud tint");
                }
            batcher.setColor(airshipTint[0] / 255f, airshipTint[1] / 255f, airshipTint[2] / 255f, 1);
        }

        batcher.draw(balloonTexture, pos.x-(balloonWidth)/2f, pos.y,
                balloonWidth/2f, 0, balloonWidth, balloonHeight, 1, 1, rotation.get());
        batcher.setColor(Color.WHITE);

        if (!additiveEffects.get(1).isComplete()) {
            additiveEffects.get(1).draw(batcher, delta);
        }
        if (!additiveEffects.get(2).isComplete()){
            additiveEffects.get(2).draw(batcher, delta);
        }

        //for (int i=0;i<sideThrustLvl+1;i++){ //starting at bottom of balloon, draw different number of thrusters
        batcher.draw(sideThrustTexture, pos.x-thrusterWidth/2f, pos.y+ 0.18f*balloonHeight ,//+ (thrusterHeight)*i
                 thrusterWidth/2f, -0.18f*balloonHeight, thrusterWidth, thrusterHeight, 1, 1, rotation.get());
        //}

        batcher.draw(rackTexture, pos.x-rackWidth/2f, pos.y-rackHeight,
                rackWidth/2f, rackHeight/2f, rackWidth, rackHeight,1,1,rotation.get());

        batcher.draw(armorTexture, pos.x-armorTexture.getRegionWidth()/2f, pos.y-rackHeight-armorTexture.getRegionHeight(),//+ (thrusterHeight)*i
                armorWidth/2, armorTexture.getRegionHeight()/2,
                armorWidth, armorHeight, 1, 1, rotation.get());

        for (Turret i : turretList) {
            if (rotation.get() < 0) {//right movement, need to rotate less?
                    xOffsetFromRotation =  -rotation.get() * (startX - i.origPosition.x)   / (140f);//divided by x distance from pos.x so effect less pronounced when close to center
                    yOffsetFromRotation =  -rotation.get() * (startY - i.origPosition.y)   / (65f);
            } else {        //left movement
                    xOffsetFromRotation =   rotation.get() * (startX - i.origPosition.x)   / (140f);
                    yOffsetFromRotation =  -rotation.get() * (startY - i.origPosition.y)   / (65f);
            }
            if (startX - i.origPosition.x<0) {//if on right side of airship we need to invert movements
                yOffsetFromRotation=-yOffsetFromRotation/1.1f;
            } else if (Math.abs(startX-i.origPosition.x) < 2) {xOffsetFromRotation=0;yOffsetFromRotation=0;}

            batcher.draw(i.texture, i.position.x-i.width/2f + xOffsetFromRotation, i.position.y-i.height/2f + yOffsetFromRotation,
                    i.width/2f, i.height/2f, i.width, i.height, 1f, 1f, i.getRotation());
        }
    }

    public void hit(int collisionDmg) {
        health-=collisionDmg;
        isFlashing = true;
        flashOpacityValue.set(1f);//always start from white flash to distinguish from bg
        if (collisionDmg<origHealth&&health>0){
            currentFlashLength=flashLengths.get((collisionDmg/origHealth)*flashLengths.size());
            flashTween = Tween.to(flashOpacityValue, -1, currentFlashLength).target(0f).ease(TweenEquations.easeOutExpo).setCallback(endFlashing).start();
        } else {
            //currentFlashLength=flashLengths.get(flashLengths.size()-1); //else make flash black (-1f-0f)
            flashOpacityValue.set(1f);    //make a death shader effect

            //.push(Tween.to(flashOpacityValue, -1, 0.3f).target(1f).ease(TweenEquations.easeOutExpo))
            flashTween = Tween.to(flashOpacityValue, -1, 2f).target(-1f).ease(TweenEquations.easeOutExpo).setCallback(endFlashing).start();
        }
        //System.out.println(currentFlashLength);
    }

}