// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class UiHandler {

    private Stage stage;
    public Table table;
    public ScrollPane scrollPane;
    public Label nameLabel;
    //public Skin skin=new Skin(Gdx.files.internal("ui/button.png"));

    public UiHandler() {
        //nameLabel = new Label("Name: ", skin);
        //TextField nameText = new TextField("Name2: ", skin);
        //TextureAtlas

       // table = new Table();
        //table.add(nameLabel);              // Row 0, column 0.
        //table.add(nameText).width(100);    // Row 0, column 1.

    }
}
