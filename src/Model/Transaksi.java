package Model;


public class Transaksi {

    private String noTransaksi, idCust, layanan, status, tanggal;
    private double berat, total;

    public Transaksi(String noTransaksi, String idCust, String layanan, String status, 
            String tanggal, double berat, double total) {
        this.noTransaksi = noTransaksi;
        this.layanan = layanan;
        this.status = status;
        this.tanggal = tanggal;
        this.berat = berat;
        this.total = total;
        this.idCust = idCust;
    }

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public String getLayanan() {
        return layanan;
    }

    public String getStatus() {
        return status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public double getBerat() {
        return berat;
    }

    public double getTotal() {
        return total;
    }

    public String getIdCust() {
        return idCust;
    }
}