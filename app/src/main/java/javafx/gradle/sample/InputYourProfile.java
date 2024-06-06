package javafx.gradle.sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.gradle.sample.config.DataBase;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

        Rectangle bottomRectangle = new Rectangle(300, 200);
        bottomRectangle.setArcWidth(20);
        bottomRectangle.setArcHeight(20);
        bottomRectangle.setFill(Color.web("#fcdb39"));

        // Create the second (top) rectangle
        Rectangle topRectangle = new Rectangle(300, 200);
        topRectangle.setArcWidth(20);
        topRectangle.setArcHeight(20);
        topRectangle.setTranslateX(-15);  // Move slightly to the left
        topRectangle.setTranslateY(-15);
        topRectangle.setFill(Color.web("#ffffff"));

        Text titleText = new Text("Masukan username dan password!"); 
        titleText.setFont(new Font("Arial", 18));
        titleText.setFill(Color.web("#121649"));
        titleText.setStyle("-fx-font-weight: bold;");

        TextField userTextField = new TextField();
        userTextField.setPromptText("Username");
        userTextField.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-width: 2px; -fx-prompt-text-fill: #191d34;");
        userTextField.setMaxWidth(200);
        userTextField.setPrefWidth(200);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-width: 2px; -fx-prompt-text-fill: #191d34;");
        passField.setMaxWidth(200);
        passField.setPrefWidth(200);

        Button saveButton = new Button("Simpan ");
        saveButton.setStyle("-fx-background-color: #f2291e; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 20; -fx-font-weight: bold;");
        saveButton.setPadding(new Insets(5, 20, 5, 20));

        saveButton.setOnAction(e -> {
            String registeredUsername = userTextField.getText();
            String registeredPassword = passField.getText();
        
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
        backButton.setPadding(new Insets(5, 20, 5, 20));
        backButton.setStyle("-fx-background-color: #121619; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 20; -fx-font-weight: bold;");

        backButton.setOnAction(e -> {
            Register rg = new Register(stage);
            rg.createContent();
        });

        VBox inputVBox = new VBox(5, userTextField, batas(), passField, batas());
        
        inputVBox.setPadding(new Insets(30));
    

        HBox buttonHBox = new HBox(94,backButton, saveButton);
        buttonHBox.setAlignment(Pos.BASELINE_RIGHT);


        StackPane stackKanan = new StackPane(bottomRectangle, topRectangle,inputVBox );
        stackKanan.setAlignment(Pos.CENTER);


        VBox text = new VBox(15, titleText,stackKanan,buttonHBox );
        text.setAlignment(Pos.CENTER_RIGHT);

        HBox boxMain = new HBox(20, leftPane,text);
        boxMain.setAlignment(Pos.CENTER);

        // Main layout
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(boxMain);
        stackPane.setStyle("-fx-background-color: #ffffff;");


        Scene scene = new Scene(stackPane, 700, 400);

        stage.setScene(scene);
        stage.setTitle("Akun");
        stage.show();
    }

    private Text batas() {
        Text batas1 = new Text("------------------------------------------");
        batas1.setFill(Color.web("#fa4e1d"));
        return batas1;
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
