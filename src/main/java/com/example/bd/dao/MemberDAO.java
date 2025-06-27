package com.example.bd.dao;

import com.example.bd.model.Hadiah;
import com.example.bd.model.Member;
import com.example.bd.model.RiwayatPenukaran;
import com.example.bd.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; // Pastikan import java.sql.Date
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    /**
     * Mengambil data member berdasarkan ID pelanggan.
     * @param idPelanggan ID dari pelanggan.
     * @return Objek Member jika ditemukan, null jika tidak.
     */
    public Member getMemberByPelangganId(int idPelanggan) {
        String sql = "SELECT * FROM member WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Member m = new Member();
                m.setIdMember(rs.getInt("id_member"));
                m.setIdPelanggan(rs.getInt("id_pelanggan"));
                m.setNamaMember(rs.getString("nama_member"));
                m.setTanggalJoin(rs.getDate("tanggal_join"));
                m.setJumlahPoin(rs.getInt("jumlah_poin"));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Memproses penukaran poin dengan hadiah.
     * Metode ini mengurangi poin member, mencatat riwayat, dan membuat voucher jika jenis hadiahnya adalah 'VOUCHER'.
     * @param idMember ID dari member yang menukarkan.
     * @param idPelanggan ID pelanggan (diperlukan untuk membuat voucher).
     * @param hadiah Objek Hadiah yang ditukarkan.
     * @throws SQLException Jika terjadi error transaksi database.
     */
    public void tukarPoin(int idMember, int idPelanggan, Hadiah hadiah) throws SQLException {
        String sqlUpdatePoin = "UPDATE member SET jumlah_poin = jumlah_poin - ? WHERE id_member = ?";
        String sqlLogPenukaran = "INSERT INTO penukaran_hadiah (id_member, id_hadiah, tanggal_penukaran) VALUES (?, ?, CURRENT_DATE)";
        String sqlBuatVoucher = "INSERT INTO voucher_pelanggan (id_pelanggan, id_hadiah, kode_voucher, tanggal_kadaluarsa) VALUES (?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            // 1. Kurangi Poin Member
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdatePoin)) {
                pstmtUpdate.setInt(1, hadiah.getBiayaPoin());
                pstmtUpdate.setInt(2, idMember);
                pstmtUpdate.executeUpdate();
            }

            // 2. Catat di Riwayat Penukaran
            try (PreparedStatement pstmtLog = conn.prepareStatement(sqlLogPenukaran)) {
                pstmtLog.setInt(1, idMember);
                pstmtLog.setInt(2, hadiah.getIdHadiah());
                pstmtLog.executeUpdate();
            }

            // 3. Jika jenisnya VOUCHER, buat voucher baru untuk pelanggan
            if ("VOUCHER".equals(hadiah.getJenisHadiah())) {
                try (PreparedStatement pstmtVoucher = conn.prepareStatement(sqlBuatVoucher)) {
                    // Generate kode voucher unik (contoh sederhana)
                    String kodeVoucher = "VC" + System.currentTimeMillis();

                    // --- PERBAIKAN DI SINI ---
                    // Mengganti karakter ilegal "৩০" dengan "30"
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
            throw e; // Lemparkan exception agar bisa ditangani di controller
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Mengambil riwayat penukaran hadiah yang pernah dilakukan oleh seorang member.
     * @param idMember ID dari member.
     * @return List berisi objek RiwayatPenukaran.
     */
    public List<RiwayatPenukaran> getRiwayatPenukaranByIdMember(int idMember) {
        List<RiwayatPenukaran> riwayatList = new ArrayList<>();
        String sql = "SELECT h.nama_hadiah, h.biaya_poin, p.tanggal_penukaran " +
                "FROM penukaran_hadiah p " +
                "JOIN hadiah h ON p.id_hadiah = h.id_hadiah " +
                "WHERE p.id_member = ? " +
                "ORDER BY p.tanggal_penukaran DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMember);
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