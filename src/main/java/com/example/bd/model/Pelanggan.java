package com.example.bd.model;

import java.sql.Date;

public class Pelanggan {
    private int idPelanggan;
    private String namaPelanggan;
    private String emailPelanggan;
    private String passwordPelanggan;
    private String alamatPelanggan;
    private String noTelpPelanggan;

    // --- FIELD BARU DARI TABEL MEMBER ---
    private int jumlahPoin;
    private Date tanggalJoin;

    // Getters and Setters (termasuk untuk field baru)
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

    public int getJumlahPoin() { return jumlahPoin; }
    public void setJumlahPoin(int jumlahPoin) { this.jumlahPoin = jumlahPoin; }

    public Date getTanggalJoin() { return tanggalJoin; }
    public void setTanggalJoin(Date tanggalJoin) { this.tanggalJoin = tanggalJoin; }
}