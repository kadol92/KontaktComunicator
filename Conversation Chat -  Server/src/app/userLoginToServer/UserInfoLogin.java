package app.userLoginToServer;

/**
 * Created by Kamil on 2016-12-10.
 */
public class UserInfoLogin {
    private UserInfo userInfo;
    private boolean isLogged;

    public UserInfoLogin(UserInfo userInfo, boolean isLogged) {
        this.userInfo = userInfo;
        this.isLogged = isLogged;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public String toString() {
        return "UserInfoLogin{" +
                "userInfo=" + userInfo +
                ", isLogged=" + isLogged + userInfo.toString()+
                '}';
    }

    public boolean isLogged() {
        return isLogged;
    }
}
