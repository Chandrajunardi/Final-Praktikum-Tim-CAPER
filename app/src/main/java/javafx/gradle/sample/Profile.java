package javafx.gradle.sample;

import javafx.stage.Stage;
import javafx.gradle.sample.config.DataBase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        

        Scene scene = new Scene(akhir, 700, 400);


        stage.setScene(scene);
        stage.setTitle("Profile");
        stage.setResizable(false);
        stage.show();
    }


    public void format(Text text){
        text.setStyle("-fx-text-fill: #ff8210;");
    }
    public Text batas(){
        return new Text("-----------------------------------------------------");
    }


}