package app;

import app.messages.ChatMessage;
import app.messages.LoginMessage;
import app.messages.WritingMessage;
import app.userLoginToServer.UserInfoLogin;
import app.userLoginToServer.VerifyUser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2016-12-01.
 */
public class ServerChat implements Runnable {
    private ServerSocket serverSocket;
    private Thread thread;
    private OutputStream outToClient;
    private Client[] clientList;
    private List<PrintWriter> printWriter;
    private int number;

    public ServerChat(int port) {

        try {
            this.clientList = new Client[10000];
            this.number = 0;
            this.serverSocket = new ServerSocket(port);
            this.thread = new Thread(this);
            this.thread.start();

            printWriter = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            int clientID =0;
            try {
                //Oczekiwanie na połączenie z klientem, utworzenie klienta i dodanie go do listy
                Socket clientSocket = serverSocket.accept();

                //Obiekty do przechwycenia informacjo o logowaniu i zaakceptowaniu go bądź też nie
                ObjectInputStream inputLogin = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputLogin = new ObjectOutputStream(clientSocket.getOutputStream());

                //Obiekt do którego zostanie wpisany obiekt logowania
                LoginMessage loginMessage;
                boolean done = false;
                UserInfoLogin userInfoLogin = null;

                while (!done) {
                    loginMessage = null;

                    try {
                        loginMessage = (LoginMessage) inputLogin.readObject();

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();

                    } catch (SocketException ex) {
                        done = true;
                    }

                    if (loginMessage != null) {
                        VerifyUser verifyUser = new VerifyUser();
                        userInfoLogin = verifyUser.checkUser(loginMessage);
                        done = true;

                        if (userInfoLogin.isLogged()) {
                            loginMessage.setLogged(true);
                            loginMessage.setUserID(userInfoLogin.getUserInfo().getID());
                            outputLogin.writeObject(loginMessage);
                        } else {
                            loginMessage.setLogged(false);
                            outputLogin.writeObject(loginMessage);
                        }


                    }

                }

                //Jesli potwierdzono zalogowanie, uruchom nowy wątek;
                if (userInfoLogin.isLogged()) {
                    clientID = userInfoLogin.getUserInfo().getID();
                    clientList[clientID] = new Client(this, clientSocket, userInfoLogin.getUserInfo().getID(), inputLogin, outputLogin);

                    Runnable r = clientList[userInfoLogin.getUserInfo().getID()];
                    Thread t = new Thread(r);
                    t.start();
                    System.out.println("Client number " + clientID + " connected to the server.");
                }

            } catch (SocketException ex) {
                System.out.println("Client number " + clientID + " disconnected from the server.");
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void logging() {

    }

    public synchronized void sendMessageToClient(ChatMessage chatMessage) {
        //W przypadku gdy client jest podłączony do serwera
        if(clientList[chatMessage.getToWho()] != null){
            clientList[chatMessage.getToWho()].sendMessage(chatMessage);
        }
        // W przeciwnym wypadku wyślij wiadomość do bazy danych, która zostanie wysłana po tym jak uzytkownik się zaloguje
        else{
        }

    }
    public synchronized void sendUserIsWriting(WritingMessage writingMessage){
        if(clientList[writingMessage.getToWho()] != null){
            clientList[writingMessage.getToWho()].sendUserIsWriting(writingMessage);
        }
    }

    public synchronized void logoutClient(int number){
        clientList[number] = null;
    }


    public static void main(String[] args) {
        //Uruchamiamy serwer chatu na porcie 8189
        ServerChat serverChat = new ServerChat(8189);

    }


}
