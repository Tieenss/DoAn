USE QuanLyHocSinh;
GO

UPDATE HocSinh
SET NienKhoa = Lop.NienKhoa
FROM HocSinh
JOIN Lop ON HocSinh.maLop = Lop.maLop
WHERE Lop.NienKhoa IS NOT NULL;
GO
