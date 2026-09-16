USE QuanLyHocSinh;
GO

-- Cập nhật NienKhoa cho các học sinh chưa có (lấy từ bảng Lop)
UPDATE HocSinh
SET NienKhoa = Lop.NienKhoa
FROM HocSinh
JOIN Lop ON HocSinh.maLop = Lop.maLop
WHERE HocSinh.NienKhoa IS NULL AND Lop.NienKhoa IS NOT NULL;
GO

-- ==========================================
-- 1. ĐỒNG BỘ DỮ LIỆU CŨ (Cho những học sinh đã tồn tại nhưng thiếu bảng điểm)
-- ==========================================
-- Tự động thêm điểm cho tất cả môn học, 2 học kỳ, năm học = niên khóa.
INSERT INTO Diem (MaHS, MaMH, HocKy, NamHoc, Diem15p, Diem1Tiet, DiemGiuaKy, DiemCuoiKy, DiemTongKet)
SELECT 
    hs.maHS, 
    mh.MaMH, 
    hk.HocKy, 
    hs.NienKhoa,
    NULL, NULL, NULL, NULL, NULL
FROM HocSinh hs
CROSS JOIN MonHoc mh
CROSS JOIN (VALUES (1), (2)) AS hk(HocKy)
WHERE hs.NienKhoa IS NOT NULL
  AND NOT EXISTS (
      -- Chỉ chèn nếu dữ liệu điểm này chưa tồn tại
      SELECT 1 FROM Diem d 
      WHERE d.MaHS = hs.maHS 
        AND d.MaMH = mh.MaMH 
        AND d.HocKy = hk.HocKy 
        AND d.NamHoc = hs.NienKhoa
  );
GO

-- ==========================================
-- 2. TẠO TRIGGER TỰ ĐỘNG CHO HỌC SINH MỚI
-- ==========================================
-- Trigger này sẽ tự động chạy mỗi khi có học sinh mới được Insert vào bảng HocSinh
CREATE OR ALTER TRIGGER trg_HocSinh_AfterInsert
ON HocSinh
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    
    INSERT INTO Diem (MaHS, MaMH, HocKy, NamHoc, Diem15p, Diem1Tiet, DiemGiuaKy, DiemCuoiKy, DiemTongKet)
    SELECT 
        i.maHS, 
        mh.MaMH, 
        hk.HocKy, 
        i.NienKhoa,
        NULL, NULL, NULL, NULL, NULL
    FROM inserted i
    CROSS JOIN MonHoc mh
    CROSS JOIN (VALUES (1), (2)) AS hk(HocKy)
    WHERE i.NienKhoa IS NOT NULL
      AND NOT EXISTS (
          SELECT 1 FROM Diem d 
          WHERE d.MaHS = i.maHS 
            AND d.MaMH = mh.MaMH 
            AND d.HocKy = hk.HocKy 
            AND d.NamHoc = i.NienKhoa
      );
END;
GO
