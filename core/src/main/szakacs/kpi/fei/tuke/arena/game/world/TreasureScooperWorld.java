package szakacs.kpi.fei.tuke.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
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
    private final List<HorizontalTunnel> tunnels;
    private int nuggetCount;

    private PlayerManagerPrivileged playerManager;
    private MethodCallAuthenticator authenticator;

    public TreasureScooperWorld(MethodCallAuthenticator authenticator) {
        this.authenticator = authenticator;
        this.tunnels = new ArrayList<>();
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
            // it is more efficient (in terms of time complexity) to add items to two lists simultaneously,
            // instead of calling new ArrayList<>(tunnelMap.values()) at the end of this method.
            this.tunnels.add(ht);
        }

        // Connect the tunnels with each other
        for (DummyTunnel dt : dummyTunnels.values()) {
            HorizontalTunnel ht = tunnelMap.get(dt.getId());
            Map<Integer, DummyTunnel> tunnelsBelowDt = dt.getConnectedTunnelsBelow();
            for (Integer xIndex : tunnelsBelowDt.keySet()) {
                ht.addInterconnect(
                        xIndex * this.offsetX,
                        tunnelMap.get(
                                tunnelsBelowDt.get(xIndex).getId()
                        )
                );
            }
        }

        // Set the entrances to the tunnel network
        Map<String, DummyEntrance> dummyEntrances = worldPrototype.getDummyEntrances();
        for (String id : dummyEntrances.keySet()){
            DummyEntrance de = dummyEntrances.get(id);
            TunnelCell entrance = new TunnelCell(
                    de.getX(),
                    de.getY(),
                    TunnelCellType.INTERCONNECT,
                    null,
                    this
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
            rootTunnel.setEntrance(prevCell);
        }
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
    public Map<String, TunnelCell> getEntrances() {
        return Collections.unmodifiableMap(entrances);
    }

    @Override
    public List<HorizontalTunnel> getTunnels(){
        return Collections.unmodifiableList(tunnels);
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
    public MethodCallAuthenticator getAuthenticator() {
        return authenticator;
    }

    @Override
    public void onNuggetCollected(Pipe pipe, int val) {
        nuggetCount--;
        Player player = pipe.getController();
        int score = playerManager.getPlayersAndScores().get(player);
        playerManager.getScoreChangeCallback().onScoreEvent(score + val, player);
    }
}