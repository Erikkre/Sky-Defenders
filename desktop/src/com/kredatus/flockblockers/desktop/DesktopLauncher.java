package com.kredatus.flockblockers.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kredatus.flockblockers.FlockBlockersMain;

public class DesktopLauncher {
	public static void main (String[] arg) {


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		//config.width=800; config.height=480;
		new LwjglApplication(new FlockBlockersMain(), config);
	}
}
