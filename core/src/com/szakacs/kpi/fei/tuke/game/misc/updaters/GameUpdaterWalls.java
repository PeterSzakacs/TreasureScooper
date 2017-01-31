package com.szakacs.kpi.fei.tuke.game.misc.updaters;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.PipeSegment;
import com.szakacs.kpi.fei.tuke.game.arena.world.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.world.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.arena.actors.Wall;
import com.szakacs.kpi.fei.tuke.game.enums.TunnelCellType;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by developer on 1.1.2017.
 *
 * Updater class to manage walls during the game,
 * specifically. their creation and destruction
 */
public class GameUpdaterWalls extends AbstractGameUpdater {

    private int turnCounter;

    // determines the number of iterations of the game loop
    // before creating a new wall.
    private int turnBound;

    // max. number of walls that can exist at the same time in the game
    private int wallCountMax;

    // current number of walls in the game world
    private int createdWallsCount;

    // list of all previous positions where a wall actor was put,
    // serves to prevent creating walls always at the same position
    private List<TunnelCell> previousPositions;

    // list of all positions where to put a wall Actor,
    // basically all TUNNEL type positions of all tunnels
    // in the game world,
    private List<TunnelCell> eligiblePositions;



    public GameUpdaterWalls(GamePrivileged game) {
        super(game);
        this.initialize();
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(31);
        } while (this.turnBound < 20);
    }

    public GameUpdaterWalls(GamePrivileged game, int turnBound) {
        super(game);
        this.initialize();
        this.turnBound = turnBound;
    }

    /**
     * Initializes the collections used in this class
     * and calculates the maximum number of walls
     * that can exist at the same time in the game
     */
    private void initialize(){
        this.turnCounter = 0;
        this.eligiblePositions = new ArrayList<>();
        for (HorizontalTunnel ht : gameWorld.getTunnels()){
            eligiblePositions.addAll(ht.getCellsBySearchCriteria(
                    (cell) ->
                            cell.getCellType() == TunnelCellType.TUNNEL
                    )
            );
        }
        this.wallCountMax = eligiblePositions.size()/6;
        this.previousPositions = new ArrayList<>(wallCountMax);
    }



    /**
     * Updates the game by creating new walls
     * at turnBound-th iterations of the game
     * loop.
     */
    @Override
    public void update() {
        turnCounter++;
        if (turnCounter > turnBound && createdWallsCount < wallCountMax) {
            turnCounter = 0;
            addWall();
        }
    }

    /**
     * Creates a new wall Actor and registers it
     * in the list of actors of the actor manager
     */
    private void addWall() {
        // get all positions, where the pipe is located, including the head
        List<PipeSegment> segmentStack = actorManager.getPipe().getSegmentStack();
        List<TunnelCell> positions = new ArrayList<>(segmentStack.size());
        for (PipeSegment seg : segmentStack){
            positions.add(seg.getCurrentPosition());
        }
        positions.add(actorManager.getPipe().getHead().getCurrentPosition());

        // pick a random TunnelCell where to put the new wall actor
        TunnelCell cell; Random rand = new Random();
        if (previousPositions.size() == wallCountMax)
            previousPositions.clear();
        do {
            cell = eligiblePositions.get(rand.nextInt(eligiblePositions.size()));
        } while (previousPositions.contains(cell) || positions.contains(cell));
        previousPositions.add(cell);

        // register the wall actor
        actorManager.registerActor(
                new Wall(cell, super.actorManager.getActorGameProxy(), this::removeWall)
        );
        this.createdWallsCount++;
    }

    /**
     * Callback function that the wall shall call when
     * it removes itself from the game.
     *
     * @param wall the wall to remove (the caller basically)
     */
    private void removeWall(Actor wall){
        this.createdWallsCount--;
        this.previousPositions.remove(wall.getCurrentPosition());
    }
}
