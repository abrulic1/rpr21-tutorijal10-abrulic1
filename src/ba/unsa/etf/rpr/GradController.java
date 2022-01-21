package ba.unsa.etf.rpr;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GradController {
    public TextField fieldNaziv;
    public Button btnOk;
    public Button btnCancel;
    public TextField fieldBrojStanovnika;
    public ChoiceBox<String> choiceDrzava;

    GeografijaDAO dao = GeografijaDAO.getInstance();
    private ArrayList<Grad> gradoviIzGeografijaDAO=dao.gradovi();
    private ObservableList<Grad> gradovi;
    private ArrayList<String>drzaveIzGeografijaDAO=dao.drzave();
    private ObservableList<String> drzave;

    public GradController(){
        drzave=FXCollections.observableList(drzaveIzGeografijaDAO);
    }


    @FXML
    public void initialize(){
        choiceDrzava.setItems(drzave);
        choiceDrzava.getSelectionModel().selectFirst();
        //TextField i brojstanovnika su po defaoultu crven, kasnije postaju zeleni ili crveni zavisno od unosa
        fieldNaziv.getStyleClass().add("prazno");
        fieldBrojStanovnika.getStyleClass().add("prazno");

        fieldNaziv.textProperty().addListener((obs,oldNaziv,newNaziv)->{
            if(!newNaziv.isEmpty()){
                fieldNaziv.getStyleClass().removeAll("prazno");
                fieldNaziv.getStyleClass().add("popunjeno");
            }
            else{
                fieldNaziv.getStyleClass().removeAll("popunjeno");
                fieldNaziv.getStyleClass().add("prazno");
            }
        });

        fieldBrojStanovnika.textProperty().addListener((obs,oldBroj,newBroj)->{
            int broj=0;

            try{
                broj=Integer.parseInt(newBroj);
            }catch(NumberFormatException e){

            }
            if(!newBroj.isEmpty() && broj>0){
                fieldBrojStanovnika.getStyleClass().removeAll("prazno");
                fieldBrojStanovnika.getStyleClass().add("popunjeno");
            }
            else{
                fieldBrojStanovnika.getStyleClass().removeAll("popunjeno");
                fieldBrojStanovnika.getStyleClass().add("prazno");
            }
        });

    }

    public void CancelAction(ActionEvent actionEvent) {
        Stage stage=(Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void getGradAction(ActionEvent actionEvent) {
        String naziv = "";
        int brojStanovnika = 0;
        String nazivDrzave="";
        if (!fieldNaziv.getText().isEmpty())
            naziv = fieldNaziv.getText();

        int broj = 0;
        try {
            broj = Integer.parseInt(fieldBrojStanovnika.getText());
        } catch (NumberFormatException e) {

        }

        if (!fieldBrojStanovnika.getText().isEmpty() && broj > 0)
            brojStanovnika = broj;

       nazivDrzave= String.valueOf(choiceDrzava.getSelectionModel().selectedItemProperty());

    }
}
