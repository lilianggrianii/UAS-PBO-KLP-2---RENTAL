package rental.model;

import java.util.ArrayList;
import java.util.List;
import rental.exception.BarangTidakTersediaException;
import rental.exception.DataTidakDitemukanException;

/** Daftar sewa (keranjang) milik seorang penyewa; boleh berisi lebih dari satu barang. */
public class KeranjangSewa {

    private final ArrayList<Barang> barang = new ArrayList<>();

    /**
     * Memasukkan barang ke daftar sewa.
     *
     * @param b barang yang dimasukkan
     * @throws BarangTidakTersediaException jika barang sedang disewa atau sudah ada di daftar
     */
    public void tambahBarang(Barang b) throws BarangTidakTersediaException {
        if (!b.isTersedia()) {
            throw new BarangTidakTersediaException("Barang '" + b.getNama() + "' sedang disewa.");
        }
        if (barang.contains(b)) {
            throw new BarangTidakTersediaException("Barang '" + b.getNama() + "' sudah ada di daftar sewa.");
        }
        barang.add(b);
    }

    /**
     * Mengeluarkan barang dari daftar sewa.
     *
     * @param id ID barang
     * @throws DataTidakDitemukanException jika barang tidak ada di daftar sewa
     */
    public void hapusBarang(String id) throws DataTidakDitemukanException {
        for (Barang b : barang) {
            if (b.getId().equalsIgnoreCase(id)) {
                barang.remove(b);
                return;
            }
        }
        throw new DataTidakDitemukanException("Barang di daftar sewa", id);
    }

    /**
     * Menghitung estimasi total biaya untuk lama sewa tertentu.
     *
     * @param hari lama sewa (hari)
     * @return estimasi total
     */
    public double hitungEstimasi(int hari) {
        double total = 0;
        for (Barang b : barang) {
            total += b.hitungBiaya(hari);
        }
        return total;
    }

    /** Mengosongkan daftar sewa. */
    public void kosongkan() {
        barang.clear();
    }

    /** @return true jika daftar sewa kosong */
    public boolean isKosong() {
        return barang.isEmpty();
    }

    /** @return salinan isi daftar sewa */
    public List<Barang> getBarang() {
        return new ArrayList<>(barang);
    }
}
