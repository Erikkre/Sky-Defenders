// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.GameObjects.Resources.MovingImageContainer;
import com.kredatus.flockblockers.GameObjects.Resources.Rank;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.NonGameHandlerScreens.Loader;
import com.kredatus.flockblockers.ui.SlideMenu;
import com.kredatus.flockblockers.ui.TouchRotatePad;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

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
    public static Touchpad movPad;
    public static TouchRotatePad aimPad;
    public static SlideMenu slideMenuLeft, slideMenuBottom;
    public Image menuButtonX, menuButtonY;
    public static boolean isTouched;
    float camWidth,camHeight;
    public GameWorld world;
    public TextButton buyButton,menuButton,playButton;
    public static Label fuelLabel,goldLabel,diamondLabel,ammoLabel, lvlLabel, rankNameLabel, roundLabel, waveLabel,airshipHealthLabel,airshipArmorLabel,expLabel;
    public static Stack expStack;
    public Random r = new Random();
    public ConcurrentLinkedQueue<MovingImageContainer> boughtItemsList= new ConcurrentLinkedQueue<MovingImageContainer>();
    //might want to implement a current stage for new screens
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
    Preferences prefs = Gdx.app.getPreferences("skyDefenders");
    public UiHandler(GameWorld world, float camWidth, float camHeight, Skin shadeSkin) {
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

        stage = (((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.stage);
        stage.addCaptureListener(new ClickListener() {//tocuh up anywhere on screen means not touching ui
            public void touchUp(InputEvent event, float x, float y, int pnt, int btn) {
                super.touchUp(event, x, y, pnt, btn);
                isTouched = false;
                super.cancel();
            }
        });
        stage.setDebugAll(true);

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
        Image rankImageBg=new Image(Loader.tA.findRegion("rankBg"));rankImageBg.setAlign(Align.center);
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

        airshipHealthSymbol=new Image(Loader.tA.findRegion("health"));table0.add(airshipHealthSymbol).size(40);

        Stack airshipHealthStack = new Stack();
        airshipHealthBar=new ProgressBar(0,Airship.healthValues[Airship.healthLvl],1,false, shadeSkin.get("default-horizontal", ProgressBar.ProgressBarStyle.class));
        airshipHealthBar.setColor(1,0,0,0.8f);expBar.setAnimateDuration(0.15f);airshipHealthBar.setValue(Airship.health);
        airshipHealthLabel= new Label(Integer.toString(Airship.health), shadeSkin,"title-plain");
        airshipHealthStack.add(airshipHealthBar);airshipHealthStack.add(airshipHealthLabel);airshipHealthLabel.setAlignment(Align.center);
        table0.add(airshipHealthStack).grow().padRight(5);

        airshipArmorSymbol=new Image(Loader.tA.findRegion("armor"));table0.add(airshipArmorSymbol).size(40);

        Stack airshipArmorStack = new Stack();
        airshipArmorBar=new ProgressBar(0,Airship.armorValues[Airship.armorLvl],1,false,shadeSkin.get("default-horizontal",ProgressBar.ProgressBarStyle.class));
        airshipArmorBar.setColor(0.2f,0.08f,0,0.8f);expBar.setAnimateDuration(0.15f);airshipArmorBar.setValue(Airship.armor);

        airshipArmorLabel= new Label(Integer.toString(Airship.armor), shadeSkin,"title-plain");
        airshipArmorStack.add(airshipArmorBar);airshipArmorStack.add(airshipArmorLabel);airshipArmorLabel.setAlignment(Align.center);
        table0.add(airshipArmorStack).grow().padRight(5);

        table0.row(); rankNameLabel=new Label(rank.rankNames[rank.lvl/78],shadeSkin,"title-plain");rankNameLabel.setFontScale(0.5f);table0.add(rankNameLabel).padLeft(2).padTop(2);


        goldSymbol=new Image(Loader.tA.findRegion("gold2"));
        table1.add(goldSymbol).size(40).padLeft(20);
        goldLabel= new Label(Integer.toString(world.gold), shadeSkin,"title-plain");
        table1.add(goldLabel).size((camWidth-(40*5))/4.2f,goldLabel.getPrefHeight()).padLeft(3);

        fuelSymbol=new Image(Loader.tA.findRegion("fuel"));
        table1.add(fuelSymbol).size(40);
        fuelLabel= new Label(Integer.toString((int)Airship.fuel), shadeSkin,"title-plain");
        table1.add(fuelLabel).size((camWidth-(40*5))/6f,fuelLabel.getPrefHeight()).padLeft(3);

        ammoSymbol=new Image(Loader.tA.findRegion("ammo"));
        table1.add(ammoSymbol).size(40);
        ammoLabel= new Label(Integer.toString(Airship.ammo), shadeSkin,"title-plain");
        table1.add(ammoLabel).size((camWidth-(40*5))/5f,ammoLabel.getPrefHeight()).padLeft(3);

        diamondSymbol=new Image(Loader.tA.findRegion("diamond"));
        table1.add(diamondSymbol).size(40);
        diamondLabel= new Label(Integer.toString(world.diamonds), shadeSkin,"title-plain");
        table1.add(diamondLabel).size(diamondLabel.getPrefWidth(),diamondLabel.getPrefHeight()).padLeft(3);

        /******************************************************************************************/



        movPad = new Touchpad(0, shadeSkin);
        movPad.setColor(1,1,1,0.25f);//touchpad.settouchpad.scaleBy(0.7f);
        shadeSkin.getDrawable("touchpad-knob").setMinWidth(50);shadeSkin.getDrawable("touchpad-knob").setMinHeight(50);
        //touchpad2.setColor(1,1,1,1f);

        //keep original height ratio but sized down with current width: .height((touchpad.getPrefHeight()*touchpad.getWidth())/touchpad.getPrefWidth())
        table2.add(movPad).width(camHeight/9f).height(camHeight/8.5f).left().bottom().expand();

        aimPad = new TouchRotatePad(0, shadeSkin);
        aimPad.setColor(1,1,1,0.25f);//touchpad.settouchpad.scaleBy(0.7f);
        //touchpad2.setColor(1,1,1,1f);

        table2.add(aimPad).width(camHeight/9f).height(camHeight/8.5f).right().bottom().expand();
        //change fill

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
        slideMenuLeft = new SlideMenu(camWidth/6.5f,camHeight/2f,"left",camWidth,camHeight,camHeight/35);//left or up
        final Table leftTable=new Table();leftTable.pad(-10);//leftTable.setFillParent(true);//leftTable.setWidth(camWidth/7f);
        final ScrollPane scrollPane=new ScrollPane(leftTable,shadeSkin,"android");//scrollPane.setWidth(camWidth/7f);
        scrollPane.setFillParent(true);scrollPane.setFadeScrollBars(true);scrollPane.setScrollBarPositions(false,false);scrollPane.setScrollingDisabled(true,false);
        slideMenuLeft.add(scrollPane).grow().pad(-10);//.width(camWidth/7f);
        //scrollPane.layout();scrollPane.layout();
        //Sprite temp=new Sprite(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("slideMenuBackground"));
        //temp.setColor(new Color(0,0,0,0.5f));
        //final Image image_backgroundX = new Image(new SpriteDrawable(temp));
        menuButtonX = new Image(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("menuButton"));

        ImageTextButton buyArmorButton=new ImageTextButton("50",shadeSkin,"buyArmor");
        buyArmorButton.clearChildren();
        buyArmorButton.add(buyArmorButton.getImage()).colspan(2);buyArmorButton.row();
        buyArmorButton.add(new Image(goldSymbol.getDrawable())).size(20);buyArmorButton.add(buyArmorButton.getLabel());
        leftTable.add(buyArmorButton).expandY().padTop(5).padBottom(5);leftTable.row();

        ImageTextButton buyAmmoButton=new ImageTextButton("10",shadeSkin,"buyAmmo");
        buyAmmoButton.clearChildren();
        buyAmmoButton.add(buyAmmoButton.getImage()).colspan(2);buyAmmoButton.row();
        buyAmmoButton.add(new Image(goldSymbol.getDrawable())).size(20);buyAmmoButton.add(buyAmmoButton.getLabel());
        leftTable.add(buyAmmoButton).expandY().padTop(5).padBottom(5);leftTable.row();

        ImageTextButton buyFuelButton=new ImageTextButton("10",shadeSkin,"buyFuel");
        buyFuelButton.clearChildren();
        buyFuelButton.add(buyFuelButton.getImage()).colspan(2);buyFuelButton.row();
        buyFuelButton.add(new Image(goldSymbol.getDrawable())).size(20);buyFuelButton.add(buyFuelButton.getLabel());
        leftTable.add(buyFuelButton).expandY().padTop(5).padBottom(5);leftTable.row();

        ImageTextButton buyHealthButton=new ImageTextButton("100",shadeSkin,"buyHealth");
        buyHealthButton.clearChildren();
        buyHealthButton.add(buyHealthButton.getImage()).colspan(2);buyHealthButton.row();
        buyHealthButton.add(new Image(goldSymbol.getDrawable())).size(20);buyHealthButton.add(buyHealthButton.getLabel());
        leftTable.add(buyHealthButton).expandY().padTop(5).padBottom(5);leftTable.row();

        ImageTextButton buyDiamondButton=new ImageTextButton("2000",shadeSkin,"buyDiamond");
        buyDiamondButton.clearChildren();
        buyDiamondButton.add(buyDiamondButton.getImage()).colspan(2);buyDiamondButton.row();
        buyDiamondButton.add(new Image(goldSymbol.getDrawable())).size(20);buyDiamondButton.add(buyDiamondButton.getLabel());
        leftTable.add(buyDiamondButton).expandY().padTop(5).padBottom(5);leftTable.row();



        //final Image rateButton = new Image(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("rateButton"));
        //final Image shareButton = new Image(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("shareButton"));



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

        menuButtonX.setName("menuButtonX");
        //image_backgroundX.setName("IMAGE_BACKGROUNDX");

        slideMenuLeft.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //super.touchUp(event, x, y, 0, 0);
                Actor actor = event.getTarget();
                //System.out.println(32123132132321f);
                if (actor.getName().equals("r")) {
                    boughtItemsList.add(new MovingImageContainer("diamond",r.nextInt(360), null, true,
                            slideMenuLeft.localToParentCoordinates(new Vector2(scrollPane.localToParentCoordinates( new Vector2(leftTable.localToParentCoordinates(new Vector2(
                            UiHandler.table0.getX()+UiHandler.expBar.getPercent()*UiHandler.expBar.getWidth(), UiHandler.table0.getY()) )))))));
                    isTouched=true;
                } else if (actor.getName().equals("a")) {
                    //Gdx.app.debug(TAG, "Share button clicked.");
                    System.out.println("Share button clicked.");
                    isTouched=true;
                } else if (actor.getName().contains("MUSIC")) {
                    //Gdx.app.debug(TAG, "Music button clicked.");
                    System.out.println("Music button clicked.");
                    //icon_music.setVisible(!icon_music.isVisible());
                    //icon_off_music.setVisible(!icon_off_music.isVisible());
                    isTouched=true;
                } else if (actor.getName().equals("IMAGE_BACKGROUNDX")){
                    System.out.println("backgroundX touched");
                    isTouched=true;
                }
                super.cancel();
            }
        });

        menuButtonX.addListener(new ClickListener(){//separate listener for touch up events
            public boolean touchDown(InputEvent event, float x, float y, int pnt, int btn) {
                System.out.println(Math.abs(Gdx.input.getDeltaX()));
                if (event.getTarget().getName().equals("menuButtonX")) {
                    System.out.println("Left menu clicked");
                    boolean closed = slideMenuLeft.isCompletelyClosedX();
                    //image_backgroundX.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
                    slideMenuLeft.showManually(closed);
                    isTouched=true;
                }
                super.cancel(); return true;
            }});
        //Utils.addListeners(listener, icon_rate, icon_share, icon_music, icon_off_music, menuButton, image_background);


        /**     ****************************************BOTTOM SLIDING MENU*****************************************     **/
        slideMenuBottom = new SlideMenu(.7f*camWidth/2.75f,camHeight/7f,"down",camWidth,camHeight, 0);
        rootTable.addActor(slideMenuBottom);
        //final Image image_backgroundY = new Image(new SpriteDrawable(temp));
        menuButtonY = new Image(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("menuButton"));
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
    public void update(float delta){
        stage.act(delta);//check if listened ui was touched, move knobs and progressBars etc
        ammoLabel.setText(Airship.ammo);fuelLabel.setText(Integer.toString((int)Airship.fuel));
        if (anyUITouched())isTouched=true;//check if any non-listened ui like slidemenus(updated in stage.act) or touchpads were touched, made false if nothing is touched
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