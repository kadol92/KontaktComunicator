package app.userLoginToServer;

/**
 * Created by Kamil on 2016-12-10.
 */
public class UserInfo {
    private String userName;
    private String password;
    private int ID;

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", ID=" + ID +
                '}';
    }

    public UserInfo(String userName, String password, int ID) {
        this.userName = userName;
        this.password = password;
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getID() {
        return ID;
    }
}
