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
        // Query diubah, umur_staff dihapus
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
                s.setNamaCabang(rs.getString("nama_cabang"));
                staffList.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public void addStaff(Staff staff) {
        // Query diubah, umur_staff dihapus
        String sql = "INSERT INTO staff (id_cabang, nama_staff, jabatan_staff, alamat_staff, no_telp_staff) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staff.getIdCabang());
            pstmt.setString(2, staff.getNamaStaff());
            pstmt.setString(3, staff.getJabatanStaff());
            pstmt.setString(4, staff.getAlamatStaff());
            pstmt.setString(5, staff.getNoTelpStaff());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStaff(Staff staff) {
        // Query diubah, umur_staff dihapus
        String sql = "UPDATE staff SET id_cabang = ?, nama_staff = ?, jabatan_staff = ?, alamat_staff = ?, no_telp_staff = ? WHERE id_staff = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staff.getIdCabang());
            pstmt.setString(2, staff.getNamaStaff());
            pstmt.setString(3, staff.getJabatanStaff());
            pstmt.setString(4, staff.getAlamatStaff());
            pstmt.setString(5, staff.getNoTelpStaff());
            pstmt.setInt(6, staff.getIdStaff());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStaff(int idStaff) {
        String sql = "DELETE FROM staff WHERE id_staff = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idStaff);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}