package com.savaleks;

import com.savaleks.model.Person;
import com.savaleks.model.PersonListWrapper;
import com.savaleks.view.BirthdayStatisticsController;
import com.savaleks.view.PersonEditDialogController;
import com.savaleks.view.PersonOverviewController;
import com.savaleks.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    /**
     * Returns the data as an observable list of Persons.
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        this.primaryStage.getIcons().add(new Image("file:resources/images/address-book.png"));

        initRootLayout();

        showPersonOverview();
    }

    /**
     * Initializes the root layout and loading last open file with address
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Get controller access to main app
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try load file with last open address
        File file = getPersonFilePath();
        if (file != null){
            loadPersonDataFromFile(file);
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public boolean showPersonEditDialog(Person person){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    // == return last open file  ==
    public File getPersonFilePath(){
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        String filePath = preferences.get("filePath", null);
        if (filePath != null){
            return new File(filePath);
        } else {
            return null;
        }
    }

    // == get path for created file ==
    public void setPersonFilePath(File file){
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        if (file != null){
            preferences.put("filePath", file.getPath());
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            preferences.remove("filePath");
            primaryStage.setTitle("AddressApp");
        }
    }

    // == loading information from XML file, data will be changed ==
    public void loadPersonDataFromFile(File file){
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // == reading XML file and unmarshaling ==
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            // == save file path ==
            setPersonFilePath(file);

        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot load data");
            alert.setContentText("Couldn't load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    // == saving current data about address holders in file ==
    public void savePersonDataToFile(File file){
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // == wrapping our data about address holders ==
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            // == marshalling and saving data in XML file ==
            m.marshal(wrapper, file);

            // == saving file path ==
            setPersonFilePath(file);

        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot save data");
            alert.setContentText("Couldn't save data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    // Open dialog window for output birthday statistics

    public void showBirthdayStatistics(){
        try {
            // load fxml file and create new scene for pop up window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // get address to controller
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(personData);

            dialogStage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
