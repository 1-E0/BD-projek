package com.example.bd.model;

public class Cabang {
    private int idCabang;
    private int idKota;
    private String alamatCabang;
    private String noTelpCabang;

    // Field baru untuk menampung nama kota dari hasil JOIN
    private String namaKota;

    @Override
    public String toString() {
        // Ini akan ditampilkan di ComboBox pada form Staff
        return alamatCabang;
    }

    // --- Getters and Setters ---
    public int getIdCabang() { return idCabang; }
    public void setIdCabang(int idCabang) { this.idCabang = idCabang; }

    public int getIdKota() { return idKota; }
    public void setIdKota(int idKota) { this.idKota = idKota; }

    public String getAlamatCabang() { return alamatCabang; }
    public void setAlamatCabang(String alamatCabang) { this.alamatCabang = alamatCabang; }

    public String getNoTelpCabang() { return noTelpCabang; }
    public void setNoTelpCabang(String noTelpCabang) { this.noTelpCabang = noTelpCabang; }

    public String getNamaKota() { return namaKota; }
    public void setNamaKota(String namaKota) { this.namaKota = namaKota; }
}