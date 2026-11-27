package rental.pembayaran;

import rental.exception.DataTidakValidException;
import rental.util.Tampilan;

/** Pembayaran melalui transfer ke rekening bank rental. */
public class TransferBank implements Pembayaran {

    @Override
    public String getNama() {
        return "Transfer Bank";
    }

    @Override
    public void prosesPembayaran(double jumlah) throws DataTidakValidException {
        if (jumlah <= 0) {
            throw new DataTidakValidException("Jumlah pembayaran harus lebih dari 0.");
        }
        System.out.println("[Transfer Bank] Pembayaran " + Tampilan.rupiah(jumlah)
                + " diterima ke rekening BCA 1234567890 a.n. Rental Kendaraan.");
    }
}
