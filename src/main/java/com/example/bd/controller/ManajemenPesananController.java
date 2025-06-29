package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.PesananDAO;
import com.example.bd.model.Admin;
import com.example.bd.model.Pesanan;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ManajemenPesananController implements Initializable {

    @FXML private TableView<Pesanan> pesananTable;
    @FXML private TableColumn<Pesanan, Integer> colIdPesanan;
    @FXML private TableColumn<Pesanan, String> colNamaPelanggan;
    @FXML private TableColumn<Pesanan, Date> colTanggal;
    @FXML private TableColumn<Pesanan, Double> colTotal;
    @FXML private TableColumn<Pesanan, String> colAlamatTujuan;
    @FXML private TableColumn<Pesanan, String> colStatusBayar;
    @FXML private TableColumn<Pesanan, String> colStatusKirim;
    @FXML private TableColumn<Pesanan, Timestamp> colJadwal;
    @FXML private TableColumn<Pesanan, Void> colAksi;

    private final PesananDAO pesananDAO = new PesananDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPesananTable();
        loadPesananData();
    }

    private void setupPesananTable() {
        colIdPesanan.setCellValueFactory(new PropertyValueFactory<>("idPesanan"));
        colNamaPelanggan.setCellValueFactory(new PropertyValueFactory<>("namaPelanggan"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalPesanan"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalHargaPesanan"));
        colAlamatTujuan.setCellValueFactory(new PropertyValueFactory<>("alamatTujuan"));
        colStatusBayar.setCellValueFactory(new PropertyValueFactory<>("statusPembayaran"));
        colStatusKirim.setCellValueFactory(new PropertyValueFactory<>("statusPengiriman"));
        colJadwal.setCellValueFactory(new PropertyValueFactory<>("jadwalPengiriman"));


        colJadwal.setCellFactory(column -> new TableCell<>() {
            private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy, HH:mm");

            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {

                    if (item == null) {
                        setText("Langsung Kirim");
                    } else {
                        setText(format.format(item));
                    }
                }
            }
        });

        addAksiButtonToTable();
    }

    private void loadPesananData() {
        Admin loggedInAdmin = UserSession.getInstance().getLoggedInAdmin();
        ObservableList<Pesanan> pesananList;

        if (loggedInAdmin != null && "Cabang".equalsIgnoreCase(loggedInAdmin.getJenisAdmin())) {
            pesananList = FXCollections.observableArrayList(pesananDAO.getPesananByCabang(loggedInAdmin.getIdCabang()));
        } else {
            pesananList = FXCollections.observableArrayList(pesananDAO.getAllPesanan());
        }
        pesananTable.setItems(pesananList);
    }

    private void addAksiButtonToTable() {
        Callback<TableColumn<Pesanan, Void>, TableCell<Pesanan, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Pesanan, Void> call(final TableColumn<Pesanan, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Detail...");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Pesanan data = getTableView().getItems().get(getIndex());
                            openEditDialog(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        colAksi.setCellFactory(cellFactory);
    }

    private void openEditDialog(Pesanan pesanan) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/EditPesananDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Pesanan #" + pesanan.getIdPesanan());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(pesananTable.getScene().getWindow());
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            EditPesananDialogController controller = loader.getController();
            controller.setData(pesanan);

            dialogStage.showAndWait();

            if (controller.isSaved()) {
                loadPesananData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleKembali(ActionEvent event) {
        Navigasi.goBack(event, "DashboardView.fxml");
    }
}