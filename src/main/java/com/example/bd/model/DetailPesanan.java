package com.example.bd.model;

public class DetailPesanan {
    private int idDetailPesanan;
    private int idPesanan;
    private int idMenu;
    private int kuantitas;
    private double hargaProduk;

    // -- FIELD BARU UNTUK MENAMPUNG NAMA MENU DARI JOIN --
    private String namaMenu;

    // --- Generate Getters and Setters untuk semua field ---

    public int getIdDetailPesanan() { return idDetailPesanan; }
    public void setIdDetailPesanan(int idDetailPesanan) { this.idDetailPesanan = idDetailPesanan; }

    public int getIdPesanan() { return idPesanan; }
    public void setIdPesanan(int idPesanan) { this.idPesanan = idPesanan; }

    public int getIdMenu() { return idMenu; }
    public void setIdMenu(int idMenu) { this.idMenu = idMenu; }

    public int getKuantitas() { return kuantitas; }
    public void setKuantitas(int kuantitas) { this.kuantitas = kuantitas; }

    public double getHargaProduk() { return hargaProduk; }
    public void setHargaProduk(double hargaProduk) { this.hargaProduk = hargaProduk; }

    public String getNamaMenu() { return namaMenu; }
    public void setNamaMenu(String namaMenu) { this.namaMenu = namaMenu; }
}