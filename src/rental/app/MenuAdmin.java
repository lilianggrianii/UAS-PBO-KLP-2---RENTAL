package rental.app;

import java.time.LocalDate;
import java.util.List;
import rental.exception.DataTidakValidException;
import rental.exception.RentalException;
import rental.model.Admin;
import rental.model.Barang;
import rental.model.Bus;
import rental.model.DaftarBarang;
import rental.model.Invoice;
import rental.model.Mobil;
import rental.model.Motor;
import rental.model.StatusTransaksi;
import rental.model.Transaksi;
import rental.util.Input;
import rental.util.Tampilan;

/** Menu CLI untuk akun Admin. */
public class MenuAdmin {

    private final Admin admin;
    private final DataRental data;

    /**
     * @param admin akun admin yang sedang login
     * @param data  data aplikasi
     */
    public MenuAdmin(Admin admin, DataRental data) {
        this.admin = admin;
        this.data = data;
    }

    /** Menjalankan loop menu admin sampai admin memilih logout. */
    public void jalankan() {
        boolean jalan = true;
        while (jalan) {
            System.out.println("\n=========== MENU ADMIN (" + admin.getUsername() + ") ===========");
            System.out.println("1. Lihat semua barang");
            System.out.println("2. Tambah barang");
            System.out.println("3. Edit barang");
            System.out.println("4. Hapus barang");
            System.out.println("5. Lihat semua transaksi");
            System.out.println("6. Terima / tolak transaksi");
            System.out.println("7. Konfirmasi pengembalian");
            System.out.println("0. Logout");
            int pilihan = Input.bacaInt("Pilih menu: ");
            try {
                switch (pilihan) {
                    case 1 -> Tampilan.tabelBarang(data.getDaftarBarang().getSemua());
                    case 2 -> tambahBarang();
                    case 3 -> editBarang();
                    case 4 -> hapusBarang();
                    case 5 -> Tampilan.tabelTransaksi(data.getSemuaTransaksi());
                    case 6 -> prosesTransaksi();
                    case 7 -> konfirmasiPengembalian();
                    case 0 -> jalan = false;
                    default -> System.out.println("[!] Menu tidak tersedia.");
                }
            } catch (RentalException e) {
                System.out.println("[!] " + e.getMessage());
            }
        }
    }

    private void tambahBarang() throws RentalException {
        DaftarBarang daftar = data.getDaftarBarang();
        System.out.println("Kategori: 1. Motor  2. Mobil  3. Bus");
        int kategori = Input.bacaInt("Pilih kategori: ");
        if (kategori < 1 || kategori > 3) {
            throw new DataTidakValidException("Kategori tidak valid.");
        }
        String nama = Input.bacaString("Nama kendaraan: ");
        double tarif = Input.bacaDouble("Tarif harian (Rp): ");
        Barang baru;
        switch (kategori) {
            case 1 -> baru = new Motor(daftar.buatId("MTR"), nama, tarif);
            case 2 -> {
                double asuransi = Input.bacaDouble("Biaya asuransi sekali per sewa (Rp): ");
                baru = new Mobil(daftar.buatId("MBL"), nama, tarif, asuransi);
            }
            default -> {
                double sopir = Input.bacaDouble("Biaya sopir per hari (Rp): ");
                baru = new Bus(daftar.buatId("BUS"), nama, tarif, sopir);
            }
        }
        admin.tambahBarang(daftar, baru);
        System.out.println("[OK] Barang ditambahkan dengan ID " + baru.getId());
    }

    private void editBarang() throws RentalException {
        DaftarBarang daftar = data.getDaftarBarang();
        Tampilan.tabelBarang(daftar.getSemua());
        String id = Input.bacaString("ID barang yang diedit: ");
        Barang b = daftar.cariById(id);
        System.out.println("(Kosongkan lalu Enter untuk tidak mengubah)");
        String nama = Input.bacaString("Nama baru [" + b.getNama() + "]: ");
        if (nama.isEmpty()) {
            nama = b.getNama();
        }
        double tarif = Input.bacaDoubleOpsional("Tarif harian baru [" + (long) b.getTarifHarian() + "]: ",
                b.getTarifHarian());
        double tambahan = b.getBiayaTambahan();
        if (b.getLabelBiayaTambahan() != null) {
            tambahan = Input.bacaDoubleOpsional(b.getLabelBiayaTambahan() + " baru ["
                    + (long) b.getBiayaTambahan() + "]: ", b.getBiayaTambahan());
        }
        admin.editBarang(daftar, id, nama, tarif, tambahan);
        System.out.println("[OK] Barang berhasil diedit.");
    }

    private void hapusBarang() throws RentalException {
        Tampilan.tabelBarang(data.getDaftarBarang().getSemua());
        String id = Input.bacaString("ID barang yang dihapus: ");
        admin.hapusBarang(data.getDaftarBarang(), id);
        System.out.println("[OK] Barang dihapus.");
    }

    private void prosesTransaksi() throws RentalException {
        List<Transaksi> menunggu = data.getTransaksiByStatus(StatusTransaksi.MENUNGGU);
        Tampilan.tabelTransaksi(menunggu);
        if (menunggu.isEmpty()) {
            return;
        }
        Transaksi t = data.cariTransaksi(Input.bacaString("ID transaksi: "));
        System.out.println("1. Terima   2. Tolak   0. Batal");
        int pilihan = Input.bacaInt("Pilih: ");
        if (pilihan == 1) {
            Invoice invoice = admin.terimaTransaksi(t, LocalDate.now());
            System.out.println("[OK] Transaksi diterima. Barang berstatus sedang disewa.");
            invoice.cetakInvoice();
        } else if (pilihan == 2) {
            admin.tolakTransaksi(t);
            System.out.println("[OK] Transaksi ditolak.");
        }
    }

    private void konfirmasiPengembalian() throws RentalException {
        List<Transaksi> disewa = data.getTransaksiByStatus(StatusTransaksi.DISEWA);
        Tampilan.tabelTransaksi(disewa);
        if (disewa.isEmpty()) {
            return;
        }
        Transaksi t = data.cariTransaksi(Input.bacaString("ID transaksi yang dikembalikan: "));
        LocalDate tanggal = Input.bacaTanggal("Tanggal kembali (yyyy-MM-dd, Enter = hari ini): ");
        Invoice invoice = admin.konfirmasiKembali(t, tanggal);
        System.out.println("[OK] Pengembalian dikonfirmasi. Barang kembali tersedia.");
        invoice.cetakInvoice();
    }
}
