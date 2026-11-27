package rental.app;

import rental.exception.DataTidakValidException;
import rental.exception.RentalException;
import rental.model.Admin;
import rental.model.Akun;
import rental.model.Penyewa;
import rental.util.Input;

/**
 * Titik masuk aplikasi Rental Kendaraan (CLI).
 * Menangani login lalu mengarahkan akun ke menu yang sesuai.
 */
public class Main {

    private static Akun akunAktif;

    /**
     * Method utama program.
     *
     * @param args argumen command line (tidak dipakai)
     */
    public static void main(String[] args) {
        DataRental data;
        try {
            data = new DataRental();
        } catch (DataTidakValidException e) {
            System.out.println("Gagal memuat data awal: " + e.getMessage());
            return;
        }

        System.out.println("=============================================");
        System.out.println("        SISTEM RENTAL KENDARAAN (CLI)");
        System.out.println("=============================================");

        boolean jalan = true;
        while (jalan) {
            System.out.println("\n1. Login sebagai Admin");
            System.out.println("2. Login sebagai Penyewa");
            System.out.println("0. Keluar");
            int pilihan = Input.bacaInt("Pilih: ");
            if (pilihan == 1 || pilihan == 2) {
                login(data, pilihan == 1 ? "Admin" : "Penyewa");
                if (akunAktif != null) {
                    // Polymorphism: akun disimpan sebagai Akun, menu dipilih sesuai jenis akunnya
                    if (akunAktif instanceof Admin admin) {
                        new MenuAdmin(admin, data).jalankan();
                    } else if (akunAktif instanceof Penyewa penyewa) {
                        new MenuPenyewa(penyewa, data).jalankan();
                    }
                    akunAktif = null;
                    System.out.println("Anda telah logout.");
                }
            } else if (pilihan == 0) {
                jalan = false;
            } else {
                System.out.println("[!] Pilihan tidak tersedia.");
            }
        }
        System.out.println("Terima kasih telah menggunakan sistem rental!");
    }

    /**
     * Meminta username dan password lalu mengisi {@code akunAktif} jika berhasil
     * dan perannya sesuai dengan pilihan di menu awal.
     *
     * @param data  data aplikasi yang berisi daftar akun
     * @param peran peran yang dipilih ("Admin" atau "Penyewa")
     */
    private static void login(DataRental data, String peran) {
        System.out.println("\n--- LOGIN " + peran.toUpperCase() + " ---");
        String username = Input.bacaString("Username: ");
        String password = Input.bacaString("Password: ");
        try {
            akunAktif = data.login(username, password, peran);
            System.out.println("Login berhasil. Selamat datang, " + akunAktif.getUsername()
                    + " (" + akunAktif.getRole() + ")!");
        } catch (RentalException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }
}
