package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.TweenedAnimation;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Helpers.InvertedTweenEquations;
import com.kredatus.flockblockers.TweenAccessors.Value;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;

public class Airship {  //engines, sideThrusters, armors and health are organized as lvl1-lvl5

    private Circle boundingCir;
    public static Vector2 pos; //lastTouchVel=new Vector2(), acc, dest, lastDest, differenceVector;
    //public boolean was
    public float gamexvel;
    public static int balloonWidth, balloonHeight, rackWidth, rackHeight, thrusterWidth, thrusterHeight, height; //x and y are at middle of textures, bottom of balloonTexture,top of rack
    protected boolean isScrolledDown;
    public float midpointY, midpointX, startY,startX;
    private boolean isAlive;
    private ArrayList<Vector2> positions = new ArrayList<Vector2>(16);

    public static int armor=100, health=100, origHealth=health; //slowdownSpeed;

    public static int lvl, engineTuning, armorLvl, sideThrustLvl;   //0-4
    public static TextureRegion balloonTexture, rackTexture, sideThrustTexture;    //balloonTexture is top part of hot air balloon, rack is bottom

    //positions 28,31    82,31  110-136 and 137-163

    public ArrayList<Turret> turretList=new ArrayList<Turret>(13);
    public Polygon rackHitbox, balloonHitbox, prelimBoundPoly1, prelimBoundPoly2;

    public int tW=32, tH=33;
    public static int airshipTouchPointer=-1, camWidth, camHeight;
    public float fingerAirshipXDiff, fingerAirshipYDiff;
    //public static boolean justTouched;

    public float currentFlashLength;// dragSpeed;
    public boolean isFlashing;
    public Value flashOpacityValue = new Value();
    public Tween flashTween;
    public TweenCallback endFlashing;
    protected ArrayList<Float> flashLengths=new ArrayList<Float>();

    public Tween tween;
    float inputX, inputY, speedDivisor;
    public static boolean justTouched;

    private static float preX, xVel, rotation;
    float xOffsetFromRotation,yOffsetFromRotation;

    public Airship(int camWidth, int camHeight) {
        this.camWidth=camWidth;
        this.camHeight=camHeight;
        armorLvl=0;
        lvl=2;
        sideThrustLvl=0;

        assignTextures(armorLvl,lvl);
        height=balloonHeight+rackHeight;
        startY=-height;
        startX=-balloonWidth;
        pos=new Vector2(startX, startY);
        //vel=new Vector2(0,50);
        assignBounds();

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

        tween=Tween.to(pos,0,3).target(camWidth-balloonWidth,camHeight-height).ease(TweenEquations.easeOutCirc).delay(2).start();
        speedDivisor=90f;//60, 75, 90, 105, 120  higher the faster
    }

    private void assignBounds(){
        float x =pos.x, y=pos.y-2, rB=balloonWidth/2f, hB=balloonHeight;

                prelimBoundPoly2= new Polygon(new float[]{x - balloonWidth/2f,y,   x - balloonWidth/2f,y+hB,    x + balloonWidth/2f,y+hB,    x + balloonWidth/2f,y});//left side
                prelimBoundPoly1= new Polygon(new float[]{x - rackWidth/2f,y,   x - rackWidth/2f,y - rackHeight,   x + rackWidth/2f,y - rackHeight,  x + rackWidth/2f,y});

                balloonHitbox = new Polygon(new float[]{
                        x, y +hB,     x - rB * 0.60f, y + hB * 0.92f,       x - rB * 0.98f, y + hB * 0.67f,          x - rB * 0.90f, y + hB * 0.37f,      x - rB * 0.40f, y,  //top to bottom left of burner
                        x + rB * 0.40f, y,        x + rB * 0.90f, y + hB * 0.37f,    x + rB * 0.98f, y + hB * 0.67f,          x + rB * 0.60f, y + hB * 0.92f //to top of balloon
                });

                if (lvl==0){
                rackHitbox = new Polygon(new float[]{
                        x - tW * 2, y ,         x - tW * 2, y - 1 * tH ,//bottom left rack
                        x + tW * 2, y - 1 * tH ,     x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==1){
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,//bottom left rack
                            x + tW * 2, y - 2 * tH ,   x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==2) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,     x - tW * 1.5f, y - 3 * tH ,//bottom left rack
                            x + tW * 1.5f, y - 3 * tH ,        x + tW * 2, y - 2 * tH ,    x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==3) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,     x - tW * 1.5f, y - 4 * tH ,//bottom left rack
                            x + tW * 1.5f, y - 4 * tH ,        x + tW * 2, y - 2 * tH ,    x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                } else if (lvl==4) {
                    rackHitbox = new Polygon(new float[]{
                            x - tW * 2, y ,     x - tW * 2, y - 2 * tH ,     x - tW * 1.5f, y - 4 * tH ,      x - tW * 1f, y - 5 * tH ,//bottom left rack
                            x + tW * 1f, y - 5 * tH , x + tW * 1.5f, y - 4 * tH , x + tW * 2, y - 2 * tH , x + tW * 2, y ,     //bottom right of burner
                        }
                    );
                }

        rackHitbox.setRotation(rotation);
        balloonHitbox.setRotation(rotation);
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
        balloonTexture=AssetHandler.airshipBalloon;rackTexture=AssetHandler.airshipRack(armorLvl,lvl+1, tH);sideThrustTexture=AssetHandler.airshipSideThruster;
        balloonWidth=(int) ((balloonTexture.getRegionWidth())*(1+0.2f*lvl)); balloonHeight=balloonTexture.getRegionHeight();
        thrusterWidth=sideThrustTexture.getRegionWidth(); thrusterHeight=(int) (sideThrustTexture.getRegionHeight()*(1+0.2f*sideThrustLvl));
        rackWidth=rackTexture.getRegionWidth(); rackHeight=rackTexture.getRegionHeight();
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

    private int getAirshipTouchPointer(){
        for (int i = 0; i < 2; i++) {
            if (pointerOnAirship(i)) {return i;}
        }
        return -1;
    }
    private void setDestAirship(){
        if (justTouched)justTouched=false;
        if (airshipTouchPointer==-1 && Gdx.input.justTouched()) {//if new press and not pressed before
            //System.out.println(InputHandler.scaleX(Gdx.input.getX())+ " *** "+  InputHandler.scaleY(Gdx.input.getY()) );
            airshipTouchPointer=getAirshipTouchPointer();
            if (airshipTouchPointer>=0){
                //System.out.println("AIRSHIP POINTER TOUCHED");
                justTouched=true;
                inputX=InputHandler.scaleX(Gdx.input.getX(airshipTouchPointer));inputY= -(InputHandler.scaleY(Gdx.input.getY(airshipTouchPointer))-camHeight);
                fingerAirshipXDiff=inputX-pos.x;fingerAirshipYDiff=inputY-pos.y;//fingerAirshipDiff doesnt change while finger is pressed which is why we get it once here
        }
        } else if (airshipTouchPointer>=0 && Gdx.input.isTouched(airshipTouchPointer) &&
                (     Math.abs((inputX+fingerAirshipXDiff)-InputHandler.scaleX(Gdx.input.getX(airshipTouchPointer))  ) >0
                    ||Math.abs((inputY+fingerAirshipYDiff)+(InputHandler.scaleY(Gdx.input.getY(airshipTouchPointer))-camHeight)  ) >0     )     ) { //if (after first press) and (airship was pressed) and (airship currently pressed)
            //System.out.println("AIRSHIP POINTER MOVED");
            inputX = InputHandler.scaleX(Gdx.input.getX(airshipTouchPointer)) - fingerAirshipXDiff ;//input with finger touch difference
            inputY = -(InputHandler.scaleY(Gdx.input.getY(airshipTouchPointer))-camHeight) - fingerAirshipYDiff ;
            if (isOnCam(inputX, inputY)) {
                        tween = Tween.to(pos, 0, (float) (Math.sqrt(Math.pow(Math.abs(pos.x-inputX),2)+Math.pow(Math.abs(pos.y-inputY),2)))/speedDivisor ).target(inputX, inputY).ease(TweenEquations.easeOutQuint).start();
            }
            //if (inputX > balloonWidth/3f   &&  inputX<camWidth-balloonWidth/3f)pos.x=inputX;
            //if (inputY > rackHeight/3f  &&   inputY<camHeight-balloonHeight/4f) pos.y=inputY;
            //} else if (airshipTouchPointer>=0) {//if (airship pointer not pressed and pointer not reset)

        } else if (airshipTouchPointer>=0&&!Gdx.input.isTouched(airshipTouchPointer)) {
            //System.out.println("AIRSHIP POINTER UNTOUCHED");
            //vel.set(Gdx.input.getDeltaX(airshipTouchPointer)*30,-(Gdx.input.getDeltaY(airshipTouchPointer))*30);
            airshipTouchPointer=-1;
        }
    }

    public void update(float delta) {
        setDestAirship();
        //System.out.print(pos.toString());
        rackHitbox.setRotation(rotation);
        balloonHitbox.setRotation(rotation);

        if (!tween.isFinished()) {
            //System.out.println("update tween");
            preX=pos.x;
            tween.update(delta);
            xVel= pos.x-preX;
            float temp = xVel/(2f*(speedDivisor/60f));
            rotation= -Math.signum(xVel)*(temp*temp); //exponent of 2 //(float) (-Math.signum(xVel)*Math.pow(Math.abs(xVel),1.5));//-xVel*2f;

            for (Turret i : turretList) {
                i.update();
                i.position.set(pos.x - (startX - i.origPosition.x), pos.y - (startY - i.origPosition.y));
            }

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

    public void draw(SpriteBatch batcher) {
        batcher.draw(balloonTexture, pos.x-(balloonWidth)/2f, pos.y,
                balloonWidth/2f, 0, balloonWidth, balloonHeight, 1, 1, rotation);

        //for (int i=0;i<sideThrustLvl+1;i++){ //starting at bottom of balloon, draw different number of thrusters
            batcher.draw(sideThrustTexture, pos.x-thrusterWidth/2f, pos.y+ 0.18f*balloonHeight ,//+ (thrusterHeight)*i
                    thrusterWidth/2f, -0.18f*balloonHeight, thrusterWidth, thrusterHeight, 1, 1, rotation);
        //}

        batcher.draw(rackTexture, pos.x-rackWidth/2f, pos.y-rackHeight,
                rackWidth/2f, rackHeight/2f, rackWidth, rackHeight,1,1,rotation);

        System.out.println("Further out turrets position: "+5 * ((startX - turretList.get(0).origPosition.x) / 50f));
        System.out.println("Closer turrets position: "+5 * ((startX - turretList.get(1).origPosition.x) / 50f));
        for (Turret i : turretList) {

            if (rotation < 0) {//right movement, need to rotate less
                    xOffsetFromRotation = 3 * -rotation / (5 * ((startX - i.origPosition.x) / 50f));
                    yOffsetFromRotation = 2 * -rotation / (5 * ((startY - i.origPosition.y) / 50f));//divided by x distance from pos.x so effect less pronounced when close to center
            } else {        //left movement
                    xOffsetFromRotation = 2 * rotation / (5 * ((startX - i.origPosition.x) / 50f));
                    yOffsetFromRotation = -rotation / (5 * ((startY - i.origPosition.y) / 50f));//
            }
            if (startX - i.origPosition.x<0) {//if on right side of airship we need to invert movements
                xOffsetFromRotation=(-xOffsetFromRotation +1)/1.1f;yOffsetFromRotation=(-yOffsetFromRotation +2)/1.1f;
            }

            batcher.draw(i.texture, i.position.x-i.width/2f + xOffsetFromRotation, i.position.y-i.height/2f + yOffsetFromRotation,
                    i.width/2f, i.height/2f, i.width, i.height, 1f, 1f, i.getRotation());
        }
    }

    public void hit(int collisionDmg) {
        health-=collisionDmg;
        isFlashing = true;
        flashOpacityValue.setValue(1f);//always start from white flash to distinguish from bg
        if (collisionDmg<origHealth&&health>0){
            currentFlashLength=flashLengths.get((collisionDmg/origHealth)*flashLengths.size());
            flashTween = Tween.to(flashOpacityValue, -1, currentFlashLength).target(0f).ease(TweenEquations.easeOutExpo).setCallback(endFlashing).start();
        } else {
            //currentFlashLength=flashLengths.get(flashLengths.size()-1); //else make flash black (-1f-0f)
            flashOpacityValue.setValue(1f);    //make a death shader effect

            //.push(Tween.to(flashOpacityValue, -1, 0.3f).target(1f).ease(TweenEquations.easeOutExpo))
            flashTween = Tween.to(flashOpacityValue, -1, 2f).target(-1f).ease(TweenEquations.easeOutExpo).setCallback(endFlashing).start();
        }
        //System.out.println(currentFlashLength);
    }
}