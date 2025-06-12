package com.example.bd.model;

import java.sql.Timestamp;

public class Review {
    private int idReview;
    private int idPelanggan;
    private int idPesanan;
    private int reviewPesanan; // Rating (1-5)
    private String komentar;
    private Timestamp tanggalReview;

    // -- FIELD BARU DARI JOIN --
    private String namaPelanggan;

    // --- Generate Getters and Setters ---

    public int getIdReview() { return idReview; }
    public void setIdReview(int idReview) { this.idReview = idReview; }

    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public int getIdPesanan() { return idPesanan; }
    public void setIdPesanan(int idPesanan) { this.idPesanan = idPesanan; }

    public int getReviewPesanan() { return reviewPesanan; }
    public void setReviewPesanan(int reviewPesanan) { this.reviewPesanan = reviewPesanan; }

    public String getKomentar() { return komentar; }
    public void setKomentar(String komentar) { this.komentar = komentar; }

    public Timestamp getTanggalReview() { return tanggalReview; }
    public void setTanggalReview(Timestamp tanggalReview) { this.tanggalReview = tanggalReview; }

    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }
}