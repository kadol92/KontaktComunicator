package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Created by Kamil on 2017-01-19.
 */
public class EditContactController {
    @FXML private Label contactName;

    public Label getContactName() {
        return contactName;
    }

    public Label getContactCity() {
        return contactCity;
    }

    public Label getContactNumber() {
        return contactNumber;
    }

    @FXML private Label contactCity;
    @FXML private Label contactNumber;
    @FXML private TextField newContactNameFiled;
    @FXML private MainController mainController;
    @FXML private Button saveChangedContact;

    public VBox getEditContactPanel() {
        return editContactPanel;
    }

    @FXML private VBox editContactPanel;

    public void init(MainController mainController){
        this.mainController = mainController;
    }

    public Button getSaveChangedContact() {
        return saveChangedContact;
    }

    //Zapisywanie zmian w dodanym kontakcie
    public void saveChangedContact(){
        mainController.updateContactName(newContactNameFiled.getText());
        contactName.setText(newContactNameFiled.getText());
        saveChangedContact.setDisable(true);
    }

}
