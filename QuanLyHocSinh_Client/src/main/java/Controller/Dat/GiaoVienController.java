package Controller.Dat;

import Api.Đat.GiaoVienApi;
import Api.Đat.ToHopMonApi;

import Model.Giaovien;
import Model.ToBoMon;
import View.Dat.QuanLyGiaoVienPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GiaoVienController {

    private final QuanLyGiaoVienPanel view;
    private final GiaoVienApi api;
    private final ToHopMonApi toHopDAO;

    private String mode = "";

    public GiaoVienController(QuanLyGiaoVienPanel view) {

        this.view = view;
        this.api = new GiaoVienApi();
        this.toHopDAO = new ToHopMonApi();

        init();
    }

    private void init() {

        loadComboBox();
        loadTable();

        setButtonState(true);

        view.getTableGV().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillForm();
            }
        });

        view.getBtnThem().addActionListener(e -> them());

        view.getBtnSua().addActionListener(e -> sua());

        view.getBtnXoa().addActionListener(e -> xoa());

        view.getBtnLuu().addActionListener(e -> luu());

        view.getBtnHuy().addActionListener(e -> huy());

        view.getBtnXem().addActionListener(e -> loadTable());

        view.getBtnTimKiem().addActionListener(e -> search());
    }

    private void loadComboBox() {

        view.getCboMaToHop().removeAllItems();

        List<ToBoMon> list = toHopDAO.getAll();

        for (ToBoMon t : list) {
            view.getCboMaToHop().addItem(t);
        }
    }

    private void loadTable() {

        try {
            List<Giaovien> list = api.getAll();
            
            if (Model.Auth.isGiaoVien()) {
                list = list.stream().filter(gv -> gv.getMaGV().equals(Model.Auth.maNguoiDung)).collect(java.util.stream.Collectors.toList());
            }

            view.getTableModel().setRowCount(0);

            for (Giaovien gv : list) {

                view.getTableModel().addRow(new Object[]{
                        gv.getMaGV(),
                        gv.getHoTen(),
                        gv.getNgaySinh(),
                        gv.getSdt(),
                        gv.getMaToHop()
                });

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(view,
                    "Không tải được danh sách giáo viên");

        }

    }

    private void fillForm() {

        int row = view.getTableGV().getSelectedRow();

        if (row < 0) return;

        view.getTxtMaGV().setText(
                view.getTableGV().getValueAt(row,0).toString());

        view.getTxtHoTen().setText(
                view.getTableGV().getValueAt(row,1).toString());

        view.getTxtSDT().setText(
                view.getTableGV().getValueAt(row,3).toString());

        try {

            Date d = new SimpleDateFormat("yyyy-MM-dd")
                    .parse(view.getTableGV().getValueAt(row,2).toString());

            view.getSpNgaySinh().setValue(d);

        } catch (Exception ignored){}

        String maTH = view.getTableGV().getValueAt(row,4).toString();

        for(int i=0;i<view.getCboMaToHop().getItemCount();i++){

            if(view.getCboMaToHop().getItemAt(i)
                    .getMaToHop()
                    .equals(maTH)){

                view.getCboMaToHop().setSelectedIndex(i);

                break;
            }

        }

    }

    private void them(){

        clearForm();

        mode="ADD";

        setButtonState(false);

        view.getTxtMaGV().setEnabled(true);
        view.getTxtHoTen().setEnabled(true);
        view.getTxtSDT().setEnabled(true);
        view.getSpNgaySinh().setEnabled(true);
        view.getCboMaToHop().setEnabled(true);
        view.getBtnHuy().setEnabled(true);
        view.getBtnLuu().setEnabled(true);

    }

    private void sua(){

        if(view.getTableGV().getSelectedRow()==-1){

            JOptionPane.showMessageDialog(view,
                    "Chọn giáo viên cần sửa");

            return;

        }

        mode="EDIT";

        setButtonState(false);

        view.getTxtMaGV().setEnabled(false);

    }

    private void xoa(){

        if(view.getTableGV().getSelectedRow()==-1){

            JOptionPane.showMessageDialog(view,
                    "Chọn giáo viên cần xóa");

            return;

        }

        int c=JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if(c!=JOptionPane.YES_OPTION) return;

        try{

            api.delete(view.getTxtMaGV().getText());

            JOptionPane.showMessageDialog(view,
                    "Đã xóa");

            loadTable();

            clearForm();

        }catch(Exception e){

            JOptionPane.showMessageDialog(view,
                    "Xóa thất bại");

        }

    }

    private void luu(){

        if(view.getTxtMaGV().getText().isEmpty()
                ||view.getTxtHoTen().getText().isEmpty()){

            JOptionPane.showMessageDialog(view,
                    "Nhập đầy đủ thông tin");

            return;

        }

        Giaovien gv=new Giaovien();

        gv.setMaGV(view.getTxtMaGV().getText());

        gv.setHoTen(view.getTxtHoTen().getText());

        gv.setSdt(view.getTxtSDT().getText());

        gv.setNgaySinh(
                new SimpleDateFormat("yyyy-MM-dd")
                        .format(view.getSpNgaySinh().getValue())
        );

        ToBoMon tb=(ToBoMon)view.getCboMaToHop().getSelectedItem();

        if(tb!=null){

            gv.setMaToHop(tb.getMaToHop());

        }

        try{

            if(mode.equals("ADD")){

                api.insert(gv);

            }else{

                api.update(gv);

            }

            JOptionPane.showMessageDialog(view,
                    "Lưu thành công");

            loadTable();

            clearForm();

            setButtonState(true);

        }catch(Exception e){

            JOptionPane.showMessageDialog(view,
                    "Lưu thất bại");

        }

    }

    private void search(){

        try{

            String keyword=view.getTxtTimKiem().getText().trim();

            if(keyword.isEmpty()){

                loadTable();

                return;

            }

            List<Giaovien> list=api.searchGiaoVien(keyword);

            if (Model.Auth.isGiaoVien()) {
                list = list.stream().filter(gv -> gv.getMaGV().equals(Model.Auth.maNguoiDung)).collect(java.util.stream.Collectors.toList());
            }

            view.getTableModel().setRowCount(0);

            for(Giaovien gv:list){

                view.getTableModel().addRow(new Object[]{

                        gv.getMaGV(),
                        gv.getHoTen(),
                        gv.getNgaySinh(),
                        gv.getSdt(),
                        gv.getMaToHop()

                });

            }

            if(list.isEmpty()){

                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy dữ liệu");

            }

        }catch(Exception e){

            JOptionPane.showMessageDialog(view,
                    "Lỗi tìm kiếm");

        }

    }

    private void huy(){

        clearForm();

        setButtonState(true);

        view.getTableGV().clearSelection();
        view.getTxtMaGV().setEnabled(false);
        view.getTxtHoTen().setEnabled(false);
        view.getTxtSDT().setEnabled(false);
        view.getSpNgaySinh().setEnabled(false);
        view.getCboMaToHop().setEnabled(false);

    }

    private void clearForm(){

        view.getTxtMaGV().setText("");

        view.getTxtHoTen().setText("");

        view.getTxtSDT().setText("");

        view.getTxtMaGV().setEnabled(true);

    }

    private void setButtonState(boolean normal){

        view.getBtnThem().setEnabled(normal);

        view.getBtnSua().setEnabled(normal);

        view.getBtnXoa().setEnabled(normal);

        view.getBtnLuu().setEnabled(!normal);

        view.getBtnHuy().setEnabled(!normal);

        view.getTableGV().setEnabled(normal);

    }

}