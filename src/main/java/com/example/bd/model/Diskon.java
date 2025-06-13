package com.example.bd.model;

import java.time.LocalDate;

public class Diskon {
    private int idDiskon;
    private String namaDiskon;
    private double persenDiskon;
    private String syaratDanKetentuanDiskon;
    private LocalDate tanggalMulaiDiskon;
    private LocalDate tanggalAkhirDiskon;
    // O campo min_pembelian foi removido

    @Override
    public String toString() {
        return namaDiskon + " (" + persenDiskon + "%)";
    }

    // Getters e Setters
    public int getIdDiskon() { return idDiskon; }
    public void setIdDiskon(int idDiskon) { this.idDiskon = idDiskon; }

    public String getNamaDiskon() { return namaDiskon; }
    public void setNamaDiskon(String namaDiskon) { this.namaDiskon = namaDiskon; }

    public double getPersenDiskon() { return persenDiskon; }
    public void setPersenDiskon(double persenDiskon) { this.persenDiskon = persenDiskon; }

    public String getSyaratDanKetentuanDiskon() { return syaratDanKetentuanDiskon; }
    public void setSyaratDanKetentuanDiskon(String syaratDanKetentuanDiskon) { this.syaratDanKetentuanDiskon = syaratDanKetentuanDiskon; }

    public LocalDate getTanggalMulaiDiskon() { return tanggalMulaiDiskon; }
    public void setTanggalMulaiDiskon(LocalDate tanggalMulaiDiskon) { this.tanggalMulaiDiskon = tanggalMulaiDiskon; }

    public LocalDate getTanggalAkhirDiskon() { return tanggalAkhirDiskon; }
    public void setTanggalAkhirDiskon(LocalDate tanggalAkhirDiskon) { this.tanggalAkhirDiskon = tanggalAkhirDiskon; }
}