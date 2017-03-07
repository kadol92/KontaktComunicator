package controllers;

import app.dataBaseConnection.DataBaseConfig;
import app.dataBaseConnection.DataBaseConnection;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import app.register.Register;


public class RegisterController {

    public TextField userNameField;
    public TextField passwdField;
    public TextField repeatPasswdField;
    public TextField firstNameField;
    public TextField secondNameField;
    public TextField cityField;
    public Label passwordCheckLabel;
    public Label userNameCheckLabel;
    public Button checkUserNameAvailabilityButton;
    public Button registerButton;
    public VBox newUserVBox;
    public Label regUserName;
    public Label regID;
    public Label regFirstName;
    public Label regLastName;
    public Label regCity;
    public Label registerInfoLabel;

    public boolean checkUserName() {
        //Sprawdzamy czy w nazwie użytkownika znajdują się spacje i zwracamy stosowny wynik
        if (userNameField.getText().contains(" ")) {
            userNameCheckLabel.setVisible(true);
            userNameCheckLabel.setText("Spacje są niedopuszczalne!");
            userNameCheckLabel.setStyle("-fx-text-fill: red");
            return false;
        } else {
            userNameCheckLabel.setVisible(false);
            return true;
        }

    }

    public boolean checkUserNameAvailability() {
        //Szczytujemy wartość pobraną z pola nazwy użytkownika
        String userName = userNameField.getText();

        //Wynik wykonania operacji w zmiennej result
        boolean result = false;

        //Sprawdzamy czy dlugosc uzytkownika jest wieksza niz 4 litery, oraz czy nie zawiera spacji
        if (userName.length() >= 4 && checkUserName()) {

            //Tworzymy obiekt Bazy danch
            DataBaseConnection db = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD);

            //Wysyłamy zapytanie czy podany użytkownik istnieje i dostajemy odpowiedz w postaci true/false
            result = !db.checkUserName(userName);

            //Jesli użytkownik nie istnieje, zwracamy stosowny komunikat
            if (result) {
                userNameCheckLabel.setVisible(true);
                userNameCheckLabel.setText("Nazwa dostępna.");
                userNameCheckLabel.setStyle("-fx-text-fill: green");
            } else {
                userNameCheckLabel.setVisible(true);
                userNameCheckLabel.setText("Nazwa zajęta!");
                userNameCheckLabel.setStyle("-fx-text-fill: red");
            }
        } else if (userName.length() < 4) {
            userNameCheckLabel.setVisible(true);
            userNameCheckLabel.setText("Zbyt mało znaków (min. 4)!");
            userNameCheckLabel.setStyle("-fx-text-fill: red");
        }

        //Zwracamy wynik zanegowany z racji n
        return result;
    }

    public boolean checkUserPassword() {
        //Sczytujemy wartości z pola do wpisywania hasła i z pola do ponownego wpisania hasła
        String password = passwdField.getText();
        String repeatPassword = repeatPasswdField.getText();

        //Zmienna do której zapisywany jest wynik
        boolean result = false;

        //Sprawdzamy czy coś znajduje się w blokach a następnie sprawdzamy czy wpisane hasła są sobie równe
        if (password.equals(repeatPassword) && !password.isEmpty() && !repeatPassword.isEmpty()) {
            passwordCheckLabel.setVisible(true);
            passwordCheckLabel.setText("Hasła są zgodne.");
            passwordCheckLabel.setStyle("-fx-text-fill: green");
            result = true;

        } else if (!password.isEmpty() && !repeatPassword.isEmpty()) {
            passwordCheckLabel.setVisible(true);
            passwordCheckLabel.setText("Hasła nie są zgodne.");
            passwordCheckLabel.setStyle("-fx-text-fill: red");
        } else {
            passwordCheckLabel.setVisible(false);
        }

        return result;
    }

    //Dodawanie nowego użytkownika do bazy danych
    public void registerUser() {

        //Sprawdzanie czy zostały spełnione warunku na odpowiednio wpisane hasło i czy nazwa użytkownika jest dostępna
        if (checkUserPassword() && checkUserNameAvailability()) {

            //Pobranie wartości z pól tekstowych
            String userName = userNameField.getText();
            String password = passwdField.getText();
            String firstName = firstNameField.getText();
            String secondName = secondNameField.getText();
            String city = cityField.getText();

            //Utworzenie nowego użytkownika z pobranych danych
            Register newUser = new Register(password, userName, firstName, secondName, city);

            //Debugowanie zapisanego użytkownika
            System.out.println(newUser.toString());

            //Tworzymy obiekt Bazy danch
            DataBaseConnection db = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD);

            //Dodanie do bazy danych nowego użytkownika i zwrócenie przypisanego do niego numeru
            int ID = db.addNewUser(newUser);

            //Warunek sprawdzajacy czy użytkownik został dodany do bazy
            if (ID > 0) {

                newUserVBox.setVisible(true);
                registerButton.setVisible(false);
                this.regUserName.setText(userName);
                this.regID.setText(Integer.toString(ID));
                this.regFirstName.setText(firstName);
                this.regLastName.setText(secondName);
                this.regCity.setText(city);
                this.registerInfoLabel.setVisible(false);

            } else {
                this.registerInfoLabel.setVisible(true);
                this.registerInfoLabel.setText("Coś poszlo nie tak. Spróbuj ponownie później. ");
                this.registerInfoLabel.setStyle("-fx-text-fill: red;");
            }


        }
    }




}
