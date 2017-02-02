package com.szakacs.kpi.fei.tuke.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.szakacs.kpi.fei.tuke.game.enums.GameType;
import com.szakacs.kpi.fei.tuke.game.misc.CoreGameRenderer;

import java.lang.reflect.Field;

public class DesktopLauncher {

    static {
        // If we wanted to load our native player, we need to specify the library path.
        // This should theoretically work

        /*String dirSeparator = System.getProperty("file.separator");
        String javaLibPath = System.getProperty("java.library.path");
        String libpath = System.getProperty("user.dir") + dirSeparator + ".." + dirSeparator + "libs";
        System.out.println(libpath);
        System.setProperty("java.library.path", libpath);
        System.out.println(System.getProperty("java.library.path"));*/


        // However, java.library.path is one of those read-only properties of the System object,
        // so either you specify it outright in the VM arguments (-Djava.library.path=/path/to/lib),
        // or use a little reflection if you really need runtime changing of this library.

        /*String dirSeparator = System.getProperty("file.separator");
        System.setProperty("java.library.path", System.getProperty("user.dir") + dirSeparator + ".." + dirSeparator + "libs" );
        Field fieldSysPath;
        try {
            fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
            fieldSysPath.setAccessible( true );
            fieldSysPath.set( null, null );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }*/
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
