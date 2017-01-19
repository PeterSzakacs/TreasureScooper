package com.szakacs.kpi.fei.tuke.game.arena.tunnels;

import com.szakacs.kpi.fei.tuke.game.arena.actors.AbstractActor;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.OnActorRemovedCallback;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.List;

/**
 * Created by developer on 31.12.2016.
 */
public class Wall extends AbstractActor {
    private TunnelCell neighbouringCell;
    private OnActorRemovedCallback onDestroyCallback;

    public Wall(TunnelCell cell, ManipulableGameInterface world, OnActorRemovedCallback onDestroyCallback) {
        super(world, ActorType.WALL);
        super.initialize(Direction.LEFT, cell);
        this.neighbouringCell = cell.getCellAtDirection(Direction.RIGHT);
        this.onDestroyCallback = onDestroyCallback;
        this.disconnectCells(cell);
        System.out.println("Wall<init>()");
    }

    @Override
    public void act(ManipulableGameInterface world) {
        List<Actor> intersecting = world.getActorsBySearchCriteria(actor ->
                actor.getType() == ActorType.BULLET
                        && this.intersects(actor));
        if (!intersecting.isEmpty()){
            world.unregisterActor(this);
        }
        //System.out.println("Wall.act()");
    }

    @Override
    public boolean intersects(Actor actor){
        return super.intersects(actor)
                || neighbouringCell.equals(actor.getCurrentPosition());
    }

    private void disconnectCells(TunnelCell currentPosition){
        this.neighbouringCell.setAtDirection(super.world, Direction.LEFT, null);
        currentPosition.setAtDirection(super.world, Direction.RIGHT, null);
        world.setOnDestroy(this, this::reconnectCells);
    }

    private void reconnectCells(){
        System.out.println("Wall.reconnectCells()");
        TunnelCell cell = super.getCurrentPosition();
        cell.setAtDirection(super.world, Direction.RIGHT, this.neighbouringCell);
        this.neighbouringCell.setAtDirection(super.world, Direction.LEFT, cell);
        this.onDestroyCallback.onRemove(this);
    }
}
