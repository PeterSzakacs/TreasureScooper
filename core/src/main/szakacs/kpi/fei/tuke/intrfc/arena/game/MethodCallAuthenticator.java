package szakacs.kpi.fei.tuke.intrfc.arena.game;

/**
 * An authenticator object used to verify that the code
 * calling a method with {@code Object authToken} as one
 * of its parameters is allowed to call it.
 */
public interface MethodCallAuthenticator {

    /**
     * Checks to see if the token passed to the code
     * calling this method is valid.
     *
     * @param token the token passed to the code calling this method from a third party
     * @return boolean true if authentication succeeds | false otherwise
     */
    boolean authenticate(Object token);
}
