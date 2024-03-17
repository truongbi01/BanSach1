package com.example.banhang.View.RecycleViewDonHang;

import android.content.ContentValues;
import com.example.banhang.database.*;

public class DonHang {
    private int maDonHang;
    private String ngayDatHang;
    private String tenNguoiDung;
    private double tongTien;
    private int trangThai;

    // Constructor
    public DonHang( String ngayDatHang, String tenNguoiDung, double tongTien, int trangThai) {
        this.ngayDatHang = ngayDatHang;
        this.tenNguoiDung = tenNguoiDung;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }
    public DonHang(int maDonHang, String ngayDatHang, String tenNguoiDung, double tongTien, int trangThai) {
        this.maDonHang = maDonHang;
        this.ngayDatHang = ngayDatHang;
        this.tenNguoiDung = tenNguoiDung;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_NGAY_DAT_HANG, ngayDatHang);
        values.put(CreateDatabase.CL_TEN_NGUOI_DUNG, tenNguoiDung);
        values.put(CreateDatabase.CL_TOTAL, tongTien);
        values.put(CreateDatabase.CL_TRANG_THAI, trangThai);
        return values;
    }

    // Getter và setter cho các thuộc tính
    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getNgayDatHang() {
        return ngayDatHang;
    }

    public void setNgayDatHang(String ngayDatHang) {
        this.ngayDatHang = ngayDatHang;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
