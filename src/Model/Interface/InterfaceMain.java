package Model.Interface;

import Model.Customer;
import Model.Transaksi;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface InterfaceMain {
    void insertTransaksi(Transaksi a);
    int getLastID();
    ArrayList loadDataTransaksi();
    void updateTransaksi(String no);
    ResultSet searchByID(String find);
    int getLastIdCust();
    void UpdateTransaksi(Transaksi updateTransaksi);
    void UpdateCustomer(Customer updateCustomer);
    void insertCust(Customer c);
    void deleteTransaksi(String noTransaksi);
    ResultSet ambilData(String IdTransaksi);
    
}
