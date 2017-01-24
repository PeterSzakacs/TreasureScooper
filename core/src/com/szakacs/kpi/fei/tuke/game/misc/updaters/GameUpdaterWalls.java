package com.szakacs.kpi.fei.tuke.game.misc.updaters;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.PipeSegment;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.Wall;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameUpdater;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by developer on 1.1.2017.
 */
public class GameUpdaterWalls implements GameUpdater {

    private final ManipulableGameInterface world;
    private List<HorizontalTunnel> tunnels;
    private List<Actor> createdWalls;
    private int turnCounter;
    private int turnBound;
    private int wallCountMax;
    private HorizontalTunnel previousTunnel;
    private List<TunnelCell> previousPicks;

    public GameUpdaterWalls(ManipulableGameInterface world) {
        this.world = world;
        this.tunnels = world.getTunnels();
        this.wallCountMax = calculateWallCountMax();
        this.createdWalls = new ArrayList<>(this.wallCountMax);
        this.turnCounter = 0;
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(26);
        } while (this.turnBound < 20);
        this.previousPicks = new ArrayList<>(10);
    }

    @Override
    public void update(ManipulableGameInterface world) {
        if (world != null && world.equals(this.world)) {
            turnCounter++;
            if (turnCounter > turnBound) {
                turnCounter = 0;
                if (createdWalls.size() < 20)
                    addWall();
            }
        }
    }

    private void addWall() {
        TunnelCell headPosition = world.getPipe().getHead().getCurrentPosition();
        List<PipeSegment> segmentStack = world.getPipe().getSegmentStack();
        List<TunnelCell> positions = new ArrayList<>(segmentStack.size());
        for (PipeSegment seg : segmentStack){
            positions.add(seg.getCurrentPosition());
        }
        // Pick a random horizontal tunnel different from the previous one
        Random rand = new Random();
        HorizontalTunnel ht;
        do {
            ht = tunnels.get(rand.nextInt(tunnels.size()));
        } while (ht == previousTunnel);
        previousTunnel = ht;

        // Pick a random cell from this tunnel different from previous picks
        // and one where there is no pipe segment
        List<TunnelCell> cells = ht.getCells();
        TunnelCell cell;
        if (previousPicks.size() == this.wallCountMax)
            previousPicks.clear();
        do {
            cell = cells.get(1 + rand.nextInt(cells.size() - 2));
        } while (previousPicks.contains(cell) || cell.equals(headPosition) || positions.contains(cell));
        previousPicks.add(cell);
        Wall wall = new Wall(cell, this.world, this::removeWall);
        world.registerActor(wall);
        this.createdWalls.add(wall);
    }

    private void removeWall(Actor wall){
        this.createdWalls.remove(wall);
        this.previousPicks.remove(wall.getCurrentPosition());
    }

    private int calculateWallCountMax(){
        int numCells = 0;
        for (HorizontalTunnel ht : this.tunnels){
            numCells += ht.getNumCells() - 2;
        }
        return numCells/6;
    }
}
