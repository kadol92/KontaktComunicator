package controllers;

import app.contacts.Contact;
import app.contacts.SearchedContact;
import app.dataBaseConnection.DataBaseConfig;
import app.dataBaseConnection.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.Collection;


/**
 * Created by Kamil on 2017-01-19.
 */
public class SearchController {
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn surnameColumn;
    @FXML
    private TableColumn cityColumn;
    @FXML
    private TableView searchTable;
    @FXML
    private VBox searchContact;
    @FXML
    private TextField searchID;
    @FXML
    private TextField searchName;
    @FXML
    private TextField searchSurname;
    @FXML
    private TextField searchCity;
    @FXML
    private MainController mainController;
    private DataBaseConnection dataBaseConnection;

    ObservableList<SearchedContact> data;

    public void init(MainController mainController) {
        this.mainController = mainController;
        this.dataBaseConnection = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD);
        idColumn.prefWidthProperty().bind(searchTable.widthProperty().divide(4));
        nameColumn.prefWidthProperty().bind(searchTable.widthProperty().divide(4));
        cityColumn.prefWidthProperty().bind(searchTable.widthProperty().divide(4));
        surnameColumn.prefWidthProperty().bind(searchTable.widthProperty().divide(4));

        idColumn.setCellValueFactory(new PropertyValueFactory<SearchedContact, Integer>("userID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<SearchedContact, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<SearchedContact, String>("surname"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<SearchedContact, String>("city"));
        this.data = FXCollections.observableArrayList();

        searchTable.setItems(data);
    }

    public VBox getSearchPanel() {
        return searchContact;
    }

    //Wyszukiwanie kontaktów
    public void searchContacts() {
        data.clear();
        data.addAll(dataBaseConnection.getContactsList(searchID.getText(), searchName.getText(), searchSurname.getText(), searchCity.getText()));
    }

    //Dodanie nowego kontaktu do listy
    public void addNewContactToList() {
        try{
            int index = searchTable.getSelectionModel().getSelectedIndex();
            String name = (String) nameColumn.getCellObservableValue(index).getValue();
            String surname = (String) surnameColumn.getCellObservableValue(index).getValue();
            String city = (String) cityColumn.getCellObservableValue(index).getValue();
            int userID = (Integer) idColumn.getCellObservableValue(index).getValue();

            if(name.isEmpty() && surname.isEmpty()){
                name = Integer.toString(userID);
            }
            else{
                name += " " + surname;
            }
            mainController.addNewContactToList(new Contact(name, city, userID));
        }catch (NullPointerException e){
            System.out.println("Nic nie wybrano!");
        }

    }
}
