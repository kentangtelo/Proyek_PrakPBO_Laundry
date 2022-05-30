package Model;


import Controller.HomeController;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model.Interface.InterfaceMain;



public class MainModel implements InterfaceMain {
    static ArrayList<Transaksi> dafTransaksi = new ArrayList();
    private Connection con;
    Koneksi conn;
    
    public MainModel() {
        conn = new Koneksi();
        conn.connect();
    }
    @Override
    public int getLastID(){
        con = conn.getKoneksi();
        ResultSet rs;
        try {
            String query = "SELECT COUNT(no_transaksi) AS lastID FROM transaksi";
            Statement s = con.createStatement();
            rs = s.executeQuery(query);
            if (rs.next())
                return rs.getInt("lastID");
        } catch (SQLException ex) {
            Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return 0;
    }
    public ArrayList<Customer> loadDataPelanggan(String find) {
        ArrayList<Customer> dafPelanggan = new ArrayList();
        con = conn.getKoneksi();
        ResultSet rs;
        try {
            String query = "SELECT * FROM customer WHERE nama = '"+find+"' ORDER BY no DESC";
            Statement s = con.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString(2);
                String nama = rs.getString(3);
                String alamat = rs.getString(4);
                String noTelp = rs.getString(5);
                String jenisKelamin = rs.getString(6);
                System.out.println(id);
                Customer c = new Customer(nama,alamat,noTelp,jenisKelamin,id);
                dafPelanggan.add(c);
            }
            System.out.println("DATA LOADED");
            return dafPelanggan;
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).
                    log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void insertTransaksi(Transaksi a){
        int no = getLastID()+1;
        try {
            String query = "INSERT INTO transaksi VALUES ('"+no+"',"
                    + "'"+a.getNoTransaksi()+"','"+a.getIdCust()+"','"+a.getLayanan()+"'"
                    + ",'"+a.getStatus()+"','"+a.getTanggal()+"','"+a.getBerat()+"','"+a.getTotal()+"')";
            Statement s = con.createStatement();
            s.execute(query);
        } catch(SQLException se){
            System.out.println(se);
        }
    }
    @Override
    public ArrayList loadDataTransaksi() {
        ArrayList result = new ArrayList();
        ArrayList<Transaksi> dafTransaksi = new ArrayList();
        ArrayList<Orang> dafPelanggan = new ArrayList();
        con = conn.getKoneksi();
        ResultSet rs;
        try {
            String query = "SELECT no_transaksi,id_customer,nama,alamat,no_hp,"
                    + "kelamin,layanan,status,tanggal_transaksi,berat,"
                    + "total FROM customer JOIN transaksi USING (id_customer) "
                    + "ORDER BY transaksi.no desc";
            Statement s = con.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String noTransaksi = rs.getString(1);
                String idCust = rs.getString(2);
                String nama = rs.getString(3);
                String alamat = rs.getString(4);
                String noTelp = rs.getString(5);
                String jenisKelamin = rs.getString(6);
                String layanan = rs.getString(7);
                String status = rs.getString(8);
                String tanggal = rs.getString(9);
                Double berat = rs.getDouble(10);
                Double total = rs.getDouble(11);
                Transaksi a = new Transaksi(noTransaksi,idCust,layanan,status,tanggal,berat,total);
                Customer p = new Customer(nama,alamat,noTelp,jenisKelamin,idCust);
                dafPelanggan.add(p);
                dafTransaksi.add(a);
            }
            result.add(dafTransaksi);
            result.add(dafPelanggan);
            System.out.println("DATA LOADED");
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(MainModel.class.getName()).
                    log(Level.SEVERE, null, ex);
            return null;
        }
    }
    @Override
    public void updateTransaksi(String no) {
        try {
            String query = "UPDATE transaksi SET status = 'Lunas' WHERE no_transaksi = '"+no+"'";
            Statement s = con.createStatement();
            s.execute(query);
        } catch(SQLException se){
            System.out.println(se);
        }
    }
    @Override
    public ResultSet searchByID(String find) {
        con = conn.getKoneksi();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM customer WHERE id_customer = '"+find+"'";
            Statement s = con.createStatement();
            rs = s.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);
            return rs;
        }
    }
    @Override
    public int getLastIdCust() {
        con = conn.getKoneksi();
        ResultSet rs;
        try {
            String query = "SELECT COUNT(id_customer) AS lastID FROM customer";
            Statement s = con.createStatement();
            rs = s.executeQuery(query);
            if (rs.next())
                return rs.getInt("lastID");
        } catch (SQLException ex) {
            Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return 0;
    }
    @Override
    public void UpdateTransaksi(Transaksi updateTransaksi){
        String no = updateTransaksi.getNoTransaksi();
        try{
            String query = "UPDATE transaksi SET layanan='"+updateTransaksi.getLayanan()+"',status='"+updateTransaksi.getStatus()+"'" +
                    ",tanggal_transaksi='"+updateTransaksi.getTanggal()+"'" +
                    ",berat='"+updateTransaksi.getBerat()+"'" +
                    ",total='"+updateTransaksi.getTotal()+"'WHERE transaksi.no_transaksi='"+no+"' ";
            Statement s = con.createStatement();
            s.execute(query);
        }catch(SQLException se){
            System.out.println(se);
        }
    }
    @Override
    public void UpdateCustomer(Customer updateCustomer){
        try{
            String query = "UPDATE customer SET nama='"+updateCustomer.getNama()+"'" +
                    ",alamat='"+updateCustomer.getAlamat()+"',no_hp='"+updateCustomer.getNoTelp()+"'" +
                    ",kelamin='"+updateCustomer.getJenisKelamin()+"' WHERE customer.id_customer='"+updateCustomer.getIdCust()+"' ";
            Statement s = con.createStatement();
            s.execute(query);
        }catch(SQLException se){
            System.out.println(se);
        }
    }
    @Override
    public void insertCust(Customer c) {
        int no = getLastIdCust()+1;
        try {
            String query = "INSERT INTO customer VALUES ('"+no+"','"+c.getIdCust()+"',"
                    + "'"+c.getNama()+"','"+c.getAlamat()+"','"+c.getNoTelp()+"',"
                    + "'"+c.getJenisKelamin()+"')";
            Statement s = con.createStatement();
            s.execute(query);
        } catch(SQLException se){
            System.out.println(se);
        }
    }

    @Override
    public void deleteTransaksi(String noTransaksi) {
        String no = noTransaksi;
        try {
            String query = "DELETE FROM transaksi WHERE no_transaksi = '"+no+"'";
            Statement s = con.createStatement();
            s.execute(query);
        } catch(SQLException se){
            System.out.println(se);
        }
    }

    @Override
    public ResultSet ambilData(String IdTransaksi) {
        con = conn.getKoneksi();
        ResultSet rs = null;
        try {
            String query = "SELECT no_transaksi,transaksi.id_customer,nama,alamat,no_hp,kelamin,layanan,status,tanggal_transaksi,berat,total " +
                    "FROM customer JOIN transaksi " +
                    "WHERE customer.id_customer = transaksi.id_customer AND transaksi.no_transaksi = '"+IdTransaksi+"'";
            Statement s = con.createStatement();
            rs = s.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);
            return rs;
        }
    }
    public ArrayList<Admin> loadDataAdmin() {
        ArrayList<Admin> dafAdmin = new ArrayList();
        con = conn.getKoneksi();
        ResultSet rs;
        try {
            String query = "SELECT * FROM admin ORDER BY id";
            Statement s = con.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String username = rs.getString(2);
                String password = rs.getString(3);
                String nama = rs.getString(4);
                Admin a = new Admin(nama,username,password,dafTransaksi);
                dafAdmin.add(a);
            }
            System.out.println("DATA LOADED");
            return dafAdmin;
        } catch (SQLException ex) {
            Logger.getLogger(MainModel.class.getName()).
                    log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void insertAdmin(Admin a) {
        try {
            String query = "INSERT INTO admin VALUES ('0','"+a.getUsername()+"', "
                    + "'"+a.getPassword()+"', '"+a.getNama()+"')";
            Statement s = con.createStatement();
            s.execute(query);
        } catch(SQLException se) {
            System.out.println(se);
        }
    }
    
    public ResultSet usernameExist(String username) {
        con = conn.getKoneksi();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM admin "
                    + "WHERE username = '"+username+"'";
            Statement s = con.createStatement();
            rs = s.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);
            return rs;
        }
    }
    public ResultSet loadDataPelanggan2() {
        con = conn.getKoneksi();
        ResultSet rs;
        try {
            String query = "SELECT id_customer,nama,alamat,no_hp,"
                    + "kelamin FROM customer";
            Statement s = con.createStatement();
            rs = s.executeQuery(query);
            System.out.println("DATA LOADED");
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(MainModel.class.getName()).
                    log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void deleteAdmin(String usernameAdmin) {
        String username = usernameAdmin;
        try {
            String query = "DELETE FROM admin WHERE username = '"+username+"'";
            Statement s = con.createStatement();
            s.execute(query);
        } catch(SQLException se){
            System.out.println(se);
        }
    }

    public void deletePelanggan(String idPelanggan) {
        String no = idPelanggan;
        try {
            String query = "DELETE FROM customer WHERE id_customer = '"+no+"'";
            Statement s = con.createStatement();
            s.execute(query);
        } catch(SQLException se){
            System.out.println(se);
        }
    }
}
