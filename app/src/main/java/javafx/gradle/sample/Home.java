package javafx.gradle.sample;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.gradle.sample.config.DataBase;

public class Home {
    
    String nama;
    Stage stage;
    UserInfo userInfo;
    Pemasukan pemasukan;
    Pengeluaran pengeluaran;
    DaftarPengeluaran daftarPengeluaran;
    DaftarPemasukan daftarPemasukan;

    public Home(Stage stage, DaftarPemasukan daftarPemasukan, DaftarPengeluaran daftarPengeluaran) {
        this.stage = stage;
        this.daftarPemasukan = daftarPemasukan;
        this.daftarPengeluaran = daftarPengeluaran;
    }

    // @SuppressWarnings("unchecked")
    public void jalan(UserInfo userInfo){
        this.userInfo = userInfo;

        Image garisMenu = new Image(getClass().getResourceAsStream("/images/Tap.png"));
        ImageView image = new ImageView(garisMenu);
        image.setFitWidth(35); 
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
            // pengeluaranChart.setPrefSize(150, 150);

            // Buat sebuah VBox untuk menampung chart
            VBox chartContainer = new VBox(pengeluaranChart);
            chartContainer.setAlignment(Pos.CENTER);
            chartContainer.setStyle("-fx-background-image: url(\"/images/LatarHalamanUtama.png\"); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center;");

            // Buat sebuah scene baru dengan VBox sebagai root
            Scene chartScene = new Scene(chartContainer, 500, 500);

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
        Text haiNama = new Text("Hai, "+ userInfo.getUsername());

        haiNama.setFont(new Font(20));
        haiNama.setFill(Color.web("#191d34"));
        haiNama.setStyle("-fx-font-weight: bold;");

        // Label untuk menampilkan jumlah saldo
        double saldoDouble = SaldoDompet.ambilSaldoDariDatabase(userInfo.getId());
        String saldoString = String.valueOf(saldoDouble);

        Label labelSaldo = new Label("Saldo Rp. " + saldoString);
        labelSaldo.setFont(new Font(16));
        labelSaldo.setStyle("-fx-font-family: Arial Narrow;-fx-background-color: #00a650; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        labelSaldo.setPadding(new Insets(4, 20, 4, 20));
        labelSaldo.setAlignment(Pos.BASELINE_LEFT);

        Label pemasukan = new Label("Tambah Pemasukan  ");
        pemasukan.setFont(new Font(16));
        pemasukan.setStyle("-fx-background-color: #6637cd; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        pemasukan.setPadding(new Insets(3, 20, 3, 20));

        pemasukan.setOnMouseClicked(e -> {
            Pemasukan pemasukanPage = new Pemasukan(stage, daftarPemasukan, daftarPengeluaran, userInfo);
            pemasukanPage.pemasukan();
        });

        Label pengeluaran = new Label("Tambah Pengeluaran");
        pengeluaran.setFont(new Font(16));
        pengeluaran.setStyle("-fx-background-color: #6637cd; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        pengeluaran.setPadding(new Insets(3, 20, 3, 20));

        pengeluaran.setOnMouseClicked(e -> {
            double saldoDompet = SaldoDompet.ambilSaldoDariDatabase(userInfo.getId());
            if (saldoDompet == 0) {
                Alert alert3 = new Alert(Alert.AlertType.INFORMATION, "Belum ada pemasukan");
                alert3.show();
            } else {
                Pengeluaran pengeluaranPage = new Pengeluaran(stage, daftarPemasukan, daftarPengeluaran, userInfo);
                pengeluaranPage.pengeluaran();
            }
        });

        // Left Vbox
        VBox leftVBox = new VBox(20, image, haiNama, labelSaldo, pemasukan, pengeluaran);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setPadding(new Insets(70));
        leftVBox.setMinWidth(350);

        // Bagian Kanan
        Image logo = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(250); 
        logoView.setFitHeight(250);

        VBox logVBox = new VBox(logoView);
        logVBox.setAlignment(Pos.CENTER_RIGHT);

        Label lihatHistori = new Label("Lihat Histori");
        lihatHistori.setFont(new Font(16));
        lihatHistori.setPadding(new Insets(3, 20, 3, 20));
        lihatHistori.setStyle("-fx-background-color:#191d34;-fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");

        lihatHistori.setOnMouseClicked(e -> {
            Histori his = new Histori(stage, userInfo.getId(), userInfo, daftarPemasukan, daftarPengeluaran);
            his.histori();
        });
        VBox rightVBox = new VBox( logVBox, lihatHistori);
        rightVBox.setAlignment(Pos.CENTER);
        rightVBox.setPadding(new Insets(20));

        // Gabung kiri kanan
        HBox mainBox = new HBox(20, leftVBox, rightVBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(30));
        mainBox.setStyle("fx-background-color: #ffffff;");

        Scene scene = new Scene(mainBox, 700, 400);

        stage.setScene(scene);
        stage.setTitle("Halaman Utama");
        stage.show();
    }
    public ObservableList<PieChart.Data> getPengeluaranData() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        // Kumpulkan data pengeluaran berdasarkan kategori dari database
        Map<String, Double> pengeluaranMap = new HashMap<>();
        List<DaftarPengeluaran.Pengeluaran> historyList = DataBase.getPengeluaranHistory(userInfo.getId()); // Ambil data transaksi dari database

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
