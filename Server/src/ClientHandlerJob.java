import org.json.simple.JSONObject;

/**
 * The Class ClientHandlerJob.
 */
public class ClientHandlerJob extends AbstractJob {

    /** The socket connection. */
    private SocketConnection socketConnection;

    /** The request process job executor. */
    private ThreadPoolJobExecutor requestProcessJobExecutor;

    /**
     * Instantiates a new client handler job.
     *
     * @param connection                the connection
     * @param requestProcessJobExecutor the request process job executor
     */
    public ClientHandlerJob(SocketConnection connection,
            ThreadPoolJobExecutor requestProcessJobExecutor) {
        this.socketConnection = connection;
        this.requestProcessJobExecutor = requestProcessJobExecutor;
    }

    /**
     * Call.
     *
     * @return the void
     * @throws Exception the exception
     */
    @Override
    protected Void call() throws Exception {
        while (!this.isCancelled()) {
            JSONObject request = this.socketConnection.receive();
            if (request != null) {
                if (!request.containsKey(Constants.PING_OPERATION)) {
                    MessageNotifier.getInstance()
                            .onMessageChanged(String.format(
                                    "Received message from client [%s]. Content: %s",
                                    this.socketConnection.getHostAddress(),
                                    request.toJSONString()));
                }

                if (!request.containsKey(Constants.SHUTDOWN_OPERATION)) {
                    RequestProcessJob job = new RequestProcessJob(
                            this.socketConnection, request);
                    this.requestProcessJobExecutor.queue(job);
                } else {
                    MessageNotifier.getInstance()
                            .onMessageChanged(String.format(
                                    "Client [%s] is shuting down.",
                                    this.socketConnection.getHostAddress()));
                    this.cancel();
                    this.socketConnection.cleanUp();
                }
            }
        }

        return null;
    }

}
