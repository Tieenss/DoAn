package TienIch;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoCodeGenerator {

    private static final Pattern DIACRITICS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");

    /**
     * Bỏ dấu tiếng Việt, chuyển chữ thường, thay thế đ/Đ.
     */
    public static String removeAccents(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFD);
        String noAccents = DIACRITICS_PATTERN.matcher(normalized).replaceAll("");
        return noAccents.replace('đ', 'd').replace('Đ', 'D');
    }

    /**
     * Tự động sinh Mã Môn học từ Tên Môn học:
     * - Toán học -> toan, Toán học 11 -> toan11, Toán 10 -> toan10
     * - Ngữ văn -> van, Tiếng Anh -> anh, Vật lý -> ly, Hoá học -> hoa...
     */
    public static String generateMaMH(String tenMH) {
        if (tenMH == null || tenMH.trim().isEmpty()) {
            return "";
        }

        String raw = removeAccents(tenMH).toLowerCase().replaceAll("[^a-z0-9\\s]", " ").trim();
        if (raw.isEmpty()) return "";

        // Trích xuất số cuối hoặc số trong tên (nếu có: ví dụ 10, 11, 12, 1...)
        String numberSuffix = "";
        Matcher numMatcher = Pattern.compile("(\\d+)\\s*$").matcher(raw);
        if (numMatcher.find()) {
            numberSuffix = numMatcher.group(1);
        } else {
            // Tìm số bất kỳ nếu không ở cuối
            Matcher anyNum = DIGIT_PATTERN.matcher(raw);
            if (anyNum.find()) {
                numberSuffix = anyNum.group();
            }
        }

        // Bỏ hết số để so khớp tên môn
        String textOnly = raw.replaceAll("\\d+", "").replaceAll("\\s+", " ").trim();

        String code;
        if (textOnly.equals("toan") || textOnly.startsWith("toan hoc") || textOnly.startsWith("toan")) {
            code = "toan";
        } else if (textOnly.equals("van") || textOnly.contains("ngu van") || textOnly.startsWith("van")) {
            code = "van";
        } else if (textOnly.contains("tieng anh") || textOnly.contains("ngoai ngu") || textOnly.equals("anh")) {
            code = "anh";
        } else if (textOnly.contains("vat ly") || textOnly.contains("vat li") || textOnly.equals("ly") || textOnly.equals("li")) {
            code = "ly";
        } else if (textOnly.contains("hoa hoc") || textOnly.contains("hoa")) {
            code = "hoa";
        } else if (textOnly.contains("sinh hoc") || textOnly.contains("sinh")) {
            code = "sinh";
        } else if (textOnly.contains("lich su") || textOnly.equals("su")) {
            code = "su";
        } else if (textOnly.contains("dia ly") || textOnly.contains("dia li") || textOnly.equals("dia")) {
            code = "dia";
        } else if (textOnly.contains("giao duc cong dan") || textOnly.contains("gdcd") || textOnly.contains("cong dan")) {
            code = "gdcd";
        } else if (textOnly.contains("tin hoc") || textOnly.contains("tin") || textOnly.contains("cong nghe thong tin")) {
            code = "tin";
        } else if (textOnly.contains("cong nghe")) {
            code = "cn";
        } else if (textOnly.contains("giao duc quoc phong") || textOnly.contains("gdqp") || textOnly.contains("quoc phong")) {
            code = "gdqp";
        } else if (textOnly.contains("the duc") || textOnly.contains("the chat")) {
            code = "theduc";
        } else if (textOnly.contains("am nhac") || textOnly.contains("nhac")) {
            code = "amnhac";
        } else if (textOnly.contains("my thuat") || textOnly.contains("mi thuat")) {
            code = "mythuat";
        } else if (textOnly.contains("ky nang song") || textOnly.contains("kns")) {
            code = "kns";
        } else if (textOnly.contains("tieng phap")) {
            code = "phap";
        } else if (textOnly.contains("tieng trung")) {
            code = "trung";
        } else if (textOnly.contains("tieng nhat")) {
            code = "nhat";
        } else if (textOnly.contains("tieng han")) {
            code = "han";
        } else {
            // Tên môn khác: viết tắt các chữ cái đầu hoặc dùng từ đầu
            String[] words = textOnly.split("\\s+");
            if (words.length == 1) {
                code = words[0];
            } else {
                StringBuilder sb = new StringBuilder();
                for (String w : words) {
                    if (!w.isEmpty()) sb.append(w.charAt(0));
                }
                code = sb.toString();
            }
        }

        return code + numberSuffix;
    }

    /**
     * Tự động sinh Mã Phòng học từ Tên Phòng học và Loại Phòng:
     * - Lý thuyết: P + số phòng (ví dụ: Phòng 201 -> P201)
     * - Thực hành:
     *   + Phòng Thí nghiệm: Tiền tố TN- (ví dụ: Phòng Thí nghiệm Hóa Sinh -> TN-HS, Phòng Thí nghiệm Vật lý -> TN-LY)
     *   + Phòng Máy tính / Tin học / Thực hành: Tiền tố LAB + số phòng (ví dụ: Phòng Máy tính 1 -> LAB1, Phòng máy tính 2 -> LAB2)
     */
    public static String generateMaPhong(String tenPhong, String loaiPhong, Collection<String> existingMaPhongs) {
        if (tenPhong == null || tenPhong.trim().isEmpty()) {
            return "";
        }

        String raw = removeAccents(tenPhong).toLowerCase().trim();
        boolean isThucHanh = loaiPhong != null && removeAccents(loaiPhong).toLowerCase().contains("thuc hanh");
        String digits = extractAllDigits(tenPhong);

        if (!isThucHanh) {
            // Phòng Lý thuyết: P + số phòng
            if (!digits.isEmpty()) {
                return "P" + digits;
            } else {
                // Không có số trong tên phòng lý thuyết -> tìm P01, P02...
                return findNextAvailableCode("P", existingMaPhongs, 1);
            }
        } else {
            // Kiểm tra xem có phải là Phòng Thí Nghiệm hay không
            boolean isThiNghiem = raw.contains("thi nghiem") || raw.contains("tn ") || raw.startsWith("tn");
            if (isThiNghiem) {
                String subCode = "";
                if (raw.contains("hoa sinh") || raw.contains("hoasinh")) {
                    subCode = "HS";
                } else if (raw.contains("vat ly") || raw.contains("vat li") || raw.contains("ly") || raw.contains("li")) {
                    subCode = "LY";
                } else if (raw.contains("hoa hoc") || raw.contains("hoa")) {
                    subCode = "HOA";
                } else if (raw.contains("sinh hoc") || raw.contains("sinh")) {
                    subCode = "SINH";
                } else if (raw.contains("tin hoc") || raw.contains("may tinh")) {
                    subCode = "TH";
                } else if (raw.contains("tieng anh") || raw.contains("ngoai ngu")) {
                    subCode = "NN";
                }

                if (!subCode.isEmpty()) {
                    return "TN-" + subCode + digits;
                } else if (!digits.isEmpty()) {
                    return "TN" + digits;
                } else {
                    return findNextAvailableCode("TN", existingMaPhongs, 1);
                }
            } else {
                // Phòng Thực hành / Máy tính: Tiền tố LAB + số phòng
                if (!digits.isEmpty()) {
                    return "LAB" + digits;
                } else {
                    return findNextAvailableCode("LAB", existingMaPhongs, 1);
                }
            }
        }
    }

    private static String extractAllDigits(String str) {
        if (str == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String findNextAvailableCode(String prefix, Collection<String> existingCodes, int minIndex) {
        Set<Integer> usedNumbers = new HashSet<>();
        if (existingCodes != null) {
            Pattern p = Pattern.compile("^" + Pattern.quote(prefix) + "(\\d+)$", Pattern.CASE_INSENSITIVE);
            for (String code : existingCodes) {
                if (code == null) continue;
                Matcher m = p.matcher(code.trim());
                if (m.matches()) {
                    try {
                        usedNumbers.add(Integer.parseInt(m.group(1)));
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        int nextNum = minIndex;
        while (usedNumbers.contains(nextNum)) {
            nextNum++;
        }
        return prefix + nextNum;
    }
}
