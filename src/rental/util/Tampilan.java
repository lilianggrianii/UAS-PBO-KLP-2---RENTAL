package rental.util;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import rental.model.Barang;
import rental.model.Transaksi;

/** Kumpulan fungsi bantu untuk menampilkan data ke layar CLI. */
public final class Tampilan {

    private static final NumberFormat FORMAT = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));

    private Tampilan() {
    }

    /**
     * Memformat angka menjadi rupiah, contoh: Rp150.000.
     *
     * @param nilai nominal
     * @return teks rupiah
     */
    public static String rupiah(double nilai) {
        return "Rp" + FORMAT.format(Math.round(nilai));
    }

    /**
     * Menampilkan tabel barang.
     *
     * @param daftar barang yang ditampilkan
     */
    public static void tabelBarang(List<Barang> daftar) {
        if (daftar.isEmpty()) {
            System.out.println("(Tidak ada barang)");
            return;
        }
        String garis = "-".repeat(92);
        System.out.println(garis);
        System.out.printf("%-8s %-20s %-8s %-13s %-28s %-9s%n",
                "ID", "Nama", "Kategori", "Tarif/Hari", "Biaya Tambahan", "Status");
        System.out.println(garis);
        for (Barang b : daftar) {
            System.out.printf("%-8s %-20s %-8s %-13s %-28s %-9s%n",
                    b.getId(), b.getNama(), b.getKategori(), rupiah(b.getTarifHarian()),
                    b.getKeteranganBiaya(), b.isTersedia() ? "Tersedia" : "Disewa");
        }
        System.out.println(garis);
    }

    /**
     * Menampilkan tabel transaksi.
     *
     * @param daftar transaksi yang ditampilkan
     */
    public static void tabelTransaksi(List<Transaksi> daftar) {
        if (daftar.isEmpty()) {
            System.out.println("(Tidak ada transaksi)");
            return;
        }
        String garis = "-".repeat(96);
        System.out.println(garis);
        System.out.printf("%-6s %-10s %-22s %-5s %-9s %-13s %-12s%n",
                "ID", "Penyewa", "Barang", "Hari", "Status", "Total", "Jatuh Tempo");
        System.out.println(garis);
        for (Transaksi t : daftar) {
            StringBuilder ids = new StringBuilder();
            for (Barang b : t.getBarang()) {
                if (ids.length() > 0) {
                    ids.append(",");
                }
                ids.append(b.getId());
            }
            System.out.printf("%-6s %-10s %-22s %-5d %-9s %-13s %-12s%n",
                    t.getId(), t.getPenyewa().getUsername(), ids, t.getLamaSewa(), t.getStatus(),
                    rupiah(t.hitungTotal()),
                    t.getTanggalJatuhTempo() == null ? "-" : t.getTanggalJatuhTempo().toString());
        }
        System.out.println(garis);
    }
}
