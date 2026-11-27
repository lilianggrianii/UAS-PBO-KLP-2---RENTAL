package rental.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/** Pembaca input keyboard yang aman: terus meminta ulang sampai input valid. */
public final class Input {

    private static final Scanner SCANNER = new Scanner(System.in);

    private Input() {
    }

    /**
     * Membaca satu baris teks.
     *
     * @param prompt teks petunjuk
     * @return teks yang diketik (sudah di-trim)
     */
    public static String bacaString(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    /**
     * Membaca bilangan bulat; mengulang jika bukan angka.
     *
     * @param prompt teks petunjuk
     * @return bilangan bulat
     */
    public static int bacaInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(bacaString(prompt));
            } catch (NumberFormatException e) {
                System.out.println("[!] Masukkan angka bulat yang valid.");
            }
        }
    }

    /**
     * Membaca bilangan desimal; mengulang jika bukan angka.
     *
     * @param prompt teks petunjuk
     * @return bilangan desimal
     */
    public static double bacaDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(bacaString(prompt));
            } catch (NumberFormatException e) {
                System.out.println("[!] Masukkan angka yang valid (contoh: 150000).");
            }
        }
    }

    /**
     * Membaca bilangan desimal opsional; jika dikosongkan, dipakai nilai bawaan.
     *
     * @param prompt teks petunjuk
     * @param bawaan nilai jika pengguna hanya menekan Enter
     * @return bilangan desimal
     */
    public static double bacaDoubleOpsional(String prompt, double bawaan) {
        while (true) {
            String s = bacaString(prompt);
            if (s.isEmpty()) {
                return bawaan;
            }
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("[!] Masukkan angka yang valid atau kosongkan.");
            }
        }
    }

    /**
     * Membaca tanggal format yyyy-MM-dd; kosong berarti hari ini.
     *
     * @param prompt teks petunjuk
     * @return tanggal yang dimasukkan
     */
    public static LocalDate bacaTanggal(String prompt) {
        while (true) {
            String s = bacaString(prompt);
            if (s.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("[!] Format tanggal harus yyyy-MM-dd (contoh: 2026-12-01).");
            }
        }
    }
}
