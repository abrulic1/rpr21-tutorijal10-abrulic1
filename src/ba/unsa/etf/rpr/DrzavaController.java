package ba.unsa.etf.rpr;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DrzavaController {
    public TextField fieldNaziv;
    public ChoiceBox choiceGrad;
    public Button btnOk;
    public Button btnCancel;


    @FXML
    public void initialize(){
        //TextField je po defaultu crven, kasnije postaje zelen ili crven zavisno od unosa
        fieldNaziv.getStyleClass().add("prazno");

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
    }

    public void CancelAction(ActionEvent actionEvent) {
        Stage stage=(Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
