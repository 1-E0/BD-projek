package com.example.bd.model;

public class Menu {
    private int idMenu;
    private String namaMenu;
    private double hargaMenu;
    private int idKategori;
    // O campo gambar_menu foi removido do esquema principal do menu

    // Getters e Setters
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

    public int getIdKategori() {
        return idKategori;
    }
    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }
}