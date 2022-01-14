package ba.unsa.etf.rpr;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import javafx.event.ActionEvent;

public class DrzavaController {
    public TextField fieldNaziv;
    public ChoiceBox<Grad> choiceGrad;
    private GeografijaDAO geografijaDAO;
    private ArrayList<Grad> gradovi;
    private Drzava drzava = null;


    public void cancelAction(ActionEvent actionEvent) {
        Stage stage = (Stage) fieldNaziv.getScene().getWindow();
        stage.close();
    }

    public void dodajDrzavu(ActionEvent actionEvent) {

    }



}
