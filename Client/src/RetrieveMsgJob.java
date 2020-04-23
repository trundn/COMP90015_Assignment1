
import org.json.simple.JSONObject;

/**
 * The Class RetrieveMsgJob.
 */
public class RetrieveMsgJob extends AbstractJob {

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

                } else if (response
                        .containsKey(Constants.RESPONE_MEANING_KEY)) {
                    MessageNotifier.getInstance().onMessageChanged(response
                            .get(Constants.RESPONE_MEANING_KEY).toString());
                }
            }
        }

        return null;
    }
}
