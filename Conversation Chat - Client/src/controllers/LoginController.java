package controllers;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import app.messages.ChatClient;
import app.messages.ClientConfig;
import app.messages.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;


public class LoginController {
    private Stage loginStage;
    private Parent login;
    private FXMLLoader loader;
    private MainController mainController;
    private ChatClient chatClient;
    @FXML
    public TextField userNameTextField;
    @FXML
    public PasswordField passwordTextField;

    @FXML
    public void init(MainController mainController) {
        System.out.println("Initialization app.login controller.");
        this.mainController = mainController;
    }

    public void showWindows() {

        try {
            loginStage = new Stage();
            //app.login = FXMLLoader.load(getClass().getResource("../controllers/loginWindow.fxml"));
            loader = new FXMLLoader(getClass().getResource("../view/loginWindow.fxml"));

            login = loader.load();

            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setTitle("Kontakt - Logowanie");
            loginStage.setScene(new Scene(login, 300, 240));
            loginStage.setResizable(false);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Logowanie do serwera
    public void loginToServer() {

        Socket socket = null;
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        boolean loginConfirm = false;
        try {

            socket = new Socket(ClientConfig.URL, ClientConfig.PORT);
            //Tworzenie obiektu do wysłania
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            //Wysłanie informacji z nazwą użytkownika i hasłem na serwer;
            outputStream.writeObject(new LoginMessage(userNameTextField.getText(), passwordTextField.getText(), false, -1));
            inputStream = new ObjectInputStream(socket.getInputStream());
            LoginMessage loginResult;
            boolean done = false;
            //boolean result = false;
            while (!done) {
                try {
                    loginResult = (LoginMessage) inputStream.readObject();

                    //Jeśli odebrano wiadomość, wyjdź z pętli
                    if (loginResult != null) {
                        done = true;

                        //Jeśli potwierdzono logowanie ustaw wynik logowania na true;
                        if (loginResult.isLogged()) {
                            loginConfirm = true;
                            mainController.setUserIDConnection(loginResult.getUserID());
                        }
                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //Jeśli zalogowano do serwera uruchom wątek odpowiedzialny za odbieranie wiadomości

        } catch (ConnectException ex) {

            System.out.println("Server is not available.");


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (loginConfirm) {
            mainController.setChatClient(new ChatClient(socket, mainController, inputStream, outputStream));
            Runnable r = mainController.getChatClient();
            Thread t = new Thread(r);
            t.setName("Thread - Recieve message from server");
            t.setDaemon(true);
            t.start();
            mainController.getListViewContacts().getSelectionModel().select(0);
            mainController.showMessagesWindow();
            mainController.displayContactsWindow();
        } else {
            System.out.println("Incorrect login or password");
        }

    }


}
