package rental.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import rental.exception.DataTidakValidException;
import rental.pembayaran.Pembayaran;

/** Satu transaksi sewa yang berisi satu atau lebih barang. */
public class Transaksi {

    private final String id;
    private final Penyewa penyewa;
    private final ArrayList<Barang> barang;
    private final int lamaSewa;
    private final Pembayaran pembayaran;
    private LocalDate tanggalSewa;
    private LocalDate tanggalJatuhTempo;
    private LocalDate tanggalKembali;
    private StatusTransaksi status = StatusTransaksi.MENUNGGU;
    private double denda = 0;

    /**
     * @param id         ID transaksi
     * @param penyewa    penyewa yang membuat transaksi
     * @param barang     barang yang disewa
     * @param lamaSewa   lama sewa dalam hari (minimal 1)
     * @param pembayaran metode pembayaran yang dipilih
     * @throws DataTidakValidException jika lama sewa kurang dari 1
     */
    public Transaksi(String id, Penyewa penyewa, List<Barang> barang, int lamaSewa, Pembayaran pembayaran)
            throws DataTidakValidException {
        if (lamaSewa < 1) {
            throw new DataTidakValidException("Lama sewa minimal 1 hari.");
        }
        this.id = id;
        this.penyewa = penyewa;
        this.barang = new ArrayList<>(barang);
        this.lamaSewa = lamaSewa;
        this.pembayaran = pembayaran;
    }

    /**
     * Menghitung total biaya sewa semua barang (tanpa denda).
     *
     * @return total biaya sewa
     */
    public double hitungTotal() {
        double total = 0;
        for (Barang b : barang) {
            total += b.hitungBiaya(lamaSewa);
        }
        return total;
    }

    /**
     * Menghitung denda jika barang dikembalikan melewati jatuh tempo.
     * Denda = jumlah hari terlambat x total denda per hari seluruh barang.
     *
     * @param tanggalPengembalian tanggal barang dikembalikan
     * @return nominal denda (0 jika tidak terlambat)
     */
    public double hitungDenda(LocalDate tanggalPengembalian) {
        long hariTerlambat = ChronoUnit.DAYS.between(tanggalJatuhTempo, tanggalPengembalian);
        if (hariTerlambat <= 0) {
            return 0;
        }
        double dendaPerHari = 0;
        for (Barang b : barang) {
            dendaPerHari += b.getDendaPerHari();
        }
        return dendaPerHari * hariTerlambat;
    }

    /**
     * Menandai sewa dimulai pada tanggal tertentu dan menghitung jatuh tempo.
     *
     * @param tanggal tanggal mulai sewa
     */
    public void mulaiSewa(LocalDate tanggal) {
        this.tanggalSewa = tanggal;
        this.tanggalJatuhTempo = tanggal.plusDays(lamaSewa);
        this.status = StatusTransaksi.DISEWA;
    }

    /**
     * Menyelesaikan transaksi saat barang dikembalikan dan menyimpan dendanya.
     *
     * @param tanggal tanggal pengembalian
     * @throws DataTidakValidException jika tanggal sebelum tanggal sewa
     */
    public void selesaikan(LocalDate tanggal) throws DataTidakValidException {
        if (tanggal.isBefore(tanggalSewa)) {
            throw new DataTidakValidException("Tanggal kembali tidak boleh sebelum tanggal sewa (" + tanggalSewa + ").");
        }
        this.tanggalKembali = tanggal;
        this.denda = hitungDenda(tanggal);
        this.status = StatusTransaksi.SELESAI;
    }

    /**
     * Mengubah status transaksi.
     *
     * @param status status baru
     */
    public void ubahStatus(StatusTransaksi status) {
        this.status = status;
    }

    /** @return ID transaksi */
    public String getId() { return id; }

    /** @return penyewa pemilik transaksi */
    public Penyewa getPenyewa() { return penyewa; }

    /** @return salinan daftar barang yang disewa */
    public List<Barang> getBarang() { return new ArrayList<>(barang); }

    /** @return lama sewa (hari) */
    public int getLamaSewa() { return lamaSewa; }

    /** @return metode pembayaran */
    public Pembayaran getPembayaran() { return pembayaran; }

    /** @return tanggal mulai sewa (null jika belum diterima) */
    public LocalDate getTanggalSewa() { return tanggalSewa; }

    /** @return tanggal jatuh tempo (null jika belum diterima) */
    public LocalDate getTanggalJatuhTempo() { return tanggalJatuhTempo; }

    /** @return tanggal pengembalian (null jika belum dikembalikan) */
    public LocalDate getTanggalKembali() { return tanggalKembali; }

    /** @return status transaksi */
    public StatusTransaksi getStatus() { return status; }

    /** @return denda yang tercatat (0 jika belum selesai / tidak terlambat) */
    public double getDenda() { return denda; }
}
