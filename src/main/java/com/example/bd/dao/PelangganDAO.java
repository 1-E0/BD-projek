package com.example.bd.dao;

import com.example.bd.model.Hadiah;
import com.example.bd.model.Pelanggan;
import com.example.bd.model.RiwayatPenukaran;
import com.example.bd.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void tukarPoin(int idPelanggan, Hadiah hadiah) throws SQLException {
        String sqlUpdatePoin = "UPDATE pelanggan SET jumlah_poin = jumlah_poin - ? WHERE id_pelanggan = ?";
        String sqlLogPenukaran = "INSERT INTO penukaran_hadiah (id_pelanggan, id_hadiah, tanggal_penukaran) VALUES (?, ?, CURRENT_DATE)";
        String sqlBuatVoucher = "INSERT INTO voucher_pelanggan (id_pelanggan, id_hadiah, kode_voucher, tanggal_kadaluarsa) VALUES (?, ?, ?, ?)";
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdatePoin)) {
                pstmtUpdate.setInt(1, hadiah.getBiayaPoin());
                pstmtUpdate.setInt(2, idPelanggan);
                pstmtUpdate.executeUpdate();
            }

            try (PreparedStatement pstmtLog = conn.prepareStatement(sqlLogPenukaran)) {
                pstmtLog.setInt(1, idPelanggan);
                pstmtLog.setInt(2, hadiah.getIdHadiah());
                pstmtLog.executeUpdate();
            }

            if ("VOUCHER".equals(hadiah.getJenisHadiah())) {
                try (PreparedStatement pstmtVoucher = conn.prepareStatement(sqlBuatVoucher)) {
                    String kodeVoucher = "VC" + System.currentTimeMillis();
                    long tigaPuluhHariInMillis = 30L * 24 * 60 * 60 * 1000;
                    Date tglKadaluarsa = new Date(System.currentTimeMillis() + tigaPuluhHariInMillis);
                    pstmtVoucher.setInt(1, idPelanggan);
                    pstmtVoucher.setInt(2, hadiah.getIdHadiah());
                    pstmtVoucher.setString(3, kodeVoucher);
                    pstmtVoucher.setDate(4, tglKadaluarsa);
                    pstmtVoucher.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<RiwayatPenukaran> getRiwayatPenukaran(int idPelanggan) {
        List<RiwayatPenukaran> riwayatList = new ArrayList<>();
        String sql = "SELECT h.nama_hadiah, h.biaya_poin, p.tanggal_penukaran " +
                "FROM penukaran_hadiah p JOIN hadiah h ON p.id_hadiah = h.id_hadiah " +
                "WHERE p.id_pelanggan = ? ORDER BY p.tanggal_penukaran DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                RiwayatPenukaran riwayat = new RiwayatPenukaran();
                riwayat.setNamaHadiah(rs.getString("nama_hadiah"));
                riwayat.setPoinDigunakan(rs.getInt("biaya_poin"));
                riwayat.setTanggalPenukaran(rs.getDate("tanggal_penukaran"));
                riwayatList.add(riwayat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return riwayatList;
    }
}