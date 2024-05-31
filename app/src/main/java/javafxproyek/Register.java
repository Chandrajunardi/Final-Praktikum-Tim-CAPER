package javafxproyek;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafxproyek.config.DataBase;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

public class Register {

    Stage stage;

    public Register(Stage stage) {
        this.stage = stage;
    }

    public void createContent() {

        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/LatarHalamanRegister1.jpg"));
        ImageView imageView = new ImageView(backgroundImage);
        
        // Mengatur ImageView untuk menutupi seluruh area
        imageView.setPreserveRatio(false); // Tidak mempertahankan rasio untuk menutupi seluruh area
        imageView.setFitWidth(800); // Placeholder untuk awal
        imageView.setFitHeight(400); // Placeholder untuk awal

        // Title Text
        Text titleText = new Text("Isi Info Kamu");
        titleText.setFont(new Font("Arial", 34));

        // TextFields
        TextField nameField = createTextField("Nama");
        TextField emailField = createTextField("Email");
        TextField ageField = createTextField("Umur");

        
        TextField addressField = createTextField("Alamat");
        TextField jobField = createTextField("Pekerjaan");

        // BataxText
        Text batas1 = new Text("----------------------------------");
        batas1.setFill(Color.BLACK);
        Text batas2 = new Text("----------------------------------");
        batas2.setFill(Color.BLACK);
        Text batas3 = new Text("----------------------------------");
        batas3.setFill(Color.BLACK);
        Text batas4 = new Text("----------------------------------");
        batas4.setFill(Color.BLACK);
        Text batas5 = new Text("----------------------------------");
        batas5.setFill(Color.BLACK);

        
        // Left Side

        // VboxInput
        VBox inputBox = new VBox(5, nameField,batas1, emailField, batas2,ageField, batas3, addressField,batas4, jobField,batas5);
        VBox leftVBox = new VBox(10, titleText, inputBox);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setPadding(new Insets(30, 0, 33, 20));

       
        // Right side button

        Image logo = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView logoImageView = new ImageView(logo);
        logoImageView.setFitWidth(270);
        logoImageView.setFitHeight(265);
        

        // Back Button
        Button backButton = new Button("Kembali");
        styleButton(backButton);

        backButton.setOnAction(e -> {
            HalamanLogin halamanLogin = new HalamanLogin(stage);
            halamanLogin.halamanLogin();
        });

        Button nextButton = new Button("Lanjut");
        styleButton(nextButton);

        nextButton.setOnAction(e -> {
            // Retrieve input data
            String savedNama = nameField.getText();
            String savedEmail = emailField.getText();
            String savedUmur = ageField.getText();
            String savedAlamat = addressField.getText();
            String savedPekerjaan = jobField.getText();
        
            // Validate input data
            if (savedNama.isEmpty() || savedEmail.isEmpty() || savedUmur.isEmpty() || savedAlamat.isEmpty() || savedPekerjaan.isEmpty()) {
                showAlert(AlertType.ERROR, "Input Error", "Semua kolom input harus diisi.");
                return;
            }
        
            boolean isSuccess = false;
            try {
                // Buat tabel jika belum ada
                DataBase.createNewTable();
                // Simpan data ke database
                isSuccess = saveData(savedNama, savedEmail, savedUmur, savedAlamat, savedPekerjaan);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        
            if (isSuccess) {
                showAlert(AlertType.INFORMATION, "Data Saved", "Data berhasil disimpan ke database.");
                InputYourProfile inputYourProfile = new InputYourProfile(stage);
                inputYourProfile.inputYourProfile();
            } else {
                showAlert(AlertType.ERROR, "Data Not Saved", "Gagal menyimpan data ke database.");
            }
        });

        // Vibox kanan
        HBox rightHbox = new HBox(20, backButton, nextButton);
        rightHbox.setAlignment(Pos.CENTER);
        VBox rightVBox = new VBox(logoImageView, rightHbox);
        rightVBox.setAlignment(Pos.CENTER_RIGHT);
        rightVBox.setPadding(new Insets(30, 0, 30, 20));

        // Main layout

        
        HBox mainHBox = new HBox(30,leftVBox, rightVBox);
        mainHBox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, mainHBox);

        Scene scene = new Scene(stackPane, 800, 400);

        // Buat agar ImageView menyesuaikan dengan ukuran Scene
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitWidth((double) newValue);
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitHeight((double) newValue);
        });

        stage.setScene(scene);
        stage.setTitle("Register");
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
        button.setFont(new Font("Arial", 24));
        button.setStyle("-fx-background-color: linear-gradient(to right, #5170ff, #ff66c4); -fx-text-fill: black; -fx-font-size: 12px;");
        button.setPadding(new Insets(5, 20, 5, 20));
    }

    private boolean saveData(String nama, String email, String umur, String alamat, String pekerjaan) throws SQLException {
        String sql = "INSERT INTO inputData (nama, email, umur, alamat, pekerjaan) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nama);
            pstmt.setString(2, email);
            pstmt.setString(3, umur);
            pstmt.setString(4, alamat);
            pstmt.setString(5, pekerjaan);
    
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
