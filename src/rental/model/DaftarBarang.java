package rental.model;

import java.util.ArrayList;
import java.util.List;
import rental.exception.BarangTidakTersediaException;
import rental.exception.DataTidakDitemukanException;
import rental.exception.DataTidakValidException;

/**
 * Menyimpan seluruh barang yang ada di sistem (koleksi ArrayList)
 * dan menyediakan operasi tambah, edit, hapus, serta pencarian.
 */
public class DaftarBarang {

    private final ArrayList<Barang> barang = new ArrayList<>();
    private int counterId = 0;

    /**
     * Membuat ID otomatis dengan format PREFIX + nomor urut 3 digit, misal MTR001.
     *
     * @param prefix awalan ID (misal MTR, MBL, BUS)
     * @return ID baru
     */
    public String buatId(String prefix) {
        counterId++;
        return prefix + String.format("%03d", counterId);
    }

    /**
     * Menambahkan barang baru.
     *
     * @param b barang yang ditambahkan
     */
    public void tambahBarang(Barang b) {
        barang.add(b);
    }

    /**
     * Mengubah data barang.
     *
     * @param id       ID barang yang diedit
     * @param nama     nama baru
     * @param tarif    tarif harian baru
     * @param tambahan biaya tambahan baru (diabaikan untuk kategori tanpa biaya tambahan)
     * @throws DataTidakDitemukanException jika ID tidak ada
     * @throws DataTidakValidException     jika data baru tidak valid
     */
    public void editBarang(String id, String nama, double tarif, double tambahan)
            throws DataTidakDitemukanException, DataTidakValidException {
        Barang b = cariById(id);
        b.setNama(nama);
        b.setTarifHarian(tarif);
        b.setBiayaTambahan(tambahan);
    }

    /**
     * Menghapus barang. Barang yang sedang disewa tidak boleh dihapus.
     *
     * @param id ID barang
     * @throws DataTidakDitemukanException  jika ID tidak ada
     * @throws BarangTidakTersediaException jika barang sedang disewa
     */
    public void hapusBarang(String id) throws DataTidakDitemukanException, BarangTidakTersediaException {
        Barang b = cariById(id);
        if (!b.isTersedia()) {
            throw new BarangTidakTersediaException(
                    "Barang '" + b.getNama() + "' sedang disewa dan tidak bisa dihapus.");
        }
        barang.remove(b);
    }

    /**
     * Mencari barang berdasarkan ID (tidak membedakan huruf besar/kecil).
     *
     * @param id ID barang
     * @return barang yang ditemukan
     * @throws DataTidakDitemukanException jika tidak ada
     */
    public Barang cariById(String id) throws DataTidakDitemukanException {
        for (Barang b : barang) {
            if (b.getId().equalsIgnoreCase(id)) {
                return b;
            }
        }
        throw new DataTidakDitemukanException("Barang", id);
    }

    /** @return salinan daftar seluruh barang */
    public List<Barang> getSemua() {
        return new ArrayList<>(barang);
    }

    /** @return daftar barang yang berstatus tersedia */
    public List<Barang> getTersedia() {
        List<Barang> hasil = new ArrayList<>();
        for (Barang b : barang) {
            if (b.isTersedia()) {
                hasil.add(b);
            }
        }
        return hasil;
    }
}
