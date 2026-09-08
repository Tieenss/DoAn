package TienIch;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JLabel;
import java.awt.*;

public class CustomTableHeaderRenderer extends DefaultTableCellRenderer {
    
    public CustomTableHeaderRenderer() {
        setBackground(new Color(100, 150, 200));  
        setForeground(Color.WHITE);               
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setOpaque(true);
        setHorizontalAlignment(JLabel.CENTER);
    }
    
    @Override
    public Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setBackground(new Color(100, 150, 200));
        setForeground(Color.WHITE);
        setOpaque(true);
        
        return this;
    }
}
