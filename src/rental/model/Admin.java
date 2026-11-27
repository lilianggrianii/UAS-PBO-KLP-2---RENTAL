package rental.model;

import java.time.LocalDate;
import rental.exception.BarangTidakTersediaException;
import rental.exception.DataTidakDitemukanException;
import rental.exception.DataTidakValidException;
import rental.exception.RentalException;

/** Akun admin: mengelola barang, menerima transaksi, dan mengonfirmasi pengembalian. */
public class Admin extends Akun {

    /**
     * @param id       ID akun
     * @param username username login
     * @param password password login
     */
    public Admin(String id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    /**
     * Menambah barang baru ke sistem.
     *
     * @param daftar daftar barang sistem
     * @param b      barang baru
     */
    public void tambahBarang(DaftarBarang daftar, Barang b) {
        daftar.tambahBarang(b);
    }

    /**
     * Mengedit barang.
     *
     * @param daftar   daftar barang sistem
     * @param id       ID barang
     * @param nama     nama baru
     * @param tarif    tarif harian baru
     * @param tambahan biaya tambahan baru
     * @throws DataTidakDitemukanException jika ID tidak ada
     * @throws DataTidakValidException     jika data tidak valid
     */
    public void editBarang(DaftarBarang daftar, String id, String nama, double tarif, double tambahan)
            throws DataTidakDitemukanException, DataTidakValidException {
        daftar.editBarang(id, nama, tarif, tambahan);
    }

    /**
     * Menghapus barang.
     *
     * @param daftar daftar barang sistem
     * @param id     ID barang
     * @throws DataTidakDitemukanException  jika ID tidak ada
     * @throws BarangTidakTersediaException jika barang sedang disewa
     */
    public void hapusBarang(DaftarBarang daftar, String id)
            throws DataTidakDitemukanException, BarangTidakTersediaException {
        daftar.hapusBarang(id);
    }

    /**
     * Menerima transaksi: barang menjadi "sedang disewa", pembayaran diproses,
     * transaksi masuk ke riwayat penyewa, lalu invoice dibuat.
     *
     * @param t       transaksi yang diterima
     * @param tanggal tanggal mulai sewa
     * @return invoice transaksi
     * @throws RentalException jika transaksi bukan berstatus MENUNGGU atau ada barang yang sudah disewa orang lain
     */
    public Invoice terimaTransaksi(Transaksi t, LocalDate tanggal) throws RentalException {
        if (t.getStatus() != StatusTransaksi.MENUNGGU) {
            throw new DataTidakValidException("Transaksi " + t.getId() + " tidak berstatus MENUNGGU.");
        }
        for (Barang b : t.getBarang()) {
            if (!b.isTersedia()) {
                throw new BarangTidakTersediaException("Barang '" + b.getNama()
                        + "' sudah disewa penyewa lain. Sebaiknya tolak transaksi ini.");
            }
        }
        t.getPembayaran().prosesPembayaran(t.hitungTotal());
        for (Barang b : t.getBarang()) {
            b.setTersedia(false);
        }
        t.mulaiSewa(tanggal);
        t.getPenyewa().tambahRiwayat(t);
        return new Invoice(t);
    }

    /**
     * Menolak transaksi yang masih menunggu.
     *
     * @param t transaksi yang ditolak
     * @throws DataTidakValidException jika transaksi tidak berstatus MENUNGGU
     */
    public void tolakTransaksi(Transaksi t) throws DataTidakValidException {
        if (t.getStatus() != StatusTransaksi.MENUNGGU) {
            throw new DataTidakValidException("Transaksi " + t.getId() + " tidak berstatus MENUNGGU.");
        }
        t.ubahStatus(StatusTransaksi.DITOLAK);
    }

    /**
     * Mengonfirmasi pengembalian: menghitung denda, memproses pembayaran denda
     * (jika ada), dan mengembalikan status barang menjadi tersedia.
     *
     * @param t       transaksi yang dikembalikan
     * @param tanggal tanggal pengembalian
     * @return invoice akhir (berisi denda jika terlambat)
     * @throws RentalException jika transaksi tidak berstatus DISEWA atau tanggal tidak valid
     */
    public Invoice konfirmasiKembali(Transaksi t, LocalDate tanggal) throws RentalException {
        if (t.getStatus() != StatusTransaksi.DISEWA) {
            throw new DataTidakValidException("Transaksi " + t.getId() + " tidak sedang disewa.");
        }
        t.selesaikan(tanggal);
        if (t.getDenda() > 0) {
            t.getPembayaran().prosesPembayaran(t.getDenda());
        }
        for (Barang b : t.getBarang()) {
            b.setTersedia(true);
        }
        return new Invoice(t);
    }
}
