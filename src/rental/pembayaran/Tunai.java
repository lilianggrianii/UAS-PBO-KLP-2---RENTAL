package rental.pembayaran;

import rental.exception.DataTidakValidException;
import rental.util.Tampilan;

/** Pembayaran tunai yang dibayarkan langsung ke admin. */
public class Tunai implements Pembayaran {

    @Override
    public String getNama() {
        return "Tunai";
    }

    @Override
    public void prosesPembayaran(double jumlah) throws DataTidakValidException {
        if (jumlah <= 0) {
            throw new DataTidakValidException("Jumlah pembayaran harus lebih dari 0.");
        }
        System.out.println("[Tunai] Uang tunai " + Tampilan.rupiah(jumlah) + " diterima oleh admin.");
    }
}
