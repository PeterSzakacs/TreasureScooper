package szakacs.kpi.fei.tuke.player;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;

/**
 * Created by developer on 22.1.2017.
 */
public class PlayerNativeA implements Player {

    private PipeHead head;
    // again, this is all code the student can implement
    private Pipe pipe;

    private Direction currentDir;
    private TunnelCell currentPosition;
    private HorizontalTunnel currentTunnel;
    private TunnelCell entrance;
    private PlayerGameInterface world;

    public PlayerNativeA() {
    }

    @Override
    public void initialize(PlayerGameInterface gameInterface, Pipe pipe){
        this.pipe = pipe;
        this.head = pipe.getHead();
        this.currentDir = this.head.getDirection();
        this.world = gameInterface;
        this.currentPosition = pipe.getHead().getCurrentPosition();
        this.currentTunnel = null;
        this.entrance = null;
        System.loadLibrary("player");
        this.initializeNativeCode(gameInterface);
    }

    private native void initializeNativeCode(PlayerGameInterface world);

    @Override
    public native void act();

    @Override
    public native void deallocate();
}
