package javafx.gradle.sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.checkerframework.checker.units.qual.s;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.gradle.sample.config.DataBase;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
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

        // Title Text
        Text titleText = new Text("Isi Info Kamu");
        titleText.setFont(new Font("Arial", 34));
        titleText.setStyle("-fx-font-weight: bold");

        // TextFields
        TextField nameField = createTextField("Nama");
        TextField emailField = createTextField("Email");
        TextField ageField = createTextField("Umur");

        
        TextField addressField = createTextField("Alamat");
        TextField jobField = createTextField("Pekerjaan");
        
        // Left Side

        // VboxInput
        VBox inputBox = new VBox(nameField,batas(), emailField, batas(), ageField, batas(), addressField, batas(), jobField, batas());
        VBox leftVBox = new VBox(15, titleText, inputBox);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setPadding(new Insets(30, 0, 33, 20));

       
        // Right side button

        Image logo = new Image(getClass().getResourceAsStream("/images/Log.png"));
        ImageView logoImageView = new ImageView(logo);
        logoImageView.setFitWidth(270);
        logoImageView.setFitHeight(265);
        

        // Back Button
        Button backButton = new Button("Kembali");
        backButton.setPadding(new Insets(5, 20, 5, 20));
        backButton.setStyle("-fx-background-color: #121619; -fx-text-fill: white; -fx-font-size: 16px;-fx-background-radius: 20;-fx-font-weight: bold;");


        backButton.setOnAction(e -> {
            HalamanLogin halamanLogin = new HalamanLogin(stage);
            halamanLogin.halamanLogin();
        });

        Button nextButton = new Button("Lanjut");
        nextButton.setStyle("-fx-background-color: #f2291e; -fx-text-fill: white; -fx-font-size: 16px;-fx-background-radius: 20;-fx-font-weight: bold;");
        nextButton.setPadding(new Insets(5, 20, 5, 20));

        nextButton.setOnAction(e -> {
            // Retrieve input data
            String savedNama = nameField.getText();
            String savedEmail = emailField.getText();

            String umurInput = ageField.getText();

            if (!umurInput.isEmpty()) {
                try {
                    double savedUmur = Double.parseDouble(umurInput);
                    if (savedUmur <= 0){
                        showAlert(AlertType.ERROR, "Error Kak", "umur harus positif!");
                        return;
                    }
                } catch (NumberFormatException fe) {
                    showAlert(AlertType.ERROR, "Error Kak", "umur harus berupa angka!");
                    return;
                }
            }

            String savedAlamat = addressField.getText();
            String savedPekerjaan = jobField.getText();
        
            // Validate input data
            if (savedNama.isEmpty() || savedEmail.isEmpty() || umurInput.isEmpty() || savedAlamat.isEmpty() || savedPekerjaan.isEmpty()) {
                showAlert(AlertType.ERROR, "Input Error", "Semua kolom input harus diisi!");
                return;
            }

            if (!savedEmail.matches("^[a-z0-9]+@gmail\\.com$")){
                showAlert(AlertType.ERROR, "Error Input", "Email anda salah!");
                return;
            }

            
        
            boolean isSuccess = false;
            try {
                // Buat tabel jika belum ada
                DataBase.createNewTable();
                // Simpan data ke database
                isSuccess = saveData(savedNama, savedEmail, umurInput, savedAlamat, savedPekerjaan);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        
            if (isSuccess) {
                showAlert(AlertType.INFORMATION, "Data Saved", "Data berhasil disimpan ke database!");
                InputYourProfile inputYourProfile = new InputYourProfile(stage);
                inputYourProfile.inputYourProfile();
            } else {
                showAlert(AlertType.ERROR, "Data Not Saved", "Gagal menyimpan data ke database!");
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
        stackPane.getChildren().addAll( mainHBox);

        Scene scene = new Scene(stackPane, 700, 400);

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

    private Text batas(){
        Text batas1 = new Text("  -------------------------------------------");
        batas1.setFill(Color.web("#fa4e1d"));
        return batas1;

    }


}
