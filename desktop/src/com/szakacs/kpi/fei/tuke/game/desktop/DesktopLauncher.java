package com.szakacs.kpi.fei.tuke.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.szakacs.kpi.fei.tuke.game.enums.GameType;
import com.szakacs.kpi.fei.tuke.game.misc.CoreGameRenderer;

public class DesktopLauncher {

    static {
        String dirSeparator = System.getProperty("file.separator");
        String javaLibPath = System.getProperty("java.library.path");
        String libpath = System.getProperty("user.dir")/* + dirSeparator + ".." + dirSeparator + "libs"*/;
        System.setProperty("java.library.path", javaLibPath + System.getProperty("path.separator") + libpath);
        System.out.println(System.getProperty("java.library.path"));
    }

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        CoreGameRenderer game = new CoreGameRenderer(GameType.ULTIMATE, "config.xml");
        config.width = game.getWorld().getWidth();
        config.height = game.getWorld().getHeight();
        config.title = "TreasureScooperWorld";
        new LwjglApplication(game, config);
    }
}
