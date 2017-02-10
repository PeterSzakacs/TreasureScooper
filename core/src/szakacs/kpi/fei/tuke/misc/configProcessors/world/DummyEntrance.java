package szakacs.kpi.fei.tuke.misc.configProcessors.world;

/**
 * Created by developer on 2.2.2017.
 */
public class DummyEntrance {

    int x;
    int y;
    DummyTunnel tunnel;

    DummyEntrance(int x, int y, DummyTunnel tunnel){
        this.x = x;
        this.y = y;
        this.tunnel = tunnel;
    }

    DummyEntrance(){
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
