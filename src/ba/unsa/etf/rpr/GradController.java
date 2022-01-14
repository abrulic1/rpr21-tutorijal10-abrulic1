package ba.unsa.etf.rpr;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GradController implements Initializable{
    public TextField fieldNaziv;
    public TextField fieldBrojStanovnika;
    public ChoiceBox<Drzava> choiceDrzava;
    private Grad grad = null;


    public void dodajGrad(ActionEvent actionEvent) {
    }

    public void cancelAction(ActionEvent actionEvent) {
        Stage stage = (Stage) fieldNaziv.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
