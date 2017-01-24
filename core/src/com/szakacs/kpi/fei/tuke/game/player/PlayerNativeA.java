package com.szakacs.kpi.fei.tuke.game.player;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.PipeHead;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Player;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.QueryableGameInterface;

/**
 * Created by developer on 22.1.2017.
 */
public class PlayerNativeA implements Player {

    private final PipeHead head;
    // again, this is all code the student can implement
    private Pipe pipe;

    private Direction currentDir;
    private TunnelCell currentPosition;
    private HorizontalTunnel currentTunnel;
    private TunnelCell entrance;
    private QueryableGameInterface world;

    public PlayerNativeA(QueryableGameInterface world, Pipe pipe) {
        this.pipe = pipe;
        this.head = pipe.getHead();
        this.currentDir = this.head.getDirection();
        this.world = world;
        this.currentPosition = pipe.getHead().getCurrentPosition();
        this.currentTunnel = null;
        this.entrance = null;
        System.loadLibrary("player");
        this.initialize();
    }

    @Override
    public native void act();

    @Override
    public native void initialize();

    @Override
    public native void deallocate();
}
