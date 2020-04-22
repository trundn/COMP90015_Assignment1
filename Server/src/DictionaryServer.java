import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Class DictionaryServer.
 */
public class DictionaryServer extends Application {

    /** The socket port. */
    public static int socketPort;

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
                    "The port and dictionary file path should be specified in command line arguments.");
        } else if (args.length > 2) {
            System.out.println(
                    "The number of command line arguments is invalid.");
        } else {
            try {
                // Parse port and dictionary path from arguments.
                int port = Integer.parseInt(args[0]);
                String dictionaryPath = args[1];

                if (port < Constants.MIN_PORT_NUMBER
                        || port > Constants.MAX_PORT_NUMBER) {
                    throw new IllegalArgumentException("Invalid port number: "
                            + port + ". It must be in range ("
                            + Constants.MIN_PORT_NUMBER + ", "
                            + Constants.MAX_PORT_NUMBER + ").");
                }

                // Keep the socket port.
                DictionaryServer.socketPort = port;

                // Launch the socket acceptor.
                SocketAcceptor.getInstance().launch(port, dictionaryPath);

                // Launch JavaFx User Interface.
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
                getClass().getResource("ServerMainScene.fxml"));

        Parent root = loader.load();
        sceneController = loader.getController();

        sceneController.showIpAddress();
        sceneController.showSocketPort(DictionaryServer.socketPort);

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
        DictionaryStorage.getInstance().persist();
        SocketAcceptor.getInstance().cleanUp();
    }

}
