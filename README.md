# BookingTour — Hệ Thống Đặt Tour Du Lịch Trực Tuyến

BookingTour là ứng dụng web đặt tour du lịch được phát triển trên nền tảng Spring Boot. Hệ thống hỗ trợ người dùng tìm kiếm, lọc và đặt tour theo điểm đến, ngày khởi hành và ngân sách; quản lý đơn đặt chỗ, thanh toán trực tuyến qua VNPay và đánh giá tour sau chuyến đi. Kèm theo đó là phân hệ quản trị phục vụ vận hành tour, đơn hàng, người dùng và theo dõi doanh thu.

## Mục lục

- [Tính năng](#tính-năng)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc project](#cấu-trúc-project)
- [Hướng dẫn cài đặt](#hướng-dẫn-cài-đặt)
- [Cấu hình](#cấu-hình)
- [Tài khoản mẫu](#tài-khoản-mẫu)
- [Triển khai với Nginx](#triển-khai-với-nginx)
- [Liên hệ](#liên-hệ)

## Tính năng

### Phân hệ người dùng

| Chức năng | Mô tả |
|---|---|
| Tìm kiếm và lọc tour | Lọc danh sách tour theo thành phố, quốc gia, điểm đến, khoảng giá và ngày khởi hành; hỗ trợ sắp xếp kết quả và phân trang. |
| Chi tiết tour | Hiển thị đầy đủ thông tin lịch trình, điểm đón, nơi lưu trú và phương tiện; ngày khởi hành được trình bày dạng lịch trong phạm vi 3 tháng để người dùng chọn ngày còn mở bán. |
| Đặt tour | Đặt chỗ theo ngày khởi hành và số lượng khách; hệ thống quản lý số chỗ còn trống của từng chuyến đi. |
| Thanh toán trực tuyến | Thanh toán qua cổng VNPay (môi trường sandbox), hiển thị kết quả giao dịch và hỗ trợ thanh toán lại đối với đơn chưa hoàn tất thanh toán. |
| Tra cứu giá vé máy bay | Truy xuất giá vé máy bay rẻ nhất cho hành trình liên quan đến tour thông qua Amadeus API. |
| Quản lý tài khoản | Đăng ký và đăng nhập bằng tài khoản nội bộ hoặc OAuth2 (Google, Facebook); cập nhật hồ sơ, thay đổi mật khẩu và ảnh đại diện. |
| Lịch sử đặt tour | Theo dõi toàn bộ đơn đã đặt cùng trạng thái xác nhận và thanh toán. |
| Tour yêu thích | Lưu các tour quan tâm để xem lại và đặt sau. |
| Đánh giá tour | Chấm điểm và gửi nhận xét cho các tour đã tham gia; điểm trung bình được hiển thị tại trang chi tiết tour. |
| Liên hệ | Gửi yêu cầu, góp ý tới ban quản trị thông qua biểu mẫu liên hệ trên website. |

### Phân hệ quản trị

| Chức năng | Mô tả |
|---|---|
| Dashboard và báo cáo | Màn hình tổng quan kèm biểu đồ doanh thu theo tháng và báo cáo hiệu quả kinh doanh theo từng tour. |
| Quản lý tour | Thêm, sửa, xóa tour; quản lý danh sách tour đang mở bán, tour đã hoàn thành và gia hạn thời gian bán tour. |
| Quản lý ngày khởi hành | Thiết lập các ngày khởi hành, giá vé và số chỗ cho từng tour. |
| Quản lý lịch trình | Xây dựng và chỉnh sửa lịch trình chi tiết của từng tour. |
| Quản lý đặt chỗ | Xem danh sách đơn đặt theo tour, theo dõi trạng thái xác nhận và thanh toán. |
| Quản lý người dùng | Thêm, sửa, xóa tài khoản người dùng; xem tổng chi tiêu của từng khách hàng. |
| Quản lý đánh giá | Xem và kiểm duyệt đánh giá của khách hàng theo tour. |
| Quản lý liên hệ | Tiếp nhận và xử lý các liên hệ do khách hàng gửi về. |

## Công nghệ sử dụng

| Hạng mục | Công nghệ |
|---|---|
| Ngôn ngữ lập trình | Java 17 |
| Framework | Spring Boot 3.5.6 (Spring Web, Spring Data JPA, Spring Security, OAuth2 Client) |
| Giao diện | Thymeleaf, Bootstrap, HTML5, CSS3, JavaScript |
| Cơ sở dữ liệu | MySQL 8.0, Hibernate, JPA Specifications |
| Bảo mật | Spring Security, đăng nhập biểu mẫu và OAuth2 (Google, Facebook), mã hóa mật khẩu BCrypt |
| Thanh toán | VNPay API |
| API bên thứ ba | Amadeus API (dữ liệu chuyến bay, giá vé) |
| Công cụ xây dựng | Maven, Maven Wrapper, Lombok, Git |

## Cấu trúc project

```
BookingTour/
|-- nginx/
|   `-- nginx.conf              # Cấu hình Nginx cho triển khai (tùy chọn)
|-- src/
|   |-- main/
|   |   |-- java/edu/bookingtour/
|   |   |   |-- BookingTourApplication.java
|   |   |   |-- client/         # Client gọi API bên thứ ba (Amadeus)
|   |   |   |-- config/         # Cấu hình bảo mật, VNPay, Web MVC, khởi tạo dữ liệu
|   |   |   |-- controller/
|   |   |   |   |-- admin/      # Controller phân hệ quản trị (/admin/**)
|   |   |   |   `-- user/       # Controller phân hệ người dùng
|   |   |   |-- dto/            # Data Transfer Objects
|   |   |   |-- entity/         # Các thực thể JPA
|   |   |   |-- repo/           # Spring Data JPA Repositories
|   |   |   |-- service/        # Logic nghiệp vụ
|   |   |   `-- specifications/ # JPA Specification phục vụ tìm kiếm, lọc tour
|   |   `-- resources/
|   |       |-- application.properties
|   |       |-- data.sql            # Dữ liệu mẫu, tự động nạp khi khởi động
|   |       |-- db/bookingtour.sql  # Bản dump cơ sở dữ liệu đầy đủ
|   |       |-- static/             # CSS, JavaScript, hình ảnh
|   |       `-- templates/          # Giao diện Thymeleaf
|   `-- test/
|-- mvnw
|-- mvnw.cmd
`-- pom.xml
```

## Hướng dẫn cài đặt

### Yêu cầu hệ thống

- JDK 17 trở lên
- MySQL Server 8.0 trở lên
- Maven 3.x, hoặc sử dụng Maven Wrapper (`mvnw`) đi kèm project

### Các bước cài đặt

**Bước 1 — Clone project**

```bash
git clone https://github.com/hungkinang/BookingTour.git
cd BookingTour
```

**Bước 2 — Tạo cơ sở dữ liệu**

```sql
CREATE DATABASE booking_tour CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Khi khởi động lần đầu, ứng dụng tự động tạo bảng theo các entity (`spring.jpa.hibernate.ddl-auto=update`) và nạp dữ liệu mẫu từ `data.sql`. Nếu cần bộ dữ liệu đầy đủ, import file `src/main/resources/db/bookingtour.sql` vào database `booking_tour` thay cho bước này.

**Bước 3 — Cấu hình ứng dụng** (xem mục [Cấu hình](#cấu-hình))

**Bước 4 — Khởi chạy**

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

**Bước 5 — Truy cập hệ thống** tại địa chỉ `http://localhost:8080`

## Cấu hình

Các thông số kết nối và tích hợp được khai báo trong `src/main/resources/application.properties`:

```properties
# Cơ sở dữ liệu
spring.datasource.url=jdbc:mysql://localhost:3306/booking_tour
spring.datasource.username=<db_username>
spring.datasource.password=<db_password>

# OAuth2 (Google, Facebook)
spring.security.oauth2.client.registration.google.client-id=<google_client_id>
spring.security.oauth2.client.registration.google.client-secret=<google_client_secret>

# Amadeus API
amadeus.api.key=<amadeus_api_key>
amadeus.api.secret=<amadeus_api_secret>

# VNPay (một số giá trị có thể ghi đè bằng biến môi trường)
vnp.tmn-code=${VNP_TMN_CODE:<tmn_code>}
vnp.hash-secret=<vnpay_hash_secret>
```

**Lưu ý bảo mật:** Không đưa thông tin bí mật thật (mật khẩu cơ sở dữ liệu, client secret, khóa API, VNPay hash secret) vào mã nguồn được đẩy lên GitHub. Khi triển khai thực tế, các giá trị này cần được cung cấp qua biến môi trường hoặc file cấu hình nằm ngoài version control.

## Tài khoản mẫu

Dữ liệu mẫu trong `data.sql` khởi tạo sẵn một tài khoản quản trị:

| Vai trò | Tên đăng nhập | Mật khẩu |
|---|---|---|
| Quản trị viên | `admin` | `admin123` |

Tài khoản này chỉ phục vụ mục đích thử nghiệm; cần thay đổi mật khẩu trước khi sử dụng trong môi trường thực tế. Người dùng mới có thể tự đăng ký tài khoản hoặc đăng nhập bằng Google, Facebook.

## Liên hệ

Mọi thắc mắc và góp ý về dự án, vui lòng tạo [Issue](https://github.com/hungkinang/BookingTour/issues) trên repository này.
