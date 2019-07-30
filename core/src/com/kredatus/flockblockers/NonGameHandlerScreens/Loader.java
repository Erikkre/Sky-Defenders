package com.kredatus.flockblockers.NonGameHandlerScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.TweenAccessors.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class Loader implements Screen {
    public static Random r = new Random();
    public static int menumusiciterator, musiciterator;
    public static  Music[] musiclist, menumusiclist;
    public static Animation[] tinyAnims, phoenixAnims, nightAnims,waterAnims,fireAnims, acidAnims,thunderAnims,goldAnims,lunarAnims;

    public static TextureAtlas tA;


    public static ArrayList<TextureRegion> bgList;

    //public static Sound splashdown, swoop, fire, birdHit, balloonHit;
    public static Music deathmenumusic;

    public static Animation<TextureRegion>
            tinyAnim1, tinyAnim2,tinyAnim3, tinyAnim4, tinyAnim5, tinyAnim6,tinyAnim7,tinyAnim8,tinyAnim9,tinyAnim10,tinyAnim11,
            coinAnimation;

    public static Preferences prefs;

    public  ShaderProgram flashShader;

    public static ParticleEffect burnerFire=new ParticleEffect(), thrusterFireLeft=new ParticleEffect(), thrusterFireRight=new ParticleEffect(); //thrusterFireUp=new ParticleEffect();
    public static Array<ParticleEffectPool.PooledEffect> additiveEffects = new Array<ParticleEffectPool.PooledEffect>(3), nonAdditiveEffects;
    public static Array<ParticleEmitter> firstEmittersOfEachEffect=new Array<ParticleEmitter>();

    public ProgressBar loadBar;
    public Label percentDoneLabel;
   // private static TweenManager manager;

    private Sprite sprite, sunshine;
    private FlockBlockersMain game;
    public static final int width= 1;
    private Skin niteSkin, shadeSkin;
    public AssetHandler assets=new AssetHandler();
    public AssetManager manager=assets.manager;
    public Stage stage;
    private int camWidth,camHeight;
    public int screenWidth, screenHeight;
    public GameHandler gameHandler;
    Image logoImg,companyNameImg;
    //CustomParticleEffectActor logoFire;

    int middleFirstLast=0, hueValue=66, hueAdder=4;

    Value loadBarAlpha= new Value(1);
    Tween glowingLoadingBarTween=Tween.to(loadBarAlpha,0,0.5f).target(0).repeatYoyo(1,0).ease(TweenEquations.easeInCubic).start();
    boolean isFirstTime;

    long soundID;
    public static TextureRegion[] ranksList;
    public Loader(FlockBlockersMain game) {
        Texture.setAssetManager(manager);

        assets.load();


        this.game = game;
        // Create (or retrieve existing) preferences file
        prefs = Gdx.app.getPreferences("skyDefenders");
        if (!prefs.contains("highScore")) {
            System.out.println("make true");
            isFirstTime=true;
            prefs.putInteger("highScore", 1);
            prefs.flush();
        }

        setupStage();
        setupLoadingBarAndLogo();
        //soundID= ((Sound) manager.get(assets.ignitionFire0Deignition7s)).play(1f);
    }

    @Override
    public void render(final float delta) {
        if (System.currentTimeMillis()-game.startTime>550) {
            load();
            //System.out.println(loadBar.getPercent() + " " + (manager.getProgress() + 0.02f));
            glowingLoadingBarTween.update(delta);
            if (manager.getProgress() != 1.0 && loadBar.getVisualPercent() < manager.getProgress() + 0.02f && glowingLoadingBarTween.isFinished()){
                glowingLoadingBarTween = Tween.to(loadBarAlpha, 0, 0.5f).target(0).repeatYoyo(1, 0).ease(TweenEquations.easeInCubic).start();
            } else if (manager.getProgress() == 1.0 && glowingLoadingBarTween.isYoyo()) glowingLoadingBarTween = Tween.to(loadBarAlpha, 0, 1f).target(0).ease(TweenEquations.easeNone).start();

            if (manager.getProgress() == 1.0 && !glowingLoadingBarTween.isYoyo() && !isFirstTime){
               // ((Sound) manager.get(assets.ignitionFire0Deignition7s)).setVolume(soundID,loadBarAlpha.get());
                //System.out.println("sound set to: "+loadBarAlpha.get());
            }
            loadBar.setColor(1, 1, 1, loadBarAlpha.get());
            percentDoneLabel.setColor(1, 1, 1, loadBarAlpha.get());
            //shadeSkin.getTiledDrawable("loading-bar-fill").tint(new Color(1,1,1,loadBarAlpha.get()));

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
            stage.act(delta);
            stage.draw();


            loadBar.setValue(manager.getProgress() + 0.032f);

            //setFireColor();
            percentDoneLabel.setText((int)(manager.getProgress()*100)+"%");
        }
    }

    private void setupLoadingBarAndLogo(){
        /********************************************************LOADBAR*/
        //niteSkin = manager.get(assets.niteRideUI);
        shadeSkin = manager.get(assets.shadeUI);
        loadBar = new ProgressBar(0, 1, 0.001f, false, shadeSkin.get("default-horizontal", ProgressBar.ProgressBarStyle.class));
        shadeSkin.getDrawable("loading-bar-fill-3d-10patch").setMinHeight(loadBar.getPrefHeight()/3f);shadeSkin.getDrawable("loading-bar-bg").setMinHeight(loadBar.getPrefHeight()/3f+4);
        //`loadBar.setColor(1,0,0,1f);

        loadBar.setAnimateDuration(0.3f);
        loadBar.setWidth(camWidth/1.5f);
        loadBar.setPosition((camWidth-loadBar.getWidth())/2,camHeight/5f-loadBar.getHeight()/2.7f);


        //3.2% is the minimum value right now
        stage.addActor(loadBar);
        //loadTable.setFillParent(true);
        //loadTable.add(loadBar).padTop(4*camHeight/5f);
        percentDoneLabel=new Label(manager.getProgress()*100+"%",shadeSkin.get("title-plain", Label.LabelStyle.class));
        percentDoneLabel.setPosition((camWidth-percentDoneLabel.getWidth())/2,camHeight/5f-loadBar.getHeight()/2);

        stage.addActor(percentDoneLabel);

        Texture companyNameTex=manager.get(assets.companyName);companyNameTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture logoImgTex=manager.get(assets.logo);logoImgTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logoImg = new Image(logoImgTex);companyNameImg= new Image(companyNameTex);

        /*PARTICLE EFFECT ACTOR BURNERFIRE
        logoFire = new CustomParticleEffectActor((ParticleEffect) manager.get(assets.logoFire));
        logoFire.particleEffect.getEmitters().get(0).getSpawnWidth().setHigh(logoImg.getWidth());
        logoFire.particleEffect.getEmitters().get(0).getEmission().setHigh(2000);
        logoFire.particleEffect.getEmitters().get(0).getVelocity().setHigh(200);
        logoFire.particleEffect.getEmitters().get(0).getLife().setHigh(900);
        logoFire.particleEffect.getEmitters().get(0).getDuration().setLow(10,90);
        logoFire.particleEffect.scaleEffect(1.2f);*/

//- logoFire.particleEffect.getEmitters().get(0).getSpawnWidth().getHighMax()/2f``
        //logoFire.setPosition(camWidth / 2f , camHeight / 2f );

        //logoFire.particleEffect.getEmitters().get(0).getTint().setColors(new float[]{244/255f,216/255f,216/255f});

        //stage.addActor(logoFire);

        /********************************************************LOGO*/
        Runnable endLoad = new Runnable() {
            @Override
            public void run() {
                isFirstTime=false;
                if (manager.getProgress()==1.0) {
                    loaded();
                    postLoad();
                }
            }
        };

        logoImg.setOrigin(logoImg.getWidth() / 2, logoImg.getHeight() / 2);
        logoImg.setPosition(camWidth / 2f - logoImg.getWidth() / 2f, camHeight / 2f + logoImg.getHeight());

        float durationOfSwingIn=1.5f;
        logoImg.addAction(sequence(alpha( 0), scaleTo(.05f, .05f),
                parallel(fadeIn(durationOfSwingIn, Interpolation.pow2),
                        scaleTo(1, 1, durationOfSwingIn, Interpolation.pow5),
                        moveTo(camWidth / 2f - logoImg.getWidth() / 2f, camHeight / 2f + companyNameImg.getHeight()/3  , durationOfSwingIn, Interpolation.bounceOut)),
                delay(1.5f), fadeOut(1.25f), run(endLoad)));


        companyNameImg.setOrigin(companyNameImg.getWidth() / 2, companyNameImg.getHeight() / 2);
        companyNameImg.setPosition(camWidth / 2f - companyNameImg.getWidth() / 2f, camHeight / 2f - companyNameImg.getHeight() );
        //companyNameImg.setScale(1.2f);
        companyNameImg.addAction(
                sequence(
                        alpha(0),
                        delay(0.7f),
                        fadeIn(2.5f, Interpolation.pow2Out),
                        delay(1),
                        fadeOut(2.5f,Interpolation.exp10),
                        run(endLoad)
                )
        );


        /*logoImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
                        moveTo(camWidth / 2f - logoImg.getWidth() / 2, camHeight / 2f - logoImg.getHeight() / 2, 2f, Interpolation.swing)),
                delay(1.5f), fadeOut(1.25f), run(transitionRunnable)));*/



        stage.addActor(logoImg);stage.addActor(companyNameImg);
    }

    private void setupStage(){
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        camHeight=1200;
        camWidth=(int)  (camHeight* (screenWidth/(float)screenHeight)) ;
        stage = new Stage(new ExtendViewport(camWidth,camHeight, new OrthographicCamera()), new SpriteBatch());
    }
    private void load(){
        if (!manager.update()){
            //System.out.println(manager.getProgress());
        } else if (!isFirstTime){
            //logoFire.particleEffect.allowCompletion();
            logoImg.clearActions();
            if (!glowingLoadingBarTween.isYoyo()&&loadBarAlpha.get()==0){
                //((Sound) manager.get(assets.ignitionFire0Deignition7s)).stop();
                loaded();
                postLoad();
            }
        }
    }

    private void postLoad(){
        shadeSkin.getSprite("loading-bar-fill-3d").setAlpha(1);
        stage.clear();
        gameHandler=new GameHandler(shadeSkin,camWidth,camHeight);
        game.setScreen(gameHandler);
    }

    /*public void setFireColor(){
        //System.out.println(logoFire.particleEffect.getEmitters().get(0).getTint().getColors()[0]);
        if (middleFirstLast == 0) {
            logoFire.particleEffect.getEmitters().get(0).getTint().setColors(new float[]{logoFire.particleEffect.getEmitters().get(0).getTint().getColors()[0], (hueValue += hueAdder) / 255f, logoFire.particleEffect.getEmitters().get(0).getTint().getColors()[2]});
        } else if (middleFirstLast == 1) {
            logoFire.particleEffect.getEmitters().get(0).getTint().setColors(new float[]{(hueValue += hueAdder) / 255f, logoFire.particleEffect.getEmitters().get(0).getTint().getColors()[1], logoFire.particleEffect.getEmitters().get(0).getTint().getColors()[2]});
        } else if (middleFirstLast == 2) {
            logoFire.particleEffect.getEmitters().get(0).getTint().setColors(new float[]{logoFire.particleEffect.getEmitters().get(0).getTint().getColors()[0], logoFire.particleEffect.getEmitters().get(0).getTint().getColors()[1], (hueValue += hueAdder) / 255f});
        }
        if (hueValue > 194 || hueValue < 16) {
            if (middleFirstLast < 2) {
                middleFirstLast++;
            } else {
                middleFirstLast = 0;
            }

            if (hueAdder == 4) hueAdder = -4;
            else hueAdder = 4;
        }
    }*/
    @Override
    public void show() {
    }
    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void hide() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }

    public void loaded(){

        shadeSkin = manager.get(assets.shadeUI);


        //burnerFire.setEmittersCleanUpBlendFunction(false);//Stop the additive effect resetting, speeding up batcher
        burnerFire=manager.get(assets.burnerFire);
        ParticleEffectPool burnerFirePool= new ParticleEffectPool(burnerFire,1,2);
        ParticleEffectPool.PooledEffect pooledEffect=burnerFirePool.obtain();
        additiveEffects.add(pooledEffect);

        thrusterFireLeft=manager.get(assets.thrusterFireLeft);
        //thrusterFireLeft.setEmittersCleanUpBlendFunction(false);//Stop the additive effect resetting, speeding up batcher
        ParticleEffectPool thrusterFireLeftPool= new ParticleEffectPool(thrusterFireLeft,1,2);
        pooledEffect=thrusterFireLeftPool.obtain();
        additiveEffects.add(pooledEffect);

        thrusterFireRight=manager.get(assets.thrusterFireRight);
        //thrusterFireRight.setEmittersCleanUpBlendFunction(false);//Stop the additive effect resetting, speeding up batcher
        ParticleEffectPool thrusterFireRightPool= new ParticleEffectPool(thrusterFireRight,1,2);
        pooledEffect=thrusterFireRightPool.obtain();
        additiveEffects.add(pooledEffect);

        for (ParticleEffectPool.PooledEffect i: additiveEffects){
            firstEmittersOfEachEffect.add(i.getEmitters().get(0));
        }

        menumusiciterator = r.nextInt(3);
        musiciterator = r.nextInt(6);

        tA=manager.get(assets.textures);

        /*logo = tA.findRegion("companyLogo");
        slidemenuBg = tA.findRegion("slideMenuBackground");
        menuButton = tA.findRegion("menuButton");
        shareButton = tA.findRegion("shareButton");
        rateButton = tA.findRegion("rateButton");*/

        TextureRegion bgCloudSeparatorTexture = tA.findRegion("cloudSeparator"); //if this line works then make the lines below look like it

        TextureRegion bgPhoenixtexture = new TextureRegion(tA.findRegion("bgPhoenix2"));
        bgPhoenixtexture.flip(false, true);
        TextureRegion bgPhoenixtexture2 = tA.findRegion("bgPhoenix2");

        TextureRegion bgThundertexture =  new TextureRegion(tA.findRegion("bgThunder2"));
        bgThundertexture.flip(false, true);
        TextureRegion bgThundertexture2 = tA.findRegion("bgThunder2");


        TextureRegion bgWatertexture = new TextureRegion(tA.findRegion("bgWater2"));
        bgWatertexture.flip(false, true);
        TextureRegion bgWatertexture2 = tA.findRegion("bgWater2");

        TextureRegion bgFiretexture = new TextureRegion(tA.findRegion("bgFire2"));
        bgFiretexture.flip(false, true);
        TextureRegion bgFiretexture2 = tA.findRegion("bgFire2");

        TextureRegion bgAcidtexture = new TextureRegion(tA.findRegion("bgAcid2"));
        bgAcidtexture.flip(false, true);
        TextureRegion bgAcidtexture2 = tA.findRegion("bgAcid2");

        TextureRegion bgNighttexture = new TextureRegion(tA.findRegion("bgNight2"));
        bgNighttexture.flip(false, true);
        TextureRegion bgNighttexture2 = tA.findRegion("bgNight2");

        TextureRegion bgLunartexture = new TextureRegion(tA.findRegion("bgLunar2"));
        bgLunartexture.flip(false,  true);
        TextureRegion bgLunartexture2 = tA.findRegion("bgLunar2");

        TextureRegion bgGoldtexture = new TextureRegion(tA.findRegion("bgGold2"));
        bgGoldtexture.flip(false, true);
        TextureRegion bgGoldtexture2 = tA.findRegion("bgGold2");

        //   0                 9                       18                      27                      36                      45                        54                      63
        //      1 2  4 5  7 8     10 11  13 14  16 17     19 20  22 23  25 26     28 29  31 32  34 35     37 38  40 41  43 44       46 47  49 50  52 53     55 56  58 59  61 62     64 65  67 68  70 71
        bgList = new ArrayList<TextureRegion>();    //3 times each to ensure speedy movement upwards, slower thru cities faster thru clouds
        Collections.addAll(bgList, bgCloudSeparatorTexture,  bgPhoenixtexture, bgPhoenixtexture2, bgCloudSeparatorTexture,  bgPhoenixtexture, bgPhoenixtexture2, bgCloudSeparatorTexture,  bgPhoenixtexture, bgPhoenixtexture2,
                bgCloudSeparatorTexture,  bgThundertexture, bgThundertexture2, bgCloudSeparatorTexture,  bgThundertexture, bgThundertexture2, bgCloudSeparatorTexture,  bgThundertexture, bgThundertexture2,
                bgCloudSeparatorTexture,  bgWatertexture, bgWatertexture2, bgCloudSeparatorTexture,  bgWatertexture, bgWatertexture2, bgCloudSeparatorTexture,  bgWatertexture, bgWatertexture2,
                bgCloudSeparatorTexture,  bgFiretexture, bgFiretexture2, bgCloudSeparatorTexture,  bgFiretexture, bgFiretexture2, bgCloudSeparatorTexture,  bgFiretexture, bgFiretexture2,
                bgCloudSeparatorTexture,  bgAcidtexture, bgAcidtexture2, bgCloudSeparatorTexture,  bgAcidtexture, bgAcidtexture2, bgCloudSeparatorTexture,  bgAcidtexture, bgAcidtexture2,
                bgCloudSeparatorTexture,  bgNighttexture, bgNighttexture2, bgCloudSeparatorTexture,  bgNighttexture, bgNighttexture2, bgCloudSeparatorTexture,  bgNighttexture, bgNighttexture2,
                bgCloudSeparatorTexture,  bgLunartexture, bgLunartexture2, bgCloudSeparatorTexture,  bgLunartexture, bgLunartexture2, bgCloudSeparatorTexture,  bgLunartexture, bgLunartexture2,
                bgCloudSeparatorTexture,  bgGoldtexture, bgGoldtexture2, bgCloudSeparatorTexture,  bgGoldtexture, bgGoldtexture2, bgCloudSeparatorTexture,  bgGoldtexture, bgGoldtexture2);



        TextureRegion coinTexture=tA.findRegion("coin");
        ArrayList<TextureRegion> tempPosition = new ArrayList<TextureRegion>(16);
        for (int i = 0; i < 17; i++) {
            if (i<16){
                TextureRegion temp = new TextureRegion(coinTexture, i*coinTexture.getRegionWidth()/16, 0, coinTexture.getRegionWidth()/16, coinTexture.getRegionWidth()/16);
                tempPosition.add(temp);
            }
        }
        coinAnimation=new Animation<TextureRegion>(0.03f, tempPosition.toArray(new TextureRegion[16]));
        coinAnimation.setPlayMode(Animation.PlayMode.LOOP); //REMEMBER THIS STEP

        phoenixAnims =birdTextureToAnimation("phoenix", 0.05f);
        thunderAnims =birdTextureToAnimation("thunder", 0.06f);
        waterAnims   =birdTextureToAnimation("water", 0.06f);
        fireAnims    =birdTextureToAnimation("fire", 0.06f);
        nightAnims   =birdTextureToAnimation("night", 0.04f);
        acidAnims    =birdTextureToAnimation("acid", 0.04f);
        lunarAnims   =birdTextureToAnimation("lunar", 0.04f);
        goldAnims    =birdTextureToAnimation("gold", 0.05f);


        tinyAnim1=tinyBirdTextureToAnimation("1");
        tinyAnim2=tinyBirdTextureToAnimation("2");
        tinyAnim3=tinyBirdTextureToAnimation("3");
        tinyAnim4=tinyBirdTextureToAnimation("4");
        tinyAnim5=tinyBirdTextureToAnimation("5");
        tinyAnim6=tinyBirdTextureToAnimation("6");
        tinyAnim7=tinyBirdTextureToAnimation("7");
        tinyAnim8=tinyBirdTextureToAnimation("8");
        tinyAnim9=tinyBirdTextureToAnimation("9");
        tinyAnim10=tinyBirdTextureToAnimation("10");
        tinyAnim11=tinyBirdTextureToAnimation("11");
        tinyAnims=new Animation[]{tinyAnim1,tinyAnim2,tinyAnim3,tinyAnim4,tinyAnim5,tinyAnim6,tinyAnim7,tinyAnim8, tinyAnim9, tinyAnim10, tinyAnim11};


        musiclist = new Music[] {manager.get(assets.music0)};

        prefs = Gdx.app.getPreferences("SkyDefenders");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }


        //flashShader.begin();

        //160*180, let player choose color but tint it in a batcher pass (batcher.setColorTint)
        /*airshipBalloon = manager.get("balloon");
        airshipSideThruster = manager.get("sideThruster");
        airshipBurnerPipe = manager.get("burnerPipes");
        reticle = manager.get("reticle");
        dragCircle=manager.get("dragCirc");
        dragLine=manager.get("dragLine");
        aimLine=manager.get("aimLine");*/



        menumusiclist = new Music[] {manager.get(assets.music0)};
        ranksList=ranksToList();

        TextureLoader.TextureParameter linearFilterParams=new TextureLoader.TextureParameter();
        linearFilterParams.minFilter=Texture.TextureFilter.Linear;linearFilterParams.magFilter=Texture.TextureFilter.Linear;
    }
    public static TextureRegion[] getRacks(String name){
        TextureRegion[] temp=new TextureRegion[7];
        for (int i=0;i<7;i++) {
            temp[i]=tA.findRegion(name+i);
        }
        return temp;
    }

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }
    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void stopMusic(Music[] list){
        if (list==musiclist){
            musiclist[musiciterator].pause();
        }else{
            menumusiclist[menumusiciterator].pause();
        }
    }
    public static void startMusic(Music[] list){
        if (list==musiclist){
            musiclist[musiciterator].play();
        }else{
            menumusiclist[menumusiciterator].play();
        }
    }
    public static void playnext(Music[] list) {
        if (list==musiclist){
            if (musiciterator<5){
                musiciterator++;
            } else{
                musiciterator=0;
            }
            list[musiciterator].setVolume(0.6f);
            list[musiciterator].play();
            list[musiciterator].setLooping(true);
        } else{ //menumusic
            /*if (menumusiciterator<1){
                menumusiciterator++;
                list[menumusiciterator].play();
                list[menumusiciterator].setLooping(true);
            } else{*/
            menumusiciterator=0;
            list[menumusiciterator].setVolume(0.6f);
            list[menumusiciterator].play();
            list[menumusiciterator].setLooping(true);
            //)
        }
    }

    @Override
    public void dispose() {
        // We must dispose of the texture when we are finished.
        manager.dispose();


        deathmenumusic.dispose();

        flashShader.dispose();
        for (Music music : musiclist){
            music.dispose();
        }
        for (Music music : menumusiclist){
            music.dispose();
        }
    }

    public TextureRegion turret(char type, int lvl, boolean proj) {
        if (!proj) return tA.findRegion(type+Integer.toString(lvl));
        else return tA.findRegion("p"+type+Integer.toString(lvl));
    }

    public static Sound turretSound(char type, int lvl) {
        return Gdx.audio.newSound(Gdx.files.internal("turretSound/"+type+lvl+".mp3"));
    }

    /*public static TextureRegion airshipRack(int armorLvl){
            return tA.findRegion("rack"+armorLvl);
    }

    public static TextureRegion armor(int armorLvl) {
        return tA.findRegion("armor"+armorLvl);//see if you can call rotatePixmap every time if rotated, but might not be efficient as might be only done for largest images on png and might want to add property to their objects (i.e. bg objects) instead
    }*/

    public   Animation<TextureRegion>[] birdTextureToAnimation(String path, float flapSpeed) {
        TextureRegion sprites = tA.findRegion(path);

        ArrayList<TextureRegion> poss = new ArrayList<TextureRegion>();
        ArrayList<TextureRegion> leftSidePositions = new ArrayList<TextureRegion>();

        TextureRegion[] front= new TextureRegion[0];
        TextureRegion[] rightSide=new TextureRegion[0];
        TextureRegion[] leftSide=new TextureRegion[0];
        TextureRegion[] back=new TextureRegion[0];



        for (int i=0;i<22;i++) {
            TextureRegion temp;
            if (i<11) {
                temp = new TextureRegion(sprites, 372 * i, 0, 372, 306);
            } else {
                temp = new TextureRegion(sprites, 372 * (i-11), 306, 372, 306);
            }
            poss.add(temp);

            if (i<6){
                TextureRegion flipTemp = new TextureRegion(sprites, 372 * i, 0, 373, 306);
                flipTemp.flip(true,false);
                leftSidePositions.add(flipTemp);
            }

            if (i == 5){
                rightSide=poss.toArray(new TextureRegion[6]);
                leftSide =leftSidePositions.toArray(new TextureRegion[6]);
                poss.clear();
                leftSidePositions.clear();
            } else if (i == 11) {
                back = poss.toArray(new TextureRegion[6]);
                poss.clear();
            } else if (i==21){
                front =  poss.toArray(new TextureRegion[10]);
            }
        }

        Animation frontFlaps= new Animation<TextureRegion>(flapSpeed, front);
        frontFlaps.setPlayMode(Animation.PlayMode.LOOP);

        Animation deathFlaps= new Animation<TextureRegion>(0.015f, front);
        deathFlaps.setPlayMode(Animation.PlayMode.LOOP);

        Animation rightFlaps= new Animation<TextureRegion>(flapSpeed, rightSide);
        rightFlaps.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Animation leftFlaps= new Animation<TextureRegion>(flapSpeed, leftSide);
        leftFlaps.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Animation backFlaps= new Animation<TextureRegion>(flapSpeed, back);
        backFlaps.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        return new Animation[]{frontFlaps, leftFlaps, rightFlaps, backFlaps, deathFlaps};
    }

    public Animation<TextureRegion> tinyBirdTextureToAnimation (String shadeNumber) {
        TextureRegion texture = tA.findRegion("tinyBird"+shadeNumber);
        ArrayList<TextureRegion> poss = new ArrayList<TextureRegion>(9);

        for (int i = 0; i < 9; i++) {
            TextureRegion temp = new TextureRegion(texture, 37 * i, 0, 37, 14);
            poss.add(temp);
        }
        Animation animation = new Animation<TextureRegion>(0.1f, poss.toArray((new TextureRegion[9])));
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        return animation;
    }
    public TextureRegion[] ranksToList () {
        TextureRegion[] ranksToList=new TextureRegion[858];//0-857 levels (858 levels including copper level 0) with all ranks combined
        for (int i = 1; i <= 11; i++) { //11 ranks
        TextureRegion texture = tA.findRegion("rank"+i);
            for (int k = 0; k < 6; k++) { //6 rows
                for (int j = 0; j < 13; j++) { //13 columns
                    ranksToList[((i-1)*78) + (k*13) + j]=new TextureRegion(texture,j*texture.getRegionWidth()/13,k*texture.getRegionHeight()/6,texture.getRegionWidth()/13,texture.getRegionHeight()/6);
                }
            }
        }
        return ranksToList;
    }
}