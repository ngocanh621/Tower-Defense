# Stone Guardians: Tower Defense - Advanced Programming Project

## Thành Viên

Nhóm [6] - Lớp [INT2215_81]

| Thành viên 1 | Nguyễn Ngọc Ánh | 24020034 |
| Thành viên 2 | Phạm Thị Trà My | 24020242 |
| Giảng viên hướng dẫn | Thầy Đặng Đức Hạnh<br>Thầy La Trịnh Hoàng Việt<br>Thầy Phạm Bảo Phúc<br>Thầy Nguyễn Đức Quyền | - |

- Học kỳ: HK1 - 2025-2026

---

## Giới Thiệu (Description)

Stone Guardians là một dự án game Tower Defense 2D hiện đại được phát triển hoàn toàn bằng Java và JavaFX. Đây là đồ án được thực hiện nhằm mục đích làm bài tập lớn cuối kỳ cho môn học Lập trình nâng cao.
---

## Các Tính Năng Chính (Key Features)

- Phát triển bằng Java 17+ với giao diện đồ họa JavaFX 21.
- Vận hành mượt mà với Game Loop 60 FPS, tính toán deltaTime chuẩn xác.
- Ứng dụng kiến trúc sạch: MVC, Event-Driven Architecture, và Object Pooling.
- Bản đồ độ phân giải cao (1280x720) với hệ thống lưới ô cờ (Grid) và thuật toán di chuyển theo định tuyến (Waypoints).
- Tích hợp hệ thống âm thanh, hiệu ứng hạt và giao diện HUD trực quan.
- Hỗ trợ hệ thống cấu hình linh hoạt (Game Config) qua JSON và lưu trữ Kỷ lục (Best Score) cục bộ.

---

## Cơ Chế Trò Chơi (Game Mechanics)

- Quản lý tài nguyên (Vàng) để tính toán chiến thuật mua và đặt tháp.
- Nâng cấp tháp để gia tăng sức mạnh hoặc bán tháp để thu hồi vốn.
- Chế độ chơi vô tận (Endless Mode): Không có màn thắng cố định, các đợt sinh quái (Waves) xuất hiện liên tục và tăng dần độ khó tự động theo thời gian.
- Bảo vệ Căn cứ (Base): Mỗi quái vật lọt qua lưới phòng thủ sẽ trừ đi 1 Máu (HP) của người chơi. Trò chơi kéo dài cho đến khi HP giảm về 0.

---

## Sơ Đồ & Kiến Trúc Thiết Kế (UML & Design Patterns)

### 1. Kiến Trúc Hướng Sự Kiện (Event-Driven Architecture)
- Sử dụng tại: EventBus, Quản lý sát thương, Cập nhật UI.
- Mục đích: Giúp các module (GameScene, WaveManager, HUD) giao tiếp với nhau mà không bị phụ thuộc trực tiếp (Decoupling). Ví dụ: Khi quái chết, hệ thống sẽ phát tín hiệu cộng tiền, phát âm thanh mà không cần gọi hàm chéo nhau.

### 2. Object Pool Pattern (Tối ưu Bộ nhớ)
- Sử dụng tại: Projectile (Đạn), Enemy (Quái vật).
- Mục đích: Tái sử dụng liên tục các thực thể xuất hiện nhiều trong game thay vì khởi tạo (new) và xóa chúng liên tục, giúp giảm tải tối đa cho bộ thu gom rác (Garbage Collector), đảm bảo game không bị giật lag.

### 3. Mô Hình MVC (Model-View-Controller)
- Mục đích: Tách biệt rõ ràng phần xử lý dữ liệu (MapModel, Entity), phần hiển thị giao diện (GameScene, HudRenderer) và phần điều khiển logic (WaveManager, PlayerState).

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
| Left Click vào ô đất trống | Mở Menu Chọn Tháp (Mua tháp mới). |
| Left Click vào tháp đã xây | Mở Menu Nâng cấp / Bán tháp hiện tại. |

### Cách chơi (How to play)

- Khởi động: Chọn "Start Game" từ màn hình chính.
- Xây dựng: Tìm các bãi đất vàng chiến thuật có viền xanh lá, click chuột để mua tháp.
- Phòng thủ: Tháp sẽ tự động dò tìm mục tiêu và xả đạn. Tiêu diệt quái vật để kiếm thêm Vàng (Gold).
- Kết thúc trò chơi (Game Over): Trò chơi kết thúc khi HP của người chơi giảm về 0 do quái vật lọt qua tuyến phòng thủ. Mục tiêu là sinh tồn qua nhiều đợt quái (Waves) nhất có thể để đạt số điểm kỷ lục.

---

## Hệ Thống Tháp Phòng Thủ (Towers)

| Tên Tháp | Mức Giá | Hiệu Ứng |
| --- | --- | --- |
| Gun Tower | 100G | Tháp súng máy: Tốc độ bắn cực nhanh, lượng sát thương ổn định. Rất tốt để dọn dẹp các mục tiêu lẻ di chuyển nhanh. |
| Slow Tower | 150G | Tháp băng: Tốc độ bắn chậm nhưng có tầm quét rộng. Khi đánh trúng sẽ làm chậm tốc độ di chuyển của quái vật trong một khoảng thời gian. |

---

## Kẻ Thù (Enemies)

| Tên Quái | Đặc Điểm |
| --- | --- |
| Goblin | Đội quân nhí nhố với tốc độ di chuyển cực nhanh nhưng lượng máu thấp. Rất dễ bị tiêu diệt bởi Gun Tower. |
| Orc | Tộc Orc da xanh với thân hình đồ sộ. Di chuyển chậm chạp nhưng có lượng Máu (HP) khổng lồ, cần nhiều hỏa lực để hạ gục. |
| Dragon | Sinh vật thống trị bầu trời (Boss) sở hữu tốc độ di chuyển và lượng máu (HP) vượt trội. |

---

## Hình Ảnh Demo (Screenshots)

- Màn Hình Chính (Main Menu)
- Bố Trí Phòng Thủ (Gameplay)
- Menu Mua / Bán Tháp
- Hệ thống Wave & Kẻ thù

*(Bạn có thể chèn các link ảnh thực tế của dự án trên GitHub vào đây)*

---

## Hướng Phát Triển Tương Lai (Future Improvements)

- Mở rộng Gameplay:
  - Thêm chế độ Endless Mode (Sinh tồn vô tận).
  - Tích hợp thêm nhiều loại Tháp (Tháp Lửa, Tháp Pháo) và các bản đồ mới.
- Nâng cấp Hệ thống:
  - Hoàn thiện thuật toán tìm đường (A* Pathfinding) thay vì dùng Waypoint cố định.
  - Lưu trữ tiến trình chơi (Save/Load game state) lên Database.

---

## Công Nghệ Sử Dụng (Technologies Used)

| Công Nghệ | Phiên Bản | Vai Trò |
| --- | --- | --- |
| Java | 17+ | Ngôn ngữ lập trình cốt lõi |
| JavaFX | 21 | Framework đồ họa & UI |
| Maven | 3.8+ | Quản lý Package & Build project |
| Gson | 2.10.1 | Phân tích cú pháp (Parse) Config JSON |

---

Cập nhật lần cuối: 12/08/2026
