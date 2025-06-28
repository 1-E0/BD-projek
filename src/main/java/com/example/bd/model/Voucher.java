package com.example.bd.model;

public class Voucher {
    private int idVoucher;
    private String namaVoucher;
    private String deskripsi;
    private double potonganHarga;
    private int poinDibutuhkan;
    private boolean aktif;

    // Getters and Setters
    public int getIdVoucher() { return idVoucher; }
    public void setIdVoucher(int idVoucher) { this.idVoucher = idVoucher; }

    public String getNamaVoucher() { return namaVoucher; }
    public void setNamaVoucher(String namaVoucher) { this.namaVoucher = namaVoucher; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public double getPotonganHarga() { return potonganHarga; }
    public void setPotonganHarga(double potonganHarga) { this.potonganHarga = potonganHarga; }

    public int getPoinDibutuhkan() { return poinDibutuhkan; }
    public void setPoinDibutuhkan(int poinDibutuhkan) { this.poinDibutuhkan = poinDibutuhkan; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}