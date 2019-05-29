// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.ui.SlideMenu;
import com.kredatus.flockblockers.ui.TouchRotatePad;

public class UiHandler {

    public static Table rootTable;
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
    private GameWorld myWorld;

    //might want to implement a current stage for new screens
    public  boolean anyUITouched(){
        for (Actor i : stage.getActors()){
            if (i instanceof SlideMenu)      {if (((SlideMenu) i).isTouched) return true;}
        }
        for (Actor i : rootTable.getChildren()){
            if (i instanceof Touchpad)      {if (((Touchpad) i).isTouched()) return true;}
            else if (i instanceof TouchRotatePad)      {if (((TouchRotatePad) i).isTouched()) return true;}
        }
        return false;
    }

    public UiHandler(float camWidth, float camHeight, GameWorld myWorld, Skin shadeSkin) {
        this.shadeSkin=shadeSkin;
        this.myWorld=myWorld;
        //nameLabel = new Label("Name: ", shadeSkin);
        //TextField nameText = new TextField("Name2: ", shadeSkin);
        //TextureAtlas

        // table = new Table();
        //table.add(nameLabel);              // Row 0, column 0.
        //table.add(nameText).width(100);    // Row 0, column 1.

        stage=(((FlockBlockersMain)Gdx.app.getApplicationListener()).loader.stage);
        stage.addCaptureListener(new ClickListener(){//tocuh up anywhere on screen means not touching ui
            public void touchUp(InputEvent event, float x, float y, int pnt, int btn) {
                super.touchUp(event, x, y, pnt, btn);
                isTouched=false;
                super.cancel();
            }});



        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center().align(Align.bottom);
        stage.addActor(rootTable);


        //rootTable.add(new Label("Shade UI", shadeSkin, "title")).colspan(3);
        rootTable.row();
        shadeSkin.getDrawable("loading-bar").setMinHeight(20);shadeSkin.getDrawable("loading-bar-fill").setMinHeight(20);
        ProgressBar loadBar = new ProgressBar(0, 1, 0.001f, false, shadeSkin);
        loadBar.setColor(1,0,0,0.5f);
        loadBar.setAnimateDuration(0.7f);

        loadBar.setWidth(camWidth/1.1f);
        loadBar.setPosition((camWidth-loadBar.getWidth())/2,camHeight-25);
        loadBar.setValue(.5f);//3.2% is the minimum value right now
        stage.addActor(loadBar);

        movPad = new Touchpad(0, shadeSkin);
        movPad.setColor(1,1,1,0.5f);//touchpad.settouchpad.scaleBy(0.7f);
        shadeSkin.getDrawable("touchpad-knob").setMinWidth(50);shadeSkin.getDrawable("touchpad-knob").setMinHeight(50);
        //touchpad2.setColor(1,1,1,1f);

        //keep original height ratio but sized down with current width: .height((touchpad.getPrefHeight()*touchpad.getWidth())/touchpad.getPrefWidth())
        rootTable.add(movPad).fill(true).width(camHeight/9f).height(camHeight/8.5f).padRight(camWidth-(camHeight/9f)*2);

        aimPad = new TouchRotatePad(0, shadeSkin);
        aimPad.setColor(1,1,1,0.25f);//touchpad.settouchpad.scaleBy(0.7f);
        //touchpad2.setColor(1,1,1,1f);

        rootTable.add(aimPad).fill(true).width(camHeight/9f).height(camHeight/8.5f);
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
        setupSlidingMenus(camWidth,camHeight);
        //stage.addCaptureListener(slideMenuLeft.getListeners().get(0));stage.addCaptureListener(slideMenuBottom.getListeners().get(0));
        //  stage.addCaptureListener(menuButtonX.getListeners().get(0));stage.addCaptureListener(men    uButtonY.getListeners().get(0));
    }

    public void setupSlidingMenus(float camWidth,float camHeight) {
        /*logo = tA.findRegion("companyLogo");
        slidemenuBg = tA.findRegion("slideMenuBackground");
        menuButton = tA.findRegion("menuButton");
        shareButton = tA.findRegion("shareButton");
        rateButton = tA.findRegion("rateButton");*/
        /**     ****************************************LEFT SLIDING MENU*****************************************     **/
        slideMenuLeft = new SlideMenu(camWidth/9f,camHeight/2f,"left",camWidth,camHeight,movPad.getHeight()/2);//left or down
        Sprite temp=new Sprite(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("slideMenuBackground"));
        temp.setColor(new Color(0,0,0,0.5f));
        final Image image_backgroundX = new Image(new SpriteDrawable(temp));
        menuButtonX = new Image(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("menuButton"));
        final Image rateButton = new Image(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("rateButton"));
        final Image shareButton = new Image(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("shareButton"));

        // add items into drawer panel.
        slideMenuLeft.add(shareButton).size(63, 85).pad(0, 52, 5, 52).expandX().row();
        //slideMenuLeft.add().height(300f).row(); // empty space
        slideMenuLeft.add(rateButton).pad(35, 52, 35, 52).expandX().row();
        //slideMenuLeft.add(icon_share).pad(35, 52, 35, 52).expandX().row();

        //icon_off_music.setVisible(false);
        //slideMenuLeft.stack(icon_music, icon_off_music).pad(52, 52, 300, 52).expandX().row(); //one on top of the other

        // setup attributes for menu navigation slideMenuLeft.
        slideMenuLeft.setBackground(image_backgroundX.getDrawable());
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
        menuButtonX.setWidth(menuButtonX.getWidth()*0.4f);menuButtonX.setHeight(menuButtonX.getHeight()*0.9f);menuButtonX.setColor(1,1,1,0.5f);
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

            rateButton.setName("RATE");
            shareButton.setName("SHARE");
            //icon_music.setName("MUSIC_ON");
            //icon_off_music.setName("MUSIC_OFF");

        menuButtonX.setName("menuButtonX");
        //image_backgroundX.setName("IMAGE_BACKGROUNDX");

        slideMenuLeft.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //super.touchUp(event, x, y, 0, 0);
                Actor actor = event.getTarget();
                //System.out.println(32123132132321f);
                if (actor.getName().equals("RATE")) {
                    //Gdx.app.debug(TAG, "Rate button clicked.");
                    System.out.println("Rate button clicked.");
                    isTouched=true;
                } else if (actor.getName().equals("SHARE")) {
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
                    image_backgroundX.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
                    slideMenuLeft.showManually(closed);
                    isTouched=true;
                }
                super.cancel(); return true;
            }});
        //Utils.addListeners(listener, icon_rate, icon_share, icon_music, icon_off_music, menuButton, image_background);


        /**     ****************************************BOTTOM SLIDING MENU*****************************************     **/
        slideMenuBottom = new SlideMenu(.7f*camWidth/2.75f,camHeight/7f,"down",camWidth,camHeight, 0);
        stage.addActor(slideMenuBottom);
        final Image image_backgroundY = new Image(new SpriteDrawable(temp));
        menuButtonY = new Image(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("menuButton"));
        menuButtonY.rotateBy(90);menuButtonY.setWidth(menuButtonY.getWidth()*0.4f);menuButtonY.setHeight(menuButtonY.getHeight()*0.9f);menuButtonY.setColor(1,1,1,0.5f);
        menuButtonY.setOrigin(Align.center);
        //menuButtonActor=menuButtonY;
        stage.addActor(menuButtonY);
        slideMenuBottom.setMoveMenuButton(menuButtonY);


        /******* BUTTONS ******/
        final TextButton buyButton = new TextButton("Buy", shadeSkin);    //set button style
        buyButton.setStyle(shadeSkin.get("round", buyButton.getStyle().getClass()));
        slideMenuBottom.add(buyButton).expand().fill().row();

        final TextButton menuButton = new TextButton("Menu", shadeSkin);    //set button style
        menuButton.setStyle(shadeSkin.get("round", menuButton.getStyle().getClass()));
        slideMenuBottom.add(menuButton).expand().fill().row();

        slideMenuBottom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (actor.equals(buyButton))
                    myWorld.buyMenu();
                else if (actor.equals(menuButton))
                    myWorld.menu();
            }
        });
        //slideMenuLeft.add().height(300f).row(); // empty space
        //slideMenuBottom.add(rateButton).pad(5).row();



        //slideMenuBottom.background(image_backgroundY.getDrawable());
        slideMenuBottom.top();

        //System.out.println(rateButton.getX()+" "+shareButton.getX());




        //slideMenuBottom.showManually(true); //show panel


        menuButtonY.setName("menuButtonY");
        image_backgroundY.setName("IMAGE_BACKGROUNDY");

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
                    image_backgroundY.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
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
        stage.act(delta);//check if listened ui was touched
        if (anyUITouched())isTouched=true;//check if any non-listened ui like slidemenus(updated in stage.act) or touchpads were touched, made false if nothing is touched
    }
}
