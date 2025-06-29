package com.example.bd.util;

import com.example.bd.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserSession {

    private static UserSession instance;
    private Pelanggan loggedInPelanggan;
    private Admin loggedInAdmin; // <-- FIELD BARU UNTUK ADMIN
    private ObservableList<ItemKeranjang> keranjang;
    private Cabang selectedCabang;

    private UserSession() {
        keranjang = FXCollections.observableArrayList();
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }


    public Admin getLoggedInAdmin() {
        return loggedInAdmin;
    }

    public void setLoggedInAdmin(Admin loggedInAdmin) {
        this.loggedInAdmin = loggedInAdmin;
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

    public Cabang getSelectedCabang() {
        return selectedCabang;
    }

    public void setSelectedCabang(Cabang selectedCabang) {
        this.selectedCabang = selectedCabang;
    }

    public int getSelectedCabangId() {
        if (selectedCabang != null) {
            return selectedCabang.getIdCabang();
        }
        return 1;
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
        loggedInAdmin = null;
        selectedCabang = null;
        clearKeranjang();
    }
}