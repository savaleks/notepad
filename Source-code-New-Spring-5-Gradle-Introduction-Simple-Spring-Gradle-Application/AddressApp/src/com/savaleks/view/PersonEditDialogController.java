package com.savaleks.view;

import com.savaleks.model.Person;
import com.savaleks.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PersonEditDialogController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField postcodeField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField birthdayField;


    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    @FXML
    private void initialize(){
    }

    // == set up scene for this window ==
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    // == get address holder for change information ==
    public void setPerson(Person person){
        this.person = person;

        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        streetField.setText(person.getStreet());
        postcodeField.setText(Integer.toString(person.getPostalCode()));
        cityField.setText(person.getCity());
        birthdayField.setText(DateUtil.format(person.getBirthday()));
        birthdayField.setPromptText("dd.mm.yyyy");
    }

    // == if user click ok, return true, another false ==
    public boolean isOkClicked(){
        return okClicked;
    }

    // == call method when user clicked button ok ==
    @FXML
    private void handleOk(){
        if (isInputValid()){
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setStreet(streetField.getText());
            person.setPostalCode(Integer.parseInt(postcodeField.getText()));
            person.setCity(cityField.getText());
            person.setBirthday(DateUtil.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    // == user click on cancel button ==
    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    // == checking user input return true if user input correct ==
    private boolean isInputValid(){
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0){
            errorMessage += "No valid first name!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0){
            errorMessage += "No valid last name!\n";
        }
        if (streetField.getText() == null || streetField.getText().length() == 0){
            errorMessage += "No valid street!\n";
        }
        if (postcodeField.getText() == null || postcodeField.getText().length() == 0){
            errorMessage += "No valid Postcode!\n";
        } else {
            // == trying convert postcode to int ==
            try {
                Integer.parseInt(postcodeField.getText());
            } catch (NumberFormatException e){
                errorMessage += "No valid postcode (must be integer)!\n";
            }
        }
        if (cityField.getText() == null || cityField.getText().length() == 0){
            errorMessage += "No valid city!\n";
        }
        if (birthdayField.getText() == null || birthdayField.getText().length() == 0){
            errorMessage += "No valid birthday!\n";
        } else {
            if (!DateUtil.validDate(birthdayField.getText())){
                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0){
            return true;
        } else {
            // == message about error ==
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Field");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
