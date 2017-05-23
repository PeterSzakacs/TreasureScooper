package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import org.junit.Assert;
import org.junit.Test;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 18.2.2017.
 */
public class TunnelCellTest {

    private GameWorldPrivileged mock = new GameWorldPrivileged() {
        int offsetX = 128, offsetY = 128, width = 4096, height = 2048;

        public MethodCallAuthenticator getAuthenticator() {return null;}
        public void onNuggetCollected(Pipe pipe, int val) {}
        public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level)
                throws GameLevelInitializationException {}
        public int getWidth() {return width;}
        public int getHeight() {return height;}
        public int getOffsetX() {return offsetX;}
        public int getOffsetY() {return offsetY;}
        public int getNuggetCount() {return 0;}
        public Map<String, TunnelCellBasic> getEntrances() {return null;}
        public Set<HorizontalTunnelBasic> getTunnels() { return null; }
        public Set<TunnelCellBasic> getCells() { return null; }
        public Set<HorizontalTunnelUpdatable> getTunnelsUpdatable() { return null;}
        public Map<String, TunnelCellUpdatable> getEntrancesUpdatable() { return null; }
    };

    @Test
    public void isWithinCell() throws Exception {
        int cellX = 12;
        int cellY = 12;
        TunnelCellBasic testCell = new TunnelCell(
                mock.getOffsetX() * cellX,
                mock.getOffsetY() * cellY,
                null,
                null,
                mock
        );
        Assert.assertTrue(testCell.isWithinCell(
                mock.getOffsetX() * cellX,
                mock.getOffsetY() * cellX
        ));
        Assert.assertTrue(testCell.isWithinCell(
                mock.getOffsetX() * cellX + mock.getOffsetX() - 1,
                mock.getOffsetY() * cellY + mock.getOffsetY() - 1
        ));

        Assert.assertFalse(testCell.isWithinCell(
                mock.getOffsetX() * (cellX - 1),
                mock.getOffsetY() * cellY
        ));
        Assert.assertFalse(testCell.isWithinCell(
                mock.getOffsetX() * (cellX + 1),
                mock.getOffsetY() * cellY
        ));
        Assert.assertFalse(testCell.isWithinCell(
                mock.getOffsetX() * cellX,
                mock.getOffsetY() * (cellY - 1)
        ));
        Assert.assertFalse(testCell.isWithinCell(
                mock.getOffsetX() * cellX,
                mock.getOffsetY() * (cellY + 1)
        ));
    }
}