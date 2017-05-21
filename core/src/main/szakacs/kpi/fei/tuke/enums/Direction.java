package szakacs.kpi.fei.tuke.enums;

/**
 * An enum type representing the four directions
 * of allowed movement within the game.
 */
public enum Direction {
    UP (0, 1),
    DOWN (0, -1),
    LEFT (-1, 0),
    RIGHT (1, 0);

    static {
        UP.opposite = DOWN;
        DOWN.opposite = UP;
        LEFT.opposite = RIGHT;
        RIGHT.opposite = LEFT;
    }

    private final int xStep;
    private final int yStep;
    private Direction opposite;

    Direction(int xStep, int yStep){
        this.xStep = xStep;
        this.yStep = yStep;
    }

    /**
     * Gets the unit shift (step) in the horizontal coordinates for a given direction.
     *
     * @return the unit step in the horizontal coordinates for a given direction.
     */
    public int getXStep(){
        return this.xStep;
    }

    /**
     * Gets the unit shift (step) in the vertical coordinates for a give direction.
     *
     * @return the unit step in the vertical coordinates for a given direction.
     */
    public int getYStep(){
        return this.yStep;
    }

    /**
     * Gets the opposite direction to the current direction.
     *
     * @return the opposite direction to the current one.
     */
    public Direction getOpposite(){
        return this.opposite;
    }

    /**
     * Calculates the direction based on the value of the horizontal
     * and vertical shifts in position. Note: one of these has to be
     * zero and the other nonzero, otherwise an exception is thrown.
     *
     * @param dx the shift in the horizontal coordinates.
     * @param dy the shift in the vertical coordinates.
     * @return the direction that corresponds to the shifts in coordinates passed.
     */
    public static Direction getDirectionByDeltas(int dx, int dy){
        if (dx != dy && dx + dy != dx && dx + dy != dy)
            throw new IllegalArgumentException("Illegal values of dx and dy. One has to be zero, the other nonzero!");
        if (dy == 0) {
            if (dx/Math.abs(dx) == 1)
                return RIGHT;
            else
                return LEFT;
        } else {
            if (dy/Math.abs(dy) == 1)
                return UP;
            else
                return DOWN;
        }
    }
}
