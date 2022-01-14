package ba.unsa.etf.rpr;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


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