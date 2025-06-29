package com.example.bd.controller;

import com.example.bd.dao.ReviewDAO;
import com.example.bd.model.Review;
import com.example.bd.util.Navigasi;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class ManajemenReviewController implements Initializable {

    @FXML private TableView<Review> reviewTable;
    @FXML private TableColumn<Review, Integer> colIdReview;
    @FXML private TableColumn<Review, Integer> colIdPesanan;
    @FXML private TableColumn<Review, String> colNamaPelanggan;
    @FXML private TableColumn<Review, Integer> colRating;
    @FXML private TableColumn<Review, String> colKomentar;
    @FXML private TableColumn<Review, Date> colTanggal;

    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadReviewData();
    }

    private void setupTableColumns() {
        colIdReview.setCellValueFactory(new PropertyValueFactory<>("idReview"));
        colIdPesanan.setCellValueFactory(new PropertyValueFactory<>("idPesanan"));
        colNamaPelanggan.setCellValueFactory(new PropertyValueFactory<>("namaPelanggan"));
        colKomentar.setCellValueFactory(new PropertyValueFactory<>("komentar"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalReview"));

        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colRating.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null || rating == 0) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox ratingBox = new HBox(3);
                    ratingBox.setAlignment(Pos.CENTER);
                    for (int i = 0; i < 5; i++) {
                        FontAwesomeIconView starIcon = new FontAwesomeIconView();
                        starIcon.setSize("1.2em");
                        if (i < rating) {
                            starIcon.setGlyphName("STAR");

                            starIcon.setStyle("-fx-fill: #FFC107;");
                        } else {
                            starIcon.setGlyphName("STAR_ALT");

                            starIcon.setStyle("-fx-fill: #BDBDBD;");
                        }
                        ratingBox.getChildren().add(starIcon);
                    }
                    setGraphic(ratingBox);
                }
            }
        });
    }

    private void loadReviewData() {
        ObservableList<Review> reviewList = FXCollections.observableArrayList(reviewDAO.getAllReviews());
        reviewTable.setItems(reviewList);
    }

    @FXML
    private void handleKembali(ActionEvent event) throws IOException {
        Navigasi.goBack(event, "DashboardView.fxml");
    }
}
