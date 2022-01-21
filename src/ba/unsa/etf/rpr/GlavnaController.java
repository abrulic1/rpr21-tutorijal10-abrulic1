package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {
    public Button btnDodajGrad;
    public Button btnDodajDrzavu;
    public Button btnIzmijeniGrad;
    public Button btnObrisiGrad;
    public TableView<Grad> tableViewGradovi;
    public TableColumn<String, Grad> colGradId;
    public TableColumn<String, Grad> colGradNaziv;
    public TableColumn<String, Grad> colGradStanovnika;
    public TableColumn<String, Grad> colGradDrzava;


    GeografijaDAO dao = GeografijaDAO.getInstance();
    private ObservableList<Grad> gradovi;
    private ArrayList<Grad> gradovi2 = new ArrayList<>();
    public GlavnaController(){

    }



    @FXML
    public void initialize(){
        gradovi2.addAll(dao.gradovi());
        gradovi=FXCollections.observableList(gradovi2);
        colGradId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGradNaziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        colGradStanovnika.setCellValueFactory(new PropertyValueFactory<>("brojStanovnika"));
        colGradDrzava.setCellValueFactory(new PropertyValueFactory<>("drzava"));
        tableViewGradovi.setItems(gradovi);

    }

    public void DodajDrzavuAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/drzava.fxml"));
        Parent root = loader.load();
        DrzavaController otvorenNoviProzor=loader.getController();
        Scene scene = new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        stage.setTitle("Država");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void IzmijeniGradAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        Parent root = loader.load();
        GradController otvorenNoviProzor=loader.getController();
        Scene scene = new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        stage.setTitle("Grad");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void DodajGradAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        Parent root = loader.load();
        GradController otvorenNoviProzor=loader.getController();
        Scene scene = new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        stage.setTitle("Grad");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
