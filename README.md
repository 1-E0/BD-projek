

-- Hapus tabel-tabel lama jika ada untuk memastikan setup yang bersih

DROP TABLE IF EXISTS laporan_performa, pelanggan_voucher, voucher, favorit_pelanggan, penukaran_hadiah, hadiah, member, detail_pesanan, pembayaran, pengiriman, review, status_pesanan, menu_harian, cabang_diskon, diskon, admin, staff, pesanan, menu, kategori, cabang, kota, metode_pembayaran, pelanggan CASCADE;

-- =================================================================
-- BAGIAN 1: PEMBUATAN STRUKTUR TABEL (CREATE TABLES)
-- =================================================================

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

-- Tabel PESANAN

CREATE TABLE pesanan (
    id_pesanan SERIAL PRIMARY KEY,
    id_pelanggan INT REFERENCES pelanggan(id_pelanggan) ON DELETE CASCADE,
    tanggal_pesanan DATE NOT NULL,
    total_harga_pesanan NUMERIC(12, 2) NOT NULL,
    status_pembayaran VARCHAR(20) NOT NULL,
    alamat_tujuan VARCHAR(100) NOT NULL,
    jadwal_pengiriman TIMESTAMP
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

-- Tabel STAFF (Kolom 'umur_staff' sudah dihapus)

CREATE TABLE staff (
    id_staff SERIAL PRIMARY KEY,
    id_cabang INT REFERENCES cabang(id_cabang) ON DELETE SET NULL,
    nama_staff VARCHAR(50) NOT NULL,
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
    jenis_admin VARCHAR(50) CHECK (jenis_admin IN ('Pusat', 'Cabang')),
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

-- =================================================================
-- BAGIAN 2: PENGISIAN DATA AWAL (INITIAL DATA)
-- =================================================================

-- Data Master Kota (98 Kota Resmi di Indonesia)

INSERT INTO kota (nama_kota) VALUES
('Ambon'), ('Balikpapan'), ('Banda Aceh'), ('Bandar Lampung'), ('Bandung'), ('Banjar'), ('Banjarbaru'), ('Banjarmasin'), ('Batam'), ('Batu'), ('Baubau'), ('Bekasi'), ('Bengkulu'), ('Bima'), ('Binjai'), ('Bitung'), ('Blitar'), ('Bogor'), ('Bontang'), ('Bukittinggi'), ('Cilegon'), ('Cimahi'), ('Cirebon'), ('Denpasar'), ('Depok'), ('Dumai'), ('Gorontalo'), ('Gunungsitoli'), ('Jakarta Barat'), ('Jakarta Pusat'), ('Jakarta Selatan'), ('Jakarta Timur'), ('Jakarta Utara'), ('Jambi'), ('Jayapura'), ('Kediri'), ('Kendari'), ('Kotamobagu'), ('Kupang'), ('Langsa'), ('Lhokseumawe'), ('Lubuklinggau'), ('Madiun'), ('Magelang'), ('Makassar'), ('Malang'), ('Manado'), ('Mataram'), ('Medan'), ('Metro'), ('Mojokerto'), ('Padang'), ('Padang Panjang'), ('Padang Sidempuan'), ('Pagar Alam'), ('Palangka Raya'), ('Palembang'), ('Palopo'), ('Palu'), ('Pangkalpinang'), ('Parepare'), ('Pariaman'), ('Pasuruan'), ('Payakumbuh'), ('Pekalongan'), ('Pekanbaru'), ('Pematangsiantar'), ('Pontianak'), ('Prabumulih'), ('Probolinggo'), ('Sabang'), ('Salatiga'), ('Samarinda'), ('Sawahlunto'), ('Semarang'), ('Serang'), ('Sibolga'), ('Singkawang'), ('Solok'), ('Sorong'), ('Subulussalam'), ('Sukabumi'), ('Sungai Penuh'), ('Surabaya'), ('Surakarta'), ('Tangerang'), ('Tangerang Selatan'), ('Tanjungbalai'), ('Tanjungpinang'), ('Tarakan'), ('Tasikmalaya'), ('Tebing Tinggi'), ('Tegal'), ('Ternate'), ('Tidore Kepulauan'), ('Tomohon'), ('Tual'), ('Yogyakarta');

INSERT INTO kategori (nama_kategori) VALUES ('Makanan Utama'), ('Minuman'), ('Dessert'), ('Paket Hemat');
INSERT INTO metode_pembayaran (cara_metode) VALUES ('Cash'), ('Transfer Bank'), ('QRIS');

-- Data Contoh Cabang (Pilih ID Kota dari daftar di atas)

INSERT INTO cabang (id_kota, alamat_cabang, no_telp_cabang) VALUES
(89, 'Jl. Raya Darmo Permai III No. 5, Surabaya', '0315551234'),
(31, 'Jl. Jend. Sudirman Kav. 52-53, Jakarta Selatan', '0215555678');

-- Data Master Voucher

INSERT INTO voucher (nama_voucher, deskripsi, potongan_harga, poin_dibutuhkan, aktif) VALUES
('Potongan Ongkir Rp 5.000', 'Gratis ongkos kirim hingga Rp 5.000.', 5000, 50, true),
('Diskon Belanja Rp 15.000', 'Dapatkan diskon belanja sebesar Rp 15.000.', 15000, 150, true),
('Voucher Cashback Rp 20.000', 'Cashback Rp 20.000 untuk min. belanja Rp 100.000.', 20000, 200, true);


-- Data Akun Awal (Admin & Pelanggan)

INSERT INTO admin (nama_admin, email_admin, password_admin, jenis_admin, status_admin, id_cabang) VALUES
('Admin Pusat Utama', 'adminpusat@gmail.com', '1', 'Pusat', 'Aktif', NULL),
('Admin Cabang Surabaya', 'admincabang@gmail.com', '1', 'Cabang', 'Aktif', 1);

INSERT INTO pelanggan (nama_pelanggan, email_pelanggan, password_pelanggan, alamat_pelanggan, no_telp_pelanggan, tanggal_join) VALUES
('Budi Santoso', 'budi@gmail.com', '1', 'Jl. Merdeka No. 10, Surabaya', '081234567890', CURRENT_DATE);

-- =================================================================
-- BAGIAN 3: PENGISIAN DATA CONTOH (SAMPLE DATA)
-- =================================================================


-- Data Master Menu


INSERT INTO menu (id_kategori, nama_menu, harga_menu) VALUES
(1, 'Nasi Goreng Spesial', 35000), (1, 'Ayam Bakar Madu', 45000), (1, 'Sate Ayam (10 tusuk)', 40000), (1, 'Rawon Daging Sapi', 55000), (1, 'Iga Bakar Saus BBQ', 85000), (1, 'Sop Buntut', 75000),
(2, 'Es Teh Manis', 8000), (2, 'Es Jeruk', 10000), (2, 'Jus Alpukat', 20000), (2, 'Kopi Susu Gula Aren', 22000), (2, 'Air Mineral', 5000),
(3, 'Puding Coklat Vla', 18000), (3, 'Es Krim Vanila', 15000), (3, 'Salad Buah Segar', 25000),
(4, 'Paket Nasi Ayam Goreng + Es Teh', 50000), (4, 'Paket Nasi Rawon + Es Jeruk', 60000);
