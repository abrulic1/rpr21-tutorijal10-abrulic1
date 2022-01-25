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
    public ChoiceBox<Drzava> choiceDrzava;
   public Grad grad=null;
    GeografijaDAO dao = GeografijaDAO.getInstance();
    private ArrayList<Grad> gradoviIzGeografijaDAO=dao.gradovi();
    private ObservableList<Grad> gradovi;
    private ArrayList<Drzava>drzaveIzGeografijaDAO=dao.drzave();
    private ObservableList<Drzava> drzave;

    public GradController(Grad gradKojiSeEdituje, ArrayList<Drzava> countries){
        grad=gradKojiSeEdituje;
        //Još jedan parametar konstruktoru klase GradController - prima grad koji se edituje ili null ako je u pitanju dodavanje novog grada.
        drzaveIzGeografijaDAO=countries;
        //drzave=FXCollections.observableList(drzaveIzGeografijaDAO);

    }

    public GradController() {
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

    public Grad getGrad() {
      return grad;
    }

    public void btnOkAction(ActionEvent actionEvent) {
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
        if(!fieldNaziv.getText().isEmpty() && !fieldBrojStanovnika.getText().isEmpty() && broj > 0 && grad==null){
            //null, dodaje se novi grad, treba mu novi id
            Grad novi=new Grad();
            grad=novi;
        grad.setNaziv(naziv);
        grad.setBrojStanovnika(brojStanovnika);
        grad.setDrzava(choiceDrzava.getSelectionModel().getSelectedItem());
         int id=dao.IDGrada();
         grad.setId(id);
        }
        /*else if(!fieldNaziv.getText().isEmpty() && !fieldBrojStanovnika.getText().isEmpty() && broj > 0 && grad!=null){
            grad.setNaziv(naziv);
            grad.setBrojStanovnika(brojStanovnika);
            grad.setDrzava(choiceDrzava.getSelectionModel().getSelectedItem());
            int stariID=dao.vratiStariIDGrada(naziv);
            grad.setId(stariID);
        }*/
        Stage stage=(Stage) btnOk.getScene().getWindow();
        stage.close();
    }
}
