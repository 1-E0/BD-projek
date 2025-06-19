-- Tabel PELANGGAN

CREATE TABLE pelanggan (

    id_pelanggan SERIAL PRIMARY KEY,

    nama_pelanggan VARCHAR(50),

    email_pelanggan VARCHAR(50),

    password_pelanggan VARCHAR(255), -- Diperpanjang untuk hashing

    alamat_pelanggan VARCHAR(100),

    no_telp_pelanggan VARCHAR(15)

);



-- Tabel KOTA

CREATE TABLE kota (

    id_kota SERIAL PRIMARY KEY,

    nama_kota VARCHAR(50)

);



-- Tabel CABANG

CREATE TABLE cabang (

    id_cabang SERIAL PRIMARY KEY,

    alamat_cabang VARCHAR(100),

    no_telp_cabang VARCHAR(15),

    id_kota INT REFERENCES kota(id_kota)

);



-- Tabel PESANAN

CREATE TABLE pesanan (

    id_pesanan SERIAL PRIMARY KEY,

    id_pelanggan INT REFERENCES pelanggan(id_pelanggan),

    tanggal_pesanan DATE,

    total_harga_pesanan NUMERIC(12, 2),

    status_pembayaran VARCHAR(20),

    alamat_tujuan VARCHAR(100)

);



-- Tabel KATEGORI

CREATE TABLE kategori (

    id_kategori SERIAL PRIMARY KEY,

    nama_kategori VARCHAR(50)

);



-- Tabel MENU

CREATE TABLE menu (

    id_menu SERIAL PRIMARY KEY,

    id_kategori INT REFERENCES kategori(id_kategori),

    nama_menu VARCHAR(50),

    harga_menu NUMERIC(12, 2)

);



-- Tabel MENU_HARIAN

CREATE TABLE menu_harian (

    id_menu_harian SERIAL PRIMARY KEY,

    id_menu INT REFERENCES menu(id_menu),

    id_cabang INT REFERENCES cabang(id_cabang),

    tanggal_menu_harian DATE,

    stok_menu_harian INT

);



-- Tabel DETAIL_PESANAN

CREATE TABLE detail_pesanan (

    id_detail_pesanan SERIAL PRIMARY KEY,

    id_pesanan INT REFERENCES pesanan(id_pesanan),

    id_menu_harian INT REFERENCES menu_harian(id_menu_harian),

    jumlah INT, -- Menambahkan kolom jumlah

    harga_produk NUMERIC(12, 2),

    catatan VARCHAR(100)

);



-- Tabel METODE_PEMBAYARAN

CREATE TABLE metode_pembayaran (

    id_metode SERIAL PRIMARY KEY,

    cara_metode VARCHAR(50)

);



-- Tabel PEMBAYARAN

CREATE TABLE pembayaran (

    id_pembayaran SERIAL PRIMARY KEY,

    id_pesanan INT REFERENCES pesanan(id_pesanan),

    id_metode INT REFERENCES metode_pembayaran(id_metode),

    total_pembayaran NUMERIC(12, 2),

    tanggal_pembayaran DATE,

    kembalian_cash NUMERIC(12, 2)

);



-- Tabel MEMBER

CREATE TABLE member (

    id_member SERIAL PRIMARY KEY,

    id_pelanggan INT REFERENCES pelanggan(id_pelanggan),

    nama_member VARCHAR(50),

    tanggal_join DATE,

    jumlah_poin INT

);



-- Tabel HADIAH

CREATE TABLE hadiah (

    id_hadiah SERIAL PRIMARY KEY,

    nama_hadiah VARCHAR(50),

    poin_hadiah INT

);



-- Tabel PENUKARAN_HADIAH (Tabel penghubung antara Member dan Hadiah)

CREATE TABLE penukaran_hadiah (

    id_penukaran SERIAL PRIMARY KEY,

    id_member INT REFERENCES member(id_member),

    id_hadiah INT REFERENCES hadiah(id_hadiah),

    tanggal_penukaran DATE

);





-- Tabel STAFF

CREATE TABLE staff (

    id_staff SERIAL PRIMARY KEY,

    id_cabang INT REFERENCES cabang(id_cabang),

    nama_staff VARCHAR(50),

    umur_staff INT,

    jabatan_staff VARCHAR(50),

    alamat_staff VARCHAR(100),

    no_telp_staff VARCHAR(15)

);



-- Tabel PENGIRIMAN

CREATE TABLE pengiriman (

    id_pengiriman SERIAL PRIMARY KEY,

    id_pesanan INT REFERENCES pesanan(id_pesanan),

    id_staff INT REFERENCES staff(id_staff), -- Diasumsikan driver adalah staff

    status_pengiriman VARCHAR(20),

    estimasi_pengiriman TIMESTAMP,

    catatan_pengiriman VARCHAR(100)

);



-- Tabel STATUS_PESANAN

CREATE TABLE status_pesanan (

    id_status_pesanan SERIAL PRIMARY KEY,

    id_pesanan INT REFERENCES pesanan(id_pesanan),

    status_pesanan VARCHAR(20), -- Contoh: 'diproses', 'dimasak', 'siap diantar'

    waktu_status TIMESTAMP

);



-- Tabel REVIEW

CREATE TABLE review (

    id_review SERIAL PRIMARY KEY,

    id_pesanan INT REFERENCES pesanan(id_pesanan),

    id_pelanggan INT REFERENCES pelanggan(id_pelanggan),

    rating INT, -- Rating 1-5

    komentar TEXT,

    tanggal_review DATE

);



-- Tabel ADMIN

CREATE TABLE admin (

    id_admin SERIAL PRIMARY KEY,

    id_cabang INT REFERENCES cabang(id_cabang),

    jenis_admin VARCHAR(50),

    nama_admin VARCHAR(50),

    email_admin VARCHAR(50),

    password_admin VARCHAR(255), -- Kolom password ditambahkan

    no_telp_admin VARCHAR(15),

    status_admin VARCHAR(50)

);



-- Tabel DISKON

CREATE TABLE diskon (

    id_diskon SERIAL PRIMARY KEY,

    nama_diskon VARCHAR(50),

    persen_diskon NUMERIC(5, 2),

    syarat_dan_ketentuan_diskon TEXT,

    tanggal_mulai_diskon DATE,

    tanggal_akhir_diskon DATE

);



-- Tabel CABANG_DISKON

CREATE TABLE cabang_diskon (

    id_cabang_diskon SERIAL PRIMARY KEY,

    id_diskon INT REFERENCES diskon(id_diskon),

    id_cabang INT REFERENCES cabang(id_cabang),

    status_cabang_diskon VARCHAR(20)

);



-- Tabel LAPORAN_PERFORMA

CREATE TABLE laporan_performa (

    id_laporan_performa SERIAL PRIMARY KEY,

    id_cabang INT REFERENCES cabang(id_cabang),

    jumlah_pesanan INT,

    total_pendapatan NUMERIC(13, 2),

    bulan INT,

    tahun INT

);
