package Model;



public class Customer extends Orang {
    
    private String idCust;
    
    public Customer(String nama, String alamat, String noTelp, String jenisKelamin,String idCust) {
        super(nama, alamat, noTelp, jenisKelamin);
        this.idCust = idCust;
    }

    public String getIdCust() {
        return idCust;
    }
}