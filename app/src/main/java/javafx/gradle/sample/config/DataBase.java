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
    // private static final String URL = "jdbc:sqlite:C:/javafx-gradle-sample/app/src/main/java/javafx/gradle/sample/db/user.db";
    private static final String URL = "jdbc:sqlite:C:/Users/USER/Desktop/Final-Praktikum-Tim-CAPER/app/src/main/java/javafx/gradle/sample/db/user.db";

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

        String historyPemasukanTableQuery = "CREATE TABLE IF NOT EXISTS history_pemasukan ("
                + "id INTEGER,"
                + "tanggal TEXT,"
                + "keterangan TEXT,"
                + "jumlah DOUBLE"
                + ")";

        String historyPengeluaranTableQuery = "CREATE TABLE IF NOT EXISTS history_pengeluaran ("
                + "id INTEGER,"
                + "tanggal TEXT,"
                + "keterangan TEXT,"
                + "jumlah DOUBLE"
                + ")";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement()) {

            stmt.execute(inputDataTableQuery);
            System.out.println("Tabel 'inputData' berhasil dibuat atau sudah ada.");

            stmt.execute(saldoDompetTableQuery);  
            System.out.println("Tabel 'saldo_dompet' berhasil dibuat atau sudah ada.");

            stmt.execute(usersTableQuery);
            System.out.println("Tabel 'users' berhasil dibuat atau sudah ada.");

            stmt.execute(historyPemasukanTableQuery);
            System.out.println("Tabel 'history_pemasukan' berhasil dibuat atau sudah ada.");

            stmt.execute(historyPengeluaranTableQuery);
            System.out.println("Tabel 'history_pengeluaran' berhasil dibuat atau sudah ada.");



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
    
    public static void savePemasukanHistory(int userId, String tanggal, String keterangan, double jumlah) {
        String insertPemasukanQuery = "INSERT INTO history_pemasukan(id, tanggal, keterangan, jumlah) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertPemasukanQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, tanggal);
            pstmt.setString(3, keterangan);
            pstmt.setDouble(4, jumlah);
            pstmt.executeUpdate();
            System.out.println("Histori pemasukan berhasil disimpan.");
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan histori pemasukan: " + e.getMessage());
        }
    }
    public static void savePengeluaranHistory(int userId, String tanggal, String keterangan, double jumlah) {
        String insertPengeluaranQuery = "INSERT INTO history_pengeluaran(id, tanggal, keterangan, jumlah) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertPengeluaranQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, tanggal);
            pstmt.setString(3, keterangan);
            pstmt.setDouble(4, jumlah);
            pstmt.executeUpdate();
            System.out.println("Histori pengeluaran berhasil disimpan.");
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan histori pengeluaran: " + e.getMessage());
        }
    }
    
    public static List<DaftarPemasukan.Pemasukan> getPemasukanHistory(int userId) {
        String query = "SELECT * FROM history_pemasukan WHERE id = ? ORDER BY tanggal";
        List<DaftarPemasukan.Pemasukan> pemasukanList = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String tanggal = rs.getString("tanggal");
                String keterangan = rs.getString("keterangan");
                double jumlah = rs.getDouble("jumlah");

                DaftarPemasukan.Pemasukan pemasukan = new DaftarPemasukan.Pemasukan(tanggal, keterangan, jumlah);
                pemasukanList.add(pemasukan);
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil histori pemasukan: " + e.getMessage());
        }

        return pemasukanList;
    }

    public static List<DaftarPengeluaran.Pengeluaran> getPengeluaranHistory(int userId) {
        String query = "SELECT * FROM history_pengeluaran WHERE id = ? ORDER BY tanggal";
        List<DaftarPengeluaran.Pengeluaran> pengeluaranList = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String tanggal = rs.getString("tanggal");
                String keterangan = rs.getString("keterangan");
                double jumlah = rs.getDouble("jumlah");

                DaftarPengeluaran.Pengeluaran pengeluaran = new DaftarPengeluaran.Pengeluaran(tanggal, keterangan, jumlah);
                pengeluaranList.add(pengeluaran);
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil histori pengeluaran: " + e.getMessage());
        }

        return pengeluaranList;
    }
    
    public static boolean deleteHistoryPemasukan(int id) {
        String sql = "DELETE FROM history_pemasukan WHERE id = ?";
        try (Connection conn = DataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("gagal menghapus" + e.getMessage());
            return false;
        }
    }
    public static boolean deleteHistoryPengeluaran(int id) {
        String sql = "DELETE FROM history_pengeluaran WHERE id = ?";
        try (Connection conn = DataBase.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("gagal menghapus" + e.getMessage());
            return false;
        }
    }

}
