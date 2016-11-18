package com.szakacs.kpi.fei.tuke.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.szakacs.kpi.fei.tuke.game.arena.TreasureScooper;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TreasureScooper.WIDTH;
		config.height = TreasureScooper.HEIGHT;
		config.title = "TreasureScooper";
		new LwjglApplication(new TreasureScooper(), config);
	}
}
