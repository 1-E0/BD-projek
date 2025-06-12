package com.example.bd.model;

public class Admin {

    private int idAdmin;
    private String namaAdmin;
    private String emailAdmin;
    private String passwordAdmin; // Ditambahkan untuk fungsi login
    private String jenisKelamin;
    private String noTelpAdmin;
    private String statusAdmin;
    private String fotoProfilAdmin;

    // Constructors (opsional, tapi baik untuk dimiliki)
    public Admin() {
    }

    
    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNamaAdmin() {
        return namaAdmin;
    }

    public void setNamaAdmin(String namaAdmin) {
        this.namaAdmin = namaAdmin;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getNoTelpAdmin() {
        return noTelpAdmin;
    }

    public void setNoTelpAdmin(String noTelpAdmin) {
        this.noTelpAdmin = noTelpAdmin;
    }

    public String getStatusAdmin() {
        return statusAdmin;
    }

    public void setStatusAdmin(String statusAdmin) {
        this.statusAdmin = statusAdmin;
    }

    public String getFotoProfilAdmin() {
        return fotoProfilAdmin;
    }

    public void setFotoProfilAdmin(String fotoProfilAdmin) {
        this.fotoProfilAdmin = fotoProfilAdmin;
    }
}