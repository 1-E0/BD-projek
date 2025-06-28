

-- Hapus tabel-tabel lama 
DROP TABLE IF EXISTS laporan_performa, pelanggan_voucher, voucher, favorit_pelanggan, penukaran_hadiah, hadiah, member, detail_pesanan, pembayaran, pengiriman, review, status_pesanan, menu_harian, cabang_diskon, diskon, admin, staff, pesanan, menu, kategori, cabang, kota, metode_pembayaran, pelanggan CASCADE;

-- Tabel PELANGGAN
CREATE TABLE pelanggan (
    id_pelanggan SERIAL PRIMARY KEY,
    nama_pelanggan VARCHAR(50) NOT NULL,
    email_pelanggan VARCHAR(50) UNIQUE NOT NULL,
    password_pelanggan VARCHAR(255) NOT NULL,
    alamat_pelanggan VARCHAR(100),
    no_telp_pelanggan VARCHAR(15),
    jumlah_poin INT NOT NULL DEFAULT 0,
    tanggal_join DATE,
    CONSTRAINT check_poin_tidak_negatif CHECK (jumlah_poin >= 0)
);

-- Tabel KOTA
CREATE TABLE kota (
    id_kota SERIAL PRIMARY KEY,
    nama_kota VARCHAR(50) NOT NULL
);

-- Tabel CABANG
CREATE TABLE cabang (
    id_cabang SERIAL PRIMARY KEY,
    id_kota INT REFERENCES kota(id_kota) ON DELETE SET NULL,
    alamat_cabang VARCHAR(100) NOT NULL,
    no_telp_cabang VARCHAR(15)
);

-- Tabel PESANAN
CREATE TABLE pesanan (
    id_pesanan SERIAL PRIMARY KEY,
    id_pelanggan INT REFERENCES pelanggan(id_pelanggan) ON DELETE CASCADE,
    tanggal_pesanan DATE NOT NULL,
    total_harga_pesanan NUMERIC(12, 2) NOT NULL,
    status_pembayaran VARCHAR(20) NOT NULL,
    alamat_tujuan VARCHAR(100) NOT NULL
);

-- Tabel KATEGORI
CREATE TABLE kategori (
    id_kategori SERIAL PRIMARY KEY,
    nama_kategori VARCHAR(50) NOT NULL
);

-- Tabel MENU
CREATE TABLE menu (
    id_menu SERIAL PRIMARY KEY,
    id_kategori INT REFERENCES kategori(id_kategori) ON DELETE SET NULL,
    nama_menu VARCHAR(50) NOT NULL,
    harga_menu NUMERIC(12, 2) NOT NULL
);

-- Tabel MENU_HARIAN
CREATE TABLE menu_harian (
    id_menu_harian SERIAL PRIMARY KEY,
    id_menu INT REFERENCES menu(id_menu) ON DELETE CASCADE,
    id_cabang INT REFERENCES cabang(id_cabang) ON DELETE CASCADE,
    tanggal_menu_harian DATE NOT NULL,
    stok_menu_harian INT NOT NULL DEFAULT 0
);

-- Tabel DETAIL_PESANAN
CREATE TABLE detail_pesanan (
    id_detail_pesanan SERIAL PRIMARY KEY,
    id_pesanan INT REFERENCES pesanan(id_pesanan) ON DELETE CASCADE,
    id_menu_harian INT REFERENCES menu_harian(id_menu_harian) ON DELETE SET NULL,
    jumlah INT NOT NULL,
    harga_produk NUMERIC(12, 2) NOT NULL,
    catatan VARCHAR(100)
);

-- Tabel METODE_PEMBAYARAN
CREATE TABLE metode_pembayaran (
    id_metode SERIAL PRIMARY KEY,
    cara_metode VARCHAR(50) NOT NULL
);

-- Tabel PEMBAYARAN
CREATE TABLE pembayaran (
    id_pembayaran SERIAL PRIMARY KEY,
    id_pesanan INT REFERENCES pesanan(id_pesanan) ON DELETE CASCADE,
    id_metode INT REFERENCES metode_pembayaran(id_metode) ON DELETE SET NULL,
    total_pembayaran NUMERIC(12, 2) NOT NULL,
    tanggal_pembayaran DATE NOT NULL
);

-- Tabel STAFF
CREATE TABLE staff (
    id_staff SERIAL PRIMARY KEY,
    id_cabang INT REFERENCES cabang(id_cabang) ON DELETE SET NULL,
    nama_staff VARCHAR(50) NOT NULL,
    umur_staff INT,
    jabatan_staff VARCHAR(50),
... (121 lines left)
Collapse
message.txt
10 KB


Message @E
﻿








E

hacc._


About Me
E
Member Since
May 7, 2017
Mutual Servers — 27
Mutual Friends — 8

View Full Profile
;
-- =================================================================
-- SKRIP DATABASE FINAL - APLIKASI CATERING
-- Versi ini mencakup semua fitur, termasuk Laporan Performa.
-- =================================================================

-- Hapus tabel-tabel lama jika ada untuk memastikan setup yang bersih
DROP TABLE IF EXISTS laporan_performa, pelanggan_voucher, voucher, favorit_pelanggan, penukaran_hadiah, hadiah, member, detail_pesanan, pembayaran, pengiriman, review, status_pesanan, menu_harian, cabang_diskon, diskon, admin, staff, pesanan, menu, kategori, cabang, kota, metode_pembayaran, pelanggan CASCADE;

-- Tabel PELANGGAN
CREATE TABLE pelanggan (
    id_pelanggan SERIAL PRIMARY KEY,
    nama_pelanggan VARCHAR(50) NOT NULL,
    email_pelanggan VARCHAR(50) UNIQUE NOT NULL,
    password_pelanggan VARCHAR(255) NOT NULL,
    alamat_pelanggan VARCHAR(100),
    no_telp_pelanggan VARCHAR(15),
    jumlah_poin INT NOT NULL DEFAULT 0,
    tanggal_join DATE,
    CONSTRAINT check_poin_tidak_negatif CHECK (jumlah_poin >= 0)
);

-- Tabel KOTA
CREATE TABLE kota (
    id_kota SERIAL PRIMARY KEY,
    nama_kota VARCHAR(50) NOT NULL
);

-- Tabel CABANG
CREATE TABLE cabang (
    id_cabang SERIAL PRIMARY KEY,
    id_kota INT REFERENCES kota(id_kota) ON DELETE SET NULL,
    alamat_cabang VARCHAR(100) NOT NULL,
    no_telp_cabang VARCHAR(15)
);

-- Tabel PESANAN
CREATE TABLE pesanan (
    id_pesanan SERIAL PRIMARY KEY,
    id_pelanggan INT REFERENCES pelanggan(id_pelanggan) ON DELETE CASCADE,
    tanggal_pesanan DATE NOT NULL,
    total_harga_pesanan NUMERIC(12, 2) NOT NULL,
    status_pembayaran VARCHAR(20) NOT NULL,
    alamat_tujuan VARCHAR(100) NOT NULL
);

-- Tabel KATEGORI
CREATE TABLE kategori (
    id_kategori SERIAL PRIMARY KEY,
    nama_kategori VARCHAR(50) NOT NULL
);

-- Tabel MENU
CREATE TABLE menu (
    id_menu SERIAL PRIMARY KEY,
    id_kategori INT REFERENCES kategori(id_kategori) ON DELETE SET NULL,
    nama_menu VARCHAR(50) NOT NULL,
    harga_menu NUMERIC(12, 2) NOT NULL
);

-- Tabel MENU_HARIAN
CREATE TABLE menu_harian (
    id_menu_harian SERIAL PRIMARY KEY,
    id_menu INT REFERENCES menu(id_menu) ON DELETE CASCADE,
    id_cabang INT REFERENCES cabang(id_cabang) ON DELETE CASCADE,
    tanggal_menu_harian DATE NOT NULL,
    stok_menu_harian INT NOT NULL DEFAULT 0
);

-- Tabel DETAIL_PESANAN
CREATE TABLE detail_pesanan (
    id_detail_pesanan SERIAL PRIMARY KEY,
    id_pesanan INT REFERENCES pesanan(id_pesanan) ON DELETE CASCADE,
    id_menu_harian INT REFERENCES menu_harian(id_menu_harian) ON DELETE SET NULL,
    jumlah INT NOT NULL,
    harga_produk NUMERIC(12, 2) NOT NULL,
    catatan VARCHAR(100)
);

-- Tabel METODE_PEMBAYARAN
CREATE TABLE metode_pembayaran (
    id_metode SERIAL PRIMARY KEY,
    cara_metode VARCHAR(50) NOT NULL
);

-- Tabel PEMBAYARAN
CREATE TABLE pembayaran (
    id_pembayaran SERIAL PRIMARY KEY,
    id_pesanan INT REFERENCES pesanan(id_pesanan) ON DELETE CASCADE,
    id_metode INT REFERENCES metode_pembayaran(id_metode) ON DELETE SET NULL,
    total_pembayaran NUMERIC(12, 2) NOT NULL,
    tanggal_pembayaran DATE NOT NULL
);

-- Tabel STAFF
CREATE TABLE staff (
    id_staff SERIAL PRIMARY KEY,
    id_cabang INT REFERENCES cabang(id_cabang) ON DELETE SET NULL,
    nama_staff VARCHAR(50) NOT NULL,
    umur_staff INT,
    jabatan_staff VARCHAR(50),
    alamat_staff VARCHAR(100),
    no_telp_staff VARCHAR(15)
);

-- Tabel PENGIRIMAN
CREATE TABLE pengiriman (
    id_pengiriman SERIAL PRIMARY KEY,
    id_pesanan INT REFERENCES pesanan(id_pesanan) ON DELETE CASCADE,
    id_staff INT REFERENCES staff(id_staff) ON DELETE SET NULL,
    status_pengiriman VARCHAR(20),
    estimasi_pengiriman TIMESTAMP,
    catatan_pengiriman VARCHAR(100)
);

-- Tabel REVIEW
CREATE TABLE review (
    id_review SERIAL PRIMARY KEY,
    id_pesanan INT REFERENCES pesanan(id_pesanan) ON DELETE CASCADE,
    id_pelanggan INT REFERENCES pelanggan(id_pelanggan) ON DELETE CASCADE,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    komentar TEXT,
    tanggal_review DATE NOT NULL
);

-- Tabel ADMIN
CREATE TABLE admin (
    id_admin SERIAL PRIMARY KEY,
    id_cabang INT REFERENCES cabang(id_cabang) ON DELETE SET NULL,
    jenis_admin VARCHAR(50),
    nama_admin VARCHAR(50) NOT NULL,
    email_admin VARCHAR(50) UNIQUE NOT NULL,
    password_admin VARCHAR(255) NOT NULL,
    no_telp_admin VARCHAR(15),
    status_admin VARCHAR(50)
);

-- Tabel DISKON
CREATE TABLE diskon (
    id_diskon SERIAL PRIMARY KEY,
    nama_diskon VARCHAR(50) NOT NULL,
    persen_diskon NUMERIC(5, 2) NOT NULL,
    syarat_dan_ketentuan_diskon TEXT,
    tanggal_mulai_diskon DATE NOT NULL,
    tanggal_akhir_diskon DATE NOT NULL
);

-- Tabel CABANG_DISKON
CREATE TABLE cabang_diskon (
    id_cabang_diskon SERIAL PRIMARY KEY,
    id_diskon INT REFERENCES diskon(id_diskon) ON DELETE CASCADE,
    id_cabang INT REFERENCES cabang(id_cabang) ON DELETE CASCADE,
    status_cabang_diskon VARCHAR(20)
);

-- Tabel FAVORIT_PELANGGAN
CREATE TABLE favorit_pelanggan (
    id_pelanggan INT NOT NULL REFERENCES pelanggan(id_pelanggan) ON DELETE CASCADE,
    id_menu INT NOT NULL REFERENCES menu(id_menu) ON DELETE CASCADE,
    PRIMARY KEY (id_pelanggan, id_menu)
);

-- Tabel VOUCHER
CREATE TABLE voucher (
    id_voucher SERIAL PRIMARY KEY,
    nama_voucher VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    potongan_harga NUMERIC(10, 2) NOT NULL,
    poin_dibutuhkan INT NOT NULL,
    aktif BOOLEAN DEFAULT TRUE
);

-- Tabel PELANGGAN_VOUCHER
CREATE TABLE pelanggan_voucher (
    id_pelanggan_voucher SERIAL PRIMARY KEY,
    id_pelanggan INT NOT NULL REFERENCES pelanggan(id_pelanggan) ON DELETE CASCADE,
    id_voucher INT NOT NULL REFERENCES voucher(id_voucher) ON DELETE CASCADE,
    kode_voucher VARCHAR(50) UNIQUE NOT NULL,
    tanggal_penukaran DATE NOT NULL DEFAULT CURRENT_DATE,
    tanggal_kadaluarsa DATE NOT NULL,
    status_voucher VARCHAR(20) DEFAULT 'AKTIF'
);

-- Tabel LAPORAN_PERFORMA
CREATE TABLE laporan_performa (
    id_laporan_performa SERIAL PRIMARY KEY,
    id_cabang INT REFERENCES cabang(id_cabang) ON DELETE SET NULL,
    jumlah_pesanan INT NOT NULL,
    total_pendapatan NUMERIC(13, 2) NOT NULL,
    bulan INT NOT NULL,
    tahun INT NOT NULL
);

-- Data Awal untuk Testing
INSERT INTO kota (nama_kota) VALUES
('Ambon'), ('Balikpapan'), ('Banda Aceh'), ('Bandar Lampung'), ('Bandung'), ('Banjar'), ('Banjarbaru'), ('Banjarmasin'), ('Batam'), ('Batu'),
('Bau-Bau'), ('Bekasi'), ('Bengkulu'), ('Bima'), ('Binjai'), ('Bitung'), ('Blitar'), ('Bogor'), ('Bontang'), ('Bukittinggi'),
('Cilegon'), ('Cimahi'), ('Cirebon'), ('Denpasar'), ('Depok'), ('Dumai'), ('Gorontalo'), ('Gunungsitoli'), ('Jakarta Barat'), ('Jakarta Pusat'),
('Jakarta Selatan'), ('Jakarta Timur'), ('Jakarta Utara'), ('Jambi'), ('Jayapura'), ('Jember'), ('Kediri'), ('Kendari'), ('Kotamobagu'), ('Kupang'),
('Langsa'), ('Lhokseumawe'), ('Lubuklinggau'), ('Madiun'), ('Magelang'), ('Makassar'), ('Malang'), ('Manado'), ('Mataram'), ('Medan'),
('Metro'), ('Mojokerto'), ('Padang'), ('Padang Panjang'), ('Padang Sidempuan'), ('Pagar Alam'), ('Palangkaraya'), ('Palembang'), ('Palopo'), ('Palu'),
('Pangkal Pinang'), ('Parepare'), ('Pariaman'), ('Pasuruan'), ('Payakumbuh'), ('Pekalongan'), ('Pekanbaru'), ('Pematangsiantar'), ('Pontianak'), ('Prabumulih'),
('Probolinggo'), ('Sabang'), ('Salatiga'), ('Samarinda'), ('Sawahlunto'), ('Semarang'), ('Serang'), ('Sibolga'), ('Singkawang'), ('Solok'),
('Sorong'), ('Subulussalam'), ('Sukabumi'), ('Sungai Penuh'), ('Surabaya'), ('Surakarta'), ('Tangerang'), ('Tangerang Selatan'), ('Tanjungbalai'), ('Tanjungpinang'),
('Tarakan'), ('Tasikmalaya'), ('Tebing Tinggi'), ('Tegal'), ('Ternate'), ('Tidore Kepulauan'), ('Tomohon'), ('Tual'), ('Yogyakarta');

INSERT INTO kategori (nama_kategori) VALUES ('Makanan Utama'), ('Minuman'), ('Dessert'), ('Paket Hemat');
INSERT INTO metode_pembayaran (cara_metode) VALUES ('Cash'), ('Transfer Bank'), ('QRIS');

INSERT INTO voucher (nama_voucher, deskripsi, potongan_harga, poin_dibutuhkan, aktif) VALUES
('Potongan Ongkir Rp 5.000', 'Gratis ongkos kirim hingga Rp 5.000 untuk pesanan Anda berikutnya.', 5000, 50, true),
('Diskon Belanja Rp 15.000', 'Dapatkan diskon belanja sebesar Rp 15.000.', 15000, 150, true),
('Voucher Cashback Rp 20.000', 'Cashback sebesar Rp 20.000 untuk pesanan di atas Rp 100.000.', 20000, 200, true),
('Voucher Lawas', 'Voucher ini sudah tidak aktif.', 1000, 10, false);

-- Data Akun Awal
INSERT INTO admin (nama_admin, email_admin, password_admin, jenis_admin, status_admin) VALUES
('Super Admin', 'admin@gmail.com', '1', 'Super', 'Aktif');

INSERT INTO pelanggan (nama_pelanggan, email_pelanggan, password_pelanggan, alamat_pelanggan, no_telp_pelanggan, tanggal_join) VALUES
('Budi Santoso', 'budi@gmail.com', '1', 'Jl. Merdeka No. 10, Surabaya', '081234567890', CURRENT_DATE);
