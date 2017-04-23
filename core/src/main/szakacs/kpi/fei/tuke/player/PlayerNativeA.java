package szakacs.kpi.fei.tuke.player;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.player.common.AbstractPlayer;

/**
 * Created by developer on 22.1.2017.
 */
public abstract class PlayerNativeA extends AbstractPlayer {

    // again, this is all code the student can implement

    private Direction currentDir;
    private TunnelCellBasic currentPosition;
    private HorizontalTunnelBasic currentTunnel;
    private TunnelCell entrance;

    public PlayerNativeA() {
    }

    @Override
    public void initialize(PlayerGameInterface gameInterface, PipeBasic pipe, PlayerToken token){
        if (getToken().validate(token)) {
            super.initialize(gameInterface, pipe, token);
            this.currentDir = this.head.getDirection();
            this.currentPosition = pipe.getHead().getCurrentPosition();
            this.currentTunnel = null;
            this.entrance = null;
            System.loadLibrary("player");
            this.initializeNativeCode(gameInterface);
        }
    }

    private native void initializeNativeCode(PlayerGameInterface world);

    @Override
    public native void act(PlayerToken token);

    @Override
    public native void deallocate();
}
