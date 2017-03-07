package app.messages;

import java.io.Serializable;

/**
 * Created by Kamil on 2016-12-04.
 */
public class LoginMessage implements Serializable {

    private static final long serialVersionUID = -4397335348927370232L;
    private String login;
    private String password;
    private boolean logged;
    private int userID;

    @Override
    public String toString() {
        return "LoginMessage{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", logged=" + logged +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void setUserID(int userID){
        this.userID = userID;
    }

    public int getUserID(){
        return this.userID;
    }

    public LoginMessage(String login, String password, boolean logged, int userID){
        this.login = login;
        this.password = password;
        this.logged = logged;
        this.userID = userID;
    }


}
