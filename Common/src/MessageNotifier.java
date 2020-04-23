
/**
 * The Class MessageNotifier.
 */
public class MessageNotifier {

    /** The instance. */
    private static MessageNotifier INSTANCE;

    /** The scene callback. */
    ScenceCallback sceneCallback;

    /**
     * Gets the single instance of MessageNotifier.
     *
     * @return single instance of MessageNotifier
     */
    public static MessageNotifier getInstance() {
        if (INSTANCE == null) {
            synchronized (MessageNotifier.class) {
                if (INSTANCE == null)
                    INSTANCE = new MessageNotifier();
            }
        }

        return INSTANCE;
    }

    /**
     * Register scene callback.
     *
     * @param callback the callback
     */
    public void registerSceneCallback(ScenceCallback callback) {
        this.sceneCallback = callback;
    }

    /**
     * Unregister scene callback.
     */
    public void unregisterSceneCallback() {
        this.sceneCallback = null;
    }

    /**
     * On message changed.
     *
     * @param newMessage the new message
     */
    public void onMessageChanged(String newMessage) {
        if (this.sceneCallback != null) {
            this.sceneCallback.onMessageChanged(newMessage);
        }
    }

}
