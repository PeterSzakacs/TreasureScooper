package com.szakacs.kpi.fei.tuke.game.arena.tunnels;

import com.szakacs.kpi.fei.tuke.game.arena.actors.AbstractActor;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

/**
 * Created by developer on 31.12.2016.
 */
public class Wall extends AbstractActor {
    private TunnelCell neighbouringCell;

    public Wall(TunnelCell cell, ManipulableGameInterface world) {
        super(world, ActorType.WALL);
        this.neighbouringCell = cell.getCellAtDirection(Direction.RIGHT);
        super.initialize(Direction.LEFT, cell);
        this.neighbouringCell.setAtDirection(super.world, Direction.LEFT, null);
        cell.setAtDirection(super.world, Direction.RIGHT, null);
    }

    @Override
    public void act(ManipulableGameInterface world) {

    }

    private void reconnectCells(){
        TunnelCell cell = super.getCurrentPosition();
        cell.setAtDirection(super.world, Direction.RIGHT, this.neighbouringCell);
        this.neighbouringCell.setAtDirection(super.world, Direction.LEFT, cell);
    }
}
