package com.szakacs.kpi.fei.tuke.game.player;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.PipeHead;
import com.szakacs.kpi.fei.tuke.game.arena.world.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.world.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Player;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.PlayerGameInterface;

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

    public PlayerNativeA(PlayerGameInterface world) {
    }

    @Override
    public void initialize(PlayerGameInterface world){
        this.pipe = world.getPipe();
        this.head = pipe.getHead();
        this.currentDir = this.head.getDirection();
        this.world = world;
        this.currentPosition = pipe.getHead().getCurrentPosition();
        this.currentTunnel = null;
        this.entrance = null;
        System.loadLibrary("player");
        this.initializeNativeCode(world);
    }

    private native void initializeNativeCode(PlayerGameInterface world);

    @Override
    public native void act();

    @Override
    public native void deallocate();
}
