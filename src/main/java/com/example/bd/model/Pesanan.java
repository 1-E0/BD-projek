package com.example.bd.model;

import java.sql.Date; // Alterado de Timestamp para Date
import java.sql.Timestamp;

public class Pesanan {
    private int idPesanan;
    private int idPelanggan;
    private Date tanggalPesanan; // Alterado para Date
    private double totalHargaPesanan;
    private String statusPembayaran;
    private String alamatTujuan;

    // Campos auxiliares
    private String namaPelanggan;
    private String statusPengiriman;
    private Timestamp waktuStatus; // Para status_pesanan

    // Getters e Setters
    public int getIdPesanan() { return idPesanan; }
    public void setIdPesanan(int idPesanan) { this.idPesanan = idPesanan; }
    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public Date getTanggalPesanan() { return tanggalPesanan; } // Alterado
    public void setTanggalPesanan(Date tanggalPesanan) { this.tanggalPesanan = tanggalPesanan; } // Alterado

    public double getTotalHargaPesanan() { return totalHargaPesanan; }
    public void setTotalHargaPesanan(double totalHargaPesanan) { this.totalHargaPesanan = totalHargaPesanan; }

    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }

    public String getAlamatTujuan() { return alamatTujuan; }
    public void setAlamatTujuan(String alamatTujuan) { this.alamatTujuan = alamatTujuan; }

    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }

    public String getStatusPengiriman() { return statusPengiriman; }
    public void setStatusPengiriman(String statusPengiriman) { this.statusPengiriman = statusPengiriman; }

    public Timestamp getWaktuStatus() { return waktuStatus; }
    public void setWaktuStatus(Timestamp waktuStatus) { this.waktuStatus = waktuStatus; }
}