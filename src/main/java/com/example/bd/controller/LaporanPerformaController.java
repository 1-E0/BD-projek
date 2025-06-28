package com.example.bd.controller;

import com.example.bd.dao.LaporanDAO;
import com.example.bd.model.LaporanPerforma;
import com.example.bd.util.Navigasi;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LaporanPerformaController implements Initializable {

    @FXML private ChoiceBox<Integer> yearChooser;
    @FXML private TableView<LaporanPerforma> reportTable;
    @FXML private TableColumn<LaporanPerforma, String> colBulan;
    @FXML private TableColumn<LaporanPerforma, String> colCabang;
    @FXML private TableColumn<LaporanPerforma, Integer> colJumlahPesanan;
    @FXML private TableColumn<LaporanPerforma, String> colTotalPendapatan;

    private final LaporanDAO laporanDAO = new LaporanDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        populateYearChooser();
    }

    private void setupTableColumns() {
        colBulan.setCellValueFactory(new PropertyValueFactory<>("namaBulan"));
        colCabang.setCellValueFactory(new PropertyValueFactory<>("namaCabang"));
        colJumlahPesanan.setCellValueFactory(new PropertyValueFactory<>("jumlahPesanan"));
        // Menggunakan getter khusus untuk format Rupiah
        colTotalPendapatan.setCellValueFactory(new PropertyValueFactory<>("totalPendapatanFormatted"));
    }

    private void populateYearChooser() {
        int currentYear = Year.now().getValue();
        // Menampilkan pilihan tahun dari 2023 hingga tahun sekarang
        yearChooser.setItems(FXCollections.observableList(
                IntStream.rangeClosed(2023, currentYear).boxed().collect(Collectors.toList())
        ));
        // Set nilai default ke tahun sekarang
        yearChooser.setValue(currentYear);
    }

    @FXML
    void handleGenerateReport() {
        Integer selectedYear = yearChooser.getValue();
        if (selectedYear != null) {
            reportTable.setItems(FXCollections.observableArrayList(
                    laporanDAO.getLaporanPerformaByYear(selectedYear)
            ));
        }
    }

    @FXML
    void handleKembali(ActionEvent event) {
        Navigasi.goBack(event, "DashboardView.fxml");
    }
}
