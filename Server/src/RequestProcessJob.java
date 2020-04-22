import org.json.simple.JSONObject;

/**
 * The Class RequestProcessJob.
 */
public class RequestProcessJob extends AbstractJob {

    /** The connection. */
    private SocketConnection connection;

    /** The request. */
    private JSONObject request;

    /**
     * Instantiates a new request process job.
     *
     * @param connection the connection
     * @param request    the request
     */
    public RequestProcessJob(SocketConnection connection, JSONObject request) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "The given request JSON object is NULL.");
        }

        this.request = request;
        this.connection = connection;
    }

    /**
     * Send response.
     *
     * @param response the response
     */
    private void sendResponse(JSONObject response) {
        if (connection != null) {
            connection.send(response);
        }
    }

    /**
     * Call.
     *
     * @return the void
     * @throws Exception the exception
     */
    @Override
    protected Void call() throws Exception {
        if (!this.isCancelled()) {
            // Get the dictionary manager instance.
            DictionaryManager dictManager = DictionaryManager.getInstance();

            // Get the request operation.
            JSONObject response;
            String word = "";
            String meaning = "";

            if (request.containsKey(Constants.OPERATION_KEY)) {
                String operation = request.get(Constants.OPERATION_KEY)
                        .toString();

                switch (operation) {
                case Constants.SEARCH_OPERATION:
                    word = request.get(Constants.WORD_KEY).toString();
                    response = dictManager.search(word);
                    this.sendResponse(response);
                    break;
                case Constants.DELETE_OPERATION:
                    word = request.get(Constants.WORD_KEY).toString();
                    response = dictManager.delete(word);
                    this.sendResponse(response);
                    break;
                case Constants.ADD_OPERATION:
                    word = request.get(Constants.WORD_KEY).toString();
                    meaning = request.get(Constants.MEANING_KEY).toString();
                    response = dictManager.add(word, meaning);
                    this.sendResponse(response);
                    break;
                default:
                    System.out.println(request.toJSONString());
                    break;
                }
            }
        }

        return null;
    }

}
