package szakacs.kpi.fei.tuke.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.*;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyTunnel;

import java.util.*;

public class TreasureScooperWorld implements GameWorldPrivileged {

    private int width;
    private int height;
    private int offsetX;
    private int offsetY;

    private final Map<String, TunnelCell> entrances;
    private final Set<HorizontalTunnel> tunnels;
    private int nuggetCount;

    private PlayerManagerPrivileged playerManager;
    private MethodCallAuthenticator authenticator;

    public TreasureScooperWorld(MethodCallAuthenticator authenticator) {
        this.authenticator = authenticator;
        this.tunnels = new HashSet<>();
        this.entrances = new HashMap<>();
    }

    private void initialize(GameWorldPrototype worldPrototype){
        this.offsetX = worldPrototype.getOffsetX();
        this.offsetY = worldPrototype.getOffsetY();
        this.width = worldPrototype.getWidth();
        this.height = worldPrototype.getHeight();
        this.buildTunnelGraph(worldPrototype);
        for (HorizontalTunnel ht : this.tunnels)
            this.nuggetCount += ht.getNuggetCount();
    }

    /**
     * Initializes the tunnels list, connects those tunnels on particular spots
     * thereby building the maze of underground tunnels and creates entrances
     * into this maze for the players.
     *
     * @param worldPrototype a GameWorldInitializerObject holding information
     *                    necessary to create the underground maze
     */
    private void buildTunnelGraph(GameWorldPrototype worldPrototype) {
        Map<String, DummyTunnel> dummyTunnels = worldPrototype.getDummyTunnels();
        Map<String, HorizontalTunnel> tunnelMap = new HashMap<>();

        // Create the tunnels from their DummyTunnel Prototypes
        for (DummyTunnel dt : dummyTunnels.values()) {
            HorizontalTunnel ht = new HorizontalTunnel(dt, this);
            tunnelMap.put(dt.getId(), ht);
            this.tunnels.add(ht);
        }

        // Set the entrances to the tunnel network
        Map<String, DummyEntrance> dummyEntrances = worldPrototype.getDummyEntrances();
        for (String id : dummyEntrances.keySet()){
            DummyEntrance de = dummyEntrances.get(id);
            TunnelCell entrance = new TunnelCell(
                    de.getX(), de.getY(),
                    TunnelCellType.INTERCONNECT,
                    null, this
            );
            this.entrances.put(id, entrance);
            HorizontalTunnel rootTunnel = tunnelMap.get(de.getTunnel().getId());
            TunnelCell newCell, prevCell = entrance;
            for (int y = entrance.getY() - offsetY; y > rootTunnel.getY(); y -= offsetY) {
                newCell = new TunnelCell(entrance.getX(), y, TunnelCellType.INTERCONNECT, null, this);
                newCell.setAtDirection(Direction.UP, prevCell, authenticator);
                prevCell.setAtDirection(Direction.DOWN, newCell, authenticator);
                prevCell = newCell;
            }
            setEntrance(rootTunnel, prevCell);
        }
        // Connect the tunnels with each other
        addInterconnects(dummyTunnels, tunnelMap);

        // Connect the tunnels with each other
/*        for (DummyTunnel dt : dummyTunnels.values()) {

            HorizontalTunnel ht = tunnelMap.get(dt.getId());
            Map<Integer, DummyTunnel> tunnelsBelowDt = dt.getConnectedTunnelsBelow();
            for (Integer xIndex : tunnelsBelowDt.keySet()) {
                addInterconnect(
                        xIndex * this.offsetX,
                        tunnelMap.get(tunnelsBelowDt.get(xIndex).getId())
                );
            }
        }*/
    }

    private void addInterconnects(Map<String, DummyTunnel> dummyTunnelsMap, Map<String, HorizontalTunnel> tunnelsMap) {
        for (String id : dummyTunnelsMap.keySet()) {
            HorizontalTunnel ht = tunnelsMap.get(id);
            DummyTunnel dt = dummyTunnelsMap.get(id);
            Map<Integer, DummyTunnel> connectedTunnels = dt.getConnectedTunnelsBelow();
            Set<TunnelCellUpdatable> allCells = ht.getUpdatableCells();
            for (Integer xPos : connectedTunnels.keySet()) {
                // Remove previous TUNNEL cell and add a new EXIT cell in its place
                TunnelCellUpdatable removed = ht.getUpdatableCellsBySearchCriteria(
                        (cell) -> cell.isWithinCell(xPos, dt.getY())
                ).iterator().next();
                allCells.remove(removed);
                TunnelCell newCell = new TunnelCell(
                        removed.getX(), removed.getY(), TunnelCellType.EXIT,
                        ht, this
                );
                allCells.add(newCell);
                // Connect the new EXIT cell with the previous TUNNEL cell's neighbors
                TunnelCellUpdatable left = removed.getCellAtDirection(Direction.LEFT);
                TunnelCellUpdatable right = removed.getCellAtDirection(Direction.RIGHT);
                newCell.setAtDirection(Direction.LEFT, left, authenticator);
                newCell.setAtDirection(Direction.RIGHT, right, authenticator);
                left.setAtDirection(Direction.RIGHT, newCell, authenticator);
                right.setAtDirection(Direction.LEFT, newCell, authenticator);
                // Create new cells until the specified exit tunnel is reached
                TunnelCell prevCell = newCell, nextCell;
                HorizontalTunnel exitTunnel = tunnelsMap.get(connectedTunnels.get(xPos).getId());
                for (int y = ht.getY() + offsetY * Direction.DOWN.getYStep();
                     y > exitTunnel.getY();
                     y += offsetY * Direction.DOWN.getYStep()) {
                    nextCell = new TunnelCell(newCell.getX(), y, TunnelCellType.INTERCONNECT, null, this);
                    nextCell.setAtDirection(Direction.UP, prevCell, authenticator);
                    prevCell.setAtDirection(Direction.DOWN, nextCell, authenticator);
                    prevCell = nextCell;
                }
                setEntrance(exitTunnel, prevCell);
            }
/*            getCellsBySearchCriteria(
                    (cell) -> cell.getCellType() == TunnelCellType.TUNNEL
                            && cell.isWithinCell(x, this.y)
            );
            if (!searchResults.isEmpty()) {
                // Remove previous TUNNEL cell and add a new EXIT cell in its place
                TunnelCellUpdatable removed = searchResults.iterator().next();
                TunnelCell added = new TunnelCell(
                        removed.getX(), removed.getY(),
                        TunnelCellType.EXIT,
                        this, world
                );
                cells.remove(removed);
                cells.add(added);

                // Connect the new EXIT cell with the previous TUNNEL cell's neighbors
                TunnelCellUpdatable left = removed.getUpdatableCellAtDirection(Direction.LEFT);
                TunnelCellUpdatable right = removed.getUpdatableCellAtDirection(Direction.RIGHT);
                added.setAtDirection(Direction.LEFT, left, world.getAuthenticator());
                added.setAtDirection(Direction.RIGHT, right, world.getAuthenticator());
                left.setAtDirection(Direction.RIGHT, added, world.getAuthenticator());
                right.setAtDirection(Direction.LEFT, added, world.getAuthenticator());

                // Create new cells until the specified exit tunnel is reached
                TunnelCell prevCell = added, nextCell;
                for (int y = this.y + world.getOffsetY() * Direction.DOWN.getYStep();
                     y > exitTunnel.getY();
                     y += world.getOffsetY() * Direction.DOWN.getYStep()) {
                    nextCell = new TunnelCell(added.getX(), y, TunnelCellType.INTERCONNECT, null, world);
                    nextCell.setAtDirection(Direction.UP, prevCell, world.getAuthenticator());
                    prevCell.setAtDirection(Direction.DOWN, nextCell, world.getAuthenticator());
                    prevCell = nextCell;
                }
                // Tell the exit tunnel to create an entrance
                exitTunnel.setEntrance(prevCell);
            }*/
        }
    }

    private void setEntrance(HorizontalTunnel exitTunnel, TunnelCellUpdatable entranceCell) {
        TunnelCellUpdatable previous = exitTunnel.getUpdatableCellsBySearchCriteria(
                (cell) -> cell.isWithinCell(entranceCell.getX(), cell.getY())
        ).iterator().next();

        /*if (entranceCell == null)
            return;*/
        /*getCellsBySearchCriteria(
                (cell) -> cell.getCellType() == TunnelCellType.TUNNEL
                        && cell.isWithinCell(entranceCell.getX(), this.y)
        );*/
        /*TunnelCellUpdatable previous =
        if ( !searchResults.isEmpty() ) {
            TunnelCellUpdatable previous = searchResults.iterator().next();*/
            TunnelCellUpdatable newCell = new TunnelCell(
                    previous.getX(), previous.getY(),
                    TunnelCellType.ENTRANCE,
                    exitTunnel, this
            );
            exitTunnel.getUpdatableCells().remove(previous);
            exitTunnel.getUpdatableCells().add(newCell);
            TunnelCellUpdatable left = previous.getCellAtDirection(Direction.LEFT);
            TunnelCellUpdatable right = previous.getCellAtDirection(Direction.RIGHT);
            left.setAtDirection(Direction.RIGHT, newCell, authenticator);
            right.setAtDirection(Direction.LEFT, newCell, authenticator);
            newCell.setAtDirection(Direction.LEFT, left, authenticator);
            newCell.setAtDirection(Direction.RIGHT, right, authenticator);
            newCell.setAtDirection(Direction.UP, entranceCell, authenticator);
            entranceCell.setAtDirection(Direction.DOWN, newCell, authenticator);
        //}
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getOffsetX() {
        return offsetX;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }

    @Override
    public int getNuggetCount() {
        return nuggetCount;
    }

    @Override
    public Map<String, TunnelCellBasic> getEntrances() {
        return Collections.unmodifiableMap(entrances);
    }

    @Override
    public Set<HorizontalTunnelBasic> getTunnels(){
        return Collections.unmodifiableSet(tunnels);
    }

    @Override
    public Set<HorizontalTunnelUpdatable> getTunnelsUpdatable() {
        return Collections.unmodifiableSet(tunnels);
    }

    @Override
    public Map<String, TunnelCellUpdatable> getEntrancesUpdatable() {
        return Collections.unmodifiableMap(entrances);
    }

    @Override
    public MethodCallAuthenticator getAuthenticator() {
        return authenticator;
    }

    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level) {
        tunnels.clear();
        entrances.clear();
        nuggetCount = 0;
        this.initialize(level.getGameWorldPrototype());
        this.playerManager = gameLevel.getPlayerManager();
    }

    @Override
    public void onNuggetCollected(Pipe pipe, int val) {
        nuggetCount--;
        Player player = pipe.getController();
        int score = playerManager.getPlayersAndScores().get(player);
        playerManager.getScoreChangeCallback().onScoreEvent(score + val, player);
    }
}