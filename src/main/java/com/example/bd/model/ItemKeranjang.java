package com.example.bd.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ItemKeranjang {
    private Menu menu;
    private IntegerProperty kuantitas;

    // --- PERUBAHAN UTAMA: Tambahkan DoubleProperty untuk subtotal ---
    private DoubleProperty subtotal;

    public ItemKeranjang(Menu menu, int kuantitas) {
        this.menu = menu;
        this.kuantitas = new SimpleIntegerProperty(kuantitas);

        // Inisialisasi subtotal dan ikat nilainya
        this.subtotal = new SimpleDoubleProperty();
        this.subtotal.bind(this.kuantitas.multiply(menu.getHargaMenu()));
    }

    public Menu getMenu() {
        return menu;
    }

    // --- Kuantitas (tidak ada perubahan) ---
    public int getKuantitas() {
        return kuantitas.get();
    }
    public void setKuantitas(int kuantitas) {
        this.kuantitas.set(kuantitas);
    }
    public IntegerProperty kuantitasProperty() {
        return kuantitas;
    }

    // --- Nama dan Harga (tidak ada perubahan) ---
    public String getNamaMenu() {
        return menu.getNamaMenu();
    }
    public double getHargaMenu() {
        return menu.getHargaMenu();
    }

    // --- Subtotal sekarang menggunakan Property ---
    public double getSubtotal() {
        return subtotal.get();
    }
    public DoubleProperty subtotalProperty() {
        return subtotal;
    }
}