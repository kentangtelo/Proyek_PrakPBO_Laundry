package Model;

import java.util.ArrayList;

/** @author faizaaulia */

public class Admin {

    private String username,password,nama;
    private ArrayList<Transaksi> transaksi;

    public Admin(String nama, String username, String password, ArrayList<Transaksi> dafTransaksi) {
        this.username = username;
        this.password = password;
        this.transaksi = transaksi;
        this.nama = nama;
    }

    public ArrayList<Transaksi> getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(ArrayList<Transaksi> transaksi) {
        this.transaksi = transaksi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
    
}
