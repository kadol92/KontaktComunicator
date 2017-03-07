package app.main;

import app.contacts.Contact;
import app.contacts.ContactsList;
import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import app.login.LoginToServer;
import app.messages.ClientConfig;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main extends Application{

    private Stage mainWindow;
    private Socket socket;
    private boolean autoLogged;
    private static Stage primaryStage;

    public Socket getSocket() {
        return socket;
    }
    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }
    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        mainWindow = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainWindow.fxml"));
        Parent root = loader.load();
        mainWindow.setTitle("Kontakt V0.1");
        mainWindow.setScene(new Scene(root, 800, 600));
        mainWindow.show();
    }


    public static void main(String[] args)
    {

        launch(args);
    }


}
