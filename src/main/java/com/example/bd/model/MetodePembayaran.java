package com.example.bd.model;

public class MetodePembayaran {
    private int idMetode;
    private String caraMetode;

    // Override toString agar mudah ditampilkan di ComboBox nanti
    @Override
    public String toString() {
        return caraMetode;
    }

    // Getters and Setters
    public int getIdMetode() { return idMetode; }
    public void setIdMetode(int idMetode) { this.idMetode = idMetode; }

    public String getCaraMetode() { return caraMetode; }
    public void setCaraMetode(String caraMetode) { this.caraMetode = caraMetode; }
}