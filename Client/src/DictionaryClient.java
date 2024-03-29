
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Class DictionaryClient.
 */
public class DictionaryClient extends Application {

    /** The scene controller. */
    private SceneController sceneController;

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    "The host address and port should be specified in command line arguments.");
        } else if (args.length > 2) {
            System.out.println(
                    "The number of command line arguments is invalid.");
        } else {
            try {
                String hostAddress = args[0];
                int port = Integer.parseInt(args[1]);

                if (port < Constants.MIN_PORT_NUMBER
                        || port > Constants.MAX_PORT_NUMBER) {
                    throw new IllegalArgumentException("Invalid port number: "
                            + port + ". It must be in range ("
                            + Constants.MIN_PORT_NUMBER + ", "
                            + Constants.MAX_PORT_NUMBER + ").");
                }

                SocketHandler controller = SocketHandler.getInstance();
                controller.setPort(port);
                controller.setHostAddress(hostAddress);

                launch(args);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Start.
     *
     * @param primaryStage the primary stage
     * @throws Exception the exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("ClientMainScene.fxml"));

        Parent root = loader.load();
        sceneController = loader.getController();

        // Register the scene callback.
        MessageNotifier.getInstance().registerSceneCallback(sceneController);

        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Stop.
     */
    @Override
    public void stop() {
        this.sceneController.unInitialize();
    }
}
