package javafx.gradle.sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home {
    
    Stage stage;

    public Home(Stage stage){
        this.stage = stage;
    }

    public void jalan(){
        // Profil
        Image garisMenu = new Image(getClass().getResourceAsStream("/images/Tap.png"));
        ImageView image = new ImageView(garisMenu);
        image.setFitWidth(35); 
        image.setFitHeight(40);

        // Labels and Buttons
        Text haiNama = new Text("Hai, Jar");
        haiNama.setFont(new Font(20));
        haiNama.setFill(Color.web("#191d34"));
        haiNama.setStyle("-fx-font-weight: bold;");

        Label labelSaldo = new Label("Saldo Rp 100000.0" );
        labelSaldo.setFont(new Font(16));
        labelSaldo.setStyle("-fx-font-family: Arial Narrow;-fx-background-color: #00a650; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        labelSaldo.setPadding(new Insets(4, 20, 4, 20));
        labelSaldo.setAlignment(Pos.BASELINE_LEFT);

        Label pemasukan = new Label("Tambah Pemasukan  ");
        pemasukan.setFont(new Font(16));
        pemasukan.setStyle("-fx-background-color: #6637cd; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        pemasukan.setPadding(new Insets(3, 20, 3, 20));

        Label pengeluaran = new Label("Tambah Pengeluaran");
        pengeluaran.setFont(new Font(16));
        pengeluaran.setStyle("-fx-background-color: #6637cd; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        pengeluaran.setPadding(new Insets(3, 20, 3, 20));

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
}
