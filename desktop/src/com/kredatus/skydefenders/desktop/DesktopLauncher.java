package com.kredatus.skydefenders.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kredatus.skydefenders.SkyDefendersMain;

public class DesktopLauncher {
	public static void main (String[] arg) {


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width=562; config.height=1000; config.forceExit=true;
		new LwjglApplication(new SkyDefendersMain(), config);
	}
}
