package com.example.bd.model;

import java.sql.Date;

public class RiwayatPenukaran {
    private String namaHadiah;
    private int poinDigunakan;
    private Date tanggalPenukaran;

    // Getters and Setters
    public String getNamaHadiah() { return namaHadiah; }
    public void setNamaHadiah(String namaHadiah) { this.namaHadiah = namaHadiah; }

    public int getPoinDigunakan() { return poinDigunakan; }
    public void setPoinDigunakan(int poinDigunakan) { this.poinDigunakan = poinDigunakan; }

    public Date getTanggalPenukaran() { return tanggalPenukaran; }
    public void setTanggalPenukaran(Date tanggalPenukaran) { this.tanggalPenukaran = tanggalPenukaran; }
}