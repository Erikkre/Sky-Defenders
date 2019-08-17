// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kredatus.skydefenders.Birds.BirdAbstractClass;
import com.kredatus.skydefenders.GameObjects.Airship;
import com.kredatus.skydefenders.GameObjects.Resources.MovingImageContainer;
import com.kredatus.skydefenders.GameObjects.Resources.Rank;
import com.kredatus.skydefenders.GameWorld.GameWorld;
import com.kredatus.skydefenders.NonGameHandlerScreens.Loader;
import com.kredatus.skydefenders.SkyDefendersMain;
import com.kredatus.skydefenders.ui.AppearOnTouchPad;
import com.kredatus.skydefenders.ui.SlideMenu;
import com.kredatus.skydefenders.ui.TouchRotatePad;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class UiHandler {

    public static Table rootTable,table0,table1;
    //public ScrollPane scrollPane;
    //public Label nameLabel;
    //public Skin shadeSkin=new Skin(Gdx.files.internal("ui/button.png"));
    public  Stage stage;
    public Skin shadeSkin;
    public static AppearOnTouchPad movPad, aimPad;
    public static SlideMenu slideMenuLeft, slideMenuBottom;
    public Image menuButtonX, menuButtonY;
    public static boolean isTouched;
    float camWidth,camHeight;
    public GameWorld world;
    public TextButton buyButton,menuButton,playButton;
    public static Label fuelLabel,goldLabel,diamondLabel,ammoLabel, lvlLabel, rankNameLabel, roundLabel, waveLabel,airshipHealthLabel,airshipArmorLabel,expLabel,goldLimitLabel,diamondLimitLabel,fuelLimitLabel,ammoLimitLabel;
    public static Stack expStack;
    public Random r = new Random();
    public  ConcurrentLinkedQueue<MovingImageContainer> boughtItemsList= new ConcurrentLinkedQueue<MovingImageContainer>();
    public ConcurrentLinkedQueue<Future> futureList= new ConcurrentLinkedQueue<Future>();
    public ConcurrentLinkedQueue<ImageTextButton> buyButtons= new ConcurrentLinkedQueue<ImageTextButton>();
    public Runnable giveDiamondGetGold,giveGoldGetDiamond,giveGoldGetArmor,giveGoldGetAmmo,giveGoldGetFuel,giveGoldGetHealth;
    public static int boughtGoldNum, boughtAmmoNum, boughtFuelNum, boughtHealthNum, boughtArmorNum, boughtDiamondNum,
    totalGoldNum,totalAmmoNum,totalHealthNum,totalArmorNum,totalDiamondNum;
    public static float totalFuelNum;
    public ScheduledFuture<?> giveDiamondGetGoldFuture,giveGoldGetDiamondFuture,giveGoldGetArmorFuture,giveGoldGetAmmoFuture,giveGoldGetFuelFuture,giveGoldGetHealthFuture;    //might want to implement a current stage for new screens
    public String armorPrice,ammoPrice,fuelPrice,healthPrice,diamondPrice,goldPrice,//1 diamond
            armorPerTap,ammoPerTap,fuelPerTap,healthPerTap,diamondPerTap,goldPerTap;
    public static double lastResourceGatherTime;public static int resourceGatherStreak;
    public  boolean anyUITouched() {
        for (Actor i : stage.getActors()){
            if (i instanceof SlideMenu)      {if (((SlideMenu) i).isTouched){ return true;}}
        }
        if (rootTable!=null){
            for (Actor i : rootTable.getChildren()){//check if rootTable is removed when stage.clear is called
                if (i instanceof Touchpad)      {if (((Touchpad) i).isTouched()){ return true;}}
                else if (i instanceof TouchRotatePad)      {if (((TouchRotatePad) i).isTouched()){ return true;}}
            }
        }
        return false;
    }

    public int rankSize;public Color rankColor;
    public static Image goldSymbol,fuelSymbol,ammoSymbol,diamondSymbol,airshipHealthSymbol,airshipArmorSymbol;public static Rank rank; public static ProgressBar expBar,airshipHealthBar,airshipArmorBar;public static Image rankImage;
    public ImageTextButton buyGoldButton,buyDiamondButton,buyFuelButton,buyArmorButton,buyAmmoButton,buyHealthButton;
    private Preferences prefs = Gdx.app.getPreferences("skyDefenders");
    public ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(10);
    public Button musicButton,soundButton;
    public UiHandler(GameWorld world, float camWidth, float camHeight, Skin shadeSkin) {
        if (Gdx.app.getVersion() >= 21) timer.setRemoveOnCancelPolicy(true);

        /******* BUTTONS *******/
        buyButton  = new TextButton("Buy", shadeSkin);    //set button style
        buyButton .setStyle(shadeSkin.get("round", buyButton.getStyle().getClass()));

        playButton  = new TextButton("Play", shadeSkin);    //set button style
        playButton .setStyle(shadeSkin.get("round", buyButton.getStyle().getClass()));

        menuButton = new TextButton("Menu", shadeSkin);    //set button style
        menuButton.setStyle(shadeSkin.get("round", menuButton.getStyle().getClass()));


        //https://github.com/kotcrab/vis-ui
        if (!VisUI.isLoaded()) VisUI.load(shadeSkin);

        this.world=world;
        this.camWidth=camWidth;this.camHeight=camHeight;
        this.shadeSkin = shadeSkin;

        //nameLabel = new Label("Name: ", shadeSkin);
        //TextField nameText = new TextField("Name2: ", shadeSkin);
        //TextureAtlas

        // table = new Table();
        //table.add(nameLabel);              // Row 0, column 0.
        //table.add(nameText).width(100);    // Row 0, column 1.

        stage = (((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.stage);
        stage.addCaptureListener(new ClickListener() {//tocuh up anywhere on screen means not touching ui
            public void touchUp(InputEvent event, float x, float y, int pnt, int btn) {
                super.touchUp(event, x, y, pnt, btn);
                isTouched = false;
                super.cancel();
            }
        });
        //stage.setDebugAll(true);

        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        loadSurvivalStage();
    }
    public void fadeAwayNumberEffect(Vector2 pos,int val,int randomizedMoveDistance,float scale,float time){
        Label effect=new Label("", shadeSkin,"title-plain");
        if (val>=0) effect.setText("+"+val);
        else effect.setText(Integer.toString(val));
        //float minHeight=shadeSkin.getDrawable("font-title").getMinHeight();
        effect.setFontScale(scale);
        effect.setPosition(pos.x,pos.y);
        effect.addAction(
                parallel(
                    sequence(
                            delay(time/3f),
                            fadeOut(time,Interpolation.exp10),
                            com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor()
                    ),
                    moveTo(pos.x+(-randomizedMoveDistance+r.nextInt(randomizedMoveDistance*2)),pos.y+(-randomizedMoveDistance+r.nextInt(randomizedMoveDistance*2)),1,Interpolation.pow3Out)
                )
        );
        stage.addActor(effect);
        //shadeSkin.getDrawable("title-plain").setMinHeight(minHeight);
    }
    public void followFadeAwayNumberEffect(final Object T,int val,int randomizedMoveDistance,float scale,float time){
        final Label effect=new Label("", shadeSkin,"title-plain");effect.setAlignment(Align.center);
        if (val>=0) effect.setText("+"+val);
        else effect.setText(Integer.toString(val));
        //float minHeight=shadeSkin.getDrawable("font-title").getMinHeight();
        effect.setFontScale(scale);
        final Vector2 v=new Vector2(-randomizedMoveDistance+r.nextInt(randomizedMoveDistance*2),-randomizedMoveDistance+r.nextInt(randomizedMoveDistance*2));

        Runnable setPos;
        if (T instanceof Airship){
            setPos = new Runnable() {
            @Override
            public void run() {
                if (T instanceof Airship) {
                    effect.setPosition(((Airship) T).pos.x+v.x,((Airship) T).pos.y+((Airship) T).balloonHeight.get()/2+v.y);
                }
            }
        };} else {
            setPos = new Runnable() {
                @Override
                public void run() {
                if (T instanceof Airship) {
                    effect.setPosition(((BirdAbstractClass) T).x+v.x, ((BirdAbstractClass) T).y+v.y);
                }
                }
            };}

        effect.addAction(
            parallel(
                sequence(
                    delay(time / 3f),
                    fadeOut(time, Interpolation.exp10),
                    com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor()
                ),
                forever(run(setPos))
            )
        );
        stage.addActor(effect);
    }

    public void survivalToBuyMenu(){
        slideMenuBottom.getCell(buyButton).setActor(playButton);
    }
    public void buyMenuToSurvival(){
        slideMenuBottom.getCell(playButton).setActor(buyButton);
    }
    public void loadSurvivalStage(){
        shadeSkin.getDrawable("loading-bar-fill-10patch").setMinHeight(26);shadeSkin.getDrawable("loading-bar-bg").setMinHeight(30);


        /***********************************************************************************each of these is a table in a different row*/
        table0=new Table().top();
        rootTable.add(table0).growX().padTop(13).padBottom(-8).padRight(1).colspan(2);rootTable.row();
        /***********************************************************************************each of these is a table in a different row*/
        table1=new Table().top();
        rootTable.add(table1).growX().colspan(2);rootTable.row();
        /***********************************************************************************each of these is a table in a different row*/
        //table0.addAction();
        //table1.addAction();

        Table table2=new Table().bottom();//aligns elements of table to bottom
        rootTable.add(table2).grow().colspan(2);rootTable.row();
        /***********************************************************************************each of these is a table in a different row*/
        Table bottomTable=new Table().bottom();
        rootTable.add(bottomTable).growX();
        /***********************************************************************************each of these is a table in a different row*/

        rank = new Rank(prefs.getInteger("lvl",0),prefs.getInteger("exp",0));

        //rankSize=35;rankColor=Color.GREEN;
        //System.out.println("lvl: "+rank.lvl+", exp: "+rank.expGained+", rank values: "+rank.expValues[rank.lvl]);

        Stack rankImageStack = new Stack();
        rankImage=new Image(Loader.ranksList[rank.lvl]);
        rankImage.setAlign(Align.center);rankImage.setName("rankImage");
        Image rankImageBg=new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("rankBg"));rankImageBg.setAlign(Align.center);
        rankImageStack.setLayoutEnabled(false);rankImageStack.add(rankImageBg);rankImageStack.add(rankImage); rankImageBg.setPosition(-4,-5f);
        table0.add(rankImageStack).size(40).colspan(1).padLeft(12).padRight(4).padTop(1);

        lvlLabel = new Label(Integer.toString(rank.lvl%78), shadeSkin,"title-plain");
        table0.add(lvlLabel).padLeft(3).padRight(2);

        expStack = new Stack();
        expBar = new ProgressBar(0, rank.expValues[rank.lvl], 1, false, shadeSkin.get("default-horizontal", ProgressBar.ProgressBarStyle.class));
        expBar.setColor(0,1,0,0.7f); expBar.setAnimateDuration(0.15f); expBar.setValue(prefs.getInteger("exp",0));//3.2% is the minimum value right now
        expLabel= new Label(Integer.toString(rank.expGained), shadeSkin,"title-plain");
        expStack.add(expBar);expStack.add(expLabel);expLabel.setAlignment(Align.center);
        table0.add(expStack).grow().padRight(4);//colSpan of this must be equal to # of however many labels there are under it

        airshipHealthSymbol=new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("health"));table0.add(airshipHealthSymbol).size(40);

        Stack airshipHealthStack = new Stack();
        airshipHealthBar=new ProgressBar(0,Airship.healthValues[Airship.healthLvl],1,false, shadeSkin.get("default-horizontal", ProgressBar.ProgressBarStyle.class));
        airshipHealthBar.setColor(1,0,0,0.8f);expBar.setAnimateDuration(0.15f);airshipHealthBar.setValue(Airship.health);
        airshipHealthLabel= new Label(Integer.toString(Airship.health), shadeSkin,"title-plain");
        airshipHealthStack.add(airshipHealthBar);airshipHealthStack.add(airshipHealthLabel);airshipHealthLabel.setAlignment(Align.center);
        table0.add(airshipHealthStack).grow().padRight(5);

        airshipArmorSymbol=new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("armor"));table0.add(airshipArmorSymbol).size(40);

        Stack airshipArmorStack = new Stack();
        airshipArmorBar=new ProgressBar(0,Airship.armorValues[Airship.armorLvl],1,false,shadeSkin.get("default-horizontal",ProgressBar.ProgressBarStyle.class));
        airshipArmorBar.setColor(0.2f,0.08f,0,0.8f);expBar.setAnimateDuration(0.15f);airshipArmorBar.setValue(Airship.armor);

        airshipArmorLabel= new Label(Integer.toString(Airship.armor), shadeSkin,"title-plain");
        airshipArmorStack.add(airshipArmorBar);airshipArmorStack.add(airshipArmorLabel);airshipArmorLabel.setAlignment(Align.center);
        table0.add(airshipArmorStack).grow().padRight(5);

        table0.row(); rankNameLabel=new Label(rank.rankNames[rank.lvl/78],shadeSkin,"title-plain");rankNameLabel.setFontScale(0.5f);table0.add(rankNameLabel).padLeft(2).padTop(2);


        goldSymbol=new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("gold2"));
        table1.add(goldSymbol).size(40);
        goldLabel= new Label(Integer.toString(world.gold), shadeSkin,"title-plain");
        goldLimitLabel=new Label("/"+Airship.goldValues[Airship.goldLvl], shadeSkin);
        Table t0=new Table();t0.add(goldLabel).size((camWidth-(40*5))/4.2f,goldLabel.getPrefHeight());t0.row();t0.add(goldLimitLabel).left();
        table1.add(t0).padLeft(3);


        fuelSymbol=new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("fuel"));
        table1.add(fuelSymbol).size(40);
        fuelLabel= new Label(Integer.toString((int)Airship.fuel), shadeSkin,"title-plain");
        fuelLimitLabel=new Label("/"+Airship.fuelValues[Airship.fuelLvl], shadeSkin);
        Table t1=new Table();t1.add(fuelLabel).size((camWidth-(40*5))/5f,fuelLabel.getPrefHeight());t1.row();t1.add(fuelLimitLabel).left();
        table1.add(t1).padLeft(3);


        ammoSymbol=new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("ammo"));
        table1.add(ammoSymbol).size(40);
        ammoLabel= new Label(Integer.toString(Airship.ammo), shadeSkin,"title-plain");
        ammoLimitLabel=new Label("/"+Airship.ammoValues[Airship.ammoLvl], shadeSkin);
        Table t2=new Table();t2.add(ammoLabel).size((camWidth-(40*5))/5f,ammoLabel.getPrefHeight());t2.row();t2.add(ammoLimitLabel).left();
        table1.add(t2).padLeft(3);


        diamondSymbol=new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("diamond"));
        table1.add(diamondSymbol).size(40);
        diamondLabel= new Label(Integer.toString(world.diamond), shadeSkin,"title-plain");
        diamondLimitLabel=new Label("/"+Airship.diamondValues[Airship.diamondLvl], shadeSkin);
        Table t3=new Table();t3.add(diamondLabel).size(diamondLabel.getPrefWidth(),diamondLabel.getPrefHeight());t3.row();t3.add(diamondLimitLabel);
        table1.add(t3).padLeft(3);
        /******************************************************************************************/


        shadeSkin.getDrawable("touchpad-knob").setMinWidth(50);shadeSkin.getDrawable("touchpad-knob").setMinHeight(50);

        movPad = new AppearOnTouchPad(0,camWidth/2, 12, shadeSkin,false);
        movPad.setColor(1,1,1,0.25f);movPad.setPosition(camWidth,camHeight);movPad.setVisible(false);
        rootTable.addActor(movPad);

        aimPad = new AppearOnTouchPad(camWidth/2,camWidth,15, shadeSkin,true);
        aimPad.setColor(1,1,1,0.25f);aimPad.setPosition(camWidth,camHeight);aimPad.setVisible(false);
        rootTable.addActor(aimPad);



        roundLabel= new Label("Round "+world.round, shadeSkin,"title-plain");
        waveLabel= new Label("Wave "+(BirdHandler.waveNumber+1)+"/8", shadeSkin,"title-plain");
        roundLabel.setAlignment(Align.left);waveLabel.setAlignment(Align.right);
        bottomTable.add(roundLabel).growX().left().pad(4);bottomTable.add(waveLabel).growX().right().pad(4);

        loadSlideMenus();
    }

    public void loadSlideMenus(){
        //stage.addCaptureListener(slideMenuLeft.getListeners().get(0));stage.addCaptureListener(slideMenuBottom.getListeners().get(0));
        //  stage.addCaptureListener(menuButtonX.getListeners().get(0));stage.addCaptureListener(men    uButtonY.getListeners().get(0));

        /*logo = tA.findRegion("companyLogo");
        slidemenuBg = tA.findRegion("slideMenuBackground");
        menuButton = tA.findRegion("menuButton");
        shareButton = tA.findRegion("shareButton");
        rateButton = tA.findRegion("rateButton");*/


        /**      ****************************************LEFT SLIDING MENU*****************************************      **/
        slideMenuLeft = new SlideMenu(camWidth/6f,camHeight/2.2f,"left",camWidth,camHeight,camHeight/35);//left or up
        slideMenuLeft.setColor(1,1,1,0.7f);
        final Table leftTable=new Table();leftTable.setSize(camWidth/6f,camHeight/2f);//leftTable.setWidth(camWidth/7f);
        final ScrollPane scrollPane=new ScrollPane(leftTable,shadeSkin,"android");//scrollPane.setWidth(camWidth/7f);
        scrollPane.setFillParent(true);scrollPane.setFadeScrollBars(true);scrollPane.setScrollBarPositions(false,false);scrollPane.setScrollingDisabled(true,false);
        slideMenuLeft.add(scrollPane).grow().pad(-10).center();//.width(camWidth/7f);
        //scrollPane.layout();scrollPane.layout();
        //Sprite temp=new Sprite(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("slideMenuBackground"));
        //temp.setColor(new Color(0,0,0,0.5f));
        //final Image image_backgroundX = new Image(new SpriteDrawable(temp));
        menuButtonX = new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("menuButton"));

        leftTable.add(new Label("BUY", shadeSkin,"title-plain")).padBottom(5).colspan(2).row();
                    armorPrice="100";ammoPrice="20";fuelPrice="20";healthPrice="200";diamondPrice="2500";goldPrice="1";//1 diamond
                    armorPerTap="20";ammoPerTap="30";fuelPerTap="50";healthPerTap="20";diamondPerTap="1";goldPerTap="500";

        buyArmorButton=new ImageTextButton(armorPrice,shadeSkin,"buyArmor");//buy cost
        buyArmorButton.clearChildren();
        buyArmorButton.add(new Label(armorPerTap, shadeSkin,"buttonfont"));buyArmorButton.add(buyArmorButton.getImage()).padLeft(-2).padBottom(5).colspan(6);buyArmorButton.row();//amount receiving
        buyArmorButton.add(new Image(goldSymbol.getDrawable())).size(20).right();buyArmorButton.add(buyArmorButton.getLabel()).left();
        leftTable.add(buyArmorButton).expandY().padTop(5).padBottom(5).colspan(2).row();

        buyAmmoButton=new ImageTextButton(ammoPrice,shadeSkin,"buyAmmo");
        buyAmmoButton.clearChildren();
        buyAmmoButton.add(new Label(ammoPerTap, shadeSkin,"buttonfont"));buyAmmoButton.add(buyAmmoButton.getImage()).padLeft(-2).padBottom(5).colspan(6);buyAmmoButton.row();//amount receiving
        buyAmmoButton.add(new Image(goldSymbol.getDrawable())).size(20).right();buyAmmoButton.add(buyAmmoButton.getLabel()).left();
        leftTable.add(buyAmmoButton).expandY().padTop(5).padBottom(5).colspan(2).row();

        buyFuelButton=new ImageTextButton(fuelPrice,shadeSkin,"buyFuel");
        buyFuelButton.clearChildren();
        buyFuelButton.add(new Label(fuelPerTap, shadeSkin,"buttonfont"));buyFuelButton.add(buyFuelButton.getImage()).padLeft(-2).padBottom(5).colspan(6);buyFuelButton.row();//amount receiving
        buyFuelButton.add(new Image(goldSymbol.getDrawable())).size(20).right();buyFuelButton.add(buyFuelButton.getLabel()).left();
        leftTable.add(buyFuelButton).expandY().padTop(5).padBottom(5).colspan(2).row();

        buyHealthButton=new ImageTextButton(healthPrice,shadeSkin,"buyHealth");
        buyHealthButton.clearChildren();
        buyHealthButton.add(new Label(healthPerTap, shadeSkin,"buttonfont"));buyHealthButton.add(buyHealthButton.getImage()).padLeft(-2).padBottom(5).colspan(6);buyHealthButton.row();//amount receiving
        buyHealthButton.add(new Image(goldSymbol.getDrawable())).size(20).right();buyHealthButton.add(buyHealthButton.getLabel()).left();
        leftTable.add(buyHealthButton).expandY().padTop(5).padBottom(5).colspan(2).row();

        buyDiamondButton=new ImageTextButton(diamondPrice,shadeSkin,"buyDiamond");
        buyDiamondButton.clearChildren();
        buyDiamondButton.add(new Label(diamondPerTap, shadeSkin,"buttonfont"));buyDiamondButton.add(buyDiamondButton.getImage()).padLeft(-2).padBottom(5).colspan(6);buyDiamondButton.row();//amount receiving
        buyDiamondButton.add(new Image(goldSymbol.getDrawable())).size(20).right();buyDiamondButton.add(buyDiamondButton.getLabel()).left();
        leftTable.add(buyDiamondButton).expandY().padTop(5).padBottom(5).colspan(2).row();

        buyGoldButton=new ImageTextButton(goldPrice,shadeSkin,"buyGold");//only one with buy cost in diamond, not gold
        buyGoldButton.clearChildren();
        buyGoldButton.add(new Label(goldPerTap, shadeSkin,"buttonfont"));buyGoldButton.add(buyGoldButton.getImage()).padLeft(-2).padBottom(5).colspan(6);buyGoldButton.row();//amount receiving
        buyGoldButton.add(new Image(diamondSymbol.getDrawable())).size(20).right();buyGoldButton.add(buyGoldButton.getLabel()).left();
        leftTable.add(buyGoldButton).expandY().padTop(5).padBottom(10).colspan(2).row();

        Collections.addAll(buyButtons,buyArmorButton,buyAmmoButton,buyFuelButton,buyHealthButton,buyDiamondButton,buyGoldButton);
        loadAllBuyButtonsTimerTasks();

        musicButton=new Button(shadeSkin, "music");soundButton=new Button(shadeSkin, "sound");soundButton.setChecked(true);musicButton.setChecked(true);
        leftTable.add(musicButton,soundButton);
        //final Image rateButton = new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("rateButton"));
        //final Image shareButton = new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("shareButton"));



        //icon_off_music.setVisible(false);
        //slideMenuLeft.stack(icon_music, icon_off_music).pad(52, 52, 300, 52).expandX().row(); //one on top of the other
        // setup attributes for menu navigation slideMenuLeft.

        slideMenuLeft.top().left();
        //slideMenuLeft.setWidthStartDrag(0);
        //slideMenuLeft.setWidthBackDrag(0);
        //slideMenuLeft.setTouchable(Touchable.enabled);

        /* z-index = 1 */
        // add image_background as a separating actor into stage to make smooth shadow with dragging value.
        //image_background.setWidth(slideMenuLeft.getWidth()*0.1f);image_background.setHeight(slideMenuLeft.getHeight());
        //uiHandler.stage.addActor(image_background);
        //slideMenuLeft.setFadeBackground(image_background, 0.5f);

        /* z-index = 2 */
        stage.addActor(slideMenuLeft);

        /* z-index = 3 */
        // add button_menu as a separating actor into stage to rotates with dragging value.
        menuButtonX.setWidth(menuButtonX.getWidth()*0.4f);menuButtonX.setHeight(menuButtonX.getHeight()*0.9f);menuButtonX.setColor(1,1,1,0.7f);
        menuButtonX.setOrigin(Align.center);
        //menuButtonActor=menuButtonX;
        stage.addActor(menuButtonX);
        slideMenuLeft.setMoveMenuButton(menuButtonX);
        //slideMenuLeft.setRotateMenuButton(menuButtonX, 90f);

        // Optional
        /*
         Image image_shadow = new Image(atlas.findRegion("image_shadow"));
         image_shadow.setHeight(NAV_HEIGHT);
         image_shadow.setX(NAV_WIDTH);
         slideMenuLeft.setAreaWidth(NAV_WIDTH + image_shadow.getWidth());
         slideMenuLeft.addActor(image_shadow);*/

        // show the panel
        //slideMenuLeft.showManually(true);  //show panel manually
        //System.out.println(image_backgroundX.getImageY());
        //System.out.println(image_backgroundY.getImageY());

        buyArmorButton.setName("r");
        buyAmmoButton.setName("a");
        buyFuelButton.setName("f");
        buyHealthButton.setName("h");
        buyDiamondButton.setName("d");
        buyGoldButton.setName("g");
        soundButton.setName("s");musicButton.setName("m");
        for (ImageTextButton i:buyButtons) {
            for (Actor j : i.getChildren()) {
                j.setName(i.getName());
            }
        }
        menuButtonX.setName("menuButtonX");
        //image_backgroundX.setName("IMAGE_BACKGROUNDX");

        slideMenuLeft.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //super.touchUp(event, x, y, 0, 0);
                Actor actor = event.getTarget();
                isTouched = true;
                if (actor.getName()==null) {
                    super.cancel();
                } else if (actor.getName().equals("m")) {
                    if (!((Button)actor).isChecked()) Loader.stopMusic(Loader.menumusiclist);
                    else Loader.startMusic(Loader.menumusiclist);
                } else if (actor.getName().equals("s")) {
                    if (!((Button)actor).isChecked()) world.soundMuted=true;
                    else world.soundMuted=false;

                } else if ((actor instanceof ImageTextButton && !((ImageTextButton) actor).isDisabled())||(actor.getParent() instanceof ImageTextButton && !((ImageTextButton) actor.getParent()).isDisabled())) {
                    if (actor.getName().equals("r")) {
                        world.gold-=Integer.parseInt(armorPrice);UiHandler.totalGoldNum-=Integer.parseInt(armorPrice);
                        boughtArmorNum += Integer.parseInt(armorPerTap);totalArmorNum+=Integer.parseInt(armorPerTap);
                        if(boughtArmorNum ==Integer.parseInt(armorPerTap))giveGoldGetArmorFuture=timer.scheduleAtFixedRate(giveGoldGetArmor, 0, (long)(1000f/ boughtArmorNum), TimeUnit.MILLISECONDS);
                        futureList.add(giveGoldGetArmorFuture);
                    } else if (actor.getName().equals("a")) {
                        world.gold -= Integer.parseInt(ammoPrice);UiHandler.totalGoldNum-=Integer.parseInt(ammoPrice);
                        boughtAmmoNum += Integer.parseInt(ammoPerTap);totalAmmoNum+= Integer.parseInt(ammoPerTap);
                        if(boughtAmmoNum ==Integer.parseInt(ammoPerTap))giveGoldGetAmmoFuture=timer.scheduleAtFixedRate(giveGoldGetAmmo, 0, (long)(1000f/ boughtAmmoNum), TimeUnit.MILLISECONDS);
                        futureList.add(giveGoldGetAmmoFuture);
                    } else if (actor.getName().equals("f")) {
                        world.gold -= Integer.parseInt(fuelPrice);UiHandler.totalGoldNum-=Integer.parseInt(fuelPrice);
                        boughtFuelNum += Integer.parseInt(fuelPerTap);totalFuelNum+= Integer.parseInt(fuelPerTap);
                        if(boughtFuelNum ==Integer.parseInt(fuelPerTap))giveGoldGetFuelFuture=timer.scheduleAtFixedRate(giveGoldGetFuel, 0, (long)(1000f/ boughtFuelNum), TimeUnit.MILLISECONDS);
                        futureList.add(giveGoldGetFuelFuture);
                    } else if (actor.getName().equals("h")) {
                        world.gold -= Integer.parseInt(healthPrice);UiHandler.totalGoldNum-=Integer.parseInt(healthPrice);
                        boughtHealthNum += Integer.parseInt(healthPerTap);totalHealthNum+= Integer.parseInt(healthPerTap);
                        if(boughtHealthNum==Integer.parseInt(healthPerTap))giveGoldGetHealthFuture=timer.scheduleAtFixedRate(giveGoldGetHealth,0,(long)(1000f/boughtHealthNum), TimeUnit.MILLISECONDS);
                        futureList.add(giveGoldGetHealthFuture);
                    } else if (actor.getName().equals("d")) {
                        world.gold -= Integer.parseInt(diamondPrice);UiHandler.totalGoldNum-=Integer.parseInt(diamondPrice);
                        boughtDiamondNum += Integer.parseInt(diamondPerTap);totalDiamondNum+= Integer.parseInt(diamondPerTap);
                        if(boughtDiamondNum==Integer.parseInt(diamondPerTap))giveGoldGetDiamondFuture=timer.scheduleAtFixedRate(giveGoldGetDiamond, 0, (long)(1000f), TimeUnit.MILLISECONDS);
                        futureList.add(giveGoldGetDiamondFuture);
                    } else if (actor.getName().equals("g")) {
                        world.diamond -= Integer.parseInt(goldPrice);totalDiamondNum-=Integer.parseInt(goldPrice);boughtGoldNum += Integer.parseInt(goldPerTap);
                        if(boughtGoldNum ==Integer.parseInt(goldPerTap))giveDiamondGetGoldFuture=timer.scheduleAtFixedRate(giveDiamondGetGold, 0, (long)(1000f/ boughtGoldNum), TimeUnit.MILLISECONDS);
                        futureList.add(giveDiamondGetGoldFuture);
                        diamondLabel.setText(world.diamond);
                    }
                    goldLabel.setText(world.gold);
                }
                super.cancel();
            }
        });

        menuButtonX.addListener(new ClickListener(){//separate listener for touch up events
            public boolean touchDown(InputEvent event, float x, float y, int pnt, int btn) {
                //System.out.println(Math.abs(Gdx.input.getDeltaX()));
                isTouched = true;
                if (event.getTarget().getName().equals("menuButtonX")) {
                    System.out.println("Left menu clicked");
                    boolean closed = slideMenuLeft.isCompletelyClosedX();
                    //image_backgroundX.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
                    slideMenuLeft.showManually(closed);
                }
                super.cancel(); return true;
            }});
        //Utils.addListeners(listener, icon_rate, icon_share, icon_music, icon_off_music, menuButton, image_background);


        /**     ****************************************BOTTOM SLIDING MENU*****************************************     **/
        slideMenuBottom = new SlideMenu(.7f*camWidth/2.75f,camHeight/7f,"down",camWidth,camHeight, 0);
        rootTable.addActor(slideMenuBottom);
        //final Image image_backgroundY = new Image(new SpriteDrawable(temp));
        menuButtonY = new Image(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("menuButton"));
        menuButtonY.rotateBy(90);menuButtonY.setWidth(menuButtonY.getWidth()*0.4f);menuButtonY.setHeight(menuButtonY.getHeight()*0.9f);menuButtonY.setColor(1,1,1,0.5f);
        menuButtonY.setOrigin(Align.center);
        //menuButtonActor=menuButtonY;
        rootTable.addActor(menuButtonY);
        slideMenuBottom.setMoveMenuButton(menuButtonY);



        slideMenuBottom.add(buyButton).grow().row();


        slideMenuBottom.add(menuButton).grow().row();

        slideMenuBottom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                isTouched = true;
                if (actor.equals(buyButton)) {
                    world.survivalToBuyMenu();
                } else if (actor.equals(menuButton))
                    world.survivalToMenu();
                else if (actor.equals(playButton))
                    world.buyMenuToSurvival();
            }
        });
        //slideMenuLeft.add().height(300f).row(); // empty space
        //slideMenuBottom.add(rateButton).pad(5).row();



        //slideMenuBottom.background(image_backgroundY.getDrawable());
        slideMenuBottom.top();

        //System.out.println(rateButton.getX()+" "+shareButton.getX());




        //slideMenuBottom.showManually(true); //show panel


        menuButtonY.setName("menuButtonY");
        //image_backgroundY.setName("IMAGE_BACKGROUNDY");

        /*slideMenuBottom.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Actor actor = event.getTarget();
                //System.out.println(32123132132321f);
                if (actor.getName().equals("BUY")) {
                    //Gdx.app.debug(TAG, "Rate button clicked.");
                    System.out.println("Rate button clicked.");
                    isTouched=true;
                } else if (actor.getName().equals("MENU")) {
                    //Gdx.app.debug(TAG, "Share button clicked.");
                    System.out.println("Share button clicked.");
                    isTouched=true;
                } else if (actor.getName().equals("IMAGE_BACKGROUNDY")){
                    System.out.println("backgroundY touched");
                    isTouched=true;
                }
                super.cancel();
            }
        });*/

        menuButtonY.addListener(new ClickListener(){//separate listener for touch up events
            public boolean touchDown(InputEvent event, float x, float y, int pnt, int btn) {
                isTouched = true;
                if (event.getTarget().getName().equals("menuButtonY")) {//have to be moving mouse slow enough to touch
                    boolean closed = slideMenuBottom.isCompletelyClosedY();
                    //image_backgroundY.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
                    slideMenuBottom.showManually(closed);
                    isTouched=true;
                }
                super.cancel(); return true;
            }});

        //image_backgroundY.addListener(listenerY);
        //slideMenuBottom.addListener(listenerY);
        //private static final String TAG = TestScreen.class.getSimpleName();

        //slideMenuBottom.pack();
        //slideMenuBottom.setVisible(true);
        //slideMenuBottom.setWidth(camWidth);
        //slideMenuBottom.align(Align.center|Align.top);
        //slideMenuBottom.setPosition(0, Gdx.graphics.getHeight());
        //slideMenuBottom.setFillParent(true);
    }
    public void disableOrEnableResourceButtons(ImageTextButton b,int amntPerTap,int curAmnt,int cap,Label limitLabel){
        int cost=Integer.parseInt(b.getText().toString());

        if (curAmnt+amntPerTap>cap) {
            b.setDisabled(true);
            b.getChildren().get(0).setColor(Color.RED);if (limitLabel!=null)limitLabel.setColor(Color.RED);
        } else if (world.gold<cost||(b==buyGoldButton&&world.diamond<cost)){
            b.setDisabled(true);
            b.getLabel().setColor(Color.RED);

        } else if (b.isDisabled()) {
            b.setDisabled(false);
            if (b.getChildren().get(0).getColor().equals(Color.RED)&&curAmnt+amntPerTap<=cap) {
                b.getChildren().get(0).setColor(Color.WHITE);if (limitLabel!=null)limitLabel.setColor(Color.WHITE);
            }
            if (b.getLabel().getColor().equals(Color.RED)&&(world.gold>=cost||(b==buyGoldButton&&world.diamond>=cost))){
                b.getLabel().setColor(Color.WHITE);
                //System.out.println("set "+b.getName()+" white because you can afford");
            }
        }
    }

    public void update(float delta){
        if (boughtItemsList!=null){
            for (MovingImageContainer i : boughtItemsList){
                i.update(delta, Airship.pos);
            }
        }

        disableOrEnableResourceButtons(buyGoldButton,Integer.parseInt(goldPerTap),totalGoldNum,Airship.goldValues[Airship.goldLvl],goldLimitLabel);
        disableOrEnableResourceButtons(buyDiamondButton,Integer.parseInt(diamondPerTap),totalDiamondNum,Airship.diamondValues[Airship.diamondLvl],diamondLimitLabel);
        disableOrEnableResourceButtons(buyFuelButton,Integer.parseInt(fuelPerTap),(int)totalFuelNum,Airship.fuelValues[Airship.fuelLvl],fuelLimitLabel);
        //System.out.println("amntWithoutBuying: "+Airship.ammo+", totalAmnt"+(int)totalAmmoNum+", cap: "+Airship.ammoValues[Airship.ammoLvl]);
        disableOrEnableResourceButtons(buyAmmoButton,Integer.parseInt(ammoPerTap),totalAmmoNum,Airship.ammoValues[Airship.ammoLvl],ammoLimitLabel);
        disableOrEnableResourceButtons(buyArmorButton,Integer.parseInt(armorPerTap),totalArmorNum,Airship.armorValues[Airship.armorLvl],null);
        disableOrEnableResourceButtons(buyHealthButton,Integer.parseInt(healthPerTap),totalHealthNum,Airship.healthValues[Airship.healthLvl],null);


        ammoLabel.setText(Airship.ammo);fuelLabel.setText(Integer.toString((int)Airship.fuel));
        if (anyUITouched())isTouched=true;//check if any non-listened ui like slidemenus(updated in stage.act) or touchpads were touched, made false if nothing is touched
        else isTouched=false;
        stage.act(delta);//check if listened ui was touched, move knobs and progressBars etc
    }
        /*if (!isTouched&&Gdx.input.justTouched()) {//if screen is touched and it is not ui
            if (!movPad.isTouched() && !movPad.isVisible() && InputHandler.scaleX(Gdx.input.getX()) < camWidth / 2) {
                movPad.setPosition(InputHandler.scaleX(Gdx.input.getX())-movPad.getWidth()/2, -(InputHandler.scaleY(Gdx.input.getY()) - camHeight)-movPad.getHeight()/2);
                movPad.setVisible(true);
                movPad.act(delta);
                System.out.println(1);
            } else if (!movPad.isTouched() && movPad.isVisible()) {
                System.out.println(2);
                movPad.setVisible(false);
            }

            if (!aimPad.isTouched() && !aimPad.isVisible() && InputHandler.scaleX(Gdx.input.getX()) > camWidth / 2) {
                aimPad.setPosition(InputHandler.scaleX(Gdx.input.getX())-aimPad.getWidth()/2, -(InputHandler.scaleY(Gdx.input.getY()) - camHeight)-aimPad.getHeight()/2);
                aimPad.setVisible(true);
                aimPad.touched=true;
                aimPad.act(delta);
            } else if (!aimPad.isTouched() && aimPad.isVisible()) {
                aimPad.setVisible(false);
            }
        }*/


    public void loadAllBuyButtonsTimerTasks(){
        giveDiamondGetGold = new Runnable() {
            @Override
            public void run() {
                if (boughtGoldNum >0) {
                    boughtGoldNum--;
                    boughtItemsList.add(new MovingImageContainer("gold", r.nextInt(360), null, true,
                            buyGoldButton.getImage().localToStageCoordinates(new Vector2(buyGoldButton.getImage().getWidth()/2,buyGoldButton.getImage().getHeight()/2))));
                } else giveDiamondGetGoldFuture.cancel(true);
            }
        };
        giveGoldGetDiamond = new Runnable() {
            @Override
            public void run() {
                if (boughtDiamondNum >0) {
                    boughtDiamondNum--;
                    boughtItemsList.add(new MovingImageContainer("diamond", 0, null, false,
                            buyDiamondButton.getImage().localToStageCoordinates(new Vector2(buyDiamondButton.getImage().getWidth()/2,buyDiamondButton.getImage().getHeight()/2))));
                } else giveGoldGetDiamondFuture.cancel(true);
            }
        };
        giveGoldGetArmor = new Runnable() {
            @Override
            public void run() {
                if (boughtArmorNum >0) {
                    boughtArmorNum--;
                    boughtItemsList.add(new MovingImageContainer("armor", 0, null, false,
                            buyArmorButton.getImage().localToStageCoordinates(new Vector2(buyArmorButton.getImage().getWidth()/2,buyArmorButton.getImage().getHeight()/2))));
                } else if (!giveGoldGetArmorFuture.isCancelled()){giveGoldGetArmorFuture.cancel(true);}
            }
        };
        giveGoldGetAmmo = new Runnable() {
            @Override
            public void run() {
                if (boughtAmmoNum >0) {
                    boughtAmmoNum--;
                    boughtItemsList.add(new MovingImageContainer("ammo", 0, null, false,
                            buyAmmoButton.getImage().localToStageCoordinates(new Vector2(buyAmmoButton.getImage().getWidth()/2,buyAmmoButton.getImage().getHeight()/2))));
                } else giveGoldGetAmmoFuture.cancel(true);
            }
        };
        giveGoldGetFuel = new Runnable() {
            @Override
            public void run() {
                if (boughtFuelNum > 0) {
                    boughtFuelNum--;
                    boughtItemsList.add(new MovingImageContainer("fuel", 0, null, false,
                            buyFuelButton.getImage().localToStageCoordinates(new Vector2(buyFuelButton.getImage().getWidth()/2,buyFuelButton.getImage().getHeight()/2))));
                } else {giveGoldGetFuelFuture.cancel(true);timer.remove(this);}
            }
        };
        giveGoldGetHealth = new Runnable() {
            @Override
            public void run() {
                if (boughtHealthNum >0) {
                    boughtHealthNum--;
                    boughtItemsList.add(new MovingImageContainer("health", 0, null, false,
                            buyHealthButton.getImage().localToStageCoordinates(new Vector2(buyHealthButton.getImage().getWidth()/2,buyHealthButton.getImage().getHeight()/2))));
                } else giveGoldGetHealthFuture.cancel(true);
            }
        };
    }
}
//aimPad.
//rootTable.row();

        /*
        Table buttonsTable = new Table();
        buttonsTable.defaults().pad(2.0f);
        TextButton dialogButton = new TextButton("Dialog", shadeSkin);
        buttonsTable.add(dialogButton);
        buttonsTable.row();
        buttonsTable.add(new TextButton("Toggle", shadeSkin, "toggle"));
        buttonsTable.row();
        buttonsTable.add(new TextButton("Start Game", shadeSkin, "round"));
        buttonsTable.row();

        Table subTable = new Table();
        subTable.defaults().pad(2.0f);
        subTable.add(new Button(shadeSkin, "music"));
        subTable.add(new Button(shadeSkin, "sound"));
        buttonsTable.add(subTable);
        rootTable.add(buttonsTable);

        Table checksTable = new Table();
        checksTable.defaults().pad(2.0f);
        checksTable.add(new CheckBox(" Standard Checkbox", shadeSkin));
        checksTable.row();
        checksTable.add(new CheckBox(" Switch Checkbox", shadeSkin, "switch"));
        checksTable.row();
        ButtonGroup radioButtonGroup = new ButtonGroup();
        Array<Button> buttons = new Array<Button>();
        for (int i = 1; i <= 5; i++) {
            buttons.add(new TextButton("Selection " + Integer.toString(i), shadeSkin, "radio"));
            checksTable.add(buttons.peek());
            checksTable.row();
            radioButtonGroup.add(buttons.peek());
        }
        checksTable.add();
        rootTable.add(checksTable);

        Table inputTable = new Table();
        inputTable.defaults().pad(2.0f);
        inputTable.add(new Label("Login Page", shadeSkin, "title-plain")).colspan(2);
        inputTable.row();
        inputTable.add(new Label(" Username: ", shadeSkin, "subtitle"));
        TextField textField = new TextField("", shadeSkin);
        textField.setMessageText("Username or Email");
        textField.setFocusTraversal(true);
        inputTable.add(textField);
        inputTable.row();
        inputTable.add(new Label(" Password: ", shadeSkin, "subtitle"));
        textField = new TextField("", shadeSkin);
        textField.setMessageText("Password");
        textField.setFocusTraversal(true);
        textField.setPasswordMode(true);
        textField.setPasswordCharacter('*');
        inputTable.add(textField);
        inputTable.row();
        inputTable.add(new Label("Choose a Server:", shadeSkin)).colspan(2).padTop(10.0f);
        inputTable.row();
        SelectBox selectBox = new SelectBox<String>(shadeSkin);
        selectBox.setItems(new Object[]{"United States (East)", "United States (West)", "China", "Europe", "Australia", "India"});
        selectBox.setMaxListCount(4);
        inputTable.add(selectBox).colspan(2);
        rootTable.add(inputTable);

        rootTable.row();
        */


        /*
        Tree tree = new Tree(shadeSkin);
        Node parentNode = new Node(new Label("Selection 1", shadeSkin));
        tree.add(parentNode);
        for (int i = 2; i <= 4; i++) {
            Node node = new Node(new Label("Selection " + i, shadeSkin));
            parentNode.add(node);
            parentNode = node;
        }
        tree.expandAll();
        tree.setPadding(2);
        rootTable.add(tree).fill();

        rootTable.row();
        Table progressTable = new Table();
        progressTable.defaults().pad(2.0f);
        Button leftButton = new Button(shadeSkin, "left");
        progressTable.add(leftButton);
        final ProgressBar progressBar = new ProgressBar(0, 100, 1, false, shadeSkin);
        progressBar.setValue(50);
        progressBar.setAnimateDuration(.3f);
        progressTable.add(progressBar);
        Button rightButton = new Button(shadeSkin, "right");
        progressTable.add(rightButton);
        rootTable.add(progressTable).padTop(5.0f).colspan(3);

        rootTable.row();
        Slider slider = new Slider(0, 100, 1, false, shadeSkin);
        slider.setValue(50);
        rootTable.add(slider).colspan(3).fillX().padTop(5.0f);

        rootTable.row();

        Window window = new Window("Window", shadeSkin);
        window.getTitleLabel().setAlignment(Align.center);
        ScrollPane scrollPane = new ScrollPane(new Label("ScrollPane. ScrollPane. ScrollPane.\nScrollPane. ScrollPane. ScrollPane.\nScrollPane. ScrollPane. ScrollPane.\nScrollPane. ScrollPane. ScrollPane.\nScrollPane. ScrollPane. ScrollPane.\n", shadeSkin), shadeSkin);
        scrollPane.setFadeScrollBars(false);
        ScrollPane androidScrollPane = new ScrollPane(new Label("Android ScrollPane. Android ScrollPane.\nAndroid ScrollPane. Android ScrollPane.\nAndroid ScrollPane. Android ScrollPane.\nAndroid ScrollPane. Android ScrollPane.\nAndroid ScrollPane. Android ScrollPane.\n", shadeSkin), shadeSkin, "android");
        SplitPane splitPane2 = new SplitPane(scrollPane, androidScrollPane, false, shadeSkin);


        Table infoTable = new Table();
        Label authorLabel = new Label("Created by Raymond \"Raeleus\" Buckley", shadeSkin, "error");
        authorLabel.setAlignment(Align.center);
        infoTable.add(authorLabel);
        infoTable.row();
        Label webLabel = new Label("Visit ray3k.com for more stuff!", shadeSkin, "optional");
        webLabel.setAlignment(Align.center);
        infoTable.add(webLabel);

        SplitPane splitPane = new SplitPane(infoTable, splitPane2, true, shadeSkin);
        window.add(splitPane);
        rootTable.add(window).colspan(3).padTop(5.0f);

        /*dialogButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                new Dialog("Shade UI", shadeSkin, "dialog") {
                    protected void result(Object object) {
                        System.out.println("Chosen: " + object);
                    }
                }.text("Are you sure?").button("Yes", true).button("No", false)
                        .key(Keys.ENTER, true).key(Keys.ESCAPE, false).show(stage).getTitleLabel().setAlignment(Align.center);
            }
        });

        leftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                progressBar.setValue(progressBar.getValue() - 5.0f);
            }
        });

        rightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                progressBar.setValue(progressBar.getValue() + 5.0f);
            }
        });
        */