package rental.model;

import rental.exception.DataTidakValidException;

/** Kategori motor: biaya = tarif harian x lama sewa (tanpa biaya tambahan). */
public class Motor extends Barang {

    /**
     * @param id          ID barang
     * @param nama        nama motor
     * @param tarifHarian tarif per hari
     * @throws DataTidakValidException jika data tidak valid
     */
    public Motor(String id, String nama, double tarifHarian) throws DataTidakValidException {
        super(id, nama, tarifHarian);
    }

    @Override
    public double hitungBiaya(int hari) {
        return getTarifHarian() * hari;
    }

    @Override
    public String getKategori() {
        return "Motor";
    }
}
