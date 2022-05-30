package Controller;

import Model.Admin;
import Model.Customer;
import Model.MainModel;
import Model.Orang;
import Model.Transaksi;
import View.AddAdminView;
import View.CustomerUpdateView;
import View.DataLaundryView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/** @author faizaaulia */

public class DataLaundryController extends MouseAdapter implements ActionListener {
    DataLaundryView view;
    MainModel model;
    AddAdminView addView;
    CustomerUpdateView updatePel;
    ArrayList<Transaksi> dafTransaksi = new ArrayList();
    
    public DataLaundryController() {
        view = new DataLaundryView();
        model = new MainModel();
        showDataAdmin();
        showDataPelanggan();
        showDataTransaksi();
        view.addActionListener(this);
        view.setVisible(true);
    }
    
    public void addAdmin() {
        String username = addView.getTfUsername();
        String password = addView.getTfPassword();
        String nama = addView.getTfNama();
        boolean userExist = false;
        try {
            userExist = model.usernameExist(username).next();
        } catch (SQLException ex) {
            Logger.getLogger(DataLaundryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (username.equals("") || password.equals("") || nama.equals(""))
            JOptionPane.showMessageDialog(addView, "Lengkapi data",
                "Error", JOptionPane.WARNING_MESSAGE);
        else {
            if (username.length() < 5 || password.length() < 5)
                JOptionPane.showMessageDialog(addView, "Username atau password invalid! (min. 5)", 
                    "Error", JOptionPane.WARNING_MESSAGE);
            else if (nama.length() < 3)
                JOptionPane.showMessageDialog(addView, "Nama invalid!", 
                    "Error", JOptionPane.WARNING_MESSAGE);
            else {
                if (userExist)
                    JOptionPane.showMessageDialog(addView, "Username sudah ada!", 
                    "Error", JOptionPane.WARNING_MESSAGE);
                else {
                    Admin a = new Admin(nama,username,password,dafTransaksi);
                    model.insertAdmin(a);
                    JOptionPane.showMessageDialog(addView, "Berhasil menambahkan " + nama, 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    addView.dispose();
                    resetForm();
                    showDataAdmin();
                }
            }
        }
    }
    
    private void showDataAdmin() {
        ArrayList<Admin> dafAdmin = model.loadDataAdmin();
        String kolom[] = {"No.", "Username", "Nama"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
              }
        };
        for (int i = 0; i < dafAdmin.size(); i++) {
            String no = Integer.toString((i+1));
            String username = dafAdmin.get(i).getUsername();
            String nama = dafAdmin.get(i).getNama();
            String data[] = {no,username,nama};
            dtm.addRow(data);
        }
        view.getTableAdmin().setModel(dtm);
    }
    
    private void showDataPelanggan() {
        ResultSet rs = model.loadDataPelanggan2();
        String kolom[] = {"ID Pelanggan","Nama", "Alamat", "No Telp", "Jenis Kelamin"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
              }
        };
        try {
            while (rs.next()) {
                String id = rs.getString(1);
                String nama = rs.getString(2);
                String alamat = rs.getString(3);
                String no = rs.getString(4);
                String jk = rs.getString(5);
                
                String data[] = {id,nama,alamat,no,jk};
                dtm.addRow(data);
            }
            view.getTablePelanggan().setModel(dtm);
        } catch (SQLException ex) {
            Logger.getLogger(DataLaundryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void showDataTransaksi() {
        ArrayList result = model.loadDataTransaksi();
        String kolom[] = {"No Transaksi","ID Pelannga", "Nama", "Alamat", "No Telp", 
            "Jenis Kelamin", "Layanan", "Status", "Tanggal", "Berat (Kg)", "Total"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
              }
        };
        ArrayList<Transaksi> transaksi = (ArrayList<Transaksi>) result.get(0);
        ArrayList<Orang> person = (ArrayList<Orang>) result.get(1);
        for (int i = 0; i < transaksi.size(); i++) {
            String noTransaksi = transaksi.get(i).getNoTransaksi();
            String idPelanggan = transaksi.get(i).getIdCust();
            String nama = person.get(i).getNama();
            String alamat = person.get(i).getAlamat();
            String noTelp = person.get(i).getNoTelp();
            String JenisKelamin = person.get(i).getJenisKelamin();
            String Layanan = transaksi.get(i).getLayanan();
            String Status = transaksi.get(i).getStatus();
            String Tanggal = transaksi.get(i).getTanggal();
            String Berat = Double.toString(transaksi.get(i).getBerat());
            String Total = Double.toString(transaksi.get(i).getTotal());
            
            String data[] = {noTransaksi,idPelanggan,nama,alamat,noTelp,JenisKelamin,
                Layanan,Status,Tanggal,Berat,Total};
            dtm.addRow(data);
        }
        view.getTableTransaksi().setModel(dtm);
    }
    public void deletePelanggan(){
        int baris = view.getTablePelanggan().getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data pelanggan terlebih dahulu ",
                "Error", JOptionPane.WARNING_MESSAGE);
        }else{
            String idPelanggan = view.getTablePelanggan().getValueAt(baris, 0).toString();
            model.deletePelanggan(idPelanggan);
            JOptionPane.showMessageDialog(view, "Berhasil delete pelanggan " + idPelanggan, 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            showDataTransaksi();
        }
    }
    public void deleteTransaksi(){
        int baris = view.getTableTransaksi().getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data transaksi terlebih dahulu ",
                "Error", JOptionPane.WARNING_MESSAGE);
        }else{
            String noTransaksi = view.getTableTransaksi().getValueAt(baris, 0).toString();
            model.deleteTransaksi(noTransaksi);
            JOptionPane.showMessageDialog(view, "Berhasil delete pelanggan " + noTransaksi, 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            showDataTransaksi();
        }
    }

    public void resetForm() {
        addView.setTfNama("");
        addView.setTfUsername("");
        addView.setTfPassword("");
    }
   
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        
        if (source.equals(view.getBtnTambah())) {
            addView = new AddAdminView();
            addView.addActionListener(this);
            addView.setVisible(true);
        }else if(source.equals(view.getBtnDeletePelanggan())){
            deletePelanggan();
            showDataPelanggan();
        }else if(source.equals(view.getBtnTambahPelanggan())){
            new CustomerBaruController();
            view.dispose();
        }else if(source.equals(view.getBtnUpdatePelanggan())){
            int baris = view.getTablePelanggan().getSelectedRow();
            if (baris == -1) {
                JOptionPane.showMessageDialog(view, "Pilih data pelanggan terlebih dahulu ",
                    "Error", JOptionPane.WARNING_MESSAGE);
            }else{
                updatePel = new CustomerUpdateView();
                updatePel.addActionListener(this);
                updatePel.setVisible(true);
                String id = view.getTablePelanggan().getValueAt(baris, 0).toString();
                String nama = view.getTablePelanggan().getValueAt(baris, 1).toString();
                String alamat = view.getTablePelanggan().getValueAt(baris, 2).toString();
                String noTelp = view.getTablePelanggan().getValueAt(baris, 3).toString();
                String jenisKelamin = view.getTablePelanggan().getValueAt(baris, 4).toString();
                
                updatePel.setTfNama(nama);
                updatePel.setLabelIdCust(id);
                updatePel.setTfAlamat(alamat);
                updatePel.setTfNoHp(noTelp);
                if(jenisKelamin.equals("Laki - Laki")){
                    updatePel.getRadioLk().setSelected(true);
                }else
                    updatePel.getRadioPr().setSelected(true);
            }
        }else if(source.equals(view.getBtnTambahTransaksi())){
            String noTransaksi = "";
            String id = "";
            String nama = "";
            String alamat = "";
            String noTelp = "";
            String jenisKelamin = "";
            String layanan = "";
            String status = "";
            String berat = "";
            String total = "";
            new MenuTransaksiController(noTransaksi,id,nama,alamat,noTelp,jenisKelamin,layanan,status,berat,total);
            view.dispose();
        }else if(source.equals(view.getBtnDeleteTransaksi())){
            deleteTransaksi();
            showDataPelanggan();
            showDataTransaksi();
        }else if(source.equals(view.getBtnUpdateTransaksi())){
            int baris = view.getTableTransaksi().getSelectedRow();
            if (baris == -1) {
                JOptionPane.showMessageDialog(view, "Pilih data transaksi terlebih dahulu ",
                    "Error", JOptionPane.WARNING_MESSAGE);
            }else{
                String noTransaksi = view.getTableTransaksi().getValueAt(baris, 0).toString();
                String id = view.getTableTransaksi().getValueAt(baris, 1).toString();
                String nama = view.getTableTransaksi().getValueAt(baris, 2).toString();
                String alamat = view.getTableTransaksi().getValueAt(baris, 3).toString();
                String noTelp = view.getTableTransaksi().getValueAt(baris, 4).toString();
                String jenisKelamin = view.getTableTransaksi().getValueAt(baris, 5).toString();
                String layanan = view.getTableTransaksi().getValueAt(baris, 6).toString();
                String status = view.getTableTransaksi().getValueAt(baris, 7).toString();
                String berat = view.getTableTransaksi().getValueAt(baris, 9).toString();
                String total = view.getTableTransaksi().getValueAt(baris, 10).toString();
                new MenuTransaksiController(noTransaksi,id,nama,alamat,noTelp,jenisKelamin,layanan,status,berat,total);
                view.dispose();
            }
        } else if (source.equals(view.getBtnKembali())) {
            view.dispose();
            new HomeController();
        }else if (source.equals(addView.getBtnSimpanAdmin())) {
            addAdmin();
        } else if (source.equals(addView.getBtnResetAdmin())){ 
            resetForm();
        }else if (source.equals(addView.getBtnBack())){
            addView.dispose();
        }else if(source.equals(updatePel.getBtnUpdatePelanggan())){
            String id = updatePel.getLabelIdCust();
            String nama = updatePel.getTfNama();
            String alamat = updatePel.getTfAlamat();
            String noTelp = updatePel.getTfNoHp();
            String jenisKelamin = updatePel.getBgJK().getSelection().getActionCommand();
            Customer updateCustomer = new Customer(nama,alamat,noTelp,jenisKelamin,id);
            model.UpdateCustomer(updateCustomer);
            JOptionPane.showMessageDialog(view, "Berhasil update pelanggan",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            updatePel.dispose();
            showDataPelanggan();
            showDataTransaksi();
        }else if(source.equals(updatePel.getBtnKembali())){
            updatePel.dispose();
        } 
    }
}