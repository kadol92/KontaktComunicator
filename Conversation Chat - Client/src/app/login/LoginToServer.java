package app.login;

import app.messages.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class LoginToServer{

    //Funkcja służąca do zalogowania na serwer
    public boolean login(String userName, String password, Socket socket){

        boolean result = false;
        try {
            //Tworzenie obiektu do wysłania
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            //Wysłanie informacji z nazwą użytkownika i hasłem na serwer;
            outputStream.writeObject(new LoginMessage(userName, password, false, -1));
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            LoginMessage loginResult;
            boolean done = false;
            while(!done){

                try {
                    loginResult = (LoginMessage) inputStream.readObject();

                    //Jeśli odebrano wiadomość, wyjdź z pętli
                    if(loginResult!= null){
                        done = true;

                        //Jeśli potwierdzono logowanie ustaw wynik logowania na true;
                        if(loginResult.isLogged()){
                            result = true;
                            result = false;
                            System.out.println(loginResult.toString());
                        }
                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Odebrany obiekt nie jest typy LoginMessage");

                }
            }

            //inputStream.close();
            //Zamknij Object(Input/Output)Stream
            //inputStream.close();
            //outputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Serwer niedostępny");
        }

        //Zwrócenie informacji o tym czy zalogowano do serwera;
        return result;
    }
}
