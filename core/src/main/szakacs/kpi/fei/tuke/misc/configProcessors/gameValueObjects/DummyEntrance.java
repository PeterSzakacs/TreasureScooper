package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

/**
 * A value object representing an entrance into the underground tunnel maze.
 */
public class DummyEntrance {

    private int x;
    private int y;
    private DummyTunnel tunnel;
    private String id;

    public DummyEntrance(int x, int y, DummyTunnel tunnel, String id) {
        this.x = x;
        this.y = y;
        this.tunnel = tunnel;
        this.id = id;
    }

    /**
     * Gets the absolute value of the horizontal coordinate of the topmost cell
     * of this entrance (that which is right below the surface).
     *
     * @return the absolute value of the horizontal coordinate of this entrance
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the absolute value of the verical coordinate of the topmost cell
     * of this entrance (that which is right below the surface).
     *
     * @return the absolute value of the vertical coordinate of this entrance
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the value object representing the horizontal tunnel this entrance leads to.
     *
     * @return the value object representing the horizontal tunnel this entrance leads to
     */
    public DummyTunnel getTunnel() {
        return tunnel;
    }

    /**
     * Gets the unique string identifier of this entrance.
     *
     * @return the unique string identifier of this entrance
     */
    public String getId() {
        return id;
    }
}
