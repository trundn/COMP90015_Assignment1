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
        if (this.connection != null) {
            MessageNotifier.getInstance().onMessageChanged(String.format(
                    "Sending response message to client [%s]. Content: %s",
                    this.connection.getHostAddress(), response.toJSONString()));

            this.connection.send(response);
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
            // Get the dictionary storage instance.
            DictionaryStorage dictStorage = DictionaryStorage.getInstance();

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
                    response = dictStorage.search(word);
                    this.sendResponse(response);
                    break;
                case Constants.DELETE_OPERATION:
                    word = request.get(Constants.WORD_KEY).toString();
                    response = dictStorage.delete(word);
                    this.sendResponse(response);
                    break;
                case Constants.ADD_OPERATION:
                    word = request.get(Constants.WORD_KEY).toString();
                    meaning = request.get(Constants.MEANING_KEY).toString();
                    response = dictStorage.add(word, meaning);
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
