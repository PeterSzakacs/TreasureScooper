package szakacs.kpi.fei.tuke.enums;

/**
 * An enumerated type describing the configuration of adjacent tunnel cells for a given cell.
 */
public enum TunnelCellType {

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
    LEFT_CROSSROAD,
    RIGHT_CROSSROAD,
    LEFT_TOP_BEND,
    LEFT_BOTTOM_BEND,
    RIGHT_TOP_BEND,
    RIGHT_BOTTOM_BEND
}
