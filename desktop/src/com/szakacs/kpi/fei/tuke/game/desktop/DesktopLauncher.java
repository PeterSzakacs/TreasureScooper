package com.szakacs.kpi.fei.tuke.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.szakacs.kpi.fei.tuke.game.arena.TreasureScooper;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		TreasureScooper world = new TreasureScooper();
		config.width = world.getWidth();
		config.height = world.getHeight();
		config.title = "TreasureScooper";
		new LwjglApplication(world, config);
	}
}
