package app.dataBaseConnection;

import app.contacts.SearchedContact;
import app.register.Register;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    //Funkcja służąca do dodawania nowego użytkownika do bazy
    public int addNewUser(Register newUser) {
        int result = 0;
        try {
            Connection connection = DriverManager.getConnection(this.url, this.userName, this.password);

            PreparedStatement statement = connection.prepareStatement("INSERT INTO users values(ID,?,?,?,?,?)");
            statement.setString(1, newUser.getUserName());
            statement.setString(2, newUser.getPassword());
            statement.setString(3, newUser.getFirstName());
            statement.setString(4, newUser.getSecondName());
            statement.setString(5, newUser.getCity());

            statement.executeUpdate();

            statement = connection.prepareStatement("SELECT ID FROM users WHERE userName=?");
            statement.setString(1,newUser.getUserName());

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getInt("ID");
            connection.close();
            statement.close();

            System.out.println(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //Funkcja do sprawdzenia czy podany użytkownik istnieje w bazie
    public boolean checkUserName(String userName) {

        boolean result = false;
        try {
            Connection connection = DriverManager.getConnection(this.url, this.userName, this.password);

            PreparedStatement statement  = connection.prepareStatement("select userName from users where userName = ?;");
            statement.setString(1, userName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                result = true;
            }
            connection.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Problem z połączeniem z bazą danych!");
        }

        return result;

    }
    //Funkcja która zwraca wyszukaną listę kontaktów
    public List<SearchedContact> getContactsList(String id, String firstName, String lastName, String city){
        List<SearchedContact> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(this.url, this.userName, this.password);
            String query = "select ID, firstName, secondName, city from users where ";
            List<String> var = new ArrayList<>();
            var.add(id);
            var.add(firstName);
            var.add(lastName);
            var.add(city);
            int number = 0;
            for(int i = 0; i < var.size(); i++){
                if(!var.get(i).isEmpty()){

                    if(number == 0){
                        switch (i){
                            case 0:
                                query+= "ID = " +   id;
                                break;
                            case 1:
                                query+= "firstName = " + "'" + firstName + "'";
                                break;
                            case 2:
                                query+= "secondName = " + "'" + lastName + "'";
                                break;
                            case 3:
                                query+= "city = " + "'" + city + "'";
                                break;

                        }
                        number++;
                    }
                    else{
                        switch (i){
                            case 1:
                                query+= " AND firstName = " + "'" + firstName + "'";
                                break;
                            case 2:
                                query+= " AND secondName = " + "'" + lastName + "'";
                                break;
                            case 3:
                                query+= " AND city = " + "'" + city + "'";
                                break;

                        }
                    }
                }
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                result.add(new SearchedContact(resultSet.getString("firstName"), resultSet.getString("secondName"), resultSet.getString("city"), resultSet.getInt("ID")));
            }

            connection.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Nie podano wszystkich parametrów");
        }

        return result;

    }


}
