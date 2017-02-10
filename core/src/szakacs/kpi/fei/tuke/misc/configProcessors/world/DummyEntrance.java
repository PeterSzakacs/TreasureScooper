package szakacs.kpi.fei.tuke.misc.configProcessors.world;

/**
 * Created by developer on 2.2.2017.
 */
public class DummyEntrance {

    int xIndex;
    int yIndex;
    DummyTunnel tunnel;

    DummyEntrance(int xIndex, int yIndex, DummyTunnel tunnel){
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.tunnel = tunnel;
    }

    DummyEntrance(){
    }

    public int getXIndex() {
        return xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }

    public DummyTunnel getTunnel() {
        return tunnel;
    }
}
