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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Histori {
    Stage stage;

    public Histori(Stage stage){
        this.stage = stage;
    }

    public void histori(){

        Image home = new Image(getClass().getResourceAsStream("/images/HomeIcon.png"));
        ImageView images = new ImageView(home);
        images.setFitWidth(35); 
        images.setFitHeight(35);
        

        VBox image = new VBox(images);
        image.setAlignment(Pos.TOP_LEFT);

        Label histori = new Label("Histori...");
        histori.setFont(new Font(16));
        histori.setStyle("-fx-font-family: Arial Narrow;-fx-background-color: #191d34; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        histori.setPadding(new Insets(4, 20, 4, 20));
        histori.setAlignment(Pos.TOP_CENTER);


        Label hapus = new Label("Hapus");
        hapus.setFont(new Font(16));
        hapus.setStyle("-fx-font-family: Arial Narrow;-fx-background-color: #ff0000; -fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 20;");
        hapus.setPadding(new Insets(4, 20, 4, 20));
        hapus.setAlignment(Pos.BASELINE_LEFT);

        Rectangle bottomRectangle = new Rectangle(630, 270);
        bottomRectangle.setArcWidth(20);
        bottomRectangle.setArcHeight(20);
        bottomRectangle.setFill(Color.web("#ff0000", 0.3));

        HBox mainTop = new HBox(200,image, histori, hapus);
        mainTop.setPadding(new Insets(20));
        mainTop.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(20, mainTop, bottomRectangle);
        mainBox.setAlignment(Pos.CENTER);


        Scene scene = new Scene(mainBox, 700, 400);

        stage.setScene(scene);
        stage.setTitle("Halaman Utama");
        stage.show();
    }
}
