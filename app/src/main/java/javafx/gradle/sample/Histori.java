package javafx.gradle.sample;

import javafx.stage.Stage;
import javafx.gradle.sample.config.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Tooltip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import com.jfoenix.controls.JFXButton.ButtonType;

import javafx.scene.control.ButtonType;

import javafx.gradle.sample.config.DataBase;


public class Histori {
    Stage stage;
    private UserInfo userInfo;
    private DaftarPemasukan daftarPemasukan;
    private DaftarPengeluaran daftarPengeluaran;
    private int id;
    private TransactionTables transactionTables;

    public Histori(Stage stage, int id, UserInfo userInfo, DaftarPemasukan daftarPemasukan, DaftarPengeluaran daftarPengeluaran) {
        this.stage = stage;
        this.id = id;
        this.userInfo = userInfo;
        this.daftarPemasukan = daftarPemasukan != null ? daftarPemasukan : new DaftarPemasukan(userInfo);
        this.daftarPengeluaran = daftarPengeluaran != null ? daftarPengeluaran : new DaftarPengeluaran(userInfo);

    }

    public void histori(){
        // Initialize TransactionTables
        TransactionTables transactionTables = new TransactionTables();

        // Create ImageView for Home icon
        Image home = new Image(getClass().getResourceAsStream("/images/HomeIcon.png"));
        ImageView images = new ImageView(home);
        images.setFitWidth(35); 
        images.setFitHeight(35);

        images.setOnMouseClicked(e ->{
            Home h = new Home(stage, daftarPemasukan, daftarPengeluaran);
            h.jalan(userInfo);
        });
        
        VBox image = new VBox(images);
        image.setAlignment(Pos.TOP_LEFT);

        // Create Histori label
        Label histori = new Label("Histori...");
        histori.setFont(new Font(16));
        histori.setStyle("-fx-font-family: Arial Narrow;-fx-background-color: #191d34; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        histori.setPadding(new Insets(4, 20, 4, 20));
        histori.setAlignment(Pos.TOP_CENTER);

        // Create Hapus label
        Label hapus = new Label("Hapus");
        hapus.setFont(new Font(16));
        hapus.setStyle("-fx-font-family: Arial Narrow;-fx-background-color: #ff0000; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        hapus.setPadding(new Insets(4, 20, 4, 20));
        hapus.setAlignment(Pos.BASELINE_LEFT);
        
        hapus.setOnMouseClicked(e -> {
            // Show options to delete "Pemasukan" or "Pengeluaran"
            Alert optionAlert = new Alert(Alert.AlertType.CONFIRMATION);
            optionAlert.setTitle("Hapus Riwayat");
            optionAlert.setHeaderText("Pilih jenis riwayat yang ingin dihapus:");
            ButtonType buttonPemasukan = new ButtonType("Pemasukan");
            ButtonType buttonPengeluaran = new ButtonType("Pengeluaran");

            optionAlert.getButtonTypes().setAll(buttonPemasukan, buttonPengeluaran);

            java.util.Optional<ButtonType> optionResult = optionAlert.showAndWait();
            if (optionResult.isPresent()) {
                if (optionResult.get() == buttonPemasukan) {
                    confirmDelete("pemasukan");
                } else if (optionResult.get() == buttonPengeluaran) {
                    confirmDelete("pengeluaran");
                }
            }
        });

        // Create the top HBox
        HBox mainTop = new HBox(200, image, histori, hapus);
        mainTop.setPadding(new Insets(20));
        mainTop.setAlignment(Pos.CENTER);

        // Create the bottom rectangle
        Rectangle bottomRectangle = new Rectangle(630, 270);
        bottomRectangle.setArcWidth(20);
        bottomRectangle.setArcHeight(20);
        bottomRectangle.setFill(Color.web("#ff0000", 0.3));

        // Create subheadings for the tables
        Label pemasukanLabel = new Label("Pemasukan");
        pemasukanLabel.setFont(new Font(16));
        pemasukanLabel.setStyle("-fx-font-family: Arial Narrow; -fx-text-fill: black; -fx-font-weight: bold;");
        pemasukanLabel.setAlignment(Pos.CENTER);

        Label pengeluaranLabel = new Label("Pengeluaran");
        pengeluaranLabel.setFont(new Font(16));
        pengeluaranLabel.setStyle("-fx-font-family: Arial Narrow; -fx-text-fill: black; -fx-font-weight: bold;");
        pengeluaranLabel.setAlignment(Pos.CENTER);

        // Create VBox for each table with its subheading
        VBox pemasukanBox = new VBox(5, pemasukanLabel, transactionTables.getPemasukanTable());
        pemasukanBox.setAlignment(Pos.CENTER);

        VBox pengeluaranBox = new VBox(5, pengeluaranLabel, transactionTables.getPengeluaranTable());
        pengeluaranBox.setAlignment(Pos.CENTER);

        // Create an HBox for the tables
        HBox tablesBox = new HBox(20, pemasukanBox, pengeluaranBox);
        tablesBox.setAlignment(Pos.CENTER);
        tablesBox.setPadding(new Insets(10));

        // Create a StackPane to hold the bottom rectangle and the tables
        StackPane bottomPane = new StackPane();
        bottomPane.getChildren().addAll(bottomRectangle, tablesBox);
        bottomPane.setAlignment(Pos.CENTER);

        // Create the main VBox
        VBox mainBox = new VBox(20, mainTop, bottomPane);
        mainBox.setAlignment(Pos.CENTER);

        // Create the scene and set it on the stage
        Scene scene = new Scene(mainBox, 700, 400);
        stage.setScene(scene);
        stage.setTitle("Histori");
        stage.show();
    }

    private void confirmDelete(String type) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi");
        confirmAlert.setHeaderText("Hapus Semua Riwayat " + (type.equals("pemasukan") ? "Pemasukan" : "Pengeluaran"));
        confirmAlert.setContentText("Apakah Anda yakin ingin menghapus semua riwayat " + type + " transaksi?");

        java.util.Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (type.equals("pemasukan")) {
                // Hapus semua item dari tabel Pemasukan
                transactionTables.getPemasukanTable().getItems().clear();
                // Hapus semua riwayat pemasukan dari database
                DataBase.deleteHistoryByIdAndType(userInfo.getId(), "pemasukan");
            } else if (type.equals("pengeluaran")) {
                // Hapus semua item dari tabel Pengeluaran
                transactionTables.getPengeluaranTable().getItems().clear();
                // Hapus semua riwayat pengeluaran dari database
                DataBase.deleteHistoryByIdAndType(userInfo.getId(), "pengeluaran");
            }
        }
    }
}
