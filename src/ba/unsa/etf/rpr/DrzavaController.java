package ba.unsa.etf.rpr;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class DrzavaController {
    public TextField fieldNaziv;
    public ChoiceBox<Grad> choiceGrad;
    public Button btnOk;
    public Button btnCancel;
    public Drzava drzava = null;
    GeografijaDAO dao = GeografijaDAO.getInstance();
    private ArrayList<Grad> gradovi2 = dao.gradovi();
    private ObservableList<Grad> gradovi = FXCollections.observableList(gradovi2);

    public DrzavaController(Drzava drzava, ArrayList<Grad> gradovi) {
        this.drzava = drzava;
        this.gradovi2 = gradovi;
    }

    @FXML

    public void initialize() {
        choiceGrad.setItems(gradovi);
        choiceGrad.getSelectionModel().selectFirst();
        //TextField je po defaultu crven, kasnije postaje zelen ili crven zavisno od unosa
        fieldNaziv.getStyleClass().add("prazno");

        fieldNaziv.textProperty().addListener((obs, oldNaziv, newNaziv) -> {
            if (!newNaziv.trim().isEmpty()) {
                fieldNaziv.getStyleClass().removeAll("prazno");
                fieldNaziv.getStyleClass().add("popunjeno");
            } else {
                fieldNaziv.getStyleClass().removeAll("popunjeno");
                fieldNaziv.getStyleClass().add("prazno");
            }
        });
    }

    public void CancelAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void btnOkAction(ActionEvent actionEvent) {
        if (!fieldNaziv.getText().trim().isEmpty() && drzava == null) {
            Drzava nova = new Drzava();
            drzava = nova;
            drzava.setNaziv(fieldNaziv.getText());
            drzava.setGlavniGrad(choiceGrad.getSelectionModel().getSelectedItem());
            int id = dao.IDDrzave();
            drzava.setId(id);
        }

        if (!fieldNaziv.getText().trim().isEmpty()) {
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        }
    }

    public Drzava getDrzavu() {
        return drzava;
    }
}
