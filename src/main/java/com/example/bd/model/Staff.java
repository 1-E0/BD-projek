package com.example.bd.model;

public class Staff {
    private int idStaff;
    private int idCabang;
    private String namaStaff;
    private int umurStaff; // Alterado de tglJoinStaff
    private String jabatanStaff;
    private String alamatStaff;
    private String noTelpStaff;

    // Campo auxiliar
    private String namaCabang;

    // Getters e Setters
    public int getIdStaff() { return idStaff; }
    public void setIdStaff(int idStaff) { this.idStaff = idStaff; }

    public int getIdCabang() { return idCabang; }
    public void setIdCabang(int idCabang) { this.idCabang = idCabang; }

    public String getNamaStaff() { return namaStaff; }
    public void setNamaStaff(String namaStaff) { this.namaStaff = namaStaff; }

    public int getUmurStaff() { return umurStaff; }
    public void setUmurStaff(int umurStaff) { this.umurStaff = umurStaff; }

    public String getJabatanStaff() { return jabatanStaff; }
    public void setJabatanStaff(String jabatanStaff) { this.jabatanStaff = jabatanStaff; }

    public String getAlamatStaff() { return alamatStaff; }
    public void setAlamatStaff(String alamatStaff) { this.alamatStaff = alamatStaff; }

    public String getNoTelpStaff() { return noTelpStaff; }
    public void setNoTelpStaff(String noTelpStaff) { this.noTelpStaff = noTelpStaff; }

    public String getNamaCabang() { return namaCabang; }
    public void setNamaCabang(String namaCabang) { this.namaCabang = namaCabang; }
}