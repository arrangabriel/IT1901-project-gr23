package client;

public class ServerResponseException extends Exception {

    /**
     * Response code.
     */
    private final int code;

    /**
     * A throwable exception pertaining to server issues.
     * @param message the exception message.
     * @param responseCode the response code.
     */
    public ServerResponseException(final String message,
                                   final int responseCode) {
        super(message);
        this.code = responseCode;
    }

    /**
     * @return the response code.
     */
    public int getCode() {
        return this.code;
    }
}
