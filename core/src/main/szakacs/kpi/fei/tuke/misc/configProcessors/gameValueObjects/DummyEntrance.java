package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

/**
 * Created by developer on 2.2.2017.
 */
public class DummyEntrance {

    private int x;
    private int y;
    private DummyTunnel tunnel;
    String id;

    public DummyEntrance(int x, int y, DummyTunnel tunnel, String id) {
        this.x = x;
        this.y = y;
        this.tunnel = tunnel;
        this.id = id;
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

    public String getId() {
        return id;
    }
}
