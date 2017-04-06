package szakacs.kpi.fei.tuke.arena.game.world;

import org.junit.Assert;
import org.junit.Test;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;

import java.util.List;
import java.util.Map;

/**
 * Created by developer on 18.2.2017.
 */
public class TunnelCellTest {

    private GameWorldBasic mock = new GameWorldBasic() {
        int offsetX = 128, offsetY = 128, width = 4096, height = 2048;

        public int getWidth() {return width;}
        public int getHeight() {return height;}
        public int getOffsetX() {return offsetX;}
        public int getOffsetY() {return offsetY;}
        public int getNuggetCount() {return 0;}
        public Map<String, TunnelCell> getEntrances() {return null;}
        public List<HorizontalTunnel> getTunnels() {return null;}
    };

    @Test
    public void isWithinCell() throws Exception {
        int cellX = 12;
        int cellY = 12;
        TunnelCell testCell = new TunnelCell(
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