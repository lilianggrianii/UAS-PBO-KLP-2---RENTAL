package rental.model;

/** Status sebuah transaksi sewa. */
public enum StatusTransaksi {
    /** Baru dibuat penyewa, menunggu diterima admin. */
    MENUNGGU,
    /** Sudah diterima admin, barang sedang disewa. */
    DISEWA,
    /** Barang sudah dikembalikan dan dikonfirmasi admin. */
    SELESAI,
    /** Ditolak oleh admin. */
    DITOLAK
}
