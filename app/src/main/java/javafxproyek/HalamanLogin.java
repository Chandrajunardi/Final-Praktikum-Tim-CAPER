package javafxproyek;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafxproyek.config.DataBase;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HalamanLogin {
    
    Stage stage;


    public HalamanLogin(Stage stage) {
        this.stage = stage;
    }

    public void halamanLogin() {
        
        // Background
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/LatarHalamanLogin.jpg"));
        ImageView imageView = new ImageView(backgroundImage);
        
        // Mengatur ImageView untuk menutupi seluruh area
        imageView.setPreserveRatio(false); // Tidak mempertahankan rasio untuk menutupi seluruh area
        imageView.setFitWidth(800); // Placeholder untuk awal
        imageView.setFitHeight(400); // Placeholder untuk awal

        // Rectangle for the main container
        Rectangle kotak = new Rectangle(750, 370);
        kotak.setArcHeight(20);
        kotak.setArcWidth(20);
        kotak.setFill(Color.web("#fa4e1d", 0.29));


        // Left side content

        Image logo = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView imageView2 = new ImageView(logo);
        imageView2.setFitHeight(100);
        imageView2.setFitWidth(100);

        Text welcomeText = new Text("Atur duit\ndengan ciamik\nbersama masterCoin!");
        welcomeText.setFont(new Font("Arial", 36));
        welcomeText.setFill(Color.web("#fbfcfc"));

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: linear-gradient(to right, #ff66c4, #ffde59); -fx-text-fill: white; -fx-font-size: 16px;");
        registerButton.setPadding(new Insets(10, 20, 10, 20));

        registerButton.setOnAction(e -> {
            Register rg = new Register(stage);
            rg.createContent();
        });

        VBox leftVBox = new VBox(10, imageView2, welcomeText, registerButton);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setPadding(new Insets(30, 20, 30, 50));

        // Right side content
        Label welcomeLabel = new Label("Welcome!");
        welcomeLabel.setFont(new Font("Arial", 24));
        welcomeLabel.setTextFill(Color.web("#5e17eb"));
        
        Text batas1 = new Text("------------------------------");
        batas1.setFill(Color.BLACK);
        
        Text batas2 = new Text("------------------------------");
        batas2.setFill(Color.BLACK);

        TextField userTextField = new TextField();
        userTextField.setPromptText("Username");
        userTextField.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-width: 2px; -fx-prompt-text-fill: white;");
        userTextField.setMaxWidth(200);
        userTextField.setPrefWidth(200);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle("-fx-background-color: transparent; -fx-text-fill: black;-fx-border-width: 2px; -fx-prompt-text-fill: white;");
        passField.setMaxWidth(200);
        passField.setPrefWidth(200);


        Button loginButton = new Button("Log in");
        loginButton.setStyle("-fx-background-color: linear-gradient(to right, #8c52ff, #00bf63); -fx-text-fill: white; -fx-font-size: 16px;");
        loginButton.setPadding(new Insets(5, 20, 5, 20));

        loginButton.setOnAction(e -> {
            String usernameInput = userTextField.getText();
            String passwordInput = passField.getText();

            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR, "Username atau password tidak boleh kosong.");
                alert1.show();
            }

            if (checkCredentials(usernameInput, passwordInput)) {
                System.out.println("Login berhasil!");
                UserInfo userInfo = getUserInfo(usernameInput);
                if (userInfo != null) {
                    DaftarPengeluaran daftarPengeluaran = new DaftarPengeluaran(userInfo);
                    DaftarPemasukan daftarPemasukan = new DaftarPemasukan(userInfo);
                    HalamanUtama halamanUtama = new HalamanUtama(stage, daftarPemasukan, daftarPengeluaran);
                    userInfo.setUnserNama(usernameInput);
                    halamanUtama.halamanUtama(userInfo);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username atau password salah.");
                alert.showAndWait();
            }
        });

        VBox inputVBox = new VBox(5, userTextField, batas1, passField, batas2);

        VBox rightVBox = new VBox(10, welcomeLabel, inputVBox, loginButton);
        rightVBox.setAlignment(Pos.CENTER);
        rightVBox.setPadding(new Insets(30));

        // Creating the container with background color
        Rectangle kotak2 = new Rectangle(300, 200);
        kotak2.setArcHeight(20);
        kotak2.setArcWidth(20);
        

        
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0, // Mulai dari kiri ke kanan
            true,       // Proporsional
            CycleMethod.NO_CYCLE, // Tanpa pengulangan
            new Stop(0, Color.web("#ff66c4", 0.38)), // Warna awal
            new Stop(1, Color.web("#ffde59", 0.38))  // Warna akhir
        );

        
        kotak2.setFill(gradient);

        StackPane dalamKotak = new StackPane(kotak2, rightVBox);
        dalamKotak.setMaxSize(300, 200);
        dalamKotak.setAlignment(Pos.CENTER);
        dalamKotak.setPadding(new Insets(30, 70, 30, 0));

        // Combine left and right content
        HBox root = new HBox(leftVBox, dalamKotak);
        root.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane(imageView, kotak, root);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setStyle("-fx-background-color: #b088f9;");

        Scene scene = new Scene(stackPane, 800, 400);

        // Buat agar ImageView menyesuaikan dengan ukuran Scene
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitWidth((double) newValue);
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitHeight((double) newValue);
        });

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    private boolean checkCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private UserInfo getUserInfo(String username)  {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                return new UserInfo(id, username);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; 
    }
}
