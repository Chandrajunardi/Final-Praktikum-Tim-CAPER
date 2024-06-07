package javafx.gradle.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.gradle.sample.config.DataBase;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class HalamanUtama {
    String nama;
    Stage stage;
    Profile profile;
    UserInfo userInfo;
    Pemasukan pemasukan;
    Pengeluaran pengeluaran;
    DaftarPengeluaran daftarPengeluaran;
    DaftarPemasukan daftarPemasukan;
    TableView<Object> tableView;
    TableColumn<Object, String> jenisTransaksiColumn;
    TableColumn<Object, String> tanggalColumn;
    TableColumn<Object, String> keteranganColumn;
    TableColumn<Object, Double> jumlahColumn;

    public HalamanUtama(Stage stage, DaftarPemasukan daftarPemasukan, DaftarPengeluaran daftarPengeluaran) {
        this.stage = stage;
        this.daftarPemasukan = daftarPemasukan;
        this.daftarPengeluaran = daftarPengeluaran;
    }

    public void halamanUtama(UserInfo userInfo) {
        this.userInfo = userInfo;

        // Buat LatarBelakang
        Image latar = new Image(getClass().getResourceAsStream("/images/LatarHalamanUtama.png"));
        ImageView imageView = new ImageView(latar);
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(800);
        imageView.setFitHeight(400);

        Rectangle kotak = new Rectangle(650, 350);
        kotak.setArcHeight(20);
        kotak.setArcWidth(20);
        kotak.setFill(Color.web("#7b51eb", 0.4));

        // Profil
        Image profileImage = new Image(getClass().getResourceAsStream("/images/Tap.png"));
        ImageView image = new ImageView(profileImage);
        image.setFitWidth(30);
        image.setFitHeight(40);

        // ContextMenu untuk opsi klik kanan
        ContextMenu contextMenu = new ContextMenu();
        MenuItem profileItem = new MenuItem("Lihat Profile");
        MenuItem diagramItem = new MenuItem("Diagram Pengeluaran");
        MenuItem logoutItem = new MenuItem("Logout");

        contextMenu.getItems().addAll(profileItem, diagramItem, logoutItem);

        // Event handler untuk menu item
        profileItem.setOnAction(event -> {
            System.out.println("Lihat Profile dipilih");
            Profile profilePage = new Profile(stage, userInfo.getId(), userInfo, daftarPemasukan, daftarPengeluaran);
            profilePage.showProfile();
        });

        diagramItem.setOnAction(event -> {
            System.out.println("Diagram Pengeluaran dipilih");
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

        logoutItem.setOnAction(event -> {
            System.out.println("Logout dipilih");
            HalamanLogin halamanLogin = new HalamanLogin(stage);
            halamanLogin.halamanLogin();
        });

        // Menambahkan event handler untuk klik pada gambar
        image.setOnMouseClicked(e -> {
            contextMenu.show(image, e.getScreenX(), e.getScreenY());
        });

        // Labels and Buttons
        Text haiNama = new Text("Hai, " + userInfo.getUsername());
        haiNama.setFont(new Font(20));
        haiNama.setFill(Color.web("#191d34"));
        haiNama.setStyle("-fx-font-weight: bold;");

        // Label untuk menampilkan jumlah saldo
        double saldoDouble = SaldoDompet.ambilSaldoDariDatabase(userInfo.getId());
        String saldoString = String.valueOf(saldoDouble);

        Label labelSaldo = new Label("Saldo Rp " + saldoString);
        labelSaldo.setFont(new Font(16));
        labelSaldo.setStyle("-fx-font-family: Arial Narrow;-fx-background-color: #00a650; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        labelSaldo.setPadding(new Insets(4, 20, 4, 20));
        labelSaldo.setAlignment(Pos.BASELINE_LEFT);

        Button pengeluaran = new Button("Add Pengeluaran");
        pengeluaran.setFont(new Font(16));
        pengeluaran.setStyle("-fx-background-color: #6637cd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20;");
        pengeluaran.setPrefSize(200, 40);


        pengeluaran.setOnAction(e -> {
            double saldoDompet = SaldoDompet.ambilSaldoDariDatabase(userInfo.getId());
            if (saldoDompet == 0) {
                Alert alert3 = new Alert(Alert.AlertType.INFORMATION, "Belum ada pemasukan");
                alert3.show();
            } else {
                Pengeluaran pengeluaranPage = new Pengeluaran(stage, daftarPemasukan, daftarPengeluaran, userInfo);
                pengeluaranPage.pengeluaran();
            }
        });

        Button pemasukan = new Button("Add Pemasukan");
        pemasukan.setFont(new Font(16));
        pemasukan.setStyle("-fx-background-color: #6637cd; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20;");
        pemasukan.setPrefSize(200, 40);


        pemasukan.setOnAction(e -> {
            Pemasukan pemasukanPage = new Pemasukan(stage, daftarPemasukan, daftarPengeluaran, userInfo);
            pemasukanPage.pemasukan();
        });

        // Left Vbox
        VBox leftVBox = new VBox(25, image, haiNama, labelSaldo, pemasukan, pengeluaran);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setPadding(new Insets(20));

        // Bagian Kanan
        Image logo = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(100);
        logoView.setFitHeight(100);

        Button lihatHistori = new Button("Lihat Histori");
        lihatHistori.setFont(new Font(16));
        lihatHistori.setPadding(new Insets(3, 20, 3, 20));
        lihatHistori.setStyle("-fx-background-color: linear-gradient(to right, #ff5757, #8c52ff); -fx-text-fill: black;");

        lihatHistori.setOnAction(e -> {
            Histori histor = new Histori(stage, userInfo, daftarPemasukan, daftarPengeluaran);
            histor.tampilkanhistori();
        });

        // Vbox kanan
        VBox logVBox = new VBox(logoView);
        logVBox.setAlignment(Pos.CENTER_RIGHT);

        VBox rightVBox = new VBox(10, logVBox, lihatHistori);
        rightVBox.setAlignment(Pos.CENTER);

        // Gabung kiri kanan
        HBox mainBox = new HBox(20, leftVBox, rightVBox);
        mainBox.setAlignment(Pos.CENTER);

        StackPane dalamPane = new StackPane();
        dalamPane.setAlignment(Pos.CENTER);
        dalamPane.getChildren().addAll(kotak, mainBox);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, dalamPane);

        Scene scene = new Scene(stackPane, 800, 400);

        // Buat agar ImageView menyesuaikan dengan ukuran Scene
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitWidth((double) newValue);
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitHeight((double) newValue);
        });

        stage.setScene(scene);
        stage.setTitle("Halaman Utama");
        stage.show();
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
