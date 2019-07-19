package com.kredatus.flockblockers.NonGameHandlerScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.TweenAccessors.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;


public class SplashScreen implements Screen {
    private static TweenManager manager;
    private SpriteBatch batcher;
    private Sprite sprite, sunshine;
    private FlockBlockersMain game;
    public static final int width= 1;
    public SplashScreen(FlockBlockersMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        sprite = new Sprite(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("logo"));
        sprite.setColor(1, 1, 1, 0);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float desiredWidth = width * .7f;
        float scale = desiredWidth / sprite.getWidth();

        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
        sprite.setPosition((width / 2) - (sprite.getWidth() / 2), (height / 2)
                - (sprite.getHeight() / 2));
        setupTween();
        batcher = new SpriteBatch();
    }

    private void setupTween() {

        manager = new TweenManager();

        TweenCallback cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               // game.setScreen(new GameHandler(ski));
            }
        };
//1.8 originally
        Tween.to(sprite, SpriteAccessor.ALPHA, 1f).target(1)
                .ease(TweenEquations.easeInOutQuad).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).repeatYoyo(1, .0f)

                .start(manager);



        //Tween.to(sprite, SpriteAccessor.ALPHA, 2f).target(30)
                //.ease(TweenEquations.easeInOutQuad).repeatYoyo(1, .4f)
                //.start(manager);

    }

    public static  TweenManager getManager() {
        return manager;
    }

    @Override
    public void render(float delta) {
        manager.update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        sprite.draw(batcher);
        batcher.end();
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

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
