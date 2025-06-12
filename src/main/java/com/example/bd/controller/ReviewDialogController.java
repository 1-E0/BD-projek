package com.example.bd.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ReviewDialogController {

    @FXML private HBox ratingBox;
    @FXML private TextArea txtKomentar;
    @FXML private Button btnKirim;

    private int rating = 0;

    public void initialize() {
        for (int i = 1; i <= 5; i++) {
            ToggleButton starButton = new ToggleButton();
            // Beri style class pada tombolnya agar bisa di-style oleh CSS
            starButton.getStyleClass().add("rating-toggle-button");

            FontAwesomeIconView icon = new FontAwesomeIconView();
            // Kita set BINTANG KOSONG sebagai ikon default
            icon.setGlyphName("STAR_ALT");
            icon.setSize("2em");
            // Beri style class pada ikonnya
            icon.getStyleClass().add("glyph-icon");

            starButton.setGraphic(icon);

            // Saat sebuah bintang diklik, panggil method untuk update visual
            final int starIndex = i;
            starButton.setOnAction(event -> {
                this.rating = starIndex;
                updateStarSelection(starIndex);
            });

            ratingBox.getChildren().add(starButton);
        }
    }

    /**
     * Method ini hanya mengatur tombol mana yang "terpilih"
     * dan mana yang tidak. CSS akan mengurus sisanya (warna dan bentuk ikon).
     */
    private void updateStarSelection(int currentRating) {
        for (int i = 0; i < ratingBox.getChildren().size(); i++) {
            ToggleButton currentButton = (ToggleButton) ratingBox.getChildren().get(i);
            FontAwesomeIconView icon = (FontAwesomeIconView) currentButton.getGraphic();

            if (i < currentRating) {
                currentButton.setSelected(true); // Set status tombol
                icon.setGlyphName("STAR");       // Ganti ikon menjadi bintang penuh
            } else {
                currentButton.setSelected(false); // Set status tombol
                icon.setGlyphName("STAR_ALT");  // Ganti ikon menjadi bintang kosong
            }
        }
    }

    @FXML
    private void handleKirim() {
        closeDialog();
    }

    @FXML
    private void handleBatal() {
        rating = 0; // Tandai sebagai batal
        closeDialog();
    }

    public int getRating() {
        return rating;
    }

    public String getKomentar() {
        return txtKomentar.getText();
    }

    private void closeDialog() {
        Stage stage = (Stage) btnKirim.getScene().getWindow();
        stage.close();
    }
}