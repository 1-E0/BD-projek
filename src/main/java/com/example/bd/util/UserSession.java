package com.example.bd.util;

import com.example.bd.model.Cabang;
import com.example.bd.model.ItemKeranjang;
import com.example.bd.model.Menu;
import com.example.bd.model.Pelanggan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserSession {

    private static UserSession instance;
    private Pelanggan loggedInPelanggan;
    private ObservableList<ItemKeranjang> keranjang;
    private Cabang selectedCabang; // <-- FIELD BARU

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

    // --- GETTER & SETTER BARU UNTUK CABANG ---
    public Cabang getSelectedCabang() {
        return selectedCabang;
    }

    public void setSelectedCabang(Cabang selectedCabang) {
        this.selectedCabang = selectedCabang;
    }

    /**
     * Helper method untuk mendapatkan ID cabang yang dipilih dengan aman.
     * Mengembalikan 1 jika tidak ada cabang yang dipilih (sebagai fallback).
     * @return int ID cabang.
     */
    public int getSelectedCabangId() {
        if (selectedCabang != null) {
            return selectedCabang.getIdCabang();
        }
        // Fallback ke cabang pertama jika tidak ada yang dipilih
        // Anda bisa sesuaikan logika ini, misal melempar error
        return 1;
    }
    // --- AKHIR GETTER & SETTER BARU ---

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
        selectedCabang = null; // <-- BERSIHKAN CABANG SAAT LOGOUT
        clearKeranjang();
    }
}