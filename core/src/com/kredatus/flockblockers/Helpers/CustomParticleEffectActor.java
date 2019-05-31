// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Helpers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class CustomParticleEffectActor extends Actor {
    public ParticleEffect particleEffect;

    public CustomParticleEffectActor(ParticleEffect particleEffect) {
        super();
        this.particleEffect = particleEffect;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch,parentAlpha);
        particleEffect.draw(batch);
    }
    @Override
    public void setPosition (float x, float y) {
        super.setPosition(x,y);
        particleEffect.setPosition(x,y);
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        particleEffect.update(delta);
    }

    public void start() {
        particleEffect.start();
    }

    public void allowCompletion() {
        particleEffect.allowCompletion();
    }

}
