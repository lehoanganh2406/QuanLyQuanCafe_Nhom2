USE master;
GO

-- DROP DATABASE QuanLyQuanCF;
-- GO

CREATE DATABASE QuanLyQuanCF;
GO

USE QuanLyQuanCF;
GO
-- TẠO SEQUENCE CHO CÁC MÃ TỰ SINH
CREATE SEQUENCE seq_Ban AS INT START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_NhanVien AS INT START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_KhachHang AS INT START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_LoaiSanPham AS INT START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_SanPham AS INT START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_HoaDon AS INT START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_ChiTietHoaDon AS INT START WITH 1 INCREMENT BY 1;
GO

CREATE TABLE Ban (
    maBan NVARCHAR(20) PRIMARY KEY DEFAULT(CONCAT('B', RIGHT('000' + CAST(NEXT VALUE FOR seq_Ban AS VARCHAR(3)), 3))),
    tenBan NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật'),
    trangThai NVARCHAR(100) NOT NULL DEFAULT (N'Trống')
);
GO

CREATE TABLE NhanVien (
    maNV NVARCHAR(20) PRIMARY KEY DEFAULT(CONCAT('NV', RIGHT('000' + CAST(NEXT VALUE FOR seq_NhanVien AS VARCHAR(3)), 3))),
    hoTen NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật'),
    diaChi NVARCHAR(255),
    dienThoai NVARCHAR(20),
    CCCD NVARCHAR(20) NOT NULL,
    ngayVaoLam DATE DEFAULT (GETDATE()),
    chucVu NVARCHAR(50) DEFAULT (N'Nhân viên')
);
GO

CREATE TABLE KhachHang (
    maKH NVARCHAR(20) PRIMARY KEY DEFAULT(CONCAT('KH', RIGHT('000' + CAST(NEXT VALUE FOR seq_KhachHang AS VARCHAR(3)), 3))),
    tenKH NVARCHAR(100) NOT NULL,
    sdt NVARCHAR(15) UNIQUE,
    diemTL INT
);
GO

CREATE TABLE TaiKhoan (
    tenDangNhap NVARCHAR(100) PRIMARY KEY,
    matKhau NVARCHAR(1000) NOT NULL DEFAULT (N'123456'),
    tenHienThi NVARCHAR(100),
    maNV NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES NhanVien(maNV) ON DELETE CASCADE,
    loaiTaiKhoan INT NOT NULL DEFAULT (0)
);
GO

CREATE TABLE LoaiSanPham (
    maLoai NVARCHAR(20) PRIMARY KEY DEFAULT(CONCAT('LSP', RIGHT('000' + CAST(NEXT VALUE FOR seq_LoaiSanPham AS VARCHAR(3)), 3))),
    loaiSP NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật')
);
GO

CREATE TABLE SanPham (
    maSP NVARCHAR(20) PRIMARY KEY DEFAULT(CONCAT('SP', RIGHT('000' + CAST(NEXT VALUE FOR seq_SanPham AS VARCHAR(3)), 3))),
    tenSP NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật'),
    soLuong INT CHECK (soLuong >= 0),
    donGia DECIMAL(18,2) NOT NULL DEFAULT (0),
    img NVARCHAR(100),
    maLoai NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES LoaiSanPham(maLoai),
    moTa NVARCHAR(200)
);
GO

CREATE TABLE HoaDon (
    maHD NVARCHAR(20) PRIMARY KEY DEFAULT(CONCAT('HD', RIGHT('000' + CAST(NEXT VALUE FOR seq_HoaDon AS VARCHAR(3)), 3))),
    maBan NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES Ban(maBan),
    maKH NVARCHAR(20) FOREIGN KEY REFERENCES KhachHang(maKH),
    maNV NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES NhanVien(maNV),
    thoiGianVao DATETIME NOT NULL DEFAULT (GETDATE()),
    thoiGianRa DATETIME,
    trangThai INT NOT NULL DEFAULT (0),
    giamGia INT NOT NULL DEFAULT (0) CHECK (giamGia BETWEEN 0 AND 100),
    tongTien DECIMAL(18,2) NOT NULL DEFAULT (0)
);
GO

CREATE TABLE ChiTietHoaDon (
    maCT NVARCHAR(20) PRIMARY KEY DEFAULT(CONCAT('CT', RIGHT('000' + CAST(NEXT VALUE FOR seq_ChiTietHoaDon AS VARCHAR(3)), 3))),
    maHD NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES HoaDon(maHD) ON DELETE CASCADE,
    maSP NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES SanPham(maSP),
    maNV NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES NhanVien(maNV),
    soLuong INT NOT NULL DEFAULT (1) CHECK (soLuong > 0)
);
GO


INSERT INTO LoaiSanPham(loaiSP)
VALUES (N'Coffee'), (N'Trà'), (N'Trà sữa'), (N'Nước ép'), (N'Bánh'), (N'Khác');
/* Lấy mã loại theo tên để dùng khi chèn sản phẩm */
DECLARE @LSP_Coffee NVARCHAR(20) = (SELECT maLoai FROM dbo.LoaiSanPham WHERE loaiSP = N'Coffee');
DECLARE @LSP_Tra    NVARCHAR(20) = (SELECT maLoai FROM dbo.LoaiSanPham WHERE loaiSP = N'Trà');
DECLARE @LSP_TraSua NVARCHAR(20) = (SELECT maLoai FROM dbo.LoaiSanPham WHERE loaiSP = N'Trà sữa');
DECLARE @LSP_NuocEp NVARCHAR(20) = (SELECT maLoai FROM dbo.LoaiSanPham WHERE loaiSP = N'Nước ép');
DECLARE @LSP_Banh   NVARCHAR(20) = (SELECT maLoai FROM dbo.LoaiSanPham WHERE loaiSP = N'Bánh');
DECLARE @LSP_Khac   NVARCHAR(20) = (SELECT maLoai FROM dbo.LoaiSanPham WHERE loaiSP = N'Khác');

-- Coffee
INSERT INTO dbo.SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Cà phê đen đá', 50, 20000, N'cf_den.png', @LSP_Coffee, N'Đậm vị truyền thống, rang nguyên chất'),
(N'Cà phê sữa',    50, 25000, N'cf_sua.png', @LSP_Coffee, N'Ngọt béo hài hòa từ sữa đặc Việt Nam'),
(N'Bạc xỉu',       40, 30000, N'bacxiu.jpg', @LSP_Coffee, N'Nhiều sữa, hợp người không quen đắng'),
(N'Cà phê muối',   40, 35000, N'cf_muoi.jpg',@LSP_Coffee, N'Hương vị mới lạ kết hợp muối tinh'),
(N'Latte',         30, 42000, N'latte.png',  @LSP_Coffee, N'Cà phê pha máy, nhiều sữa, thơm nhẹ'),
(N'Cappuccino',    30, 45000, N'capu.png',   @LSP_Coffee, N'Lớp foam dày, vị đậm vừa'),
(N'Mocha',         25, 48000, N'mocha.jpg',  @LSP_Coffee, N'Kết hợp cà phê và chocolate thơm béo');

-- Trà
INSERT INTO dbo.SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Trà đào cam sả',       40, 35000, N'tradaocamsa.jpg',      @LSP_Tra, N'Thanh mát, hương đào tự nhiên'),
(N'Trà chanh sả',         50, 30000, N'trachanhsa.jpg',       @LSP_Tra, N'Vị truyền thống dễ uống'),
(N'Trà atiso đỏ',         30, 32000, N'traatiso.jpg',         @LSP_Tra, N'Tốt cho sức khỏe, đẹp da'),
(N'Trà tắc mật ong',      50, 29000, N'tratac.jpg',           @LSP_Tra, N'Giải khát cực đã'),
(N'Trà vải',              40, 35000, N'travai.jpg',           @LSP_Tra, N'Ngọt nhẹ, hương vải thơm'),
(N'Trà đào truyền thống', 50, 30000, N'tradaotruyenthong.jpg',@LSP_Tra, N'Đào ngâm nguyên miếng'),
(N'Trà hoa nhài',         30, 28000, N'trahoanhai.jpeg',      @LSP_Tra, N'Hương thơm thư giãn');

-- Trà sữa
INSERT INTO dbo.SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Trà sữa trân châu', 60, 35000, N'suatt.jpeg',     @LSP_TraSua, N'Trân châu dai mềm'),
(N'Trà sữa matcha',    50, 38000, N'suamatcha.jpg',  @LSP_TraSua, N'Vị matcha thanh đắng nhẹ'),
(N'Trà sữa đường đen', 50, 39000, N'suaduongden.jpg',@LSP_TraSua, N'Topping caramel cực cuốn'),
(N'Trà sữa khoai môn', 40, 37000, N'suakhoaimon.jpg',@LSP_TraSua, N'Ngọt béo, màu tím đẹp'),
(N'Trà sữa socola',    40, 36000, N'suasocola.jpg',  @LSP_TraSua, N'Ngọt dịu, trẻ em yêu thích'),
(N'Trà sữa phô mai',   35, 42000, N'suaphomai.jpg',  @LSP_TraSua, N'Lớp kem cheese béo mặn'),
(N'Trà sữa ô long',    45, 38000, N'suaolong.jpg',   @LSP_TraSua, N'Hương ô long đậm vị');

-- Nước ép
INSERT INTO dbo.SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Nước ép cam',        40, 30000, N'epcam.jpg',     @LSP_NuocEp, N'Cam tươi 100%'),
(N'Nước ép dứa',        30, 28000, N'epdua.jpg',     @LSP_NuocEp, N'Giảm mỡ hỗ trợ tiêu hóa'),
(N'Nước ép dưa hấu',    40, 28000, N'ephau.jpg',     @LSP_NuocEp, N'Lạnh, giải nhiệt nhanh'),
(N'Nước ép táo',        30, 35000, N'eptao.jpg',     @LSP_NuocEp, N'Ngọt thanh, vitamin cao'),
(N'Nước ép cà rốt',     20, 28000, N'epcarot.jpg',   @LSP_NuocEp, N'Tốt cho mắt & da'),
(N'Nước ép ổi',         30, 32000, N'epoi.jpg',      @LSP_NuocEp, N'Nhiều vitamin C'),
(N'Nước ép chanh dây',  30, 30000, N'epchanhday.jpg',@LSP_NuocEp, N'Chua ngọt dễ uống');

-- Bánh
INSERT INTO dbo.SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Bánh phô mai',       30, 40000, N'banhphomai.png',   @LSP_Banh, N'Vị phô mai béo mịn, thơm ngon'),
(N'Tiramisu',           25, 45000, N'banhtiramisu.jpg', @LSP_Banh, N'Bánh Ý nổi tiếng, mềm và đậm vị cafe'),
(N'Bánh mousse socola', 25, 42000, N'banhsocola.jpg',   @LSP_Banh, N'Ngọt nhẹ, chocolate béo mịn'),
(N'Bánh red velvet',    20, 45000, N'banhred.jpg',      @LSP_Banh, N'Màu đỏ đẹp mắt, vị thanh nhẹ'),
(N'Bánh su kem',        40, 15000, N'banhsocola.jpg',   @LSP_Banh, N'Nhân kem béo mềm, rất dễ ăn'),
(N'Bánh flan caramel',  35, 20000, N'banhflan.png',     @LSP_Banh, N'Caramel thơm, mềm tan'),
(N'Bánh cookies bơ',    50, 10000, N'banhcookies.jpg',  @LSP_Banh, N'Giòn rụm, thơm mùi bơ');

-- Khác (topping)
INSERT INTO dbo.SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Trân châu đen',   100,  8000, N'khacchantrau.jpg',      @LSP_Khac, N'Trân châu dai giòn, thêm vào trà sữa'),
(N'Trân châu trắng', 100,  9000, N'khacchanchautrang.jpg', @LSP_Khac, N'Thơm nhẹ vị sữa'),
(N'Thạch phô mai',   100, 10000, N'khacphomai.jpg',        @LSP_Khac, N'Thạch nhân cheese béo mặn'),
(N'Kem cheese',       80, 12000, N'khackem.jpg',           @LSP_Khac, N'Topping béo mặn, siêu cuốn'),
(N'Thạch dừa',        90,  8000, N'khacthachdua.jpg',      @LSP_Khac, N'Giòn nhẹ, mát lạnh');

GO

INSERT INTO dbo.KhachHang (tenKH, sdt, diemTL)
VALUES
(N'Nguyễn Văn An',      N'0901000001', 120),
(N'Trần Thị Bích',      N'0901000002',  80),
(N'Lê Hoàng Anh',       N'0901000003', 200),
(N'Phạm Minh Khoa',     N'0901000004',  50),
(N'Võ Thị Thu Trang',   N'0901000005',  30),
(N'Đỗ Thanh Tùng',      N'0901000006', 150),
(N'Bùi Ngọc Hân',       N'0901000007',  70),
(N'Huỳnh Đức Thịnh',    N'0901000008',  95),
(N'Phan Thảo Nhi',      N'0901000009',  40),
(N'Ngô Quang Huy',      N'0901000010', 110);
GO

SELECT * FROM Ban;
SELECT * FROM SanPham;
SELECT * FROM NhanVien;
SELECT * FROM HoaDon;
SELECT * FROM KhachHang;
SELECT * FROM TaiKhoan;
SELECT * FROM LoaiSanPham;
SELECT * FROM ChiTietHoaDon;
GO

