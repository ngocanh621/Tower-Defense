# 📊 KẾ HOẠCH THỰC HIỆN - Tower Defense 2D MVP

**Thời Gian Dự Án:** 18 ngày làm việc (3 tuần)  
**Dự Kiến Hoàn Thành:** 2026-07-31  
**Tạo:** 2026-07-13

---

## Mục Lục

1. [Tóm Tắt Điều Hành](#tóm-tắt-điều-hành)
2. [Các Giai Đoạn Dự Án](#các-giai-đoạn-dự-án)
3. [Chi Tiết Phân Chia Nhiệm Vụ](#chi-tiết-phân-chia-nhiệm-vụ)
4. [Phụ Thuộc & Mốc Quan Trọng](#phụ-thuộc--mốc-quan-trọng)
5. [Quản Lý Rủi Ro](#quản-lý-rủi-ro)
6. [Tiêu Chí Thành Công](#tiêu-chí-thành-công)

---

## Tóm Tắt Điều Hành

Tài liệu này nêu chi tiết kế hoạch thực hiện hoàn chỉnh cho Tower Defense 2D MVP trong 3 tuần với phân chia nhiệm vụ theo ngày.

**Các Ngày Quan Trọng:**
- **Tuần 1 (Ngày 1-6):** Core Mechanics - Lưới, Kẻ Thù, Tháp, Chiến Đấu
- **Tuần 2 (Ngày 7-12):** Đồ Họa & Nội Dung - Sprite, Âm Thanh, UI, Sóng Quái
- **Tuần 3 (Ngày 13-18):** Cân Bằng & Hoàn Chỉnh - Điều Chỉnh, Hiệu Suất, Phát Hành

**Năng Lực Đội:** Một lập trình viên toàn thời gian  
**Phương Pháp:** Agile với check-in hàng ngày và theo dõi tiến độ

---

## Các Giai Đoạn Dự Án

### Giai Đoạn 1: Nền Tảng (Ngày 1) ✅ HOÀN THÀNH

**Mục Tiêu:** Thiết lập cấu trúc dự án và vòng lặp game cơ bản

**Trạng Thái:** ✅ HOÀN THÀNH 2026-07-13

**Kết Quả Giao Hàng:**
- Dự án Maven với JavaFX 21
- Game loop (60 FPS AnimationTimer)
- Scene manager (Menu ↔ Game chuyển tiếp)
- Các lớp cơ bản (Entity, EventBus, ObjectPool)
- Tài liệu (API docs, dev guide)

---

### Giai Đoạn 2: Core Mechanics (Ngày 2-6) ⏳ ĐANG TIẾN HÀNH

**Mục Tiêu:** Triển khai game có thể chơi được với mechanics cơ bản

**Khoảng Thời Gian:** 2026-07-14 đến 2026-07-19

**Kết Quả Giao Hàng Chính:**
- Hệ thống lưới với tải bản đồ CSV
- Chuyển động kẻ thù với pathfinding waypoint
- Đặt tháp với hệ thống nhắm mục tiêu
- Hệ thống chiến đấu với đạn
- Hệ thống sóng quái với 3 sóng kiểm tra

**Tiêu Chí Thành Công:**
- Có thể đặt tháp → tháp bắn → kẻ thù chết → trao vàng
- Hoàn thành 3 sóng mà không lỗi
- Không có lỗi gameplay lớn

---

### Giai Đoạn 3: Đồ Họa & Nội Dung (Ngày 7-12) ⏳ CẦN LÀAM

**Mục Tiêu:** Thêm đồ họa pixel, âm thanh và nội dung cuối cùng

**Khoảng Thời Gian:** 2026-07-22 đến 2026-07-26

**Kết Quả Giao Hàng Chính:**
- Sprite pixel art được tích hợp
- Hiệu ứng âm thanh và nhạc nền
- HUD/UI được hoàn chỉnh
- 5 sóng hoàn chỉnh với boss
- Hệ thống nâng cấp tháp (tùy chọn)

**Tiêu Chí Thành Công:**
- Game có vẻ chuyên nghiệp và âm thanh tuyệt vời
- Có thể chơi tất cả các sóng
- Có thể thắng và thua game

---

### Giai Đoạn 4: Cân Bằng & Hoàn Chỉnh (Ngày 13-18) ⏳ CẦN LÀAM

**Mục Tiêu:** Điều chỉnh gameplay, tối ưu hóa và phát hành

**Khoảng Thời Gian:** 2026-07-29 đến 2026-07-31

**Kết Quả Giao Hàng Chính:**
- Cân bằng game (chỉ số kẻ thù, chi phí tháp)
- Tối ưu hóa hiệu suất
- Sửa lỗi từ QA
- Đóng gói JAR có thể thực thi
- Tài liệu phát hành

**Tiêu Chí Thành Công:**
- Game chạy ở 60 FPS liên tục
- Cân bằng khó độ (Normal có thể thắng)
- Không có lỗi quan trọng
- Package phát hành sạch sẽ

---

## Chi Tiết Phân Chia Nhiệm Vụ

### TUẦN 1: CORE MECHANICS

---

#### NGÀY 1: Cài Đặt Dự Án & Game Loop ✅

**Thời Gian:** 2 giờ  
**Trạng Thái:** ✅ HOÀN THÀNH

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Hoàn Thành | Ghi Chú |
|----|----------|-----------|-----------|-----------|---------|
| W1D1-1 | Tạo cấu trúc dự án Maven | 15m | ✅ | 100% | `pom.xml` được cấu hình |
| W1D1-2 | Cấu hình phụ thuộc JavaFX 21 | 15m | ✅ | 100% | Tất cả module được thiết lập |
| W1D1-3 | Tạo lớp `GameApplication.java` chính | 20m | ✅ | 100% | Entry point sẵn sàng |
| W1D1-4 | Triển khai `GameLoop` với AnimationTimer | 30m | ✅ | 100% | Vòng lặp 60 FPS hoạt động |
| W1D1-5 | Tạo `SceneManager` cho chuyển tiếp cảnh | 20m | ✅ | 100% | Menu ↔ Game chuyển tiếp |
| W1D1-6 | Thiết lập Canvas + GraphicsContext | 15m | ✅ | 100% | Vẽ sẵn sàng |
| W1D1-7 | Tạo lớp cơ bản `Entity` | 15m | ✅ | 100% | Nền tảng cho tất cả đối tượng |
| W1D1-8 | Triển khai hệ thống `EventBus` | 20m | ✅ | 100% | Xuất bản sự kiện sẵn sàng |
| W1D1-9 | Tạo generic `ObjectPool<T>` | 20m | ✅ | 100% | Mẫu pool được triển khai |
| W1D1-10 | Tạo bộ điều khiển `PlayerState` | 15m | ✅ | 100% | Quản lý HP/Vàng |
| W1D1-11 | Thiết lập `GameConfig` và `Constants` | 15m | ✅ | 100% | Tất cả hằng số tập trung |
| W1D1-12 | Tạo tài liệu dự án | 30m | ✅ | 100% | README, hướng dẫn, checklist |
| W1D1-13 | Khởi tạo repository Git | 10m | ⏳ | 0% | Sẵn sàng khi cần thiết |

**Hoàn Thành:** 100% ✅

**Git Commit:** `feat: cài đặt dự án + nền tảng game loop`

---

#### NGÀY 2: Hệ Thống Lưới & Bản Đồ ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ  
**Trạng Thái:** ⏳ Chưa Bắt Đầu

**Phụ Thuộc:** Hoàn thành Ngày 1 ✅

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W1D2-1 | Tạo lớp `Grid.java` (M×N ô) | 30m | ⏳ | Mảng ô, accessors |
| W1D2-2 | Tạo `MapManager.java` để tải CSV | 30m | ⏳ | Parse CSV, xác thực bản đồ |
| W1D2-3 | Triển khai render lưới trên Canvas | 45m | ⏳ | Vẽ ô với các màu |
| W1D2-4 | Thêm highlight di chuột ô | 30m | ⏳ | Trình xử lý di chuyển chuột |
| W1D2-5 | Tạo file bản đồ mẫu (3 biến thể) | 20m | ⏳ | Map1.csv, Map2.csv, Map3.csv |
| W1D2-6 | Kiểm tra tích hợp: tải bản đồ, render, highlight | 15m | ⏳ | Kiểm tra thủ công |
| W1D2-7 | Sửa lỗi & hoàn chỉnh | 15m | ⏳ | Các vấn đề tìm thấy |

**Tiêu Chí Chấp Nhận:**
- ✅ Bản đồ tải từ CSV mà không lỗi
- ✅ Lưới render chính xác với 32×18 ô
- ✅ Ô highlight khi di chuột
- ✅ Không có glitch render

**Git Commit:** `feat: hệ thống lưới + tải bản đồ CSV`

---

#### NGÀY 3: Pathfinding & Chuyển Động Kẻ Thù ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 4 giờ  
**Trạng Thái:** ⏳ Chưa Bắt Đầu

**Phụ Thuộc:** Hoàn thành Ngày 2 (dữ liệu bản đồ)

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W1D3-1 | Tạo lớp kẻ thù `Enemy` trừu tượng | 30m | ⏳ | Mở rộng Entity, định nghĩa interface |
| W1D3-2 | Triển khai loại kẻ thù `Goblin` | 20m | ⏳ | HP=50, Speed=120, Reward=10 |
| W1D3-3 | Triển khai loại kẻ thù `Orc` | 20m | ⏳ | HP=200, Speed=60, Reward=25 |
| W1D3-4 | Triển khai loại kẻ thù `Dragon` | 20m | ⏳ | HP=500, Speed=40, Reward=60 |
| W1D3-5 | Tạo `EnemyFactory` để tạo entities | 20m | ⏳ | Mẫu Factory |
| W1D3-6 | Triển khai pathfinding dựa trên waypoint | 45m | ⏳ | Tải waypoint từ bản đồ, lerp chuyển động |
| W1D3-7 | Triển khai logic chết/xóa kẻ thù | 20m | ⏳ | Despawn, xuất bản sự kiện ENEMY_DIED |
| W1D3-8 | Triển khai logic tiếp cận cơ sở | 20m | ⏳ | Làm sát thương người chơi, xuất bản sự kiện |
| W1D3-9 | Render kẻ thù trên canvas | 30m | ⏳ | Vẽ hình tròn/placeholder có màu |
| W1D3-10 | Kiểm tra tích hợp: spawn → chuyển động → xóa | 20m | ⏳ | Kiểm tra thủ công |

**Tiêu Chí Chấp Nhận:**
- ✅ Kẻ thù spawn tại điểm spawn
- ✅ Kẻ thù theo waypoint trơn tru
- ✅ Kẻ thù tiếp cận cơ sở và gây sát thương
- ✅ Kẻ thù hiển thị trên canvas
- ✅ Không có glitch pathfinding

**Git Commit:** `feat: entity kẻ thù + chuyển động waypoint`

---

#### NGÀY 4: Hệ Thống Đặt Tháp ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 4 giờ  
**Trạng Thái:** ⏳ Chưa Bắt Đầu

**Phụ Thuộc:** Ngày 2 (lưới), Ngày 3 (kẻ thù)

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W1D4-1 | Tạo lớp tháp `Tower` trừu tượng | 30m | ⏳ | Cơ sở cho GunTower, SlowTower |
| W1D4-2 | Triển khai `GunTower` (nhanh, sát thương thấp) | 25m | ⏳ | Range=150, FireRate=1.0, Cost=100 |
| W1D4-3 | Triển khai `SlowTower` (chậm, debuff) | 25m | ⏳ | Range=200, FireRate=0.5, Cost=150 |
| W1D4-4 | Tạo `TowerFactory` | 20m | ⏳ | Mẫu Factory để tạo tháp |
| W1D4-5 | Triển khai trình xử lý click đặt tháp | 40m | ⏳ | Click chuột → đặt tháp (nếu có tiền) |
| W1D4-6 | Triển khai phát hiện phạm vi tháp | 25m | ⏳ | Tìm kẻ thù trong phạm vi bằng khoảng cách |
| W1D4-7 | Tạo lớp render `HUD` | 30m | ⏳ | Vẽ thanh HP, bộ đếm vàng, thông tin sóng |
| W1D4-8 | Render tháp trên canvas | 30m | ⏳ | Vẽ hình vuông/placeholder có màu |
| W1D4-9 | Render xem trước phạm vi tháp khi di chuột | 25m | ⏳ | Hiển thị hình tròn khi chọn vị trí tháp |
| W1D4-10 | Kiểm tra tích hợp: click → tháp đặt → vàng trừ | 20m | ⏳ | Kiểm tra thủ công |

**Tiêu Chí Chấp Nhận:**
- ✅ Click ô trống → tháp được đặt (nếu có vàng đủ)
- ✅ Tháp hiển thị trên canvas
- ✅ Phạm vi được hiển thị khi di chuột
- ✅ Vàng trừ từ trạng thái người chơi
- ✅ HUD hiển thị HP/Vàng chính xác

**Git Commit:** `feat: đặt tháp + trạng thái người chơi`

---

#### NGÀY 5: Chiến Đấu & Đạn ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ  
**Trạng Thái:** ⏳ Chưa Bắt Đầu

**Phụ Thuộc:** Ngày 3 (kẻ thù), Ngày 4 (tháp)

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W1D5-1 | Tạo lớp `Projectile` (Poolable) | 30m | ⏳ | Triển khai interface Poolable |
| W1D5-2 | Triển khai logic bắn tháp | 40m | ⏳ | Bộ đếm fire rate, lựa chọn mục tiêu |
| W1D5-3 | Triển khai chuyển động đạn (homing) | 30m | ⏳ | Di chuyển tới mục tiêu mỗi frame |
| W1D5-4 | Triển khai phát hiện va chạm | 25m | ⏳ | Kiểm tra overlap Projectile-Enemy |
| W1D5-5 | Triển khai ứng dụng sát thương | 25m | ⏳ | Ứng dụng sát thương, giảm HP kẻ thù |
| W1D5-6 | Render đạn trên canvas | 20m | ⏳ | Vẽ như hình tròn nhỏ |
| W1D5-7 | Thiết lập ObjectPool cho đạn | 20m | ⏳ | Cấp phát trước 100 đạn |
| W1D5-8 | Xác minh cơ chế debuff tháp chậm | 20m | ⏳ | Giảm tốc độ kẻ thù tạm thời |
| W1D5-9 | Kiểm tra tích hợp: bắn → bhit → sát thương | 20m | ⏳ | Kiểm tra gameplay thủ công |
| W1D5-10 | Kiểm tra hiệu suất: 100 đạn | 15m | ⏳ | Xác minh 60 FPS với object pool |

**Tiêu Chí Chấp Nhận:**
- ✅ Tháp bắn tại kẻ thù trong phạm vi
- ✅ Đạn di chuyển trơn tru tới mục tiêu
- ✅ Đạn va chạm với kẻ thù
- ✅ Kẻ thù nhận sát thương khi trúng
- ✅ ObjectPool hoạt động hiệu quả
- ✅ Duy trì 60 FPS

**Git Commit:** `feat: hệ thống chiến đấu + object pool đạn`

---

#### NGÀY 6: Hệ Thống Sóng & Tích Hợp ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ  
**Trạng Thái:** ⏳ Chưa Bắt Đầu

**Phụ Thuộc:** Tất cả các nhiệm vụ Ngày 1-5

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W1D6-1 | Tạo lớp dữ liệu `Wave` | 20m | ⏳ | Số sóng, danh sách kẻ thù |
| W1D6-2 | Tạo `WaveManager` cho tiến trình sóng | 40m | ⏳ | Tải sóng, spawn entities, theo dõi tiến độ |
| W1D6-3 | Triển khai logic spawn sóng | 35m | ⏳ | Spawn kẻ thù theo khoảng thời gian |
| W1D6-4 | Triển khai trễ giữa các sóng (10 giây) | 15m | ⏳ | Tạm dừng giữa các sóng |
| W1D6-5 | Tải 3 sóng mẫu từ JSON | 15m | ⏳ | Parse file waves.json |
| W1D6-6 | Triển khai sự kiện bắt đầu/kết thúc sóng | 20m | ⏳ | Xuất bản qua EventBus |
| W1D6-7 | Triển khai điều kiện thắng (tất cả sóng xóa) | 15m | ⏳ | Xuất bản sự kiện GAME_WON |
| W1D6-8 | Triển khai điều kiện thua (HP = 0) | 15m | ⏳ | Đã có trong PlayerState, kích hoạt game over |
| W1D6-9 | Kiểm tra vòng lặp game đầy đủ: menu → chơi → sóng | 45m | ⏳ | Chơi qua tất cả 3 sóng |
| W1D6-10 | Sửa lỗi từ chơi qua đầy đủ | 30m | ⏳ | Giải quyết vấn đề tìm thấy |
| W1D6-11 | Cân bằng đầu tiên: điều chỉnh số liệu | 20m | ⏳ | Điều chỉnh HP kẻ thù, chi phí tháp, v.v. |

**Tiêu Chí Chấp Nhận:**
- ✅ Sóng spawn kẻ thù chính xác
- ✅ Có thể hoàn thành tất cả 3 sóng
- ✅ Màn hình thắng/thua xuất hiện chính xác
- ✅ Không có lỗi hoặc crash lớn
- ✅ Vòng lặp cốt lõi có thể chơi từ đầu đến cuối
- ✅ Có thể khởi động lại game từ menu

**Git Commit:** `feat: hệ thống sóng + kiểm tra tích hợp đầy đủ W1`

**Mốc Quan Trọng:** 🎯 **Kết Thúc Tuần 1 - Core Mechanics Hoàn Thành**

---

### TUẦN 2: ĐỒ HỌA & NỘI DUNG

---

#### NGÀY 7: Pipeline Tài Sản & Hệ Thống Sprite ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ

**Phụ Thuộc:** Tuần 1 hoàn thành

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W2D7-1 | Chọn chủ đề: Trung Cổ vs Sci-Fi | 15m | ⏳ | Chọn một cho tính nhất quán |
| W2D7-2 | Tải xuống/thu thập tài sản pixel art | 45m | ⏳ | Từ itch.io, OpenGameArt.org |
| W2D7-3 | Tạo lớp `SpriteSheet` | 30m | ⏳ | Tải PNG, trích xuất khung hình |
| W2D7-4 | Tạo lớp `AnimationPlayer` | 30m | ⏳ | Chu kỳ qua các khung hình hoạt ảnh |
| W2D7-5 | Tích hợp sprite vào render kẻ thù | 20m | ⏳ | Thay thế placeholder |
| W2D7-6 | Tích hợp sprite vào render tháp | 20m | ⏳ | Thay thế placeholder |
| W2D7-7 | Xác minh tải sprite & hoạt ảnh | 15m | ⏳ | Kiểm tra glitch trực quan |

**Git Commit:** `feat: hệ thống sprite + animation player`

---

#### NGÀY 8: Tileset Bản Đồ & Hoàn Chỉnh Trực Quan ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3.5 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W2D8-1 | Thay thế lưới bằng render dựa trên tileset | 45m | ⏳ | Cỏ, đường, gạch nước |
| W2D8-2 | Tạo render bản đồ đa lớp | 30m | ⏳ | Nền, địa hình, lớp phủ |
| W2D8-3 | Xem trước đặt tháp (ghost sprite) | 25m | ⏳ | Hiển thị xem trước tháp bán trong suốt |
| W2D8-4 | Hoạt ảnh chết kẻ thù (fade/explosion) | 30m | ⏳ | Phản hồi trực quan |
| W2D8-5 | Sprite đạn (mũi tên/tia laser) | 20m | ⏳ | Thay thế hình tròn placeholder |
| W2D8-6 | Kiểm tra tất cả thay đổi trực quan | 20m | ⏳ | QA trực quan |

**Git Commit:** `art: tileset bản đồ + sprite entity được tích hợp`

---

#### NGÀY 9: UI/UX - HUD & Menu ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W2D9-1 | Thiết kế UI menu chính (FXML) | 30m | ⏳ | Nút Chơi, Cài Đặt, Thoát |
| W2D9-2 | Tạo bộ điều khiển menu | 25m | ⏳ | Xử lý nút |
| W2D9-3 | Nâng cao HUD: trực quan thanh sức khỏe | 25m | ⏳ | Thanh đỏ, nhãn văn bản |
| W2D9-4 | Nâng cao HUD: bộ đếm vàng | 20m | ⏳ | Hiển thị vàng hiện tại |
| W2D9-5 | Nâng cao HUD: chỉ báo sóng | 20m | ⏳ | Hiển thị "Sóng 2/5" |
| W2D9-6 | Bảng chọn tháp (thanh dưới) | 30m | ⏳ | Hiển thị tháp khả dụng, chi phí |
| W2D9-7 | Xem trước phạm vi tháp khi di chuột | 20m | ⏳ | Hình tròn trực quan hiển thị phạm vi |
| W2D9-8 | Menu tạm dừng (phím ESC) | 25m | ⏳ | Tiếp tục, Cài Đặt, Thoát |
| W2D9-9 | Màn hình Kết Thúc Game | 20m | ⏳ | Hiển thị điểm cuối cùng, nút Thử Lại |
| W2D9-10 | Màn hình Chiến Thắng | 20m | ⏳ | Hiển thị hoàn thành, phần thưởng |

**Git Commit:** `feat: đầy đủ HUD + menu screens`

---

#### NGÀY 10: Nâng Cấp & Bán Tháp (Tính Năng Tùy Chọn) ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 2 giờ  
**Trạng Thái:** Tùy chọn (thực hiện nếu vượt tiến độ)

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W2D10-1 | Click tháp → menu bật lên | 25m | ⏳ | Nút Nâng Cấp, Bán |
| W2D10-2 | Triển khai logic nâng cấp | 25m | ⏳ | Tăng sát thương, phạm vi, fire rate |
| W2D10-3 | Triển khai logic bán | 20m | ⏳ | Hoàn lại 50% chi phí tháp |
| W2D10-4 | Xác minh cơ chế nâng cấp/bán | 20m | ⏳ | Kiểm tra |

**Git Commit:** `feat: hệ thống nâng cấp/bán tháp` (nếu hoàn thành)

---

#### NGÀY 11: Hiệu Ứng Âm Thanh & Nhạc Nền ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W2D11-1 | Tạo singleton `AudioManager` | 30m | ⏳ | Điều khiển âm thanh tập trung |
| W2D11-2 | Thêm SFX bắn tháp | 20m | ⏳ | Âm thanh pew/whoosh ngắn |
| W2D11-3 | Thêm SFX chết kẻ thù | 20m | ⏳ | Âm thanh chết/bị bắn |
| W2D11-4 | Thêm SFX bắt đầu sóng | 15m | ⏳ | Âm thanh báo động/fanfare |
| W2D11-5 | Thêm SFX game over | 15m | ⏳ | Âm thanh buồn/kịch tính |
| W2D11-6 | Tìm/tải xuống nhạc miễn phí bản quyền | 30m | ⏳ | Vòng lặp BGM nền |
| W2D11-7 | Thêm nhạc nền vào game | 20m | ⏳ | Vòng lặp trong khi chơi |
| W2D11-8 | Tạo bảng cài đặt âm lượng | 25m | ⏳ | Thanh trượt Nhạc/SFX |
| W2D11-9 | Kiểm tra tất cả tích hợp âm thanh | 15m | ⏳ | QA âm thanh |

**Git Commit:** `feat: hệ thống âm thanh + tích hợp sfx`

---

#### NGÀY 12: Mở Rộng Nội Dung & Hoàn Chỉnh Bản Đồ ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W2D12-1 | Tạo 3 bản đồ bổ sung | 30m | ⏳ | Đa dạng thiết kế cấp độ |
| W2D12-2 | Mở rộng sóng thành 5 sóng tổng cộng | 30m | ⏳ | Tăng độ khó |
| W2D12-3 | Thêm enemy boss sóng | 20m | ⏳ | Boss đặc biệt 1 mỗi sóng |
| W2D12-4 | Cân bằng lần 2: phản hồi sớm | 20m | ⏳ | Điều chỉnh dựa trên cảm giác độ khó |
| W2D12-5 | Hoàn chỉnh tất cả chuyển tiếp trực quan | 20m | ⏳ | Fade mịn, hoạt ảnh |
| W2D12-6 | Ghi hình gameplay demo | 20m | ⏳ | Để portfolio/showcase |
| W2D12-7 | Thu thập ảnh chụp màn hình | 15m | ⏳ | Để tài liệu |

**Git Commit:** `content: 5 sóng + sóng boss + hoàn thiện bản đồ`

**Mốc Quan Trọng:** 🎯 **Kết Thúc Tuần 2 - Đồ Họa & Nội Dung Hoàn Thành**

---

### TUẦN 3: CÂN BẰNG & HOÀN CHỈNH

---

#### NGÀY 13: Cân Bằng Game Lần 1 ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W3D13-1 | Tạo bảng tính cân bằng game | 30m | ⏳ | Tài liệu tất cả số liệu |
| W3D13-2 | Chơi qua tất cả sóng, ghi chú độ khó | 60m | ⏳ | Xác định vấn đề cân bằng |
| W3D13-3 | Điều chỉnh giá trị HP kẻ thù | 20m | ⏳ | Quá dễ? Quá khó? |
| W3D13-4 | Điều chỉnh tốc độ chuyển động kẻ thù | 15m | ⏳ | Điều chỉnh nhịp độ |
| W3D13-5 | Điều chỉnh chi phí tháp | 15m | ⏳ | Cân bằng tiến trình |
| W3D13-6 | Điều chỉnh giá trị sát thương tháp | 15m | ⏳ | Hiệu quả |
| W3D13-7 | Điều chỉnh phần thưởng vàng | 15m | ⏳ | Cân bằng kinh tế |
| W3D13-8 | Kiểm tra cân bằng được cập nhật | 30m | ⏳ | Chơi lại qua tất cả |

**Git Commit:** `balance: điều chỉnh chỉ số kẻ thù + tháp lần 1`

---

#### NGÀY 14: Hiệu Suất & Sửa Lỗi ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W3D14-1 | Profile game với heap dumps | 30m | ⏳ | Kiểm tra rò rỉ bộ nhớ |
| W3D14-2 | Phân tích áp lực GC | 20m | ⏳ | Xác minh object pooling hoạt động |
| W3D14-3 | Tối ưu hóa số lượng gọi render | 20m | ⏳ | Giảm thao tác Canvas |
| W3D14-4 | Sửa các lỗi rõ ràng | 40m | ⏳ | Giải quyết vấn đề được báo cáo |
| W3D14-5 | Xác minh 60 FPS được duy trì | 20m | ⏳ | Xuyên suốt gameplay |
| W3D14-6 | Thêm phân vùng không gian nếu cần | 30m | ⏳ | Nếu 100+ kẻ thù lag |

**Git Commit:** `fix: tối ưu hóa hiệu suất + sửa lỗi`

---

#### NGÀY 15: Hoàn Chỉnh Trực Quan & Hiệu Ứng ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W3D15-1 | Hệ thống hạt cho hiệu ứng | 40m | ⏳ | Bụi, tia lửa, máu |
| W3D15-2 | Hạt chết kẻ thù | 20m | ⏳ | Hiệu ứng nổ |
| W3D15-3 | Tia chớp nòng tháp bắn | 20m | ⏳ | Phản hồi trực quan |
| W3D15-4 | Chỉ báo hiệu ứng chậm | 20m | ⏳ | Halo lạnh/xanh |
| W3D15-5 | Rung màn hình khi bị bắn | 20m | ⏳ | Phản hồi tác động |
| W3D15-6 | Số sát thương nổi | 20m | ⏳ | Hiển thị sát thương gây ra |
| W3D15-7 | Easing mềm mại cho tất cả chuyển động | 15m | ⏳ | Chuyển động ít giật gân hơn |
| W3D15-8 | Hoàn chỉnh hoạt ảnh UI | 15m | ⏳ | Hover nút, chuyển tiếp |

**Git Commit:** `polish: hiệu ứng hạt + rung màn hình`

---

#### NGÀY 16: Cân Bằng Game Lần 2 & Độ Khó ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W3D16-1 | Thêm UI chọn độ khó | 30m | ⏳ | Nút Dễ/Bình Thường/Khó |
| W3D16-2 | Triển khai scaling độ khó | 25m | ⏳ | Nhân chỉ số kẻ thù theo độ khó |
| W3D16-3 | Kiểm tra chế độ Dễ (nên dễ thắng) | 20m | ⏳ | Xác minh cân bằng |
| W3D16-4 | Kiểm tra chế độ Bình Thường (thách thức nhưng công bằng) | 20m | ⏳ | Xác minh cân bằng |
| W3D16-5 | Kiểm tra chế độ Khó (yêu cầu chiến lược) | 20m | ⏳ | Xác minh cân bằng |
| W3D16-6 | Điều chỉnh cân bằng cuối cùng | 30m | ⏳ | Dựa trên kiểm tra |
| W3D16-7 | Điều chỉnh fine-tune lần 2 | 20m | ⏳ | Điều chỉnh cuối cùng |

**Git Commit:** `balance: hệ thống độ khó + điều chỉnh cuối cùng`

---

#### NGÀY 17: QA Cuối Cùng & Trường Hợp Biên ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 3 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W3D17-1 | Kiểm tra: đặt tháp trên đường (không hợp lệ) | 15m | ⏳ | Nên từ chối |
| W3D17-2 | Kiểm tra: mua tháp không đủ vàng | 15m | ⏳ | Nên từ chối |
| W3D17-3 | Kiểm tra: hoàn thành game ở tất cả độ khó | 45m | ⏳ | Điều kiện thắng |
| W3D17-4 | Kiểm tra: thua game vì HP → 0 | 15m | ⏳ | Điều kiện thua |
| W3D17-5 | Kiểm tra: tạm dừng và tiếp tục | 10m | ⏳ | Chức năng tạm dừng |
| W3D17-6 | Kiểm tra: menu cài đặt (thanh trượt âm lượng) | 10m | ⏳ | Lưu cài đặt |
| W3D17-7 | Kiểm tra: quay lại menu giữa chơi | 10m | ⏳ | Reset trạng thái |
| W3D17-8 | Kiểm tra: khởi động lại game nhiều lần | 15m | ⏳ | Không có rò rỉ bộ nhớ |
| W3D17-9 | Viết README với hướng dẫn cài đặt | 20m | ⏳ | Tài liệu |
| W3D17-10 | Kiểm tra đa nền tảng (nếu có thể) | 15m | ⏳ | Windows/Mac/Linux |
| W3D17-11 | Tạo JAR tập tin cuối cùng | 15m | ⏳ | Package có thể thực thi |
| W3D17-12 | Kiểm tra thực thi JAR | 10m | ⏳ | Xác minh nó hoạt động độc lập |

**Git Commit:** `qa: sửa trường hợp biên + đóng gói`

---

#### NGÀY 18: Phát Hành & Tài Liệu ⏳ CẦN LÀAM

**Thời Gian Ước Tính:** 2 giờ

##### Nhiệm Vụ

| ID | Nhiệm Vụ | Thời Gian | Trạng Thái | Ghi Chú |
|----|----------|-----------|-----------|---------|
| W3D18-1 | Tạo tag phát hành Git (v1.0.0-mvp) | 15m | ⏳ | Kiểm soát phiên bản |
| W3D18-2 | Viết ghi chú phát hành | 30m | ⏳ | Tính năng, vấn đề đã biết |
| W3D18-3 | Ghi hình video gameplay demo | 30m | ⏳ | 60-90 giây |
| W3D18-4 | Tạo trang phát hành GitHub | 20m | ⏳ | Liên kết tải xuống |
| W3D18-5 | Xem xét tài liệu cuối cùng | 20m | ⏳ | Đảm bảo mọi thứ rõ ràng |
| W3D18-6 | Sao lưu mã nguồn | 10m | ⏳ | Ổ cứng bên ngoài/đám mây |
| W3D18-7 | Chúc mừng! 🎉 | N/A | ⏳ | Dự án hoàn thành! |

**Git Commit:** `release: v1.0.0-mvp`

**Mốc Quan Trọng:** 🎯 **PHÁT HÀNH - Tower Defense 2D MVP Hoàn Thành!**

---

## Phụ Thuộc & Mốc Quan Trọng

### Cây Phụ Thuộc

```
Ngày 1: Nền Tảng
  ↓
Ngày 2: Hệ Thống Lưới (phụ thuộc: Ngày 1)
  ↓
Ngày 3: Chuyển Động Kẻ Thù (phụ thuộc: Ngày 2)
  ↓
Ngày 4: Đặt Tháp (phụ thuộc: Ngày 2, 3)
  ↓
Ngày 5: Hệ Thống Chiến Đấu (phụ thuộc: Ngày 3, 4)
  ↓
Ngày 6: Hệ Thống Sóng (phụ thuộc: Ngày 5)
  ├─→ Mốc: 🎯 Core Mechanics Hoàn Thành
  ↓
Ngày 7: Pipeline Tài Sản (phụ thuộc: Ngày 6)
  ↓
Ngày 8: Tileset Bản Đồ (phụ thuộc: Ngày 7)
  ↓
Ngày 9: UI/UX (phụ thuộc: Ngày 8)
  ↓
Ngày 10-12: Nội Dung & Âm Thanh (phụ thuộc: Ngày 9)
  ├─→ Mốc: 🎯 Đồ Họa & Nội Dung Hoàn Thành
  ↓
Ngày 13-17: Cân Bằng & Hiệu Suất (phụ thuộc: Ngày 12)
  ↓
Ngày 18: Phát Hành (phụ thuộc: Ngày 17)
  └─→ Mốc: 🎯 MVP Được Phát Hành!
```

### Đường Dẫn Quan Trọng

**Các nhiệm vụ sẽ trì hoãn dự án nếu bị trì hoãn:**

1. ✅ Ngày 1: Nền Tảng
2. ⏳ Ngày 2: Hệ Thống Lưới
3. ⏳ Ngày 3: Chuyển Động Kẻ Thù
4. ⏳ Ngày 5: Hệ Thống Chiến Đấu
5. ⏳ Ngày 6: Hệ Thống Sóng
6. ⏳ Ngày 9: UI/UX (để có thể chơi được)
7. ⏳ Ngày 13: Cân Bằng (vì yếu tố vui)

### Mốc Quan Trọng

| Mốc Quan Trọng | Ngày | Tiêu Chí |
|-----------|------|----------|
| ✅ Nền Tảng Hoàn Thành | 2026-07-13 | Game loop + lớp cơ bản hoạt động |
| ⏳ Core Mechanics Hoàn Thành | 2026-07-19 | Vòng lặp gameplay đầy đủ có thể chơi (Ngày 2-6) |
| ⏳ Đồ Họa & Nội Dung Hoàn Thành | 2026-07-26 | Sprite, âm thanh, hoàn chỉnh (Ngày 7-12) |
| ⏳ **MVP Được Phát Hành** | 2026-07-31 | Cân bằng, hoàn chỉnh, đóng gói (Ngày 13-18) |

---

## Quản Lý Rủi Ro

### Các Mục Rủi Ro Cao

| Rủi Ro | Xác Suất | Tác Động | Giảm Thiểu |
|--------|----------|----------|-----------|
| Lỗi threading JavaFX | Trung Bình | Cao | Kiểm tra AnimationTimer vào Ngày 1 ✅ |
| Trễ thu thập tài sản | Trung Bình | Cao | Tải trước từ itch.io vào Ngày 7 |
| Giảm hiệu suất | Trung Bình | Trung Bình | Object pooling từ Ngày 1 ✅ |
| Tăng scope tính năng | Cao | Cao | Kỷ luật backlog nghiêm ngặt, không tính năng mới |
| Cân bằng mất quá lâu | Trung Bình | Trung Bình | Kế hoạch sớm, điều chỉnh lặp lại |
| Lỗi phút cuối | Trung Bình | Trung Bình | Kiểm tra hàng ngày, QA vào Ngày 17 |

### Chiến Lược Giảm Thiểu

**1. Ngăn Chặn Tăng Scope Tính Năng**
- Tất cả ý tưởng mới → BACKLOG.md
- Xem xét checklist hàng ngày
- Quy tắc: Không có tính năng bên ngoài phạm vi ngày hiện tại

**2. Bảo Hiểm Hiệu Suất**
- Kiến trúc ObjectPool sẵn sàng Ngày 1 ✅
- Sử dụng phân vùng không gian nếu cần
- Profile vào Ngày 14

**3. Cổng Chất Lượng**
- Tích hợp hàng ngày (Ngày 6, 12 chơi qua hoàn chỉnh)
- Tài liệu lỗi khi tìm thấy
- Dành 1 giờ mỗi ngày để sửa

**4. Đệm Khoảng Thời Gian**
- Đệm 15% cho mỗi ngày (54 phút khả dụng mỗi ngày 3 giờ)
- 2 ngày đầy đủ khả dụng cho vấn đề không mong muốn

---

## Tiêu Chí Thành Công

### Yêu Cầu Hoàn Thành Giai Đoạn

#### Giai Đoạn 1: Nền Tảng ✅
- [x] Game biên dịch và chạy
- [x] Màn hình Menu hiển thị
- [x] Game loop chạy ở 60 FPS
- [x] Tất cả lớp cơ bản được tạo
- [x] EventBus hoạt động

#### Giai Đoạn 2: Core Mechanics
- [ ] Lưới render từ bản đồ CSV
- [ ] Kẻ thù spawn và chuyển động
- [ ] Tháp có thể được đặt và bắn
- [ ] Đạn va chạm và gây sát thương
- [ ] 3 sóng có thể hoàn thành
- [ ] Điều kiện thắng/thua hoạt động
- [ ] Vòng lặp game hoàn chỉnh có thể chơi từ đầu đến cuối

#### Giai Đoạn 3: Đồ Họa & Nội Dung
- [ ] Sprite pixel art được tích hợp
- [ ] Hiệu ứng âm thanh phát
- [ ] Nhạc vòng lặp trong game
- [ ] HUD hoàn chỉnh và hoạt động
- [ ] 5 sóng có boss
- [ ] Menu trông chuyên nghiệp
- [ ] Tất cả chuyển tiếp mịn

#### Giai Đoạn 4: Cân Bằng & Hoàn Chỉnh
- [ ] Game chạy ở 60 FPS liên tục
- [ ] Tất cả 3 mức độ khó có thể chơi được
- [ ] Không có lỗi quan trọng
- [ ] Chỉ số kẻ thù/tháp cân bằng
- [ ] File JAR có thể thực thi
- [ ] Tài liệu hoàn chỉnh
- [ ] Code sạch sẽ và có ghi chú

### Danh Sách Kiểm Tra Phát Hành Cuối Cùng

- [ ] Game chạy mà không crash
- [ ] Điều kiện thắng: đánh bại tất cả 5 sóng
- [ ] Điều kiện thua: sức khỏe → 0
- [ ] Có thể khởi động lại từ menu
- [ ] Cài đặt tồn tại
- [ ] Bật tắt âm thanh hoạt động
- [ ] 3 mức độ khó
- [ ] Ít nhất 3 bản đồ khả dụng
- [ ] Sprite pixel art (không phải placeholder)
- [ ] Hiệu ứng âm thanh nghe rõ
- [ ] Nhạc nền có
- [ ] HUD hiển thị tất cả thông tin cần
- [ ] README có hướng dẫn cài đặt
- [ ] File JAR hoạt động độc lập
- [ ] Không có glitch trực quan
- [ ] Không có lỗi quan trọng đã biết

---

## Theo Dõi Nỗ Lực: Ước Tính so với Thực Tế

**Sẽ được cập nhật khi công việc tiến hành**

| Tuần | Giai Đoạn | Giờ Ước Tính | Giờ Thực Tế | Trạng Thái | %Δ |
|------|-----------|-----------|-----------|--------|-----|
| 1 | Core | 19h | ⏳ | — | — |
| 2 | Đồ Họa | 18h | ⏳ | — | — |
| 3 | Hoàn Chỉnh | 16h | ⏳ | — | — |
| **Tổng** | **MVP** | **53h** | ⏳ | — | — |

---

## Giao Tiếp & Cập Nhật

### Cập Nhật Hàng Ngày
- Sáng: Kiểm tra danh sách nhiệm vụ so với kế hoạch
- Tối: Cập nhật trạng thái nhiệm vụ (chưa bắt đầu / đang tiến hành / hoàn thành)
- Ghi lại giờ cấp độ cho mỗi nhiệm vụ

### Mốc Quan Trọng Hàng Tuần
- Kết thúc Tuần 1 (Ngày 6): Core mechanics có thể chơi được
- Kết thúc Tuần 2 (Ngày 12): Đồ họa/âm thanh hoàn thành
- Kết thúc Tuần 3 (Ngày 18): Sẵn sàng phát hành

### Cập Nhật Tài Liệu
- Giữ WEEK1_CHECKLIST.md, STATUS.md hiện tại
- Commit sau mỗi nhiệm vụ
- Cập nhật API_DOCUMENTATION.md nếu thêm lớp mới

---

## Phụ Lục: Mã Nhiệm Vụ

**Định Dạng:** `W[Tuần]D[Ngày]-[SốNhiệmVụ]`

Ví dụ: `W1D2-3` = Tuần 1, Ngày 2, Nhiệm Vụ 3

---

*Cập Nhật Lần Cuối: 2026-07-13 | Xem Xét Tiếp Theo: 2026-07-19 (Kết Thúc Tuần 1)*
