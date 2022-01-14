package ba.unsa.etf.rpr;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController implements Initializable {

    public TableView<Grad> tableViewGradovi;
    public TableColumn<Grad, String> colGradId;
    public TableColumn<Grad, String> colGradNaziv;
    public TableColumn<Grad, String> colGradStanovnika;
    public TableColumn<Grad, Drzava> colGradDrzava;

    private GeografijaDAO geografijaDAO;

    public GlavnaController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        List<Grad> gradovi = geografijaDAO.gradovi();
//        gradovi.forEach(e -> tableViewGradovi.getItems().add(e));
        geografijaDAO = GeografijaDAO.getInstance();
        colGradId.setCellValueFactory(new PropertyValueFactory<Grad, String>("id"));
        colGradNaziv.setCellValueFactory(new PropertyValueFactory<Grad, String>("naziv"));
        colGradStanovnika.setCellValueFactory(new PropertyValueFactory<Grad, String>("brojStanovnika"));
        colGradDrzava.setCellValueFactory(new PropertyValueFactory<Grad, Drzava>("drzava"));
        tableViewGradovi.getItems().setAll(geografijaDAO.gradovi());
        GeografijaDAO.removeInstance();
    }

    public void dodajGrad(ActionEvent actionEvent) {
    }

    public void dodajDrzavu(ActionEvent actionEvent) {
    }

    public void izmijeniGrad(ActionEvent actionEvent) {
    }

    public void obrisiGrad(ActionEvent actionEvent) {
    }
}