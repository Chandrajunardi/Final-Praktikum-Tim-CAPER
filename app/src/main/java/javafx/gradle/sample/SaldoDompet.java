package javafx.gradle.sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.gradle.sample.config.DataBase;

public class SaldoDompet {

    public static double ambilSaldoDariDatabase(int userId) {
        String query = "SELECT saldo FROM saldo_dompet WHERE id = ?";
        double saldo = 0.0;
        
        try (Connection conn = DataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                saldo = rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil saldo dari database: " + e.getMessage());
        }
        
        return saldo;
    }

    public static double hitungSaldoDompet(int userId, double totalPengeluaran, double totalPemasukan) {
        double saldoDompet = ambilSaldoDariDatabase(userId);
        System.out.println("Saldo sebelum: " + saldoDompet);

        saldoDompet += totalPemasukan - totalPengeluaran;

        System.out.println("Total Pemasukan: " + totalPemasukan + ", Total Pengeluaran: " + totalPengeluaran);
        System.out.println("Saldo setelah: " + saldoDompet);
        
        DataBase.saveSaldoDompet(userId, saldoDompet);
        // saldoDompet = ambilSaldoDariDatabase(userId);

        return saldoDompet;
    }

    public static double getSaldoDompet(double saldoDompet) {
        return saldoDompet;
    }
}
