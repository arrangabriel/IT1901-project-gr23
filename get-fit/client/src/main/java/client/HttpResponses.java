package client;

import java.util.Map;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class HttpResponses {

    /**
     * All possible responses.
     */
    static final Map<Integer, String> RESPONSES = Map.of(
            200, "OK",
            400, "Bad Request",
            404, "Not Found",
            500, "Internal Server Error"
    );

    /**
     * Converts code to response.
     *
     * @param code code.
     * @return response.
     */
    public static String getResponseText(final int code) {
        return RESPONSES.get(code);
    }

}
