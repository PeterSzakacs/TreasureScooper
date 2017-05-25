package szakacs.kpi.fei.tuke.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.*;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyTunnel;

import java.util.*;

/**
 * The class managing and representing the game world.
 */
public class TreasureScooperWorld implements GameWorldPrivileged {

    private int width;
    private int height;
    private int offsetX;
    private int offsetY;

    private int nuggetCount;
    private final Map<String, TunnelCell> entrances;
    private final Set<HorizontalTunnel> tunnels;
    private final MethodCallAuthenticator authenticator;
    private PlayerManagerPrivileged playerManager;

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
        Map<String, DummyEntrance> dummyEntrances = worldPrototype.getDummyEntrances();
        Map<String, HorizontalTunnel> tunnelMap = new HashMap<>();

        // Create the tunnels from their DummyTunnel Prototypes
        for (DummyTunnel dt : dummyTunnels.values()) {
            HorizontalTunnel ht = new HorizontalTunnel(dt, this);
            tunnelMap.put(dt.getId(), ht);
            this.tunnels.add(ht);
        }
        // Set the entrances to the tunnel network
        for (String id : dummyEntrances.keySet()){
            DummyEntrance de = dummyEntrances.get(id);
            TunnelCell entrance = new TunnelCell(
                    de.getX(), de.getY(),
                    TunnelCellType.INTERCONNECT,
                    null, this
            );
            this.entrances.put(id, entrance);
            createInterconnect(entrance, tunnelMap.get(de.getTunnel().getId()));
        }
        // Connect the tunnels with each other
        addInterconnects(dummyTunnels, tunnelMap);
    }

    private void addInterconnects(Map<String, DummyTunnel> dummyTunnelsMap, Map<String, HorizontalTunnel> tunnelsMap) {
        for (String id : dummyTunnelsMap.keySet()) {
            HorizontalTunnel ht = tunnelsMap.get(id);
            DummyTunnel dt = dummyTunnelsMap.get(id);
            Map<Integer, DummyTunnel> connectedTunnels = dt.getConnectedTunnelsBelow();
            for (Integer xPos : connectedTunnels.keySet()) {
                // Remove previous cell with a new cell that has an adjacent cell downwards.
                TunnelCellUpdatable previous = ht.getCellsBySearchCriteria(
                        (cell) -> cell.isWithinCell(xPos, dt.getY()),
                        authenticator
                ).iterator().next();
                HorizontalTunnel exitTunnel = tunnelsMap.get(connectedTunnels.get(xPos).getId());
                TunnelCellType newCellType = TunnelCellType.EXIT;
                switch (previous.getCellType()){
                    case ENTRANCE:
                        newCellType = TunnelCellType.CROSSROAD; break;
                    case LEFT_EDGE:
                        newCellType = TunnelCellType.LEFT_BOTTOM_BEND; break;
                    case LEFT_TOP_BEND:
                        newCellType = TunnelCellType.LEFT_CROSSROAD; break;
                    case RIGHT_EDGE:
                        newCellType = TunnelCellType.RIGHT_BOTTOM_BEND; break;
                    case RIGHT_TOP_BEND:
                        newCellType = TunnelCellType.RIGHT_CROSSROAD; break;
                }
                TunnelCellUpdatable newCell = replaceCell(previous, newCellType, ht);
                // Create new cells until the specified exit tunnel is reached
                createInterconnect(newCell, exitTunnel);
            }
        }
    }


    private void setEntrance(HorizontalTunnel exitTunnel, TunnelCellUpdatable entranceCell) {
        TunnelCellUpdatable previous = exitTunnel.getCellsBySearchCriteria(
                (cell) -> cell.isWithinCell(entranceCell.getX(), cell.getY()),
                authenticator
        ).iterator().next();
        TunnelCellType newCellType = TunnelCellType.ENTRANCE;
        switch (previous.getCellType()){
            case EXIT:
                newCellType = TunnelCellType.CROSSROAD; break;
            case LEFT_EDGE:
                newCellType = TunnelCellType.LEFT_TOP_BEND; break;
            case LEFT_BOTTOM_BEND:
                newCellType = TunnelCellType.LEFT_CROSSROAD; break;
            case RIGHT_EDGE:
                newCellType = TunnelCellType.RIGHT_TOP_BEND; break;
            case RIGHT_BOTTOM_BEND:
                newCellType = TunnelCellType.RIGHT_CROSSROAD; break;
        }
        TunnelCellUpdatable newCell = replaceCell(previous, newCellType, exitTunnel);
        newCell.setAtDirection(Direction.UP, entranceCell, authenticator);
        entranceCell.setAtDirection(Direction.DOWN, newCell, authenticator);
    }


    private void createInterconnect(TunnelCellUpdatable start, HorizontalTunnel endTunnel){
        TunnelCellUpdatable newCell, prevCell = start;
        for (int y = start.getY() - offsetY; y > endTunnel.getY(); y -= offsetY) {
            newCell = new TunnelCell(start.getX(), y, TunnelCellType.INTERCONNECT, null, this);
            newCell.setAtDirection(Direction.UP, prevCell, authenticator);
            prevCell.setAtDirection(Direction.DOWN, newCell, authenticator);
            prevCell = newCell;
        }
        setEntrance(endTunnel, prevCell);
    }


    private TunnelCellUpdatable replaceCell(TunnelCellUpdatable former, TunnelCellType newCellType, HorizontalTunnel tunnel){
        TunnelCellUpdatable newCell = new TunnelCell(
                former.getX(), former.getY(),
                newCellType,
                tunnel, this
        );
        Set<TunnelCellUpdatable> allCells = tunnel.getCells(authenticator);
        allCells.remove(former);
        allCells.add(newCell);
        for (Direction direction : Direction.values()){
            TunnelCellUpdatable neighbour = former.getCellAtDirection(direction);
            if (neighbour != null){
                newCell.setAtDirection(direction, neighbour, authenticator);
                neighbour.setAtDirection(direction.getOpposite(), newCell, authenticator);
            }
        }
        return newCell;
    }



    // GameWorldBasic methods



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
    public Set<TunnelCellBasic> getCells(){
        Set<TunnelCellBasic> cellsSet = new HashSet<>();
        for (TunnelCellBasic cell : entrances.values()) {
            // Don't care if we try to later add the same cell twice,
            // since this is a Set<>.
            do {
                cellsSet.add(cell);
                cell = cell.getCellAtDirection(Direction.DOWN);
            } while (cell != null);
        }
        for (HorizontalTunnelBasic ht : tunnels) {
            Set<TunnelCellBasic> cells = ht.getCells();
            for (TunnelCellBasic cell : cells) {
                do {
                    cellsSet.add(cell);
                    cell = cell.getCellAtDirection(Direction.DOWN);
                } while (cell != null);
            }
        }
        return cellsSet;
    }

    // GameWorldUpdatable methods



    @Override
    public Set<HorizontalTunnelUpdatable> getTunnelsUpdatable() {
        return Collections.unmodifiableSet(tunnels);
    }

    @Override
    public Map<String, TunnelCellUpdatable> getEntrancesUpdatable() {
        return Collections.unmodifiableMap(entrances);
    }



    // GameWorldPrivileged methods



    @Override
    public MethodCallAuthenticator getAuthenticator() {
        return authenticator;
    }

    @Override
    public void onNuggetCollected(Pipe pipe, int val) {
        nuggetCount--;
        Map<PlayerToken, Pipe> pipeMap = playerManager.getPipesUpdatable();
        for (PlayerToken token : pipeMap.keySet()){
            Pipe potentialPipe = pipeMap.get(token);
            if (potentialPipe.equals(pipe)){
                PlayerInfo info = playerManager.getPlayerTokenMap().get(token);
                playerManager.getScoreChangeCallback().onScoreEvent(
                        info.getScore() + val, token);
                break;
            }
        }
    }

    //// ResettableGameClass methods

    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level) {
        tunnels.clear();
        entrances.clear();
        nuggetCount = 0;
        this.initialize(level.getGameWorldPrototype());
        this.playerManager = gameLevel.getPlayerManager();
    }
}