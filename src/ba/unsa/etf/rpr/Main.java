package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Main{

    public static void main(String[] args) {
    }

    public static String ispisiGradove() {
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO geografijaDAO = GeografijaDAO.getInstance();
        ArrayList<Grad> gradovi = geografijaDAO.gradovi();
        String vrati="";
        for(Grad g : gradovi)
            vrati+=g.getNaziv()+ " (" + g.getDrzava().getNaziv() + ") - " + g.getBrojStanovnika()+"\n";
        return vrati;
    }

    public static void glavniGrad() {
        System.out.println("Unesite ime drzave: ");
        Scanner scanner = new Scanner(System.in);
        String drzava = scanner.nextLine();

        GeografijaDAO geografijaDAO = GeografijaDAO.getInstance();
        Grad grad = geografijaDAO.glavniGrad(drzava);
        if(grad == null) System.out.println("Nepostojeća država");
        else System.out.println(String.format("Glavni grad države %s je %s", drzava, grad.getNaziv()));
    }

}
