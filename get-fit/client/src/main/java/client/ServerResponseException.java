package client;

public class ServerResponseException extends Exception {

    private int code;

    public ServerResponseException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}