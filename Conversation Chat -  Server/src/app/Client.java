package app;

import app.messages.ChatMessage;
import app.messages.WritingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Kamil on 2016-12-01.
 */
public class Client implements Runnable {
    private ServerChat serverChat;
    private Socket clientSocket;
    private int number;
    private Thread thread;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    ChatMessage message;
    private Queue<ChatMessage> messageObject;


    public Client(ServerChat serverChat, Socket clientSocket, int number, ObjectInputStream in, ObjectOutputStream out) {
        this.serverChat = serverChat;
        this.clientSocket = clientSocket;
        this.number = number;
        this.inFromClient = in;
        this.outToClient = out;

        this.messageObject = new LinkedList<>();

    }

    public void sendMessage(ChatMessage chatMessage) {
        try {
            outToClient.writeObject(chatMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUserIsWriting(WritingMessage writingMessage) {
        try {
            outToClient.writeObject(writingMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        boolean done = false;
        ChatMessage chatMessage;
        WritingMessage writingMessage;
        Object message;
        //ObjectInputStream inputStream;
        try {
            // przyposujemy obiekt służacy do komunikacji z klientem
            inFromClient = new ObjectInputStream(this.clientSocket.getInputStream());

            while (!done) {
                chatMessage = null;
                writingMessage = null;
                message = null;
                try {
                    //Czekamy na wiadomość od klienta
                    message = inFromClient.readObject();
                    //Jesli odebrana wiadomosc to wiadomosc tekstowa to zapisz to jako wiadomosc tekstowa
                    if (message instanceof ChatMessage) {
                        chatMessage = (ChatMessage) message;
                    }
                    //Jesli wiadomosc informujaca czy dany user pisze, to zapisz to jako wiadomosc o pisaniu
                    else if (message instanceof WritingMessage) {
                        writingMessage = (WritingMessage) message;
                    }



                } catch (IOException e) {

                    //Jesli klient sie rozlaczyl od serwera, wyloguj go
                    serverChat.logoutClient(this.number);
                    System.out.println("Client number " + this.number + " disconnected from the server.");
                    done = true;
                } catch (ClassNotFoundException e) {
                    serverChat.logoutClient(this.number);
                    e.printStackTrace();
                    done = true;
                }
                //Jeśli odebrano wiadomosc tekstowa, wyslij ja do odpowiedniego klieta
                if (chatMessage != null) {
                    serverChat.sendMessageToClient(chatMessage);
                }
                //Jesli odebrano informacje o pisaniu do danego klienta, wyslij to
                else if (writingMessage != null) {
                    serverChat.sendUserIsWriting(writingMessage);
                }

            }
        } catch (IOException e) {
            serverChat.logoutClient(this.number);
            System.out.println("Client number " + this.number + " disconnected from the server.");
        }

    }

}
