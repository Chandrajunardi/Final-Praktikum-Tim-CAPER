package javafx.gradle.sample;

import java.util.ArrayList;
import java.util.List;

public class History {
    public static List<Object> gabungkanDanUrutkan(List<?> daftarPemasukan, List<?> daftarPengeluaran) {
        List<Object> semuaHistory = new ArrayList<>(daftarPemasukan);
        semuaHistory.addAll(daftarPengeluaran);
        semuaHistory.sort((t1, t2) -> {
            String tanggal1 = (t1 instanceof DaftarPemasukan.Pemasukan) ? ((DaftarPemasukan.Pemasukan) t1).getTanggalInput() : ((DaftarPengeluaran.Pengeluaran) t1).getTanggalInput();
            String tanggal2 = (t2 instanceof DaftarPemasukan.Pemasukan) ? ((DaftarPemasukan.Pemasukan) t2).getTanggalInput() : ((DaftarPengeluaran.Pengeluaran) t2).getTanggalInput();
            return tanggal2.compareTo(tanggal1);
        });
        return semuaHistory;
    }
}


