package com.example.bd.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private boolean submitted = false;

    public void initialize() {
        for (int i = 1; i <= 5; i++) {
            ToggleButton starButton = new ToggleButton();
            starButton.getStyleClass().add("rating-toggle-button");

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setGlyphName("STAR_ALT");
            icon.setSize("2em");
            icon.getStyleClass().add("glyph-icon");

            starButton.setGraphic(icon);

            final int starIndex = i;
            starButton.setOnAction(event -> {
                this.rating = starIndex;
                updateStarSelection(starIndex);
            });

            ratingBox.getChildren().add(starButton);
        }
    }

    private void updateStarSelection(int currentRating) {
        for (int i = 0; i < ratingBox.getChildren().size(); i++) {
            ToggleButton currentButton = (ToggleButton) ratingBox.getChildren().get(i);
            FontAwesomeIconView icon = (FontAwesomeIconView) currentButton.getGraphic();

            if (i < currentRating) {
                currentButton.setSelected(true);
                icon.setGlyphName("STAR");
            } else {
                currentButton.setSelected(false);
                icon.setGlyphName("STAR_ALT");
            }
        }
    }

    @FXML
    private void handleKirim() {

        if (rating > 0) {
            this.submitted = true;
            closeDialog();
        } else {

            showAlert(Alert.AlertType.WARNING, "Peringatan", "Silakan pilih rating bintang terlebih dahulu sebelum mengirim.");
        }
    }

    @FXML
    private void handleBatal() {
        this.rating = 0;
        this.submitted = false;
        closeDialog();
    }


    public boolean isSubmitted() {
        return this.submitted;
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}