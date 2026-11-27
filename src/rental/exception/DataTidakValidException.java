package rental.exception;

/** Dilempar ketika input atau data yang dimasukkan tidak memenuhi aturan (misal tarif negatif). */
public class DataTidakValidException extends RentalException {

    /**
     * @param pesan penjelasan data yang tidak valid
     */
    public DataTidakValidException(String pesan) {
        super(pesan);
    }
}

