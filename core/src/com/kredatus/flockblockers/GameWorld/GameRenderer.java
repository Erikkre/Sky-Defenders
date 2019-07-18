// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.GameObjects.Background;
import com.kredatus.flockblockers.GameObjects.Projectile;
import com.kredatus.flockblockers.GameObjects.Resources.MovingImageContainer;
import com.kredatus.flockblockers.GameObjects.TinyBird;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.LightHandler;
import com.kredatus.flockblockers.Handlers.TargetHandler;
import com.kredatus.flockblockers.Handlers.TinyBirdHandler;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.Helpers.CustomShapeRenderer;
import com.kredatus.flockblockers.Screens.Loader;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.ui.SimpleButton;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

//import com.kredatus.flockblockers.GameObjects.Boost;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */

public class GameRenderer {

    // Tween stuff
    public TweenManager manager;//, sunshineManager, sunshineManager2;
    private Value alpha = new Value(), beta = new Value(), charlie=new Value(), delta = new Value();

    //Buttons
    private List<SimpleButton> menuButtons;
    private List<SimpleButton> deathButtons;
    private SimpleButton readyButton, menuButton, nextButton;
    private GameWorld world;
    public OrthographicCamera cam;
    public static ExtendViewport viewport;
    private ShapeRenderer shapeRenderer; private CustomShapeRenderer shapeRendererCust;
    boolean turnback=true;
    public static SpriteBatch batcher;
    private Airship airship;
    public static Vector3 campos;
    private Animation frontFlaps, leftSideFlaps, rightSideFlaps, flipflaps, frontViewFlaps, backFlaps;
    //private TextureRegion gliderMid, vertflipgliderMid;

    private TextureRegion
            creditsbg, deathmenubg, newHighscore, topscore, deathmenuscore, rating, youvedied, boostdown,
            gliderbg, instrbg, readybg, frontglidermid, worldStabilized, gun, projectile, reticle;

    private Background background, background2, background3, background4;
    //public ArrayList<Boost> boostlist, flipboostlist, invboostlist, invflipboostlist;

    float rotate = 0, previousvalue, deadBirdScale=1;
    int scorenumber = 0;
    //public static boolean splashdown = false;
    public static FreeTypeFontGenerator.FreeTypeFontParameter smallfont, bigfont, xsmallfont, xxsmallfont;

    public static FreeTypeFontGenerator gamefont, otherfont;
    public static BitmapFont font, titlefont, storyfont, instrfont, droidSerifFont;


    public static ConcurrentLinkedQueue<BirdAbstractClass> activeBirdQueue, deadBirdQueue;
    public static ConcurrentLinkedQueue<TinyBird> tinyBirdQueue;
    //public ArrayList<Turret> turretList;
    public ConcurrentLinkedQueue<Projectile> projectileList;
    private float highScorelen, len, endgamelen, tryAgainlen, boostTextLen, scorelen, startLevellen, titlelen,
            c0len, c1len, c2len, c3len, c4len, c5len, c6len, c7len, c8len, c9len, c10len, c11len, c12len, c13len, c14len,
            s0len, s1len, s2len, s3len, s4len, s5len, s6len, s7len, s8len, s9len, s10len, s11len, s12len, s13len, s14len;
    private GlyphLayout Highscore, endgame, tryAgain,  boost, score, startLevel, title,
            c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14,
            s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14;

    private Color transitionColor;
    private int camWidth, camHeight;
    private BgHandler bgHandler;
    private BirdHandler birdHandler;
    private TinyBirdHandler tinyBirdHandler;
    private TargetHandler targetHandler;
    //private TurretHandler turretHandler;
    private UiHandler uiHandler;
    private LightHandler lightHandler;
    //Table table;

    private ShaderProgram flashShader;


    public GameRenderer(GameWorld world,LightHandler lightHandler, TinyBirdHandler tinyBirdHandler, BirdHandler birdHandler, BgHandler bgHandler, TargetHandler targetHandler, UiHandler uiHandler, Airship airship, int camWidth, int camHeight) {
        this.world = world;
        this.lightHandler=lightHandler;
        this.tinyBirdHandler=tinyBirdHandler;
        this.birdHandler=birdHandler;
        this.bgHandler=bgHandler;
        this.targetHandler = targetHandler;
        //this.turretHandler=turretHandler;
        this.uiHandler=uiHandler;
        this.airship = airship;
        this.camWidth=camWidth;
        this.camHeight=camHeight;


        batcher = new SpriteBatch();
        //System.out.println("batcher color: "+batcher.getColor()+", white: "+new Color(1,1,1,1));

        cam=(OrthographicCamera)((FlockBlockersMain)Gdx.app.getApplicationListener()).loader.stage.getCamera();
        batcher.setProjectionMatrix(cam.combined);

        System.out.println("Height: "+camHeight);
        //cam.position.set(new Vector3(0,0,0));
        //cam.position.x=9*camWidth/10;  //seems random but is 1/2(glider position in camWidth) + 2/5
        //cam.update();
        //viewport = new FitViewport(camWidth, viewHeight, cam);
        //viewport.apply();


        shapeRendererCust= new CustomShapeRenderer();
        shapeRendererCust.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        // Attach batcher to camera
        // Call helper methods to initialize instance variables
        initGameObjects();
        initAssets();
        //survivalBgTweens();
        bigfont = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallfont = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //smallfontLengthSetup();
        //bigfontLengthSetup();
        //storyFontSetup();
        //instrFontSetup();
        //creditsFontSetup();
        transitionColor = new Color();
        //makeTransition(255, 255, 255, .5f);
        manager = new TweenManager();
        //prepareSunshine();
        //System.out.println(batcher.getBlendDstFunc()+" "+batcher.getBlendDstFuncAlpha()+" "+batcher.getBlendSrcFunc()+" "+batcher.getBlendSrcFuncAlpha()+" "+batcher.getPackedColor());
    }

    public void assignButtonsUsingInputHandlerAndUiHandler(InputHandler inputHandler,UiHandler uiHandler){
        this.menuButtons = inputHandler.getMenuButtons();
        this.deathButtons = inputHandler.getDeathButtons();

        this.readyButton = inputHandler.getReadyButton();
        this.menuButton = inputHandler.getMenuButton();
        this.nextButton =  inputHandler.getNextButton();
        this.uiHandler=uiHandler;
    }
    /*private void survivalBgTweens() {
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(alpha, -1, 0).target(0).ease(TweenEquations.easeOutQuad)
                .start(manager);
    }*/

public void setRotate(float angle){
    cam.rotate(angle);
    cam.update();
    batcher.setProjectionMatrix(cam.combined);
}
    private void initAssets() {

        reticle=((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("reticle");

        //gliderMid = AssetHandler.gliderMid;
        //vertflipgliderMid = AssetHandler.vertflipgliderMid;

        activeBirdQueue=birdHandler.activeBirdQueue;
        deadBirdQueue=  birdHandler.deadBirdQueue;
        tinyBirdQueue= tinyBirdHandler.tinyBirdQueue;

        projectileList= targetHandler.projectileList;

        flashShader = ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.manager.get(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.assets.flashShader);
    }

    private void initGameObjects() {

        background = bgHandler.getBackground();
        background2 = bgHandler.getBackground2();
        //background3 = bgHandler.getBackground3();
        //background4 = bgHandler.getBackground4();
/*
        boostlist = bgHandler.getboostlist();
        flipboostlist = bgHandler.getflipboostlist();
        invboostlist = bgHandler.getinvboostlist();
        invflipboostlist = bgHandler.getinvflipboostlist();
*/


        // table=uiHandler.table;
    }

    private void smallfontLengthSetup() {
        smallfont.size = 32;
        smallfont.minFilter = Texture.TextureFilter.Linear;
        smallfont.magFilter = Texture.TextureFilter.MipMapNearestLinear;
        //smallfont.characters = "'0123456789.:?!,-%ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; //score and boost
        smallfont.borderWidth = 2;
        smallfont.color = Color.WHITE;
        smallfont.borderColor = Color.BLACK;
        smallfont.shadowColor = Color.BLACK;
        //smallfont.shadowOffsetX = 4;
        //smallfont.shadowOffsetY = 2;
        gamefont = new FreeTypeFontGenerator(Gdx.files.internal("font/blowbrush.ttf"));
        otherfont = new FreeTypeFontGenerator(Gdx.files.internal("font/DroidSerif-Bold.ttf"));
        droidSerifFont= otherfont.generateFont(smallfont);
        droidSerifFont.setUseIntegerPositions(false);
        droidSerifFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = gamefont.generateFont(smallfont);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);

        boost = new GlyphLayout(font, "Boost: 99");
        boostTextLen = boost.width;

        score = new GlyphLayout(font, "Gold: 100");
        scorelen = score.width;
    }

    private void bigfontLengthSetup() {
        bigfont = smallfont;
        bigfont.size = 168;
        bigfont.borderWidth = 3;
        bigfont.color = Color.ORANGE;
        //bigfont.borderColor=Color.BLACK;
        bigfont.shadowColor = Color.BLACK;
        bigfont.shadowOffsetX = 10;
        bigfont.shadowOffsetY = 14;
        bigfont.borderStraight = false;
        titlefont = gamefont.generateFont(bigfont);
        title = new GlyphLayout(titlefont, "Cloud Defenders");
        titlelen = title.width;
    }

    private void storyFontSetup() {
        xsmallfont = smallfont;
        xsmallfont.size = 28;
        storyfont = gamefont.generateFont(xsmallfont);

        s0 = new GlyphLayout(storyfont, "Stories are told of a hero that only appears in times of great chaos,");
        s0len = s0.width;
    }

    private void instrFontSetup() {
        xxsmallfont = smallfont;
        xxsmallfont.size = 26;
        instrfont = gamefont.generateFont(xxsmallfont);
        c0 = new GlyphLayout(instrfont, "1. TAP THE SCREEN TO MAKE THE PHOENIX FLY AWAY FROM THE CLOSEST GROUND");
        c0len = c0.width;
    }

    public void drawInstr() {
        instrfont.draw(batcher, "1. TAP THE SCREEN TO MAKE THE PHOENIX FLY AWAY FROM THE CLOSEST GROUND", cam.position.x - c0len / 2, camHeight / 11);
    }

    private void drawBackground() {
    batcher.disableBlending();
        batcher.draw(background.getTexture(), background.getX(), background.getY(),
                background.getWidth(), background.getHeight());

        batcher.draw(background2.getTexture(), background2.getX(), background2.getY(),
               background2.getWidth(), background2.getHeight());
        batcher.enableBlending();

    }

    public void drawCreditsbg() {
        if ((float)creditsbg.getRegionHeight()/(creditsbg.getRegionWidth())<camHeight/camWidth){
            batcher.draw(creditsbg, 0, 5, camWidth, camWidth * ((float)creditsbg.getRegionHeight() / creditsbg.getRegionWidth()));
        } else {
            batcher.draw(creditsbg, cam.position.x- (camHeight*((float)creditsbg.getRegionWidth() / creditsbg.getRegionHeight()))/2, 5, camHeight*((float)creditsbg.getRegionWidth() / creditsbg.getRegionHeight()), camHeight-80);}
    }

    public void drawInstrbg() {
        if ((float)instrbg.getRegionHeight()/(instrbg.getRegionWidth())<camHeight/camWidth){
            batcher.draw(instrbg, 0, 0, camWidth, camWidth * ((float)instrbg.getRegionHeight() / instrbg.getRegionWidth()));
        } else {
            batcher.draw(instrbg, cam.position.x- (camHeight*((float)instrbg.getRegionWidth() / instrbg.getRegionHeight()))/2, 0, camHeight*((float)instrbg.getRegionWidth() / instrbg.getRegionHeight()), camHeight-50);}
    }

/*
    public void prepareSunshine(){
        beta.set(camWidth*1.6f); //start val
        Tween.registerAccessor(Value.class, new ValueAccessor());
        sunshineManager = new TweenManager();
        Tween.to(beta, -1, 6f).target(0)    //end val
                .ease(TweenEquations.easeOutQuad).repeat(Tween.INFINITY,5f).start(sunshineManager);

        sunshineManager2= new TweenManager();
        charlie.set(AssetHandler.frontGliderMid.getRegionWidth()/2);
        Tween.to(charlie, -1, 10).target(0)    //end val
                .ease(TweenEquations.easeInOutExpo).repeatYoyo(Tween.INFINITY,0).start(sunshineManager2);
        delta.set(AssetHandler.frontGliderMid.getRegionHeight()/2);
        Tween.to(delta, -1, 10).target(0)    //end val
                .ease(TweenEquations.easeInOutExpo).repeatYoyo(Tween.INFINITY,0).start(sunshineManager2);
    }
*/

    private void drawMenuUI() {
        titlefont.draw(batcher, "Cloud Defenders", cam.position.x - titlelen/2, 10);
        titlefont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        for (SimpleButton button : menuButtons) {
            button.draw(batcher);
        }
    }

    private void drawSpritesDeathMenu(float runTime) {
        rotate += 1;

        /*if (charlie.get()<previousvalue && turnback==false && charlie.get()<0.85*AssetHandler.frontGliderMid.getRegionWidth()/2){   //if bird going away and 0.2 of the way there and facing towards, face away
            turnback=true;
        } else if (charlie.get()> previousvalue && turnback==true){   //if bird coming and facing away, face towards
            turnback=false;
        }
        if (!turnback){
            batcher.draw(gliderbg, cam.position.x-charlie.get()*1.5f, glider.getPosition().y- 40  - charlie.get()*1.5f, charlie.get()*1.5f, charlie.get()*1.5f,
                    charlie.get()*3, charlie.get()*3, 1, 1, rotate);
            frontTexture = (TextureRegion) frontViewFlaps.getKeyFrame(runTime+0.5f);
            batcher.draw(frontTexture, cam.position.x-charlie.get()* ((float)frontTexture.getRegionWidth()/frontglidermid.getRegionWidth()), glider.getPosition().y - 40 -delta.get()* ((float)frontTexture.getRegionHeight()/frontglidermid.getRegionHeight()),
                    charlie.get()*2*((float)frontTexture.getRegionWidth()/frontglidermid.getRegionWidth()), delta.get()*2* ((float)frontTexture.getRegionHeight()/frontglidermid.getRegionHeight()));
        } else {
            frontTexture = (TextureRegion) backFlaps.getKeyFrame(runTime+0.5f);
            batcher.draw(frontTexture, cam.position.x-charlie.get()* ((float)frontTexture.getRegionWidth()/frontglidermid.getRegionWidth()), glider.getPosition().y - 40 -delta.get()* ((float)frontTexture.getRegionHeight()/frontglidermid.getRegionHeight()),
                    charlie.get()*2*((float)frontTexture.getRegionWidth()/frontglidermid.getRegionWidth()), delta.get()*2* ((float)frontTexture.getRegionHeight()/frontglidermid.getRegionHeight()));
            batcher.draw(gliderbg, cam.position.x-charlie.get()*1.5f, glider.getPosition().y- 40  - charlie.get()*1.5f, charlie.get()*1.5f, charlie.get()*1.5f,
                    charlie.get()*3, charlie.get()*3, 1, 1, rotate);
        }
        previousvalue=charlie.get();*/
    }

    private void drawSpritesMenu(float runTime) {


    }

    private void drawDeathMenu() {
        for (SimpleButton button : deathButtons) {
            button.draw(batcher);}
        batcher.draw(deathmenubg, cam.position.x - deathmenubg.getRegionWidth() / 2 + 5, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight());
        batcher.draw(deathmenuscore, cam.position.x + deathmenubg.getRegionWidth() / 3 - topscore.getRegionWidth() / 4 + 70,camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+5,
                deathmenuscore.getRegionWidth() / 2, deathmenuscore.getRegionHeight() / 2);
        font.draw(batcher, "" + scorenumber, cam.position.x + deathmenubg.getRegionWidth() / 3 - topscore.getRegionWidth() / 4 + 40, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+90);
        batcher.draw(topscore, cam.position.x + deathmenubg.getRegionWidth() / 3 - topscore.getRegionWidth() / 4 + 20, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+130, topscore.getRegionWidth() / 2, topscore.getRegionHeight() / 2);
        batcher.draw(rating, cam.position.x - deathmenubg.getRegionWidth() / 3 + 10, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+35, rating.getRegionWidth() / 2, rating.getRegionHeight() / 2);

        droidSerifFont.draw(batcher, "%", cam.position.x + deathmenubg.getRegionWidth() / 3 - topscore.getRegionWidth() / 4 + 110, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+90);
        droidSerifFont.draw(batcher, "%", cam.position.x + deathmenubg.getRegionWidth() / 3 - topscore.getRegionWidth() / 4 + 110 , camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+230);

        batcher.draw(boostdown, cam.position.x - 40,  camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        batcher.draw(boostdown, cam.position.x - 135, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        batcher.draw(boostdown, cam.position.x - 230, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        batcher.draw(boostdown, cam.position.x - 325, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        batcher.draw(boostdown, cam.position.x - 420, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);

        /*if (scorenumber >= 20) {
            batcher.draw(boosttexture, cam.position.x - 420, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        }
        if (scorenumber >= 40) {
            batcher.draw(boosttexture, cam.position.x - 325, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        }
        if (scorenumber >= 60) {
            batcher.draw(boosttexture, cam.position.x - 230, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        }
        if (scorenumber >= 80) {
            batcher.draw(boosttexture, cam.position.x - 135, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        }
        if (scorenumber >= 100) {
            batcher.draw(boosttexture, cam.position.x - 40, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        }*/

        if (scorenumber== Loader.getHighScore()){
            font.draw(batcher,""+scorenumber, cam.position.x + deathmenubg.getRegionWidth() / 3 - topscore.getRegionWidth() / 4 + 40 , camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+230);
            batcher.draw(newHighscore, cam.position.x - newHighscore.getRegionWidth() / 4, camHeight -(newHighscore.getRegionHeight()/2)-10, newHighscore.getRegionWidth()/2, newHighscore.getRegionHeight()/2);
        } else if (scorenumber< Loader.getHighScore()) {
            font.draw(batcher, "" + Loader.getHighScore(), cam.position.x + deathmenubg.getRegionWidth() / 3 - topscore.getRegionWidth() / 4 + 40 , camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+230);
            batcher.draw(youvedied, cam.position.x - youvedied.getRegionWidth() / 4, camHeight - (youvedied.getRegionHeight() / 2) - 10, youvedied.getRegionWidth() / 2, youvedied.getRegionHeight() / 2);
        }
        if (scorenumber>=100) {
            batcher.draw(worldStabilized, cam.position.x - worldStabilized.getRegionWidth() / 4, camHeight - (worldStabilized.getRegionHeight() / 2) - 10, worldStabilized.getRegionWidth() / 1.9f, worldStabilized.getRegionHeight() / 1.9f);
        }
    }

    /*private void drawWorldMenu(){
        batcher.draw(bgRockies, camWidth/4-150, camHeight/3-100, 300, 200);
        batcher.draw(bgCanyon, 2*camWidth/4-150, camHeight/3-100, 300, 200);
        batcher.draw(bgThousandSuns, 3*camWidth/4-150, camHeight/3-100, 300, 200);
        batcher.draw(bgSnow, camWidth/4-150, 2*camHeight/3-100, 300, 200);
        batcher.draw(bgDesert, 2*camWidth/4-150, 2*camHeight/3-100, 300, 200);
        batcher.draw(bgChicago, 3*camWidth/4-150, 2*camHeight/3-100, 300, 200);
    }*/

    private void drawScore() {
        //scorenumber=
        //font.draw(batcher,  Integer.toString(world.gold), (camWidth / 2f) - scorelen*2, 19.5f*camHeight/20f);
        //droidSerifFont.draw(batcher, "%", cam.position.x + (camWidth / 2) - 40, cam.position.y - camHeight / 2 + 5);
        //font.draw(batcher, "POWER: " + (int)world.boost, cam.position.x - (camWidth / 2) + 5, cam.position.y - camHeight / 2 + 5 );
    }
    /*
        public void setCamPositionOriginal(){
            cam.position.x=camWidth/2;
            cam.position.y=camHeight/2;
            cam.update();
            batcher.setProjectionMatrix(cam.combined);
            campos = cam.position;
        }

        public void setCamPosition(float x,float y){
            cam.position.x=x;
            cam.position.y=y;
            cam.update();
            batcher.setProjectionMatrix(cam.combined);
            campos = cam.position;
        }
    */

    public void drawSurvival(float runTime, float delta) {
//burnerFire.setEmittersCleanUpBlendFunction(false);//can use this to make tall textures ghostly, see what blending function actually enables that
        for (TinyBird i : tinyBirdQueue){
            batcher.draw((TextureRegion) i.animation.getKeyFrame(runTime+i.flapRandomFactor), i.pos.x-i.width/2, i.pos.y-i.height/2,
                    i.width/2, i.height/2, i.width, i.height, 1f, 1f, i.rotation);
        }

        if (airship.isFlashing){
            //batcher.setColor(1,1,1,k.flashOpacityValue.get());
            batcher.setShader(flashShader);

            //System.out.println("Value sent to: "+k.flashOpacityValue.get());
            flashShader.setUniformf("flashOpacityValue", airship.flashOpacityValue.get());
            //flashShader.begin();
        }

        for (BirdAbstractClass k : deadBirdQueue) {
            if (k.isFlashing){
                //batcher.setColor(1,1,1,k.flashOpacityValue.get());
                //flashShader.begin();
                batcher.setShader(flashShader);
                //System.out.println("Value sent to: "+k.flashOpacityValue.get());
                flashShader.setUniformf("flashOpacityValue", k.flashOpacityValue.get());
            }
            batcher.draw((TextureRegion) k.animation.getKeyFrame(runTime+k.flapRandomFactor), k.x - k.width / 2, k.y - k.height / 2,
                    k.width/2, k.height/2, k.width, k.height, 1, 1, k.rotation);
            if (k.isFlashing){
                //batcher.setColor(1,1,1,1);
                batcher.setShader(null);
                k.flashTween.update(delta);
                //flashShader.end();
            }
            if (!k.dropsList.isEmpty()) {
                for (MovingImageContainer l : k.dropsList) {
                    l.draw(runTime, batcher);
                }
            }
        }

        drawAirship(delta);   //after deadBirds but before activeBirds
        if (airship.isFlashing){
            //batcher.setColor(1,1,1,1);
            batcher.setShader(null);
            //flashShader.end();
            airship.flashTween.update(delta);
        }
        /*for (Turret i : turretList) {
            batcher.draw(i.texture, i.position.x-i.width/2, i.position.y-i.height/2,
                    i.width/2, i.height/2, i.width, i.height, 1f, 1f, i.getRotation());
        } delegated to airship*/

        drawProjectiles();

        for (BirdAbstractClass k : activeBirdQueue) {/*
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(255,0,0, 1f);

            shapeRenderer.polygon(k.boundingPoly.getTransformedVertices());
            shapeRenderer.end();*/

            if (k.isFlashing){
                //batcher.setColor(1,1,1,k.flashOpacityValue.get());
                batcher.setShader(flashShader);

                //System.out.println("Value sent to: "+k.flashOpacityValue.get());
                flashShader.setUniformf("flashOpacityValue", k.flashOpacityValue.get());
                //flashShader.begin();
            }
            batcher.draw((TextureRegion) k.animation.getKeyFrame(runTime+k.flapRandomFactor), k.x - k.width / 2, k.y - k.height / 2,
                    k.width/2, k.height/2, k.width, k.height, 1f, 1f, k.rotation);
            if (k.isFlashing){

                //batcher.setColor(1,1,1,1);
                batcher.setShader(null);
                //flashShader.end();
                k.flashTween.update(delta);

            }

            //batcher.end();
            //batcher.begin();
        }
        drawAirshipReticle();
/*      if (airship.balloonHitbox!=null){
        shapeRendererCust.begin(ShapeRenderer.ShapeType.Line);
        shapeRendererCust.setColor(255,0,0, 1f);

        shapeRendererCust.polygon(airship.rackHitbox.getTransformedVertices());
        shapeRendererCust.polygon(airship.balloonHitbox.getTransformedVertices());
        shapeRendererCust.end();}*/


    }
    public void drawAirshipReticle(){
        airship.drawReticle(batcher);
    }
    public void drawAirship(float delta){
        airship.draw(batcher, delta);
    }
    public void drawProjectiles(){
        for (Projectile j : projectileList) {
            batcher.draw(j.texture, j.position.x-j.width/2, j.position.y-j.height/2,
                    j.width/2-j.xPosOffset, j.height/2, j.width, j.height, 1f, 1f, j.rotation);
        }
    }
    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        //Gdx.gl20.glClearColor(0.25f, 0.25f, 0.25f, 1);
        //Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //batcher.disableBlending();
        batcher.begin();

        //batcher.enableBlending();

        if (world.isSurvival()) {
            /*if (runTime<2){
            makeTransition(0, 0, 0, 10f);}*/

            drawBackground();
            batcher.end();
            lightHandler.renderBack();
            batcher.begin();
            drawSurvival(runTime, delta);

            drawScore();
            batcher.flush();
            batcher.end();
            uiHandler.stage.draw();

            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Line);//topPadding is 5, left padding is 6
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(6-2,camHeight-5-(uiHandler.rankSize)-2,uiHandler.rankSize+5,uiHandler.rankSize+4);

            //shapeRenderer.rect(6-1,camHeight-5-(uiHandler.rankSize)-1,uiHandler.rankSize+3,uiHandler.rankSize+2);
            shapeRenderer.end();*/
            //batcher.setColor(Color.WHITE);

            lightHandler.renderFront();
            batcher.begin();
       /* }  else if (world.isRunning()) {
            drawBackground();
            drawGlider(runTime);
            //drawBoosts();
            drawScore();

        } else if (world.isReady()) {
            drawBackground();
            //drawBoosts();
            if (AssetHandler.getHighScore()<15){
                batcher.draw(readybg, cam.position.x-camWidth/2, -camHeight/3f, readybg.getRegionWidth(), readybg.getRegionHeight());
            }
            drawGlider(runTime);
            readyButton.draw(batcher);
            drawScore();
*/      } else if (world.isBuyMenu()){
            drawBackground();
            batcher.end();
            lightHandler.renderBack();
            batcher.begin();
            drawSurvival(runTime, delta);

            drawScore();
            batcher.flush();
            batcher.end();
            uiHandler.stage.draw();
            //batcher.setColor(Color.WHITE);

            lightHandler.renderFront();
            batcher.begin();

        } else if (world.isMenu()) {
            drawBackground();
            //sunshineManager.update(delta);
            //drawSpritesMenu(runTime);
            //drawMenuUI();

        } else if (world.isDeathMenu()) {
            drawBackground();
            //sunshineManager2.update(delta);
            drawSpritesDeathMenu(runTime);
            drawDeathMenu();

        } else if (world.isCredits()) {
            drawCreditsbg();
            menuButton.draw(batcher);

        } else if (world.isInstr()) {
            drawInstrbg();
            menuButton.draw(batcher);
            nextButton.draw(batcher);

        } else if (world.isLogos()) {
            /*if (world.startGame) {
                drawBackground();
                batcher.end();
                lightHandler.renderBack();
                batcher.begin();
                drawSurvival(runTime, delta);

                drawScore();
                // batcher.flush();
                batcher.end();
                uiHandler.stage.draw();
                //batcher.setColor(Color.WHITE);

                lightHandler.renderFront();
                batcher.begin();

            }
            world.drawLogos(batcher,camWidth,camHeight);*/
        }
        //`System.out.println(batcher.getColor());
        //System.out.println(batcher.getColor().r+" "+batcher.getColor().g+" "+batcher.getColor().b+" "+batcher.getColor().a);

        batcher.end();
        //make usre having outside of batcher does nothing

        drawTransition(delta);
        //burnerFire.setEmittersCleanUpBlendFunction(false);//can use this to make tall textures ghostly, see what blending function actually enables that
        //System.out.println("gameRenderer edge:"+(cam.position.x - camWidth / 2));

    }

    public void makeTransition(int r, int g, int b, float duration) {
        transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
        alpha.set(1);
        manager.killAll();
        Tween.to(alpha, -1, duration).target(0)
                .ease(TweenEquations.easeOutQuad).start(manager);
    }

    private void drawTransition(float delta) {
        if (alpha.get() > 0) {
            manager.update(delta);
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(transitionColor.r, transitionColor.g,transitionColor.b, alpha.get());
            shapeRenderer.rect(-camWidth*.1f, -camHeight*.1f, camWidth*1.2f, camHeight*1.2f);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL30.GL_BLEND);
        }
    }

    public static Vector3 getCameraPosition() { return campos; }

    public static void dispose(){
        //foreRayHandler.dispose();

    }
}
