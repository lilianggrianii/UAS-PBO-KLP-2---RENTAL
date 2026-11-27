package rental.exception;

/** Dilempar ketika barang yang diminta sedang disewa atau tidak boleh diproses. */
public class BarangTidakTersediaException extends RentalException {

    /**
     * @param pesan penjelasan mengapa barang tidak tersedia
     */
    public BarangTidakTersediaException(String pesan) {
        super(pesan);
    }
}

