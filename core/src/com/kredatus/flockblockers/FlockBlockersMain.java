/*
Copyright 2017 Erik Kredatus

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.kredatus.flockblockers;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Game;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.GlideOrDieHelpers.AssetLoader;
import com.kredatus.flockblockers.Screens.SplashScreen;
import com.uwsoft.editor.renderer.SceneLoader;

public class FlockBlockersMain extends Game {
    private SceneLoader s1;
    @Override
    public void create() {
        SceneLoader s1= new SceneLoader();
        s1.loadScene("MainScene");


        Gdx.app.log("GlideorDie", "created");
        AssetLoader.load();

        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {

        super.render();
        s1.getEngine().update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
        GameRenderer.dispose();
    }
}