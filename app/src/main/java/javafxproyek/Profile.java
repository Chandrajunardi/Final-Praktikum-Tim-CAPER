package javafxproyek;

import javafx.stage.Stage;
import javafxproyek.config.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Tooltip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {
    private Stage stage;
    private String nama, email, umur, alamat, pekerjaan;
    private UserInfo userInfo;
    private DaftarPemasukan daftarPemasukan;
    private DaftarPengeluaran daftarPengeluaran;
    private int id;

    public Profile(Stage stage, int id, UserInfo userInfo, DaftarPemasukan daftarPemasukan, DaftarPengeluaran daftarPengeluaran) {
        this.stage = stage;
        this.id = id;
        this.userInfo = userInfo;
        this.daftarPemasukan = daftarPemasukan != null ? daftarPemasukan : new DaftarPemasukan(userInfo);
        this.daftarPengeluaran = daftarPengeluaran != null ? daftarPengeluaran : new DaftarPengeluaran(userInfo);
        fetchDataFromDatabase();
    }

    

    private void fetchDataFromDatabase() {

        try (Connection conn = DataBase.connect();
            PreparedStatement pstmt = conn.prepareStatement("SELECT nama, email, umur, alamat, pekerjaan FROM inputData WHERE id = ?")) {
            pstmt.setInt(1, id); 
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                nama = rs.getString("nama");
                email = rs.getString("email");
                umur = rs.getString("umur");
                alamat = rs.getString("alamat");
                pekerjaan = rs.getString("pekerjaan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showProfile() {
        // Background image
        Image background = new Image(getClass().getResourceAsStream("/images/LatarHalamanUtama.png"));
        ImageView backgroundImageView = new ImageView(background);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(400);

        // Container with gradient background
        Rectangle container = new Rectangle(500, 370);
        container.setArcHeight(20);
        container.setArcWidth(20);
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 0,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#ff5757", 0.31)),
                new Stop(1, Color.web("#8c52ff", 0.31))
        );
        container.setFill(gradient);

        // Home Icon
        Image homeImage = new Image(getClass().getResourceAsStream("/images/HomeIcon.png"));
        ImageView homeImageView = new ImageView(homeImage);
        homeImageView.setFitWidth(35);
        homeImageView.setFitHeight(35);

        homeImageView.setOnMouseClicked(e -> {
            HalamanUtama hal = new HalamanUtama(stage, daftarPemasukan, daftarPengeluaran);
            hal.halamanUtama(userInfo);
        });

        // Profile Icon
        Image profileIcon = new Image(getClass().getResourceAsStream("/images/Profil.png"));
        ImageView profileImageView = new ImageView(profileIcon);
        profileImageView.setFitWidth(60);
        profileImageView.setFitHeight(60);

        // Tambahkan event handler ke ImageView
        
        // Labels for each piece of data
        Label namaLabel = new Label("Nama      :");
        Label namaValue = new Label(nama);
        setupLabel(namaLabel, namaValue);

        Label emailLabel = new Label("Email       :");
        Label emailValue = new Label(email);
        setupLabel(emailLabel, emailValue);

        Label umurLabel = new Label("Umur       :");
        Label umurValue = new Label(umur);
        setupLabel(umurLabel, umurValue);
 
        Label alamatLabel = new Label("Alamat     :");
        Label alamatValue = new Label(alamat);
        setupLabel(alamatLabel, alamatValue);

        Label pekerjaanLabel = new Label("Pekerjaan:");
        Label pekerjaanValue = new Label(pekerjaan);
        setupLabel(pekerjaanLabel, pekerjaanValue);


        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: linear-gradient(to right, #ffde59, #ff914d); -fx-text-fill: white; -fx-font-size: 16px;");
        logout.setPadding(new Insets(5, 20, 5, 20));
        logout.setOnAction(e -> {
            HalamanLogin halamanLogin = new HalamanLogin(stage);
            halamanLogin.halamanLogin();
        });
        

        // GridPane for labels and values
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(namaLabel, 0, 0);
        gridPane.add(namaValue, 1, 0);
        gridPane.add(emailLabel, 0, 1);
        gridPane.add(emailValue, 1, 1);
        gridPane.add(umurLabel, 0, 2);
        gridPane.add(umurValue, 1, 2);
        gridPane.add(alamatLabel, 0, 3);
        gridPane.add(alamatValue, 1, 3);
        gridPane.add(pekerjaanLabel, 0, 4);
        gridPane.add(pekerjaanValue, 1, 4);

        // HBox satu = new HBox(10,gridPane, pengeluaranChart);
        

        VBox textVBox = new VBox(20, profileImageView, gridPane, logout);
        textVBox.setAlignment(Pos.CENTER);
        textVBox.setPadding(new Insets(10, 10, 10, 10));

        StackPane containerStack = new StackPane(container, textVBox);
        containerStack.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, containerStack, homeImageView);
        StackPane.setAlignment(homeImageView, Pos.CENTER_LEFT);
        StackPane.setMargin(homeImageView, new Insets(10));
        
        profileImageView.setOnMouseClicked(event -> {
            // Buat sebuah PieChart dengan data pengeluaran
            PieChart pengeluaranChart = new PieChart(getPengeluaranData());
            pengeluaranChart.setTitle("Diagram Pengeluaran");

            // Buat sebuah VBox untuk menampung chart
            VBox chartContainer = new VBox(pengeluaranChart);
            chartContainer.setAlignment(Pos.CENTER);
            chartContainer.setStyle("-fx-background-image: url(\"/images/LatarHalamanUtama.png\"); " +
                            "-fx-background-size: cover; " +
                            "-fx-background-position: center;");

            // Buat sebuah scene baru dengan VBox sebagai root
            Scene chartScene = new Scene(chartContainer, 400, 400);

            // Buat sebuah stage baru untuk menampilkan scene kecil
            Stage chartStage = new Stage();
            chartStage.setScene(chartScene);
            chartStage.setTitle("Diagram Pengeluaran");
            
            // Tampilkan stage baru
            chartStage.show();
        });

        Scene scene = new Scene(root, 800, 400);

        // Ensure the background image resizes with the scene
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            backgroundImageView.setFitWidth((double) newValue);
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            backgroundImageView.setFitHeight((double) newValue);
        });

        stage.setScene(scene);
        stage.setTitle("Profile");
        stage.show();
    }

    private void setupLabel(Label label, Label value) {
        label.setStyle("-fx-font-family: Arial Narrow; -fx-font-size: 24; -fx-background-color: transparent;");
        value.setStyle("-fx-font-family: Arial Narrow; -fx-font-size: 24; -fx-background-color: transparent;");
    }



    public ObservableList<PieChart.Data> getPengeluaranData() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        // Kumpulkan data pengeluaran berdasarkan kategori dari database
        Map<String, Double> pengeluaranMap = new HashMap<>();
        List<Object> historyList = DataBase.getTransactionHistory(userInfo.getId()); // Ambil data transaksi dari database

        // Loop melalui data transaksi dan tambahkan ke pengeluaranMap
        for (Object transaction : historyList) {
            if (transaction instanceof DaftarPengeluaran.Pengeluaran) {
                DaftarPengeluaran.Pengeluaran pengeluaran = (DaftarPengeluaran.Pengeluaran) transaction;
                String kategori = pengeluaran.getKeteranganInput(); // Asumsikan keterangan sebagai kategori
                double jumlah = pengeluaran.getJumlahInput();
                pengeluaranMap.put(kategori, pengeluaranMap.getOrDefault(kategori, 0.0) + jumlah);
            }
        }

        // Tambahkan data ke dalam PieChart
        for (Map.Entry<String, Double> entry : pengeluaranMap.entrySet()) {
            String kategori = entry.getKey();
            double jumlah = entry.getValue();
            String keterangan = kategori + " : " + String.format("%.2f", jumlah); // Buat keterangan dengan format "kategori : jumlah"
            PieChart.Data slice = new PieChart.Data(kategori, jumlah);
            Tooltip.install(slice.getNode(), new Tooltip(keterangan)); // Tambahkan tooltip untuk menampilkan keterangan
            data.add(slice);
        }

        return data;
    }


}