package rental.pembayaran;

import rental.exception.DataTidakValidException;
import rental.util.Tampilan;

/** Pembayaran dengan memindai kode QRIS. */
public class QRIS implements Pembayaran {

    @Override
    public String getNama() {
        return "QRIS";
    }

    @Override
    public void prosesPembayaran(double jumlah) throws DataTidakValidException {
        if (jumlah <= 0) {
            throw new DataTidakValidException("Jumlah pembayaran harus lebih dari 0.");
        }
        System.out.println("[QRIS] Scan QR berhasil, pembayaran " + Tampilan.rupiah(jumlah) + " diterima.");
    }
}
