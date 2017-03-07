package app.messages;

import java.io.Serializable;

/**
 * Created by Kamil on 2016-12-01.
 */
public class ChatMessage implements Serializable{
    private static final long serialVersionUID = -2087667482202460305L;
    private String message;
    private int fromWho;
    private int toWho;


    public ChatMessage(String message, int fromWho, int toWho) {
        this.message = message;
        this.fromWho = fromWho;
        this.toWho = toWho;
    }


    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", fromWho=" + fromWho +
                ", toWho=" + toWho ;
    }




    public String getMessage() {
        return message;
    }



    public int getFromWho() {
        return fromWho;
    }

    public void setFromWho(int fromWho) {
        this.fromWho = fromWho;
    }

    public int getToWho() {
        return toWho;
    }

    public void setToWho(int toWho) {
        this.toWho = toWho;
    }


}
