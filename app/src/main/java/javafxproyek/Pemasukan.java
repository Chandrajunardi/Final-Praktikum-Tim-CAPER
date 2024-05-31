package javafxproyek;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.LocalDate;

public class Pemasukan {
    Stage stage;
    private DaftarPemasukan daftarPemasukan;
    private DaftarPengeluaran daftarPengeluaran;
    private UserInfo userInfo;

    private DatePicker tanggalField;
    private TextField keteranganField;
    private TextField jumlahField;

    public Pemasukan(Stage stage, DaftarPemasukan daftarPemasukan, DaftarPengeluaran daftarPengeluaran, UserInfo userInfo) {
        this.stage = stage;
        this.daftarPemasukan = daftarPemasukan != null ? daftarPemasukan : new DaftarPemasukan(userInfo);
        this.daftarPengeluaran = daftarPengeluaran != null ? daftarPengeluaran : new DaftarPengeluaran(userInfo);
        this.userInfo = userInfo;
    }

    public void pemasukan() {
        // Latar Belakang
        Image latar = new Image(getClass().getResourceAsStream("/images/LatarHalamanInput.png"));
        ImageView latarImageView = new ImageView(latar);
        // Mengatur ImageView untuk menutupi seluruh area
        latarImageView.setPreserveRatio(false); // Tidak mempertahankan rasio untuk menutupi seluruh area
        latarImageView.setFitWidth(800); // Placeholder untuk awal
        latarImageView.setFitHeight(400); // Placeholder untuk awal
        
        // Vbox bagian kiri
        
        // Create background rectangle
        Rectangle roundedSquare = new Rectangle(300, 270);
        roundedSquare.setArcWidth(20);
        roundedSquare.setArcHeight(20);
        roundedSquare.setFill(Color.web("#8c52ff", 0.2));
        
        
        // batas
        Text batas1 = new Text("-------------------------------------");
        batas1.setFill(Color.BLACK);
        
        Text batas2 = new Text("-------------------------------------");
        batas2.setFill(Color.BLACK);
        
        // Inisialisasi TextField
        tanggalField = new DatePicker();
        tanggalField.setPromptText("Pilih tanggal");
        tanggalField.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: black;" +
            "-fx-border-width: 2px;" +
            "-fx-prompt-text-fill: black;"
        );

        
        keteranganField = new TextField();
        keteranganField.setPromptText("Masukkan keterangan");
        keteranganField.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-width: 2px; -fx-prompt-text-fill: black;");
        
        jumlahField = new TextField();
        jumlahField.setPromptText("Masukkan jumlah");
        jumlahField.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-width: 2px; -fx-prompt-text-fill: black;");
        
        // Membuat tombol untuk mengirim data
        Button submitButton = new Button("Simpan");
        submitButton.setStyle("-fx-background-color: linear-gradient(to right, #ff66c4, #ffde59); -fx-text-fill: black; -fx-font-size: 12px;");
        submitButton.setPadding(new Insets(5, 20, 5, 20));
  
        submitButton.setOnAction(e -> {
            LocalDate tanggalInput = tanggalField.getValue();
            String keteranganInput = keteranganField.getText();
            double jumlahInput = getJumlahFieldInput();

            if (tanggalInput == null || keteranganInput.isEmpty() || jumlahInput == 0) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Data tidak boleh kosong atau jumlah harus berupa angka.");
                alert1.showAndWait();
            } else {
                DaftarPemasukan.Pemasukan pemasukan = new DaftarPemasukan.Pemasukan(tanggalInput.toString(), keteranganInput, jumlahInput);
                daftarPemasukan.tambahPemasukan(pemasukan);

                tanggalField.setValue(null);
                keteranganField.clear();
                jumlahField.clear();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data berhasil disimpan.");
                alert.showAndWait();
                HalamanUtama halamanUtama = new HalamanUtama(stage, daftarPemasukan, daftarPengeluaran);
                halamanUtama.halamanUtama(userInfo);
            }
        });

        // Membuat tombol batal
        Button batal = new Button("Batal");
        batal.setStyle("-fx-background-color: linear-gradient(to right, #5170ff, #ff66c4); -fx-text-fill: black; -fx-font-size: 12px;");
        batal.setPadding(new Insets(5, 20, 5, 20));
        
        batal.setOnAction(e -> {
            tanggalField.setValue(null);
            keteranganField.clear();
            jumlahField.clear();
            HalamanUtama halamanUtama = new HalamanUtama(stage, daftarPemasukan, null);
            halamanUtama.halamanUtama(userInfo);
        });
        
        VBox leftFieldVBox = new VBox(10, tanggalField, batas1, keteranganField, batas2, jumlahField);
        leftFieldVBox.setPadding(new Insets(20));
        leftFieldVBox.setAlignment(Pos.CENTER_LEFT);
        
        HBox leftButtonHBox = new HBox(20, batal, submitButton);
        leftButtonHBox.setAlignment(Pos.CENTER);
        leftButtonHBox.setPadding(new Insets(20));
        
        VBox leftMainVBox = new VBox(10, leftFieldVBox, leftButtonHBox);
        leftMainVBox.setAlignment(Pos.CENTER);
        
        StackPane inKotakPane = new StackPane();
        inKotakPane.setAlignment(Pos.CENTER);
        inKotakPane.getChildren().addAll(roundedSquare, leftMainVBox);
        
        // Vbox bagian Kanan.
        
        // Logo
        Image log = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView loImageView = new ImageView(log);
        
        loImageView.setFitHeight(250); // Sesuaikan ukuran logo
        loImageView.setFitWidth(250);
        
        // Membuat teks judul
        Text text = new Text("Input Pemasukan");
        text.setStyle("-fx-font-family: Arial Narrow; -fx-font-size: 24;");

        
        // Membuat tombol kembali ke menu utama
        // Button kembali = new Button("Kembali");
        // kembali.setStyle("-fx-background-color: linear-gradient(to right, #ff66c4,#5170ff); -fx-text-fill: black; -fx-font-size: 12px;");
        // kembali.setPadding(new Insets(5, 20, 5, 20));
        
        // kembali.setOnAction(e -> {
        //     HalamanUtama halamanUtama = new HalamanUtama(stage, daftarPemasukan, null);
        //     halamanUtama.halamanUtama(userInfo);
        // });
 
        // buat Vbox kanan
        VBox textVbox = new VBox(text);
        textVbox.setAlignment(Pos.CENTER);
        
        VBox rightVBox = new VBox(10, textVbox, loImageView);
        rightVBox.setAlignment(Pos.CENTER);
        
        rightVBox.setPadding(new Insets(10));
        
        // satukan kiri kanan
        HBox mainBox = new HBox(20, inKotakPane, rightVBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(20));
        
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(latarImageView, mainBox);
        
        Scene scene = new Scene(stackPane, 800, 400);
        
        // Buat agar ImageView menyesuaikan dengan ukuran Scene
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            latarImageView.setFitWidth((double) newValue);
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            latarImageView.setFitHeight((double) newValue);
        });
        
        scene.getStylesheets().add(getClass().getResource("/Style/styles.css").toExternalForm());
        
        stage.setScene(scene);
        stage.setTitle("MasterCoin");
        stage.show();
    }

    public double getJumlahFieldInput() {
        String jumlahInput = jumlahField.getText();
        if (!jumlahInput.isEmpty()) {
            try {
                return Double.parseDouble(jumlahInput);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Jumlah harus berupa angka.");
                alert.showAndWait();
                return 0;
            }
        } else {
            return 0;
        }
    }
}
