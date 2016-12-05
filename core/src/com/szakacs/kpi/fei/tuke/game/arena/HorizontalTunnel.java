package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.TunnelCellType;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.GoldCollector;
import com.szakacs.kpi.fei.tuke.game.intrfc.callbacks.TunnelEventHandler;
import com.szakacs.kpi.fei.tuke.game.misc.DummyTunnel;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by developer on 5.11.2016.
 */
public class HorizontalTunnel {
    private int x;
    private int y;
    private List<TunnelCell> cells;
    private List<TunnelCell> entrances;
    private List<TunnelCell> exits;
    private List<Enemy> enemies;
    private List<Enemy> searchResults;
    private int turnCounter;
    private int turnBound;
    private int nuggetCount;
    private ManipulableGameInterface world;

    private class TunnelHandler implements TunnelEventHandler {
        @Override
        public void onEnemyDestroyed(Enemy enemy) {
            HorizontalTunnel.this.enemies.remove(enemy);
            HorizontalTunnel.this.worldCallback.onEnemyDestroyed(enemy);
        }
        @Override
        public void onNuggetCollected(GoldCollector collector) {
            HorizontalTunnel.this.nuggetCount--;
            HorizontalTunnel.this.worldCallback.onNuggetCollected(collector);
        }
    }
    private TunnelHandler handler;
    private TunnelEventHandler worldCallback;

    /*
     * begin builder methods
     */

    HorizontalTunnel(DummyTunnel dt, ManipulableGameInterface world, TunnelEventHandler worldCallback){
        this.x = dt.getXIndex()*world.getOffsetX();
        this.y = dt.getYIndex()*world.getOffsetY();
        this.world = world;
        this.handler = new TunnelHandler();
        this.worldCallback = worldCallback;
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(200);
        } while (this.turnBound < 100);
        this.buildTunnel(dt.getNumCells());
        this.enemies = new ArrayList<>(3);
        this.searchResults = new ArrayList<>(3);
        this.nuggetCount = dt.getNumCells();
    }

    private void buildTunnel(int numCells){
        this.entrances = new ArrayList<>(3);
        this.exits = new ArrayList<>(3);
        this.cells = new ArrayList<>(numCells);
        TunnelCell rightmostCell, prevCell, newCell;
        int rightEdge = this.x + (numCells - 1) * world.getOffsetX();
        prevCell = new TunnelCell(this.x, this.y, TunnelCellType.LEFT_EDGE, this.handler);
        this.cells.add(prevCell);
        for (int x = this.x + world.getOffsetX(); x < rightEdge; x += world.getOffsetX()){
            newCell = new TunnelCell(x, this.y, TunnelCellType.TUNNEL, this.handler);
            this.cells.add(newCell);
            newCell.setAtDirection(Direction.LEFT, prevCell);
            prevCell.setAtDirection(Direction.RIGHT, newCell);
            prevCell = newCell;
        }
        rightmostCell = new TunnelCell(
                rightEdge, this.y, TunnelCellType.RIGHT_EDGE, this.handler);
        rightmostCell.setAtDirection(Direction.LEFT, prevCell);
        prevCell.setAtDirection(Direction.RIGHT, rightmostCell);
        this.cells.add(rightmostCell);
    }

    void addInterconnects(Map<Integer, HorizontalTunnel> interconnects) {
        if (interconnects == null || interconnects.isEmpty())
            return;
        for (Integer xCoord : interconnects.keySet()){
            HorizontalTunnel exitTunnel = interconnects.get(xCoord);
            int idx = xCoord/world.getOffsetX();
            TunnelCell removed = this.cells.get(idx);
            TunnelCell added = new TunnelCell(removed.getX(), removed.getY(), TunnelCellType.EXIT, this.handler);
            TunnelCell prevCell = added, nextCell;
            this.cells.set(idx, added);
            this.exits.add(added);
            TunnelCell left = removed.getCellAtDirection(Direction.LEFT);
            TunnelCell right = removed.getCellAtDirection(Direction.RIGHT);
            added.setAtDirection(Direction.LEFT, left);
            added.setAtDirection(Direction.RIGHT, right);
            left.setAtDirection(Direction.RIGHT, added);
            right.setAtDirection(Direction.LEFT, added);
            for (int y = this.y - world.getOffsetY(); y > exitTunnel.getY(); y -= world.getOffsetY()){
                nextCell = new TunnelCell(xCoord, y, TunnelCellType.INTERCONNECT, this.handler);
                nextCell.setAtDirection(Direction.UP, prevCell);
                prevCell.setAtDirection(Direction.DOWN, nextCell);
                prevCell = nextCell;
            }
            exitTunnel.setEntrance(prevCell);
        }
    }

    void setEntrance(TunnelCell entranceCell) {
        if (entranceCell == null || Math.abs(entranceCell.getY() - this.y) > world.getOffsetY())
            return;
        int idx = entranceCell.getX()/world.getOffsetX();
        TunnelCell previous = this.cells.get(idx);
        if (previous.getX() == entranceCell.getX()) {
            TunnelCell newCell = new TunnelCell(previous.getX(), previous.getY(), TunnelCellType.ENTRANCE, this.handler);
            this.cells.set(idx, newCell);
            entrances.add(newCell);
            TunnelCell left = previous.getCellAtDirection(Direction.LEFT);
            TunnelCell right = previous.getCellAtDirection(Direction.RIGHT);
            left.setAtDirection(Direction.RIGHT, newCell);
            right.setAtDirection(Direction.LEFT, newCell);
            newCell.setAtDirection(Direction.LEFT, left);
            newCell.setAtDirection(Direction.RIGHT, right);
            newCell.setAtDirection(Direction.UP, entranceCell);
            entranceCell.setAtDirection(Direction.DOWN, newCell);
        }
    }



    /*
     * end builder methods
     */
    /*
     * begin public getters
     */

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNumCells() {
        return cells.size();
    }

    public int getWidth(){
        return cells.size()*world.getOffsetX();
    }

    public List<TunnelCell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public List<TunnelCell> getEntrances(){
        return Collections.unmodifiableList(this.entrances);
    }

    public List<TunnelCell> getExits(){
        return Collections.unmodifiableList(this.exits);
    }

    public TunnelCell getNearestEntrance(int x) {
        return getNearestCell(entrances, x);
    }

    public TunnelCell getNearestExit(int x) {
        return getNearestCell(exits, x);
    }

    public int getNuggetCount(){
        return this.nuggetCount;
    }

    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    public List<Enemy> getEnemyBySearchCriteria(Predicate<Enemy> searchCriteria){
        searchResults.clear();
        for (Enemy enemy : this.enemies){
            if (searchCriteria.test(enemy))
                searchResults.add(enemy);
        }
        return searchResults;
    }



    /*
     * end public getters
     */
    /*
     * begin tunnel manipulation methods
     */

    void act(){
        turnCounter++;
        if (turnCounter > turnBound) {
            turnCounter = 0;
            if (this.enemies.size() < 2)
                createNewEnemy();
        }
    }

    void destroyEnemy(Enemy enemy){
        this.handler.onEnemyDestroyed(enemy);
    }

    /*
     * end tunnel manipulation methods
     */
    /*
     * begin helper methods
     */

    private TunnelCell getNearestCell(List<TunnelCell> cells, int x){
        if (cells == null || cells.isEmpty()){
            return null;
        }
        TunnelCell nearest = cells.get(0);
        int nearest_diff = Math.abs(nearest.getX() - x), diff_x;
        for (TunnelCell cell : cells) {
            diff_x = Math.abs(x - cell.getX());
            if (diff_x < nearest_diff)
                nearest = cell;
        }
        return nearest;
    }

    private Enemy createNewEnemy(){
        Direction dir = new Date().getTime() % 2 == 0 ? Direction.LEFT : Direction.RIGHT;
        Enemy added = new Enemy(dir, this, this.handler, this.world);
        this.enemies.add(added);
        this.world.registerActor(added);
        return added;
    }

    /*
     * end helper methods
     */
}