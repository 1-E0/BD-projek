package com.example.bd.model;

import java.time.LocalDate;

public class MenuHarian {
    private int idMenuHarian;
    private int idMenu;
    private int idCabang;
    private LocalDate tanggalMenuHarian;
    private int stokMenuHarian;

    // Campos auxiliares para exibição
    private String namaMenu;
    private double hargaMenu;

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

    // Getters e Setters
    public int getIdMenuHarian() {
        return idMenuHarian;
    }

    public void setIdMenuHarian(int idMenuHarian) {
        this.idMenuHarian = idMenuHarian;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getIdCabang() {
        return idCabang;
    }

    public void setIdCabang(int idCabang) {
        this.idCabang = idCabang;
    }

    public LocalDate getTanggalMenuHarian() {
        return tanggalMenuHarian;
    }

    public void setTanggalMenuHarian(LocalDate tanggalMenuHarian) {
        this.tanggalMenuHarian = tanggalMenuHarian;
    }

    public int getStokMenuHarian() {
        return stokMenuHarian;
    }

    public void setStokMenuHarian(int stokMenuHarian) {
        this.stokMenuHarian = stokMenuHarian;
    }
}