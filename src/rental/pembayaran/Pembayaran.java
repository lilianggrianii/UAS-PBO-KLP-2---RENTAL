package rental.pembayaran;

import rental.exception.DataTidakValidException;

/**
 * Interface metode pembayaran. Setiap metode (Transfer Bank, QRIS, Tunai, dst)
 * memiliki cara memproses pembayaran sendiri-sendiri (polymorphism).
 */
public interface Pembayaran {

    /**
     * @return nama metode pembayaran yang ditampilkan ke pengguna
     */
    String getNama();

    /**
     * Memproses pembayaran sejumlah tertentu.
     *
     * @param jumlah nominal yang dibayarkan (harus lebih dari 0)
     * @throws DataTidakValidException jika jumlah tidak valid
     */
    void prosesPembayaran(double jumlah) throws DataTidakValidException;
}
