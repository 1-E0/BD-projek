package com.example.bd.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Pesanan {
    private int idPesanan;
    private int idPelanggan;
    private Date tanggalPesanan;
    private double totalHargaPesanan;
    private String statusPembayaran;
    private String alamatTujuan;
    private Timestamp jadwalPengiriman;


    private String namaPelanggan;
    private String statusPengiriman;


    public int getIdPesanan() { return idPesanan; }
    public void setIdPesanan(int idPesanan) { this.idPesanan = idPesanan; }
    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }
    public Date getTanggalPesanan() { return tanggalPesanan; }
    public void setTanggalPesanan(Date tanggalPesanan) { this.tanggalPesanan = tanggalPesanan; }
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


    public Timestamp getJadwalPengiriman() { return jadwalPengiriman; }
    public void setJadwalPengiriman(Timestamp jadwalPengiriman) { this.jadwalPengiriman = jadwalPengiriman; }
}