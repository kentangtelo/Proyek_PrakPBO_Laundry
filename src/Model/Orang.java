package Model;



public abstract class Orang {
    private String nama,alamat,noTelp,jenisKelamin;

    public Orang(String nama,String alamat,String noTelp,String jenisKelamin) {
        this.nama = nama;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.jenisKelamin = jenisKelamin;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }
    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }
    public String getNoTelp() {
        return noTelp;
    }
}