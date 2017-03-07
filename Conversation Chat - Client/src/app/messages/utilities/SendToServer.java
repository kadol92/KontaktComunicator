package app.messages.utilities;

import app.messages.ChatMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Kamil on 2016-12-05.
 */
public class SendToServer {

    public boolean sendMessage(ChatMessage chatMessage, ObjectOutputStream outputStream){
        boolean result;
        try {

            //wyślij wiadomość na serwer
            outputStream.writeObject(chatMessage);
            result = true;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Serwer niedostępny");
            result = false;
        }

        //Zwróć informacje na temat tego czy wysłanie na serwer się powiodło
        return result;

    }
}
