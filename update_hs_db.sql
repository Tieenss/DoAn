USE QuanLyHocSinh;
GO

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[HocSinh]') AND name = 'NienKhoa')
BEGIN
    ALTER TABLE HocSinh ADD NienKhoa NVARCHAR(20);
END
GO

UPDATE HocSinh SET NienKhoa = '2021-2024' WHERE NienKhoa IS NULL;
GO
