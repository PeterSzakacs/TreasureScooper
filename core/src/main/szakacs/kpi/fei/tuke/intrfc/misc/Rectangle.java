package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.enums.Direction;

/**
 * <p>A descriptor of a rendered game object
 * based on a mathematical rectangle. In case
 * we were to switch the rendering library and
 * possibly the coordinate system, we should
 * only need to re-implement this interface and
 * change the values in the {@link Direction}
 * enum and use our new implementation.</p>
 */
public interface Rectangle {

    /**
     * Gets the horizontal coordinate of the center of the rectangle.
     * Same as the horizontal coordinate of the specific game object.
     *
     * @return the horizontal coordinate of the center of the rectangle.
     */
    int getCenterX();

    /**
     * Gets the vertical coordinate of the center of the rectangle.
     * Same as the vertical coordinate of the specific game object.
     *
     * @return the vertical coordinate of the center of the rectangle.
     */
    int getCenterY();

    /**
     * Gets a horizontal coordinate for a game objects visual representation
     * used for rendering. The semantics of the actual value returned depends
     * on the coordinate system of the rendering library used.
     *
     * @return the horizontal coordinate of the game objects visual representation.
     */
    int getRectangleX();

    /**
     * Gets a vertical coordinate for a game objects visual representation
     * used for rendering. The semantics of the actual value returned
     * depends on the coordinate system of the rendering library used.
     *
     * @return the vertical coordinate of the game objects visual representation.
     */
    int getRectangleY();

    /**
     * Gets the width of the rectangle.
     *
     * @return the width of the rectangle.
     */
    int getWidth();

    /**
     * Gets the height of the rectangle.
     *
     * @return the height of the rectangle.
     */
    int getHeight();

    /**
     * Checks if this rectangle overlaps or touches another rectangle.
     *
     * @param other the rectangle to compare against.
     * @return boolean true if this rectangle overlaps or touches
     *         the other rectangle | false otherwise.
     */
    boolean overlaps(Rectangle other);
}
