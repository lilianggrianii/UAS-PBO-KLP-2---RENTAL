package rental.app;

import java.util.List;
import rental.exception.DataTidakValidException;
import rental.exception.RentalException;
import rental.model.Barang;
import rental.model.Invoice;
import rental.model.Penyewa;
import rental.model.StatusTransaksi;
import rental.model.Transaksi;
import rental.pembayaran.Pembayaran;
import rental.util.Input;
import rental.util.Tampilan;

/** Menu CLI untuk akun Penyewa. */
public class MenuPenyewa {

    private final Penyewa penyewa;
    private final DataRental data;

    /**
     * @param penyewa akun penyewa yang sedang login
     * @param data    data aplikasi
     */
    public MenuPenyewa(Penyewa penyewa, DataRental data) {
        this.penyewa = penyewa;
        this.data = data;
    }

    /** Menjalankan loop menu penyewa sampai penyewa memilih logout. */
    public void jalankan() {
        boolean jalan = true;
        while (jalan) {
            System.out.println("\n=========== MENU PENYEWA (" + penyewa.getUsername() + ") ===========");
            System.out.println("1. Lihat barang tersedia");
            System.out.println("2. Tambah barang ke daftar sewa");
            System.out.println("3. Lihat daftar sewa");
            System.out.println("4. Hapus barang dari daftar sewa");
            System.out.println("5. Buat transaksi sewa");
            System.out.println("6. Status transaksi saya");
            System.out.println("7. Riwayat sewa (transaksi yang sudah diterima)");
            System.out.println("0. Logout");
            int pilihan = Input.bacaInt("Pilih menu: ");
            try {
                switch (pilihan) {
                    case 1 -> penyewa.lihatBarang(data.getDaftarBarang());
                    case 2 -> tambahKeDaftarSewa();
                    case 3 -> lihatDaftarSewa();
                    case 4 -> hapusDariDaftarSewa();
                    case 5 -> buatTransaksi();
                    case 6 -> Tampilan.tabelTransaksi(data.getTransaksiPenyewa(penyewa));
                    case 7 -> lihatRiwayat();
                    case 0 -> jalan = false;
                    default -> System.out.println("[!] Menu tidak tersedia.");
                }
            } catch (RentalException e) {
                System.out.println("[!] " + e.getMessage());
            }
        }
    }

    private void tambahKeDaftarSewa() throws RentalException {
        penyewa.lihatBarang(data.getDaftarBarang());
        String id = Input.bacaString("ID barang yang ingin disewa: ");
        Barang b = data.getDaftarBarang().cariById(id);
        penyewa.getKeranjang().tambahBarang(b);
        System.out.println("[OK] " + b.getNama() + " masuk ke daftar sewa.");
    }

    private void lihatDaftarSewa() {
        List<Barang> isi = penyewa.getKeranjang().getBarang();
        System.out.println("--- Daftar Sewa ---");
        Tampilan.tabelBarang(isi);
    }

    private void hapusDariDaftarSewa() throws RentalException {
        lihatDaftarSewa();
        if (penyewa.getKeranjang().isKosong()) {
            return;
        }
        penyewa.getKeranjang().hapusBarang(Input.bacaString("ID barang yang dihapus: "));
        System.out.println("[OK] Barang dihapus dari daftar sewa.");
    }

    private void buatTransaksi() throws RentalException {
        if (penyewa.getKeranjang().isKosong()) {
            throw new DataTidakValidException("Daftar sewa masih kosong. Tambahkan barang dulu (menu 2).");
        }
        lihatDaftarSewa();
        int lama = Input.bacaInt("Lama sewa (hari): ");
        if (lama < 1) {
            throw new DataTidakValidException("Lama sewa minimal 1 hari.");
        }
        System.out.println("Estimasi total: " + Tampilan.rupiah(penyewa.getKeranjang().hitungEstimasi(lama)));

        List<Pembayaran> metode = data.getMetodePembayaran();
        System.out.println("Metode pembayaran:");
        for (int i = 0; i < metode.size(); i++) {
            System.out.println((i + 1) + ". " + metode.get(i).getNama());
        }
        int pilih = Input.bacaInt("Pilih metode: ");
        if (pilih < 1 || pilih > metode.size()) {
            throw new DataTidakValidException("Metode pembayaran tidak valid.");
        }
        Pembayaran dipilih = metode.get(pilih - 1);

        String yakin = Input.bacaString("Buat transaksi? (y/n): ");
        if (!yakin.equalsIgnoreCase("y")) {
            System.out.println("Dibatalkan.");
            return;
        }
        Transaksi t = penyewa.buatTransaksi(data.buatIdTransaksi(), lama, dipilih);
        data.tambahTransaksi(t);
        System.out.println("[OK] Transaksi " + t.getId() + " dibuat. Menunggu persetujuan admin.");
    }

    private void lihatRiwayat() throws RentalException {
        penyewa.lihatRiwayat();
        if (penyewa.getRiwayat().isEmpty()) {
            return;
        }
        String id = Input.bacaString("Ketik ID transaksi untuk lihat invoice (Enter = kembali): ");
        if (id.isEmpty()) {
            return;
        }
        Transaksi t = data.cariTransaksi(id);
        if (t.getPenyewa() != penyewa || t.getStatus() == StatusTransaksi.MENUNGGU
                || t.getStatus() == StatusTransaksi.DITOLAK) {
            throw new DataTidakValidException("Transaksi tidak ada di riwayat Anda.");
        }
        new Invoice(t).cetakInvoice();
    }
}
