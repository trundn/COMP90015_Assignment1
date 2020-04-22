import org.json.simple.JSONObject;

/**
 * The Class RequestBuilder.
 */
public class RequestBuilder {

    /**
     * Builds the shut down request.
     *
     * @param hostAddress the host address
     * @return the JSON object
     */
    @SuppressWarnings("unchecked")
    public static JSONObject buildShutDownRequest(String hostAddress) {
        JSONObject request = new JSONObject();
        request.put(Constants.SHUTDOWN_OPERATION, hostAddress);

        return request;
    }

    /**
     * Builds the ping request.
     *
     * @param hostAddress the host address
     * @return the JSON object
     */
    @SuppressWarnings("unchecked")
    public static JSONObject buildPingRequest(String hostAddress) {
        JSONObject request = new JSONObject();
        request.put(Constants.PING_OPERATION, hostAddress);

        return request;
    }

    /**
     * Builds the search request.
     *
     * @param word the word
     * @return the JSON object
     */
    @SuppressWarnings("unchecked")
    public static JSONObject buildSearchRequest(String word) {
        JSONObject request = new JSONObject();

        request.put(Constants.OPERATION_KEY, Constants.SEARCH_OPERATION);
        request.put(Constants.WORD_KEY, word);

        return request;
    }

    /**
     * Builds the delete request.
     *
     * @param word the word
     * @return the JSON object
     */
    @SuppressWarnings("unchecked")
    public static JSONObject buildDeleteRequest(String word) {
        JSONObject request = new JSONObject();

        request.put(Constants.OPERATION_KEY, Constants.DELETE_OPERATION);
        request.put(Constants.WORD_KEY, word);

        return request;
    }

    /**
     * Builds the add request.
     *
     * @param word    the word
     * @param meaning the meaning
     * @return the JSON object
     */
    @SuppressWarnings("unchecked")
    public static JSONObject buildAddRequest(String word, String meaning) {
        JSONObject request = new JSONObject();

        request.put(Constants.OPERATION_KEY, Constants.ADD_OPERATION);
        request.put(Constants.WORD_KEY, word);
        request.put(Constants.MEANING_KEY, meaning);

        return request;
    }

}
