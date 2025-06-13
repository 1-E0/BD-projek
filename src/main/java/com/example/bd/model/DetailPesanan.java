package com.example.bd.model;

public class DetailPesanan {
    private int idDetailPesanan;
    private int idPesanan;
    private int idMenuHarian; // Alterado de idMenu
    private int jumlah; // Alterado de kuantitas
    private double hargaProduk;
    private String catatan; // Adicionado

    // Campo auxiliar para exibição
    private String namaMenu;
    private int idMenu; // Adicionado para referência

    // Getters e Setters
    public int getIdDetailPesanan() { return idDetailPesanan; }
    public void setIdDetailPesanan(int idDetailPesanan) { this.idDetailPesanan = idDetailPesanan; }

    public int getIdPesanan() { return idPesanan; }
    public void setIdPesanan(int idPesanan) { this.idPesanan = idPesanan; }

    public int getIdMenuHarian() { return idMenuHarian; }
    public void setIdMenuHarian(int idMenuHarian) { this.idMenuHarian = idMenuHarian; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public double getHargaProduk() { return hargaProduk; }
    public void setHargaProduk(double hargaProduk) { this.hargaProduk = hargaProduk; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public String getNamaMenu() { return namaMenu; }
    public void setNamaMenu(String namaMenu) { this.namaMenu = namaMenu; }

    public int getIdMenu() { return idMenu; }
    public void setIdMenu(int idMenu) { this.idMenu = idMenu; }
}