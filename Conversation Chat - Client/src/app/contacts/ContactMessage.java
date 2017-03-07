package app.contacts;

/**
 * Created by Kamil on 2017-01-19.
 */
public class ContactMessage {
    private String message;

    public boolean isSendToServer() {
        return sendToServer;
    }

    public void setSendToServer(boolean sendToServer) {
        this.sendToServer = sendToServer;
    }

    private boolean sendToServer;

    public ContactMessage(String message, boolean sendToServer) {
        this.message = message;
        this.sendToServer = sendToServer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
