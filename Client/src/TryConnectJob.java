import java.util.concurrent.TimeUnit;

/**
 * The Class TryConnectJob.
 */
public class TryConnectJob extends AbstractJob {

    /**
     * Call.
     *
     * @return the void
     * @throws Exception the exception
     */
    @Override
    protected Void call() throws Exception {
        while (!this.isCancelled()) {
            SocketHandler handler = SocketHandler.getInstance();

            if (!handler.isConnected()) {
                // Sleep 1 second to avoid performing too many re-try
                // connections
                TimeUnit.SECONDS.sleep(1);

                // Establish connection to socket server.
                String message = handler.connect();
                updateMessage(message);
            }
        }

        return null;
    }

}
