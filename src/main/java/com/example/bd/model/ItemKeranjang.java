package com.example.bd.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ItemKeranjang {
    private Menu menu;
    private IntegerProperty kuantitas;
    private DoubleProperty subtotal;


    private int idMenuHarian;

    public ItemKeranjang(Menu menu, int kuantitas, int idMenuHarian) {
        this.menu = menu;
        this.kuantitas = new SimpleIntegerProperty(kuantitas);
        this.idMenuHarian = idMenuHarian;

        this.subtotal = new SimpleDoubleProperty();
        this.subtotal.bind(this.kuantitas.multiply(menu.getHargaMenu()));
    }

    public int getIdMenuHarian() {
        return idMenuHarian;
    }

    public void setIdMenuHarian(int idMenuHarian) {
        this.idMenuHarian = idMenuHarian;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getKuantitas() {
        return kuantitas.get();
    }
    public void setKuantitas(int kuantitas) {
        this.kuantitas.set(kuantitas);
    }
    public IntegerProperty kuantitasProperty() {
        return kuantitas;
    }

    public String getNamaMenu() {
        return menu.getNamaMenu();
    }
    public double getHargaMenu() {
        return menu.getHargaMenu();
    }

    public double getSubtotal() {
        return subtotal.get();
    }
    public DoubleProperty subtotalProperty() {
        return subtotal;
    }
}