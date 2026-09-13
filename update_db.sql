USE QuanLyHocSinh;
GO

-- 1. Bảng Diem
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Diem]') AND name = 'NamHoc')
BEGIN
    ALTER TABLE Diem ADD NamHoc NVARCHAR(20);
END
GO

UPDATE Diem SET NamHoc = '2023-2024' WHERE NamHoc IS NULL;
GO

ALTER TABLE Diem ALTER COLUMN NamHoc NVARCHAR(20) NOT NULL;
GO

-- Xóa Khóa chính cũ của bảng Diem
DECLARE @ConstraintName nvarchar(200);
SELECT @ConstraintName = name FROM sys.key_constraints 
WHERE type = 'PK' AND parent_object_id = OBJECT_ID('Diem');
IF @ConstraintName IS NOT NULL
BEGIN
    DECLARE @SQL nvarchar(1000) = 'ALTER TABLE Diem DROP CONSTRAINT ' + @ConstraintName;
    EXEC(@SQL);
END
GO

-- Tạo khóa chính mới nếu chưa có
IF NOT EXISTS (SELECT * FROM sys.key_constraints WHERE type = 'PK' AND parent_object_id = OBJECT_ID('Diem'))
BEGIN
    ALTER TABLE Diem ADD CONSTRAINT PK_Diem PRIMARY KEY (MaHS, MaMH, HocKy, NamHoc);
END
GO

-- 2. Bảng LichThi
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[LichThi]') AND name = 'NamHoc')
BEGIN
    ALTER TABLE LichThi ADD NamHoc NVARCHAR(20);
    ALTER TABLE LichThi ADD HocKy INT;
END
GO

UPDATE LichThi SET NamHoc = '2023-2024', HocKy = 1 WHERE NamHoc IS NULL;
GO

-- 3. Bảng ThoiKhoaBieu
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[ThoiKhoaBieu]') AND name = 'NamHoc')
BEGIN
    ALTER TABLE ThoiKhoaBieu ADD NamHoc NVARCHAR(20);
    ALTER TABLE ThoiKhoaBieu ADD HocKy INT;
END
GO

UPDATE ThoiKhoaBieu SET NamHoc = '2023-2024', HocKy = 1 WHERE NamHoc IS NULL;
GO
