package com.kredatus.flockblockers.GlideOrDieHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mr. Kredatus on 8/27/2017.
 **/

public class AssetLoader {
    public static Random r = new Random();
    public static int menumusiciterator, musiciterator;
    public static  Music[] musiclist, menumusiclist;
    public static Animation[] phoenixAnimations, flockAnimations,waterAnimations,fireAnimations, acidAnimations,thunderAnimations,goldAnimations,lunarAnimations;
    public static Texture bgPhoenix, bgAcid, bgFire, bgFlock, bgGold, bgLunar, bgThunder, bgWater, phoenixBird, flockBird, acidBird, waterBird, thunderBird, fireBird, goldBird, lunarBird, boost, boostdowntexture, logoTexture, playtexture, playdowntexture, newHighscoretexture,
            creditstexture, creditsdowntexture, exittexture, exitdowntexture, retrytexture, retrydowntexture, readytexture, readydowntexture,
            storytexture, storydowntexture, scoretexture, ratingtexture, topscoretexture, menutexture, menudowntexture, youvediedtexture,
            creditsbgtexture, deathmenubgtexture, instrtexture, instrdowntexture, gliderbgtexture, instrbgtexture, readybgtexture, nexttexture,
            nextdowntexture, worldStabilizedtexture;
    public static TextureRegion  horflipbgtexture, vertflipbgtexture, horvertflipbgtexture, boosttexture,boostdown,logo, playdown,
            play, credits, creditsdown, exit, exitdown, retry, retrydown, ready, readydown, story, storydown, instr, instrdown, menu, menudown,
            score, rating, topscore, youvedied, newHighscore, creditsbg, deathmenubg, gliderbg, instrbg, readybg, next, nextdown, worldStabilized;

    public static ArrayList<Vector3> boostcoords;
    public static ArrayList<TextureRegion> bgList;
    public static int bgscaling, gliderscaling, boostnumber, coordslistsize;
    public static Sound splashdown, swoop, fire;
    public static Music deathmenumusic;
    public static Vector3 p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19,
            p20, p21, p22, p23, p24, p25, p26, p27, p28, p29, p30, p31;

    public static Animation<TextureRegion> frontFlaps, rightSideFlaps, leftSideFlaps, backFlaps, whiteTinyBirdAnimations, blackTinyBirdAnimations,greyTinyBirdAnimations;
    //public static TextureRegion gliderMid, gliderDown, gliderUp ,vertflipgliderMid, vertflipgliderDown, vertflipgliderUp,
            //frontGliderMid, frontGliderDown, frontGliderUp, frontGliderUpHigh, backgliderMid, backgliderDown, backgliderUp;

    public static Preferences prefs;

    public static void load() {
        menumusiciterator = r.nextInt(3);
        musiciterator = r.nextInt(6);

        //button textures
        playtexture = new Texture(Gdx.files.internal("ui/play.png"));
        playtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        playdowntexture = new Texture(Gdx.files.internal("ui/playdown.png"));
        playdowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        playdown = new TextureRegion(playdowntexture, 0, 0, 347, 220);
        play = new TextureRegion(playtexture, 0, 0, 347, 220);
        play.flip(false, true);
        playdown.flip(false, true);

        creditstexture = new Texture(Gdx.files.internal("ui/credits.png"));
        creditstexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        creditsdowntexture = new Texture(Gdx.files.internal("ui/creditsdown.png"));
        creditsdowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        credits = new TextureRegion(creditstexture, 0, 0, 536, 221);
        credits.flip(false, true);
        creditsdown = new TextureRegion(creditsdowntexture, 0, 0, 536, 221);
        creditsdown.flip(false, true);

        exittexture = new Texture(Gdx.files.internal("ui/exit.png"));
        exittexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        exitdowntexture = new Texture(Gdx.files.internal("ui/exitdown.png"));
        exitdowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        exit = new TextureRegion(exittexture, 0, 0, 350, 221);
        exit.flip(false, true);
        exitdown = new TextureRegion(exitdowntexture, 0, 0, 350, 221);
        exitdown.flip(false, true);

        retrytexture = new Texture(Gdx.files.internal("ui/retry.png"));
        retrytexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        retrydowntexture = new Texture(Gdx.files.internal("ui/retrydown.png"));
        retrydowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        retry = new TextureRegion(retrytexture, 0, 0, 465, 222);
        retry.flip(false, true);
        retrydown = new TextureRegion(retrydowntexture, 0, 0, 465, 222);
        retrydown.flip(false, true);

        readytexture = new Texture(Gdx.files.internal("ui/ready.png"));
        readytexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        readydowntexture = new Texture(Gdx.files.internal("ui/readydown.png"));
        readydowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ready = new TextureRegion(readytexture, 0, 0, 495, 222);
        ready.flip(false, true);
        readydown = new TextureRegion(readydowntexture, 0, 0, 495, 222);
        readydown.flip(false, true);

        storytexture = new Texture(Gdx.files.internal("ui/story.png"));
        storytexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        storydowntexture = new Texture(Gdx.files.internal("ui/storydown.png"));
        storydowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        story = new TextureRegion(storytexture, 0, 0, 426, 221);
        story.flip(false, true);
        storydown = new TextureRegion(storydowntexture, 0, 0, 426, 221);
        storydown.flip(false, true);

        instrtexture = new Texture(Gdx.files.internal("ui/instr.png"));
        instrtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        instrdowntexture = new Texture(Gdx.files.internal("ui/instrdown.png"));
        instrdowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        instr = new TextureRegion(instrtexture, instrtexture.getWidth(), instrtexture.getHeight());
        instr.flip(false, true);
        instrdown = new TextureRegion(instrdowntexture, instrdowntexture.getWidth(), instrdowntexture.getHeight());
        instrdown.flip(false, true);

        menutexture = new Texture(Gdx.files.internal("ui/menu.png"));
        menutexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menudowntexture = new Texture(Gdx.files.internal("ui/menudown.png"));
        menudowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menu = new TextureRegion(menutexture, 0, 0, 409, 221);
        menu.flip(false, true);
        menudown = new TextureRegion(menudowntexture, 0, 0, 409, 221);
        menudown.flip(false, true);

        nexttexture = new Texture(Gdx.files.internal("ui/next.png"));
        nexttexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        nextdowntexture = new Texture(Gdx.files.internal("ui/nextdown.png"));
        nextdowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        nextdown = new TextureRegion(nextdowntexture, 0, 0, 347, 220);
        next = new TextureRegion(nexttexture, 0, 0, 347, 220);
        next.flip(false, true);
        nextdown.flip(false, true);

        //non-button textures *************************************************************************************************
        worldStabilizedtexture = new Texture(Gdx.files.internal("ui/worldStabilized.png"));
        worldStabilizedtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        worldStabilized = new TextureRegion(worldStabilizedtexture, worldStabilizedtexture.getWidth(), worldStabilizedtexture.getHeight());
        worldStabilized.flip(false, true);

        logoTexture = new Texture(Gdx.files.internal("backgrounds/companyLogo.jpg"));
        logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logo = new TextureRegion(logoTexture, 0, 0, 1100, 800);

        newHighscoretexture = new Texture(Gdx.files.internal("ui/newHighscore.png"));
        newHighscoretexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        newHighscore = new TextureRegion(newHighscoretexture, 0, 0, 644, 184);
        newHighscore.flip(false, true);

        youvediedtexture = new Texture(Gdx.files.internal("ui/youveDied.png"));
        youvediedtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        youvedied = new TextureRegion(youvediedtexture, 0, 0, 782, 182);
        youvedied.flip(false, true);

        scoretexture = new Texture(Gdx.files.internal("ui/score.png"));
        scoretexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        score = new TextureRegion(scoretexture, 0, 0, 446, 221);
        score.flip(false, true);

        ratingtexture = new Texture(Gdx.files.internal("ui/rating.png"));
        ratingtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        rating = new TextureRegion(ratingtexture, 0, 0, 507, 221);
        rating.flip(false, true);

        topscoretexture = new Texture(Gdx.files.internal("ui/topscore.png"));
        topscoretexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        topscore = new TextureRegion(topscoretexture, 0, 0, 680, 221);
        topscore.flip(false, true);

        //BACKGROUNDS **************************************************************************************************************
        gliderbgtexture = new Texture(Gdx.files.internal("backgrounds/gliderbg.png"));
        gliderbgtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gliderbg = new TextureRegion(gliderbgtexture, gliderbgtexture.getWidth(), gliderbgtexture.getHeight());
        gliderbg.flip(false, true);

        creditsbgtexture = new Texture(Gdx.files.internal("backgrounds/creditsbg.jpg"));
        creditsbgtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        creditsbg = new TextureRegion(creditsbgtexture, creditsbgtexture.getWidth(), creditsbgtexture.getHeight());
        creditsbg.flip(false, true);

        deathmenubgtexture = new Texture(Gdx.files.internal("backgrounds/deathmenubg.png"));
        deathmenubgtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        deathmenubg = new TextureRegion(deathmenubgtexture, deathmenubgtexture.getWidth(), deathmenubgtexture.getHeight());
        deathmenubg.flip(false, true);

        instrbgtexture = new Texture(Gdx.files.internal("backgrounds/instrbg.png"));
        instrbgtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        instrbg = new TextureRegion(instrbgtexture, instrbgtexture.getWidth(), instrbgtexture.getHeight());
        instrbg.flip(false, true);

        readybgtexture = new Texture(Gdx.files.internal("backgrounds/readybg.png"));
        readybgtexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        readybg = new TextureRegion(readybgtexture, readybgtexture.getWidth(), readybgtexture.getHeight());
        readybg.flip(false, true);


        //if button click: choose this map, load this list of locations to randomize boosts at *************************************
        bgList = new ArrayList<TextureRegion>();


        bgPhoenix = new Texture(Gdx.files.internal("backgrounds/levels/bgPhoenix.jpg"));
        bgPhoenix.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        TextureRegion bgPhoenixtexture = new TextureRegion(bgPhoenix, bgPhoenix.getWidth(), bgPhoenix.getHeight());
        bgPhoenixtexture.flip(false, true);
        bgList.add(bgPhoenixtexture);

        TextureRegion bgPhoenixtexture2 = new TextureRegion(bgPhoenix, bgPhoenix.getWidth(), bgPhoenix.getHeight());
        bgList.add(bgPhoenixtexture2);


        bgFlock = new Texture(Gdx.files.internal("backgrounds/levels/bgFlock.jpg"));
        bgFlock.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        TextureRegion bgFlocktexture = new TextureRegion(bgFlock, bgFlock.getWidth(), bgFlock.getHeight());
        bgFlocktexture.flip(false, true);
        bgList.add(bgFlocktexture);

        TextureRegion bgFlocktexture2 = new TextureRegion(bgFlock, bgFlock.getWidth(), bgFlock.getHeight());
        bgList.add(bgFlocktexture2);


        bgWater = new Texture(Gdx.files.internal("backgrounds/levels/bgWater.jpg"));
        bgWater.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        TextureRegion bgWatertexture = new TextureRegion(bgWater, bgWater.getWidth(), bgWater.getHeight());
        bgWatertexture.flip(false, true);
        bgList.add(bgWatertexture);

        TextureRegion bgWatertexture2 = new TextureRegion(bgWater, bgWater.getWidth(), bgWater.getHeight());
        bgList.add(bgWatertexture2);


        bgAcid = new Texture(Gdx.files.internal("backgrounds/levels/bgAcid.jpg"));
        bgAcid.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        TextureRegion bgAcidtexture = new TextureRegion(bgAcid, bgAcid.getWidth(), bgAcid.getHeight());
        bgAcidtexture.flip(false, true);
        bgList.add(bgAcidtexture);

        TextureRegion bgAcidtexture2 = new TextureRegion(bgAcid, bgAcid.getWidth(), bgAcid.getHeight());
        bgList.add(bgAcidtexture2);


        bgFire = new Texture(Gdx.files.internal("backgrounds/levels/bgFire.jpg"));
        bgFire.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        TextureRegion bgFiretexture = new TextureRegion(bgFire, bgFire.getWidth(), bgFire.getHeight());
        bgFiretexture.flip(false, true);
        bgList.add(bgFiretexture);

        TextureRegion bgFiretexture2 = new TextureRegion(bgFire, bgFire.getWidth(), bgFire.getHeight());
        bgList.add(bgFiretexture2);


        bgThunder = new Texture(Gdx.files.internal("backgrounds/levels/bgThunder.jpg"));
        bgThunder.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        TextureRegion bgThundertexture = new TextureRegion(bgThunder, bgThunder.getWidth(), bgThunder.getHeight());
        bgThundertexture.flip(false, true);
        bgList.add(bgThundertexture);

        TextureRegion bgThundertexture2 = new TextureRegion(bgThunder, bgThunder.getWidth(), bgThunder.getHeight());
        bgList.add(bgThundertexture2);


        bgLunar = new Texture(Gdx.files.internal("backgrounds/levels/bgLunar.jpg"));
        bgLunar.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        TextureRegion bgLunartexture = new TextureRegion(bgLunar, bgLunar.getWidth(), bgLunar.getHeight());
        bgLunartexture.flip(false, true);
        bgList.add(bgLunartexture);

        TextureRegion bgLunartexture2 = new TextureRegion(bgLunar, bgLunar.getWidth(), bgLunar.getHeight());
        bgList.add(bgLunartexture2);

        bgGold = new Texture(Gdx.files.internal("backgrounds/levels/bgGold.jpg"));
        bgGold.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        TextureRegion bgGoldtexture = new TextureRegion(bgGold, bgGold.getWidth(), bgGold.getHeight());
        bgGoldtexture.flip(false, true);
        bgList.add(bgGoldtexture);


        TextureRegion bgGoldtexture2 = new TextureRegion(bgGold, bgGold.getWidth(), bgGold.getHeight());
        bgList.add(bgGoldtexture2);



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
        boost = new Texture(Gdx.files.internal("sprites/boost.png"));
        boost.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        boosttexture = new TextureRegion(boost, boost.getWidth(), boost.getHeight());
        boosttexture.flip(false, true);
        boostdowntexture = new Texture(Gdx.files.internal("ui/boostdown.png"));
        boostdowntexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        boostdown = new TextureRegion(boostdowntexture, boostdowntexture.getWidth(), boostdowntexture.getHeight());
        boostdown.flip(false, true);

        phoenixBird = new Texture(Gdx.files.internal("sprites/phoenix.png"));
        phoenixBird.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        phoenixAnimations = birdTextureToSprite(phoenixBird);


        Texture greyTinyBird = new Texture(Gdx.files.internal("sprites/greyTinyBird.png"));
        greyTinyBirdAnimations=tinyBirdTextureToAnimation(greyTinyBird);

        Texture blackTinyBird = new Texture(Gdx.files.internal("sprites/blackTinyBird.png"));
        blackTinyBirdAnimations=tinyBirdTextureToAnimation(blackTinyBird);

        Texture whiteTinyBird = new Texture(Gdx.files.internal("sprites/whiteTinyBird.png"));
        whiteTinyBirdAnimations=tinyBirdTextureToAnimation(whiteTinyBird);
        /*
        flockBird = new Texture(Gdx.files.internal("sprites/flock.png"));
        flockBird.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        flockAnimations = textureToSprite(flockBird);

        waterBird = new Texture(Gdx.files.internal("sprites/water.png"));
        waterBird.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        waterAnimations = textureToSprite(waterBird);

        fireBird = new Texture(Gdx.files.internal("sprites/fire.png"));
        fireBird.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        fireAnimations = textureToSprite(fireBird);

        acidBird = new Texture(Gdx.files.internal("sprites/acid.png"));
        acidBird.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        acidAnimations = textureToSprite(acidBird);

        thunderBird = new Texture(Gdx.files.internal("sprites/thunder.png"));
        thunderBird.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        thunderAnimations = textureToSprite(thunderBird);

        lunarBird = new Texture(Gdx.files.internal("sprites/lunar.png"));
        lunarBird.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        lunarAnimations = textureToSprite(lunarBird);

        goldBird = new Texture(Gdx.files.internal("sprites/gold.png"));
        goldBird.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        goldAnimations = textureToSprite(goldBird);
*/

/*
        //flip world
        vertflipgliderMid = new TextureRegion(sprites, 1020, 890, 379, 464);
        vertflipgliderMid.flip(false, false);

        vertflipgliderDown= new TextureRegion(sprites, 545, 890, 404, 464);
        vertflipgliderDown.flip(false, false);

        vertflipgliderUp= new TextureRegion(sprites, 1500, 890, 379, 464);
        vertflipgliderUp.flip(false, false);

        TextureRegion[] flipPositions = { vertflipgliderUp, vertflipgliderMid, vertflipgliderDown, vertflipgliderMid };
        flipflaps= new Animation<TextureRegion>(0.12f, flipPositions);
        flipflaps.setPlayMode(Animation.PlayMode.LOOP);

        //inbetween worlds
        frontGliderMid = new TextureRegion(sprites, 9, 38, 504, 259);
        frontGliderMid.flip(false, true);

        frontGliderDown= new TextureRegion(sprites, 520, 43, 414, 266);
        frontGliderDown.flip(false, true);

        frontGliderUp= new TextureRegion(sprites, 941, 45, 494, 242);
        frontGliderUp.flip(false, true);

        frontGliderUpHigh= new TextureRegion(sprites, 1439, 20, 454, 274);
        frontGliderUpHigh.flip(false, true);

        TextureRegion[] frontPositions = {  frontGliderDown, frontGliderUp, frontGliderUpHigh, frontGliderMid };
        frontViewFlaps= new Animation<TextureRegion>(0.2f, frontPositions);
        frontViewFlaps.setPlayMode(Animation.PlayMode.LOOP);

        backgliderMid = new TextureRegion(sprites, 961, 1365, 503, 444);
        backgliderMid.flip(false, true);

        backgliderDown= new TextureRegion(sprites, 485, 1365, 484, 444);
        backgliderDown.flip(false, true);

        backgliderUp= new TextureRegion(sprites, 1465, 1365, 426, 444);
        backgliderUp.flip(false, true);

        TextureRegion[] backPositions = { backgliderUp, backgliderMid, backgliderDown, backgliderMid };
        backflaps= new Animation<TextureRegion>(0.2f, backPositions);
        backflaps.setPlayMode(Animation.PlayMode.LOOP);*/

        //SOUNDWORK
        fire = Gdx.audio.newSound(Gdx.files.internal("sound/fire.wav"));
        splashdown = Gdx.audio.newSound(Gdx.files.internal("sound/splashdown.wav"));
        swoop = Gdx.audio.newSound(Gdx.files.internal("sound/swoop.wav"));

        musiclist = new Music[] { Gdx.audio.newMusic(Gdx.files.internal("music/bgMusic.mp3")), Gdx.audio.newMusic(Gdx.files.internal("music/bgMusic2.mp3")),
                Gdx.audio.newMusic(Gdx.files.internal("music/bgMusic3.mp3")), Gdx.audio.newMusic(Gdx.files.internal("music/bgMusic4.mp3")), Gdx.audio.newMusic(Gdx.files.internal("music/bgMusic5.mp3")), Gdx.audio.newMusic(Gdx.files.internal("music/bgMusic6.mp3"))};
        menumusiclist = new Music[] {Gdx.audio.newMusic(Gdx.files.internal("music/menuMusic.mp3")), Gdx.audio.newMusic(Gdx.files.internal("music/menuMusic2.mp3"))};
        deathmenumusic = Gdx.audio.newMusic(Gdx.files.internal("music/deathmenuMusic.mp3"));

        // Create (or retrieve existing) preferences file
        prefs = Gdx.app.getPreferences("GlideorDie");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }
    }

    private static Animation tinyBirdTextureToAnimation (Texture texture) {
        TextureRegion[] positions = new TextureRegion[7];

        for (int i = 0; i < 6; i++) {
            TextureRegion temp = new TextureRegion(texture, 77 * i, 0, 77, 29);
            positions[i] = (temp);
        }
        return new Animation<TextureRegion>(0.125f, positions);
    }

    public static Animation[] birdTextureToSprite (Texture texture) {
        ArrayList<TextureRegion> positions = new ArrayList<TextureRegion>();

        TextureRegion[] front = new TextureRegion[0];
        TextureRegion[] side = new TextureRegion[0];
        TextureRegion[] back = new TextureRegion[0];

        for (int i = 0; i < 16; i++) {
            TextureRegion temp = new TextureRegion(texture, 481 * i, 0, 481, 423);
            positions.add(temp);
            if (i == 5) {
                front = positions.toArray(new TextureRegion[6]);
                positions.clear();
            } else if (i == 11) {
                side = positions.toArray(new TextureRegion[6]);
                positions.clear();
                if (texture != phoenixBird) {
                    break;
                }
            } else if (i == 15) {
                back = positions.toArray(new TextureRegion[4]);
                positions.clear();
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
    }
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
            if (menumusiciterator<1){
                menumusiciterator++;
                list[menumusiciterator].play();
                list[menumusiciterator].setLooping(true);
            } else{
                menumusiciterator=0;
                list[menumusiciterator].play();
                list[menumusiciterator].setLooping(true);
            }
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
        bgFlock.dispose();

        phoenixBird.dispose();
        acidBird.dispose();
        waterBird.dispose();
        fireBird.dispose();
        flockBird.dispose();
        thunderBird.dispose();
        goldBird.dispose();
        lunarBird.dispose();
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

        splashdown.dispose();
        swoop.dispose();
        fire.dispose();
        deathmenumusic.dispose();

        for (Music music : musiclist){
            music.dispose();
        }
        for (Music music : menumusiclist){
            music.dispose();
        }
    }
}