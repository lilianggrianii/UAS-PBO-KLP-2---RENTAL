package rental.model;

import rental.exception.DataTidakValidException;

/**
 * Class abstrak untuk semua barang (kendaraan) yang disewakan.
 * Setiap kategori wajib mengimplementasikan cara menghitung biayanya sendiri.
 */
public abstract class Barang {

    private final String id;
    private String nama;
    private double tarifHarian;
    private boolean tersedia = true;

    /**
     * @param id          ID unik barang
     * @param nama        nama barang
     * @param tarifHarian tarif sewa per hari (harus lebih dari 0)
     * @throws DataTidakValidException jika nama kosong atau tarif tidak valid
     */
    protected Barang(String id, String nama, double tarifHarian) throws DataTidakValidException {
        this.id = id;
        setNama(nama);
        setTarifHarian(tarifHarian);
    }

    /**
     * Menghitung total biaya sewa barang ini. Cara hitung berbeda tiap kategori.
     *
     * @param hari lama sewa dalam hari
     * @return total biaya sewa
     */
    public abstract double hitungBiaya(int hari);

    /** @return nama kategori barang (Motor, Mobil, Bus) */
    public abstract String getKategori();

    /** @return penjelasan singkat biaya tambahan, "-" jika tidak ada */
    public String getKeteranganBiaya() {
        return "-";
    }

    /**
     * @return label biaya tambahan yang bisa diisi admin (misal "Biaya asuransi"),
     *         atau null jika kategori ini tidak punya biaya tambahan
     */
    public String getLabelBiayaTambahan() {
        return null;
    }

    /** @return nilai biaya tambahan saat ini (0 jika tidak ada) */
    public double getBiayaTambahan() {
        return 0;
    }

    /**
     * Mengubah biaya tambahan. Kategori tanpa biaya tambahan mengabaikannya.
     *
     * @param biaya nilai biaya tambahan baru
     * @throws DataTidakValidException jika nilai negatif
     */
    public void setBiayaTambahan(double biaya) throws DataTidakValidException {
        // default: tidak ada biaya tambahan
    }

    /**
     * Denda keterlambatan per hari untuk barang ini, yaitu 50% dari tarif harian.
     *
     * @return nominal denda per hari
     */
    public double getDendaPerHari() {
        return tarifHarian * 0.5;
    }

    /**
     * Memvalidasi nilai biaya tambahan agar tidak negatif.
     *
     * @param nilai nilai yang diperiksa
     * @return nilai yang sama jika valid
     * @throws DataTidakValidException jika nilai negatif
     */
    protected static double validasiBiaya(double nilai) throws DataTidakValidException {
        if (nilai < 0) {
            throw new DataTidakValidException("Biaya tidak boleh negatif.");
        }
        return nilai;
    }

    /** @return ID barang */
    public String getId() {
        return id;
    }

    /** @return nama barang */
    public String getNama() {
        return nama;
    }

    /**
     * @param nama nama baru (tidak boleh kosong)
     * @throws DataTidakValidException jika nama kosong
     */
    public void setNama(String nama) throws DataTidakValidException {
        if (nama == null || nama.isBlank()) {
            throw new DataTidakValidException("Nama barang tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    /** @return tarif sewa per hari */
    public double getTarifHarian() {
        return tarifHarian;
    }

    /**
     * @param tarifHarian tarif baru (harus lebih dari 0)
     * @throws DataTidakValidException jika tarif nol atau negatif
     */
    public void setTarifHarian(double tarifHarian) throws DataTidakValidException {
        if (tarifHarian <= 0) {
            throw new DataTidakValidException("Tarif harian harus lebih dari 0.");
        }
        this.tarifHarian = tarifHarian;
    }

    /** @return true jika barang sedang tersedia untuk disewa */
    public boolean isTersedia() {
        return tersedia;
    }

    /** @param tersedia status ketersediaan baru */
    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }
}
