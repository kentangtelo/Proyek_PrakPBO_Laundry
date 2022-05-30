/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Customer;
import Model.MainModel;
import View.CustomerBaruView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.JOptionPane;

/**
 *
 * @author TUF
 */
public class CustomerBaruController extends MouseAdapter implements ActionListener{
    CustomerBaruView view;
    MainModel model;
    Customer c;
    private String idPel;

    public CustomerBaruController() {
        view = new CustomerBaruView();
        model = new MainModel();
        view.addActionListener(this);
        view.setVisible(true);
    }
    
    public void resetForm() {
        view.setTfNama("");
        view.setTfAlamat("");
        view.setTfNoHp("");
        view.getBgJK().clearSelection();
        view.setLabelIdCust("null");
    }
    
    public Customer cek(Customer c, String idPel){
        String nama = view.getTfNama();
        String alamat = view.getTfAlamat();
        String noTelp = view.getTfNoHp();
        boolean bgNotNull = view.getRadioLk().isSelected() || view.getRadioPr().isSelected();
         if (nama.equals("") || alamat.equals("") || noTelp.equals("") || !bgNotNull) {
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
            String jenisKelamin = view.getBgJK().getSelection().getActionCommand();
            idPel = "CS00" + (model.getLastIdCust()+1);
            view.setLabelIdCust(idPel);
            return c = new Customer(nama,alamat,noTelp,jenisKelamin,idPel);
         }
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if(source.equals(view.getBtnTambahPelanggan())){
            if(cek(c,idPel) != null){
                model.insertCust(cek(c,idPel));
                JOptionPane.showMessageDialog(view, "Berhasil menambahkan pelanggan baru", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(source.equals(view.getBtnTambahTransaksi())){
            String id = view.getLabelIdCust();
            String nama = view.getTfNama();
            String alamat = view.getTfAlamat();
            String noTelp = view.getTfNoHp();
            String jenisKelamin = view.getBgJK().getSelection().getActionCommand();
            String noTransaksi = "";
            String layanan = "";
            String status = "";
            String berat = "";
            String total = "";
            new MenuTransaksiController(noTransaksi,id,nama,alamat,noTelp,jenisKelamin,layanan,status,berat,total);
            view.dispose();
        }else if(source.equals(view.getBtnKembali())){
            resetForm();
            new HomeController();
            view.dispose();
        }
    }
}
