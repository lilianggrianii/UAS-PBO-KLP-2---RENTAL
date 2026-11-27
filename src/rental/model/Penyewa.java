package rental.model;

import java.util.ArrayList;
import java.util.List;
import rental.exception.DataTidakValidException;
import rental.pembayaran.Pembayaran;
import rental.util.Tampilan;

/** Akun penyewa: bisa melihat barang, mengisi daftar sewa, membuat transaksi, dan melihat riwayat. */
public class Penyewa extends Akun {

    private final KeranjangSewa keranjang = new KeranjangSewa();
    private final ArrayList<Transaksi> riwayat = new ArrayList<>();

    /**
     * @param id       ID akun
     * @param username username login
     * @param password password login
     */
    public Penyewa(String id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getRole() {
        return "Penyewa";
    }

    /**
     * Menampilkan barang yang tersedia untuk disewa.
     *
     * @param daftar daftar barang sistem
     */
    public void lihatBarang(DaftarBarang daftar) {
        Tampilan.tabelBarang(daftar.getTersedia());
    }

    /**
     * Membuat transaksi dari seluruh isi daftar sewa. Daftar sewa dikosongkan setelahnya.
     *
     * @param idTransaksi ID untuk transaksi baru
     * @param lamaSewa    lama sewa (hari)
     * @param pembayaran  metode pembayaran
     * @return transaksi baru berstatus MENUNGGU
     * @throws DataTidakValidException jika daftar sewa kosong atau lama sewa tidak valid
     */
    public Transaksi buatTransaksi(String idTransaksi, int lamaSewa, Pembayaran pembayaran)
            throws DataTidakValidException {
        if (keranjang.isKosong()) {
            throw new DataTidakValidException("Daftar sewa masih kosong.");
        }
        Transaksi t = new Transaksi(idTransaksi, this, keranjang.getBarang(), lamaSewa, pembayaran);
        keranjang.kosongkan();
        return t;
    }

    /** Menampilkan riwayat sewa (transaksi yang sudah diterima admin). */
    public void lihatRiwayat() {
        Tampilan.tabelTransaksi(riwayat);
    }

    /**
     * Menambahkan transaksi ke riwayat. Dipanggil admin saat transaksi diterima.
     *
     * @param t transaksi yang diterima
     */
    public void tambahRiwayat(Transaksi t) {
        riwayat.add(t);
    }

    /** @return daftar sewa milik penyewa */
    public KeranjangSewa getKeranjang() {
        return keranjang;
    }

    /** @return salinan riwayat transaksi */
    public List<Transaksi> getRiwayat() {
        return new ArrayList<>(riwayat);
    }
}
