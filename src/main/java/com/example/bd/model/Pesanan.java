package com.example.bd.model;

import java.sql.Timestamp;

public class Pesanan {
    private int idPesanan;
    private int idPelanggan;
    private Timestamp tanggalPesanan;
    private double totalHargaPesanan;
    private String statusPembayaran;
    private String alamatTujuan;

    // -- FIELD BARU UNTUK NAMA PELANGGAN --
    private String namaPelanggan;

    // --- Generate Getters and Setters untuk semua field ---

    public int getIdPesanan() { return idPesanan; }
    public void setIdPesanan(int idPesanan) { this.idPesanan = idPesanan; }
    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }
    public Timestamp getTanggalPesanan() { return tanggalPesanan; }
    public void setTanggalPesanan(Timestamp tanggalPesanan) { this.tanggalPesanan = tanggalPesanan; }
    public double getTotalHargaPesanan() { return totalHargaPesanan; }
    public void setTotalHargaPesanan(double totalHargaPesanan) { this.totalHargaPesanan = totalHargaPesanan; }
    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
    public String getAlamatTujuan() { return alamatTujuan; }
    public void setAlamatTujuan(String alamatTujuan) { this.alamatTujuan = alamatTujuan; }
    private String statusPengiriman;

    // -- GETTER DAN SETTER UNTUK FIELD BARU --
    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }
    public String getStatusPengiriman() { return statusPengiriman; }
    public void setStatusPengiriman(String statusPengiriman) { this.statusPengiriman = statusPengiriman; }
}