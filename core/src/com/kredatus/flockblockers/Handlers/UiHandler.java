// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.ui.SlideMenu;

import java.io.File;

public class UiHandler {

    public Table table;
    //public ScrollPane scrollPane;
    //public Label nameLabel;
    //public Skin skin=new Skin(Gdx.files.internal("ui/button.png"));
    public  Stage stage;
    public Skin skin;
    public static Touchpad movPad, aimPad;

    public UiHandler(Viewport viewport, SpriteBatch batcher, float camWidth, float camHeight) {
        //nameLabel = new Label("Name: ", skin);
        //TextField nameText = new TextField("Name2: ", skin);
        //TextureAtlas

        // table = new Table();
        //table.add(nameLabel);              // Row 0, column 0.
        //table.add(nameText).width(100);    // Row 0, column 1.


        skin = new Skin(Gdx.files.internal("ui"+File.separator+"shadeui"+File.separator+"uiskin.json"));
        stage = new Stage(viewport, batcher);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.align(Align.bottom);
        stage.addActor(rootTable);

        //rootTable.add(new Label("Shade UI", skin, "title")).colspan(3);
        rootTable.row();
        movPad = new Touchpad(0, skin);
        movPad.setColor(1,1,1,0.25f);//touchpad.settouchpad.scaleBy(0.7f);
        //touchpad2.setColor(1,1,1,1f);

        //keep original height ratio but sized down with current width: .height((touchpad.getPrefHeight()*touchpad.getWidth())/touchpad.getPrefWidth())
        rootTable.add(movPad).fill(true).width(camWidth/3.5f).height(camWidth/4f).padRight((camWidth*1.5f)/3.5f);

        aimPad = new Touchpad(0, skin);
        aimPad.setColor(1,1,1,0.25f);//touchpad.settouchpad.scaleBy(0.7f);
        //touchpad2.setColor(1,1,1,1f);

        rootTable.add(aimPad).fill(true).width(camWidth/3.5f).height(camWidth/4f);
        //aimPad.
        //rootTable.row();

        /*
        Table buttonsTable = new Table();
        buttonsTable.defaults().pad(2.0f);
        TextButton dialogButton = new TextButton("Dialog", skin);
        buttonsTable.add(dialogButton);
        buttonsTable.row();
        buttonsTable.add(new TextButton("Toggle", skin, "toggle"));
        buttonsTable.row();
        buttonsTable.add(new TextButton("Start Game", skin, "round"));
        buttonsTable.row();

        Table subTable = new Table();
        subTable.defaults().pad(2.0f);
        subTable.add(new Button(skin, "music"));
        subTable.add(new Button(skin, "sound"));
        buttonsTable.add(subTable);
        rootTable.add(buttonsTable);

        Table checksTable = new Table();
        checksTable.defaults().pad(2.0f);
        checksTable.add(new CheckBox(" Standard Checkbox", skin));
        checksTable.row();
        checksTable.add(new CheckBox(" Switch Checkbox", skin, "switch"));
        checksTable.row();
        ButtonGroup radioButtonGroup = new ButtonGroup();
        Array<Button> buttons = new Array<Button>();
        for (int i = 1; i <= 5; i++) {
            buttons.add(new TextButton("Selection " + Integer.toString(i), skin, "radio"));
            checksTable.add(buttons.peek());
            checksTable.row();
            radioButtonGroup.add(buttons.peek());
        }
        checksTable.add();
        rootTable.add(checksTable);

        Table inputTable = new Table();
        inputTable.defaults().pad(2.0f);
        inputTable.add(new Label("Login Page", skin, "title-plain")).colspan(2);
        inputTable.row();
        inputTable.add(new Label(" Username: ", skin, "subtitle"));
        TextField textField = new TextField("", skin);
        textField.setMessageText("Username or Email");
        textField.setFocusTraversal(true);
        inputTable.add(textField);
        inputTable.row();
        inputTable.add(new Label(" Password: ", skin, "subtitle"));
        textField = new TextField("", skin);
        textField.setMessageText("Password");
        textField.setFocusTraversal(true);
        textField.setPasswordMode(true);
        textField.setPasswordCharacter('*');
        inputTable.add(textField);
        inputTable.row();
        inputTable.add(new Label("Choose a Server:", skin)).colspan(2).padTop(10.0f);
        inputTable.row();
        SelectBox selectBox = new SelectBox<String>(skin);
        selectBox.setItems(new Object[]{"United States (East)", "United States (West)", "China", "Europe", "Australia", "India"});
        selectBox.setMaxListCount(4);
        inputTable.add(selectBox).colspan(2);
        rootTable.add(inputTable);

        rootTable.row();
        */


        /*
        Tree tree = new Tree(skin);
        Node parentNode = new Node(new Label("Selection 1", skin));
        tree.add(parentNode);
        for (int i = 2; i <= 4; i++) {
            Node node = new Node(new Label("Selection " + i, skin));
            parentNode.add(node);
            parentNode = node;
        }
        tree.expandAll();
        tree.setPadding(2);
        rootTable.add(tree).fill();

        rootTable.row();
        Table progressTable = new Table();
        progressTable.defaults().pad(2.0f);
        Button leftButton = new Button(skin, "left");
        progressTable.add(leftButton);
        final ProgressBar progressBar = new ProgressBar(0, 100, 1, false, skin);
        progressBar.setValue(50);
        progressBar.setAnimateDuration(.3f);
        progressTable.add(progressBar);
        Button rightButton = new Button(skin, "right");
        progressTable.add(rightButton);
        rootTable.add(progressTable).padTop(5.0f).colspan(3);

        rootTable.row();
        Slider slider = new Slider(0, 100, 1, false, skin);
        slider.setValue(50);
        rootTable.add(slider).colspan(3).fillX().padTop(5.0f);

        rootTable.row();

        Window window = new Window("Window", skin);
        window.getTitleLabel().setAlignment(Align.center);
        ScrollPane scrollPane = new ScrollPane(new Label("ScrollPane. ScrollPane. ScrollPane.\nScrollPane. ScrollPane. ScrollPane.\nScrollPane. ScrollPane. ScrollPane.\nScrollPane. ScrollPane. ScrollPane.\nScrollPane. ScrollPane. ScrollPane.\n", skin), skin);
        scrollPane.setFadeScrollBars(false);
        ScrollPane androidScrollPane = new ScrollPane(new Label("Android ScrollPane. Android ScrollPane.\nAndroid ScrollPane. Android ScrollPane.\nAndroid ScrollPane. Android ScrollPane.\nAndroid ScrollPane. Android ScrollPane.\nAndroid ScrollPane. Android ScrollPane.\n", skin), skin, "android");
        SplitPane splitPane2 = new SplitPane(scrollPane, androidScrollPane, false, skin);


        Table infoTable = new Table();
        Label authorLabel = new Label("Created by Raymond \"Raeleus\" Buckley", skin, "error");
        authorLabel.setAlignment(Align.center);
        infoTable.add(authorLabel);
        infoTable.row();
        Label webLabel = new Label("Visit ray3k.com for more stuff!", skin, "optional");
        webLabel.setAlignment(Align.center);
        infoTable.add(webLabel);

        SplitPane splitPane = new SplitPane(infoTable, splitPane2, true, skin);
        window.add(splitPane);
        rootTable.add(window).colspan(3).padTop(5.0f);

        /*dialogButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                new Dialog("Shade UI", skin, "dialog") {
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
    }

    public void setupSlidingMenus(float camWidth,float camHeight) {
        /**     ****************************************LEFT SLIDING MENU*****************************************     **/
        final SlideMenu slideMenuLeft = new SlideMenu(camWidth/9f,camHeight,"left",camWidth,camHeight);//left or down
        Sprite temp=new Sprite(AssetHandler.slidemenuBg);
        temp.setColor(new Color(0,0,0,0.5f));
        final Image image_backgroundX = new Image(new SpriteDrawable(temp));
        final Image menuButtonX = new Image(AssetHandler.menuButton);
        final Image rateButton = new Image(AssetHandler.rateButton);
        final Image shareButton = new Image(AssetHandler.shareButton);

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
        menuButtonX.setOrigin(Align.center);menuButtonX.setY(camHeight/2f);
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

            /*icon_rate.setName("RATE");
            icon_share.setName("SHARE");
            icon_music.setName("MUSIC_ON");
            icon_off_music.setName("MUSIC_OFF");*/
        menuButtonX.setName("menuButtonX");
        image_backgroundX.setName("IMAGE_BACKGROUNDX");

        ClickListener listenerX = new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                boolean closed = slideMenuLeft.isCompletelyClosedX();
                Actor actor = event.getTarget();
                //System.out.println(32123132132321f);
                if (actor.getName().equals("RATE")) {
                    //Gdx.app.debug(TAG, "Rate button clicked.");

                } else if (actor.getName().equals("SHARE")) {
                    //Gdx.app.debug(TAG, "Share button clicked.");


                } else if (actor.getName().contains("MUSIC")) {
                    //Gdx.app.debug(TAG, "Music button clicked.");

                    //icon_music.setVisible(!icon_music.isVisible());
                    //icon_off_music.setVisible(!icon_off_music.isVisible());
                } else if (actor.getName().equals("menuButtonX")||actor.getName().equals("IMAGE_BACKGROUNDY")) {
                    //Gdx.app.debug(TAG, "Menu button clicked.");

                    image_backgroundX.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
                    slideMenuLeft.showManually(closed);
                    System.out.println("leftSlideMenu clicked");
                    menuButtonX.rotateBy(180);
                }
            }
        };

        menuButtonX.addListener(listenerX);
        image_backgroundX.addListener(listenerX);
        //Utils.addListeners(listener, icon_rate, icon_share, icon_music, icon_off_music, menuButton, image_background);

        /**     ****************************************BOTTOM SLIDING MENU*****************************************     **/
        final SlideMenu slideMenuBottom = new SlideMenu(camWidth/2.5f,camHeight/8f,"down",camWidth,camHeight);
        final Image image_backgroundY = new Image(new SpriteDrawable(temp));
        final Image menuButtonY = new Image(AssetHandler.menuButton);



        slideMenuBottom.add(shareButton).pad(5).row();
        //slideMenuLeft.add().height(300f).row(); // empty space
        slideMenuBottom.add(rateButton).pad(5).row();


        slideMenuBottom.background(image_backgroundY.getDrawable());
        slideMenuBottom.top();


        System.out.println(rateButton.getX()+" "+shareButton.getX());


        stage.addActor(slideMenuBottom);
        menuButtonY.rotateBy(90);menuButtonY.setWidth(menuButtonY.getWidth()*0.4f);menuButtonY.setHeight(menuButtonY.getHeight()*0.9f);menuButtonY.setColor(1,1,1,0.5f);
        menuButtonY.setOrigin(Align.center);menuButtonY.setX(camWidth/2f-menuButtonX.getWidth()/2f);
        //menuButtonActor=menuButtonY;
        stage.addActor(menuButtonY);
        slideMenuBottom.setMoveMenuButton(menuButtonY);

        slideMenuBottom.showManually(true); //show panel

        menuButtonY.setName("menuButtonY");
        image_backgroundY.setName("IMAGE_BACKGROUNDY");

        ClickListener listenerY = new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                boolean closed = slideMenuBottom.isCompletelyClosedY();
                Actor actor = event.getTarget();
                //System.out.println(32123132132321f);
                if (actor.getName().equals("menuButtonY")||actor.getName().equals("IMAGE_BACKGROUNDY")) {
                    //Gdx.app.debug(TAG, "Menu button clicked.");
                    System.out.println("*********************************************************");
                    image_backgroundY.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
                    slideMenuBottom.showManually(closed);
                    System.out.println("leftSlideMenu clicked");
                }
            }
        };

        menuButtonY.addListener(listenerY);
        image_backgroundY.addListener(listenerY);
        //private static final String TAG = TestScreen.class.getSimpleName();

        //slideMenuBottom.pack();
        //slideMenuBottom.setVisible(true);
        //slideMenuBottom.setWidth(camWidth);
        //slideMenuBottom.align(Align.center|Align.top);
        //slideMenuBottom.setPosition(0, Gdx.graphics.getHeight());
        //slideMenuBottom.setFillParent(true);
    }
}
