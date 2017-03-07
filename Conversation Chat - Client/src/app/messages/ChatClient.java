package app.messages;

import com.sun.xml.internal.fastinfoset.algorithm.IEEE754FloatingPointEncodingAlgorithm;
import controllers.MainController;
import app.messages.utilities.ReadFromServer;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * Created by Kamil on 2016-12-03.
 */
public class ChatClient implements Runnable {

    private Socket socket;

    private ChatMessage chatMessage;
    private WritingMessage writingMessage;
    private MainController mainController;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public ChatClient(Socket socket, MainController mainController, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.mainController = mainController;
        this.inputStream = in;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        ReadFromServer readFromServer = new ReadFromServer();

        boolean logged = true;

        //Oczekiwanie na wiadomości z serwera;
        while (logged) {
            chatMessage = null;
            writingMessage = null;
            Object message;
            try {
                message = inputStream.readObject();

                if(message instanceof ChatMessage){
                    chatMessage = (ChatMessage) message;
                }
                else if(message instanceof WritingMessage){
                    writingMessage = (WritingMessage) message;
                }
            }
            //W przypadku jakiegoś błędu, rozłączenie z serwerem
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                logged = false;
            } catch (IOException ex) {
                logged = false;
            }

            if (chatMessage != null) {

                try{
                    mainController.showMessage(chatMessage);
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
            else if(writingMessage != null){
                mainController.showUserIsWriting(writingMessage);
            }
        }

        //Zamknięcie obiektów
        try {
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendMessage(ChatMessage chatMessage) throws IOException {
        outputStream.writeObject(chatMessage);
    }

    public void sendUserIsWriting(WritingMessage writingMessage) throws IOException{
        outputStream.writeObject(writingMessage);
    }

}
