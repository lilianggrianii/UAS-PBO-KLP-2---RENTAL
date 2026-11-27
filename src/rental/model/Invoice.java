package rental.model;

import rental.util.Tampilan;

/** Invoice (struk) dari sebuah transaksi. Dicetak saat transaksi diterima dan saat barang dikembalikan. */
public class Invoice {

    private final Transaksi transaksi;
    private final double total;
    private final double denda;

    /**
     * @param transaksi transaksi yang dibuatkan invoice
     */
    public Invoice(Transaksi transaksi) {
        this.transaksi = transaksi;
        this.total = transaksi.hitungTotal();
        this.denda = transaksi.getDenda();
    }

    /**
     * Menyusun isi invoice sebagai teks (dipakai oleh tampilan CLI maupun GUI).
     *
     * @return teks invoice lengkap
     */
    public String getTeks() {
        StringBuilder sb = new StringBuilder();
        String garis = "=============================================\n";
        String pemisah = "---------------------------------------------\n";
        sb.append(garis).append("                  INVOICE\n").append(garis);
        sb.append("No. Transaksi : ").append(transaksi.getId()).append('\n');
        sb.append("Penyewa       : ").append(transaksi.getPenyewa().getUsername()).append('\n');
        sb.append("Tanggal Sewa  : ").append(transaksi.getTanggalSewa()).append('\n');
        sb.append("Jatuh Tempo   : ").append(transaksi.getTanggalJatuhTempo()).append('\n');
        if (transaksi.getTanggalKembali() != null) {
            sb.append("Tgl Kembali   : ").append(transaksi.getTanggalKembali()).append('\n');
        }
        sb.append("Lama Sewa     : ").append(transaksi.getLamaSewa()).append(" hari\n");
        sb.append("Pembayaran    : ").append(transaksi.getPembayaran().getNama()).append('\n');
        sb.append(pemisah);
        for (Barang b : transaksi.getBarang()) {
            sb.append(String.format("%-8s %-20s %12s%n", b.getId(), b.getNama(),
                    Tampilan.rupiah(b.hitungBiaya(transaksi.getLamaSewa()))));
        }
        sb.append(pemisah);
        sb.append(String.format("%-29s %12s%n", "Total sewa", Tampilan.rupiah(total)));
        sb.append(String.format("%-29s %12s%n", "Denda keterlambatan", Tampilan.rupiah(denda)));
        sb.append(String.format("%-29s %12s%n", "TOTAL BAYAR", Tampilan.rupiah(total + denda)));
        sb.append(garis);
        return sb.toString();
    }

    /** Mencetak invoice ke layar (CLI). */
    public void cetakInvoice() {
        System.out.println();
        System.out.print(getTeks());
    }
}
