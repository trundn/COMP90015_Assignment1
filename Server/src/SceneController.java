import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * The Class SceneController.
 */
public class SceneController implements Initializable {

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
        // TODO Auto-generated method stub

    }

}
