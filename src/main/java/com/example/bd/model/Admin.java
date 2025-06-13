package com.example.bd.model;

public class Admin {

    private int idAdmin;
    private int idCabang; // Adicionado
    private String jenisAdmin; // Adicionado
    private String namaAdmin;
    private String emailAdmin;
    private String passwordAdmin;
    private String noTelpAdmin; // Renomeado
    private String statusAdmin;

    // Getters e Setters
    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public int getIdCabang() {
        return idCabang;
    }

    public void setIdCabang(int idCabang) {
        this.idCabang = idCabang;
    }

    public String getJenisAdmin() {
        return jenisAdmin;
    }

    public void setJenisAdmin(String jenisAdmin) {
        this.jenisAdmin = jenisAdmin;
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
}