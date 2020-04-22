import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * The Class SceneController.
 */
public class SceneController implements Initializable, ScenceCallback {

    /** The IP address field. */
    @FXML
    private TextField ipAddressField;

    /** The port field. */
    @FXML
    private TextField portField;

    /** The application log area. */
    @FXML
    private TextArea appLogArea;

    /**
     * Initialize.
     *
     * @param arg0 the URL
     * @param arg1 the resource bundle.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    /**
     * Show socket port.
     *
     * @param port the port
     */
    public void showSocketPort(int port) {
        this.portField.setText(Integer.toString(port));
    }

    /**
     * Show ip address.
     */
    public void showIpAddress() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipAddressField.setText(ip.getHostAddress());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * On message changed.
     *
     * @param newMessage the new message
     */
    @Override
    public void onMessageChanged(String newMessage) {
        this.appLogArea.appendText(newMessage);
        this.appLogArea.appendText(Constants.NEW_LINE);
    }

}
