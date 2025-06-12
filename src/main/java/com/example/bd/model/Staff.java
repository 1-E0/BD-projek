package com.example.bd.model;

import java.time.LocalDate;

public class Staff {
    private int idStaff;
    private int idCabang;
    private String namaStaff;
    private String jabatanStaff;
    private String alamatStaff;
    private String noTelpStaff;
    private LocalDate tglJoinStaff;
    private String fotoProfilStaff;

    // -- FIELD BARU UNTUK NAMA CABANG --
    private String namaCabang;

    // --- Generate Getters and Setters untuk semua field ---

    public int getIdStaff() { return idStaff; }
    public void setIdStaff(int idStaff) { this.idStaff = idStaff; }

    public int getIdCabang() { return idCabang; }
    public void setIdCabang(int idCabang) { this.idCabang = idCabang; }

    public String getNamaStaff() { return namaStaff; }
    public void setNamaStaff(String namaStaff) { this.namaStaff = namaStaff; }

    public String getJabatanStaff() { return jabatanStaff; }
    public void setJabatanStaff(String jabatanStaff) { this.jabatanStaff = jabatanStaff; }

    public String getAlamatStaff() { return alamatStaff; }
    public void setAlamatStaff(String alamatStaff) { this.alamatStaff = alamatStaff; }

    public String getNoTelpStaff() { return noTelpStaff; }
    public void setNoTelpStaff(String noTelpStaff) { this.noTelpStaff = noTelpStaff; }

    public LocalDate getTglJoinStaff() { return tglJoinStaff; }
    public void setTglJoinStaff(LocalDate tglJoinStaff) { this.tglJoinStaff = tglJoinStaff; }

    public String getFotoProfilStaff() { return fotoProfilStaff; }
    public void setFotoProfilStaff(String fotoProfilStaff) { this.fotoProfilStaff = fotoProfilStaff; }

    // -- GETTER DAN SETTER UNTUK FIELD BARU --
    public String getNamaCabang() { return namaCabang; }
    public void setNamaCabang(String namaCabang) { this.namaCabang = namaCabang; }
}