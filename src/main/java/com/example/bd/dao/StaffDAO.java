package com.example.bd.dao;

import com.example.bd.model.Staff;
import com.example.bd.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        // **PERUBAHAN QUERY DI SINI**
        // Kita menggunakan alamat_cabang sebagai nama cabang
        String sql = "SELECT s.*, c.alamat_cabang as nama_cabang FROM staff s LEFT JOIN cabang c ON s.id_cabang = c.id_cabang ORDER BY s.id_staff ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Staff s = new Staff();
                s.setIdStaff(rs.getInt("id_staff"));
                s.setIdCabang(rs.getInt("id_cabang"));
                s.setNamaStaff(rs.getString("nama_staff"));
                s.setJabatanStaff(rs.getString("jabatan_staff"));
                s.setAlamatStaff(rs.getString("alamat_staff"));
                s.setNoTelpStaff(rs.getString("no_telp_staff"));

                Date tglJoin = rs.getDate("tgl_join_staff");
                if (tglJoin != null) {
                    s.setTglJoinStaff(tglJoin.toLocalDate());
                }

                // **BARIS BARU UNTUK MENGAMBIL NAMA CABANG**
                s.setNamaCabang(rs.getString("nama_cabang"));

                staffList.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
    // ... method addStaff, updateStaff, deleteStaff tetap sama ...
    public void addStaff(Staff staff) { /* ... kode Anda sebelumnya ... */ }
    public void updateStaff(Staff staff) { /* ... kode Anda sebelumnya ... */ }
    public void deleteStaff(int idStaff) { /* ... kode Anda sebelumnya ... */ }
}