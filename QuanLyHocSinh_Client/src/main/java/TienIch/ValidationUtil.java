package TienIch;

import java.util.regex.Pattern;

public class ValidationUtil {

    // Chỉ cho phép chữ cái (không dấu) và số: ví dụ MH01, LAB1, P101
    private static final Pattern MA_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    // Cho phép chữ cái tiếng Việt (có dấu \p{L}), chữ số và khoảng trắng: ví dụ Toán học, Phòng Lab 1
    private static final Pattern TEN_PATTERN = Pattern.compile("^[a-zA-Z0-9\\p{L} ]+$");

    // Năm học phải gồm đúng 4 chữ số: ví dụ 2025
    private static final Pattern NAM_PATTERN = Pattern.compile("^\\d{4}$");

    public static final String MSG_MIN_LENGTH = "Vui lòng nhập từ 2 ký tự trở lên và không được để trống!";
    public static final String MSG_SPECIAL_CHAR = "Không được chứa ký tự đặc biệt! Vui lòng nhập lại thông tin.";

    public static String clean(String str) {
        return str == null ? "" : str.trim();
    }


    public static String validateMa(String ma) {
        return validateMa(ma, "");
    }

    public static String validateMa(String ma, String fieldLabel) {
        String s = clean(ma);
        String prefix = (fieldLabel == null || fieldLabel.trim().isEmpty()) ? "" : fieldLabel.trim() + ": ";
        if (s.length() < 2) {
            return prefix + MSG_MIN_LENGTH;
        }
        if (!MA_PATTERN.matcher(s).matches()) {
            return prefix + MSG_SPECIAL_CHAR;
        }
        return null;
    }

    public static String validateTen(String ten) {
        return validateTen(ten, "");
    }

    public static String validateTen(String ten, String fieldLabel) {
        String s = clean(ten);
        String prefix = (fieldLabel == null || fieldLabel.trim().isEmpty()) ? "" : fieldLabel.trim() + ": ";
        if (s.length() < 2) {
            return prefix + MSG_MIN_LENGTH;
        }
        if (!TEN_PATTERN.matcher(s).matches()) {
            return prefix + MSG_SPECIAL_CHAR;
        }
        return null;
    }

    public static String validateNam(String nam) {
        String s = clean(nam);
        if (s.isEmpty()) {
            return "Năm học không được để trống!";
        }
        if (!NAM_PATTERN.matcher(s).matches()) {
            return "Năm học phải là số 4 chữ số hợp lệ (ví dụ: 2025) và không chứa ký tự đặc biệt!";
        }
        return null;
    }

    public static String validateSucChua(String sucChuaStr) {
        String s = clean(sucChuaStr);
        if (s.isEmpty()) {
            return "Sức chứa phòng học không được để trống!";
        }
        try {
            int sc = Integer.parseInt(s);
            if (sc <= 0) {
                return "Sức chứa phòng học phải là số nguyên dương lớn hơn 0!";
            }
        } catch (NumberFormatException e) {
            return "Sức chứa phòng học phải là số nguyên hợp lệ!";
        }
        return null;
    }
}
