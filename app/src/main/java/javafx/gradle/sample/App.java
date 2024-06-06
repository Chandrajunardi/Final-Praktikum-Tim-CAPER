package javafx.gradle.sample;


import java.io.FileInputStream;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.gradle.sample.config.DataBase;
import javafx.scene.image.Image;



public class App extends Application {
    private UserInfo userInfo;
    @Override
    public void start(Stage primaryStage) {

        DataBase.createNewTable();
        DaftarPengeluaran daftarPengeluaran = new DaftarPengeluaran(userInfo);
        DaftarPemasukan daftarPemasukan = new DaftarPemasukan(userInfo);

        HalamanLogin login = new HalamanLogin(primaryStage);
        login.halamanLogin();

        
        
        primaryStage.setTitle("MasterKoin");
        // primaryStage.setResizable(true);
        try {
            FileInputStream iconApp = new FileInputStream("src/main/resources/images/Log.png");
            Image logo = new Image(iconApp);
            primaryStage.getIcons().add(logo);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    } 

    public static void main(String[] args) {
        launch(args);
    }
}






        