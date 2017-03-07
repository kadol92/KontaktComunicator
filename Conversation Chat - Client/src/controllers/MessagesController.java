package controllers;

import app.contacts.ContactMessage;
import app.messages.ChatClient;
import app.messages.ChatMessage;
import app.messages.WritingMessage;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kamil on 2017-01-15.
 */
public class MessagesController {
    @FXML
    public TitledPane optionsPane;
    @FXML
    public GridPane infoGridPane;
    public TextArea messageTextArea;
    public TextArea messageBox;
    public Label messageTextAreaInfo;
    public int userIDList;
    @FXML
    public Label userNameLabel;
    private boolean animate;
    private String writing;
    private ChatClient chatClient;
    private boolean userSendWriting;
    private Thread threadAnimate;
    private Task<Void> animateText;
    @FXML
    private Button editContact;
    @FXML
    public MainController mainController;

    public void init(MainController mainController) {
        System.out.println("Initialization app.messages controller.");
        messageTextArea.setWrapText(true);
        messageBox.setWrapText(true);
        messageTextAreaInfo.setVisible(false);
        this.animate = false;
        this.writing = new String();
        this.userSendWriting = false;
        this.mainController = mainController;

    }

    public synchronized Task<Void> getAnimateText() {
        return animateText;
    }

    public synchronized String getWriting() {
        return writing;
    }

    public synchronized void setWriting(String writing) {
        this.writing = writing;
    }

    public synchronized boolean isAnimate() {
        return animate;
    }

    public synchronized void setAnimate(boolean animate) {
        this.animate = animate;
    }

    //Funkcja odpowiadająca za monitorowanie aktywności
    public void animateWriting(String userName) {
        animateText = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (isAnimate()) {
                    for (int i = 0; i < 4; i++) {
                        if (i == 0) setWriting(" [Pisze   ]");
                        else if (i == 1) setWriting(" [Pisze.  ]");
                        else if (i == 2) setWriting(" [Pisze.. ]");
                        else if (i == 3) setWriting(" [Pisze...]");
                        setWriting(userName + writing);
                        Platform.runLater(() -> {
                            userNameLabel.setText(getWriting());
                        });
                        Thread.sleep(500);
                    }

                }

                return null;
            }
        };
        threadAnimate = new Thread(animateText);

        threadAnimate.setDaemon(true);
        threadAnimate.setName("Animate Writing");
        threadAnimate.start();

    }
    //Wyśwuetlanie informacji o tym czy dany kontakt obecnie coś pisze
    public void showUserIsWriting(WritingMessage writingMessage, int userIDList, int activeContact, String userName) {

        if (writingMessage.isUserWritng()) {
            if (activeContact == userIDList) {
                if (animateText != null) {
                    Platform.runLater(() -> {
                        if (animateText.isRunning()) {
                            animateText.cancel();
                        }
                    });

                }
                Platform.runLater(() -> {
                    setAnimate(true);
                    animateWriting(userName);
                });

            }
        } else if (activeContact == userIDList) {
            if (animateText != null) {
                Platform.runLater(() -> {
                    if (getAnimateText().isRunning()) {
                        animateText.cancel();
                    }

                });

            }
            Platform.runLater(() -> {
                setAnimate(false);
                userNameLabel.setText(userName);
            });

        }
    }
     // Wuświetlanie najpotrzebiejszych informacji o kontakcie
    public void setDetailsWindow(String userName, int userIDList, boolean isUserWriting) {
        this.userIDList = userIDList;
        if (isUserWriting) {
            if (animateText != null) {
                if (animateText.isRunning()) {
                    animateText.cancel();
                }

            }
            setAnimate(true);
            animateWriting(userName);
        } else {
            if (animateText != null) {
                if (animateText.isRunning()) {
                    animateText.cancel();
                }

            }
            setAnimate(false);
            userNameLabel.setText(userName);
        }
        List<ContactMessage> messagesC = mainController.getContactCurrentMessages(this.userIDList);
        StringBuilder sb = new StringBuilder();
        String you = "Ty:" + System.getProperty("line.separator");
        String userContact = userName + ":" + System.getProperty("line.separator");
        for (ContactMessage cm : messagesC) {
            if (cm.isSendToServer()) {
                sb.append(you + cm.getMessage());
            } else {
                sb.append(userContact + cm.getMessage());
            }
        }

        messageBox.setText(sb.toString());
        messageBox.deselect();
        messageBox.setScrollTop(Double.MAX_VALUE);
    }

    public void showEditContactPanel() {
        mainController.showEditContactPanel();
    }

    //Rozszerzenie okna od opcji konwersacji
    public void expandinfoGridPane() {
        if (optionsPane.isExpanded()) {
            infoGridPane.setMinHeight(165);

        } else {
            infoGridPane.setMinHeight(50);
        }

    }

    //Wyświetlanie odebranej wiadomości
    public void showMessage(ChatMessage chatMessage, int userIDList, int activeContact, String contactName) {
        contactName = contactName + ":" + System.getProperty("line.separator");
        String message = chatMessage.getMessage() + System.getProperty("line.separator");
        if (userIDList == activeContact) {
            messageBox.appendText(contactName + message);
        }
        mainController.saveContactMessage(userIDList, new ContactMessage(message, false));
    }

    //Wysłanie wiadomości na serwer do odpowiedniego kontaktu
    public void sendMessage() {
        try {
            messageTextAreaInfo.setVisible(false);
            if (!this.userSendWriting) {
                mainController.sendWritingUserToChatClient(true, this.userIDList);
                this.userSendWriting = true;
            }

            String txt = messageTextArea.getText();
            if (messageTextArea.getText().substring(messageTextArea.getText().length() - 1).equals("\n")) {
                if (messageTextArea.getText().equals("\n")) {
                    messageTextArea.clear();
                } else {
                    messageTextArea.clear();
                    String temp = "Ty:\n";
                    String message = temp + txt + System.getProperty("line.separator");
                    String messageSave = txt + System.getProperty("line.separator");

                    mainController.sendMessageToChatClient(txt, this.userIDList);
                    messageBox.appendText(message);
                    mainController.saveContactMessage(this.userIDList, new ContactMessage(messageSave, true));
                    mainController.sendWritingUserToChatClient(false, this.userIDList);
                    this.userSendWriting = false;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("User logout");
            messageTextAreaInfo.setVisible(true);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("No text");
            this.userSendWriting = false;
            try {
                mainController.sendWritingUserToChatClient(false, this.userIDList);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
