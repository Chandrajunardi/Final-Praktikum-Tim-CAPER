package javafx.gradle.sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.gradle.sample.config.DataBase;
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


        // Left side content
        Image logo = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView imageView2 = new ImageView(logo);
        imageView2.setFitHeight(150);
        imageView2.setFitWidth(150);

        Text welcomeText = new Text("Atur duit dengan\nciamik bersama\nMasterCoin!");
        welcomeText.setFont(new Font("Arial", 36));
        welcomeText.setFill(Color.web("#121649"));

        Button registerButton = new Button("Daftar");
        registerButton.setPadding(new Insets(5, 20, 5, 20));
        registerButton.setStyle("-fx-background-color: #121619; -fx-text-fill: white; -fx-font-size: 16px;-fx-background-radius: 20;-fx-font-weight: bold;");
        registerButton.setOnAction(e -> {
            Register rg = new Register(stage);
            rg.createContent();
        });

        VBox leftVBox = new VBox(15, imageView2, welcomeText, registerButton);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setPadding(new Insets(6, 20, 30, 50));

        // Right side content

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
        topRectangle.setFill(Color.web("#ffffff", 0.79));


        Text textKanan = new Text("Selamat Datang!");      
        textKanan.setFont(new Font("Arial", 25));
        textKanan.setFill(Color.web("#121649"));

        TextField userTextField = new TextField();
        userTextField.setPromptText(" Username");
        userTextField.setStyle("-fx-background-color: #fafafa; -fx-text-fill: black; -fx-border-width: 2px;-fx-prompt-text-fill: #191d34;");
        userTextField.setMaxWidth(200);
        userTextField.setPrefWidth(200);

        PasswordField passField = new PasswordField();
        passField.setPromptText(" Password");
        passField.setStyle("-fx-background-color: #fafafa; -fx-text-fill: black; -fx-border-width: 2px;-fx-prompt-text-fill: #191d34;");
        passField.setMaxWidth(200);
        passField.setPrefWidth(200);

        Button loginButton = new Button("Masuk");
        loginButton.setStyle("-fx-background-color: #ff8210; -fx-text-fill: white; -fx-font-size: 16px;-fx-background-radius: 20;-fx-font-weight: bold;");
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
                    Home halamanUtama = new Home(stage, daftarPemasukan, daftarPengeluaran);
                    userInfo.setUnserNama(usernameInput);
                    halamanUtama.jalan(userInfo);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username atau password salah.");
                alert.showAndWait();
            }
        });

        VBox inputVBox = new VBox(5, userTextField, batas(), passField, batas());
        inputVBox.setAlignment(Pos.CENTER);

        HBox masuk = new HBox(loginButton);
        masuk.setAlignment(Pos.BASELINE_RIGHT);

        VBox rightVBox = new VBox(15,textKanan, inputVBox, masuk);
        rightVBox.setAlignment(Pos.CENTER);
        rightVBox.setPadding(new Insets(30));

        StackPane stackKanan = new StackPane(bottomRectangle, topRectangle, rightVBox);
        stackKanan.setAlignment(Pos.CENTER_RIGHT);
    

        // Combine left and right content
        HBox root = new HBox(leftVBox, stackKanan);
        root.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane(root);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setStyle("-fx-background-color: #ffffff;");

        Scene scene = new Scene(stackPane, 700, 400);

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

    private UserInfo getUserInfo(String username) {
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

    private Text batas(){
        Text batas2 = new Text("-------------------------------------");
        batas2.setFill(Color.web("#787880"));
        return batas2;
    }
}
