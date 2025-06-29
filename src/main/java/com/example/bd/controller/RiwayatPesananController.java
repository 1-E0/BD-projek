package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.PesananDAO;
import com.example.bd.model.Pelanggan;
import com.example.bd.model.Pesanan;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RiwayatPesananController implements Initializable {

    @FXML private Label lblTitle;
    @FXML private ListView<Pesanan> pesananListView;

    private final PesananDAO pesananDAO = new PesananDAO();
    private Pelanggan pelangganLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pelangganLogin = UserSession.getInstance().getLoggedInPelanggan();
        if (pelangganLogin == null) {
            lblTitle.setText("Error: Tidak ada data login");
            return;
        }

        setupPesananListView();
        loadPesananData();
    }

    private void loadPesananData() {
        ObservableList<Pesanan> pesananList = FXCollections.observableArrayList(
                pesananDAO.getPesananByPelanggan(pelangganLogin.getIdPelanggan())
        );
        pesananListView.setItems(pesananList);
    }

    private void setupPesananListView() {
        pesananListView.setCellFactory(listView -> new ListCell<Pesanan>() {
            @Override
            protected void updateItem(Pesanan pesanan, boolean empty) {
                super.updateItem(pesanan, empty);
                if (empty || pesanan == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/RiwayatPesananItem.fxml"));
                        Node graphic = loader.load();
                        RiwayatPesananItemController controller = loader.getController();
                        controller.setData(pesanan);
                        setGraphic(graphic);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    private void handleKembali(ActionEvent event) throws IOException {
        Navigasi.goBack(event, "PelangganDashboardView.fxml");
    }
}