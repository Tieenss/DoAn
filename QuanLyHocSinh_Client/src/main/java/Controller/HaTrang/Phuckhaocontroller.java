package Controller.HaTrang;

import Api.HaTrang.PhucKhaoApiClient;
import Model.Phuckhao;
import View.HaTrang.QuanLyPhucKhaoPanel;
import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;

public class Phuckhaocontroller {
    private QuanLyPhucKhaoPanel view;
    private PhucKhaoApiClient dao;
    private List<Phuckhao> listCurrent;

    public Phuckhaocontroller(QuanLyPhucKhaoPanel view) {
        this.view = view;
        this.dao = new PhucKhaoApiClient();
        initEvents();
        loadData();
    }

    private void initEvents() {
        boolean[] editMode = {false};

        Runnable setIdleState = () -> {
            if (Model.Auth.isHocSinh()) {
                view.setCrudButtonState(true, false, false, false, false);
            } else {
                view.setCrudButtonState(false, false, false, false, false);
            }
            view.setInputEditable(false);
        };

        Runnable setAddState = () -> {
            view.setCrudButtonState(false, false, false, true, true);
            view.setInputEditable(true);
        };

        Runnable setSelectedState = () -> {
            if (Model.Auth.isHocSinh()) {
                view.setCrudButtonState(false, true, true, false, true);
            } else {
                view.setCrudButtonState(false, true, false, false, true);
            }
            view.setInputEditable(false);
        };

        Runnable setEditState = () -> {
            view.setCrudButtonState(false, false, false, true, true);
            view.setInputEditable(true);
        };

        setIdleState.run();

        view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    editMode[0] = true;
                    view.fillForm(row);
                    setSelectedState.run();
                }
            }
        });

        view.getBtnThem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMode[0] = false;
                view.refresh();
                view.getTable().clearSelection();
                setAddState.run();
            }
        });

        view.getBtnSua().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row != -1) {
                    editMode[0] = true;
                    view.fillForm(row);
                    setEditState.run();
                } else {
                    JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần sửa trên danh sách trước!");
                }
            }
        });

        view.getBtnLuu().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editMode[0]) {
                    int row = view.getTable().getSelectedRow();
                    if (row != -1 && validateForm()) {
                        Phuckhao pk = getForm();
                        int modelRow = view.getTable().convertRowIndexToModel(row);
                        pk.setMaPK(listCurrent.get(modelRow).getMaPK());
                        if (dao.update(pk)) {
                            JOptionPane.showMessageDialog(view, "Cập nhật yêu cầu phúc khảo thành công!");
                            loadData();
                            view.refresh();
                            editMode[0] = false;
                            setIdleState.run();
                        } else {
                            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(view, "Chọn dòng cần sửa!");
                    }
                } else {
                    if (validateForm()) {
                        Phuckhao pk = getForm();
                        if (dao.insert(pk)) {
                            JOptionPane.showMessageDialog(view, "Gửi yêu cầu phúc khảo thành công!");
                            loadData();
                            view.refresh();
                            editMode[0] = false;
                            setIdleState.run();
                        } else {
                            JOptionPane.showMessageDialog(view, "Thêm thất bại! Vui lòng kiểm tra lại Mã Môn Học.");
                        }
                    }
                }
            }
        });

        view.getBtnXoa().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row != -1) {
                    int modelRow = view.getTable().convertRowIndexToModel(row);
                    int id = listCurrent.get(modelRow).getMaPK();
                    int confirm = JOptionPane.showConfirmDialog(
                            view, "Bạn có chắc chắn muốn xóa yêu cầu này?", "Xác nhận xóa",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (dao.delete(id)) {
                            JOptionPane.showMessageDialog(view, "Xóa đơn thành công!");
                            loadData();
                            view.refresh();
                            editMode[0] = false;
                            setIdleState.run();
                        } else {
                            JOptionPane.showMessageDialog(view, "Xóa đơn thất bại!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần xóa!");
                }
            }
        });

        view.getBtnLoc().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tuKhoa = view.getLocKeyword().trim();

                if (tuKhoa.isEmpty()) {
                    loadData();
                    return;
                }
                
                List<Phuckhao> searchResult = dao.search(tuKhoa);
                
                if (Model.Auth.isHocSinh()) {
                    searchResult = searchResult.stream().filter(pk -> pk.getMaHS().equals(Model.Auth.maNguoiDung)).collect(java.util.stream.Collectors.toList());
                }

                listCurrent = searchResult;
                view.loadTable(listCurrent);

                if (listCurrent == null || listCurrent.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Không tìm thấy yêu cầu phúc khảo nào với từ khóa: " + tuKhoa, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        view.getBtnHuy().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.refresh();
                loadData();
                editMode[0] = false;
                setIdleState.run();
            }
        });
    }

    private void loadData() {
        try {
            List<Phuckhao> list = dao.getAll();
            if (Model.Auth.isHocSinh()) {
                list = list.stream().filter(pk -> pk.getMaHS().equals(Model.Auth.maNguoiDung)).collect(java.util.stream.Collectors.toList());
            }
            listCurrent = list;
            view.loadTable(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateForm() {
        if (view.getMaHS().trim().isEmpty() || view.getMaMH().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Mã học sinh và Mã môn học không được để trống!");
            return false;
        }
        return true;
    }

    private Phuckhao getForm() {
        Phuckhao pk = new Phuckhao();
        pk.setMaHS(view.getMaHS().trim().toUpperCase());
        pk.setMaMH(view.getMaMH().trim().toUpperCase());
        pk.setTrangThai(view.getTrangThai().trim());
        pk.setLyDo(view.getLyDo().trim());
        pk.setNgayGui(new java.util.Date());
        return pk;
    }
}