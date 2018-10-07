package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class Background extends Scrollable{
    public int width, height;
    public Background(float x, float y, int width, int height, TextureRegion texture) {
        super(x, y, width, height, texture);
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }

    public void onRestart(float xpos, float ypos) {
        x = xpos;
        y = ypos;
    }
}