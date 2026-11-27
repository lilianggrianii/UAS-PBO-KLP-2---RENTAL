package rental.exception;

/** Dilempar ketika data (barang, transaksi, dll) dengan ID tertentu tidak ada. */
public class DataTidakDitemukanException extends RentalException {

    /**
     * @param jenisData nama jenis data, misalnya "Barang" atau "Transaksi"
     * @param id        ID yang dicari
     */
    public DataTidakDitemukanException(String jenisData, String id) {
        super(jenisData + " dengan ID '" + id + "' tidak ditemukan.");
    }
}

