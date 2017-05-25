package szakacs.kpi.fei.tuke.misc;

import szakacs.kpi.fei.tuke.intrfc.misc.Rectangle;

/**
 * <p>The libGDX specific implementation of the
 * {@link Rectangle} interface for a game object
 * descriptor.</p>
 *
 * <p>The current implementation uses libGDX with its
 * coordinate origin at lower left. The lower left
 * corner set of coordinates is returned by
 * {@link Rectangle#getRectangleX()} and
 * {@link Rectangle#getRectangleY()}).</p>
 */
public class GDXRectangle implements Rectangle {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int centerX;
    protected int centerY;

    public GDXRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.centerX = x + width/2;
        this.centerY = y + height/2;
    }

    @Override
    public int getCenterX() {
        return centerX;
    }

    @Override
    public int getCenterY() {
        return centerY;
    }

    /**
     * Returns the horizontal coordinate of the lower left corner of this rectangle.
     *
     * @return the horizontal coordinate of the lower left corner of this rectangle
     */
    @Override
    public int getRectangleX() {
        return x;
    }

    /**
     * Returns the vertical coordinate of the lower left corner of this rectangle.
     *
     * @return the vertical coordinate of the lower left corner of this rectangle
     */
    @Override
    public int getRectangleY() {
        return y;
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
    public boolean overlaps(Rectangle other) {
        if (other == null)
            return false;
        else return x < other.getRectangleX() + width && x + width > other.getRectangleX()
                && y < other.getRectangleY() + height && y + height > other.getRectangleY();
    }
}
