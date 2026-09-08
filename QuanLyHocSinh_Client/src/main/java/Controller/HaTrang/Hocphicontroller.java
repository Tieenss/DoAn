package Controller.HaTrang;

import Api.HaTrang.HocPhiApiClient;
import Model.Auth;
import Model.Hocphi;
import View.HaTrang.QuanLyHocPhiPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;

public class Hocphicontroller {
    private final QuanLyHocPhiPanel view;
    private final HocPhiApiClient dao;
    private int selectedMaHP = 0;

    public Hocphicontroller(QuanLyHocPhiPanel view) {
        this.view = view;
        this.dao = new HocPhiApiClient();
        initEvents();
        loadTatCaDuLieu();
    }

    private void initEvents() {
        boolean[] updateMode = {false};

        Runnable setFormState = () -> {
            if (!Auth.isHocSinh()) {
                boolean hasSelected = view.getTableHocPhi().getSelectedRow() != -1;
                view.setCrudButtonState(!hasSelected && !updateMode[0], hasSelected && !updateMode[0], hasSelected && !updateMode[0], updateMode[0], updateMode[0]);
                view.setInputEditable(updateMode[0]);
                if (updateMode[0] && view.getTableHocPhi().getSelectedRow() != -1) {
                    view.getTxtMaHS().setEditable(false); 
                }
            }
        };

        setFormState.run();

        if (view.getBtnLoc() != null) view.getBtnLoc().addActionListener(e -> locDuLieu());

        if (view.getBtnThem() != null) {
            view.getBtnThem().addActionListener(e -> {
                updateMode[0] = false;
                selectedMaHP = 0;
                view.refreshForm();
                updateMode[0] = true; 
                setFormState.run();
                updateMode[0] = false; 
            });
        }

        if (view.getBtnSua() != null) {
            view.getBtnSua().addActionListener(e -> {
                if (view.getTableHocPhi().getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(view, "Vui lòng chọn một dòng cần sửa!");
                    return;
                }
                updateMode[0] = true;
                setFormState.run();
            });
        }

        if (view.getBtnLuu() != null) {
            view.getBtnLuu().addActionListener(e -> {
                if (xuLyLuu(updateMode[0])) {
                    updateMode[0] = false;
                    view.getTableHocPhi().clearSelection();
                    setFormState.run();
                }
            });
        }

        if (view.getBtnXoa() != null) {
            view.getBtnXoa().addActionListener(e -> {
                if (xoaHocPhi()) {
                    updateMode[0] = false;
                    view.getTableHocPhi().clearSelection();
                    setFormState.run();
                }
            });
        }

        if (view.getBtnHuy() != null) {
            view.getBtnHuy().addActionListener(e -> {
                view.refreshForm();
                updateMode[0] = false;
                view.getTableHocPhi().clearSelection();
                setFormState.run();
            });
        }

        view.getTableHocPhi().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = view.getTableHocPhi().getSelectedRow();
                if (selectedRow >= 0) {
                    selectedMaHP = Integer.parseInt(view.getTableHocPhi().getValueAt(selectedRow, 0).toString());
                    setFormState.run();
                }
            }
        });

        if (view.getTxtMaHS() != null) {
            view.getTxtMaHS().addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    String maHS = view.getTxtMaHS().getText().trim();
                    if (!maHS.isEmpty() && !updateMode[0] && view.getTxtMaLopCT() != null) {
                        view.getTxtMaLopCT().setText(maHS.toUpperCase().startsWith("HS") ? "10A1" : "CHƯA XÁC ĐỊNH");
                    }
                }
            });
        }

        if (view.getTxtTongTien() != null && view.getTxtMienGiam() != null) {
            KeyAdapter tinhTienTuDong = new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        long tongTien = view.getTxtTongTien().getText().trim().isEmpty() ? 0 : Long.parseLong(view.getTxtTongTien().getText().trim());
                        long mienGiam = view.getTxtMienGiam().getText().trim().isEmpty() ? 0 : Long.parseLong(view.getTxtMienGiam().getText().trim());
                        if (view.getTxtPhaiDong() != null) view.getTxtPhaiDong().setText(String.valueOf(tongTien - mienGiam));
                    } catch (NumberFormatException ex) {  }
                }
            };
            view.getTxtTongTien().addKeyListener(tinhTienTuDong);
            view.getTxtMienGiam().addKeyListener(tinhTienTuDong);
        }
    }

    private void loadTatCaDuLieu() {
        try {
            view.loadTable(Auth.isHocSinh() ? dao.getByMaHS(Auth.maNguoiDung) : dao.getAllHocPhi());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void locDuLieu() {
        try {
            Object cboHocKySelected = view.getCboHocKy().getSelectedItem();
            String maLop = view.getTxtMaLop().getText().trim().toUpperCase();
            String hocKyStr = (cboHocKySelected != null) ? cboHocKySelected.toString().trim() : "";
            String namHoc = view.getTxtNamHoc().getText().trim();

            if (maLop.isEmpty() && hocKyStr.isEmpty() && namHoc.isEmpty()) {
                loadTatCaDuLieu();
                return;
            }

            List<Hocphi> list = dao.getHocPhiByLop(maLop, hocKyStr.isEmpty() ? 0 : Integer.parseInt(hocKyStr), namHoc);

            if (Auth.isHocSinh()) {
                list = list.stream().filter(hp -> hp.getMaHS() != null && hp.getMaHS().equalsIgnoreCase(Auth.maNguoiDung)).toList();
            }

            view.loadTable(list);
            if (list.isEmpty()) JOptionPane.showMessageDialog(view, "Không tìm thấy dữ liệu học phí phù hợp!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi định dạng số khi xử lý Học Kỳ!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean xuLyLuu(boolean isUpdate) {
        try {
            String maHS = view.getTxtMaHS().getText().trim();
            String maLop = view.getTxtMaLopCT().getText().trim();
            String tongTienStr = view.getTxtTongTien().getText().trim();
            String mienGiamStr = view.getTxtMienGiam().getText().trim();
            Object hocKyObj = view.getCboHocKyCT().getSelectedItem();
            Object trangThaiObj = view.getCboTrangThai().getSelectedItem();

            String hocKyStr = hocKyObj == null ? "" : hocKyObj.toString().trim();
            String namHoc = view.getTxtNamHocCT().getText().trim();
            String trangThaiStr = trangThaiObj == null ? "" : trangThaiObj.toString().trim();

            if (maHS.isEmpty() || tongTienStr.isEmpty() || hocKyStr.isEmpty() || namHoc.isEmpty() || maLop.equalsIgnoreCase("CHƯA XÁC ĐỊNH")) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin hợp lệ!");
                return false;
            }

            long tongTien = Long.parseLong(tongTienStr);
            long mienGiam = mienGiamStr.isEmpty() ? 0 : Long.parseLong(mienGiamStr);

            if (mienGiam > tongTien) {
                JOptionPane.showMessageDialog(view, "Số tiền miễn giảm không được lớn hơn tổng tiền học phí!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Hocphi hp = new Hocphi();
            if (isUpdate) hp.setMaHP(selectedMaHP);
            hp.setMaHS(maHS);
            hp.setMaLop(maLop);
            hp.setHocKy(Integer.parseInt(hocKyStr));
            hp.setNamHoc(namHoc);
            hp.setTongTien(tongTien);
            hp.setMienGiam(mienGiam);
            hp.setPhaiDong(tongTien - mienGiam);
            hp.setTrangThai(trangThaiStr.isEmpty() ? "Chưa đóng" : trangThaiStr);

            if (dao.saveHocPhi(hp)) {
                JOptionPane.showMessageDialog(view, isUpdate ? "Cập nhật học phí thành công!" : "Thêm mới học phí thành công!");
                loadTatCaDuLieu();
                view.refreshForm();
                return true;
            }
            JOptionPane.showMessageDialog(view, "Lưu thất bại! Kiểm tra kết nối API/DB.");
            return false;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Số tiền học phí hoặc miễn giảm phải nhập định dạng số!");
            return false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage());
            return false;
        }
    }

    private boolean xoaHocPhi() {
        int selectedRow = view.getTableHocPhi().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một dòng trên bảng để xóa!");
            return false;
        }

        if (JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa học phí này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.deleteHocPhi((int) view.getTableHocPhi().getValueAt(selectedRow, 0))) {
                JOptionPane.showMessageDialog(view, "Xóa thành công!");
                loadTatCaDuLieu();
                view.refreshForm();
                return true;
            }
            JOptionPane.showMessageDialog(view, "Xóa thất bại!");
        }
        return false;
    }
}