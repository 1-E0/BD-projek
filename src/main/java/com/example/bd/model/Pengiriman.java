package com.example.bd.model;

public class Pengiriman {
    private int idPengiriman;
    private int idPesanan;
    private String statusPengiriman;
    private String catatanPengiriman;

    public int getIdPengiriman() {
        return idPengiriman;
    }

    public void setIdPengiriman(int idPengiriman) {
        this.idPengiriman = idPengiriman;
    }

    public int getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(int idPesanan) {
        this.idPesanan = idPesanan;
    }

    public String getStatusPengiriman() {
        return statusPengiriman;
    }

    public void setStatusPengiriman(String statusPengiriman) {
        this.statusPengiriman = statusPengiriman;
    }

    public String getCatatanPengiriman() {
        return catatanPengiriman;
    }

    public void setCatatanPengiriman(String catatanPengiriman) {
        this.catatanPengiriman = catatanPengiriman;
    }
}