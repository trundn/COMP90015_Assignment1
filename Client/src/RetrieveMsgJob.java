
import org.json.simple.JSONObject;

/**
 * The Class RetrieveMsgJob.
 */
public class RetrieveMsgJob extends AbstractJob {

    /** The scene callback. */
    private ScenceCallback sceneCallback;

    /**
     * Instantiates a new retrieve message job.
     *
     * @param sceneCallback the scene callback
     */
    public RetrieveMsgJob(ScenceCallback sceneCallback) {
        this.sceneCallback = sceneCallback;
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
            // Get the response message.
            JSONObject response = SocketHandler.getInstance().receive();

            if (response != null) {
                // Extract the response content.
                if (response.containsKey(Constants.RESPONSE_KEY)) {
                    updateMessage(
                            response.get(Constants.RESPONSE_KEY).toString());

                }

                // Enable all buttons on target scene
                if (this.sceneCallback != null) {
                    this.sceneCallback.onEnableAllButtons();
                }
            }

        }

        return null;
    }
}
