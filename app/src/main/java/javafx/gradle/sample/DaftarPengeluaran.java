package javafx.gradle.sample;

import java.util.ArrayList;
import java.util.List;

import javafx.gradle.sample.config.DataBase;

public class DaftarPengeluaran extends DataHarian {
    private UserInfo userInfo;
    private static double totalPengeluaran;
    private static List<Pengeluaran> pengeluaranList = new ArrayList<>();

    public DaftarPengeluaran(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void daftarPengeluaran() {
        if (pengeluaranList.isEmpty()) {
            System.out.println("Tidak ada pengeluaran");
        } else {
            for (Pengeluaran pengeluaran : pengeluaranList) {
                System.out.println(pengeluaran);
            }
        }
    }

    public void saveHistori(String jenisTransaksi, String tanggal, String keterangan, double jumlah) {
        DataBase.saveTransactionHistory(userInfo.getId(), jenisTransaksi, tanggal, keterangan, jumlah);
    }

    public static double hitungTotalPengeluaran() {
        totalPengeluaran = pengeluaranList.stream().mapToDouble(Pengeluaran::getJumlahInput).sum();
        return totalPengeluaran;
    }

    public void tambahPengeluaran(Pengeluaran pengeluaran) {
        pengeluaranList.add(pengeluaran);
        saveHistori("Pengeluaran", pengeluaran.getTanggalInput(), pengeluaran.getKeteranganInput(), pengeluaran.getJumlahInput());
        DataBase.saveSaldoDompet(userInfo.getId(), SaldoDompet.hitungSaldoDompet(userInfo.getId(), pengeluaran.getJumlahInput(),0 ));
    }

    public static List<Pengeluaran> getPengeluaranList() {
        return pengeluaranList;
    }

    public static class Pengeluaran {
        private String tanggal;
        private String keterangan;
        private double jumlah;

        public Pengeluaran(String tanggal, String keterangan, double jumlah) {
            this.tanggal = tanggal;
            this.keterangan = keterangan;
            this.jumlah = jumlah;
        }

        public String getTanggalInput() {
            return tanggal;
        }

        public String getKeteranganInput() {
            return keterangan;
        }

        public double getJumlahInput() {
            return jumlah;
        }

        @Override
        public String toString() {
            return "Tanggal: " + tanggal + ", Keterangan: " + keterangan + ", Jumlah: " + jumlah;
        }
    }

    @Override
    public void daftarPemasukan() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'daftarPemasukan'");
    }

    
}
