package Controller.Dat;

import Api.Đat.ToHopMonApi;
import Model.ToBoMon;
import View.Dat.QuanLyToBoMonPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;

public class ToBoMonController {

    private QuanLyToBoMonPanel view;
    private ToHopMonApi dao;
    private String currentMode = ""; 

    public ToBoMonController(QuanLyToBoMonPanel view) {
        this.view = view;
        this.dao = new ToHopMonApi();

        initEvents();

        loadData();
    }

    private void loadData() {
        List<ToBoMon> list = dao.getAll();
        view.setTableData(list);
    }

    private void initEvents() {
        view.getTableTBM().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = view.getTableTBM().getSelectedRow();
                if (r >= 0) {
                    view.getTxtMaToHop().setText(view.getTableTBM().getValueAt(r, 0).toString());
                    view.getTxtTenToHop().setText(view.getTableTBM().getValueAt(r, 1).toString());
                }
            }
        });
        view.getBtnTim().addActionListener(e -> {
            String keyword = view.getTxtTimKiem().getText().trim();

            if (keyword.isEmpty()) {
                loadData();
                return;
            }

            List<ToBoMon> resultList = dao.search(keyword);

            if (resultList != null && !resultList.isEmpty()) {
                view.setTableData(resultList);
            } else {
                JOptionPane.showMessageDialog(view, "Không tìm thấy tổ bộ môn nào khớp với từ khóa!");
                view.setTableData(resultList); 
            }
        });

        view.getBtnThem().addActionListener(e -> {
            view.clearForm();
            currentMode = "ADD";
            view.setButtonState(false); 
            view.getTxtMaToHop().setEnabled(true);
            view.getTxtMaToHop().requestFocus();
        });

        view.getBtnSua().addActionListener(e -> {
            if (view.getTableTBM().getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần sửa!");
                return;
            }
            currentMode = "EDIT";
            view.setButtonState(false);
            view.getTxtMaToHop().setEnabled(false); 
            view.getTxtTenToHop().requestFocus();
        });

        view.getBtnXoa().addActionListener(e -> {
            int r = view.getTableTBM().getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần xóa!");
                return;
            }
            String ma = view.getTableTBM().getValueAt(r, 0).toString();
            int opt = JOptionPane.showConfirmDialog(view,
                    "Bạn có chắc chắn muốn xóa tổ hợp này?\nCác giáo viên thuộc tổ này sẽ bị mất thông tin tổ bộ môn.",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (opt == JOptionPane.YES_OPTION) {
                if (dao.delete(ma)) {
                    JOptionPane.showMessageDialog(view, "Xóa thành công!");
                    loadData();
                    view.clearForm();
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa thất bại!");
                }
            }
        });

        view.getBtnLuu().addActionListener(e -> {
            String ma = view.getTxtMaToHop().getText().trim();
            String ten = view.getTxtTenToHop().getText().trim();

            if (ma.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            ToBoMon tbm = new ToBoMon(ma, ten, ""); 
            boolean kq = false;

            if ("ADD".equals(currentMode)) {
                // Kiểm tra trùng mã trên giao diện
                boolean isDuplicate = false;
                for (int i = 0; i < view.getTableTBM().getRowCount(); i++) {
                    if (view.getTableTBM().getValueAt(i, 0).toString().equalsIgnoreCase(ma)) {
                        isDuplicate = true;
                        break;
                    }
                }
                
                if (isDuplicate) {
                    JOptionPane.showMessageDialog(view, "Trùng mã!");
                    return;
                }
                
                kq = dao.insert(tbm);
            } else if ("EDIT".equals(currentMode)) {
                kq = dao.update(tbm);
            }

            if (kq) {
                JOptionPane.showMessageDialog(view, "Lưu thành công!");
                loadData();
                view.setButtonState(true); 
                view.clearForm();
                currentMode = "";
            } else {
                if ("ADD".equals(currentMode)) {
                    JOptionPane.showMessageDialog(view, "Trùng mã!");
                } else {
                    JOptionPane.showMessageDialog(view, "Lưu thất bại!");
                }
            }
        });

 
        view.getBtnHuy().addActionListener(e -> {
            view.setButtonState(true);
            view.clearForm();
            currentMode = "";
            view.getTableTBM().clearSelection();
            loadData(); 
        });
    }
}