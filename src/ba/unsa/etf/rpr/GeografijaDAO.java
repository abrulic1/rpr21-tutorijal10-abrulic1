package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.AllPermission;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn;

    private PreparedStatement glavniGradUpit, dajDrzavuUpit, obrisiDrzavuUpit, obrisiGradoveDrzave, idDrzaveUpit, vratiGradoveSortiraneUpit;

    public static GeografijaDAO getInstance(){
        if(instance==null) instance=new GeografijaDAO();
        return instance;
    }

    private GeografijaDAO(){
        try {
            conn= DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            glavniGradUpit=conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava FROM grad, drzava WHERE grad.drzava=drzava.id AND drzava.glavni_grad=?");
        } catch (SQLException e) {
            //sada ako dodje do izuzetka popunjavamo bazu default podacima
            regenerisiBazu();
            try {
                glavniGradUpit=conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava FROM grad, drzava WHERE grad.drzava=drzava.id AND drzava.glavni_grad=?");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        try {
            dajDrzavuUpit=conn.prepareStatement("SELECT * FROM drzava WHERE id=?");
            obrisiDrzavuUpit=conn.prepareStatement("DELETE FROM drzava WHERE id=?");
            obrisiGradoveDrzave= conn.prepareStatement("DELETE FROM grad WHERE drzava=?");
            idDrzaveUpit= conn.prepareStatement("SELECT id FROM drzava WHERE naziv=?");
            vratiGradoveSortiraneUpit= conn.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerisiBazu() {
        try {
            conn= DriverManager.getConnection("jdbc:sqlite:korisnici.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Scanner ulaz=null;
        try {
            ulaz=new Scanner(new FileInputStream("korisnik.db.sql"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String sqlUpit="";
        while(ulaz.hasNext()){
            sqlUpit+=ulaz.nextLine();
            if(sqlUpit.charAt(sqlUpit.length()-1)==';') {
                try {
                    Statement stmt=conn.createStatement();
                    stmt.execute(sqlUpit);
                    sqlUpit="";
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        ulaz.close();
    }

    public Grad glavniGrad(String drzava){
        try {
            glavniGradUpit.setString(1,drzava);
            ResultSet rs= glavniGradUpit.executeQuery();
            if(!rs.next()){
                //nema grada
                return null;
            }
            return dajGradIzResultSeta(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Grad dajGradIzResultSeta(ResultSet rs) throws SQLException {
        //exception ce biti uhvacen u ovoj gore public metodi ako se desi
        Grad grad= new Grad(rs.getInt(1), rs.getString(2), rs.getInt(3), null);
        grad.setDrzava(dajDrzavu(rs.getInt(4), grad));  //za dati ID
        return grad;
    }

    private Drzava dajDrzavu(int id, Grad grad) {
        try {
            dajDrzavuUpit.setInt(1, id);
            ResultSet rs=dajDrzavuUpit.executeQuery();
            if(!rs.next()) return null;
            return dajDrzavuIzResultSeta(rs, grad);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //POSLAT GRAD DA NE BI BILO ONE ZAFRKANCIJE...
    private Drzava dajDrzavuIzResultSeta(ResultSet rs, Grad grad) throws SQLException {
        return new Drzava(rs.getInt(1), rs.getString(2),grad);
    }



    public void obrisiDrzavu(String drzava){
        //trebamo dobiti id odavdje
        //brise drzavu iz baze, ali uzeti u obzir da moramo obrisati i gradove
        try {
            idDrzaveUpit.setString(1, drzava);
            ResultSet rs=idDrzaveUpit.executeQuery();
            int id=rs.getInt(1);
            if(!rs.next()) return;
            //u suprotnom brisemo tj saljemo id i brisemo
            obrisiGradoveDrzave.setInt(1, id);
            obrisiGradoveDrzave.execute();
            obrisiDrzavuUpit.setInt(1, id);
            obrisiDrzavuUpit.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> vrati = new ArrayList<>();
        try {
            ResultSet rs = vratiGradoveSortiraneUpit.executeQuery();
            if(!rs.next()) return null;
            while(rs.next())
                vrati.add(new Grad(rs.getInt(1), rs.getString(2), rs.getInt(3), null));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vrati;
        //stavila sam drzavu na null da se ne peglam sad sa tim opet
    }
}