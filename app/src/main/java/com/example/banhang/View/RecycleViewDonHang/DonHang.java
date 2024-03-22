package com.example.banhang.View.RecycleViewDonHang;

import android.content.ContentValues;
import android.content.Context;

import com.example.banhang.database.*;

import java.util.ArrayList;

public class DonHang {
    private String maDonHang;
    private String ngayDatHang;
    private String tenNguoiDung;
    private double tongTien;
    private int trangThai;
    private int soLuong;



    private ArrayList<String> productIds;
    private String idSanPham;



    private String tenSanPham;
    private String moTa;
    private String giaTien;
    private String anhSanPham;
    private String tenDangNhap;

    public String getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(String idSanPham) {
        this.idSanPham = idSanPham;
    }

    public DonHang(){};
    // Constructor
    public DonHang( String ngayDatHang, String tenNguoiDung, double tongTien, int trangThai, String idSanPham) {
        this.ngayDatHang = ngayDatHang;
        this.tenNguoiDung = tenNguoiDung;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.idSanPham = idSanPham;
        this.productIds = new ArrayList<>(); // Khởi tạo danh sách sản phẩm
    }
    public DonHang( String ngayDatHang, String tenNguoiDung, double tongTien, int trangThai, String idSanPham,int soLuong) {
        this.ngayDatHang = ngayDatHang;
        this.tenNguoiDung = tenNguoiDung;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.idSanPham = idSanPham;
        this.soLuong = soLuong;
        this.productIds = new ArrayList<>(); // Khởi tạo danh sách sản phẩm

    }
    public DonHang(String maDonHang, String ngayDatHang, String tenNguoiDung, double tongTien, int trangThai,String idSanPham) {
        this.maDonHang = maDonHang;
        this.ngayDatHang = ngayDatHang;
        this.tenNguoiDung = tenNguoiDung;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.idSanPham = idSanPham;

    }
    public DonHang(Context context, String maDonHang, String ngayDatHang, String tenNguoiDung, double tongTien, int trangThai, String idSanPham) {
        this.maDonHang = maDonHang;
        this.ngayDatHang = ngayDatHang;
        this.tenNguoiDung = tenNguoiDung;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.idSanPham = idSanPham;

    }
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_NGAY_DAT_HANG, ngayDatHang);
        values.put(CreateDatabase.CL_TEN_NGUOI_DUNG, tenNguoiDung);
        values.put(CreateDatabase.CL_TOTAL, tongTien);
        values.put(CreateDatabase.CL_TRANG_THAI, trangThai);
        values.put(CreateDatabase.CL_SAN_PHAM_ID_Donhang,idSanPham);
        values.put(CreateDatabase.CL_SO_LUONG,soLuong);
        return values;
    }

    // Getter và setter cho các thuộc tính
    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }
    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    public void addProductId(String productId) {
        this.productIds.add(productId);
    }
    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
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
    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(String giaTien) {
        this.giaTien = giaTien;
    }

    public String getAnhSanPham() {
        return anhSanPham;
    }

    public void setAnhSanPham(String anhSanPham) {
        this.anhSanPham = anhSanPham;
    }
}
