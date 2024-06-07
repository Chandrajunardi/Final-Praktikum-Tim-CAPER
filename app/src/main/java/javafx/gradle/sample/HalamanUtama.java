// package javafx.gradle.sample;

// import javafx.beans.property.SimpleDoubleProperty;
// import javafx.beans.property.SimpleStringProperty;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.ButtonType;
// import javafx.scene.control.Label;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.StackPane;
// import javafx.scene.layout.VBox;
// import javafx.scene.paint.Color;
// import javafx.scene.shape.Rectangle;
// import javafx.scene.text.Font;
// import javafx.scene.text.Text;
// import javafx.stage.Stage;
// import javafx.gradle.sample.config.DataBase;
// import java.util.List;

// public class HalamanUtama {
//     String nama;
//     Stage stage;
//     UserInfo userInfo;
//     Pemasukan pemasukan;
//     Pengeluaran pengeluaran;
//     DaftarPengeluaran daftarPengeluaran;
//     DaftarPemasukan daftarPemasukan;
//     TableView<Object> tableView;
//     TableColumn<Object, String> jenisTransaksiColumn;
//     TableColumn<Object, String> tanggalColumn;
//     TableColumn<Object, String> keteranganColumn;
//     TableColumn<Object, Double> jumlahColumn;

//     public HalamanUtama(Stage stage, DaftarPemasukan daftarPemasukan, DaftarPengeluaran daftarPengeluaran) {
//         this.stage = stage;
//         this.daftarPemasukan = daftarPemasukan;
//         this.daftarPengeluaran = daftarPengeluaran;
//     }

//     public void setupTable() {
//         tableView = new TableView<>();
//         tableView.setMaxSize(350, 200);
//         tableView.setStyle("-fx-background-color: #fbfcfc;-fx-font-weight: bold;");

//         jenisTransaksiColumn = new TableColumn<>("Jenis Transaksi");
//         jenisTransaksiColumn.setCellValueFactory(data -> {
//             if (data.getValue() instanceof DaftarPemasukan.Pemasukan) {
//                 return new SimpleStringProperty("Pemasukan");
//             } else {
//                 return new SimpleStringProperty("Pengeluaran");
//             }
//         });

//         tanggalColumn = new TableColumn<>("Tanggal");
//         tanggalColumn.setCellValueFactory(data -> {
//             if (data.getValue() instanceof DaftarPemasukan.Pemasukan) {
//                 return new SimpleStringProperty(((DaftarPemasukan.Pemasukan) data.getValue()).getTanggalInput());
//             } else {
//                 return new SimpleStringProperty(((DaftarPengeluaran.Pengeluaran) data.getValue()).getTanggalInput());
//             }
//         });

//         keteranganColumn = new TableColumn<>("Keterangan");
//         keteranganColumn.setCellValueFactory(data -> {
//             if (data.getValue() instanceof DaftarPemasukan.Pemasukan) {
//                 return new SimpleStringProperty(((DaftarPemasukan.Pemasukan) data.getValue()).getKeteranganInput());
//             } else {
//                 return new SimpleStringProperty(((DaftarPengeluaran.Pengeluaran) data.getValue()).getKeteranganInput());
//             }
//         });

//         jumlahColumn = new TableColumn<>("Jumlah");
//         jumlahColumn.setCellValueFactory(data -> {
//             if (data.getValue() instanceof DaftarPemasukan.Pemasukan) {
//                 return new SimpleDoubleProperty(((DaftarPemasukan.Pemasukan) data.getValue()).getJumlahInput()).asObject();
//             } else {
//                 return new SimpleDoubleProperty(((DaftarPengeluaran.Pengeluaran) data.getValue()).getJumlahInput()).asObject();
//             }
//         });

//         tableView.getColumns().addAll(jenisTransaksiColumn, tanggalColumn, keteranganColumn, jumlahColumn);
//     }
//     @SuppressWarnings("unchecked")

//     public void halamanUtama(UserInfo userInfo) {
        
//         setupTable();

//        // Buat LatarBelakang
//         Image latar = new Image(getClass().getResourceAsStream("/images/LatarHalamanUtama.png"));
//         ImageView imageView = new ImageView(latar);
//         // Mengatur ImageView untuk menutupi seluruh area
//         imageView.setPreserveRatio(false); // Tidak mempertahankan rasio untuk menutupi seluruh area
//         imageView.setFitWidth(800); // Placeholder untuk awal
//         imageView.setFitHeight(400); // Placeholder untuk awal

//         Rectangle kotak = new Rectangle(650, 350);
//         kotak.setArcHeight(20);
//         kotak.setArcWidth(20);
//         kotak.setFill(Color.web("#7b51eb", 0.4));

//         // Profil
//         Image profileImage = new Image(getClass().getResourceAsStream("/images/IconProfil.png"));

//         ImageView image = new ImageView(profileImage);

//         image.setFitWidth(50); 
//         image.setFitHeight(50); 

//         image.setOnMouseClicked(e -> {
//         Profile profilePage = new Profile(stage, userInfo.getId(), userInfo, daftarPemasukan, daftarPengeluaran);
//         profilePage.showProfile();
//         });
     
   
//         // Labels and Buttons
//         Text haiNama = new Text("Hai, " + userInfo.getUsername());
//         haiNama.setFont(new Font(20));
//         haiNama.setFill(Color.BLACK);

   
//         // Label untuk menampilkan jumlah saldo
//         double saldoDouble = SaldoDompet.ambilSaldoDariDatabase(userInfo.getId());
//         String saldoString = String.valueOf(saldoDouble);

//         Button labelSaldo = new Button("Saldo Rp " + saldoString);
//         labelSaldo.setFont(new Font(16));
//         labelSaldo.setPadding(new Insets(5, 50, 5, 25));
//         labelSaldo.setStyle("-fx-font-family: Arial Narrow;-fx-background-color: linear-gradient(to right, #ff3131, #ff914d); -fx-text-fill: black;");
        
//         labelSaldo.setAlignment(Pos.BASELINE_LEFT);


//         Button pengeluaran = new Button("Add Pengeluaran");
//         pengeluaran.setFont(new Font(16));
//         pengeluaran.setStyle("-fx-background-color: linear-gradient(to right, #5170ff, #ff66c4); -fx-text-fill: white;");
//         pengeluaran.setMaxSize(200, 40);

//         pengeluaran.setOnAction(e -> {
//                 double saldoDompet = SaldoDompet.ambilSaldoDariDatabase(userInfo.getId());
//             if (saldoDompet == 0) {
//                 Alert alert3 = new Alert(Alert.AlertType.INFORMATION, "Belum ada pemasukan");
//                 alert3.show();
//             } else {
//                 Pengeluaran pengeluaranPage = new Pengeluaran(stage, daftarPemasukan, daftarPengeluaran, userInfo);
//                 pengeluaranPage.pengeluaran();
//             }
//         });


//         Button pemasukan = new Button("Add Pemasukan");
//         pemasukan.setFont(new Font(16));
//         pemasukan.setStyle("-fx-background-color: linear-gradient(to right, #5170ff, #ff66c4); -fx-text-fill: white;");
//         pemasukan.setMaxSize(200, 40);

//         pemasukan.setOnAction(e -> {
//             Pemasukan pemasukanPage = new Pemasukan(stage, daftarPemasukan, daftarPengeluaran, userInfo);
//             pemasukanPage.pemasukan();
//         });
        
//         // Left Vbox
//         VBox leftVBox = new VBox(25, image, haiNama, labelSaldo, pemasukan, pengeluaran);
//         leftVBox.setAlignment(Pos.CENTER_LEFT);
//         leftVBox.setPadding(new Insets(20)); 

//         // Bagian Kanan
//         Image logo = new Image(getClass().getResourceAsStream("/images/Log.png"));
//         ImageView logoView = new ImageView(logo);
        
//         logoView.setFitWidth(100); 
//         logoView.setFitHeight(100); 

//         Button hapus = new Button("Hapus");
//         hapus.setFont(new Font(16));
//         hapus.setPadding(new Insets(3, 20, 3, 20));
//         hapus.setStyle("-fx-background-color: linear-gradient(to right, #ff5757, #8c52ff); -fx-text-fill: black;");

        
        
//         // TableView for History
//         TableView<Object> tableView = new TableView<>();
//         tableView.setMaxSize(350, 170);
//         tableView.setStyle("-fx-background-color: linear-gradient(to right, #5170ff, #ff66c4);-fx-font-weight: bold;");
//         tableView.setOpacity(0.55);

//         TableColumn<Object, String> jenisTransaksiColumn = new TableColumn<>("Jenis Transaksi");
//         jenisTransaksiColumn.setCellValueFactory(data -> {
//             if (data.getValue() instanceof DaftarPemasukan.Pemasukan) {
//                 return new SimpleStringProperty("Pemasukan");
//             } else {
//                 return new SimpleStringProperty("Pengeluaran");
//             }
//         });

//         TableColumn<Object, String> tanggalColumn = new TableColumn<>("Tanggal");
//         tanggalColumn.setCellValueFactory(data -> {
//             if (data.getValue() instanceof DaftarPemasukan.Pemasukan) {
//                 return new SimpleStringProperty(((DaftarPemasukan.Pemasukan) data.getValue()).getTanggalInput());
//             } else {
//                 return new SimpleStringProperty(((DaftarPengeluaran.Pengeluaran) data.getValue()).getTanggalInput());
//             }
//         });

//         TableColumn<Object, String> keteranganColumn = new TableColumn<>("Keterangan");
//         keteranganColumn.setCellValueFactory(data -> {
//             if (data.getValue() instanceof DaftarPemasukan.Pemasukan) {
//                 return new SimpleStringProperty(((DaftarPemasukan.Pemasukan) data.getValue()).getKeteranganInput());
//             } else {
//                 return new SimpleStringProperty(((DaftarPengeluaran.Pengeluaran) data.getValue()).getKeteranganInput());
//             }
//         });

//         TableColumn<Object, Double> jumlahColumn = new TableColumn<>("Jumlah");
//         jumlahColumn.setCellValueFactory(data -> {
//             if (data.getValue() instanceof DaftarPemasukan.Pemasukan) {
//                 return new SimpleDoubleProperty(((DaftarPemasukan.Pemasukan) data.getValue()).getJumlahInput()).asObject();
//             } else {
//                 return new SimpleDoubleProperty(((DaftarPengeluaran.Pengeluaran) data.getValue()).getJumlahInput()).asObject();
//             }
//         });
        
//         tableView.getColumns().addAll(jenisTransaksiColumn, tanggalColumn, keteranganColumn, jumlahColumn);
        
        

//         // Placeholder for TableView
//         Label historiPlaceholder = new Label("Belum ada histori");
//         historiPlaceholder.setStyle("-fx-background-color: #F5F5F5;-fx-font-weight: bold;");
//         tableView.setPlaceholder(historiPlaceholder);


//         List<Object> historyList = DataBase.getTransactionHistory(userInfo.getId());
//         tableView.getItems().addAll(historyList);
         
        
//         hapus.setOnAction(e -> {
//             // Konfirmasi pengguna sebelum menghapus
//             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//             alert.setTitle("Konfirmasi");
//             alert.setHeaderText("Hapus Semua Riwayat");
//             alert.setContentText("Apakah Anda yakin ingin menghapus semua riwayat transaksi?");
        
//             java.util.Optional<ButtonType> result = alert.showAndWait();
//             if (result.isPresent() && result.get() == ButtonType.OK) {
//                 // Hapus semua item dari tabel
//                 tableView.getItems().clear();
//                 // Hapus semua riwayat dari database
//                 DataBase.deleteHistoryById(userInfo.getId());
//             }
//         });

//         // Vboc kanan
//         VBox logVBox = new VBox(logoView);
//         logVBox.setAlignment(Pos.CENTER_RIGHT);


//         VBox rightVBox = new VBox(10, logVBox, tableView, hapus);
//         rightVBox.setAlignment(Pos.CENTER);

//         // Gabung kiri kanan

//         HBox mainBox = new HBox(20, leftVBox, rightVBox);
//         mainBox.setAlignment(Pos.CENTER);

//         StackPane dalamPane = new StackPane();
//         dalamPane.setAlignment(Pos.CENTER);
//         dalamPane.getChildren().addAll(kotak, mainBox);

//         StackPane stackPane = new StackPane();
//         stackPane.getChildren().addAll(imageView, dalamPane);

//         Scene scene = new Scene(stackPane, 800, 400);

//         // Buat agar ImageView menyesuaikan dengan ukuran Scene
//         scene.widthProperty().addListener((observable, oldValue, newValue) -> {
//             imageView.setFitWidth((double) newValue);
//         });
//         scene.heightProperty().addListener((observable, oldValue, newValue) -> {
//             imageView.setFitHeight((double) newValue);
//         });

//         stage.setScene(scene);
//         stage.setTitle("Halaman Utama");
//         stage.show();
//     }    
// }