package app.login;

import java.net.Socket;

/**
 * Created by Kamil on 2016-12-10.
 */
public class SocketResult {
    private Socket socket;
    private boolean loginConfirm;

    public SocketResult(Socket socket, boolean loginConfirm) {
        this.socket = socket;
        this.loginConfirm = loginConfirm;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isLoginConfirm() {
        return loginConfirm;
    }
}
