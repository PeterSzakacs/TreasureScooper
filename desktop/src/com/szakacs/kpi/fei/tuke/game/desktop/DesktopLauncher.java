package com.szakacs.kpi.fei.tuke.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.szakacs.kpi.fei.tuke.game.misc.GameRenderer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		GameRenderer game = new GameRenderer();
		config.width = game.getWorld().getWidth();
		config.height = game.getWorld().getHeight();
		config.title = "TreasureScooper";
		new LwjglApplication(game, config);
	}
}
