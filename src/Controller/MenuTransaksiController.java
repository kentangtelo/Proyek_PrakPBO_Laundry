/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Customer;
import Model.MainModel;
import Model.Orang;
import Model.Transaksi;
import View.CariPelangganView;
import View.MenuTransaksiView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MenuTransaksiController extends MouseAdapter implements ActionListener {
    MenuTransaksiView view;
    MainModel model;
    Transaksi b;
    private String idPel;
    CariPelangganView cariPelangganView;
    
    public MenuTransaksiController(String noTransaksi, String id, String nama, String alamat, String noTelp, String jenisKelamin, String layanan, String status, String berat, String total) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYY");
        Date date = new Date();
        String tgl = dateFormat.format(date);
        view = new MenuTransaksiView();
        model = new MainModel();
        view.addActionListener(this);
        view.setVisible(true);
        view.setLabelTanggal(tgl);
        view.setLabelIdCust(id);
        view.setLabelIdTransaksi(noTransaksi);
        view.setTfNama(nama);
        view.setTfAlamat(alamat);
        view.setTfNoHp(noTelp);
        if(jenisKelamin.equals("Laki - Laki")){
            view.getRadioLk().setSelected(true);
        }else if(jenisKelamin.equals("Perempuan")){
            view.getRadioPr().setSelected(true);
        }else{
            view.getRadioLk().setSelected(false);
            view.getRadioPr().setSelected(false);
        }
        switch (layanan) {
            case "Requler":
                view.getCbLayanan().setSelectedIndex(0);
                break;
            case "Oneday":
                view.getCbLayanan().setSelectedIndex(1);
                break;
            case "Sameday":
                view.getCbLayanan().setSelectedIndex(2);
                break;
            case "Express":
                view.getCbLayanan().setSelectedIndex(3);
                break;
            default:
                break;
        }
       if(status.equals("Belum Lunas"))
           view.getCbBayar().setSelectedIndex(0);
       else if(status.equals("Lunas"))
           view.getCbBayar().setSelectedIndex(1);
       view.setTfBerat(berat);
       view.setTfTotal(total);
        showDataTransaksi();
    }
    
    public void resetForm() {
        view.setTfNama("");
        view.setTfAlamat("");
        view.setTfNoHp("");
        view.getBgJK().clearSelection();
        view.setTfBerat("");
        view.getCbLayanan().setSelectedIndex(0);
        view.setTfTotal("");
        view.getCbBayar().setSelectedIndex(0);
        view.setLabelIdCust("null");
        view.setLabelIdTransaksi("null");
    }

    public Transaksi cek(Transaksi b,String idPel) {
        String nama = view.getTfNama();
        String alamat = view.getTfAlamat();
        String noTelp = view.getTfNoHp();
        boolean bgNotNull = view.getRadioLk().isSelected() || view.getRadioPr().isSelected();
        String berat = view.getTfBerat();
        String layanan = view.getCbLayanan().getSelectedItem().toString();
        String pembayaran = view.getCbBayar().getSelectedItem().toString();
        double total=0;
        int inLayanan = view.getCbLayanan().getSelectedIndex();
        if (nama.equals("") || alamat.equals("") || noTelp.equals("") || berat.equals("") || !bgNotNull) {
            JOptionPane.showMessageDialog(view, "Lengkapi data", "Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        else {
            if (nama.length() < 3) {
                JOptionPane.showMessageDialog(view, "Nama invalid!", 
                    "Error", JOptionPane.WARNING_MESSAGE); 
                return null;
            }
            else if (alamat.length() < 3) {
                JOptionPane.showMessageDialog(view, "Alamat invalid!",
                    "Error", JOptionPane.WARNING_MESSAGE); 
                return null;
            }
            else if (noTelp.length() < 12 && noTelp.length() > 12) {
                JOptionPane.showMessageDialog(view, "No. Telp. invalid!", 
                    "Error", JOptionPane.WARNING_MESSAGE); 
                return null;
            }
            else {
                double beratt = Double.parseDouble(berat);
                switch (inLayanan){
                    case 0 : total = beratt*6000; break;
                    case 1 : total = beratt*8000; break;
                    case 2 : total = beratt*10000; break;
                    case 3 : total = beratt*12000; break;
                }
                view.setTfTotal(Double.toString(total));
                String noTransaksi = "TR00" + (model.getLastID()+1);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String tglOrder = dateFormat.format(date);
                String idCust = idPel;
                return b = new Transaksi(noTransaksi,idCust,layanan,pembayaran,tglOrder,beratt,total);
            }
        }
    }
    
    private void showDataTransaksi() {
        ArrayList result = model.loadDataTransaksi();
        String kolom[] = {"No Transaksi","ID Customer", "Nama", "Alamat", "No Telp", 
            "Jenis Kelamin", "Layanan", "Status", "Tanggal", "Berat(Kg)", "Total"};
        DefaultTableModel dtm = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
              }
        };
        ArrayList<Transaksi> transaksi = (ArrayList<Transaksi>) result.get(0);
        ArrayList<Orang> person = (ArrayList<Orang>) result.get(1);
        for (int i = 0; i < transaksi.size(); i++) {
            String no = Integer.toString((i+1));
            String noTransaksi = transaksi.get(i).getNoTransaksi();
            String IDCustomer = transaksi.get(i).getIdCust();
            String nama = person.get(i).getNama();
            String alamat = person.get(i).getAlamat();
            String noTelp = person.get(i).getNoTelp();
            String JenisKelamin = person.get(i).getJenisKelamin();
            String Layanan = transaksi.get(i).getLayanan();
            String Status = transaksi.get(i).getStatus();
            String Tanggal = transaksi.get(i).getTanggal();
            String Berat = Double.toString(transaksi.get(i).getBerat());
            String Total = Double.toString(transaksi.get(i).getTotal());
            
            String data[] = {noTransaksi,IDCustomer,nama,alamat,noTelp,JenisKelamin,
                Layanan,Status,Tanggal,Berat,Total};
            dtm.addRow(data);
        }
        view.getTableTransaksi().setModel(dtm);
    }
    public void updateData(){
        String nama = view.getTfNama();
        String alamat = view.getTfAlamat();
        String noTelp = view.getTfNoHp();
        String jenisKelamin = view.getBgJK().getSelection().getActionCommand();
        String berat = view.getTfBerat();
        String layanan = view.getCbLayanan().getSelectedItem().toString();
        String pembayaran = view.getCbBayar().getSelectedItem().toString();
        double total = view.getTfTotal();
        String labelId = view.getLabelIdCust();
        String labelIdTransaksi = view.getLabelIdTransaksi();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String tanggal = dateFormat.format(date);
        double beratt = Double.parseDouble(berat);
        Customer updateCustomer = new Customer(nama,alamat,noTelp,jenisKelamin,labelId);
        Transaksi updateTransaksi = new Transaksi(labelIdTransaksi,labelId,layanan,pembayaran,tanggal,beratt,total);
        model.UpdateCustomer(updateCustomer);
        model.UpdateTransaksi(updateTransaksi);
        JOptionPane.showMessageDialog(view, "Berhasil update ke transaksi",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }
    public void ambilData(){
        int baris = view.getTableTransaksi().getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data transaksi terlebih dahulu ",
                "Error", JOptionPane.WARNING_MESSAGE);
        }else {
            try {
                String IdTransaksi = view.getTableTransaksi().getValueAt(baris, 0).toString();
                ResultSet result = model.ambilData(IdTransaksi);
                if(result.next()){
                    view.setLabelIdCust(result.getString(2));
                    idPel = result.getString(2);
                    view.setLabelIdTransaksi(result.getString(1));
                    view.setTfNama(result.getString(3));
                    view.setTfAlamat(result.getString(4));
                    view.setTfNoHp(result.getString(5));
                    if (result.getString(6).equals("Laki - Laki"))
                        view.getRadioLk().setSelected(true);
                    else
                        view.getRadioPr().setSelected(true);
                    switch (result.getString(7)) {
                        case "Requler":
                            view.getCbLayanan().setSelectedIndex(0);
                            break;
                        case "Oneday":
                            view.getCbLayanan().setSelectedIndex(1);
                            break;
                        case "Sameday":
                            view.getCbLayanan().setSelectedIndex(2);
                            break;
                        case "Express":
                            view.getCbLayanan().setSelectedIndex(3);
                            break;
                        default:
                            break;
                    }
                    view.setTfBerat(result.getString(10));
                    view.setTfTotal(result.getString(11));
                    if(result.getString(8).equals("Belum Lunas"))
                        view.getCbBayar().setSelectedIndex(0);
                    else 
                        view.getCbBayar().setSelectedIndex(1);
                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(MenuTransaksiController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void updateTransaksi() {
        int baris = view.getTableTransaksi().getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data transaksi terlebih dahulu ",
                "Error", JOptionPane.WARNING_MESSAGE);
        } else {
                String no = view.getTableTransaksi().getValueAt(baris, 0).toString();
                String status = view.getTableTransaksi().getValueAt(baris, 7).toString();
                if (status.equals("Belum Lunas")) {
                    model.updateTransaksi(no);
                    JOptionPane.showMessageDialog(view, "Berhasil mengupdate transaksi " + no, 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    showDataTransaksi();
                } else {
                    JOptionPane.showMessageDialog(view, "Transaksi sudah diselesaikan",
                        "Error", JOptionPane.WARNING_MESSAGE);
                }
                view.getTableTransaksi().getSelectionModel().clearSelection();
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
            JOptionPane.showMessageDialog(view, "Berhasil delete transaksi " + noTransaksi, 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            showDataTransaksi();
        }
    }
    public void searchCust() {
        String searchBy = view.gettfCariPelanggan();
        int idx = view.getComboCari().getSelectedIndex();
        switch (idx) {
            case 0 : {
                if (searchBy.equals(""))
                    JOptionPane.showMessageDialog(view, "Masukan invalid!",
                        "Error", JOptionPane.WARNING_MESSAGE);
                else {
                    ArrayList<Customer> dafPelanggan = model.loadDataPelanggan(searchBy);
                    switch (dafPelanggan.size()) {
                        case 0:
                            JOptionPane.showMessageDialog(view, "Pelanggan " + searchBy + " tidak ada!",
                                    "Error", JOptionPane.WARNING_MESSAGE);
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(view, "Menambahkan pelanggan ke transaksi",
                                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            view.setLabelIdCust(dafPelanggan.get(0).getIdCust());
                            System.out.println(view.getLabelIdCust());
                            idPel = dafPelanggan.get(0).getIdCust();
                            view.setTfNama(dafPelanggan.get(0).getNama());
                            view.setTfAlamat(dafPelanggan.get(0).getAlamat());
                            view.setTfNoHp(dafPelanggan.get(0).getNoTelp());
                            if (dafPelanggan.get(0).getJenisKelamin().equals("Laki - Laki"))
                                view.getRadioLk().setSelected(true);
                            else
                                view.getRadioPr().setSelected(true);
                            break;
                        default:
                            cariPelangganView = new CariPelangganView();
                            cariPelangganView.setVisible(true);
                            cariPelangganView.addActionListenter(this);
                            String kolom[] = {"No.","ID Cust", "Nama", "Alamat", "No Telp", "Jenis Kelamin"};
                            DefaultTableModel dtm = new DefaultTableModel(null, kolom) {
                                @Override
                                public boolean isCellEditable(int rowIndex, int mColIndex) {
                                    return false;
                                }
                            };  for (int i = 0; i < dafPelanggan.size(); i++) {
                                String no = Integer.toString((i+1));
                                String id = dafPelanggan.get(i).getIdCust();
                                String nama = dafPelanggan.get(i).getNama();
                                String alamat = dafPelanggan.get(i).getAlamat();
                                String noTelp = dafPelanggan.get(i).getNoTelp();
                                String JenisKelamin = dafPelanggan.get(i).getJenisKelamin();
                                
                                String data[] = {no,id,nama,alamat,noTelp,JenisKelamin};
                                dtm.addRow(data);
                            }   cariPelangganView.getTableCariPelanggan().setModel(dtm);
                            break;
                    }
                }
                break;
            }
            case 1 : {
                if (searchBy.equals(""))
                    JOptionPane.showMessageDialog(view, "Masukan invalid!",
                        "Error", JOptionPane.WARNING_MESSAGE);
                else {
                    ResultSet rs = model.searchByID(searchBy);
                    try {
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(view, "Menambahkan pelanggan ke transaksi", 
                                "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            view.setLabelIdCust(rs.getString(2));
                            idPel = rs.getString(2);
                            view.setTfNama(rs.getString(3));
                            view.setTfAlamat(rs.getString(4));
                            view.setTfNoHp(rs.getString(5));
                            if (rs.getString(6).equals("Laki - Laki"))
                                view.getRadioLk().setSelected(true);
                            else
                                view.getRadioPr().setSelected(true);
                        } else
                            JOptionPane.showMessageDialog(view, "Pelanggan ID " + searchBy + " tidak ada!",
                                "Error", JOptionPane.WARNING_MESSAGE);
                    } catch (SQLException ex) {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source.equals(view.getBtnHitung())) {
            cek(b,idPel);
        } else if (source.equals(view.getBtnTambah())) {
            if (cek(b,idPel) != null) {
                if (view.getLabelIdCust().equals("null")) {
                    String nama = view.getTfNama();
                    String alamat = view.getTfAlamat();
                    String no = view.getTfNoHp();
                    String jenisKelamin = view.getBgJK().getSelection().getActionCommand();
                    String id = "CS00" + (model.getLastIdCust()+1);
                    Customer c = new Customer(nama,alamat,no,jenisKelamin,id);
                    model.insertCust(c);
                    view.setLabelIdCust(id);
                }
                idPel = view.getLabelIdCust();
                model.insertTransaksi(cek(b,idPel));
                JOptionPane.showMessageDialog(view, "Berhasil menambahkan transaksi baru", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                showDataTransaksi();
            }
        } else if (source.equals(view.getBtnUpdate())) {
            updateTransaksi();
        } else if (source.equals(view.getBtnReset())) {
            resetForm();
        } else if (source.equals(view.getBtnRefresh())) {
            showDataTransaksi();
        }else if(source.equals(view.getBtnDelete())){
            deleteTransaksi();
        } else if(source.equals(view.getBtnUpdateData())){
            if (cek(b,idPel) != null) {
                updateData();
                showDataTransaksi();
            }
        }else if(source.equals(view.getBtnAmbil())){
            ambilData();
        }else if(source.equals(view.getBtnKembali())){
            new HomeController();
            view.dispose();
        }else if (source.equals(view.getBtnCariPelanggan())) {
            searchCust();
        }else if (source.equals(cariPelangganView.getBtnBackCariPelanggan())) {
            cariPelangganView.dispose();
        } else if (source.equals(cariPelangganView.getBtnAdd())) {
            int baris = cariPelangganView.getTableCariPelanggan().getSelectedRow();
            if (baris == -1) {
                JOptionPane.showMessageDialog(cariPelangganView, "Pilih pelanggan terlebih dahulu!",
                        "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                view.setLabelIdCust(cariPelangganView.getTableCariPelanggan().
                        getValueAt(baris, 1).toString());
                idPel = view.getLabelIdCust();
                view.setTfNama(cariPelangganView.getTableCariPelanggan().
                        getValueAt(baris, 2).toString());
                view.setTfAlamat(cariPelangganView.getTableCariPelanggan().
                        getValueAt(baris, 3).toString());
                view.setTfNoHp(cariPelangganView.getTableCariPelanggan().
                        getValueAt(baris, 4).toString());
                if (cariPelangganView.getTableCariPelanggan().getValueAt(baris, 5).
                        toString().equals("Laki - Laki"))
                    view.getRadioLk().setSelected(true);
                else
                    view.getRadioPr().setSelected(true);
                cariPelangganView.dispose();
            }
        }
    }
}