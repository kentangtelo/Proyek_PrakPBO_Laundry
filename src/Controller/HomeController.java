/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.HomeView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

/**
 *
 * @author TUF
 */
public class HomeController extends MouseAdapter implements ActionListener {
    HomeView view;
    
    public HomeController() {
        view = new HomeView();
        view.addActionListener(this);
        view.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        
        if(source.equals(view.getBtnCustomerBaru())){
            new CustomerBaruController();
            view.dispose();
        }else if(source.equals(view.getBtnTransaksiBaru())){
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
        }else if(source.equals(view.getBtnDataLaundry())){
            new DataLaundryController();
            view.dispose();
        }else if(source.equals(view.getBtnLogOut())){
            view.dispose();
            new LoginController();
        }
    }
    
    
}
