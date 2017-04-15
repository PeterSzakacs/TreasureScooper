package szakacs.kpi.fei.tuke.enums;

/**
 * An enum representing the type of the smallest segment of the collecting pipe.
 */
public enum PipeSegmentType {

    /**
     * A straight segment from top to bottom or vice-versa.
     */
    VERTICAL,

    /**
     * A straight segment from left to right or vice-versa.
     */
    HORIZONTAL,

    /**
     * A bend in the pipe from top to left (or left to top).
     */
    TOP_LEFT,

    /**
     * A bend in the pipe from top to right (or right to top).
     */
    TOP_RIGHT,

    /**
     * A bend in the pipe from bottom to left (or left to bottom).
     */
    BOTTOM_LEFT,

    /**
     * A bend in the pipe from bottom to right (or right to bottom).
     */
    BOTTOM_RIGHT
}
