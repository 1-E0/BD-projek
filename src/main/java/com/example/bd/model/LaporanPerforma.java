package com.example.bd.model;

import java.text.NumberFormat;
import java.util.Locale;

public class LaporanPerforma {

    private String namaBulan;
    private int tahun;
    private String namaCabang;
    private int jumlahPesanan;
    private double totalPendapatan;

    public String getNamaBulan() {
        return namaBulan;
    }

    public void setNamaBulan(String namaBulan) {
        this.namaBulan = namaBulan;
    }

    public int getTahun() {
        return tahun;
    }

    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    public String getNamaCabang() {
        return namaCabang;
    }

    public void setNamaCabang(String namaCabang) {
        this.namaCabang = namaCabang;
    }

    public int getJumlahPesanan() {
        return jumlahPesanan;
    }

    public void setJumlahPesanan(int jumlahPesanan) {
        this.jumlahPesanan = jumlahPesanan;
    }

    public double getTotalPendapatan() {
        return totalPendapatan;
    }

    // Getter khusus untuk format tampilan Rupiah
    public String getTotalPendapatanFormatted() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(totalPendapatan);
    }

    public void setTotalPendapatan(double totalPendapatan) {
        this.totalPendapatan = totalPendapatan;
    }
}
