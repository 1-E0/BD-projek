package com.example.bd.model;

import java.sql.Date;

public class VoucherPelanggan {
    private int idVoucherPelanggan;
    private int idPelanggan;
    private int idHadiah;
    private String kodeVoucher;
    private String statusVoucher;
    private Date tanggalDibuat;
    private Date tanggalKadaluarsa;

    // Informasi tambahan dari join dengan tabel hadiah
    private String namaHadiah;
    private Double nilaiVoucher;

    // Getters and Setters
    public int getIdVoucherPelanggan() { return idVoucherPelanggan; }
    public void setIdVoucherPelanggan(int idVoucherPelanggan) { this.idVoucherPelanggan = idVoucherPelanggan; }

    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public int getIdHadiah() { return idHadiah; }
    public void setIdHadiah(int idHadiah) { this.idHadiah = idHadiah; }

    public String getKodeVoucher() { return kodeVoucher; }
    public void setKodeVoucher(String kodeVoucher) { this.kodeVoucher = kodeVoucher; }

    public String getStatusVoucher() { return statusVoucher; }
    public void setStatusVoucher(String statusVoucher) { this.statusVoucher = statusVoucher; }

    public Date getTanggalDibuat() { return tanggalDibuat; }
    public void setTanggalDibuat(Date tanggalDibuat) { this.tanggalDibuat = tanggalDibuat; }

    public Date getTanggalKadaluarsa() { return tanggalKadaluarsa; }
    public void setTanggalKadaluarsa(Date tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }

    public String getNamaHadiah() { return namaHadiah; }
    public void setNamaHadiah(String namaHadiah) { this.namaHadiah = namaHadiah; }

    public Double getNilaiVoucher() { return nilaiVoucher; }
    public void setNilaiVoucher(Double nilaiVoucher) { this.nilaiVoucher = nilaiVoucher; }

    // Untuk ditampilkan di ComboBox
    @Override
    public String toString() {
        if (namaHadiah == null) return "Pilih Voucher...";
        return String.format("%s (Diskon %.0f%%)", namaHadiah, nilaiVoucher);
    }
}