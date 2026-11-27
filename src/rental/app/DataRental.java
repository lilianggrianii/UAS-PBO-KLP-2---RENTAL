package rental.app;

import java.util.ArrayList;
import java.util.List;
import rental.exception.DataTidakDitemukanException;
import rental.exception.DataTidakValidException;
import rental.exception.LoginGagalException;
import rental.model.Admin;
import rental.model.Akun;
import rental.model.Bus;
import rental.model.DaftarBarang;
import rental.model.Mobil;
import rental.model.Motor;
import rental.model.Penyewa;
import rental.model.StatusTransaksi;
import rental.model.Transaksi;
import rental.pembayaran.Pembayaran;
import rental.pembayaran.QRIS;
import rental.pembayaran.TransferBank;
import rental.pembayaran.Tunai;

/**
 * Pusat data aplikasi (disimpan di memori): akun, barang, transaksi,
 * dan daftar metode pembayaran. Berisi data awal agar sistem langsung bisa dicoba.
 */
public class DataRental {

    private final ArrayList<Akun> daftarAkun = new ArrayList<>();
    private final DaftarBarang daftarBarang = new DaftarBarang();
    private final ArrayList<Transaksi> semuaTransaksi = new ArrayList<>();
    private final ArrayList<Pembayaran> metodePembayaran = new ArrayList<>();
    private int counterTransaksi = 0;

    /**
     * Membuat data awal: 1 admin, 2 penyewa, 7 kendaraan, dan 3 metode pembayaran.
     *
     * @throws DataTidakValidException jika data awal tidak valid
     */
    public DataRental() throws DataTidakValidException {
        daftarAkun.add(new Admin("A01", "admin", "admin123"));
        daftarAkun.add(new Penyewa("P01", "budi", "budi123"));
        daftarAkun.add(new Penyewa("P02", "siti", "siti123"));

        metodePembayaran.add(new TransferBank());
        metodePembayaran.add(new QRIS());
        metodePembayaran.add(new Tunai());

        daftarBarang.tambahBarang(new Motor(daftarBarang.buatId("MTR"), "Honda Beat", 80000));
        daftarBarang.tambahBarang(new Motor(daftarBarang.buatId("MTR"), "Yamaha NMAX", 120000));
        daftarBarang.tambahBarang(new Mobil(daftarBarang.buatId("MBL"), "Toyota Avanza", 350000, 75000));
        daftarBarang.tambahBarang(new Mobil(daftarBarang.buatId("MBL"), "Honda Brio", 300000, 75000));
        daftarBarang.tambahBarang(new Mobil(daftarBarang.buatId("MBL"), "Mitsubishi Xpander", 400000, 100000));
        daftarBarang.tambahBarang(new Bus(daftarBarang.buatId("BUS"), "Bus Medium 30 Seat", 1500000, 500000));
        daftarBarang.tambahBarang(new Bus(daftarBarang.buatId("BUS"), "Bus Big 50 Seat", 2500000, 700000));
    }

    /**
     * Mencari akun yang cocok dengan username dan password.
     *
     * @param username username yang dimasukkan
     * @param password password yang dimasukkan
     * @return akun yang berhasil login (Admin atau Penyewa)
     * @throws LoginGagalException jika tidak ada akun yang cocok
     */
    public Akun login(String username, String password) throws LoginGagalException {
        for (Akun a : daftarAkun) {
            if (a.login(username, password)) {
                return a;
            }
        }
        throw new LoginGagalException();
    }

    /**
     * Login dengan memastikan peran akun sesuai pilihan pengguna di menu awal.
     *
     * @param username username yang dimasukkan
     * @param password password yang dimasukkan
     * @param peran    peran yang dipilih ("Admin" atau "Penyewa")
     * @return akun yang berhasil login
     * @throws LoginGagalException jika kredensial salah atau akun bukan berperan sesuai pilihan
     */
    public Akun login(String username, String password, String peran) throws LoginGagalException {
        Akun akun = login(username, password);
        if (!akun.getRole().equals(peran)) {
            throw new LoginGagalException("Akun '" + username + "' bukan akun " + peran + ".");
        }
        return akun;
    }

    /**
     * Membuat ID transaksi baru, contoh T001.
     *
     * @return ID transaksi
     */
    public String buatIdTransaksi() {
        counterTransaksi++;
        return "T" + String.format("%03d", counterTransaksi);
    }

    /**
     * Menyimpan transaksi baru ke sistem.
     *
     * @param t transaksi baru
     */
    public void tambahTransaksi(Transaksi t) {
        semuaTransaksi.add(t);
    }

    /**
     * Mencari transaksi berdasarkan ID.
     *
     * @param id ID transaksi
     * @return transaksi yang ditemukan
     * @throws DataTidakDitemukanException jika tidak ada
     */
    public Transaksi cariTransaksi(String id) throws DataTidakDitemukanException {
        for (Transaksi t : semuaTransaksi) {
            if (t.getId().equalsIgnoreCase(id)) {
                return t;
            }
        }
        throw new DataTidakDitemukanException("Transaksi", id);
    }

    /**
     * Mengambil transaksi dengan status tertentu.
     *
     * @param status status yang dicari
     * @return daftar transaksi berstatus tersebut
     */
    public List<Transaksi> getTransaksiByStatus(StatusTransaksi status) {
        List<Transaksi> hasil = new ArrayList<>();
        for (Transaksi t : semuaTransaksi) {
            if (t.getStatus() == status) {
                hasil.add(t);
            }
        }
        return hasil;
    }

    /**
     * Mengambil semua transaksi milik satu penyewa (semua status).
     *
     * @param p penyewa
     * @return daftar transaksi penyewa tersebut
     */
    public List<Transaksi> getTransaksiPenyewa(Penyewa p) {
        List<Transaksi> hasil = new ArrayList<>();
        for (Transaksi t : semuaTransaksi) {
            if (t.getPenyewa() == p) {
                hasil.add(t);
            }
        }
        return hasil;
    }

    /** @return salinan semua transaksi */
    public List<Transaksi> getSemuaTransaksi() {
        return new ArrayList<>(semuaTransaksi);
    }

    /** @return daftar barang sistem */
    public DaftarBarang getDaftarBarang() {
        return daftarBarang;
    }

    /** @return salinan daftar metode pembayaran */
    public List<Pembayaran> getMetodePembayaran() {
        return new ArrayList<>(metodePembayaran);
    }
}
