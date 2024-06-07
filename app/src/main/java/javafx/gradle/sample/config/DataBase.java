package javafx.gradle.sample.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.gradle.sample.DaftarPemasukan;
import javafx.gradle.sample.DaftarPengeluaran;
import javafx.gradle.sample.UserInfo;

public class DataBase {
    // private static final String URL = "jdbc:sqlite:C:/Users/USER/Documents/Final-Praktikum-Tim-CAPER/javaFXProyek/app/src/main/java/javafxproyek/db/user.db";
    private static final String URL = "jdbc:sqlite:C:/javafx-gradle-sample/app/src/main/java/javafx/gradle/sample/db/user.db";
    static UserInfo userInfo;

    // Metode untuk menyambungkan ke database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Koneksi ke database berhasil!");
        } catch (SQLException e) {
            System.out.println("Gagal menyambung ke database: " + e.getMessage());
        }
        return conn;
    }

    // Metode untuk membuat tabel-tabel baru di database
    public static void createNewTable() {
        String historyTableQuery = "CREATE TABLE IF NOT EXISTS history ("
                + "id INTEGER,"
                + "jenis TEXT,"
                + "tanggal TEXT,"
                + "keterangan TEXT,"
                + "jumlah DOUBLE"
                + ")";
                
        String inputDataTableQuery = "CREATE TABLE IF NOT EXISTS inputData ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nama TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "umur TEXT NOT NULL,"
                + "alamat TEXT NOT NULL,"
                + "pekerjaan TEXT NOT NULL"
                + ")";

        String saldoDompetTableQuery = "CREATE TABLE IF NOT EXISTS saldo_dompet ("
                + "id INTEGER PRIMARY KEY,"
                + "saldo DOUBLE"
                + ")";

        String usersTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL,"
                + "password TEXT NOT NULL"
                + ")";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(historyTableQuery);
            System.out.println("Tabel 'history' berhasil dibuat atau sudah ada.");

            stmt.execute(inputDataTableQuery);
            System.out.println("Tabel 'inputData' berhasil dibuat atau sudah ada.");

            stmt.execute(saldoDompetTableQuery);  
            System.out.println("Tabel 'saldo_dompet' berhasil dibuat atau sudah ada.");

            stmt.execute(usersTableQuery);
            System.out.println("Tabel 'users' berhasil dibuat atau sudah ada.");

        } catch (SQLException e) {
            System.out.println("Gagal membuat tabel: " + e.getMessage());
        }
    }

    // Metode untuk menyimpan saldo ke tabel saldo_dompet
    public static void saveSaldoDompet(int userId, double saldo) {
        String insertSaldoQuery = "INSERT INTO saldo_dompet(id, saldo) VALUES (?, ?) "
                                + "ON CONFLICT(id) DO UPDATE SET saldo=?";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(insertSaldoQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setDouble(2, saldo);
            pstmt.setDouble(3, saldo);
            pstmt.executeUpdate();
            System.out.println("Saldo dompet berhasil disimpan atau diperbarui.");
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan saldo dompet: " + e.getMessage());
        }
    }

    // Metode untuk menyimpan histori transaksi ke tabel history
    public static void saveTransactionHistory(int id, String jenis, String tanggal, String keterangan, double jumlah) {
        String insertHistoryQuery = "INSERT INTO history(id, jenis, tanggal, keterangan, jumlah) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(insertHistoryQuery)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, jenis);
            pstmt.setString(3, tanggal);
            pstmt.setString(4, keterangan);
            pstmt.setDouble(5, jumlah);
            pstmt.executeUpdate();
            System.out.println("Histori transaksi berhasil disimpan.");
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan histori transaksi: " + e.getMessage());
        }
    }
    
    public static List<Object> getTransactionHistory(int userId) {
        String query = "SELECT * FROM history WHERE id = ? ORDER BY tanggal";
        List<Object> historyList = new ArrayList<>();
    
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                String jenis = rs.getString("jenis");
                String tanggal = rs.getString("tanggal");
                String keterangan = rs.getString("keterangan");
                double jumlah = rs.getDouble("jumlah");
    
                if ("Pemasukan".equals(jenis)) {
                    DaftarPemasukan.Pemasukan pemasukan = new DaftarPemasukan.Pemasukan(tanggal, keterangan, jumlah);
                    historyList.add(pemasukan);
                } else if ("Pengeluaran".equals(jenis)) {
                    DaftarPengeluaran.Pengeluaran pengeluaran = new DaftarPengeluaran.Pengeluaran(tanggal, keterangan, jumlah);
                    historyList.add(pengeluaran);
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil histori transaksi: " + e.getMessage());
        }
    
        return historyList;
    }
    public static boolean deleteHistoryByIdAndType(int userId, String type) {
        String sql = "DELETE FROM history WHERE user_id = ? AND type = ?";
        try (Connection conn = DataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, type);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    

}
