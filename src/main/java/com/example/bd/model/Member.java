package com.example.bd.model;

import java.sql.Date;

public class Member {
    private int idMember;
    private int idPelanggan;
    private String namaMember;
    private Date tanggalJoin;
    private int jumlahPoin;

    // Getters and Setters
    public int getIdMember() { return idMember; }
    public void setIdMember(int idMember) { this.idMember = idMember; }

    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public String getNamaMember() { return namaMember; }
    public void setNamaMember(String namaMember) { this.namaMember = namaMember; }

    public Date getTanggalJoin() { return tanggalJoin; }
    public void setTanggalJoin(Date tanggalJoin) { this.tanggalJoin = tanggalJoin; }

    public int getJumlahPoin() { return jumlahPoin; }
    public void setJumlahPoin(int jumlahPoin) { this.jumlahPoin = jumlahPoin; }
}