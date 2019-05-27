// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Mr. Kredatus on 8/27/2017.
 **/

public class AssetHandler {/*
    public static Random r = new Random();
    public static int menumusiciterator, musiciterator;
    public static  Music[] musiclist, menumusiclist;
    public static Animation[] tinyAnims, phoenixAnimations, nightAnimations,waterAnimations,fireAnimations, acidAnimations,thunderAnimations,goldAnimations,lunarAnimations;

    public static TextureRegion
            bgPhoenixtexture, bgPhoenixtexture2, bgAcidtexture,bgAcidtexture2, bgFiretexture, bgFiretexture2, bgNighttexture, bgNighttexture2,bgGoldtexture, bgGoldtexture2,
            bgLunartexture, bgLunartexture2,bgThundertexture, bgThundertexture2,bgWatertexture, bgWatertexture2,horflipbgtexture, vertflipbgtexture, horvertflipbgtexture,
            boosttexture,boostdown,logo,slidemenuBg, menuButton, rateButton, shareButton, playdown, play, credits, creditsdown, exit, exitdown, retry, retrydown, ready, readydown, story, storydown, instr, instrdown, menu,
            menudown, score, rating, topscore, youvedied, newHighscore, creditsbg, deathmenubg, gliderbg, instrbg, readybg, next, nextdown, worldStabilized, coinSymbol, reticle,
            bgCloudSeparatorTexture, airshipBalloon, airshipSideThruster, airshipBurnerPipe, dragCircle, dragLine, aimLine;

    public static TextureRegion[] rackTextures = new TextureRegion[7];
    public static ArrayList<Vector3> boostcoords;
    public static ArrayList<TextureRegion> bgList;
    public static int bgscaling, gliderscaling, boostnumber, coordslistsize;
    public static Sound splashdown, swoop, fire, birdHit, balloonHit;
    public static Music deathmenumusic;

    public static Animation<TextureRegion> rightSideFlaps, leftSideFlaps,
            tinyAnim1, tinyAnim2,tinyAnim3, tinyAnim4, tinyAnim5, tinyAnim6,tinyAnim7,tinyAnim8,tinyAnim9,tinyAnim10,tinyAnim11,
            coinAnimation;
    //public static TextureRegion gliderMid, gliderDown, gliderUp ,vertflipgliderMid, vertflipgliderDown, vertflipgliderUp,
    //frontGliderMid, frontGliderDown, frontGliderUp, frontGliderUpHigh, backgliderMid, backgliderDown, backgliderUp;

    public static Preferences prefs;

    public static ShaderProgram flashShader;

    public static ParticleEffect burnerFire=new ParticleEffect(), thrusterFireLeft=new ParticleEffect(), thrusterFireRight=new ParticleEffect(); //thrusterFireUp=new ParticleEffect();
    public static Array<ParticleEffectPool.PooledEffect> additiveEffects = new Array<ParticleEffectPool.PooledEffect>(3), nonAdditiveEffects;
    public static Array<ParticleEmitter> emitters=new Array<ParticleEmitter>();
*/


    public  AssetManager manager= new AssetManager();

    public   String textures="textures/texturePack.txt",
    burnerFire="effects/burnerFire.p", thrusterFireLeft="effects/thrusterFireLeft.p",thrusterFireRight="effects/thrusterFireRight.p",
    fire="sound/fire.wav", swoop="sound/swoop.wav",birdHit="sound/birdHit.mp3",balloonHit="sound/balloonHit.mp3",
    music0="music/music0.mp3",
    skin="ui/shadeui/uiskin.json",
    flashShader="shaders/flash.vert";

    public void load() {

        manager.load(skin, Skin.class);//syncronous loading, done on loader line assets.load();
        manager.finishLoading();

        manager.load(burnerFire, ParticleEffect.class);//asyncronous loading, continued along using update(); in loader
        manager.load(thrusterFireLeft,ParticleEffect.class);
        manager.load(thrusterFireRight,ParticleEffect.class);

        manager.load(fire, Sound.class);
        manager.load(swoop,Sound.class);
        manager.load(birdHit,Sound.class);
        manager.load(balloonHit,Sound.class);

        manager.load(music0, Music.class);

        ShaderProgram.pedantic = false;
        manager.load(flashShader,ShaderProgram.class);


        manager.load(textures, TextureAtlas.class);


        //textureAtlas.findRegion()
        //If your particle effect includes additive or pre-multiplied particle emitters
        //you can turn off blend function clean-up to save a lot of draw calls, but
        //remember to switch the Batch back to GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA
        //before drawing "regular" sprites or your Stage.


        // Create (or retrieve existing) preferences file




    }


/*
    public static Animation[] birdTextureToSprite (Texture texture) {
        ArrayList<TextureRegion> poss = new ArrayList<TextureRegion>();

        TextureRegion[] front = new TextureRegion[0];
        TextureRegion[] side = new TextureRegion[0];
        TextureRegion[] back = new TextureRegion[0];

        for (int i = 0; i < 16; i++) {
            TextureRegion temp = manager.get()texture, 481 * i, 0, 481, 423);
            poss.add(temp);
            if (i == 5) {
                front = poss.toArray(new TextureRegion[6]);
                poss.clear();
            } else if (i == 11) {
                side = poss.toArray(new TextureRegion[6]);
                poss.clear();
                if (texture != phoenixBird) {
                    break;
                }
            } else if (i == 15) {
                back = poss.toArray(new TextureRegion[4]);
                poss.clear();
            }
        }

        frontFlaps = new Animation<TextureRegion>(0.15f, front);
        frontFlaps.setPlayMode(Animation.PlayMode.LOOP);

        rightSideFlaps = new Animation<TextureRegion>(0.12f, side);
        rightSideFlaps.setPlayMode(Animation.PlayMode.LOOP);
        for (TextureRegion i : side) {
            i.flip(true, false);
        }
        leftSideFlaps = new Animation<TextureRegion>(0.12f, side);
        leftSideFlaps.setPlayMode(Animation.PlayMode.LOOP);

        Animation[] animationList;
        if (texture==phoenixBird){
            backFlaps = new Animation<TextureRegion>(0.12f, back);
            backFlaps.setPlayMode(Animation.PlayMode.LOOP);
            animationList= new Animation[]{frontFlaps, rightSideFlaps, leftSideFlaps, backFlaps};
        } else {
            animationList = new Animation[]{frontFlaps, rightSideFlaps, leftSideFlaps};
        }
        return animationList;

    }*/


}