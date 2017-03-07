package controllers;

import app.contacts.Contact;
import app.contacts.ContactMessage;
import app.contacts.ContactsList;
import app.main.Main;
import app.messages.ChatClient;
import app.messages.ChatMessage;
import app.messages.WritingMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    private Media incomingSound;
    private MediaPlayer mediaPlayer;
    private Stage registerStage;
    private Parent register;
    private ChatClient chatClient;
    private ContactsList contactsList;
    @FXML
    private VBox mainPanel;
    @FXML
    public ListView listViewContacts;
    @FXML
    private LoginController loginController;
    @FXML
    public VBox login;
    @FXML
    private MessagesController messagesController;
    @FXML
    private SearchController searchContactController;
    @FXML
    private EditContactController editContactController;
    @FXML
    public VBox messages;

    public int userIDConnection;

    public void setChatClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public ChatClient getChatClient() {
        return this.chatClient;
    }

    public void setUserIDConnection(int userIDConnection) {
        this.userIDConnection = userIDConnection;
    }

    //Wyśwuetlanie informacji o tym czy dany użytjownik obecnie pisze
    public void showUserIsWriting(WritingMessage writingMessage) {
        int userIDList = -1;
        for (int i = 0; i < contactsList.getUsersNumber(); i++) {

            if (writingMessage.getFromWho() == contactsList.getContact(i).getUserID()) {
                userIDList = i;
            }
        }

        if (userIDList >= 0) {
            contactsList.getContact(userIDList).setUserWriting(writingMessage.isUserWritng());
            int activeContact = listViewContacts.getSelectionModel().getSelectedIndex();
            String userName = contactsList.getContact(userIDList).getName();
            messagesController.showUserIsWriting(writingMessage, userIDList, activeContact, userName);
        }
    }

    //Wyświetlanie wiadomości
    public void showMessage(ChatMessage chatMessage) {
        int userIDList = -1;
        String contactName = new String();
        for (int i = 0; i < contactsList.getUsersNumber(); i++) {

            if (chatMessage.getFromWho() == contactsList.getContact(i).getUserID()) {

                userIDList = i;
                contactName = contactsList.getContact(i).getName();
            }
        }

        if (userIDList < 0) {
            contactsList.addContact(new Contact(Integer.toString(chatMessage.getFromWho()), "", chatMessage.getFromWho()));
            listViewContacts.getItems().clear();
            listViewContacts.getItems().addAll(contactsList.getAllContacts());

            for (int i = 0; i < contactsList.getUsersNumber(); i++) {
                if (chatMessage.getFromWho() == contactsList.getContact(i).getUserID()) {
                    userIDList = i;
                    contactName = contactsList.getContact(i).getName();
                }
            }

        }


        int activeContact = listViewContacts.getSelectionModel().getSelectedIndex();

        if (!mainWindow.isFocused() || activeContact != userIDList) {
            Platform.runLater(() -> {

                try {

                    Media incomingSound = new Media(MainController.class.getResource("/app/sounds/incomingMessage.mp3").toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(incomingSound);
                    mediaPlayer.autoPlayProperty();
                    mediaPlayer.play();
                } catch (URISyntaxException e) {
                    System.out.println("Uri exceptopm");
                }

            });
        }
        if (activeContact != userIDList) {
            int unreadMessages = contactsList.getContact(userIDList).getUnreadMessages() + 1;
            contactsList.getContact(userIDList).setUnreadMessages(unreadMessages);
            listViewContacts.refresh();
        }
        messagesController.showMessage(chatMessage, userIDList, activeContact, contactName);
    }

    public ListView getListViewContacts() {

        return listViewContacts;
    }

    //Dodanie nowej osoby do spisu kontaktów
    public void addNewContactToList(Contact contact) {
        contactsList.addContact(contact);
        Platform.runLater(() -> {
            listViewContacts.getItems().clear();
            listViewContacts.getItems().addAll(contactsList.getAllContacts());
        });
    }

    //odświeżenie listy kontaktów
    public void updateContactName(String name) {
        contactsList.getContact(listViewContacts.getSelectionModel().getSelectedIndex()).setName(name);
        contactsList.sortContacts();
        Platform.runLater(() -> {
            listViewContacts.getItems().clear();
            listViewContacts.getItems().addAll(contactsList.getAllContacts());
        });

    }
    //Wyświetlenie okna do logowania
    public void showLoginWindow() {

        login.setVisible(true);
        messages.setVisible(false);
        editContactController.getEditContactPanel().setVisible(false);
        searchContactController.getSearchPanel().setVisible(false);
    }

    //Wyświetlenie okna do wyszukiwania użytkowników
    public void showSearchPanel() {
        login.setVisible(false);
        messages.setVisible(false);
        editContactController.getEditContactPanel().setVisible(false);
        searchContactController.getSearchPanel().setVisible(true);
    }

    //Wyświetlenie okna z wiadomościami
    public void showMessagesWindow() {
        login.setVisible(false);
        messages.setVisible(true);
        editContactController.getEditContactPanel().setVisible(false);
        searchContactController.getSearchPanel().setVisible(false);

    }

    //Wysłanie wiadomości na serwer
    public void sendMessageToChatClient(String message, int userIDList) throws IOException {
        int toWho = contactsList.getContact(userIDList).getUserID();
        chatClient.sendMessage(new ChatMessage(message, this.userIDConnection, toWho));

    }

    //Wysłanie informacjo o pisaniu na serwer
    public void sendWritingUserToChatClient(boolean isUserWriting, int userIDList) throws IOException {
        int toWho = contactsList.getContact(userIDList).getUserID();
        chatClient.sendUserIsWriting(new WritingMessage(isUserWriting, this.userIDConnection, toWho));
    }

    //Wyśwuetlenie okna z informacjami o kontakcie
    public void displayContactsWindow() {
        try {
            int userID = listViewContacts.getSelectionModel().getSelectedIndex();
            boolean isUserWriting = contactsList.getContact(userID).isUserWriting();
            String userName = contactsList.getContact(userID).getName();
            contactsList.getContact(userID).setUnreadMessages(0);
            listViewContacts.refresh();
            messagesController.setDetailsWindow(userName, userID, isUserWriting);
            showMessagesWindow();
        } catch (Exception e) {
            System.out.println("No selected item.");
        }

    }

    //Zapisanie historii wiadomości
    public void saveContactMessage(int userIDList, ContactMessage message) {
        contactsList.getContact(userIDList).setCurrentCorversation(message);
        //contactsList.getContact(userIDList).getUserID()
    }


    //Pobranie historii wiadomości
    public List<ContactMessage> getContactCurrentMessages(int userIDList) {
        return contactsList.getContact(userIDList).getCurrentCorversation();
    }

    //pokazywanie okna do rejestracji
    public void showRegisterWindow() {

        try {
            registerStage = new Stage();
            register = FXMLLoader.load(getClass().getResource("/view/registerWindow.fxml"));

            registerStage.initModality(Modality.APPLICATION_MODAL);
            registerStage.setTitle("Kontakt - Rejestracja");
            registerStage.setScene(new Scene(register, 600, 640));
            registerStage.setResizable(false);
            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //Pokazywanie okna edycji kontaktu
    public void showEditContactPanel() {
        login.setVisible(false);
        messages.setVisible(false);
        editContactController.getEditContactPanel().setVisible(true);
        editContactController.getContactCity().setText("Miejscowość: " + contactsList.getContact(listViewContacts.getSelectionModel().getSelectedIndex()).getCity());
        editContactController.getContactName().setText(contactsList.getContact(listViewContacts.getSelectionModel().getSelectedIndex()).getName());
        editContactController.getContactNumber().setText("Numer ID: " + Integer.toString(contactsList.getContact(listViewContacts.getSelectionModel().getSelectedIndex()).getUserID()));
        editContactController.getSaveChangedContact().setDisable(false);
    }

    Stage mainWindow;

    //Inicjalizacja głównego kontrolera
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initizalization app.main controller.");


        //mediaPlayer.play();
        messagesController.init(this);
        loginController.init(this);
        editContactController.init(this);
        searchContactController.init(this);

        contactsList = new ContactsList();
        listViewContacts.getItems().addAll(contactsList.getAllContacts());

        mainWindow = Main.getPrimaryStage();


    }
}
