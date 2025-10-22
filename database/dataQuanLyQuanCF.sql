USE master;
GO

-- DROP DATABASE QuanLyQuanCF;
-- GO

CREATE DATABASE QuanLyQuanCF;
GO

USE QuanLyQuanCF;
GO

-- Bảng Ban
CREATE TABLE Ban (
    maBan     INT IDENTITY PRIMARY KEY,
    tenBan    NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật'),
    trangThai NVARCHAR(100) NOT NULL DEFAULT (N'Trống')  -- Trống / Có người
);
GO

-- Bảng NhanVien
CREATE TABLE NhanVien (
    maNV       INT IDENTITY PRIMARY KEY,
    hoTen      NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật'),
    diaChi     NVARCHAR(255),
    dienThoai  NVARCHAR(20),
    CCCD       NVARCHAR(20) NOT NULL,
    ngayVaoLam DATE DEFAULT (GETDATE()),
    chucVu     NVARCHAR(50) DEFAULT (N'Nhân viên')
);
GO

-- Bảng TaiKhoan
CREATE TABLE TaiKhoan (
    tenDangNhap  NVARCHAR(100)  PRIMARY KEY,
    matKhau      NVARCHAR(1000) NOT NULL DEFAULT (N'123456'),
    tenHienThi   NVARCHAR(100),
    maNV         INT NOT NULL,
    loaiTaiKhoan INT NOT NULL DEFAULT (0),  -- 1: admin, 0: nhân viên
    FOREIGN KEY (maNV) REFERENCES dbo.NhanVien(maNV) ON DELETE CASCADE
);
GO

-- Bảng LoaiSanPham
CREATE TABLE LoaiSanPham (
    maLoai  INT IDENTITY PRIMARY KEY,
    tenLoai NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật')
);
GO

-- Bảng SanPham
CREATE TABLE SanPham (
    maSP   INT IDENTITY PRIMARY KEY,
    tenSP  NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật'),
    maLoai INT NOT NULL DEFAULT (1),
    donGia DECIMAL(18,2) NOT NULL DEFAULT (0),
    FOREIGN KEY (maLoai) REFERENCES dbo.LoaiSanPham(maLoai)
);
GO

-- Bảng HoaDon
CREATE TABLE HoaDon (
    maHD        INT IDENTITY PRIMARY KEY,
    maBan       INT NOT NULL,
    thoiGianVao DATETIME NOT NULL DEFAULT (GETDATE()),
    thoiGianRa  DATETIME,
    trangThai   INT NOT NULL DEFAULT (0),   -- 0: chưa thanh toán, 1: đã thanh toán
    giamGia     INT NOT NULL DEFAULT (0),
    tongTien    DECIMAL(18,2) NOT NULL DEFAULT (0),
    FOREIGN KEY (maBan) REFERENCES dbo.Ban(maBan)
);
GO

-- Bảng ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maCT    INT IDENTITY PRIMARY KEY,
    maHD    INT NOT NULL,
    maSP    INT NOT NULL,
    maNV    INT NOT NULL,
    soLuong INT NOT NULL DEFAULT (0),
    FOREIGN KEY (maHD) REFERENCES dbo.HoaDon(maHD),
    FOREIGN KEY (maSP) REFERENCES dbo.SanPham(maSP),
    FOREIGN KEY (maNV) REFERENCES dbo.NhanVien(maNV)
);
GO
