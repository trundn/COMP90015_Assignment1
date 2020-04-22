import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;

/**
 * The Class SceneController.
 */
public class SceneController implements Initializable, ScenceCallback {

    /** The message label. */
    @FXML
    private Label messageLabel;

    /** The word input. */
    @FXML
    private TextField wordInput;

    /** The meaning area. */
    @FXML
    private TextArea meaningArea;

    /** The application log area. */
    @FXML
    private TextArea appLogArea;

    /** The search button. */
    @FXML
    private Button searchButton;

    /** The delete button. */
    @FXML
    private Button deleteButton;

    /** The add button. */
    @FXML
    private Button addButton;

    /** The new line. */
    private String newLine = System.getProperty("line.separator");

    /** The ping job. */
    private PingJob pingJob;

    /** The try connect job. */
    private TryConnectJob tryConnectJob;

    /** The retrieve message job. */
    private RetrieveMsgJob retrieveMsgJob;

    /** The job executor. */
    private ThreadPoolJobExecutor jobExecutor;

    /**
     * On enable all buttons.
     */
    @Override
    public void onEnableAllButtons() {
        this.searchButton.setDisable(false);
        this.addButton.setDisable(false);
        this.deleteButton.setDisable(false);
    }

    /**
     * On disable all buttons.
     */
    @Override
    public void onDisableAllButtons() {
//        this.searchButton.setDisable(true);
//        this.addButton.setDisable(true);
//        this.deleteButton.setDisable(true);
    }

    /**
     * Handle send request.
     *
     * @param request the request
     */
    private void handleSendRequest(JSONObject request) {
        SocketHandler handler = SocketHandler.getInstance();
        String error = handler.send(request);

        if (StringHelper.isNullOrEmpty(error)) {
            this.onDisableAllButtons();
        } else {
            this.appLogArea.appendText(error);
            this.appLogArea.appendText(this.newLine);
        }
    }

    /**
     * Handle search button action.
     *
     * @param event the event
     */
    @FXML
    protected void handleSearchButtonAction(ActionEvent event) {
        Window owner = deleteButton.getScene().getWindow();
        String word = wordInput.getText().toLowerCase();

        if (StringHelper.isNullOrEmpty(word)) {
            AlertHelper.showAlert(AlertType.ERROR, owner, "Error",
                    "The word is not specified.");
        } else {
            JSONObject request = RequestBuilder.buildSearchRequest(word);
            this.handleSendRequest(request);
        }
    }

    /**
     * Handle delete button action.
     *
     * @param event the event
     */
    @FXML
    protected void handleDeleteButtonAction(ActionEvent event) {
        Window owner = deleteButton.getScene().getWindow();
        String word = wordInput.getText().toLowerCase();

        if (StringHelper.isNullOrEmpty(word)) {
            AlertHelper.showAlert(AlertType.ERROR, owner, "Error",
                    "The word is not specified.");
        } else {
            JSONObject request = RequestBuilder.buildDeleteRequest(word);
            this.handleSendRequest(request);
        }
    }

    /**
     * Handle add button action.
     *
     * @param event the event
     */
    @FXML
    protected void handleAddButtonAction(ActionEvent event) {
        Window owner = deleteButton.getScene().getWindow();
        String word = wordInput.getText().toLowerCase();
        String meaning = meaningArea.getText();

        if (StringHelper.isNullOrEmpty(word)) {
            AlertHelper.showAlert(AlertType.ERROR, owner, "Error",
                    "The word is not specified.");
        } else if (StringHelper.isNullOrEmpty(meaning)) {
            AlertHelper.showAlert(AlertType.ERROR, owner, "Error",
                    "The meaning is not specified.");
        } else {
            JSONObject request = RequestBuilder.buildAddRequest(word, meaning);
            this.handleSendRequest(request);
        }
    }

    /**
     * Initialize.
     *
     * @param arg0 the URL
     * @param arg1 the resource bundle
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Instantiate needed jobs.
        this.pingJob = new PingJob();
        this.tryConnectJob = new TryConnectJob();
        this.retrieveMsgJob = new RetrieveMsgJob(this);

        try {
            this.jobExecutor = new ThreadPoolJobExecutor(-1, 3, 3);
            this.jobExecutor.queue(this.pingJob);
            this.jobExecutor.queue(this.tryConnectJob);
            this.jobExecutor.queue(this.retrieveMsgJob);

            // Observe job messages and put into the application log.
            this.pingJob.messageProperty()
                    .addListener((obs, oldMessage, newMessage) -> {
                        this.appLogArea.appendText(newMessage);
                        this.appLogArea.appendText(this.newLine);
                    });

            this.tryConnectJob.messageProperty()
                    .addListener((obs, oldMessage, newMessage) -> {
                        this.appLogArea.appendText(newMessage);
                        this.appLogArea.appendText(this.newLine);
                    });

            this.retrieveMsgJob.messageProperty()
                    .addListener((obs, oldMessage, newMessage) -> {
                        this.appLogArea.appendText(newMessage);
                        this.appLogArea.appendText(this.newLine);
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Uninitialize.
     */
    public void unInitialize() {
        try {
            this.jobExecutor.terminate();
            this.jobExecutor
                    .waitForTermination(Duration.ofSeconds(5).toMillis());

            // Clean up resource for socket handler.
            SocketHandler.getInstance().cleanUp();
        } catch (TimeoutException e) {
            this.jobExecutor.forceInterrupt();
        }
    }

}
