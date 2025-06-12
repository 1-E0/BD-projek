package com.example.bd.util;

import com.example.bd.model.ItemKeranjang;
import com.example.bd.model.Menu;
import com.example.bd.model.Pelanggan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserSession {

    private static UserSession instance;
    private Pelanggan loggedInPelanggan;

    // --- KERANJANG BELANJA GLOBAL ---
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

    // --- METHOD BARU UNTUK MENGELOLA KERANJANG ---

    public ObservableList<ItemKeranjang> getKeranjang() {
        return keranjang;
    }

    public void addItemToCart(Menu menu, int kuantitas) {
        // Cek apakah item sudah ada di keranjang
        for (ItemKeranjang item : keranjang) {
            if (item.getMenu().getIdMenu() == menu.getIdMenu()) {
                item.setKuantitas(item.getKuantitas() + kuantitas); // Cukup update kuantitas
                return;
            }
        }
        // Jika belum ada, tambahkan item baru
        keranjang.add(new ItemKeranjang(menu, kuantitas));
    }

    public void clearKeranjang() {
        keranjang.clear();
    }

    public void cleanUserSession() {
        loggedInPelanggan = null;
        clearKeranjang(); // Sekalian bersihkan keranjang saat logout
    }
}