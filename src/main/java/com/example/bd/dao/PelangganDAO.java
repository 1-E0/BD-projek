package com.example.bd.dao;

import com.example.bd.model.Pelanggan;
import com.example.bd.model.PelangganVoucher;
import com.example.bd.model.Voucher;
import com.example.bd.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PelangganDAO {
    private final Connection conn = DatabaseConnection.getConnection();


    public List<Pelanggan> getAllPelanggan() {
        List<Pelanggan> pelangganList = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan ORDER BY id_pelanggan ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pelanggan p = new Pelanggan();
                p.setIdPelanggan(rs.getInt("id_pelanggan"));
                p.setNamaPelanggan(rs.getString("nama_pelanggan"));
                p.setEmailPelanggan(rs.getString("email_pelanggan"));
                p.setAlamatPelanggan(rs.getString("alamat_pelanggan"));
                p.setNoTelpPelanggan(rs.getString("no_telp_pelanggan"));
                pelangganList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pelangganList;
    }

    public void updateProfil(Pelanggan pelanggan) {
        String sql = "UPDATE pelanggan SET nama_pelanggan = ?, email_pelanggan = ?, alamat_pelanggan = ?, no_telp_pelanggan = ? WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelanggan.getNamaPelanggan());
            pstmt.setString(2, pelanggan.getEmailPelanggan());
            pstmt.setString(3, pelanggan.getAlamatPelanggan());
            pstmt.setString(4, pelanggan.getNoTelpPelanggan());
            pstmt.setInt(5, pelanggan.getIdPelanggan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(int idPelanggan, String passwordBaru) {
        String sql = "UPDATE pelanggan SET password_pelanggan = ? WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, passwordBaru);
            pstmt.setInt(2, idPelanggan);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPelanggan(Pelanggan pelanggan) throws SQLException {
        String sql = "INSERT INTO pelanggan (nama_pelanggan, email_pelanggan, password_pelanggan, alamat_pelanggan, no_telp_pelanggan, jumlah_poin, tanggal_join) VALUES (?, ?, ?, ?, ?, 0, CURRENT_DATE)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelanggan.getNamaPelanggan());
            pstmt.setString(2, pelanggan.getEmailPelanggan());
            pstmt.setString(3, pelanggan.getPasswordPelanggan());
            pstmt.setString(4, pelanggan.getAlamatPelanggan());
            pstmt.setString(5, pelanggan.getNoTelpPelanggan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updatePelanggan(Pelanggan pelanggan) {
        String sql = "UPDATE pelanggan SET nama_pelanggan = ?, email_pelanggan = ?, alamat_pelanggan = ?, no_telp_pelanggan = ? WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelanggan.getNamaPelanggan());
            pstmt.setString(2, pelanggan.getEmailPelanggan());
            pstmt.setString(3, pelanggan.getAlamatPelanggan());
            pstmt.setString(4, pelanggan.getNoTelpPelanggan());
            pstmt.setInt(5, pelanggan.getIdPelanggan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePelanggan(int idPelanggan) {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pelanggan validateLogin(String email, String password) {
        String sql = "SELECT * FROM pelanggan WHERE email_pelanggan = ? AND password_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Pelanggan p = new Pelanggan();
                p.setIdPelanggan(rs.getInt("id_pelanggan"));
                p.setNamaPelanggan(rs.getString("nama_pelanggan"));
                p.setEmailPelanggan(rs.getString("email_pelanggan"));
                p.setAlamatPelanggan(rs.getString("alamat_pelanggan"));
                p.setNoTelpPelanggan(rs.getString("no_telp_pelanggan"));
                p.setJumlahPoin(rs.getInt("jumlah_poin"));
                p.setTanggalJoin(rs.getDate("tanggal_join"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEmailRegistered(String email) {
        String sql = "SELECT COUNT(*) FROM pelanggan WHERE email_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean tukarPoinDenganVoucher(int idPelanggan, Voucher voucher) throws SQLException {
        String sqlCheckPoin = "SELECT jumlah_poin FROM pelanggan WHERE id_pelanggan = ?";
        String sqlUpdatePoin = "UPDATE pelanggan SET jumlah_poin = jumlah_poin - ? WHERE id_pelanggan = ?";
        String sqlBuatVoucher = "INSERT INTO pelanggan_voucher (id_pelanggan, id_voucher, kode_voucher, tanggal_kadaluarsa) VALUES (?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);


            int poinSaatIni;
            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheckPoin)) {
                pstmtCheck.setInt(1, idPelanggan);
                ResultSet rs = pstmtCheck.executeQuery();
                if (rs.next()) {
                    poinSaatIni = rs.getInt("jumlah_poin");
                } else {
                    conn.rollback();
                    return false;
                }
            }

            if (poinSaatIni < voucher.getPoinDibutuhkan()) {
                conn.rollback();
                return false;
            }


            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdatePoin)) {
                pstmtUpdate.setInt(1, voucher.getPoinDibutuhkan());
                pstmtUpdate.setInt(2, idPelanggan);
                pstmtUpdate.executeUpdate();
            }


            try (PreparedStatement pstmtVoucher = conn.prepareStatement(sqlBuatVoucher)) {
                String kodeVoucherUnik = "VC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                long tigaPuluhHariInMillis = 30L * 24 * 60 * 60 * 1000;
                Date tglKadaluarsa = new Date(System.currentTimeMillis() + tigaPuluhHariInMillis);

                pstmtVoucher.setInt(1, idPelanggan);
                pstmtVoucher.setInt(2, voucher.getIdVoucher());
                pstmtVoucher.setString(3, kodeVoucherUnik);
                pstmtVoucher.setDate(4, tglKadaluarsa);
                pstmtVoucher.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<PelangganVoucher> getRiwayatPenukaranVoucher(int idPelanggan) {
        List<PelangganVoucher> riwayatList = new ArrayList<>();
        String sql = "SELECT pv.*, v.nama_voucher, v.poin_dibutuhkan " +
                "FROM pelanggan_voucher pv " +
                "JOIN voucher v ON pv.id_voucher = v.id_voucher " +
                "WHERE pv.id_pelanggan = ? ORDER BY pv.tanggal_penukaran DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PelangganVoucher riwayat = new PelangganVoucher();
                riwayat.setKodeVoucher(rs.getString("kode_voucher"));
                riwayat.setTanggalPenukaran(rs.getDate("tanggal_penukaran"));
                riwayat.setStatusVoucher(rs.getString("status_voucher"));
                riwayat.setNamaVoucher(rs.getString("nama_voucher"));
                riwayat.setPoinDigunakan(rs.getInt("poin_dibutuhkan"));
                riwayatList.add(riwayat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return riwayatList;
    }

    public List<PelangganVoucher> getVoucherAktifByPelanggan(int idPelanggan) {
        List<PelangganVoucher> voucherList = new ArrayList<>();
        String sql = "SELECT pv.*, v.nama_voucher, v.potongan_harga " +
                "FROM pelanggan_voucher pv " +
                "JOIN voucher v ON pv.id_voucher = v.id_voucher " +
                "WHERE pv.id_pelanggan = ? AND pv.status_voucher = 'AKTIF' " +
                "AND pv.tanggal_kadaluarsa >= CURRENT_DATE";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PelangganVoucher pv = new PelangganVoucher();
                pv.setIdPelangganVoucher(rs.getInt("id_pelanggan_voucher"));
                pv.setNamaVoucher(rs.getString("nama_voucher"));
                pv.setPotonganHarga(rs.getDouble("potongan_harga"));
                voucherList.add(pv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voucherList;
    }

    public void gunakanVoucher(int idPelangganVoucher) {
        String sql = "UPDATE pelanggan_voucher SET status_voucher = 'DIGUNAKAN' WHERE id_pelanggan_voucher = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelangganVoucher);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pelanggan getPelangganById(int idPelanggan) {
        String sql = "SELECT * FROM pelanggan WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Pelanggan p = new Pelanggan();
                p.setIdPelanggan(rs.getInt("id_pelanggan"));
                p.setNamaPelanggan(rs.getString("nama_pelanggan"));
                p.setEmailPelanggan(rs.getString("email_pelanggan"));
                p.setAlamatPelanggan(rs.getString("alamat_pelanggan"));
                p.setNoTelpPelanggan(rs.getString("no_telp_pelanggan"));
                p.setJumlahPoin(rs.getInt("jumlah_poin"));
                p.setTanggalJoin(rs.getDate("tanggal_join"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}