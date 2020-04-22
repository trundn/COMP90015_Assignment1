import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * The Class PingJob.
 */
public class PingJob extends AbstractJob {

    /** The ping data. */
    private final int PING_DATA = 0xFF;

    /**
     * Call.
     *
     * @return the void
     * @throws Exception the exception
     */
    @Override
    protected Void call() throws Exception {
        SocketHandler handler = SocketHandler.getInstance();
        while (!this.isCancelled()) {
            try {
                String error = handler.send(RequestBuilder.buildPingRequest());
                if (!StringHelper.isNullOrEmpty(error)) {
                    handler.setConnected(false);
                    updateMessage("Failed to ping to server. Error: " + error);
                }
                // handler.sendUrgentData(PING_DATA);
            } catch (Exception ex) {
//                handler.setConnected(false);
//                updateMessage(
//                        "Failed to ping to server. Error: " + ex.getMessage());
            }

            TimeUnit.SECONDS.sleep(5);
        }

        return null;
    }

}
