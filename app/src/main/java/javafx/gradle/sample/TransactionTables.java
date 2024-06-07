package javafx.gradle.sample;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.gradle.sample.config.DataBase;

public class TransactionTables {
    private TableView<DaftarPemasukan.Pemasukan> pemasukanTable;
    private TableView<DaftarPengeluaran.Pengeluaran> pengeluaranTable;

    public TransactionTables() {
        setupPemasukanTable();
        setupPengeluaranTable();
    }

    private void setupPemasukanTable() {
        pemasukanTable = new TableView<>();
        pemasukanTable.setMaxSize(350, 200);
        pemasukanTable.setStyle("-fx-background-color: #fbfcfc;-fx-font-weight: bold;");

        TableColumn<DaftarPemasukan.Pemasukan, String> tanggalColumn = new TableColumn<>("Tanggal");
        tanggalColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggalInput()));

        TableColumn<DaftarPemasukan.Pemasukan, String> keteranganColumn = new TableColumn<>("Keterangan");
        keteranganColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKeteranganInput()));

        TableColumn<DaftarPemasukan.Pemasukan, Double> jumlahColumn = new TableColumn<>("Jumlah");
        jumlahColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getJumlahInput()).asObject());

        pemasukanTable.getColumns().addAll(tanggalColumn, keteranganColumn, jumlahColumn);

        @SuppressWarnings("unchecked")
        Label historiPlaceholder = new Label("Belum ada histori pemasukan");
        historiPlaceholder.setStyle("-fx-background-color: #F5F5F5;-fx-font-weight: bold;");
        pemasukanTable.setPlaceholder(historiPlaceholder);
    }

    private void setupPengeluaranTable() {
        pengeluaranTable = new TableView<>();
        pengeluaranTable.setMaxSize(350, 200);
        pengeluaranTable.setStyle("-fx-background-color: #fbfcfc;-fx-font-weight: bold;");

        TableColumn<DaftarPengeluaran.Pengeluaran, String> tanggalColumn = new TableColumn<>("Tanggal");
        tanggalColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggalInput()));

        TableColumn<DaftarPengeluaran.Pengeluaran, String> keteranganColumn = new TableColumn<>("Keterangan");
        keteranganColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKeteranganInput()));

        TableColumn<DaftarPengeluaran.Pengeluaran, Double> jumlahColumn = new TableColumn<>("Jumlah");
        jumlahColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getJumlahInput()).asObject());

        pengeluaranTable.getColumns().addAll(tanggalColumn, keteranganColumn, jumlahColumn);

        Label historiPlaceholder = new Label("Belum ada histori pengeluaran");
        historiPlaceholder.setStyle("-fx-background-color: #F5F5F5;-fx-font-weight: bold;");
        pengeluaranTable.setPlaceholder(historiPlaceholder);
    }

    public TableView<DaftarPemasukan.Pemasukan> getPemasukanTable() {
        return pemasukanTable;
    }

    public TableView<DaftarPengeluaran.Pengeluaran> getPengeluaranTable() {
        return pengeluaranTable;
    }

    public VBox getTablesVBox() {
        VBox vbox = new VBox(20, pemasukanTable, pengeluaranTable);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        return vbox;
    }
}
