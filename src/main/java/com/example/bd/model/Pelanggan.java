package com.example.bd.model;

public class Pelanggan {
    private int idPelanggan;
    private String namaPelanggan;
    private String emailPelanggan;
    private String passwordPelanggan; // PENTING: Di aplikasi nyata, ini harus di-hash
    private String alamatPelanggan;
    private String noTelpPelanggan;

    // Getters and Setters
    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }

    public String getEmailPelanggan() { return emailPelanggan; }
    public void setEmailPelanggan(String emailPelanggan) { this.emailPelanggan = emailPelanggan; }

    public String getPasswordPelanggan() { return passwordPelanggan; }
    public void setPasswordPelanggan(String passwordPelanggan) { this.passwordPelanggan = passwordPelanggan; }

    public String getAlamatPelanggan() { return alamatPelanggan; }
    public void setAlamatPelanggan(String alamatPelanggan) { this.alamatPelanggan = alamatPelanggan; }

    public String getNoTelpPelanggan() { return noTelpPelanggan; }
    public void setNoTelpPelanggan(String noTelpPelanggan) { this.noTelpPelanggan = noTelpPelanggan; }
}