package com.example.bd.model;

public class Hadiah {
    private int idHadiah;
    private String namaHadiah;
    private String jenisHadiah; // BARANG atau VOUCHER
    private String deskripsiHadiah;
    private int biayaPoin;
    private Double nilaiVoucher; // Persentase diskon
    private String statusHadiah;

    // Getters and Setters
    public int getIdHadiah() { return idHadiah; }
    public void setIdHadiah(int idHadiah) { this.idHadiah = idHadiah; }

    public String getNamaHadiah() { return namaHadiah; }
    public void setNamaHadiah(String namaHadiah) { this.namaHadiah = namaHadiah; }

    public String getJenisHadiah() { return jenisHadiah; }
    public void setJenisHadiah(String jenisHadiah) { this.jenisHadiah = jenisHadiah; }

    public String getDeskripsiHadiah() { return deskripsiHadiah; }
    public void setDeskripsiHadiah(String deskripsiHadiah) { this.deskripsiHadiah = deskripsiHadiah; }

    public int getBiayaPoin() { return biayaPoin; }
    public void setBiayaPoin(int biayaPoin) { this.biayaPoin = biayaPoin; }

    public Double getNilaiVoucher() { return nilaiVoucher; }
    public void setNilaiVoucher(Double nilaiVoucher) { this.nilaiVoucher = nilaiVoucher; }

    public String getStatusHadiah() { return statusHadiah; }
    public void setStatusHadiah(String statusHadiah) { this.statusHadiah = statusHadiah; }
}