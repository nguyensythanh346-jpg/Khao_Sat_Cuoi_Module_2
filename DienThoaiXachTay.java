package quanlydienthoai;

public class DienThoaiXachTay extends DienThoai {
    private String quocGiaXachTay;
    private String trangThai; // "Da sua chua" hoặc "Chua sua chua"

    public DienThoaiXachTay(int id, String ten, double giaBan, int soLuong, String nhaSanXuat, String quocGiaXachTay, String trangThai) {
        super(id, ten, giaBan, soLuong, nhaSanXuat);
        this.quocGiaXachTay = quocGiaXachTay;
        this.trangThai = trangThai;
    }

    public String getQuocGiaXachTay() { return quocGiaXachTay; }
    public String getTrangThai() { return trangThai; }

    @Override
    public String hienThiThongTinRieng() {
        return quocGiaXachTay + "," + trangThai;
    }

    @Override
    public String toString() {
        return super.toString() + "," + quocGiaXachTay + "," + trangThai;
    }
}