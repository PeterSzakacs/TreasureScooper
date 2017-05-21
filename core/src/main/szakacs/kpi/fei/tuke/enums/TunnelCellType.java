package szakacs.kpi.fei.tuke.enums;

/**
 * An enum type describing the configuration of adjacent tunnel cells for a given cell.
 */
public enum
TunnelCellType {

    /**
     * Cells to left, right, up.
     */
    ENTRANCE,

    /**
     * Cells to left, right, down.
     */
    EXIT,

    /**
     * Only a cell to the right.
     */
    LEFT_EDGE,

    /**
     * Only a cell to the left.
     */
    RIGHT_EDGE,

    /**
     * Cells to left and right.
     */
    TUNNEL,

    /**
     * Cells in all directions.
     */
    CROSSROAD,
    /**
     * Cells to up and down.
     */
    INTERCONNECT,
    /**
     * Cells to up, down and right.
     */
    LEFT_CROSSROAD,
    /**
     * Cells to up, down and left.
     */
    RIGHT_CROSSROAD,
    /**
     * Cells to down and right.
     */
    LEFT_TOP_BEND,
    /**
     * Cells to up and right.
     */
    LEFT_BOTTOM_BEND,
    /**
     * Cells to down and left.
     */
    RIGHT_TOP_BEND,
    /**
     * Cells to up and left.
     */
    RIGHT_BOTTOM_BEND
}
