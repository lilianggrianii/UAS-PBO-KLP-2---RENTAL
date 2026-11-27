# Sistem Rental Kendaraan (Java CLI)

## Deskripsi Singkat
Aplikasi penyewaan kendaraan berbasis Command Line Interface (CLI) yang dibuat dengan Java
untuk UAS Praktikum PBO. Terdapat dua jenis akun: **Admin** (kelola kendaraan, terima transaksi,
konfirmasi pengembalian + denda) dan **Penyewa** (lihat kendaraan, isi daftar sewa, buat transaksi,
pilih metode pembayaran, lihat riwayat).

Kategori kendaraan dengan cara hitung biaya berbeda:

 Kategori - Cara hitung biaya 
 
 Motor - tarif harian x lama sewa 
 Mobil - (tarif harian x lama sewa) + biaya asuransi (sekali per sewa) 
 Bus   - (tarif harian + biaya sopir per hari) x lama sewa 

Metode pembayaran: Transfer Bank, QRIS, Tunai.
Denda keterlambatan: 50% tarif harian per barang, per hari terlambat.

## Konsep OOP yang Digunakan
- **Abstraction**: `Akun`, `Barang` (abstract class), `Pembayaran` (interface)
- **Inheritance**: `Admin`/`Penyewa` extends `Akun`; `Motor`/`Mobil`/`Bus` extends `Barang`
- **Polymorphism**: `hitungBiaya()` berbeda tiap kategori, `prosesPembayaran()` berbeda tiap metode
- **Encapsulation**: semua atribut `private` dengan getter/setter tervalidasi
- **Collection**: `ArrayList` (daftar barang, keranjang, transaksi, riwayat, metode pembayaran)
- **Exception**: `RentalException` dan turunannya (`LoginGagalException`, `BarangTidakTersediaException`, `DataTidakDitemukanException`, `DataTidakValidException`)

## Struktur Project
```
src/rental/
  app/          Main, DataRental, MenuAdmin, MenuPenyewa
  model/        Akun, Admin, Penyewa, Barang, Motor, Mobil, Bus,
                DaftarBarang, KeranjangSewa, Transaksi, Invoice, StatusTransaksi
  pembayaran/   Pembayaran (interface), TransferBank, QRIS, Tunai
  exception/    RentalException + 4 exception turunan
  util/         Input, Tampilan
```

## Cara Menjalankan

**1. Compile**

Linux / Mac / Git Bash:
```bash
mkdir out
javac -d out $(find src -name "*.java")
```
Windows PowerShell:
```powershell
mkdir out
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

**2. Jalankan**
```bash
java -cp out rental.app.Main
```

Membuat dokumentasi Javadoc:
```bash
javadoc -d docs -encoding UTF-8 -sourcepath src -subpackages rental
```

## Akun Demo
| Role | Username | Password |
|------|----------|----------|
| Admin   | admin | admin123 |
| Penyewa | budi  | budi123  |
| Penyewa | siti  | siti123  |

## Anggota Kelompok
 Abdan Syakura  - 250810701100017
 Farhan Syafiq  - 250810701100039
 Lili Anggriani - 250810701100106 
