package com.example.bd.model;

public class Menu {
    private int idMenu;
    private String namaMenu;
    private double hargaMenu;
    private String gambarMenu;

    // -- FIELD BARU UNTUK FOREIGN KEY KE TABEL KATEGORI --
    private int idKategori;

    // Getters and Setters
    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public double getHargaMenu() {
        return hargaMenu;
    }

    public void setHargaMenu(double hargaMenu) {
        this.hargaMenu = hargaMenu;
    }

    public String getGambarMenu() {
        return gambarMenu;
    }

    public void setGambarMenu(String gambarMenu) {
        this.gambarMenu = gambarMenu;
    }

    // -- GETTER & SETTER BARU UNTUK ID KATEGORI --
    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }
}