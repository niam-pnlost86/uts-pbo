
/*
 * Sistem Kasir POS Berbasis Java
 * Materi: Encapsulation, Inheritance,
 * Polymorphism, dan Abstraction
 */

package com.mycompany.uts;

import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;

// ========================================
// ABSTRACTION DAN ENCAPSULATION
// ========================================
abstract class Produk {

    private String nama;
    private int harga;

    public Produk(String nama, int harga) {
        this.nama = nama;
        this.harga = harga;
    }

    // Getter untuk mengakses atribut private
    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }

    // Method abstrak
    public abstract int hitungSubtotal(int jumlah);

    public abstract String getJenis();
}

// ========================================
// INHERITANCE: PRODUK MAKANAN
// ========================================
class ProdukMakanan extends Produk {

    public ProdukMakanan(String nama, int harga) {
        super(nama, harga);
    }

    @Override
    public int hitungSubtotal(int jumlah) {
        return getHarga() * jumlah;
    }

    @Override
    public String getJenis() {
        return "Makanan";
    }
}

// ========================================
// INHERITANCE: PRODUK MINUMAN
// ========================================
class ProdukMinuman extends Produk {

    public ProdukMinuman(String nama, int harga) {
        super(nama, harga);
    }

    @Override
    public int hitungSubtotal(int jumlah) {
        // Diskon simulasi 5% untuk minuman
        int subtotal = getHarga() * jumlah;
        return (int) (subtotal * 0.95);
    }

    @Override
    public String getJenis() {
        return "Minuman";
    }
}

// ========================================
// PROGRAM UTAMA KASIR POS
// ========================================
public class Uts {

    static Scanner input = new Scanner(System.in);

    static NumberFormat rupiah =
            NumberFormat.getCurrencyInstance(
                    new Locale("id", "ID"));

    static String formatRupiah(int nominal) {
        return rupiah.format(nominal);
    }

    // Daftar produk
    static Produk[] daftarProduk = {
        new ProdukMakanan("Roti", 5000),
        new ProdukMakanan("Nasi Goreng", 15000),
        new ProdukMakanan("Mie Instan", 4000),
        new ProdukMinuman("Air Mineral", 3000),
        new ProdukMinuman("Es Teh", 5000),
        new ProdukMinuman("Kopi", 7000)
    };

    // Input angka dengan validasi
    static int inputAngka(String pesan) {
        while (true) {
            System.out.print(pesan);

            if (input.hasNextInt()) {
                return input.nextInt();
            }

            System.out.println(
                    "Input tidak valid! Masukkan angka.");
            input.next();
        }
    }

    // Menampilkan daftar produk
    static void tampilkanProduk() {
        System.out.println();
        System.out.println("================================================");
        System.out.println("             TOKO MAJU JAYA");
        System.out.println("              SISTEM KASIR POS");
        System.out.println("================================================");

        System.out.printf("%-5s %-18s %-12s %12s%n",
                "Kode", "Nama Produk", "Jenis", "Harga");

        System.out.println("------------------------------------------------");

        for (int i = 0; i < daftarProduk.length; i++) {
            Produk produk = daftarProduk[i];

            System.out.printf("%-5d %-18s %-12s %12s%n",
                    i + 1,
                    produk.getNama(),
                    produk.getJenis(),
                    formatRupiah(produk.getHarga()));
        }

        System.out.println("------------------------------------------------");
        System.out.println("0. Selesaikan transaksi");
        System.out.println("9. Batalkan transaksi");
    }

    // Proses transaksi
    static void prosesTransaksi() {

        int[] jumlahBeli = new int[daftarProduk.length];

        while (true) {
            tampilkanProduk();

            int kode = inputAngka("Pilih kode produk: ");

            if (kode == 0) {
                break;
            }

            if (kode == 9) {
                System.out.println(
                        "Transaksi berhasil dibatalkan.");
                return;
            }

            if (kode < 1 || kode > daftarProduk.length) {
                System.out.println(
                        "Kode produk tidak tersedia!");
                continue;
            }

            int jumlah = inputAngka("Masukkan jumlah beli: ");

            if (jumlah <= 0) {
                System.out.println(
                        "Jumlah beli harus lebih dari 0!");
                continue;
            }

            // Menambahkan jumlah barang
            jumlahBeli[kode - 1] += jumlah;

            Produk produk = daftarProduk[kode - 1];

            // Polymorphism
            int subtotal = produk.hitungSubtotal(
                    jumlahBeli[kode - 1]);

            System.out.println();
            System.out.println("Produk dipilih : "
                    + produk.getNama());
            System.out.println("Jenis produk   : "
                    + produk.getJenis());
            System.out.println("Jumlah beli    : "
                    + jumlahBeli[kode - 1]);
            System.out.println("Subtotal       : "
                    + formatRupiah(subtotal));
        }

        // Menghitung dan mencetak struk
        cetakStruk(jumlahBeli);
    }

    // Mencetak struk belanja
    static void cetakStruk(int[] jumlahBeli) {

        int total = 0;

        System.out.println();
        System.out.println("================================================");
        System.out.println("                 STRUK BELANJA");
        System.out.println("                TOKO MAJU JAYA");
        System.out.println("================================================");

        for (int i = 0; i < daftarProduk.length; i++) {

            if (jumlahBeli[i] > 0) {

                Produk produk = daftarProduk[i];

                int subtotal = produk.hitungSubtotal(
                        jumlahBeli[i]);

                total += subtotal;

                System.out.println(produk.getNama()
                        + " (" + produk.getJenis() + ")");

                System.out.println("  "
                        + jumlahBeli[i] + " x "
                        + formatRupiah(produk.getHarga()));

                System.out.println("  Subtotal: "
                        + formatRupiah(subtotal));
            }
        }

        if (total == 0) {
            System.out.println(
                    "Tidak ada barang yang dibeli.");
            System.out.println("================================================");
            return;
        }

        System.out.println("------------------------------------------------");
        System.out.println("TOTAL BELANJA : "
                + formatRupiah(total));

        // Proses pembayaran
        int bayar;

        do {
            bayar = inputAngka("Uang pembayaran: ");

            if (bayar < 0) {
                System.out.println(
                        "Pembayaran tidak boleh negatif!");
            } else if (bayar < total) {
                System.out.println("Uang Anda kurang: "
                        + formatRupiah(total - bayar));
            }

        } while (bayar < total);

        int kembalian = bayar - total;

        System.out.println("TOTAL BAYAR   : "
                + formatRupiah(bayar));

        System.out.println("KEMBALIAN     : "
                + formatRupiah(kembalian));

        System.out.println("================================================");
        System.out.println("     Terima kasih telah berbelanja!");
        System.out.println("================================================");
    }

    // Method utama
    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   APLIKASI KASIR POS - JAVA OOP");
        System.out.println("========================================");

        while (true) {

            prosesTransaksi();

            System.out.print(
                    "\nApakah ingin transaksi baru? (y/n): ");

            String jawaban = input.next();

            if (!jawaban.equalsIgnoreCase("y")) {
                break;
            }
        }

        System.out.println();
        System.out.println(
                "Program kasir selesai. Terima kasih!");

        input.close();
    }
}
