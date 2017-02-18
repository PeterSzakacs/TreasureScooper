package szakacs.kpi.fei.tuke.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.CoreGameRenderer;

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
        CoreGameRenderer game = null;
        try {
            game = new CoreGameRenderer("config.xml");
        } catch (ConfigProcessingException e) {
            e.printStackTrace();
            System.exit(1);
        }
        config.width = game.getWorld().getWidth();
        config.height = game.getWorld().getHeight();
        config.title = "TreasureScooperWorld";
        config.forceExit = true;
        new LwjglApplication(game, config);
    }
}
