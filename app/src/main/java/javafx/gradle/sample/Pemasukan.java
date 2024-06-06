package javafx.gradle.sample;

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
        
        // Vbox bagian kiri
        
        Rectangle bottomRectangle = new Rectangle(300, 230);
        bottomRectangle.setArcWidth(20);
        bottomRectangle.setArcHeight(20);
        bottomRectangle.setFill(Color.web("#fcdb39"));

        // Create the second (top) rectangle
        Rectangle topRectangle = new Rectangle(300, 230);
        topRectangle.setArcWidth(20);
        topRectangle.setArcHeight(20);
        topRectangle.setFill(Color.PURPLE);
        topRectangle.setTranslateX(-15);  // Move slightly to the left
        topRectangle.setTranslateY(-15);
        topRectangle.setFill(Color.web("#ffffff", 0.75));

        
        
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
        submitButton.setStyle("-fx-background-color: #ff8210; -fx-text-fill: white; -fx-font-size: 16px;-fx-background-radius: 20;-fx-font-weight: bold;");
        submitButton.setPadding(new Insets(5, 20, 5, 20));

        submitButton.setOnAction(e -> {
            LocalDate tanggalInput = tanggalField.getValue();
            String keteranganInput = keteranganField.getText();
            double jumlahInput = getJumlahFieldInput();



            if (tanggalInput == null || keteranganInput.isEmpty() || jumlahInput <= 0) {
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
        batal.setStyle("-fx-background-color: #191d34; -fx-text-fill: white; -fx-font-size: 16px;-fx-background-radius: 20;-fx-font-weight: bold;");
        batal.setPadding(new Insets(5, 20, 5, 20));

        batal.setOnAction(e -> {
            tanggalField.setValue(null);
            keteranganField.clear();
            jumlahField.clear();
            HalamanUtama halamanUtama = new HalamanUtama(stage, daftarPemasukan, null);
            halamanUtama.halamanUtama(userInfo);
        });
        
        VBox leftFieldVBox = new VBox( tanggalField, batas(), keteranganField, batas(), jumlahField, batas());
        leftFieldVBox.setPadding(new Insets(20));
        leftFieldVBox.setAlignment(Pos.CENTER_LEFT);
        
        HBox leftButtonHBox = new HBox(94, batal, submitButton);
        leftButtonHBox.setAlignment(Pos.CENTER);
        leftButtonHBox.setPadding(new Insets(20));

        StackPane stackPane = new StackPane(bottomRectangle, topRectangle, leftFieldVBox);
        stackPane.setAlignment(Pos.CENTER_LEFT);


        
        VBox leftMainVBox = new VBox(10, stackPane, leftButtonHBox);
        leftMainVBox.setAlignment(Pos.CENTER);
        
        
        // Vbox bagian Kanan.
        
        // Logo
        Image log = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView loImageView = new ImageView(log);
        
        loImageView.setFitHeight(250); // Sesuaikan ukuran logo
        loImageView.setFitWidth(250);
        
        // Membuat teks judul
        Text text = new Text("Input Pemasukan");
        text.setStyle("-fx-font-family: Arial Narrow; -fx-font-size: 24;-fx-font-weight: bold;");
        text.setFill(Color.web("191d34"));

        
        
        VBox rightVBox = new VBox(10, text, loImageView);
        rightVBox.setAlignment(Pos.CENTER);
        
        rightVBox.setPadding(new Insets(10));
        
        // satukan kiri kanan
        HBox mainBox = new HBox(20, leftMainVBox, rightVBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(20));
        
        StackPane over = new StackPane(mainBox);
        over.setAlignment(Pos.CENTER);
        over.setStyle("-fx-background-color: #ffffff;");

        
        Scene scene = new Scene(over, 700, 400);
        
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

    private Text batas(){
        Text batas2 = new Text("-------------------------------------");
        batas2.setFill(Color.web("fa4e1d"));
        return batas2;
    }

  
}
