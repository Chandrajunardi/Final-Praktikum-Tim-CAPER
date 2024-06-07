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

public class Pengeluaran {
    Stage stage;
    private UserInfo userInfo;
    private DaftarPemasukan daftarPemasukan;
    private DaftarPengeluaran daftarPengeluaran;

    private DatePicker tanggal;
    private TextField keterangan;
    private TextField jumlah;

    public Pengeluaran(Stage stage, DaftarPemasukan daftarPemasukan, DaftarPengeluaran daftarPengeluaran, UserInfo userInfo) {
        this.stage = stage;
        this.daftarPemasukan = daftarPemasukan != null ? daftarPemasukan : new DaftarPemasukan(userInfo);
        this.daftarPengeluaran = daftarPengeluaran != null ? daftarPengeluaran : new DaftarPengeluaran(userInfo);
        this.userInfo = userInfo;
    }

    public void pengeluaran() {
        Rectangle bottomRectangle = new Rectangle(300, 230);
        bottomRectangle.setArcWidth(20);
        bottomRectangle.setArcHeight(20);
        bottomRectangle.setFill(Color.web("#fcdb39"));

        Rectangle topRectangle = new Rectangle(300, 230);
        topRectangle.setArcWidth(20);
        topRectangle.setArcHeight(20);
        topRectangle.setFill(Color.web("#ffffff", 0.75));
        topRectangle.setTranslateX(-15); 
        topRectangle.setTranslateY(-15);

        tanggal = new DatePicker();
        tanggal.setPromptText("Pilih tanggal");
        tanggal.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: black;" +
            "-fx-border-width: 2px;" +
            "-fx-prompt-text-fill: black;"
        );

        keterangan = new TextField();
        keterangan.setPromptText("Masukkan keterangan");
        keterangan.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-width: 2px; -fx-prompt-text-fill: black;");

        jumlah = new TextField();
        jumlah.setPromptText("Masukkan jumlah");
        jumlah.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-width: 2px; -fx-prompt-text-fill: black;");

        Button submitButton = new Button("Simpan");
        submitButton.setStyle("-fx-background-color: #ff8210; -fx-text-fill: white; -fx-font-size: 16px;-fx-background-radius: 20;-fx-font-weight: bold;");
        submitButton.setPadding(new Insets(5, 20, 5, 20));

        submitButton.setOnAction(e -> {
            LocalDate tanggalInput = tanggal.getValue();
            String keteranganInput = keterangan.getText();
            double jumlahInput = getJumlahInput();

            if (tanggalInput == null || keteranganInput.isEmpty() || jumlahInput <= 0) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Data tidak boleh kosong");
                alert1.showAndWait();
            } else {
                double saldoDouble = SaldoDompet.ambilSaldoDariDatabase(userInfo.getId());
                if (jumlahInput > saldoDouble ) {
                    Alert alert4 = new Alert(Alert.AlertType.INFORMATION, "Pengeluaran melebihi limit");
                    alert4.showAndWait();
                    tanggal.setValue(null);
                    keterangan.clear();
                    jumlah.clear();
                } else {
                    DaftarPengeluaran.Pengeluaran pengeluaran = new DaftarPengeluaran.Pengeluaran(tanggalInput.toString(), keteranganInput, jumlahInput);
                    daftarPengeluaran.tambahPengeluaran(pengeluaran);

                    tanggal.setValue(null);
                    keterangan.clear();
                    jumlah.clear();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data berhasil disimpan.");
                    alert.showAndWait();
                    HalamanUtama halamanUtama = new HalamanUtama(stage, daftarPemasukan, daftarPengeluaran);
                    halamanUtama.halamanUtama(userInfo);
                }
            }
        });

        Button batal = new Button("Batal");
        batal.setStyle("-fx-background-color: #191d34; -fx-text-fill: white; -fx-font-size: 16px;-fx-background-radius: 20;-fx-font-weight: bold;");
        batal.setPadding(new Insets(5, 20, 5, 20));

        batal.setOnAction(e -> {
            tanggal.setValue(null);
            keterangan.clear();
            jumlah.clear();
            HalamanUtama halamanUtama = new HalamanUtama(stage, null, daftarPengeluaran);
            halamanUtama.halamanUtama(userInfo);
        });

        VBox leftFieldVBox = new VBox(10, tanggal, batas(), keterangan, batas(), jumlah, batas());
        leftFieldVBox.setPadding(new Insets(20));
        leftFieldVBox.setAlignment(Pos.CENTER_LEFT);

        HBox leftButtonHBox = new HBox(20, batal, submitButton);
        leftButtonHBox.setAlignment(Pos.CENTER);
        leftButtonHBox.setPadding(new Insets(20));

        StackPane stackPane = new StackPane(bottomRectangle, topRectangle, leftFieldVBox);
        stackPane.setAlignment(Pos.CENTER_LEFT);

        VBox leftMainVBox = new VBox(10, stackPane, leftButtonHBox);
        leftMainVBox.setAlignment(Pos.CENTER);

        Image log = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView loImageView = new ImageView(log);
        loImageView.setFitHeight(250); 
        loImageView.setFitWidth(250);

        Text text = new Text("Input Pengeluaran");
        text.setStyle("-fx-font-family: Arial Narrow; -fx-font-size: 24;-fx-font-weight: bold;");
        text.setFill(Color.web("191d34"));

        VBox rightVBox = new VBox(10, text, loImageView);
        rightVBox.setAlignment(Pos.CENTER);
        rightVBox.setPadding(new Insets(10));

        HBox mainBox = new HBox(20, leftMainVBox, rightVBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(20));

        StackPane root = new StackPane(mainBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ffffff;");

        Scene scene = new Scene(root, 700, 400);
        scene.getStylesheets().add(getClass().getResource("/Style/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("MasterCoin");
        stage.show();
    }

    public double getJumlahInput() {
        String jumlahInput = jumlah.getText();
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
