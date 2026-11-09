package quanlydienthoai;

public class DienThoaiChinhHang extends DienThoai {
    private int thoiGianBaoHanh; // Số ngày
    private String phamViBaoHanh; // "Toan Quoc" hoặc "Quoc Te"

    public DienThoaiChinhHang(int id, String ten, double giaBan, int soLuong, String nhaSanXuat, int thoiGianBaoHanh, String phamViBaoHanh) {
        super(id, ten, giaBan, soLuong, nhaSanXuat);
        this.thoiGianBaoHanh = thoiGianBaoHanh;
        this.phamViBaoHanh = phamViBaoHanh;
    }

    public int getThoiGianBaoHanh() { return thoiGianBaoHanh; }
    public String getPhamViBaoHanh() { return phamViBaoHanh; }

    @Override
    public String hienThiThongTinRieng() {
        return thoiGianBaoHanh + "," + phamViBaoHanh;
    }

    @Override
    public String toString() {
        return super.toString() + "," + thoiGianBaoHanh + "," + phamViBaoHanh;
    }
}