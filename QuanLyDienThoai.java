package quanlydienthoai;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuanLyDienThoai {
    // Đường dẫn file CSV
    private static final String FILE_PATH = "data/mobiles.csv";
    private List<DienThoai> danhSachDienThoai = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    // Hàm khởi tạo: Tự động đọc dữ liệu từ file khi bắt đầu
    public QuanLyDienThoai() {
        docFileCSV();
    }

    // 1. Hàm đọc dữ liệu từ file CSV
    public void docFileCSV() {
        danhSachDienThoai.clear(); // Xóa danh sách cũ trước khi đọc mới
        File file = new File(FILE_PATH);

        // Nếu file chưa tồn tại thì bỏ qua (hoặc tạo thư mục data)
        if (!file.exists()) {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs(); // Tạo thư mục data nếu chưa có
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) { // Cần ít nhất 7 trường cho điện thoại chính hãng
                    int id = Integer.parseInt(data[0].trim());
                    String ten = data[1].trim();
                    double giaBan = Double.parseDouble(data[2].trim());
                    int soLuong = Integer.parseInt(data[3].trim());
                    String nhaSX = data[4].trim();
                    if (data.length == 7) {
                        try {
                            // Thử parse trường thứ 6 thành số (thời gian bảo hành)
                            int thoiGianBH = Integer.parseInt(data[5].trim());
                            String phamViBH = data[6].trim();
                            danhSachDienThoai.add(new DienThoaiChinhHang(id, ten, giaBan, soLuong, nhaSX, thoiGianBH, phamViBH));
                        } catch (NumberFormatException e) {
                            // Nếu không phải số -> là Xách tay
                            String quocGia = data[5].trim();
                            String trangThai = data[6].trim();
                            danhSachDienThoai.add(new DienThoaiXachTay(id, ten, giaBan, soLuong, nhaSX, quocGia, trangThai));
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Lỗi khi đọc file CSV: " + e.getMessage());
        }
    }

    // 2. Hàm ghi dữ liệu xuống file CSV
    public void ghiFileCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (DienThoai dt : danhSachDienThoai) {
                bw.write(dt.toString()); // toString đã được override để trả về chuỗi CSV
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file CSV: " + e.getMessage());
        }
    }

    // 3. Hàm hiển thị Menu chính
    public void hienThiMenu() {
        int luaChon;
        do {
            System.out.println("\n--- CHƯƠNG TRÌNH QUẢN LÝ ĐIỆN THOẠI ---");
            System.out.println("1. Thêm mới");
            System.out.println("2. Xóa");
            System.out.println("3. Xem danh sách điện thoại");
            System.out.println("4. Tìm kiếm");
            System.out.println("5. Thoát");
            System.out.print("Chọn chức năng: ");

            try {
                luaChon = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
                luaChon = -1;
                continue;
            }

            switch (luaChon) {
                case 1: themMoi(); break;
                case 2: xoa(); break;
                case 3: hienThiDanhSach(); break;
                case 4: timKiem(); break;
                case 5: System.out.println("Thoát chương trình."); break;
                default: System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (luaChon != 5);
    }

    // Các hàm xử lý chi tiết (sẽ được viết ở Bước 4)
    public void themMoi() {
        System.out.println("\n--- THÊM MỚI ĐIỆN THOẠI ---");
        System.out.println("Chọn loại điện thoại: 1. Chính hãng | 2. Xách tay");
        int loai = Integer.parseInt(scanner.nextLine());

        // Tự động tăng ID
        int idMoi = danhSachDienThoai.isEmpty() ? 1 : danhSachDienThoai.get(danhSachDienThoai.size() - 1).getId() + 1;

        System.out.print("Nhập tên điện thoại: ");
        String ten = scanner.nextLine();
        if (ten.trim().isEmpty()) {
            System.out.println("Lỗi: Tên không được để trống!");
            return;
        }

        double giaBan = nhapSoDuong("Nhập giá bán: ");
        if (giaBan == -1) return;

        int soLuong = (int) nhapSoDuong("Nhập số lượng: ");
        if (soLuong == -1) return;

        System.out.print("Nhập nhà sản xuất: ");
        String nhaSX = scanner.nextLine();
        if (nhaSX.trim().isEmpty()) {
            System.out.println("Lỗi: Nhà sản xuất không được để trống!");
            return;
        }

        if (loai == 1) {
            // Validate thời gian bảo hành (1 - 730 ngày)
            int thoiGianBH = -1;
            while (thoiGianBH == -1) {
                System.out.print("Nhập thời gian bảo hành (ngày, <= 730): ");
                try {
                    thoiGianBH = Integer.parseInt(scanner.nextLine());
                    if (thoiGianBH <= 0 || thoiGianBH > 730) {
                        System.out.println("Lỗi: Thời gian bảo hành phải từ 1 đến 730 ngày!");
                        thoiGianBH = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi: Vui lòng nhập số!");
                }
            }

            // Validate phạm vi bảo hành
            String phamVi = "";
            while (true) {
                System.out.print("Nhập phạm vi bảo hành (Toan Quoc / Quoc Te): ");
                phamVi = scanner.nextLine();
                if (phamVi.equalsIgnoreCase("Toan Quoc") || phamVi.equalsIgnoreCase("Quoc Te")) {
                    break;
                }
                System.out.println("Lỗi: Chỉ nhập 'Toan Quoc' hoặc 'Quoc Te'!");
            }

            danhSachDienThoai.add(new DienThoaiChinhHang(idMoi, ten, giaBan, soLuong, nhaSX, thoiGianBH, phamVi));
        } else if (loai == 2) {
            // Validate quốc gia xách tay (không được là Viet Nam)
            String quocGia = "";
            while (true) {
                System.out.print("Nhập quốc gia xách tay: ");
                quocGia = scanner.nextLine();
                if (!quocGia.equalsIgnoreCase("Viet Nam")) {
                    break;
                }
                System.out.println("Lỗi: Quốc gia xách tay không được là 'Viet Nam'!");
            }

            // Validate trạng thái
            String trangThai = "";
            while (true) {
                System.out.print("Nhập trạng thái (Da sua chua / Chua sua chua): ");
                trangThai = scanner.nextLine();
                if (trangThai.equalsIgnoreCase("Da sua chua") || trangThai.equalsIgnoreCase("Chua sua chua")) {
                    break;
                }
                System.out.println("Lỗi: Chỉ nhập 'Da sua chua' hoặc 'Chua sua chua'!");
            }

            danhSachDienThoai.add(new DienThoaiXachTay(idMoi, ten, giaBan, soLuong, nhaSX, quocGia, trangThai));
        } else {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        ghiFileCSV(); // Ghi xuống file sau khi thêm
        System.out.println("=> Thêm mới thành công! ID: " + idMoi);
    }

    // Hàm phụ trợ để nhập số dương
    private double nhapSoDuong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double so = Double.parseDouble(scanner.nextLine());
                if (so > 0) return so;
                System.out.println("Lỗi: Giá trị phải là số dương!");
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số!");
            }
        }
    }
    public void xoa() {
        System.out.println("\n--- XÓA ĐIỆN THOẠI ---");
        System.out.print("Nhập ID điện thoại cần xóa: ");

        try {
            int idXoa = Integer.parseInt(scanner.nextLine());

            // Tìm điện thoại theo ID
            DienThoai dtCanXoa = null;
            for (DienThoai dt : danhSachDienThoai) {
                if (dt.getId() == idXoa) {
                    dtCanXoa = dt;
                    break;
                }
            }

            // Nếu không tìm thấy -> Ném Exception
            if (dtCanXoa == null) {
                throw new NotFoundProductException("ID điện thoại không tồn tại.");
            }

            // Nếu tìm thấy -> Hỏi xác nhận
            System.out.println("Bạn có chắc muốn xóa điện thoại: " + dtCanXoa.getTen() + " (ID: " + idXoa + ")?");
            System.out.print("Nhập 'Yes' để xóa, 'No' để hủy: ");
            String xacNhan = scanner.nextLine();

            if (xacNhan.equalsIgnoreCase("Yes")) {
                danhSachDienThoai.remove(dtCanXoa);
                ghiFileCSV(); // Cập nhật lại file
                System.out.println("=> Đã xóa thành công!");
                hienThiDanhSach(); // Hiển thị lại danh sách sau khi xóa
            } else {
                System.out.println("=> Đã hủy thao tác xóa.");
            }

        } catch (NotFoundProductException e) {
            System.out.println("Lỗi: " + e.getMessage());
            System.out.println("Nhấn Enter để quay lại menu chính...");
            scanner.nextLine();
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID phải là số!");
        }
    }
    public void hienThiDanhSach() {
        System.out.println("\n--- DANH SÁCH ĐIỆN THOẠI ---");
        if (danhSachDienThoai.isEmpty()) {
            System.out.println("Danh sách hiện đang trống!");
            return;
        }

        for (DienThoai dt : danhSachDienThoai) {
            // In thông tin chung
            System.out.printf("ID: %d | Tên: %s | Giá: %.0f | SL: %d | NSX: %s",
                    dt.getId(), dt.getTen(), dt.getGiaBan(), dt.getSoLuong(), dt.getNhaSanXuat());

            // In thông tin riêng dựa vào loại điện thoại
            if (dt instanceof DienThoaiChinhHang) {
                DienThoaiChinhHang chinhHang = (DienThoaiChinhHang) dt;
                System.out.printf(" | Bảo hành: %d ngày | Phạm vi: %s%n",
                        chinhHang.getThoiGianBaoHanh(), chinhHang.getPhamViBaoHanh());
            } else if (dt instanceof DienThoaiXachTay) {
                DienThoaiXachTay xachTay = (DienThoaiXachTay) dt;
                System.out.printf(" | Quốc gia: %s | Trạng thái: %s%n",
                        xachTay.getQuocGiaXachTay(), xachTay.getTrangThai());
            }
        }
        System.out.println("---------------------------------------------------------------");
    }
    public void timKiem() {
        System.out.println("\n--- TÌM KIẾM ĐIỆN THOẠI ---");
        System.out.print("Nhập ID hoặc Tên điện thoại cần tìm: ");
        String tuKhoa = scanner.nextLine().toLowerCase();

        boolean timThay = false;
        System.out.println("--- KẾT QUẢ TÌM KIẾM ---");

        for (DienThoai dt : danhSachDienThoai) {
            // Tìm kiếm gần đúng theo ID (chuyển ID thành chuỗi để so sánh chứa) hoặc theo Tên
            if (String.valueOf(dt.getId()).contains(tuKhoa) || dt.getTen().toLowerCase().contains(tuKhoa)) {
                // In thông tin giống hàm hiển thị
                System.out.printf("ID: %d | Tên: %s | Giá: %.0f | SL: %d | NSX: %s",
                        dt.getId(), dt.getTen(), dt.getGiaBan(), dt.getSoLuong(), dt.getNhaSanXuat());

                if (dt instanceof DienThoaiChinhHang) {
                    DienThoaiChinhHang chinhHang = (DienThoaiChinhHang) dt;
                    System.out.printf(" | Bảo hành: %d ngày | Phạm vi: %s%n",
                            chinhHang.getThoiGianBaoHanh(), chinhHang.getPhamViBaoHanh());
                } else if (dt instanceof DienThoaiXachTay) {
                    DienThoaiXachTay xachTay = (DienThoaiXachTay) dt;
                    System.out.printf(" | Quốc gia: %s | Trạng thái: %s%n",
                            xachTay.getQuocGiaXachTay(), xachTay.getTrangThai());
                }
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("Không tìm thấy điện thoại nào phù hợp với từ khóa: " + tuKhoa);
        }
        System.out.println("---------------------------------------------------------------");
    }

    // Hàm main để chạy chương trình
    public static void main(String[] args) {
        QuanLyDienThoai ql = new QuanLyDienThoai();
        ql.hienThiMenu();
    }
}