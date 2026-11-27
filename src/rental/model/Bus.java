package rental.model;

import rental.exception.DataTidakValidException;
import rental.util.Tampilan;

/** Kategori bus: biaya = (tarif harian + biaya sopir per hari) x lama sewa. */
public class Bus extends Barang {

    private double biayaSopirPerHari;

    /**
     * @param id                ID barang
     * @param nama              nama bus
     * @param tarifHarian       tarif per hari
     * @param biayaSopirPerHari biaya sopir per hari
     * @throws DataTidakValidException jika data tidak valid
     */
    public Bus(String id, String nama, double tarifHarian, double biayaSopirPerHari)
            throws DataTidakValidException {
        super(id, nama, tarifHarian);
        this.biayaSopirPerHari = validasiBiaya(biayaSopirPerHari);
    }

    @Override
    public double hitungBiaya(int hari) {
        return (getTarifHarian() + biayaSopirPerHari) * hari;
    }

    @Override
    public String getKategori() {
        return "Bus";
    }

    @Override
    public String getKeteranganBiaya() {
        return "Sopir " + Tampilan.rupiah(biayaSopirPerHari) + "/hari";
    }

    @Override
    public String getLabelBiayaTambahan() {
        return "Biaya sopir per hari";
    }

    @Override
    public double getBiayaTambahan() {
        return biayaSopirPerHari;
    }

    @Override
    public void setBiayaTambahan(double biaya) throws DataTidakValidException {
        this.biayaSopirPerHari = validasiBiaya(biaya);
    }
}
