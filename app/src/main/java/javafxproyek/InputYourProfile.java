package javafxproyek;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafxproyek.config.DataBase;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

public class InputYourProfile {

    Stage stage;

    public InputYourProfile(Stage stage) {
        this.stage = stage;
    }

    public void inputYourProfile() {

        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/LatarHalamanRegister2.jpg"));
        ImageView imageView = new ImageView(backgroundImage);
        
        // Mengatur ImageView untuk menutupi seluruh area
        imageView.setPreserveRatio(false); // Tidak mempertahankan rasio untuk menutupi seluruh area
        imageView.setFitWidth(800); // Placeholder untuk awal
        imageView.setFitHeight(400); // Placeholder untuk awal

        // bagian Kiri
        // Tambahkan logo
        Image logoImage = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(270); // Sesuaikan ukuran logo
        logoView.setFitWidth(270);

        VBox leftPane = new VBox(logoView);
        leftPane.setAlignment(Pos.CENTER_LEFT);
        leftPane.setPadding(new Insets(20)); 

        // Bagian Kanan
        // Kotak input dan tombol
        Text titleText = new Text("Enter the secret code!"); 
        titleText.setStyle("-fx-font-family: Arial Narrow; -fx-font-size: 24;");
        
       
        titleText.setFill(Color.BLACK);

        TextField usernameField = createTextField("Username");
        TextField passwordField = createTextField("Password");

        Button saveButton = new Button("Simpan ");
        styleButton(saveButton);

        saveButton.setOnAction(e -> {
            String registeredUsername = usernameField.getText();
            String registeredPassword = passwordField.getText();
        
            // Validate input data
            if (registeredUsername.isEmpty() || registeredPassword.isEmpty()) {
                showAlert("Error", "Username dan Password tidak boleh kosong.");
                return;
            }
        
            if (registeredPassword.length() < 8) {
                showAlert("Error", "Password harus lebih dari 8 karakter.");
                return;
            }
        
            if (isUsernameTaken(registeredUsername)) {
                showAlert("Error", "Username sudah ada, silakan pilih username lain.");
            } else {
                if (saveUserToDatabase(registeredUsername, registeredPassword)) {
                    System.out.println("Username terdaftar: " + registeredUsername);
                    System.out.println("Password terdaftar: " + registeredPassword);
                    HalamanLogin login = new HalamanLogin(stage);
                    login.halamanLogin();
                } else {
                    showAlert("Error", "Gagal menyimpan user ke database.");
                }
            }
        });

        Button backButton = new Button("Kembali");
        styleButton(backButton);

        backButton.setOnAction(e -> {
            Register rg = new Register(stage);
            rg.createContent();
        });

        Text batas1 = new Text("-------------------------------------------------");
        batas1.setFill(Color.BLACK);

        VBox inputVBox = new VBox(5, usernameField, batas1, passwordField);

        HBox buttonHBox = new HBox(70, backButton, saveButton);
        buttonHBox.setAlignment(Pos.CENTER_LEFT);

        VBox fieldsVBox = new VBox(20, titleText,inputVBox, buttonHBox);
        fieldsVBox.setAlignment(Pos.CENTER_LEFT);

        HBox boxMain = new HBox(20,leftPane, fieldsVBox);
        boxMain.setAlignment(Pos.CENTER);

        // Main layout
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, boxMain);

        Scene scene = new Scene(stackPane, 800, 400);

        // Buat agar ImageView menyesuaikan dengan ukuran Scene
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitWidth((double) newValue);
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitHeight((double) newValue);
        });

        stage.setScene(scene);
        stage.setTitle("Akun");
        stage.show();
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-width: 2px; -fx-prompt-text-fill: black;");
        textField.setMaxWidth(200);
        textField.setPrefWidth(200);
        return textField;
    }

    private void styleButton(Button button) {
        button.setFont(new Font(24));
        button.setStyle("-fx-background-color: linear-gradient(to right, #5170ff, #ff66c4); -fx-text-fill: black; -fx-font-size: 12px;");
        button.setPadding(new Insets(5, 20, 5, 20));
    }

    private boolean saveUserToDatabase(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?,?)";

        try (Connection conn = DataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = DataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
