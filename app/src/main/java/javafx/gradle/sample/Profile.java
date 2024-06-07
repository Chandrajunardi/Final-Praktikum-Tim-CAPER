package javafx.gradle.sample;

import javafx.stage.Stage;
import javafx.gradle.sample.config.DataBase;
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
        
        Image home = new Image(getClass().getResourceAsStream("/images/HomeIcon.png"));
        ImageView images = new ImageView(home);
        images.setFitWidth(35); 
        images.setFitHeight(35);

        HBox imageHbox = new HBox(images);
        imageHbox.setAlignment(Pos.CENTER);

        imageHbox.setOnMouseClicked(e -> {
            // HalamanUtama hal = new HalamanUtama(stage, daftarPemasukan, daftarPengeluaran);
            // hal.halamanUtama(userInfo);
            Home h = new Home(stage, daftarPemasukan, daftarPengeluaran);
            h.jalan(userInfo);
        });

        Rectangle container = new Rectangle(370, 250);
        container.setArcHeight(20);
        container.setArcWidth(20);
        container.setFill(Color.web("#ffffff"));


        // Set border color and width
        container.setStroke(Color.web("#ff0000"));
        container.setStrokeWidth(2);

        Text title = new Text("Informasi Pribadi");
        title.setFont(new Font("Arial", 25));
        title.setFill(Color.web("#121649"));

        Text name = new Text("Nama\t\t:   " + nama);
        format(name);
        Text Email = new Text("Email\t\t:   " + email);
        format(Email);
        Text Umur = new Text("Umur\t\t:   " + umur);
        format(Umur);
        Text Alamat = new Text("Alamat\t\t:   " + alamat);
        format(Alamat);
        Text Pekerjaan = new Text("Pekerjaan\t\t:   " + pekerjaan);
        format(Pekerjaan);

        VBox centerVBox = new VBox(title);
        centerVBox.setAlignment(Pos.CENTER);


        VBox cenBox = new VBox(name, batas(), Email, batas(), Umur, batas(), Alamat, batas(), Pekerjaan, batas());
        cenBox.setAlignment(Pos.CENTER_LEFT);
        cenBox.setPadding(new Insets(0, 0, 0, 220));

        

        StackPane tPane = new StackPane(container, cenBox);
        tPane.setAlignment(Pos.CENTER);
        // tPane.setPadding(new Insets(1, 170, 0, 70));

        VBox maniV = new VBox(20, centerVBox, tPane);
        maniV.setAlignment(Pos.CENTER);

        VBox akhir = new VBox(6, imageHbox, maniV);
        
        // profileImageView.setOnMouseClicked(event -> {
        //     // Buat sebuah PieChart dengan data pengeluaran
        //     PieChart pengeluaranChart = new PieChart(getPengeluaranData());
        //     pengeluaranChart.setTitle("Diagram Pengeluaran");

        //     // Buat sebuah VBox untuk menampung chart
        //     VBox chartContainer = new VBox(pengeluaranChart);
        //     chartContainer.setAlignment(Pos.CENTER);
        //     chartContainer.setStyle("-fx-background-image: url(\"/images/LatarHalamanUtama.png\"); " +
        //                     "-fx-background-size: cover; " +
        //                     "-fx-background-position: center;");

        //     // Buat sebuah scene baru dengan VBox sebagai root
        //     Scene chartScene = new Scene(chartContainer, 400, 400);

        //     // Buat sebuah stage baru untuk menampilkan scene kecil
        //     Stage chartStage = new Stage();
        //     chartStage.setScene(chartScene);
        //     chartStage.setTitle("Diagram Pengeluaran");
            
        //     // Tampilkan stage baru
        //     chartStage.show();
        // });

        Scene scene = new Scene(akhir, 700, 400);


        stage.setScene(scene);
        stage.setTitle("Profile");
        stage.setResizable(false);
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

    public void format(Text text){
        text.setStyle("-fx-text-fill: #ff8210;");
    }
    public Text batas(){
        return new Text("-----------------------------------------------------");
    }


}