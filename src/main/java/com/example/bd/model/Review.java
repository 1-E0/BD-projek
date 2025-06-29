package com.example.bd.model;

import java.sql.Date;

public class Review {
    private int idReview;
    private int idPelanggan;
    private int idPesanan;
    private int rating;
    private String komentar;
    private Date tanggalReview;


    private String namaPelanggan;


    public int getIdReview() { return idReview; }
    public void setIdReview(int idReview) { this.idReview = idReview; }

    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public int getIdPesanan() { return idPesanan; }
    public void setIdPesanan(int idPesanan) { this.idPesanan = idPesanan; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getKomentar() { return komentar; }
    public void setKomentar(String komentar) { this.komentar = komentar; }

    public Date getTanggalReview() { return tanggalReview; }
    public void setTanggalReview(Date tanggalReview) { this.tanggalReview = tanggalReview; }

    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }
}