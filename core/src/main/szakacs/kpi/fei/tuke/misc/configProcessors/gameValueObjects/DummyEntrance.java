package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

/**
 * Created by developer on 2.2.2017.
 */
public class DummyEntrance {

    private int x;
    private int y;
    private DummyTunnel tunnel;

    public DummyEntrance(int x, int y, DummyTunnel tunnel){
        this.x = x;
        this.y = y;
        this.tunnel = tunnel;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public DummyTunnel getTunnel() {
        return tunnel;
    }
}
