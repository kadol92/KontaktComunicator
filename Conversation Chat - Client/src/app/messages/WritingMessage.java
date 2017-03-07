package app.messages;

import java.io.Serializable;

/**
 * Created by Kamil on 2017-01-21.
 */
public class WritingMessage implements Serializable{
    private static final long serialVersionUID = 9115086040886821545L;
    private boolean userWritng;
    private int fromWho;
    private int toWho;

    public WritingMessage(boolean userWritng, int fromWho, int toWho) {
        this.userWritng = userWritng;
        this.fromWho = fromWho;
        this.toWho = toWho;
    }

    public boolean isUserWritng() {
        return userWritng;
    }

    public int getFromWho() {
        return fromWho;
    }

    public int getToWho() {
        return toWho;
    }
}
