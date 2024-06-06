package javafx.gradle.sample;

import java.util.ArrayList;
import java.util.List;

import javafx.gradle.sample.config.DataBase;

public class DaftarPemasukan extends DataHarian {
    // private Pemasukan pemasukan;
    private UserInfo userInfo;
    private static double totalPemasukan;
    private static List<Pemasukan> pemasukanList = new ArrayList<>();

    public DaftarPemasukan(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void daftarPemasukan() {
        if (pemasukanList.isEmpty()) {
            System.out.println("Tidak ada pemasukan");
        } else {
            for (Pemasukan pemasukan : pemasukanList) {
                System.out.println(pemasukan);
            }
        }
    }

    public void saveHistori(String jenisTransaksi, String tanggal, String keterangan, double jumlah) {
        DataBase.saveTransactionHistory(userInfo.getId(), jenisTransaksi, tanggal, keterangan, jumlah);
    }

    public static double hitungTotalPemasukan() {
        totalPemasukan = pemasukanList.stream().mapToDouble(Pemasukan::getJumlahInput).sum();
        return totalPemasukan;

    }

    public void tambahPemasukan(Pemasukan pemasukan) {
        pemasukanList.add(pemasukan);
        saveHistori("Pemasukan", pemasukan.getTanggalInput(), pemasukan.getKeteranganInput(), pemasukan.getJumlahInput());
        DataBase.saveSaldoDompet(userInfo.getId(), SaldoDompet.hitungSaldoDompet(userInfo.getId(), 0, pemasukan.getJumlahInput()));
    }

    public static List<Pemasukan> getPemasukanList() {
        return pemasukanList;
    }

    public static class Pemasukan {
        private String tanggal;
        private String keterangan;
        private double jumlah;

        public Pemasukan(String tanggal, String keterangan, double jumlah) {
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
    public void daftarPengeluaran() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'daftarPengeluaran'");
    }



}