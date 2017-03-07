package app.DataBase;



import app.userLoginToServer.UserInfo;

import java.sql.*;

/**
 * Created by Kamil on 2016-12-03.
 */
public class DataBaseConnection {

    private String url;
    private String userName;
    private String password;

    public DataBaseConnection(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;

    }

    public void getUserPassword(){



    }

    public UserInfo getUserPassword(String userName) {

        UserInfo userInfo = null;
        String name="";
        String password="";
        int ID=-1;
        try {
            Connection connection = DriverManager.getConnection(this.url, this.userName, this.password);

            PreparedStatement statement  = connection.prepareStatement("select * from users where userName = ?;");
            statement.setString(1, userName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                name = resultSet.getString("userName");
                password = resultSet.getString("userPassword");
                ID = resultSet.getInt("ID");

            }
            connection.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Problem z połączeniem z bazą danych!");
        }

        return new UserInfo(name,password,ID);

    }


}
