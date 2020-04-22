javac --module-path %JAVA_FX_PATH% --add-modules javafx.controls,javafx.fxml DictionaryClient.java
jar cfe DictionaryClient.jar DictionaryClient *.class MainScene.fxml