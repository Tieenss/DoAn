package TienIch;

import javax.swing.*;
import java.awt.*;

public class ButtonStyleHelper {

    private static final Color COLOR_ADD = new Color(33, 150, 243); 
    private static final Color COLOR_EDIT = new Color(255, 152, 0); 
    private static final Color COLOR_DELETE = new Color(244, 67, 54); 
    private static final Color COLOR_SAVE = new Color(76, 175, 80); 
    private static final Color COLOR_CANCEL = new Color(117, 117, 117); 
    private static final Color COLOR_VIEW = new Color(0, 150, 136); 
    private static final Color COLOR_SEARCH = new Color(63, 81, 181); 
    private static final Color COLOR_EXPORT = new Color(76, 175, 80); 
    private static final Color TEXT_COLOR = Color.BLACK;
    
    public static void styleButtonAdd(JButton btn) {
        btn.setBackground(COLOR_ADD);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public static void styleButtonEdit(JButton btn) {
        btn.setBackground(COLOR_EDIT);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public static void styleButtonDelete(JButton btn) {
        btn.setBackground(COLOR_DELETE);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public static void styleButtonSave(JButton btn) {
        btn.setBackground(COLOR_SAVE);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public static void styleButtonCancel(JButton btn) {
        btn.setBackground(COLOR_CANCEL);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public static void styleButtonView(JButton btn) {
        btn.setBackground(COLOR_VIEW);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public static void styleButtonSearch(JButton btn) {
        btn.setBackground(COLOR_SEARCH);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public static void styleButtonExport(JButton btn) {
        btn.setBackground(COLOR_EXPORT);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public static void styleButtonFilter(JButton btn) {
        btn.setBackground(COLOR_VIEW);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public static void styleButtonRefresh(JButton btn) {
        btn.setBackground(COLOR_VIEW);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
}
