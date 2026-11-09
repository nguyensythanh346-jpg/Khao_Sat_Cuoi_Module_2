package quanlydienthoai;

public abstract class DienThoai {
    protected int id;
    protected String ten;
    protected double giaBan;
    protected int soLuong;
    protected String nhaSanXuat;

    public DienThoai(int id, String ten, double giaBan, int soLuong, String nhaSanXuat) {
        this.id = id;
        this.ten = ten;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.nhaSanXuat = nhaSanXuat;
    }
    public abstract String hienThiThongTinRieng();

    // Getter và Setter cho các thuộc tính chung
    public int getId() { return id; }
    public String getTen() { return ten; }
    public double getGiaBan() { return giaBan; }
    public int getSoLuong() { return soLuong; }
    public String getNhaSanXuat() { return nhaSanXuat; }
    @Override
    public String toString() {
        return id + "," + ten + "," + giaBan + "," + soLuong + "," + nhaSanXuat;
    }
}