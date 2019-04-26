// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Turret;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.LightHandler;
import com.kredatus.flockblockers.Handlers.TargetHandler;
import com.kredatus.flockblockers.Handlers.TinyBirdHandler;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.TweenAccessors.BirdAccessor;
import com.kredatus.flockblockers.TweenAccessors.LightAccessor;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;
import com.kredatus.flockblockers.TweenAccessors.VectorAccessor;
import com.kredatus.flockblockers.ui.SlideMenu;

import aurelienribon.tweenengine.Tween;
import box2dLight.Light;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */

public class GameHandler implements Screen {
    private GameWorld world;
    private GameRenderer renderer;
    public BgHandler bgHandler;
    private BirdHandler birdHandler;
    private TargetHandler targetHandler;
    //private TurretHandler turretHandler;
    private TinyBirdHandler tinyBirdHandler;
    public UiHandler uiHandler;
    public LightHandler lightHandler;
    public static float runTime;
    public static int camWidth, camHeight;
    public boolean isPaused=false;
    public static double timeOfPause, timeOfResume;
    public int birdType = FlockBlockersMain.birdType;
    private Airship airship;
    private Actor menuButtonActor;
    public GameHandler() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        camHeight=1000;
        camWidth= (int) (camHeight* (screenWidth/screenHeight)) ;
        //System.out.println("width: "+camWidth);


        Tween.registerAccessor(Value.class, new ValueAccessor());
        Tween.registerAccessor(BirdAbstractClass.class, new BirdAccessor());
        Tween.registerAccessor(Vector2.class, new VectorAccessor());
        Tween.registerAccessor(Light.class, new LightAccessor());
        Tween.setWaypointsLimit(10);


        tinyBirdHandler = new TinyBirdHandler();
        bgHandler = new BgHandler(camWidth, camHeight, birdType);
        birdHandler= new BirdHandler(bgHandler, camWidth, camHeight, birdType);

        //turretHandler = new TurretHandler(camWidth, camHeight);
        lightHandler= new LightHandler(bgHandler);
        airship=new Airship(camWidth, camHeight, birdType);
        targetHandler = new TargetHandler(airship);

        world = new GameWorld(airship, camWidth, camHeight, bgHandler,birdHandler, targetHandler,tinyBirdHandler,uiHandler, lightHandler);


        renderer = new GameRenderer(world, camWidth, camHeight);

        bgHandler.setRendererAndCam(renderer);
        lightHandler.setCam(renderer);

        world.setRenderer(renderer);

        uiHandler=new UiHandler(renderer.viewport, renderer.batcher, camWidth, camHeight);
        InputHandler inputHandler=new InputHandler(world, screenWidth / camWidth, screenHeight / camHeight, camWidth, camHeight);
        Gdx.input.setInputProcessor(uiHandler.stage);
        renderer.assignButtonsUsingInputHandler(inputHandler);


        //private static final String TAG = TestScreen.class.getSimpleName();

        final SlideMenu slideMenu = new SlideMenu(camWidth/9f,camHeight,"left",camWidth,camHeight);//left or down
        Sprite temp=new Sprite(AssetHandler.slidemenuBg);
        temp.setColor(new Color(0,0,0,0.5f));
        final Image image_backgroundX = new Image(new SpriteDrawable(temp));
        final Image menuButtonX = new Image(AssetHandler.menuButton);
        final Image shareButton = new Image(AssetHandler.shareButton);
        final Image rateButton = new Image(AssetHandler.rateButton);

        // add items into drawer panel.
        slideMenu.add(shareButton).size(63, 85).pad(0, 52, 5, 52).expandX().row();
        //slideMenu.add().height(300f).row(); // empty space
        slideMenu.add(rateButton).pad(35, 52, 35, 52).expandX().row();
        //slideMenu.add(icon_share).pad(35, 52, 35, 52).expandX().row();

        //icon_off_music.setVisible(false);
        //slideMenu.stack(icon_music, icon_off_music).pad(52, 52, 300, 52).expandX().row(); //one on top of the other

        // setup attributes for menu navigation slideMenu.
        slideMenu.setBackground(image_backgroundX.getDrawable());
        slideMenu.top().left();
        //slideMenu.setWidthStartDrag(0);
        //slideMenu.setWidthBackDrag(0);
        //slideMenu.setTouchable(Touchable.enabled);

        /* z-index = 1 */
        // add image_background as a separating actor into stage to make smooth shadow with dragging value.
        //image_background.setWidth(slideMenu.getWidth()*0.1f);image_background.setHeight(slideMenu.getHeight());
        //uiHandler.stage.addActor(image_background);
        //slideMenu.setFadeBackground(image_background, 0.5f);

        /* z-index = 2 */
        uiHandler.stage.addActor(slideMenu);

        /* z-index = 3 */
        // add button_menu as a separating actor into stage to rotates with dragging value.
        menuButtonX.setPosition(30,camHeight/2f);menuButtonX.setWidth(menuButtonX.getWidth()*0.4f);menuButtonX.setHeight(menuButtonX.getHeight()*0.9f);menuButtonX.setColor(1,1,1,0.5f);
        menuButtonX.setOrigin(Align.center);
        menuButtonActor=menuButtonX;
        uiHandler.stage.addActor(menuButtonX);
        slideMenu.setMoveMenuButton(menuButtonX);
        //slideMenu.setRotateMenuButton(menuButtonX, 90f);

        /** Optional
         Image image_shadow = new Image(atlas.findRegion("image_shadow"));
         image_shadow.setHeight(NAV_HEIGHT);
         image_shadow.setX(NAV_WIDTH);
         slideMenu.setAreaWidth(NAV_WIDTH + image_shadow.getWidth());
         slideMenu.addActor(image_shadow);

         // show the panel
         slideMenu.showManually(true);
         /************ add item listener ***********/


            /*icon_rate.setName("RATE");
            icon_share.setName("SHARE");
            icon_music.setName("MUSIC_ON");
            icon_off_music.setName("MUSIC_OFF");*/
            menuButtonX.setName("menuButtonX");
            image_backgroundX.setName("IMAGE_BACKGROUNDX");

            ClickListener listenerX = new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    boolean closed = slideMenu.isCompletelyClosedX();
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
                    } else if (actor.getName().equals("menuButtonX")) {
                        //Gdx.app.debug(TAG, "Menu button clicked.");

                        image_backgroundX.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
                        slideMenu.showManually(closed);
                        System.out.println("**************************************************************lol");
                        menuButtonX.rotateBy(180);
                    }
                }
            };

            menuButtonX.addListener(listenerX);
        image_backgroundX.addListener(listenerX);
        //Utils.addListeners(listener, icon_rate, icon_share, icon_music, icon_off_music, menuButton, image_background);
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            runTime += delta;
            world.update(delta, runTime);
            renderer.render(delta, runTime);

            //Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
            //            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            uiHandler.stage.act(delta);
            uiHandler.stage.draw();
            renderer.batcher.setColor(Color.WHITE);
        }
    }

    @Override
    public void resize(int width, int height) {
        uiHandler.stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.app.log( Integer.toString(Gdx.graphics.getWidth()),Integer.toString(Gdx.graphics.getHeight()) );
        Gdx.app.log("GameHandler", "show called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameHandler", "hide called");
    }

    @Override
    public void pause() {
        Gdx.app.log("GameHandler", "pause called");
        if (!FlockBlockersMain.dontPauseOnUnfocus){
            isPaused=true;

            timeOfPause=System.currentTimeMillis();
            for (Turret i : airship.turretList){
                if (i.firing) {
                    i.stopFiring();
                    i.firingStoppedByGamePause = true;
                }
            }

            birdHandler.pause();
            AssetHandler.stopMusic(AssetHandler.menumusiclist);
        }
    }

    @Override
    public void resume() {
        Gdx.app.log("GameHandler", "resume called");
        AssetHandler.startMusic(AssetHandler.menumusiclist);
        if (!FlockBlockersMain.dontPauseOnUnfocus) {
            isPaused = false;

            timeOfResume = System.currentTimeMillis();    //has to be before turrets restart firing so know how much longer left of interval before fire next shot

            for (Turret i : airship.turretList) {
                if (i.firingStoppedByGamePause) {
                    i.startFiring();
                    i.firingStoppedByGamePause = false;
                }
            }

            birdHandler.resume();
        }
    }

    @Override
    public void dispose() {
        uiHandler.stage.dispose();
        uiHandler.skin.dispose();
    }

}