package com.kredatus.flockblockers.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.TweenAccessors.SpriteAccessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;


public class Loader implements Screen {
    public static Random r = new Random();
    public static int menumusiciterator, musiciterator;
    public static  Music[] musiclist, menumusiclist;
    public static Animation[] tinyAnims, phoenixAnims, nightAnims,waterAnims,fireAnims, acidAnims,thunderAnims,goldAnims,lunarAnims;

    public static TextureAtlas tA;

    public static TextureRegion coinSymbol;

    public static TextureRegion[] rackTextures = new TextureRegion[7];

    public static ArrayList<TextureRegion> bgList;

    //public static Sound splashdown, swoop, fire, birdHit, balloonHit;
    public static Music deathmenumusic;

    public static Animation<TextureRegion> rightSideFlaps, leftSideFlaps,
            tinyAnim1, tinyAnim2,tinyAnim3, tinyAnim4, tinyAnim5, tinyAnim6,tinyAnim7,tinyAnim8,tinyAnim9,tinyAnim10,tinyAnim11,
            coinAnimation;

    public static Preferences prefs;

    public static ShaderProgram flashShader;

    public static ParticleEffect burnerFire=new ParticleEffect(), thrusterFireLeft=new ParticleEffect(), thrusterFireRight=new ParticleEffect(); //thrusterFireUp=new ParticleEffect();
    public static Array<ParticleEffectPool.PooledEffect> additiveEffects = new Array<ParticleEffectPool.PooledEffect>(3), nonAdditiveEffects;
    public static Array<ParticleEmitter> emitters=new Array<ParticleEmitter>();
    public ProgressBar loadBar;

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
    public Loader(FlockBlockersMain game) {
        Texture.setAssetManager(manager);
        this.game = game;

        setupStage();
        assets.load();

        //niteSkin = manager.get(assets.niteRideUI);
        shadeSkin = manager.get(assets.shadeUI);

        //for the background
        //pbStyle.background.setLeftWidth(11);
        //pbStyle.knobBefore.setRightWidth(11);
        //niteSkin.getDrawable("progress-bar-red-h").setMinHeight(10);niteSkin.getDrawable("progress-bar-bg").setMinHeight(10);
        //shadeSkin.getDrawable("loading-bar-fill").setLeftWidth(20); shadeSkin.getDrawable("loading-bar-fill").setRightWidth(20);shadeSkin.getDrawable("loading-bar-fill").setMinWidth(500);
        //shadeSkin.getDrawable("loading-bar-fill").setLeftWidth(20);
        //TextureRegion region=shadeSkin.getRegion("loading-bar-fill");
        //shadeSkin.getTiledDrawable("loading-bar-fill").setRegion(region); //= new DrawableTegetRegion("loading-bar-fill").setRegionWidth(300);
        loadBar = new ProgressBar(0, 1, 0.001f, false, shadeSkin);
        loadBar.setColor(1,0,0,0.5f);
        loadBar.setAnimateDuration(0.1f);
        loadBar.setWidth(camWidth/1.1f);
        loadBar.setPosition((camWidth-loadBar.getWidth())/2,camHeight/5f);

        loadBar.setValue(1.5f);//3.2% is the minimum value right now
        stage.addActor(loadBar);
        //loadTable.setFillParent(true);

        //loadTable.add(loadBar).padTop(4*camHeight/5f);

        stage.addActor(loadBar);
    }

    private void setupStage(){
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        camHeight=1000;
        camWidth=(int)  (camHeight* (screenWidth/(float)screenHeight)) ;
        stage = new Stage(new ExtendViewport(camWidth,camHeight, new OrthographicCamera()), new SpriteBatch());
    }
    private void load(){

        if (!manager.update()){
            System.out.println(manager.getProgress());
        } else {
            loaded();
            stage.clear();
            gameHandler=new GameHandler(shadeSkin,camWidth,camHeight);
            game.setScreen(gameHandler);
        }
    }

    private void postLoad(){

    }

    @Override
    public void show() {



    }

    private void setupTween() {


        TweenCallback cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {

                System.out.println("new gamehandler");
            }
        };
//1.8 originally
        Tween.to(sprite, SpriteAccessor.ALPHA, 1f).target(1)
                .ease(TweenEquations.easeInOutQuad).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).repeatYoyo(1, .0f)
                .start();

        //Tween.to(sprite, SpriteAccessor.ALPHA, 2f).target(30)
        //.ease(TweenEquations.easeInOutQuad).repeatYoyo(1, .4f)
        //.start(manager);

    }

    

    @Override
    public void render(float delta) {

        load();
        loadBar.setValue(manager.getProgress()+0.03f);

        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

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
            emitters.add(i.getEmitters().get(0));
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
                TextureRegion temp = new TextureRegion(coinTexture, 32 * i, 0, 32, 32);
                tempPosition.add(temp);
            } else {
                coinSymbol= new TextureRegion(coinTexture, 32 * i, 0, 32, 32);
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

        for (int i=0;i<7;i++) {
            rackTextures[i]=tA.findRegion("rack"+i);
        }

        menumusiclist = new Music[] {manager.get(assets.music0)};

        TextureLoader.TextureParameter linearFilterParams=new TextureLoader.TextureParameter();
        linearFilterParams.minFilter=Texture.TextureFilter.Linear;linearFilterParams.magFilter=Texture.TextureFilter.Linear;
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
}
