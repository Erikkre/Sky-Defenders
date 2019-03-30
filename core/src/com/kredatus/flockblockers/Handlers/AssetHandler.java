// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Mr. Kredatus on 8/27/2017.
 **/

public class AssetHandler {
    public static Random r = new Random();
    public static int menumusiciterator, musiciterator;
    public static  Music[] musiclist, menumusiclist;
    public static Animation[] tinyAnims, phoenixAnimations, nightAnimations,waterAnimations,fireAnimations, acidAnimations,thunderAnimations,goldAnimations,lunarAnimations;
    public static Texture sprites, texture, f0Texture, f1Texture,  f2Texture, f3Texture, f4Texture, f5Texture, f6Texture, f7Texture, f8Texture, f9Texture,
            bgPhoenix,bgNight,bgWater,bgAcid,bgFire,bgThunder,bgLunar,bgGold, phoenixBird, nightBird, acidBird, waterBird, thunderBird, fireBird, goldBird, lunarBird,
            boost, boostdowntexture, logoTexture, playtexture, playdowntexture, newHighscoretexture, creditstexture, creditsdowntexture, exittexture, exitdowntexture,
            retrytexture, retrydowntexture, readytexture, readydowntexture, storytexture, storydowntexture, scoretexture, ratingtexture, topscoretexture, menutexture,
            menudowntexture, youvediedtexture, creditsbgtexture, deathmenubgtexture, instrtexture, instrdowntexture, gliderbgtexture, instrbgtexture, readybgtexture,
            nexttexture, nextdowntexture, worldStabilizedtexture, reticleTexture, airshipBalloonTexture, airshipSideThrusterTexture, airshipBurnerPipeTexture;
    public static TextureRegion f0, f0Proj, f1, f1Proj, f2, f2Proj,f3, f3Proj,f4, f4Proj,f5, f5Proj,f6, f6Proj,f7, f7Proj,f8, f8Proj,f9, f9Proj,
            s0, s0Proj,s1, s1Proj,s2, s2Proj,s3, s3Proj,s4, s4Proj,s5, s5Proj,s6, s6Proj,s7, s7Proj,s8, s8Proj,s9, s9Proj,
            d0, d0Proj,d1, d1Proj,d2, d2Proj,d3, d3Proj,d4, d4Proj,d5, d5Proj,d6, d6Proj,d7, d7Proj,d8, d8Proj,d9, d9Proj,
            bgPhoenixtexture, bgPhoenixtexture2, bgAcidtexture,bgAcidtexture2, bgFiretexture, bgFiretexture2, bgNighttexture, bgNighttexture2,bgGoldtexture, bgGoldtexture2,
            bgLunartexture, bgLunartexture2,bgThundertexture, bgThundertexture2,bgWatertexture, bgWatertexture2,horflipbgtexture, vertflipbgtexture, horvertflipbgtexture,
            boosttexture,boostdown,logo, playdown, play, credits, creditsdown, exit, exitdown, retry, retrydown, ready, readydown, story, storydown, instr, instrdown, menu,
            menudown, score, rating, topscore, youvedied, newHighscore, creditsbg, deathmenubg, gliderbg, instrbg, readybg, next, nextdown, worldStabilized, coinSymbol, reticle,
            bgCloudSeparatorTexture, airshipBalloon, airshipSideThruster, airshipBurnerPipe;

    public static ArrayList<Vector3> boostcoords;
    public static ArrayList<TextureRegion> bgList;
    public static int bgscaling, gliderscaling, boostnumber, coordslistsize;
    public static Sound splashdown, swoop, fire;
    public static Music deathmenumusic;
    public static Vector3 p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19,
            p20, p21, p22, p23, p24, p25, p26, p27, p28, p29, p30, p31;

    public static Animation<TextureRegion> rightSideFlaps, leftSideFlaps,
            tinyAnim1, tinyAnim2,tinyAnim3, tinyAnim4, tinyAnim5, tinyAnim6,tinyAnim7,tinyAnim8,tinyAnim9,tinyAnim10,tinyAnim11,
            coinAnimation;
    //public static TextureRegion gliderMid, gliderDown, gliderUp ,vertflipgliderMid, vertflipgliderDown, vertflipgliderUp,
            //frontGliderMid, frontGliderDown, frontGliderUp, frontGliderUpHigh, backgliderMid, backgliderDown, backgliderUp;

    public static Preferences prefs;

    public static ShaderProgram flashShader;

    public static ParticleEffect burnerFire=new ParticleEffect(), thrusterFireLeft=new ParticleEffect(), thrusterFireRight=new ParticleEffect(); //thrusterFireUp=new ParticleEffect();
    public static ParticleEffectPool burnerFirePool, thrusterFireLeftPool, thrusterFireRightPool;
    public static Array<PooledEffect> additiveEffects = new Array<PooledEffect>(), nonAdditiveEffects;
    public static Array<ParticleEmitter> emitters=new Array<ParticleEmitter>();
    public static void load() {
        //If your particle effect includes additive or pre-multiplied particle emitters
//you can turn off blend function clean-up to save a lot of draw calls, but
//remember to switch the Batch back to GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA
//before drawing "regular" sprites or your Stage.

        burnerFire.load(Gdx.files.internal("effects"+File.separator+"burnerFire.p"),Gdx.files.internal("particles"));
        //burnerFire.setEmittersCleanUpBlendFunction(false);//Stop the additive effect resetting, speeding up batcher
        burnerFirePool= new ParticleEffectPool(burnerFire,1,2);
        PooledEffect pooledEffect=burnerFirePool.obtain();
        additiveEffects.add(pooledEffect);

        /*thrusterFireUp.load(Gdx.files.internal("effects"+File.separator+"thrusterFireUp.p"),Gdx.files.internal("particles"));
        //thrusterFireUp.setEmittersCleanUpBlendFunction(false);//Stop the additive effect resetting, speeding up batcher
        thrusterFireUpPool= new ParticleEffectPool(thrusterFireUp,1,2);
        pooledEffect=thrusterFireUpPool.obtain();
        additiveEffects.add(pooledEffect);*/

        thrusterFireLeft.load(Gdx.files.internal("effects"+File.separator+"thrusterFireLeft.p"),Gdx.files.internal("particles"));
        //thrusterFireLeft.setEmittersCleanUpBlendFunction(false);//Stop the additive effect resetting, speeding up batcher
        thrusterFireLeftPool= new ParticleEffectPool(thrusterFireLeft,1,2);
        pooledEffect=thrusterFireLeftPool.obtain();
        additiveEffects.add(pooledEffect);

        thrusterFireRight.load(Gdx.files.internal("effects"+File.separator+"thrusterFireRight.p"),Gdx.files.internal("particles"));
        //thrusterFireRight.setEmittersCleanUpBlendFunction(false);//Stop the additive effect resetting, speeding up batcher
        thrusterFireRightPool= new ParticleEffectPool(thrusterFireRight,1,2);
        pooledEffect=thrusterFireRightPool.obtain();
        additiveEffects.add(pooledEffect);

        for (PooledEffect i: additiveEffects){
            emitters.add(i.getEmitters().get(0));
        }

        menumusiciterator = r.nextInt(3);
        musiciterator = r.nextInt(6);

        //button textures
        playtexture = new Texture(Gdx.files.internal("ui"+File.separator+"play.png"));
        playtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        playdowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"playdown.png"));
        playdowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        playdown = new TextureRegion(playdowntexture, 0, 0, 347, 220);
        play = new TextureRegion(playtexture, 0, 0, 347, 220);
        play.flip(false, true);
        playdown.flip(false, true);

        creditstexture = new Texture(Gdx.files.internal("ui"+File.separator+"credits.png"));
        creditstexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        creditsdowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"creditsdown.png"));
        creditsdowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        credits = new TextureRegion(creditstexture, 0, 0, 536, 221);
        credits.flip(false, true);
        creditsdown = new TextureRegion(creditsdowntexture, 0, 0, 536, 221);
        creditsdown.flip(false, true);

        exittexture = new Texture(Gdx.files.internal("ui"+File.separator+"exit.png"));
        exittexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        exitdowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"exitdown.png"));
        exitdowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        exit = new TextureRegion(exittexture, 0, 0, 350, 221);
        exit.flip(false, true);
        exitdown = new TextureRegion(exitdowntexture, 0, 0, 350, 221);
        exitdown.flip(false, true);

        retrytexture = new Texture(Gdx.files.internal("ui"+File.separator+"retry.png"));
        retrytexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        retrydowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"retrydown.png"));
        retrydowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        retry = new TextureRegion(retrytexture, 0, 0, 465, 222);
        retry.flip(false, true);
        retrydown = new TextureRegion(retrydowntexture, 0, 0, 465, 222);
        retrydown.flip(false, true);

        readytexture = new Texture(Gdx.files.internal("ui"+File.separator+"ready.png"));
        readytexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        readydowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"readydown.png"));
        readydowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        ready = new TextureRegion(readytexture, 0, 0, 495, 222);
        ready.flip(false, true);
        readydown = new TextureRegion(readydowntexture, 0, 0, 495, 222);
        readydown.flip(false, true);

        storytexture = new Texture(Gdx.files.internal("ui"+File.separator+"story.png"));
        storytexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        storydowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"storydown.png"));
        storydowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        story = new TextureRegion(storytexture, 0, 0, 426, 221);
        story.flip(false, true);
        storydown = new TextureRegion(storydowntexture, 0, 0, 426, 221);
        storydown.flip(false, true);

        instrtexture = new Texture(Gdx.files.internal("ui"+File.separator+"instr.png"));
        instrtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        instrdowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"instrdown.png"));
        instrdowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        instr = new TextureRegion(instrtexture, instrtexture.getWidth(), instrtexture.getHeight());
        instr.flip(false, true);
        instrdown = new TextureRegion(instrdowntexture, instrdowntexture.getWidth(), instrdowntexture.getHeight());
        instrdown.flip(false, true);

        menutexture = new Texture(Gdx.files.internal("ui"+File.separator+"menu.png"));
        menutexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        menudowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"menudown.png"));
        menudowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        menu = new TextureRegion(menutexture, 0, 0, 409, 221);
        menu.flip(false, true);
        menudown = new TextureRegion(menudowntexture, 0, 0, 409, 221);
        menudown.flip(false, true);

        nexttexture = new Texture(Gdx.files.internal("ui"+File.separator+"next.png"));
        nexttexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        nextdowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"nextdown.png"));
        nextdowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        nextdown = new TextureRegion(nextdowntexture, 0, 0, 347, 220);
        next = new TextureRegion(nexttexture, 0, 0, 347, 220);
        next.flip(false, true);
        nextdown.flip(false, true);

        //non-button textures *************************************************************************************************
        worldStabilizedtexture = new Texture(Gdx.files.internal("ui"+File.separator+"worldStabilized.png"));
        worldStabilizedtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        worldStabilized = new TextureRegion(worldStabilizedtexture, worldStabilizedtexture.getWidth(), worldStabilizedtexture.getHeight());
        worldStabilized.flip(false, true);

        logoTexture = new Texture(Gdx.files.internal("backgrounds"+File.separator+"companyLogo.jpg"));
        logoTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        logo = new TextureRegion(logoTexture, 0, 0, 1100, 800);

        newHighscoretexture = new Texture(Gdx.files.internal("ui"+File.separator+"newHighscore.png"));
        newHighscoretexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        newHighscore = new TextureRegion(newHighscoretexture, 0, 0, 644, 184);
        newHighscore.flip(false, true);

        youvediedtexture = new Texture(Gdx.files.internal("ui"+File.separator+"youveDied.png"));
        youvediedtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        youvedied = new TextureRegion(youvediedtexture, 0, 0, 782, 182);
        youvedied.flip(false, true);

        scoretexture = new Texture(Gdx.files.internal("ui"+File.separator+"score.png"));
        scoretexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        score = new TextureRegion(scoretexture, 0, 0, 446, 221);
        score.flip(false, true);

        ratingtexture = new Texture(Gdx.files.internal("ui"+File.separator+"rating.png"));
        ratingtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        rating = new TextureRegion(ratingtexture, 0, 0, 507, 221);
        rating.flip(false, true);

        topscoretexture = new Texture(Gdx.files.internal("ui"+File.separator+"topscore.png"));
        topscoretexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        topscore = new TextureRegion(topscoretexture, 0, 0, 680, 221);
        topscore.flip(false, true);

        //BACKGROUNDS **************************************************************************************************************
        gliderbgtexture = new Texture(Gdx.files.internal("backgrounds"+File.separator+"gliderbg.png"));
        gliderbgtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        gliderbg = new TextureRegion(gliderbgtexture, gliderbgtexture.getWidth(), gliderbgtexture.getHeight());
        gliderbg.flip(false, true);

        /*creditsbgtexture = new Texture(Gdx.files.internal("backgrounds"+File.separator+"creditsbg.jpg"));
        creditsbgtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        creditsbg = new TextureRegion(creditsbgtexture, creditsbgtexture.getWidth(), creditsbgtexture.getHeight());
        creditsbg.flip(false, true);

        deathmenubgtexture = new Texture(Gdx.files.internal("backgrounds"+File.separator+"deathmenubg.png"));
        deathmenubgtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        deathmenubg = new TextureRegion(deathmenubgtexture, deathmenubgtexture.getWidth(), deathmenubgtexture.getHeight());
        deathmenubg.flip(false, true);

        instrbgtexture = new Texture(Gdx.files.internal("backgrounds"+File.separator+"instrbg.png"));
        instrbgtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        instrbg = new TextureRegion(instrbgtexture, instrbgtexture.getWidth(), instrbgtexture.getHeight());
        instrbg.flip(false, true);

        readybgtexture = new Texture(Gdx.files.internal("backgrounds"+File.separator+"readybg.png"));
        readybgtexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        readybg = new TextureRegion(readybgtexture, readybgtexture.getWidth(), readybgtexture.getHeight());
        readybg.flip(false, true);*/




        Texture bgCloudSeparator= new Texture(Gdx.files.internal("backgrounds"+File.separator+"levels"+File.separator+"cloudSeparator.jpg"));
        bgCloudSeparator.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bgCloudSeparatorTexture = new TextureRegion(bgCloudSeparator, bgCloudSeparator.getWidth(), bgCloudSeparator.getHeight());


        bgPhoenix = new Texture(Gdx.files.internal("backgrounds"+File.separator+"levels"+File.separator+"bgPhoenix2.jpg"));
        bgPhoenix.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bgPhoenixtexture = new TextureRegion(bgPhoenix, bgPhoenix.getWidth(), bgPhoenix.getHeight());
        bgPhoenixtexture.flip(false, true);
        bgPhoenixtexture2 = new TextureRegion(bgPhoenix, bgPhoenix.getWidth(), bgPhoenix.getHeight());


        bgThunder = new Texture(Gdx.files.internal("backgrounds"+File.separator+"levels"+File.separator+"bgThunder2.jpg"));
        bgThunder.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bgThundertexture = new TextureRegion(bgThunder, bgThunder.getWidth(), bgThunder.getHeight());
        bgThundertexture.flip(false, true);
        bgThundertexture2 = new TextureRegion(bgThunder, bgThunder.getWidth(), bgThunder.getHeight());


        bgWater = new Texture(Gdx.files.internal("backgrounds"+File.separator+"levels"+File.separator+"bgWater2.jpg"));
        bgWater.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bgWatertexture = new TextureRegion(bgWater, bgWater.getWidth(), bgWater.getHeight());
        bgWatertexture.flip(false, true);
        bgWatertexture2 = new TextureRegion(bgWater, bgWater.getWidth(), bgWater.getHeight());


        bgFire = new Texture(Gdx.files.internal("backgrounds"+File.separator+"levels"+File.separator+"bgFire2.jpg"));
        bgFire.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bgFiretexture = new TextureRegion(bgFire, bgFire.getWidth(), bgFire.getHeight());
        bgFiretexture.flip(false, true);
        bgFiretexture2 = new TextureRegion(bgFire, bgFire.getWidth(), bgFire.getHeight());


        bgAcid = new Texture(Gdx.files.internal("backgrounds"+File.separator+"levels"+File.separator+"bgAcid2.jpg"));
        bgAcid.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bgAcidtexture = new TextureRegion(bgAcid, bgAcid.getWidth(), bgAcid.getHeight());
        bgAcidtexture.flip(false, true);
        bgAcidtexture2 = new TextureRegion(bgAcid, bgAcid.getWidth(), bgAcid.getHeight());


        bgNight = new Texture(Gdx.files.internal("backgrounds"+File.separator+"levels"+File.separator+"bgNight2.jpg"));
        bgNight.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bgNighttexture = new TextureRegion(bgNight, bgNight.getWidth(), bgNight.getHeight());
        bgNighttexture.flip(false, true);
        bgNighttexture2 = new TextureRegion(bgNight, bgNight.getWidth(), bgNight.getHeight());


        bgLunar = new Texture(Gdx.files.internal("backgrounds"+File.separator+"levels"+File.separator+"bgLunar2.jpg"));
        bgLunar.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bgLunartexture = new TextureRegion(bgLunar, bgLunar.getWidth(), bgLunar.getHeight());
        bgLunartexture.flip(false, true);
        bgLunartexture2 = new TextureRegion(bgLunar, bgLunar.getWidth(), bgLunar.getHeight());


        bgGold = new Texture(Gdx.files.internal("backgrounds"+File.separator+"levels"+File.separator+"bgGold2.jpg"));
        bgGold.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bgGoldtexture = new TextureRegion(bgGold, bgGold.getWidth(), bgGold.getHeight());
        bgGoldtexture.flip(false, true);
        bgGoldtexture2 = new TextureRegion(bgGold, bgGold.getWidth(), bgGold.getHeight());

        //               9                       18                      27                      36                      45                        54                      63
        //1 2  4 5  7 8     10 11  13 14  16 17     19 20  22 23  25 26     28 29  31 32  34 35     37 38  40 41  43 44       46 47  49 50  52 53     55 56  58 59  61 62     64 65  67 68  70 71
        bgList = new ArrayList<TextureRegion>();    //3 times each to ensure speedy movement upwards, slower thru cities faster thru clouds
        Collections.addAll(bgList, bgCloudSeparatorTexture,  bgPhoenixtexture, bgPhoenixtexture2, bgCloudSeparatorTexture,  bgPhoenixtexture, bgPhoenixtexture2, bgCloudSeparatorTexture,  bgPhoenixtexture, bgPhoenixtexture2,
                bgCloudSeparatorTexture,  bgThundertexture, bgThundertexture2, bgCloudSeparatorTexture,  bgThundertexture, bgThundertexture2, bgCloudSeparatorTexture,  bgThundertexture, bgThundertexture2,
                bgCloudSeparatorTexture,  bgWatertexture, bgWatertexture2, bgCloudSeparatorTexture,  bgWatertexture, bgWatertexture2, bgCloudSeparatorTexture,  bgWatertexture, bgWatertexture2,
                bgCloudSeparatorTexture,  bgFiretexture, bgFiretexture2, bgCloudSeparatorTexture,  bgFiretexture, bgFiretexture2, bgCloudSeparatorTexture,  bgFiretexture, bgFiretexture2,
                bgCloudSeparatorTexture,  bgAcidtexture, bgAcidtexture2, bgCloudSeparatorTexture,  bgAcidtexture, bgAcidtexture2, bgCloudSeparatorTexture,  bgAcidtexture, bgAcidtexture2,
                bgCloudSeparatorTexture,  bgNighttexture, bgNighttexture2, bgCloudSeparatorTexture,  bgNighttexture, bgNighttexture2, bgCloudSeparatorTexture,  bgNighttexture, bgNighttexture2,
                bgCloudSeparatorTexture,  bgLunartexture, bgLunartexture2, bgCloudSeparatorTexture,  bgLunartexture, bgLunartexture2, bgCloudSeparatorTexture,  bgLunartexture, bgLunartexture2,
                bgCloudSeparatorTexture,  bgGoldtexture, bgGoldtexture2, bgCloudSeparatorTexture,  bgGoldtexture, bgGoldtexture2, bgCloudSeparatorTexture,  bgGoldtexture, bgGoldtexture2);

                /*
        coordslistsize=22;
        boostnumber=20; //14 boosts for 2 map sizes: 50% random chance of each boost being rendered in its coordinate
        ArrayList<Vector3> boostcoordslist = new ArrayList<Vector3>();      //there will be 7 variables for this map

        //Coords for locations of boost for each map, 3rd coord is size: (3 charges) 6=large, 20=medium, 40=small, (1 charge)
        p0  = new Vector3(451,694,20);
        p1  = new Vector3(490,230,40);
        p2  = new Vector3(600,230,40);
        p3  = new Vector3(690,260,40);
        p4  = new Vector3(750,330,40);
        p5  = new Vector3(1040,470,20);
        p6  = new Vector3(1330,660,20);
        p7  = new Vector3(1570,730,20);
        p8  = new Vector3(1590,458,40);
        p9  = new Vector3(1680,400,40);
        p10 = new Vector3(1820,350,40);
        p11 = new Vector3(1930,385,40);
        p12 = new Vector3(2048,1070,6);
        p13 = new Vector3(2200,280,40);
        p14 = new Vector3(2400,230,40);
        p15 = new Vector3(2470,790,20);
        p16 = new Vector3(2570,330,40);
        p17 = new Vector3(2800,540,20);
        p18 = new Vector3(3390,300,40);
        p19 = new Vector3(3470,240,40);
        p20 = new Vector3(3610,210,40);
        p21 = new Vector3(3740,260,40);


        Collections.addAll(boostcoordslist, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23, p24, p25, p26, p27, p28, p29, p30, p31);
        boostcoords=boostcoordslist;
*/
        //SPRITEWORK
        gliderscaling = 1;
        boost = new Texture(Gdx.files.internal("sprites"+File.separator+"boost.png"));
        boost.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        boosttexture = new TextureRegion(boost, boost.getWidth(), boost.getHeight());
        boosttexture.flip(false, true);
        boostdowntexture = new Texture(Gdx.files.internal("ui"+File.separator+"boostdown.png"));
        boostdowntexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        boostdown = new TextureRegion(boostdowntexture, boostdowntexture.getWidth(), boostdowntexture.getHeight());
        boostdown.flip(false, true);

        f0Texture= new Texture(Gdx.files.internal("sprites"+File.separator+"turrets"+File.separator+"f0.png"));
        f0Texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        //turret.flip(false, true);
        f0 = new TextureRegion(f0Texture,0, 4,32,13);
        f0Proj = new TextureRegion(f0Texture,0, 0,32,3);

        Texture coinTexture=new Texture(Gdx.files.internal("flockBlockersExtra"+File.separator+"bayatGames"+File.separator+"coin.png"));
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


        phoenixAnimations = birdTextureToAnimation("phoenix.png", 0.05f);

        thunderAnimations = birdTextureToAnimation("thunder.png", 0.06f);
        waterAnimations   = birdTextureToAnimation("water.png", 0.06f);
        fireAnimations    = birdTextureToAnimation("fire.png", 0.06f);

        nightAnimations   = birdTextureToAnimation("night.png", 0.04f);
        acidAnimations    = birdTextureToAnimation("acid.png", 0.04f);
        lunarAnimations   = birdTextureToAnimation("lunar.png", 0.04f);

        goldAnimations    = birdTextureToAnimation("gold.png", 0.05f);


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

        //SOUNDWORK
        fire = Gdx.audio.newSound(Gdx.files.internal("sound"+File.separator+"fire.wav"));
        splashdown = Gdx.audio.newSound(Gdx.files.internal("sound"+File.separator+"splashdown.wav"));
        swoop = Gdx.audio.newSound(Gdx.files.internal("sound"+File.separator+"swoop.wav"));

        musiclist = new Music[] { Gdx.audio.newMusic(Gdx.files.internal("music"+File.separator+"bgMusic.mp3")), Gdx.audio.newMusic(Gdx.files.internal("music"+File.separator+"bgMusic2.mp3")),
                Gdx.audio.newMusic(Gdx.files.internal("music"+File.separator+"bgMusic3.mp3")), Gdx.audio.newMusic(Gdx.files.internal("music"+File.separator+"bgMusic4.mp3")), Gdx.audio.newMusic(Gdx.files.internal("music"+File.separator+"bgMusic5.mp3")), Gdx.audio.newMusic(Gdx.files.internal("music"+File.separator+"bgMusic6.mp3"))};
        menumusiclist = new Music[] {Gdx.audio.newMusic(Gdx.files.internal("music"+File.separator+"menuMusic.mp3"))};//, Gdx.audio.newMusic(Gdx.files.internal("music"+File.separator+"menuMusic2.mp3"))};
        deathmenumusic = Gdx.audio.newMusic(Gdx.files.internal("music"+File.separator+"deathmenuMusic.mp3"));

        // Create (or retrieve existing) preferences file
        prefs = Gdx.app.getPreferences("GlideorDie");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        ShaderProgram.pedantic = false;
        flashShader = new ShaderProgram(Gdx.files.internal(
                "shaders"+File.separator+"flash-vert.glsl").readString(),
                Gdx.files.internal("shaders"+ File.separator+"flash-frag.glsl").readString());
       //flashShader.begin();
        if (!flashShader.isCompiled()) {
            System.err.println(flashShader.getLog());
            System.exit(0);
        }

        if (flashShader.getLog().length()!=0){
            System.out.println(flashShader.getLog());
        }

        //160*180, let player choose color but tint it in a batcher pass (batcher.setColorTint)
        airshipBalloonTexture = new Texture(Gdx.files.internal("sprites"+File.separator+"balloons"+File.separator+"balloon.png"));
        airshipBalloonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        airshipBalloon = new TextureRegion(airshipBalloonTexture);

        airshipSideThrusterTexture = new Texture(Gdx.files.internal("sprites"+File.separator+"balloons"+File.separator+"sideThruster.png"));
        airshipSideThrusterTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        airshipSideThruster = new TextureRegion(airshipSideThrusterTexture, airshipSideThrusterTexture.getWidth(), airshipSideThrusterTexture.getHeight());

        airshipBurnerPipeTexture = new Texture(Gdx.files.internal("sprites"+File.separator+"balloons"+File.separator+"burnerPipes.png"));
        airshipBurnerPipeTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        airshipBurnerPipe = new TextureRegion(airshipBurnerPipeTexture);
    }

    public static TextureRegion airshipRack(int armorLvl){
            Texture rack = new Texture(Gdx.files.internal("sprites"+File.separator+"balloons"+File.separator+"rack"+armorLvl+".png"));
            rack.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            return new TextureRegion(rack);
    }

    public static TextureRegion armor(int armorLvl) {
        Texture armor = new Texture(Gdx.files.internal("sprites"+File.separator+"balloons"+File.separator+"armor"+armorLvl+".png"));
        armor.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return new TextureRegion(armor);
    }

    private static Animation<TextureRegion>[] birdTextureToAnimation(String path, float flapSpeed) {
        sprites = new Texture(Gdx.files.internal("sprites"+File.separator+"birds"+File.separator+""+path));
        sprites.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        ArrayList<TextureRegion> poss = new ArrayList<TextureRegion>();
        ArrayList<TextureRegion> leftSidePositions = new ArrayList<TextureRegion>();

        TextureRegion[] front=new TextureRegion[0];
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

    private static Animation<TextureRegion> tinyBirdTextureToAnimation (String shadeNumber) {
        texture = new Texture(Gdx.files.internal("sprites"+File.separator+"tinyBirds"+File.separator+"tinyBird"+shadeNumber+".png"));
        ArrayList<TextureRegion> poss = new ArrayList<TextureRegion>(9);

        for (int i = 0; i < 9; i++) {
            TextureRegion temp = new TextureRegion(texture, 37 * i, 0, 37, 14);
            poss.add(temp);
        }
        Animation animation = new Animation<TextureRegion>(0.1f, poss.toArray((new TextureRegion[9])));
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        return animation;
    }
/*
    public static Animation[] birdTextureToSprite (Texture texture) {
        ArrayList<TextureRegion> poss = new ArrayList<TextureRegion>();

        TextureRegion[] front = new TextureRegion[0];
        TextureRegion[] side = new TextureRegion[0];
        TextureRegion[] back = new TextureRegion[0];

        for (int i = 0; i < 16; i++) {
            TextureRegion temp = new TextureRegion(texture, 481 * i, 0, 481, 423);
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
    public static int getbgScaling(){
        return bgscaling;
    }
    public static int getgliderScaling(){
        return gliderscaling;
    }
    public static int getBoostnumber(){return boostnumber;}
    public static int getcoordslistsize(){return coordslistsize;}
    public static ArrayList<Vector3> getBoostcoords() {return boostcoords;}

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }
    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void stopmusic(Music[] list){
        if (list==musiclist){
            musiclist[musiciterator].stop();
        }else{
            menumusiclist[menumusiciterator].stop();
        }
    }

    public static void playnext(Music[] list){
        if (list==musiclist){
            if (musiciterator<5){
                musiciterator++;
            } else{
                musiciterator=0;
            }
            list[musiciterator].play();
            list[musiciterator].setLooping(true);
        } else{ //menumusic
            /*if (menumusiciterator<1){
                menumusiciterator++;
                list[menumusiciterator].play();
                list[menumusiciterator].setLooping(true);
            } else{*/
                menumusiciterator=0;
                list[menumusiciterator].play();
                list[menumusiciterator].setLooping(true);
            //)
        }
    }

    public static void dispose() {
        // We must dispose of the texture when we are finished.
        bgPhoenix.dispose();
        bgWater.dispose();
        bgAcid.dispose();
        bgFire.dispose();
        bgGold.dispose();
        bgLunar.dispose();
        bgThunder.dispose();
        bgNight.dispose();

        sprites.dispose();
        boost.dispose();

        playtexture.dispose();
        playdowntexture.dispose();
        boostdowntexture.dispose();
        logoTexture.dispose();
        exittexture.dispose();
        exitdowntexture.dispose();
        menutexture.dispose();
        menudowntexture.dispose();
        youvediedtexture.dispose();
        newHighscoretexture.dispose();
        storytexture.dispose();
        storydowntexture.dispose();
        creditstexture.dispose();
        creditsdowntexture.dispose();
        scoretexture.dispose();
        topscoretexture.dispose();
        ratingtexture.dispose();
        retrytexture.dispose();
        readydowntexture.dispose();
        deathmenubgtexture.dispose();
        creditsbgtexture.dispose();
        gliderbgtexture.dispose();
        readybgtexture.dispose();
        readytexture.dispose();
        readydowntexture.dispose();
        nexttexture.dispose();
        nextdowntexture.dispose();
        texture.dispose();

        splashdown.dispose();
        swoop.dispose();
        fire.dispose();
        deathmenumusic.dispose();

        flashShader.dispose();
        for (Music music : musiclist){
            music.dispose();
        }
        for (Music music : menumusiclist){
            music.dispose();
        }
    }
}