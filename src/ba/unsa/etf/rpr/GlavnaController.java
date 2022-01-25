package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {
    public Button btnDodajGrad;
    public Button btnDodajDrzavu;
    public Button btnIzmijeniGrad;
    public Button btnObrisiGrad;
    public TableView<Grad> tableViewGradovi;
    public TableColumn<Integer, Grad> colGradId;
    public TableColumn<String, Grad> colGradNaziv;
    public TableColumn<Integer, Grad> colGradStanovnika;
    public TableColumn<Drzava, Grad> colGradDrzava;

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
       DrzavaController kontroler = new DrzavaController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drzava.fxml"));
        loader.setController(kontroler);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        stage.setTitle("Drzava");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnHiding(lambda->{
            Drzava drzava= kontroler.getDrzavu();
            if(drzava!=null){
                dao.dodajDrzavu(drzava);
            }
        });
    }

    public void IzmijeniGradAction(ActionEvent actionEvent) throws IOException {
         if(tableViewGradovi.getSelectionModel().getSelectedItem()!=null){
             Grad grad=tableViewGradovi.getSelectionModel().getSelectedItem();
             GradController kontroler = new GradController();
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
             loader.setController(kontroler);
             Parent root = loader.load();
             Stage stage = new Stage();
             Scene scene = new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
             stage.setTitle("Grad");
             stage.setScene(scene);
             stage.setResizable(false);
             kontroler.fieldNaziv.setText(grad.getNaziv());
             kontroler.fieldBrojStanovnika.setText(String.valueOf(grad.getBrojStanovnika()));
             kontroler.choiceDrzava.getSelectionModel().select(grad.getDrzava());
             stage.show();
             stage.setOnHiding(lambda -> {
                 Grad grad2 = kontroler.getGrad();
                 if (grad2 != null) {
                     grad2.setId(grad.getId());
                     dao.izmijeniGrad(grad2);
                     tableViewGradovi.getSelectionModel().getSelectedItem().setNaziv(grad2.getNaziv());
                     tableViewGradovi.getSelectionModel().getSelectedItem().setBrojStanovnika(grad2.getBrojStanovnika());
                     tableViewGradovi.getSelectionModel().getSelectedItem().setDrzava(grad2.getDrzava());
                     tableViewGradovi.refresh();
                 }
             });
         }
    }

    public void DodajGradAction(ActionEvent actionEvent) throws IOException {
        GradController kontroler = new GradController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        loader.setController(kontroler);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        stage.setTitle("Grad");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnHiding(lambda->{
            Grad grad= kontroler.getGrad();
            if(grad!=null){
                dao.dodajGrad(grad);
                tableViewGradovi.getItems().add(grad);
                tableViewGradovi.refresh();
            }
        });
    }



    public void ObrisiGradAction(ActionEvent actionEvent) {
        Grad grad = tableViewGradovi.getSelectionModel().getSelectedItem();

        if (grad != null) {
            dao = GeografijaDAO.getInstance();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Da li zelite obrisati selektovani grad?");
            alert.setHeaderText("");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                dao.obrisiGrad(grad.getId());
                tableViewGradovi.getItems().remove(grad);
                tableViewGradovi.refresh();
            }
        }
    }
}


