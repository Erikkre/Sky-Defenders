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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kredatus.flockblockers.GameObjects.Background;
//import com.kredatus.flockblockers.GameObjects.Boost;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Glider;
import com.kredatus.flockblockers.GameObjects.Projectile;
import com.kredatus.flockblockers.GameObjects.Turret;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.TurretHandler;
import com.kredatus.flockblockers.Screens.SplashScreen;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;
import com.kredatus.flockblockers.ui.SimpleButton;


import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

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
    private GameWorld myWorld;
    public OrthographicCamera cam;
    private FitViewport viewport;
    private ShapeRenderer shapeRenderer;
    boolean turnback=true;
    private SpriteBatch batcher;
    private Glider glider;
    public static Vector3 camposition;
    private Animation frontFlaps, leftSideFlaps, rightSideFlaps, flipflaps, frontViewFlaps, backFlaps;
    //private TextureRegion gliderMid, vertflipgliderMid;
    private int gliderscaling= AssetHandler.getgliderScaling();
    private TextureRegion  horflipbgtexture, vertflipbgtexture, horvertflipbgtexture, boosttexture, frontTexture,
            creditsbg, deathmenubg, newHighscore, topscore, deathmenuscore, rating, youvedied, boostdown,
            gliderbg, instrbg, readybg, frontglidermid, worldStabilized, gun, projectile;

    private Background background, background2, background3, background4;
    //public ArrayList<Boost> boostlist, flipboostlist, invboostlist, invflipboostlist;

    float rotate = 0, previousvalue;
    int scorenumber = 0;
    //public static boolean splashdown = false;
    public static FreeTypeFontGenerator.FreeTypeFontParameter smallfont, bigfont, xsmallfont, xxsmallfont;

    public static FreeTypeFontGenerator gamefont, otherfont;
    public static BitmapFont font, titlefont, storyfont, instrfont, droidSerifFont;


    public static ConcurrentLinkedQueue<BirdAbstractClass> activeBirdQueue;
    public ArrayList<Turret> turretList;
    public ConcurrentLinkedQueue<Projectile> projectileList;
    float highScorelen, len, endgamelen, tryAgainlen, boostTextLen, scorelen, startLevellen, titlelen,
            c0len, c1len, c2len, c3len, c4len, c5len, c6len, c7len, c8len, c9len, c10len, c11len, c12len, c13len, c14len,
            s0len, s1len, s2len, s3len, s4len, s5len, s6len, s7len, s8len, s9len, s10len, s11len, s12len, s13len, s14len;
    private GlyphLayout Highscore, endgame, tryAgain,  boost, score, startLevel, title,
            c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14,
            s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14;

    private Color transitionColor;
    private int camWidth, camHeight;
private BgHandler bgHandler;
private BirdHandler birdHandler;
private TurretHandler turretHandler;
    public GameRenderer(GameWorld world, int camWidth, int camHeight, BgHandler bgHandler, BirdHandler birdHandler, TurretHandler turretHandler) {
        this.birdHandler=birdHandler;
        this.bgHandler=bgHandler;
        this.turretHandler=turretHandler;
        this.camWidth=camWidth;
        this.camHeight=camHeight;

        myWorld = world;

        this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor()).getMenuButtons();
        this.deathButtons = ((InputHandler) Gdx.input.getInputProcessor()).getDeathButtons();

        this.readyButton = ((InputHandler) Gdx.input.getInputProcessor()).getReadyButton();
        this.menuButton = ((InputHandler) Gdx.input.getInputProcessor()).getMenuButton();
        this.nextButton =  ((InputHandler) Gdx.input.getInputProcessor()).getNextButton();

        batcher = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, camWidth, camHeight);
        cam.position.set(new Vector3(camWidth/2,camHeight/2,0));
        cam.update();
        batcher.setProjectionMatrix(cam.combined);
        System.out.println("Height: "+camHeight);
        //cam.position.set(new Vector3(0,0,0));
        //cam.position.x=9*camWidth/10;  //seems random but is 1/2(glider position in camWidth) + 2/5
        //cam.update();
        //viewport = new FitViewport(camWidth, viewHeight, cam);
        //viewport.apply();




        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        // Attach batcher to camera
        // Call helper methods to initialize instance variables
        initGameObjects();
        initAssets();
        //setupTweens();
        bigfont = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallfont = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallfontLengthSetup();
        bigfontLengthSetup();
        storyFontSetup();
        instrFontSetup();
        //creditsFontSetup();
        transitionColor = new Color();
        prepareTransition(255, 255, 255, .5f);
        //prepareSunshine();
    }

    /*private void setupTweens() {
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
       //bgPhoenix = AssetHandler.bgPhoenixtexture;
        horflipbgtexture = AssetHandler.horflipbgtexture;

        //flipworld
        vertflipbgtexture = AssetHandler.vertflipbgtexture;
        horvertflipbgtexture = AssetHandler.horvertflipbgtexture;

        boosttexture = AssetHandler.boosttexture;

        //gliderMid = AssetHandler.gliderMid;
        //vertflipgliderMid = AssetHandler.vertflipgliderMid;

        activeBirdQueue=birdHandler.activeBirdQueue;
        turretList= turretHandler.turretList;
        projectileList=turretHandler.projectileList;
        //tinyBirdList=birdHandler.activeBirdQueue;

        //frontFlaps = AssetHandler.frontFlaps;
        //flipflaps = AssetHandler.flipflaps;
        //frontViewFlaps = null;
        //frontglidermid=AssetHandler.frontGliderMid;
        //backFlaps= AssetHandler.backFlaps;

        creditsbg = AssetHandler.creditsbg;
        deathmenubg = AssetHandler.deathmenubg;
        gliderbg = AssetHandler.gliderbg;
        readybg = AssetHandler.readybg;
        instrbg = AssetHandler.instrbg;

        worldStabilized = AssetHandler.worldStabilized;
        newHighscore = AssetHandler.newHighscore;
        topscore = AssetHandler.topscore;
        deathmenuscore = AssetHandler.score;
        rating = AssetHandler.rating;
        youvedied = AssetHandler.youvedied;
        boostdown = AssetHandler.boostdown;
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
        glider = myWorld.getGlider();
    }

    private void smallfontLengthSetup() {
        smallfont.size = 48;
        smallfont.minFilter = Texture.TextureFilter.Linear;
        smallfont.magFilter = Texture.TextureFilter.MipMapNearestLinear;
        //smallfont.characters = "'0123456789.:?!,-%ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; //score and boost
        smallfont.flip = true;
        smallfont.borderWidth = 2;
        smallfont.color = Color.ORANGE;
        smallfont.borderColor = Color.BLACK;
        smallfont.shadowColor = Color.BLACK;
        smallfont.shadowOffsetX = 4;
        smallfont.shadowOffsetY = 2;
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

        score = new GlyphLayout(font, "STABILITY: 1069 ");
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
        s1 = new GlyphLayout(storyfont, "an elemental deity known simply as The Phoenix. Since the dawn of");
        s1len = s1.width;
        s2 = new GlyphLayout(storyfont, "time, it's infinite lives and elemental power are fueled by its home");
        s2len = s2.width;
        s3 = new GlyphLayout(storyfont, "star, Sol. After an impossible cosmic event, the universe has begun");
        s3len = s3.width;
        s4 = new GlyphLayout(storyfont, "to collapse. Reality is folding in on itself, worlds are destabilizing");
        s4len = s4.width;
        s5 = new GlyphLayout(storyfont, "and the Phoenix's purpose has been made clear. It must save the");
        s5len = s5.width;
        s6 = new GlyphLayout(storyfont, "universe once more. Able to traverse all planes of reality, it uses Sol's");
        s6len = s6.width;
        s7 = new GlyphLayout(storyfont, "power to stabilize worlds that it travels through. Only by absorbing");
        s7len = s7.width;
        s8 = new GlyphLayout(storyfont, "faraway fragments of Sol is it able to keep flying further away from");
        s8len = s8.width;
        s9 = new GlyphLayout(storyfont, "home. However, the closer it comes to saving a world, the less fragments");
        s9len = s9.width;
        s10 = new GlyphLayout(storyfont, "appear there for the Phoenix to use. The Phoenix will stop at nothing");
        s10len = s10.width;
        s11 = new GlyphLayout(storyfont, "to prevent the collapse of reality by travelling the universe and");
        s11len = s11.width;
        s12 = new GlyphLayout(storyfont, "stabilizing every last world.");
        s12len = s12.width;
    }

    private void instrFontSetup() {
        xxsmallfont = smallfont;
        xxsmallfont.size = 26;
        instrfont = gamefont.generateFont(xxsmallfont);
        c0 = new GlyphLayout(instrfont, "1. TAP THE SCREEN TO MAKE THE PHOENIX FLY AWAY FROM THE CLOSEST GROUND");
        c0len = c0.width;
        c1 = new GlyphLayout(instrfont, "2. DO NOT CRASH PHOENIX INTO WATER/EDGES; HE WILL HAVE TO REVIVE");
        c1len = c1.width;
        c2 = new GlyphLayout(instrfont, "3. GAIN POWER TO FLAP WINGS BY ABSORBING FIREBALLS, OR FRAGMENTS OF SOL");
        c2len = c2.width;
        c3 = new GlyphLayout(instrfont, "4. LARGER FRAGMENTS THAT GATHER AT EDGES OF THE WORLD HAVE MORE POWER");
        c3len = c3.width;
        c4 = new GlyphLayout(instrfont, "5. USABLE POWER TO FLAP WINGS-NUMBER AT TOP LEFT");
        c4len = c4.width;
        c5 = new GlyphLayout(instrfont, "6. FLYING FURTHER INCREASES WORLD STABILITY-PERCENTAGE AT TOP RIGHT");
        c5len = c5.width;
        c6 = new GlyphLayout(instrfont, "7. LESS FRAGMENTS APPEAR THE MORE THE PHOENIX STABILIZES THE WORLD");
        c6len = c6.width;
        c7 = new GlyphLayout(instrfont, "8. IF STABILITY REACHES 100 THEN PHOENIX WILL HAVE SAVED THE WORLD");
        c7len = c7.width;/*
        c7 = new GlyphLayout(instrfont, "\"Music: https://www.bensound.com/royalty-free-music");
        c7len = c7.width;
        c8 = new GlyphLayout(instrfont, "\"Fire.wav\" by SGAK of http://freesound.org");
        c8len = c8.width;
        c9 = new GlyphLayout(instrfont, "\nAll under CREATIVE COMMONS LICENSE");
        c9len = c9.width;
        c10 = new GlyphLayout(instrfont, "\n\n");
        c10len = c10.width;*/
    }

    public void drawInstr() {
        instrfont.draw(batcher, "1. TAP THE SCREEN TO MAKE THE PHOENIX FLY AWAY FROM THE CLOSEST GROUND", cam.position.x - c0len / 2, camHeight / 11);
        instrfont.draw(batcher, "2. DO NOT CRASH PHOENIX INTO WATER/EDGES; HE WILL HAVE TO REVIVE", cam.position.x - c1len / 2, 2 * camHeight / 11);
        instrfont.draw(batcher, "3. GAIN POWER TO FLAP WINGS BY ABSORBING FIREBALLS, OR FRAGMENTS OF SOL", cam.position.x - c2len / 2, 3 * camHeight / 11);
        instrfont.draw(batcher, "4. LARGER FRAGMENTS THAT GATHER AT EDGES OF THE WORLD HAVE MORE POWER", cam.position.x - c3len / 2, 4 * camHeight / 11);
        instrfont.draw(batcher, "5. USABLE POWER TO FLAP WINGS-NUMBER AT TOP LEFT", cam.position.x - c4len / 2, 5 * camHeight / 11);
        instrfont.draw(batcher, "6. FLYING FURTHER INCREASES WORLD STABILITY-PERCENTAGE AT TOP RIGHT", cam.position.x - c5len / 2, 6 * camHeight / 11);
        instrfont.draw(batcher, "7. LESS FRAGMENTS APPEAR THE MORE THE PHOENIX STABILIZES THE WORLD", cam.position.x - c6len / 2, 7 * camHeight / 11);
        instrfont.draw(batcher, "8. IF STABILITY REACHES 100 THEN PHOENIX WILL HAVE SAVED THE WORLD", cam.position.x - c7len / 2, 8 * camHeight / 11);
    }

    private void drawBackground() {
        batcher.draw(background.getTexture(), background.getX(), background.getY(),
                background.getWidth(), background.getHeight());

        batcher.draw(background2.getTexture(), background2.getX(), background2.getY(),
               background2.getWidth(), background2.getHeight());

        //flip world
        //batcher.draw(vertflipbgtexture, background3.getX(), background3.getY(),
         //       background3.getWidth(), background3.getHeight());

       //batcher.draw(horvertflipbgtexture, background4.getX(), background4.getY(),
       //         background4.getWidth(), background4.getHeight());
    }

/*
    private void specificdrawBoosts(ArrayList<Boost> boostlist) {
        for (int i = 0; i < boostlist.size(); i++) {
            rotate += 0.3;
            batcher.draw(boosttexture, boostlist.get(i).getX(), boostlist.get(i).getY(), boostlist.get(i).getWidth() / 2.0f, boostlist.get(i).getHeight() / 2.0f, boostlist.get(i).getWidth(),
                    boostlist.get(i).getHeight(), 1, 1, rotate);
        }
    }

    private void drawBoosts() {
        specificdrawBoosts(boostlist);
        specificdrawBoosts(invboostlist);
        specificdrawBoosts(flipboostlist);
        specificdrawBoosts(invflipboostlist);
    }*/

    private void drawGlider(float runTime){
        /*if (glider.isAlive()){
            //setCamPosition( glider.getPosition().x + camWidth / 2.5f, glider.getPosition().y+glider.getHeight()/2);

            if (glider.isNormalWorld()) {
                if (glider.shouldntFlap()&& !myWorld.isReady()) {
                    batcher.draw(gliderMid, glider.getPosition().x, glider.getPosition().y,
                            glider.getWidth() / 2.0f, glider.getHeight() / 2.0f, glider.getWidth(), glider.getHeight(), 1, 1, glider.getRotation());
                } else {
                    batcher.draw((TextureRegion) frontFlaps.getKeyFrame(runTime+0.1f), glider.getPosition().x, glider.getPosition().y,
                            glider.getWidth() / 2.0f, glider.getHeight() / 2.0f, glider.getWidth(), glider.getHeight(), 1, 1, glider.getRotation());
                }
            }
            //inbetween flip world
            else if (glider.betweenWorlds()) {
                frontTexture = (TextureRegion) frontViewFlaps.getKeyFrame(runTime);
                batcher.draw(frontTexture, glider.getPosition().x, glider.getPosition().y,
                        (frontTexture.getRegionWidth()/gliderscaling)/ 2.0f, (frontTexture.getRegionHeight()/gliderscaling) / 2.0f,
                        frontTexture.getRegionWidth()/gliderscaling, frontTexture.getRegionHeight()/gliderscaling, 1, 1, glider.getRotation() + 90);
            } else {
                if (glider.shouldntFlap()) {
                    batcher.draw(vertflipgliderMid, glider.getPosition().x, glider.getPosition().y,
                            glider.getWidth() / 2.0f, glider.getHeight() / 2.0f, glider.getWidth(), glider.getHeight(), 1, 1, glider.getRotation());
                } else {
                    batcher.draw((TextureRegion) flipflaps.getKeyFrame(runTime), glider.getPosition().x, glider.getPosition().y,
                            glider.getWidth() / 2.0f, glider.getHeight() / 2.0f, glider.getWidth(), glider.getHeight(), 1, 1, glider.getRotation());
                }
            }
        }else{
            /*if (splashdown) {
                if (glider.isNormalWorld()){
                    cam.position.x = glider.getPosition().x - camWidth;
                    cam.position.y = glider.getPosition().y - camHeight;
                }else{
                    cam.position.x = glider.getPosition().x - camWidth;
                    cam.position.y = glider.getPosition().y + camHeight;
                }
                prepareTransition(0, 0, 0, 3f);
                setCamPosition( glider.getPosition().x + camWidth / 2.5f, glider.getPosition().y);
                splashdown=false;}
            AssetHandler.frontViewFlaps.setFrameDuration((glider.distanceAfterDeath() / 200 + 0.17f));
            frontTexture = (TextureRegion) frontViewFlaps.getKeyFrame(runTime);
            batcher.draw(frontTexture, glider.getPosition().x, glider.getPosition().y, frontTexture.getRegionWidth() /gliderscaling/ 2.0f, frontTexture.getRegionHeight() /gliderscaling/ 2.0f,
                    frontTexture.getRegionWidth()/gliderscaling, frontTexture.getRegionHeight()/gliderscaling, 1, 1, glider.getRotation() - 90);
        }*/
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
        beta.setValue(camWidth*1.6f); //start val
        Tween.registerAccessor(Value.class, new ValueAccessor());
        sunshineManager = new TweenManager();
        Tween.to(beta, -1, 6f).target(0)    //end val
                .ease(TweenEquations.easeOutQuad).repeat(Tween.INFINITY,5f).start(sunshineManager);

        sunshineManager2= new TweenManager();
        charlie.setValue(AssetHandler.frontGliderMid.getRegionWidth()/2);
        Tween.to(charlie, -1, 10).target(0)    //end val
                .ease(TweenEquations.easeInOutExpo).repeatYoyo(Tween.INFINITY,0).start(sunshineManager2);
        delta.setValue(AssetHandler.frontGliderMid.getRegionHeight()/2);
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

        /*if (charlie.getValue()<previousvalue && turnback==false && charlie.getValue()<0.85*AssetHandler.frontGliderMid.getRegionWidth()/2){   //if bird going away and 0.2 of the way there and facing towards, face away
            turnback=true;
        } else if (charlie.getValue()> previousvalue && turnback==true){   //if bird coming and facing away, face towards
            turnback=false;
        }
        if (!turnback){
            batcher.draw(gliderbg, cam.position.x-charlie.getValue()*1.5f, glider.getPosition().y- 40  - charlie.getValue()*1.5f, charlie.getValue()*1.5f, charlie.getValue()*1.5f,
                    charlie.getValue()*3, charlie.getValue()*3, 1, 1, rotate);
            frontTexture = (TextureRegion) frontViewFlaps.getKeyFrame(runTime+0.5f);
            batcher.draw(frontTexture, cam.position.x-charlie.getValue()* ((float)frontTexture.getRegionWidth()/frontglidermid.getRegionWidth()), glider.getPosition().y - 40 -delta.getValue()* ((float)frontTexture.getRegionHeight()/frontglidermid.getRegionHeight()),
                    charlie.getValue()*2*((float)frontTexture.getRegionWidth()/frontglidermid.getRegionWidth()), delta.getValue()*2* ((float)frontTexture.getRegionHeight()/frontglidermid.getRegionHeight()));
        } else {
            frontTexture = (TextureRegion) backFlaps.getKeyFrame(runTime+0.5f);
            batcher.draw(frontTexture, cam.position.x-charlie.getValue()* ((float)frontTexture.getRegionWidth()/frontglidermid.getRegionWidth()), glider.getPosition().y - 40 -delta.getValue()* ((float)frontTexture.getRegionHeight()/frontglidermid.getRegionHeight()),
                    charlie.getValue()*2*((float)frontTexture.getRegionWidth()/frontglidermid.getRegionWidth()), delta.getValue()*2* ((float)frontTexture.getRegionHeight()/frontglidermid.getRegionHeight()));
            batcher.draw(gliderbg, cam.position.x-charlie.getValue()*1.5f, glider.getPosition().y- 40  - charlie.getValue()*1.5f, charlie.getValue()*1.5f, charlie.getValue()*1.5f,
                    charlie.getValue()*3, charlie.getValue()*3, 1, 1, rotate);
        }
        previousvalue=charlie.getValue();*/
    }

    private void drawSpritesMenu(float runTime) {
        rotate += 3;
        batcher.draw(gliderbg, cam.position.x-beta.getValue(), camHeight/2-50 -beta.getValue(), beta.getValue(), beta.getValue(),
                beta.getValue()*2, beta.getValue()*2, 1, 1, rotate);
        frontTexture = (TextureRegion) frontFlaps.getKeyFrame(runTime+0.5f);

        batcher.draw(frontTexture, cam.position.x-frontTexture.getRegionWidth()/(gliderscaling/1.8f)/2, glider.getPosition().y +cam.position.y- glider.starty-30 -frontTexture.getRegionHeight()/(gliderscaling /1.8f)/2, frontTexture.getRegionWidth()/(gliderscaling/1.8f), frontTexture.getRegionHeight()/(gliderscaling/1.8f));
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

        batcher.draw(boostdown, cam.position.x - 40, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        batcher.draw(boostdown, cam.position.x - 135, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        batcher.draw(boostdown, cam.position.x - 230, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        batcher.draw(boostdown, cam.position.x - 325, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);
        batcher.draw(boostdown, cam.position.x - 420, camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+140, 105, 120);

        if (scorenumber >= 20) {
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
        }

        if (scorenumber== AssetHandler.getHighScore()){
            font.draw(batcher,""+scorenumber, cam.position.x + deathmenubg.getRegionWidth() / 3 - topscore.getRegionWidth() / 4 + 40 , camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+230);
            batcher.draw(newHighscore, cam.position.x - newHighscore.getRegionWidth() / 4, camHeight -(newHighscore.getRegionHeight()/2)-10, newHighscore.getRegionWidth()/2, newHighscore.getRegionHeight()/2);
        } else if (scorenumber< AssetHandler.getHighScore()) {
            font.draw(batcher, "" + AssetHandler.getHighScore(), cam.position.x + deathmenubg.getRegionWidth() / 3 - topscore.getRegionWidth() / 4 + 40 , camHeight-newHighscore.getRegionHeight()/2-deathmenubg.getRegionHeight()+230);
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
        scorenumber= (int) (glider.getPosition().x - camWidth / 2)/2000;
        font.draw(batcher, "STABILITY: " + scorenumber, cam.position.x + (camWidth / 2) - scorelen, cam.position.y - camHeight / 2 + 5);
        droidSerifFont.draw(batcher, "%", cam.position.x + (camWidth / 2) - 40, cam.position.y - camHeight / 2 + 5);
        font.draw(batcher, "POWER: " + (int)myWorld.boost, cam.position.x - (camWidth / 2) + 5, cam.position.y - camHeight / 2 + 5 );
    }
    /*
        public void setCamPositionOriginal(){
            cam.position.x=camWidth/2;
            cam.position.y=camHeight/2;
            cam.update();
            batcher.setProjectionMatrix(cam.combined);
            camposition = cam.position;
        }

        public void setCamPosition(float x,float y){
            cam.position.x=x;
            cam.position.y=y;
            cam.update();
            batcher.setProjectionMatrix(cam.combined);
            camposition = cam.position;
        }
    */

    public void drawStory(float runTime, float delta) {
        //System.out.println("Glider: "+glider.getPosition());
        //System.out.println("Cam: "+cam.position);
        //glider.getPosition().x+glider.getWidth()/2  glider.getPosition().y+glider.getHeight()/2

        for (Turret i : turretList) {
            batcher.draw(i.texture, i.position.x-i.width/2, i.position.y-i.height/2,
                    i.width/2, i.height/2, i.width, i.height, 1f, 1f, i.rotation);
        }

        for (Projectile j : projectileList) {
            batcher.draw(j.texture, j.position.x-j.width/2, j.position.y-j.height/2,
                    j.width/2, j.height/2, j.width, j.height, 1f, 1f, j.rotation);
        }
        for (BirdAbstractClass j : activeBirdQueue) {
            batcher.draw((TextureRegion) j.animation.getKeyFrame(runTime), j.x - j.width / 2, j.y - j.height / 2,
                    j.width/2, j.height/2, j.width, j.height, 1f, 1f, j.rotation);
batcher.end();
            //Gdx.gl.glEnable(GL30.GL_BLEND);
           // Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

            //shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(255,0,0, 1f);

            //i.boundingPoly.setPosition(i.x, i.y);
            shapeRenderer.polygon(j.boundingPoly.getTransformedVertices());        //
            shapeRenderer.end();
            batcher.begin();
           // Gdx.gl.glDisable(GL30.GL_BLEND);
            //System.out.println("x: "+(i.x - i.width / 2) + " y: "+ (i.y - i.height / 2));
        }

        //activeBirdList.get(0).update(delta);
        /*
        batcher.draw((TextureRegion) frontFlaps.getKeyFrame(runTime+0.1f), glider.getPosition().x-glider.getWidth()/2, glider.getPosition().y-glider.getHeight()/2,
                glider.getPosition().x-glider.getWidth()/2,glider.getPosition().y-glider.getHeight()/2, glider.getWidth(), glider.getHeight(), 1, 1, glider.getRotation());*/

        /*
        storyfont.draw(batcher, "Stories are told of a hero that only appears in times of great chaos,", cam.position.x - s0len / 2, camHeight / 15);
        storyfont.draw(batcher, "an elemental deity known simply as The Phoenix. Since the dawn of", cam.position.x - s2len / 2, 2 * camHeight / 15);
        storyfont.draw(batcher, "time, its infinite lives and elemental power are fueled by its home", cam.position.x - s1len / 2, 3 * camHeight / 15);
        storyfont.draw(batcher, "star, Sol. After an impossible cosmic event, the universe has begun", cam.position.x - s3len / 2, 4 * camHeight / 15);
        storyfont.draw(batcher, "to collapse. Reality is folding in on itself, worlds are destabilizing", cam.position.x - s4len / 2, 5 * camHeight / 15);
        storyfont.draw(batcher, "and the Phoenix's purpose has been made clear. It must save the", cam.position.x - s5len / 2, 6 * camHeight / 15);
        storyfont.draw(batcher, "universe once more. Able to traverse all planes of reality, it uses Sol's", cam.position.x - s6len / 2, 7 * camHeight / 15);
        storyfont.draw(batcher, "power to stabilize the worlds it travels through. Only by absorbing", cam.position.x - s7len / 2, 8 * camHeight / 15);
        storyfont.draw(batcher, "faraway fragments of Sol is it able to keep flying further away from", cam.position.x - s8len / 2, 9 * camHeight / 15);
        storyfont.draw(batcher, "home. However, the closer it comes to saving a world, the less fragments", cam.position.x - s9len / 2, 10 * camHeight / 15);
        storyfont.draw(batcher, "appear there for the Phoenix to use. The Phoenix will stop at nothing", cam.position.x - s10len / 2, 11 * camHeight / 15);
        storyfont.draw(batcher, "to prevent the collapse of reality by travelling the universe and", cam.position.x - s11len / 2, 12 * camHeight / 15);
        storyfont.draw(batcher, "stabilizing every last world.", cam.position.x - s12len / 2, 13 * camHeight / 15);*/
    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        //batcher.disableBlending();
        batcher.enableBlending();

        if (myWorld.isStory()) {

            //System.out.println(glider.getPosition());
            //System.out.print("Cam position:" + cam.position);
            if (runTime<2){ //start of game intro
//                System.out.println(cam.position);
            prepareTransition(0, 0, 0, 10f);}
            //if (!bgHandler.vertPosBg.isStarted()){
                //bgHandler.vertPosBg.start(manager);
                //bgHandler.horizPosBg.start(manager);
            //}
            drawBackground();
            /*
            cam.position.y+=10;
            cam.update();
            batcher.setProjectionMatrix(cam.combined);*/


            drawStory(runTime, delta);

            /*
            if (AssetHandler.getHighScore() == 0) {
                nextButton.draw(batcher);
            } else {
                menuButton.draw(batcher);
            }*/
        }  else if (myWorld.isRunning()) {
            drawBackground();
            drawGlider(runTime);
            //drawBoosts();
            drawScore();

        } else if (myWorld.isReady()) {
            drawBackground();
            //drawBoosts();
            if (AssetHandler.getHighScore()<15){
                batcher.draw(readybg, cam.position.x-camWidth/2, -camHeight/3f, readybg.getRegionWidth(), readybg.getRegionHeight());
            }
            drawGlider(runTime);
            readyButton.draw(batcher);
            drawScore();

        } else if (myWorld.isMenu()) {
            drawBackground();
            //sunshineManager.update(delta);
            drawSpritesMenu(runTime);
            drawMenuUI();

        } else if (myWorld.isDeathMenu()) {
            drawBackground();
            //sunshineManager2.update(delta);
            drawSpritesDeathMenu(runTime);
            drawDeathMenu();

        } else if (myWorld.isCredits()) {
            drawCreditsbg();
            menuButton.draw(batcher);

        } else if (myWorld.isInstr()) {
            drawInstrbg();
            menuButton.draw(batcher);
            nextButton.draw(batcher);

        } else if (myWorld.isInstr2()) {
            drawInstr();
            menuButton.draw(batcher);
        }

        batcher.end();
        drawTransition(delta);
        //System.out.println("gameRenderer edge:"+(cam.position.x - camWidth / 2));
    }

    public void prepareTransition(int r, int g, int b, float duration) {
        transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
        alpha.setValue(1);
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = SplashScreen.getManager();
        Tween.to(alpha, -1, duration).target(0)
                .ease(TweenEquations.easeOutQuad).start(manager);
    }

    private void drawTransition(float delta) {
     //   if (alpha.getValue() > 0) {
         //   manager.update(delta);
          //  Gdx.gl.glEnable(GL30.GL_BLEND);
          //  Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
           // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
          //  shapeRenderer.setColor(transitionColor.r, transitionColor.g,transitionColor.b, alpha.getValue());
          //  shapeRenderer.rect(-camWidth*5000, -camHeight*5000, camWidth*10000, camHeight*10000);
          //  shapeRenderer.end();
          //  Gdx.gl.glDisable(GL30.GL_BLEND);
      //  }
    }

    public static Vector3 getCameraPosition() { return camposition; }

    public static void dispose(){
        font.dispose();
        gamefont.dispose();
        otherfont.dispose();
        instrfont.dispose();
        titlefont.dispose();
        droidSerifFont.dispose();
    }
}
