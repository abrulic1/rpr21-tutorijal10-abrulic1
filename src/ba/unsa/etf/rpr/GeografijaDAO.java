package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn;

    private PreparedStatement drzavaUpit, gradUpit, obrisiGradUpit, sacuvajPromjeneUpit, obrisiDrzavuUpit, vratiGradoveUpit, vratiDrzavuUpit,
            dodajGradUpit, dodajDrzavuUpit, vratiMaxIDGradUpit, vratiMaxIDDrzavaUpit, izmijeniGradUpit, nadjiDrzavuUpit,
            vratiDrzavuNaOsnovuIDUpit, vratiSveDrzaveIzBazeUpit, obrisiGradIzBazeUpit, trenutniGradUpit;

    public static GeografijaDAO getInstance() {
        if (instance == null) instance = new GeografijaDAO();
        return instance;
    }

    private GeografijaDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        try {
            drzavaUpit = conn.prepareStatement("SELECT id FROM drzava WHERE naziv=?");
        } catch (SQLException e) {
            //sada ako dodje do izuzetka popunjavamo bazu default podacima
            regenerisiBazu();
            try {
                drzavaUpit = conn.prepareStatement("SELECT id FROM drzava WHERE naziv=?");
            } catch (SQLException ex) {
                // ex.printStackTrace();
            }
        }

        try {
            gradUpit = conn.prepareStatement("SELECT id, naziv, broj_stanovnika FROM grad WHERE drzava=?");
            obrisiGradUpit = conn.prepareStatement("DELETE FROM grad WHERE drzava=?");
            sacuvajPromjeneUpit = conn.prepareStatement("COMMIT");
            obrisiDrzavuUpit = conn.prepareStatement("DELETE FROM drzava WHERE naziv=?");
            vratiGradoveUpit = conn.prepareStatement("SELECT id, naziv, broj_stanovnika, drzava FROM grad ORDER BY broj_stanovnika DESC");
            vratiDrzavuUpit = conn.prepareStatement("SELECT id, naziv, glavni_grad FROM drzava WHERE glavni_grad=?");
            dodajGradUpit = conn.prepareStatement("INSERT INTO grad VALUES(?,?,?,?)");
            dodajDrzavuUpit = conn.prepareStatement("INSERT INTO drzava VALUES(?,?,?)");
            vratiMaxIDGradUpit = conn.prepareStatement("SELECT MAX(id)+1 FROM grad");
            vratiMaxIDDrzavaUpit = conn.prepareStatement("SELECT MAX(id) FROM drzava");
            izmijeniGradUpit = conn.prepareStatement("UPDATE grad SET naziv=?, broj_stanovnika=?, drzava=? WHERE id=?");
            nadjiDrzavuUpit = conn.prepareStatement("SELECT id, naziv, glavni_grad FROM drzava WHERE naziv=?");
            vratiDrzavuNaOsnovuIDUpit = conn.prepareStatement("SELECT id, naziv, glavni_grad FROM drzava WHERE id=?");
            vratiSveDrzaveIzBazeUpit = conn.prepareStatement("SELECT id, naziv, glavni_grad FROM drzava ORDER BY naziv");
            obrisiGradIzBazeUpit = conn.prepareStatement("DELETE FROM grad WHERE id=?");
            trenutniGradUpit = conn.prepareStatement("SELECT id, naziv, broj_stanovnika, drzava FROM grad WHERE naziv=?");
        } catch (SQLException e) {
            //e.printStackTrace();
        }

    }

    public static void removeInstance() {
        if (instance == null) return;
        try {
            instance.conn.close();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        instance = null;
    }

    private void regenerisiBazu() {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("baza.db.sql"));
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
        }
        String sqlUpit = "";
        while (ulaz.hasNext()) {
            sqlUpit += ulaz.nextLine();
            if (sqlUpit.charAt(sqlUpit.length() - 1) == ';') {
                try {
                    Statement stmt = conn.createStatement();
                    stmt.execute(sqlUpit);
                    sqlUpit = "";
                } catch (SQLException e) {
                    // e.printStackTrace();
                }
            }
        }
        ulaz.close();
    }

    public Grad glavniGrad(String drzava) {
        //vraca glavni grad drzave
        Grad vrati = new Grad();
        Drzava country = new Drzava();
        country.setNaziv(drzava);
        try {
            drzavaUpit.setString(1, drzava);
            ResultSet rs = drzavaUpit.executeQuery();
            if (!rs.next()) return null;
            country.setId(rs.getInt(1));
            gradUpit.setInt(1, rs.getInt(1));
            rs = gradUpit.executeQuery();
            if (!rs.next()) return null;
            vrati.setId(rs.getInt(1));
            vrati.setNaziv(rs.getString(2));
            vrati.setBrojStanovnika(rs.getInt(3));
            //mora se i drzava postaviti
            country.setGlavniGrad(vrati);
            vrati.setDrzava(country);
        } catch (SQLException e) {
            //  e.printStackTrace();
        }
        return vrati;
    }


    public void obrisiDrzavu(String drzava) {
        //brise i sve gradove u toj drzavi
        try {
            drzavaUpit.setString(1, drzava);
            ResultSet rs = drzavaUpit.executeQuery();
            if (!rs.next()) return;
            // throw new NullPointerException("Takva drzava ne postoji u bazi podataka");

            gradUpit.setInt(1, rs.getInt(1));
            rs = gradUpit.executeQuery();
            if (!rs.next()) throw new NullPointerException("Takav grad ne postoji u bazi podataka");
            obrisiGradUpit.setInt(1, rs.getInt(1));
            obrisiGradUpit.execute();
            while (rs.next())
                obrisiGradUpit.execute();
            //sacuvajPromjeneUpit.execute();

            obrisiDrzavuUpit.execute();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public void obrisiGrad(int ID) {

        try {
            obrisiGradIzBazeUpit.setInt(1, ID);
            obrisiGradIzBazeUpit.execute();
            //ako je glavni grad mora se i njegova drzava obrisati
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> vrati = new ArrayList<>();
        try {
            ResultSet rs = vratiGradoveUpit.executeQuery();
            while (rs.next()) {
                Grad grad = new Grad();
                grad.setId(rs.getInt(1));
                grad.setNaziv(rs.getString(2));
                grad.setBrojStanovnika(rs.getInt(3));
                int idDrzave = rs.getInt(4);
                vratiDrzavuNaOsnovuIDUpit.setInt(1, idDrzave);
                ResultSet rs2 = vratiDrzavuNaOsnovuIDUpit.executeQuery();
                rs2.next();
                Drzava drzava = new Drzava();
                drzava.setId(idDrzave);
                drzava.setNaziv(rs2.getString(2));
                glavniGrad(drzava.getNaziv());
                grad.setDrzava(drzava);
                //  vratiDrzavu(grad);
                vrati.add(grad);

            }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return vrati;
    }


    private void vratiDrzavu(Grad grad) {
        Drzava drzava = new Drzava();
        try {
            vratiDrzavuUpit.setInt(1, grad.getId());
            ResultSet rs = vratiDrzavuUpit.executeQuery();
            rs.next();
            drzava.setId(rs.getInt(1));
            drzava.setNaziv(rs.getString(2));
            drzava.setGlavniGrad(grad);
            grad.setDrzava(drzava);
        } catch (SQLException e) {
            // e.printStackTrace();
        }

    }


    void dodajGrad(Grad grad) {
        //prvo provjera je li grad postoji
        //ArrayList<Grad> sviGradovi=gradovi();
        //if(sviGradovi.contains(grad)) return;
        try {
            ResultSet rs = vratiMaxIDGradUpit.executeQuery();
            int id = 1;
            if (rs.next()) id = rs.getInt(1);
            grad.setId(id);
            dodajGradUpit.setInt(1, id);
            dodajGradUpit.setString(2, grad.getNaziv());
            dodajGradUpit.setInt(3, grad.getBrojStanovnika());
            dodajGradUpit.setInt(4, grad.getDrzava().getId());
            dodajGradUpit.executeUpdate();
        } catch (SQLException e) {
            //  e.printStackTrace();
        }
    }

    void dodajDrzavu(Drzava drzava) {
        try {
            ResultSet rs = vratiMaxIDDrzavaUpit.executeQuery();
            int id = 1;
            if (rs.next()) id = rs.getInt(1) + 1;
            //drzava.setId(id);
            dodajDrzavuUpit.setInt(1, id);
            dodajDrzavuUpit.setString(2, drzava.getNaziv());
            dodajDrzavuUpit.setInt(3, drzava.getGlavniGrad().getId());
            dodajDrzavuUpit.executeUpdate();
        } catch (SQLException e) {
          // e.printStackTrace();
        }

    }


    void izmijeniGrad(Grad grad) {
        try {
            izmijeniGradUpit.setString(1, grad.getNaziv());
            izmijeniGradUpit.setInt(2, grad.getBrojStanovnika());
            izmijeniGradUpit.setInt(3, grad.getDrzava().getId());
            izmijeniGradUpit.setInt(4, grad.getId());
            izmijeniGradUpit.execute();
        } catch (SQLException e) {
            //  e.printStackTrace();
        }
    }

    Drzava nadjiDrzavu(String drzava) {
        Drzava vrati = new Drzava();
        try {
            nadjiDrzavuUpit.setString(1, drzava);
            ResultSet rs = nadjiDrzavuUpit.executeQuery();
            if (!rs.next()) return null;

            vrati.setId(rs.getInt(1));
            vrati.setNaziv(rs.getString(2));
            postaviGrad(vrati);
        } catch (SQLException e) {
            //  e.printStackTrace();
        }
        return vrati;
    }

    private void postaviGrad(Drzava drzava) {
        Grad grad = new Grad();
        try {
            gradUpit.setInt(1, drzava.getId());
            ResultSet rs = gradUpit.executeQuery();
            grad.setId(rs.getInt(1));
            grad.setNaziv(rs.getString(2));
            grad.setBrojStanovnika(rs.getInt(3));
            grad.setDrzava(drzava);
            drzava.setGlavniGrad(grad);
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }


    // Metoda za potrebe testova, vraća bazu u polazno stanje
    public void vratiBazuNaDefault() {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM grad");
            stmt.executeUpdate("DELETE FROM drzava");
            regenerisiBazu();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Grad nadjiGrad(String nazivGrada) {
        Grad grad = new Grad();
        try {
            trenutniGradUpit.setString(1, nazivGrada);
            ResultSet rs = trenutniGradUpit.executeQuery();
            if (rs.next()) {
                grad.setId(rs.getInt(1));
                grad.setNaziv(rs.getString(2));
                grad.setBrojStanovnika(rs.getInt(3));
                vratiDrzavu(grad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grad;
    }

    public ArrayList<Drzava> drzave() {
        ArrayList<Drzava> vrati = new ArrayList<>();
        try {
            ResultSet rs = vratiSveDrzaveIzBazeUpit.executeQuery();
            while (rs.next()) {
                Drzava drzava = new Drzava();
                drzava.setId(rs.getInt(1));
                drzava.setNaziv(rs.getString(2));
                glavniGrad(drzava.getNaziv());
                vrati.add(drzava);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vrati;
    }


    public int IDGrada() {
        int id = 1;
        try {
            ResultSet rs = vratiMaxIDGradUpit.executeQuery();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return id;
    }

    public int IDDrzave() {
        int id = 1;
        try {
            ResultSet rs = vratiMaxIDDrzavaUpit.executeQuery();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return id;
    }

   /* public int vratiStariIDGrada(String naziv) {
        try {
            trenutniIDGradaUpit.setString(1, naziv);
            ResultSet rs= trenutniIDGradaUpit.executeQuery();
            if(rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }*/


}