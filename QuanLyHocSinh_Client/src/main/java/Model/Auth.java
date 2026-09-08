package Model;

public class Auth {

    public static String currentUser = "";
    public static String currentRole = "";
    public static String maNguoiDung = "";

    public static void clear() {
        currentUser = "";
        currentRole = "";
        maNguoiDung = "";
    }

    public static boolean isLogin() {
        return currentUser != null && !currentUser.isEmpty();
    }

    private static String role() {
        return currentRole == null ? "" : currentRole.trim().toLowerCase();
    }

    public static boolean isAdmin() {
        return role().equals("admin");
    }

    public static boolean isGiaoVien() {
        return role().equals("giaovien")
                || role().equals("giao vien")
                || role().equals("giáo viên");
    }

    public static boolean isHocSinh() {
        return role().equals("hocsinh")
                || role().equals("hoc sinh")
                || role().equals("học sinh");
    }

    public static boolean canViewHocSinh(String maHSTarget) {
        
        if (isAdmin()) {
            return true;
        }
        
        if (isHocSinh()) {
            return maNguoiDung.equals(maHSTarget);
        }
        
        return false;
    }

    public static boolean canViewGiaoVien(String maGVTarget) {
        
        if (isAdmin()) {
            return true;
        }
        
        if (isGiaoVien()) {
            return maNguoiDung.equals(maGVTarget);
        }
        
        return false;
    }

    public static boolean canEditData(String maNguoiDungData) {
        
        if (isAdmin()) {
            return true;
        }
        
        return maNguoiDung.equals(maNguoiDungData);
    }
}