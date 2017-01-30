package com.szakacs.kpi.fei.tuke.game.arena.actors;

import com.szakacs.kpi.fei.tuke.game.arena.world.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.callbacks.OnActorRemovedCallback;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorld;

import java.util.List;

/**
 * Created by developer on 31.12.2016.
 */
public class Wall extends AbstractActor {
    private TunnelCell neighbouringCell;
    private OnActorRemovedCallback onDestroyCallback;

    public Wall(TunnelCell cell, ActorGameInterface world, OnActorRemovedCallback onDestroyCallback) {
        super(world, ActorType.WALL);
        super.initialize(Direction.LEFT, cell);
        this.neighbouringCell = cell.getCellAtDirection(Direction.RIGHT);
        this.onDestroyCallback = onDestroyCallback;
        this.disconnectCells(cell);
        System.out.println("Wall<init>()");
    }

    @Override
    public void act(ActorGameInterface world) {
        if (world != null && world.equals(super.world)) {
            List<Actor> intersecting = super.world.getActorsBySearchCriteria(actor ->
                    actor.getType() == ActorType.BULLET
                            && this.intersects(actor));
            if (!intersecting.isEmpty()) {
                super.world.unregisterActor(this);
            }
        }
        //System.out.println("Wall.act()");
    }

    @Override
    public boolean intersects(Actor actor){
        return super.intersects(actor)
                || neighbouringCell.equals(actor.getCurrentPosition());
    }

    private void disconnectCells(TunnelCell currentPosition){
        this.neighbouringCell.setAtDirection(Direction.LEFT, null, super.world.getGameWorld());
        currentPosition.setAtDirection(Direction.RIGHT, null, super.world.getGameWorld());
        world.setOnDestroy(this, this::reconnectCells);
    }

    private void reconnectCells(){
        System.out.println("Wall.reconnectCells()");
        TunnelCell cell = super.getCurrentPosition();
        cell.setAtDirection(Direction.RIGHT, this.neighbouringCell, super.world.getGameWorld());
        this.neighbouringCell.setAtDirection(Direction.LEFT, cell, super.world.getGameWorld());
        this.onDestroyCallback.onRemove(this);
    }
}
