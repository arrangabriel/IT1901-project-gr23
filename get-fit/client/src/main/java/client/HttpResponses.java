package client;

import java.util.Map;

public class HttpResponses {
    static final Map<Integer, String> responses = Map.of(
        200, "OK",
        400, "Bad Request",
        404, "Not Found",
        500, "Internal Server Error"
    );

    public static String getResponseText(int code) {
        return responses.get(code);
    }

}
