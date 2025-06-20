package com.example.bd.util;

import com.example.bd.model.ItemKeranjang;
import com.example.bd.model.Menu;
import com.example.bd.model.Pelanggan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserSession {

    private static UserSession instance;
    private Pelanggan loggedInPelanggan;
    private ObservableList<ItemKeranjang> keranjang;

    private UserSession() {
        keranjang = FXCollections.observableArrayList();
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public Pelanggan getLoggedInPelanggan() {
        return loggedInPelanggan;
    }

    public void setLoggedInPelanggan(Pelanggan loggedInPelanggan) {
        this.loggedInPelanggan = loggedInPelanggan;
    }

    public ObservableList<ItemKeranjang> getKeranjang() {
        return keranjang;
    }

    public void addItemToCart(Menu menu, int kuantitas, int idMenuHarian) {
        for (ItemKeranjang item : keranjang) {
            if (item.getIdMenuHarian() == idMenuHarian) {
                item.setKuantitas(item.getKuantitas() + kuantitas);
                return;
            }
        }
        keranjang.add(new ItemKeranjang(menu, kuantitas, idMenuHarian));
    }

    public void clearKeranjang() {
        keranjang.clear();
    }

    public void cleanUserSession() {
        loggedInPelanggan = null;
        clearKeranjang();
    }
}