package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class UiHandler {
    public ScrollPane scrollPane;
    public Label nameLabel;
    public Skin skin=new Skin(Gdx.files.internal("ui/button.png"));
    public static Table table;
    public UiHandler() {
        nameLabel = new Label("Name: ", skin);
        TextField nameText = new TextField("Name2: ", skin);


        table = new Table();
        table.add(nameLabel);              // Row 0, column 0.
        table.add(nameText).width(100);    // Row 0, column 1.

    }
}
