# Stone Guardians: Tower Defense - Advanced Programming Project

## Thành Viên & Giảng Viên Hướng Dẫn

- **Nhóm:** 6 | **Lớp:** INT2215_81
- **Học kỳ:** Học kỳ phụ (2025-2026)

### Thành viên:
1. Nguyễn Ngọc Ánh - MSSV: 24020034
2. Phạm Thị Trà My - MSSV: 24020242

### Giảng viên hướng dẫn:
- Thầy Đặng Đức Hạnh
- Thầy La Trịnh Hoàng Việt
- Thầy Phạm Bảo Phúc
- Thầy Nguyễn Đức Quyền

---

## Giới Thiệu (Description)

Stone Guardians là một dự án game Tower Defense 2D hiện đại được phát triển hoàn toàn bằng Java và JavaFX. Đây là đồ án được thực hiện nhằm mục đích làm bài tập lớn cuối kỳ cho môn học Lập trình nâng cao.

---

## Các Tính Năng Chính (Key Features)

- Phát triển bằng Java 17 với giao diện đồ họa JavaFX 21.
- Vận hành mượt mà với Game Loop 60 FPS, tính toán deltaTime chuẩn xác.
- Ứng dụng kiến trúc sạch: MVC, Event-Driven Architecture, và Object Pooling.
- Bản đồ độ phân giải cao (1280x720) với hệ thống lưới ô cờ (Grid 40x40px) và di chuyển theo mốc định tuyến (Waypoints).
- Tích hợp hệ thống âm thanh, hiệu ứng cháy nổ và giao diện HUD trực quan.
- Khởi tạo tài nguyên người chơi: 20 Máu (HP) và 100 Vàng (Gold) ban đầu.
- Lưu trữ Kỷ lục (Best Score) cục bộ tại tệp highscore.txt.

---

## Cơ Chế Trò Chơi (Game Mechanics)

- Quản lý tài nguyên (Vàng) để tính toán chiến thuật mua và đặt tháp.
- Nâng cấp tháp (tối đa cấp 3) để gia tăng sức mạnh hoặc bán tháp để thu hồi lại vàng.
- Chế độ chơi vô tận (Endless Mode): Không có màn thắng cố định, các đợt sinh quái (Waves) xuất hiện liên tục và tăng dần độ khó tự động theo thời gian.
- Bảo vệ Căn cứ (Base): Mỗi quái vật lọt qua lưới phòng thủ sẽ trừ Máu (HP) của người chơi tùy thuộc loại quái. Trò chơi kết thúc (Game Over) khi HP giảm về 0.

---

## Sơ Đồ & Kiến Trúc Thiết Kế (UML & Design Patterns)

### 1. Kiến Trúc Hướng Sự Kiện (Event-Driven Architecture)
- Sử dụng tại: Class EventBus, quản lý sự kiện WAVE_STARTED, ENEMY_DIED, ENEMY_REACHED_BASE, WAVE_COMPLETED.
- Mục đích: Giúp các module (GameScene, WaveManager, HudRenderer) giao tiếp với nhau mà không bị phụ thuộc trực tiếp (Decoupling). Khi quái chết, hệ thống tự phát sự kiện cộng vàng, cộng điểm và tạo hiệu ứng nổ.

### 2. Object Pool Pattern (Tối ưu Bộ nhớ)
- Sử dụng tại: Projectile (Đạn) và Enemy (Quái vật) cài đặt giao diện Poolable.
- Mục đích: Tái sử dụng liên tục các thực thể xuất hiện nhiều trong game thay vì khởi tạo (new) và xóa chúng liên tục, giúp giảm tải tối đa cho bộ thu gom rác (Garbage Collector), đảm bảo game duy trì 60 FPS không bị giật lag.

### 3. Mô Hình MVC (Model-View-Controller)
- Model: Tách biệt xử lý dữ liệu (MapModel, Entity, Tower, Enemy, Projectile).
- View: Quản lý hiển thị đồ họa trực tiếp trên Canvas và HUD (GameScene, HudRenderer).
- Controller: Điều khiển trạng thái người chơi và logic đợt quái (WaveManager, PlayerState).

---

## Hướng Dẫn Cài Đặt (Installation)

1. Clone dự án từ kho lưu trữ (Repository):
   ```
   git clone https://github.com/ngocanh621/Tower-Defense.git
   cd Tower-Defense
   ```

2. Mở thư mục dự án bằng IDE (VS Code, IntelliJ IDEA, Eclipse).

3. Biên dịch dự án bằng Maven:
   ```
   mvn clean compile
   ```
   Hoặc trên Windows dùng Maven Wrapper:
   ```
   .\mvnw.cmd clean compile
   ```

4. Khởi chạy game:
   ```
   mvn javafx:run
   ```
   Hoặc trên Windows:
   ```
   .\mvnw.cmd javafx:run
   ```

---

## Hướng Dẫn Chơi (Usage)

### Thao tác điều khiển (Controls)

| Thao tác chuột | Hành động |
| --- | --- |
| Hover (Di chuột) | Hiển thị viền tô sáng báo hiệu ô đất (Xanh: Hợp lệ / Đỏ: Không thể đặt). |
| Left Click vào ô đất trống | Mở Menu Chọn Tháp (Mua Gun Tower hoặc Slow Tower). |
| Left Click vào tháp đã xây | Mở Menu Nâng cấp / Bán tháp hiện tại. |

### Cách chơi (How to play)

- Khởi động: Chọn "Start Game" từ màn hình chính.
- Xây dựng: Tìm các bãi đất chiến thuật có viền xanh lá, click chuột để mua tháp.
- Phòng thủ: Tháp sẽ tự động dò tìm mục tiêu và xả đạn trong tầm bắn. Tiêu diệt quái vật để kiếm thêm Vàng (Gold) và Điểm (Score).
- Kết thúc trò chơi (Game Over): Trò chơi kết thúc khi HP của người chơi giảm về 0. Mục tiêu là sinh tồn qua nhiều đợt quái (Waves) nhất có thể để đạt điểm số kỷ lục.

---

## Hệ Thống Tháp Phòng Thủ (Towers)

| Tên Tháp | Mức Giá | Tầm Bắn | Tốc Độ Bắn | Hiệu Ứng |
| --- | --- | --- | --- | --- |
| Gun Tower | 15G | 150px | 1.0 phát/giây | Tháp súng máy: Tốc độ bắn nhanh, giá thành rẻ. Rất hiệu quả ở giai đoạn đầu game. |
| Slow Tower | 25G | 200px | 0.5 phát/giây | Tháp băng: Tầm quét rộng, tự động làm chậm tốc độ di chuyển của quái vật bị trúng đạn. |

---

## Kẻ Thù (Enemies)

| Tên Quái | Máu (HP) | Tốc Độ | Phần Thưởng | Đặc Điểm |
| --- | --- | --- | --- | --- |
| Goblin | 50 HP | 120 px/s | 10 Gold | Tốc độ di chuyển cực nhanh nhưng máu thấp. |
| Orc | 200 HP | 60 px/s | 25 Gold | Tốc độ di chuyển chậm nhưng có lượng máu khổng lồ. |
| Dragon | 500 HP | 40 px/s | 60 Gold | Sinh vật Boss có lượng máu và kích thước lớn nhất. |

---

## Hình Ảnh Demo (Screenshots)

- Màn Hình Chính (Main Menu)
- Bố Trí Phòng Thủ (Gameplay)
- Menu Mua / Bán Tháp
- Hệ thống Wave & Kẻ thù

---

## Hướng Phát Triển Tương Lai (Future Improvements)

- Mở rộng Gameplay:
  - Tích hợp thêm nhiều loại Tháp (Tháp Lửa, Tháp Pháo) và các bản đồ mới.
  - Thêm hiệu ứng âm thanh và hoạt ảnh kỹ năng đặc biệt.
- Nâng cấp Hệ thống:
  - Hoàn thiện thuật toán tìm đường (A* Pathfinding) thay vì dùng Waypoint cố định.
  - Lưu trữ tiến trình chơi (Save/Load game state).

---

## Công Nghệ Sử Dụng (Technologies Used)

| Công Nghệ | Phiên Bản | Vai Trò |
| --- | --- | --- |
| Java | 17 | Ngôn ngữ lập trình cốt lõi |
| JavaFX | 21.0.2 | Framework đồ họa & UI |
| Maven | 3.8+ | Quản lý Package & Build project |
| Gson | 2.10.1 | Phân tích dữ liệu JSON |
| JUnit | 5.9.2 | Kiểm thử đơn vị (Unit Testing) |

---

Cập nhật lần cuối: 12/08/2026
