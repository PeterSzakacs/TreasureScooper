package szakacs.kpi.fei.tuke.intrfc.player;

/**
 * The interface for a token that is unique for each player
 * and which serves as an authentication of sorts to prevent
 * other players in multiplayer scenarios from interfering
 * with one another unduly.
 */
public interface PlayerToken {

    /**
     * Check if the token passed to it is valid
     * (basically checks if it is the same instance).
     *
     * @param token the token passed to the method in which validate()
     *              is called and which needs to be checked.
     *
     * @return boolean true if token is valid | false otherwise
     */
    boolean validate(PlayerToken token);
}
