package com.example.bd.model;

import java.sql.Date;

public class PelangganVoucher {
    private int idPelangganVoucher;
    private int idPelanggan;
    private int idVoucher;
    private String kodeVoucher;
    private Date tanggalPenukaran;
    private Date tanggalKadaluarsa;
    private String statusVoucher;

    private String namaVoucher;
    private int poinDigunakan;
    private double potonganHarga;


    public int getIdPelangganVoucher() { return idPelangganVoucher; }
    public void setIdPelangganVoucher(int idPelangganVoucher) { this.idPelangganVoucher = idPelangganVoucher; }

    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public int getIdVoucher() { return idVoucher; }
    public void setIdVoucher(int idVoucher) { this.idVoucher = idVoucher; }

    public String getKodeVoucher() { return kodeVoucher; }
    public void setKodeVoucher(String kodeVoucher) { this.kodeVoucher = kodeVoucher; }

    public Date getTanggalPenukaran() { return tanggalPenukaran; }
    public void setTanggalPenukaran(Date tanggalPenukaran) { this.tanggalPenukaran = tanggalPenukaran; }

    public Date getTanggalKadaluarsa() { return tanggalKadaluarsa; }
    public void setTanggalKadaluarsa(Date tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }

    public String getStatusVoucher() { return statusVoucher; }
    public void setStatusVoucher(String statusVoucher) { this.statusVoucher = statusVoucher; }

    public String getNamaVoucher() { return namaVoucher; }
    public void setNamaVoucher(String namaVoucher) { this.namaVoucher = namaVoucher; }

    public int getPoinDigunakan() { return poinDigunakan; }
    public void setPoinDigunakan(int poinDigunakan) { this.poinDigunakan = poinDigunakan; }

    public double getPotonganHarga() { return potonganHarga; }
    public void setPotonganHarga(double potonganHarga) { this.potonganHarga = potonganHarga; }
}