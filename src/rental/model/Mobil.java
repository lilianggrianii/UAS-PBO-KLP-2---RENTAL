package rental.model;

import rental.exception.DataTidakValidException;
import rental.util.Tampilan;

/** Kategori mobil: biaya = (tarif harian x lama sewa) + biaya asuransi (sekali per sewa). */
public class Mobil extends Barang {

    private double biayaAsuransi;

    /**
     * @param id            ID barang
     * @param nama          nama mobil
     * @param tarifHarian   tarif per hari
     * @param biayaAsuransi biaya asuransi sekali bayar per transaksi
     * @throws DataTidakValidException jika data tidak valid
     */
    public Mobil(String id, String nama, double tarifHarian, double biayaAsuransi)
            throws DataTidakValidException {
        super(id, nama, tarifHarian);
        this.biayaAsuransi = validasiBiaya(biayaAsuransi);
    }

    @Override
    public double hitungBiaya(int hari) {
        return getTarifHarian() * hari + biayaAsuransi;
    }

    @Override
    public String getKategori() {
        return "Mobil";
    }

    @Override
    public String getKeteranganBiaya() {
        return "Asuransi " + Tampilan.rupiah(biayaAsuransi) + " (sekali)";
    }

    @Override
    public String getLabelBiayaTambahan() {
        return "Biaya asuransi (sekali per sewa)";
    }

    @Override
    public double getBiayaTambahan() {
        return biayaAsuransi;
    }

    @Override
    public void setBiayaTambahan(double biaya) throws DataTidakValidException {
        this.biayaAsuransi = validasiBiaya(biaya);
    }
}
