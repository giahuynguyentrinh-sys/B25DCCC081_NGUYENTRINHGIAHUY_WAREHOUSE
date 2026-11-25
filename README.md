# B25DCCC081_NGUYENTRINHGIAHUY_WAREHOUSE
# Bài thực hành Android – Ứng dụng Quản lý kho hàng (SQLite)

- **Họ tên:** Nguyễn Trịnh Gia Huy
- **Mã sinh viên:** B25DCCC081
- **Lớp:** D25CQCC03-B

## Mô tả

Ứng dụng Android (Java + XML) giúp chủ cửa hàng nhỏ quản lý sản phẩm trong kho: giá bán, số lượng tồn và cảnh báo sắp hết hàng (số lượng < 5). Dữ liệu lưu bằng SQLite (`warehouse.db`, phiên bản 1) thông qua `SQLiteOpenHelper`.

## Cấu trúc mã nguồn (`app/src/main/java/com/example/warehouse`)

| File | Vai trò |
|---|---|
| `Product.java` | Lớp biểu diễn sản phẩm |
| `DatabaseHelper.java` | Tạo bảng `users`, `products`, chèn dữ liệu mẫu, các phương thức truy vấn |
| `LoginActivity.java` | Màn hình 1: đăng nhập (kiểm tra bảng `users`) |
| `MainActivity.java` | Màn hình 2: danh sách sản phẩm và thống kê |
| `AddProductActivity.java` | Màn hình 3: thêm sản phẩm |
| `ProductDetailActivity.java` | Màn hình 4: chi tiết, nhập thêm, bán, xóa |
| `ProductAdapter.java` | Adapter hiển thị danh sách trên `ListView` |

Layout XML và drawable nằm trong `app/src/main/res`.

## Hướng dẫn chạy

1. Mở Android Studio, tạo project mới: **Empty Views Activity**, ngôn ngữ **Java**, package `com.example.warehouse`, Minimum SDK 24 trở lên.
2. Xóa `MainActivity.java` và `activity_main.xml` do Android Studio tạo sẵn, rồi chép đè các thư mục `java`, `res` và file `AndroidManifest.xml` của repo này vào `app/src/main/`.
3. Bấm **Sync Project with Gradle Files**, sau đó chạy (Run) trên máy ảo hoặc điện thoại thật.
4. Đăng nhập bằng tài khoản mẫu: `admin` / `123456`.
