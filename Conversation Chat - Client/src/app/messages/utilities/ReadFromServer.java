package app.messages.utilities;

import app.messages.ChatMessage;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Kamil on 2016-12-05.
 */
public class ReadFromServer {

    public ChatMessage readMessage(ObjectInputStream inputStream) throws IOException, ClassNotFoundException{

        //Odbierz wiadomość z serwera
        ChatMessage chatMessage = (ChatMessage) inputStream.readObject();

        return  chatMessage;
    }
}
