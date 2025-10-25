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
--Bảng LoaiKhachHang
CREATE TABLE LoaiKhachHang (
    maLoaiKH NVARCHAR(20) PRIMARY KEY,
    tenLoaiKH NVARCHAR(100) NOT NULL ,
    moTa NVARCHAR(255) NULL -- Có thể thêm mô tả hoặc các thuộc tính khác sau này
);
-- Bảng KhachHang
CREATE TABLE KhachHang (
    maKH NVARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    email NVARCHAR(100),
	sdt VARCHAR(15) UNIQUE, -- Thêm UNIQUE để tránh trùng SĐT
    ngaySinh Date,
    maLoaiKH NVARCHAR(20) FOREIGN KEY REFERENCES LoaiKhachHang(maLoaiKH) -- Dùng khóa ngoại
);
-- Bảng TaiKhoan
CREATE TABLE TaiKhoan (
    tenDangNhap  NVARCHAR(100)  PRIMARY KEY,
    matKhau      NVARCHAR(1000) NOT NULL DEFAULT (N'123456'),
    tenHienThi   NVARCHAR(100),
    maNV         INT NOT NULL FOREIGN KEY REFERENCES NhanVien(maNV) ON DELETE CASCADE,
    loaiTaiKhoan INT NOT NULL DEFAULT (0)  -- 1: admin, 0: nhân viên
    
);
GO

-- Bảng LoaiSanPham
CREATE TABLE LoaiSanPham (
    maLoai  INT IDENTITY PRIMARY KEY,
    loaiSP NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật')
);
GO

-- Bảng SanPham
CREATE TABLE SanPham (
    maSP   INT IDENTITY PRIMARY KEY,
    tenSP  NVARCHAR(100) NOT NULL DEFAULT (N'Chưa cập nhật'),
    soLuong INT CHECK (soLuong > 0),
    donGia DECIMAL(18,2) NOT NULL DEFAULT (0) CHECK (donGia >= 0),
    img   NVARCHAR(100),
    maLoai INT NOT NULL FOREIGN KEY REFERENCES LoaiSanPham(maLoai),
    moTa NVARCHAR(200)
);
GO

-- Bảng HoaDon
CREATE TABLE HoaDon (
    maHD        INT IDENTITY PRIMARY KEY,
    maBan       INT NOT NULL FOREIGN KEY REFERENCES Ban(maBan),
    maKH NVARCHAR(20) FOREIGN KEY REFERENCES KhachHang(maKH),
    maNV INT NOT NULL FOREIGN KEY REFERENCES NhanVien(maNV),
    thoiGianVao DATETIME NOT NULL DEFAULT (GETDATE()),
    thoiGianRa  DATETIME,
    trangThai   INT NOT NULL DEFAULT (0),   -- 0: chưa thanh toán, 1: đã thanh toán
    giamGia     INT NOT NULL DEFAULT (0) CHECK (giamGia BETWEEN 0 AND 100),
    tongTien    DECIMAL(18,2) NOT NULL DEFAULT (0) CHECK (tongTien >= 0)
);
GO

-- Bảng ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maCT    INT IDENTITY PRIMARY KEY,
    maHD    INT NOT NULL FOREIGN KEY REFERENCES HoaDon(maHD) ON DELETE CASCADE,
    maSP    INT NOT NULL FOREIGN KEY REFERENCES SanPham(maSP),
    maNV    INT NOT NULL FOREIGN KEY REFERENCES NhanVien(maNV),
    soLuong INT NOT NULL DEFAULT (1) CHECK (soLuong > 0)
    
);
GO

INSERT INTO LoaiSanPham(loaiSP)
VALUES (N'Coffee'), (N'Trà'), (N'Trà sữa'), (N'Nước ép'), (N'Bánh'), (N'Khác');
GO

INSERT INTO SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Cà phê đen đá', 50, 20000, N'cf_den.png', 1, N'Đậm vị truyền thống, rang nguyên chất'),
(N'Cà phê sữa', 50, 25000, N'cf_sua.png', 1, N'Ngọt béo hài hòa từ sữa đặc Việt Nam'),
(N'Bạc xỉu', 40, 30000, N'bacxiu.jpg', 1, N'Nhiều sữa, hợp người không quen đắng'),
(N'Cà phê muối', 40, 35000, N'cf_muoi.jpg', 1, N'Hương vị mới lạ kết hợp muối tinh'),
(N'Latte', 30, 42000, N'latte.png', 1, N'Cà phê pha máy, nhiều sữa, thơm nhẹ'),
(N'Cappuccino', 30, 45000, N'capu.png', 1, N'Lớp foam dày, vị đậm vừa'),
(N'Mocha', 25, 48000, N'mocha.jpg', 1, N'Kết hợp cà phê và chocolate thơm béo');
GO

INSERT INTO SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Trà đào cam sả', 40, 35000, N'tradaocamsa.jpg', 2, N'Thanh mát, hương đào tự nhiên'),
(N'Trà chanh sả', 50, 30000, N'trachanhsa.jpg', 2, N'Vị truyền thống dễ uống'),
(N'Trà atiso đỏ', 30, 32000, N'traatiso.jpg', 2, N'Tốt cho sức khỏe, đẹp da'),
(N'Trà tắc mật ong', 50, 29000, N'tratac.jpg', 2, N'Giải khát cực đã'),
(N'Trà vải', 40, 35000, N'travai.jpg', 2, N'Ngọt nhẹ, hương vải thơm'),
(N'Trà đào truyền thống', 50, 30000, N'tradaotruyenthong.jpg', 2, N'Đào ngâm nguyên miếng'),
(N'Trà hoa nhài', 30, 28000, N'trahoanhai.jpeg', 2, N'Hương thơm thư giãn');
GO

INSERT INTO SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Trà sữa trân châu', 60, 35000, N'suatt.jpeg', 3, N'Trân châu dai mềm'),
(N'Trà sữa matcha', 50, 38000, N'suamatcha.jpg', 3, N'Vị matcha thanh đắng nhẹ'),
(N'Trà sữa đường đen', 50, 39000, N'suaduongden.jpg', 3, N'Topping caramel cực cuốn'),
(N'Trà sữa khoai môn', 40, 37000, N'suakhoaimon.jpg', 3, N'Ngọt béo, màu tím đẹp'),
(N'Trà sữa socola', 40, 36000, N'suasocola.jpg', 3, N'Ngọt dịu, trẻ em yêu thích'),
(N'Trà sữa phô mai', 35, 42000, N'suaphomai.jpg', 3, N'Lớp kem cheese béo mặn'),
(N'Trà sữa ô long', 45, 38000, N'suaolong.jpg', 3, N'Hương ô long đậm vị');
GO

INSERT INTO SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Nước ép cam', 40, 30000, N'epcam.jpg', 4, N'Cam tươi 100%'),
(N'Nước ép dứa', 30, 28000, N'epdua.jpg', 4, N'Giảm mỡ hỗ trợ tiêu hóa'),
(N'Nước ép dưa hấu', 40, 28000, N'ephau.jpg', 4, N'Lạnh, giải nhiệt nhanh'),
(N'Nước ép táo', 30, 35000, N'eptao.jpg', 4, N'Ngọt thanh, vitamin cao'),
(N'Nước ép cà rốt', 20, 28000, N'epcarot.jpg', 4, N'Tốt cho mắt & da'),
(N'Nước ép ổi', 30, 32000, N'epoi.jpg', 4, N'Nhiều vitamin C'),
(N'Nước ép chanh dây', 30, 30000, N'epchanhday.jpg', 4, N'Chua ngọt dễ uống')
GO
INSERT INTO SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Bánh phô mai',          30, 40000, N'banhphomai.png', 5, N'Vị phô mai béo mịn, thơm ngon'),
(N'Tiramisu',               25, 45000, N'banhtiramisu.jpg', 5, N'Bánh Ý nổi tiếng, mềm và đậm vị cafe'),
(N'Bánh mousse socola',     25, 42000, N'banhsocola.jpg', 5, N'Ngọt nhẹ, chocolate béo mịn'),
(N'Bánh red velvet',        20, 45000, N'banhred.jpg', 5, N'Màu đỏ đẹp mắt, vị thanh nhẹ'),
(N'Bánh su kem',            40, 15000, N'banhsocola.jpg', 5, N'Nhân kem béo mềm, rất dễ ăn'),
(N'Bánh flan caramel',      35, 20000, N'banhflan.png', 5, N'Caramel thơm, mềm tan'),
(N'Bánh cookies bơ',        50, 10000, N'banhcookies.jpg', 5, N'Giòn rụm, thơm mùi bơ');
GO
INSERT INTO SanPham(tenSP, soLuong, donGia, img, maLoai, moTa)
VALUES
(N'Trân châu đen',     100, 8000,  N'khacchantrau.jpg', 6, N'Trân châu dai giòn, thêm vào trà sữa'),
(N'Trân châu trắng',   100, 9000,  N'khacchanchautrang.jpg', 6, N'Thơm nhẹ vị sữa'),
(N'Thạch phô mai',     100, 10000, N'khacphomai.jpg', 6, N'Thạch nhân cheese béo mặn'),
(N'Kem cheese',        80,  12000, N'khackem.jpg', 6, N'Topping béo mặn, siêu cuốn'),
(N'Thạch dừa',         90,  8000,  N'khacthachdua.jpg', 6, N'Giòn nhẹ, mát lạnh')
GO
