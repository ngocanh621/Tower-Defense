# 📚 TÀI LIỆU API - Tower Defense 2D MVP

**Phiên bản:** 1.0.0  
**Cập nhật lần cuối:** 2026-07-13  
**Tác giả:** Nhóm Phát Triển Tower Defense

---

## Mục Lục

1. [Gói Core (`com.game.core`)](#gói-core)
2. [Gói Model (`com.game.model`)](#gói-model)
3. [Gói System (`com.game.system`)](#gói-system)
4. [Gói Map (`com.game.map`)](#gói-map)
5. [Gói Controller (`com.game.controller`)](#gói-controller)
6. [Gói Utility (`com.game.util`)](#gói-utility)

---

## Gói Core

### `GameApplication` (Điểm Vào Chính)

**Vị trí:** `src/main/java/com/game/core/GameApplication.java`

**Vai trò:** Điểm vào chính của ứng dụng JavaFX, khởi tạo cửa sổ chính, thiết lập trình quản lý cảnh, và khởi động vòng lặp game.

#### Methods (Phương Thức)

##### `start(Stage primaryStage) : void`

**Mục đích:** Phương thức vòng đời JavaFX được gọi khi ứng dụng khởi động.

**Tham số:**
- `primaryStage: Stage` - Cửa sổ chính của ứng dụng

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Đặt tiêu đề cửa sổ thành "Tower Defense 2D MVP"
- Tạo cửa sổ với kích thước 1280×720
- Khởi tạo `SceneManager`
- Tạo cảnh menu ban đầu
- Khởi động `GameLoop`
- Thiết lập bộ xử lý đóng cửa sổ

**Ném Exception:** Exception (từ chữ ký start của JavaFX)

**Ví dụ:**
```java
// Được gọi tự động bởi JavaFX
// Thực thi từ main()
```

---

##### `main(String[] args) : void`

**Mục đích:** Điểm vào chương trình, khởi chạy ứng dụng JavaFX.

**Tham số:**
- `args: String[]` - Các tham số dòng lệnh (không sử dụng)

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Gọi `launch(args)` kích hoạt `start(Stage)`

**Ví dụ:**
```bash
java com.game.core.GameApplication
```

---

### `GameLoop` (Vòng Lặp Cập Nhật/Render 60 FPS)

**Vị trí:** `src/main/java/com/game/core/GameLoop.java`

**Vai trò:** Vòng lặp game chính chạy ở ~60 FPS sử dụng `AnimationTimer` của JavaFX. Quản lý tính toán thời gian delta và ủy quyền cập nhật/render cho trình quản lý cảnh.

#### Hằng số

```java
TARGET_FPS = 60.0              // Khung hình mục tiêu mỗi giây
FRAME_TIME = 1/60.0 ≈ 0.01667 // Thời gian khung hình dự kiến (giây)
```

#### Methods (Phương Thức)

##### `GameLoop(SceneManager sceneManager) : constructor`

**Mục đích:** Khởi tạo vòng lặp game với tham chiếu trình quản lý cảnh.

**Tham số:**
- `sceneManager: SceneManager` - Trình quản lý cảnh để ủy quyền cập nhật

**Tác dụng phụ:**
- Lưu trữ tham chiếu trình quản lý cảnh
- Tạo instance `AnimationTimer`
- Đặt `lastFrameTime` thành 0

**Ví dụ:**
```java
SceneManager sceneManager = new SceneManager(primaryStage);
GameLoop gameLoop = new GameLoop(sceneManager);
gameLoop.start();
```

---

##### `start() : void`

**Mục đích:** Khởi động vòng lặp game (bắt đầu gọi cập nhật/render mỗi khung).

**Tham số:** Không

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Khởi động `animationTimer`
- In "GameLoop started" ra console

**An toàn luồng:** Phải được gọi từ luồng ứng dụng FX

**Ví dụ:**
```java
gameLoop.start();
```

---

##### `stop() : void`

**Mục đích:** Dừng vòng lặp game (tạm dừng chu kỳ cập nhật/render).

**Tham số:** Không

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Dừng `animationTimer`
- In "GameLoop stopped" ra console

**Ví dụ:**
```java
gameLoop.stop();
```

---

##### `update(double deltaTime) : void` (Riêng tư)

**Mục đích:** Cập nhật logic game cho khung hiện tại bằng cách ủy quyền cho cảnh hiện tại.

**Tham số:**
- `deltaTime: double` - Thời gian trôi qua từ khung trước (giây)

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Gọi `currentGame.update(deltaTime)` nếu cảnh game tồn tại

**Ghi chú:** Phương thức trợ giúp riêng tư, không dùng bên ngoài

---

##### `render() : void` (Riêng tư)

**Mục đích:** Render khung hiện tại bằng cách ủy quyền cho cảnh hiện tại.

**Tham số:** Không

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Gọi `currentGame.render()` nếu cảnh game tồn tại

**Ghi chú:** Phương thức trợ giúp riêng tư, không dùng bên ngoài

---

### `SceneManager` (Trình Quản Lý Chuyển Đổi Cảnh)

**Vị trí:** `src/main/java/com/game/core/SceneManager.java`

**Vai trò:** Quản lý chuyển đổi giữa các cảnh game khác nhau (Menu, Game, GameOver). Cung cấp phương thức tạo cảnh và xử lý chuyển đổi cảnh.

#### Methods (Phương Thức)

##### `SceneManager(Stage primaryStage) : constructor`

**Mục đích:** Khởi tạo trình quản lý cảnh với tham chiếu cửa sổ chính.

**Tham số:**
- `primaryStage: Stage` - Cửa sổ để quản lý cảnh

**Tác dụng phụ:**
- Lưu trữ tham chiếu cửa sổ
- Đặt `currentGame` thành null

**Ví dụ:**
```java
SceneManager sceneManager = new SceneManager(primaryStage);
```

---

##### `createMenuScene() : Scene`

**Mục đích:** Tạo và trả về cảnh menu chính với giao diện cơ bản và bộ xử lý input.

**Tham số:** Không

**Giá trị trả về:** `Scene` - Cảnh menu được tạo

**Tác dụng phụ:**
- Tạo BorderPane mới với canvas
- Vẽ giao diện menu (tiêu đề, hướng dẫn)
- Đăng ký bộ xử lý nhấn phím (SPACE để bắt đầu, ESC để thoát)
- Trả về Scene mới

**Bộ xử lý phím:**
- `SPACE` - Gọi `switchToGameScene()`
- `ESC` - Thoát ứng dụng

**Ví dụ:**
```java
Scene menuScene = sceneManager.createMenuScene();
primaryStage.setScene(menuScene);
```

---

##### `createGameScene() : Scene`

**Mục đích:** Tạo và trả về cảnh game chính với canvas rendering và bộ xử lý input.

**Tham số:** Không

**Giá trị trả về:** `Scene` - Cảnh game được tạo

**Tác dụng phụ:**
- Tạo instance `GameScene` mới
- Đặt `currentGame` thành cảnh game mới
- Đăng ký bộ xử lý input (bàn phím, chuột)
- Trả về Scene mới

**Bộ xử lý Input:**
- `ESC` key - Quay lại menu
- `Mouse click` - Ủy quyền cho `GameScene.handleMouseClick()`
- `Mouse move` - Ủy quyền cho `GameScene.handleMouseMove()`
- Phím khác - Ủy quyền cho `GameScene.handleKeyPress()`

**Ví dụ:**
```java
Scene gameScene = sceneManager.createGameScene();
primaryStage.setScene(gameScene);
```

---

##### `switchToMenuScene() : void`

**Mục đích:** Chuyển ứng dụng sang cảnh menu chính.

**Tham số:** Không

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Gọi `createMenuScene()` để lấy cảnh menu mới
- Đặt cảnh primaryStage thành cảnh menu
- Đặt `currentGame` thành null

**Ví dụ:**
```java
sceneManager.switchToMenuScene();
```

---

##### `switchToGameScene() : void`

**Mục đích:** Chuyển ứng dụng sang cảnh game.

**Tham số:** Không

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Gọi `createGameScene()` để lấy cảnh game mới
- Đặt cảnh primaryStage thành cảnh game
- Đặt `currentGame` thành cảnh game mới

**Ví dụ:**
```java
sceneManager.switchToGameScene();
```

---

##### `getCurrentGame() : GameScene`

**Mục đích:** Lấy tham chiếu cảnh game hiện tại đang hoạt động.

**Tham số:** Không

**Giá trị trả về:** `GameScene` - Cảnh game hiện tại, hoặc null nếu ở menu

**Sử dụng:** GameLoop sử dụng để lấy cảnh để cập nhật/render

**Ví dụ:**
```java
GameScene current = sceneManager.getCurrentGame();
if (current != null) {
    current.update(deltaTime);
}
```

---

### `GameScene` (Trạng Thái Game & Rendering)

**Vị trí:** `src/main/java/com/game/core/GameScene.java`

**Vai trò:** Đại diện cho một phiên chơi game duy nhất. Quản lý logic cập nhật và rendering cho bảng game, các thực thể và giao diện người dùng.

#### Methods (Phương Thức)

##### `GameScene(Canvas canvas, GraphicsContext gc) : constructor`

**Mục đích:** Khởi tạo cảnh game với canvas rendering và graphics context.

**Tham số:**
- `canvas: Canvas` - Canvas JavaFX để render
- `gc: GraphicsContext` - Graphics context để vẽ

**Tác dụng phụ:**
- Lưu trữ tham chiếu canvas và graphics context

**Ví dụ:**
```java
Canvas gameCanvas = new Canvas(1280, 720);
GraphicsContext gc = gameCanvas.getGraphicsContext2D();
GameScene scene = new GameScene(gameCanvas, gc);
```

---

##### `update(double deltaTime) : void`

**Mục đích:** Cập nhật tất cả logic game cho khung hiện tại.

**Tham số:**
- `deltaTime: double` - Thời gian trôi qua từ khung trước (giây)

**Giá trị trả về:** `void`

**Trách nhiệm (Sẽ triển khai):**
- Cập nhật trạng thái lưới
- Cập nhật kẻ thù (chuyển động, sát thương)
- Cập nhật tháp (nhắm mục tiêu, bắn)
- Cập nhật đạn (chuyển động, va chạm)
- Kiểm tra điều kiện thắng/thua
- Xử lý phát hành sự kiện

**Ví dụ:**
```java
scene.update(0.01667); // Gọi mỗi khung
```

---

##### `render() : void`

**Mục đích:** Render khung game hoàn chỉnh lên canvas.

**Tham số:** Không

**Giá trị trả về:** `void`

**Trách nhiệm:**
- Xóa canvas
- Vẽ các ô lưới
- Vẽ tháp
- Vẽ kẻ thù
- Vẽ đạn
- Vẽ HUD (máu khỏe, vàng, thông tin sóng)

**Tác dụng phụ:**
- Sửa đổi nội dung canvas thông qua graphics context

**Ghi chú Hiệu suất:** Được gọi ~60 lần mỗi giây

**Ví dụ:**
```java
scene.render(); // Gọi mỗi khung sau cập nhật
```

---

##### `handleKeyPress(KeyEvent event) : void`

**Mục đích:** Xử lý input bàn phím trong lúc chơi game.

**Tham số:**
- `event: KeyEvent` - Sự kiện nhấn phím

**Giá trị trả về:** `void`

**Sẽ triển khai:**
- Lựa chọn tháp (phím số 1-9?)
- Xoay/xem trước đặt tháp
- Tăng tốc/giảm tốc game
- Bật tắt tạm dừng

**Ví dụ:**
```java
// Được kích hoạt khi người dùng nhấn phím trong cảnh game
scene.handleKeyPress(event);
```

---

##### `handleMouseClick(MouseEvent event) : void`

**Mục đích:** Xử lý sự kiện nhấp chuột trong lúc chơi game.

**Tham số:**
- `event: MouseEvent` - Sự kiện nhấp chuột

**Giá trị trả về:** `void`

**Sẽ triển khai:**
- Đặt tháp tại vị trí nhấp
- Lựa chọn/bỏ lựa chọn tháp
- Nâng cấp/bán tháp

**Ví dụ:**
```java
// Được kích hoạt khi người dùng nhấp trong cảnh game
scene.handleMouseClick(event);
```

---

##### `handleMouseMove(MouseEvent event) : void`

**Mục đích:** Xử lý sự kiện di chuyển chuột trong lúc chơi game.

**Tham số:**
- `event: MouseEvent` - Sự kiện di chuyển chuột

**Giá trị trả về:** `void`

**Sẽ triển khai:**
- Xem trước đặt tháp (tháp ma theo con trỏ)
- Làm nổi bật ô
- Hiển thị xem trước tầm của tháp

**Ví dụ:**
```java
// Được kích hoạt khi người dùng di chuyển chuột trong cảnh game
scene.handleMouseMove(event);
```

---

##### `drawPlaceholderGrid() : void` (Riêng tư)

**Mục đích:** Vẽ lưới placeholder cho kiểm tra phát triển.

**Tham số:** Không

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Vẽ đường lưới trên canvas
- Vẽ lớp phủ văn bản giao diện

**Ghi chú:** Phương thức tạm thời cho phát triển; sẽ được thay thế bằng rendering lưới thực

---

## Gói Model

### `Poolable` (Interface)

**Vị trí:** `src/main/java/com/game/model/Poolable.java`

**Vai trò:** Interface cho các đối tượng có thể được tái sử dụng thông qua ObjectPool. Cho phép tái sử dụng bộ nhớ hiệu quả.

#### Methods (Phương Thức)

##### `reset() : void`

**Mục đích:** Đặt lại đối tượng về trạng thái ban đầu/rỗng để tái sử dụng.

**Tham số:** Không

**Giá trị trả về:** `void`

**Người triển khai phải:** Xóa tất cả trạng thái đối tượng (vị trí, mục tiêu, sát thương, v.v.)

**Ví dụ:**
```java
class Projectile implements Poolable {
    public void reset() {
        this.x = 0;
        this.y = 0;
        this.target = null;
        this.active = false;
    }
}
```

---

##### `isActive() : boolean`

**Mục đích:** Kiểm tra xem đối tượng này có đang được sử dụng hay không.

**Tham số:** Không

**Giá trị trả về:** `boolean` - true nếu hoạt động, false nếu trong pool

**Ví dụ:**
```java
if (projectile.isActive()) {
    projectile.update(deltaTime);
}
```

---

##### `setActive(boolean active) : void`

**Mục đích:** Đặt trạng thái hoạt động của đối tượng này.

**Tham số:**
- `active: boolean` - true để đánh dấu hoạt động, false để đánh dấu có sẵn cho pool

**Giá trị trả về:** `void`

**Ví dụ:**
```java
projectile.setActive(true);  // Đánh dấu đang sử dụng
projectile.setActive(false); // Đánh dấu có sẵn cho pool
```

---

### `Entity` (Lớp Cơ Sở Trừu Tượng)

**Vị trí:** `src/main/java/com/game/model/Entity.java`

**Vai trò:** Lớp cơ sở cho tất cả các đối tượng game với vị trí, kích thước và vòng đời cơ bản. Cha của Tháp, Kẻ thù và Đạn.

#### Fields (Trường)

```java
protected float x           // Vị trí X (pixel)
protected float y           // Vị trí Y (pixel)
protected float width       // Chiều rộng (pixel)
protected float height      // Chiều cao (pixel)
protected boolean active    // Liệu đối tượng này có hoạt động hay không
```

#### Methods (Phương Thức)

##### `Entity(float x, float y, float width, float height) : constructor`

**Mục đích:** Khởi tạo thực thể với vị trí và kích thước.

**Tham số:**
- `x: float` - Vị trí X tính bằng pixel
- `y: float` - Vị trí Y tính bằng pixel
- `width: float` - Chiều rộng tính bằng pixel
- `height: float` - Chiều cao tính bằng pixel

**Tác dụng phụ:**
- Đặt vị trí và kích thước
- Đặt `active` thành true

**Ví dụ:**
```java
Enemy goblin = new Goblin(100, 50, 32, 32);
```

---

##### `update(double deltaTime) : void` (Trừu tượng)

**Mục đích:** Cập nhật logic thực thể cho khung hiện tại.

**Tham số:**
- `deltaTime: double` - Thời gian trôi qua từ khung trước (giây)

**Giá trị trả về:** `void`

**Hợp đồng:** Mỗi thực thể phải triển khai phương thức này

**Ví dụ:**
```java
// Được triển khai bởi Enemy, Tower, Projectile, v.v.
enemy.update(deltaTime);
```

---

##### `isActive() : boolean`

**Mục đích:** Kiểm tra xem thực thể có hoạt động hay không.

**Tham số:** Không

**Giá trị trả về:** `boolean` - Trạng thái hoạt động

**Ví dụ:**
```java
if (entity.isActive()) {
    entity.update(deltaTime);
}
```

---

##### `setActive(boolean active) : void`

**Mục đích:** Đặt trạng thái hoạt động của thực thể.

**Tham số:**
- `active: boolean` - Trạng thái hoạt động mới

**Giá trị trả về:** `void`

**Ví dụ:**
```java
entity.setActive(false); // Hủy kích hoạt
```

---

##### `distanceTo(Entity other) : float`

**Mục đích:** Tính khoảng cách đến một thực thể khác.

**Tham số:**
- `other: Entity` - Thực thể khác

**Giá trị trả về:** `float` - Khoảng cách tính bằng pixel

**Công thức:** `sqrt((x1-x2)² + (y1-y2)²)`

**Ví dụ:**
```java
float distance = tower.distanceTo(enemy);
if (distance < tower.getRange()) {
    tower.fire(enemy);
}
```

---

##### `overlaps(Entity other) : boolean`

**Mục đích:** Kiểm tra xem hộp giới hạn của thực thể này có chồng lắp với hộp khác hay không.

**Tham số:**
- `other: Entity` - Thực thể khác

**Giá trị trả về:** `boolean` - true nếu hộp giới hạn chồng lắp

**Ví dụ:**
```java
if (projectile.overlaps(enemy)) {
    projectile.hit(enemy);
}
```

---

##### Các Getter và Setter

```java
getX() : float              // Lấy vị trí X
setX(float x) : void        // Đặt vị trí X
getY() : float              // Lấy vị trí Y
setY(float y) : void        // Đặt vị trí Y
getWidth() : float          // Lấy chiều rộng
setWidth(float width) : void
getHeight() : float         // Lấy chiều cao
setHeight(float height) : void
```

---

## Gói System

### `GameEvent` (Enum)

**Vị trí:** `src/main/java/com/game/system/GameEvent.java`

**Vai trò:** Enumeration của tất cả các sự kiện game có thể cho phát hành/đăng ký event bus.

#### Events (Sự Kiện)

```java
ENEMY_DIED              // Phát hành khi máu kẻ thù = 0
ENEMY_REACHED_BASE      // Phát hành khi kẻ thù đến căn cứ người chơi
WAVE_STARTED            // Phát hành ở đầu sóng mới
WAVE_COMPLETED          // Phát hành khi tất cả kẻ thù trong sóng bị tiêu diệt
TOWER_PLACED            // Phát hành khi tháp được đặt
TOWER_REMOVED           // Phát hành khi tháp bị xóa
PLAYER_GOLD_CHANGED     // Phát hành khi số vàng của người chơi thay đổi
PLAYER_HP_CHANGED       // Phát hành khi sức khỏe của người chơi thay đổi
GAME_OVER               // Phát hành khi người chơi thua
GAME_WON                // Phát hành khi người chơi thắng
```

---

### `EventBus` (Singleton)

**Vị trí:** `src/main/java/com/game/system/EventBus.java`

**Vai trò:** Hệ thống phân phối sự kiện trung tâm. Cho phép ghép nới lỏng bằng cách cho phép các hệ thống giao tiếp thông qua sự kiện thay vì tham chiếu trực tiếp.

#### Methods (Phương Thức)

##### `getInstance() : EventBus` (Tĩnh)

**Mục đích:** Lấy instance singleton của EventBus.

**Tham số:** Không

**Giá trị trả về:** `EventBus` - Instance singleton

**An toàn luồng:** An toàn cho truy cập đồng thời (đảm bảo tải lớp)

**Ví dụ:**
```java
EventBus bus = EventBus.getInstance();
```

---

##### `subscribe(GameEvent event, Consumer<Object> listener) : void`

**Mục đích:** Đăng ký cho một sự kiện game.

**Tham số:**
- `event: GameEvent` - Sự kiện để đăng ký
- `listener: Consumer<Object>` - Hàm gọi lại khi sự kiện được phát hành

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Thêm listener vào danh sách người đăng ký của sự kiện

**Ví dụ:**
```java
EventBus.getInstance().subscribe(GameEvent.ENEMY_DIED, e -> {
    Enemy enemy = (Enemy) e;
    playerState.addGold(enemy.getReward());
});
```

---

##### `unsubscribe(GameEvent event, Consumer<Object> listener) : void`

**Mục đích:** Hủy đăng ký khỏi một sự kiện game.

**Tham số:**
- `event: GameEvent` - Sự kiện để hủy đăng ký
- `listener: Consumer<Object>` - Listener để xóa

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Xóa listener khỏi danh sách người đăng ký của sự kiện

**Ví dụ:**
```java
EventBus.getInstance().unsubscribe(GameEvent.WAVE_STARTED, myListener);
```

---

##### `publish(GameEvent event, Object data) : void`

**Mục đích:** Phát hành sự kiện cho tất cả người đăng ký với tải trọng dữ liệu.

**Tham số:**
- `event: GameEvent` - Loại sự kiện
- `data: Object` - Dữ liệu sự kiện (có thể null)

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Gọi tất cả listener đã đăng ký cho sự kiện này
- Bao bọc các ngoại lệ từ listener để một listener thất bại không dừng những listener khác

**Xử lý lỗi:** Bắt và ghi nhật ký các ngoại lệ từ các listener riêng lẻ

**Ví dụ:**
```java
EventBus.getInstance().publish(GameEvent.ENEMY_DIED, deadEnemy);
```

---

##### `publish(GameEvent event) : void`

**Mục đích:** Phát hành sự kiện cho tất cả người đăng ký mà không có dữ liệu.

**Tham số:**
- `event: GameEvent` - Loại sự kiện

**Giá trị trả về:** `void`

**Phương thức tiện lợi:** Gọi `publish(event, null)`

**Ví dụ:**
```java
EventBus.getInstance().publish(GameEvent.WAVE_COMPLETED);
```

---

##### `clear() : void`

**Mục đích:** Xóa tất cả listener sự kiện (hữu ích cho kiểm tra/dọn dẹp).

**Tham số:** Không

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Xóa tất cả người đăng ký khỏi tất cả sự kiện

**Ví dụ:**
```java
EventBus.getInstance().clear(); // Để kiểm tra
```

---

##### `clear(GameEvent event) : void`

**Mục đích:** Xóa listener cho sự kiện cụ thể.

**Tham số:**
- `event: GameEvent` - Sự kiện để xóa

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Xóa tất cả người đăng ký khỏi sự kiện được chỉ định

**Ví dụ:**
```java
EventBus.getInstance().clear(GameEvent.WAVE_STARTED);
```

---

### `ObjectPool<T>` (Chung Chung)

**Vị trí:** `src/main/java/com/game/system/ObjectPool.java`

**Vai trò:** Pool đối tượng chung cho tái sử dụng các instance và giảm áp lực thu gom rác. Lý tưởng cho đạn, hạt, và các đối tượng khác được tạo/hủy thường xuyên.

#### Tham số Kiểu

```java
<T extends Poolable>  // Kiểu chung phải triển khai interface Poolable
```

#### Methods (Phương Thức)

##### `ObjectPool(Supplier<T> factory, int initialSize) : constructor`

**Mục đích:** Tạo pool đối tượng mới với các instance được cấp trước.

**Tham số:**
- `factory: Supplier<T>` - Hàm factory để tạo các instance mới
- `initialSize: int` - Số lượng đối tượng để cấp trước

**Tác dụng phụ:**
- Tạo pool ban đầu các đối tượng
- Lưu trữ tham chiếu factory để tạo các đối tượng bổ sung khi cần

**Ví dụ:**
```java
ObjectPool<Projectile> projectilePool = new ObjectPool<>(
    () -> new Projectile(),  // Factory
    100                       // Cấp trước 100 đạn
);
```

---

##### `acquire() : T`

**Mục đích:** Lấy một đối tượng từ pool.

**Tham số:** Không

**Giá trị trả về:** `T` - Một đối tượng từ pool (hoặc vừa tạo nếu pool trống)

**Tác dụng phụ:**
- Đánh dấu đối tượng là hoạt động
- Thêm vào tập hợp inUse
- Tạo đối tượng mới nếu pool trống (mở rộng tự động)

**Ví dụ:**
```java
Projectile p = projectilePool.acquire();
p.initialize(tower, target);
```

---

##### `release(T obj) : void`

**Mục đích:** Trả lại một đối tượng cho pool để tái sử dụng.

**Tham số:**
- `obj: T` - Đối tượng để trả lại

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Gọi `obj.reset()` để xóa trạng thái
- Đánh dấu đối tượng là không hoạt động
- Xóa khỏi tập hợp inUse
- Thêm vào hàng đợi có sẵn

**Ví dụ:**
```java
projectilePool.release(projectile);
```

---

##### `getAvailableCount() : int`

**Mục đích:** Lấy số lượng đối tượng có sẵn trong pool.

**Tham số:** Không

**Giá trị trả về:** `int` - Số lượng đối tượng chưa sử dụng

**Ví dụ:**
```java
int available = projectilePool.getAvailableCount();
```

---

##### `getInUseCount() : int`

**Mục đích:** Lấy số lượng đối tượng hiện đang được sử dụng.

**Tham số:** Không

**Giá trị trả về:** `int` - Số lượng đối tượng hoạt động

**Ví dụ:**
```java
int inUse = projectilePool.getInUseCount();
```

---

##### `getTotalCount() : int`

**Mục đích:** Lấy tổng số đối tượng trong pool (có sẵn + đang sử dụng).

**Tham số:** Không

**Giá trị trả về:** `int` - Tổng số

**Ví dụ:**
```java
int total = projectilePool.getTotalCount();
```

---

##### `expand(int count) : void`

**Mục đích:** Thêm nhiều đối tượng vào pool.

**Tham số:**
- `count: int` - Số lượng đối tượng để tạo

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Tạo các đối tượng mới bằng factory
- Thêm chúng vào hàng đợi có sẵn

**Ví dụ:**
```java
projectilePool.expand(50); // Thêm 50 đối tượng nữa
```

---

##### `clear() : void`

**Mục đích:** Xóa toàn bộ pool.

**Tham số:** Không

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Làm trống hàng đợi có sẵn
- Làm trống tập hợp inUse

**Ví dụ:**
```java
projectilePool.clear(); // Để kiểm tra/dọn dẹp
```

---

## Gói Map

### `CellType` (Enum)

**Vị trí:** `src/main/java/com/game/map/CellType.java`

**Vai trò:** Enumeration của các loại ô lưới khác nhau.

#### Loại Ô

```java
EMPTY       // Có thể đặt tháp
PATH        // Đường dẫn của kẻ thù
OCCUPIED    // Đã có tháp
SPAWN       // Điểm xuất hiện của kẻ thù
BASE        // Căn cứ người chơi
```

---

### `Cell` (Ô Lưới)

**Vị trí:** `src/main/java/com/game/map/Cell.java`

**Vai trò:** Đại diện cho một ô duy nhất trong lưới game với thông tin vị trí và loại.

#### Fields (Trường)

```java
final int row           // Chỉ số hàng
final int col           // Chỉ số cột
CellType type           // Loại ô
```

#### Methods (Phương Thức)

##### `Cell(int row, int col, CellType type) : constructor`

**Mục đích:** Tạo một ô lưới.

**Tham số:**
- `row: int` - Chỉ số hàng (dựa 0)
- `col: int` - Chỉ số cột (dựa 0)
- `type: CellType` - Loại ô ban đầu

**Ví dụ:**
```java
Cell cell = new Cell(5, 10, CellType.EMPTY);
```

---

##### `getRow() : int`

**Mục đích:** Lấy chỉ số hàng.

**Giá trị trả về:** `int` - Hàng (dựa 0)

---

##### `getCol() : int`

**Mục đích:** Lấy chỉ số cột.

**Giá trị trả về:** `int` - Cột (dựa 0)

---

##### `getType() : CellType`

**Mục đích:** Lấy loại ô.

**Giá trị trả về:** `CellType` - Loại ô hiện tại

---

##### `setType(CellType type) : void`

**Mục đích:** Đặt loại ô (ví dụ: đặt tháp).

**Tham số:**
- `type: CellType` - Loại mới

**Ví dụ:**
```java
cell.setType(CellType.OCCUPIED); // Tháp được đặt
```

---

##### `canPlaceTower() : boolean`

**Mục đích:** Kiểm tra xem tháp có thể được đặt trên ô này hay không.

**Tham số:** Không

**Giá trị trả về:** `boolean` - true nếu ô là EMPTY, false nếu không

**Ví dụ:**
```java
if (cell.canPlaceTower()) {
    // Đặt tháp
}
```

---

## Gói Controller

### `PlayerState` (Trình Quản Lý Trạng Thái Game)

**Vị trí:** `src/main/java/com/game/controller/PlayerState.java`

**Vai trò:** Quản lý sức khỏe và vàng của người chơi, phát hành các sự kiện thay đổi trạng thái.

#### Fields (Trường)

```java
int health          // Sức khỏe hiện tại
int maxHealth        // Sức khỏe tối đa (không thay đổi)
int gold            // Số lượng vàng hiện tại
```

#### Methods (Phương Thức)

##### `PlayerState(int initialHealth, int initialGold) : constructor`

**Mục đích:** Tạo trạng thái người chơi với các giá trị ban đầu.

**Tham số:**
- `initialHealth: int` - Sức khỏe ban đầu (cũng trở thành maxHealth)
- `initialGold: int` - Số lượng vàng ban đầu

**Ví dụ:**
```java
PlayerState player = new PlayerState(20, 100);
```

---

##### `takeDamage(int damage) : void`

**Mục đích:** Giảm sức khỏe người chơi, phát hành sự kiện, kiểm tra game over.

**Tham số:**
- `damage: int` - Lượng sát thương để nhận

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Giảm sức khỏe (tối thiểu 0)
- Phát hành sự kiện `PLAYER_HP_CHANGED`
- Phát hành sự kiện `GAME_OVER` nếu sức khỏe đạt 0

**Ví dụ:**
```java
player.takeDamage(5); // Nhận 5 sát thương
```

---

##### `addGold(int amount) : void`

**Mục đích:** Thêm vàng cho người chơi, phát hành sự kiện.

**Tham số:**
- `amount: int` - Vàng để thêm

**Giá trị trả về:** `void`

**Tác dụng phụ:**
- Tăng vàng
- Phát hành sự kiện `PLAYER_GOLD_CHANGED`

**Ví dụ:**
```java
player.addGold(25); // Kiếm 25 vàng từ kẻ thù
```

---

##### `spendGold(int amount) : boolean`

**Mục đích:** Cố gắng chi tiêu vàng (ví dụ: mua tháp).

**Tham số:**
- `amount: int` - Vàng để chi tiêu

**Giá trị trả về:** `boolean` - true nếu đủ vàng, false nếu không

**Tác dụng phụ:**
- Nếu thành công: giảm vàng, phát hành sự kiện `PLAYER_GOLD_CHANGED`
- Nếu thất bại: không thay đổi

**Ví dụ:**
```java
if (player.spendGold(100)) {
    // Đặt tháp thành công
} else {
    // Vàng không đủ
}
```

---

##### `getHealth() : int`

**Mục đích:** Lấy sức khỏe hiện tại.

**Giá trị trả về:** `int` - Sức khỏe hiện tại

---

##### `getMaxHealth() : int`

**Mục đích:** Lấy sức khỏe tối đa.

**Giá trị trả về:** `int` - Sức khỏe tối đa

---

##### `getGold() : int`

**Mục đích:** Lấy vàng hiện tại.

**Giá trị trả về:** `int` - Vàng hiện tại

---

##### `setHealth(int health) : void`

**Mục đích:** Trực tiếp đặt sức khỏe (sử dụng cẩn thận).

**Tham số:**
- `health: int` - Giá trị sức khỏe mới

---

##### `setGold(int gold) : void`

**Mục đích:** Trực tiếp đặt vàng (sử dụng cẩn thận).

**Tham số:**
- `gold: int` - Giá trị vàng mới

---

##### `isAlive() : boolean`

**Mục đích:** Kiểm tra xem người chơi có còn sống hay không.

**Tham số:** Không

**Giá trị trả về:** `boolean` - true nếu sức khỏe > 0

**Ví dụ:**
```java
if (!player.isAlive()) {
    gameScene.gameOver();
}
```

---

## Gói Utility

### `GameConfig` (Hằng Số Cấu Hình)

**Vị trí:** `src/main/java/com/game/util/GameConfig.java`

**Vai trò:** Kho lưu trữ trung tâm cho tất cả các giá trị cấu hình game. Sửa đổi các hằng số này để điều chỉnh gameplay.

#### Cài Đặt Cửa Sổ

```java
WINDOW_WIDTH = 1280           // pixel
WINDOW_HEIGHT = 720           // pixel
WINDOW_TITLE = "Tower Defense 2D MVP"
```

#### Cài Đặt Game

```java
TARGET_FPS = 60.0             // Khung hình mỗi giây
FRAME_TIME = 1/60 ≈ 0.01667   // Giây mỗi khung hình
```

#### Cài Đặt Lưới

```java
GRID_CELL_SIZE = 40           // Pixel mỗi ô
GRID_WIDTH = 32               // Ô (1280/40)
GRID_HEIGHT = 18              // Ô (720/40)
```

#### Cài Đặt Người Chơi

```java
STARTING_HEALTH = 20          // Sức khỏe ban đầu
STARTING_GOLD = 100           // Vàng ban đầu
```

#### Cài Đặt Tháp (GunTower)

```java
TOWER_GUN_COST = 100f         // Chi phí vàng
TOWER_GUN_RANGE = 150f        // Pixel
TOWER_GUN_FIRE_RATE = 1.0f    // Phát bắn mỗi giây
```

#### Cài Đặt Tháp (SlowTower)

```java
TOWER_SLOW_COST = 150f        // Chi phí vàng
TOWER_SLOW_RANGE = 200f       // Pixel
TOWER_SLOW_FIRE_RATE = 0.5f   // Phát bắn mỗi giây
```

#### Cài Đặt Kẻ Thù (Goblin)

```java
ENEMY_GOBLIN_HP = 50f         // Điểm sức khỏe
ENEMY_GOBLIN_SPEED = 120f     // Pixel mỗi giây
ENEMY_GOBLIN_REWARD = 10      // Vàng khi chết
```

#### Cài Đặt Kẻ Thù (Orc)

```java
ENEMY_ORC_HP = 200f           // Điểm sức khỏe
ENEMY_ORC_SPEED = 60f         // Pixel mỗi giây
ENEMY_ORC_REWARD = 25         // Vàng khi chết
```

#### Cài Đặt Kẻ Thù (Dragon)

```java
ENEMY_DRAGON_HP = 500f        // Điểm sức khỏe
ENEMY_DRAGON_SPEED = 40f      // Pixel mỗi giây
ENEMY_DRAGON_REWARD = 60      // Vàng khi chết
```

#### Cài Đặt Đạn

```java
PROJECTILE_SPEED = 500f       // Pixel mỗi giây
PROJECTILE_DAMAGE = 10        // Sát thương mỗi phát trúng
```

#### Cài Đặt Sóng

```java
WAVE_SPAWN_INTERVAL = 0.5f    // Giây giữa các lần xuất hiện của kẻ thù
WAVE_DELAY = 10f              // Giây giữa các sóng
```

**Ghi chú:** Lớp tĩnh, không thể khởi tạo

---

### `Constants` (Hằng Số Hình Ảnh & Gỡ Lỗi)

**Vị trí:** `src/main/java/com/game/util/Constants.java`

**Vai trò:** Hằng số hình ảnh và gỡ lỗi thời chạy.

#### Màu Sắc (Chuỗi Hex)

```java
COLOR_BACKGROUND = "#0a0e27"      // Nền xanh đậm
COLOR_GRID = "#333333"             // Đường lưới tối
COLOR_PATH = "#556677"             // Màu đường dẫn
COLOR_EMPTY = "#1a1a2e"            // Màu ô trống
COLOR_TOWER_SELECTED = "#ffff00"   // Làm nổi bật vàng
```

#### Hằng Số Giao Diện

```java
HUD_PADDING = 10                   // Pixel
HUD_FONT_SIZE = 16                 // Điểm
```

#### Hoạt Hình

```java
SPRITE_FRAME_TIME = 100            // Mili giây mỗi khung hình sprite
```

#### Vật Lý

```java
GRAVITY = 0f                       // Không có trọng lực (2D từ trên xuống)
```

#### Cờ Gỡ Lỗi

```java
DEBUG_MODE = true                  // Bật/tắt tất cả tính năng gỡ lỗi
DEBUG_GRID = true                  // Hiển thị lớp phủ lưới
DEBUG_COLLISION = false            // Hiển thị hộp va chạm
```

**Ghi chú:** Lớp tĩnh, không thể khởi tạo

---

## Các Lớp Sẽ Triển Khai

### Hệ Thống Tháp

**Lớp để triển khai:**
- `Tower` (lớp cơ sở trừu tượng)
- `GunTower` (bắn nhanh, sát thương thấp)
- `SlowTower` (bắn chậm, áp dụng debuff)
- `TowerFactory` (tạo tháp)

---

### Hệ Thống Kẻ Thù

**Lớp để triển khai:**
- `Enemy` (lớp cơ sở trừu tượng)
- `Goblin` (nhanh, máu ít)
- `Orc` (chậm, máu nhiều)
- `Dragon` (rất chậm, máu rất nhiều, miễn slow một phần)
- `EnemyFactory` (tạo kẻ thù)

---

### Hệ Thống Chiến Đấu

**Lớp để triển khai:**
- `Projectile` (triển khai Poolable)
- `DamageNumber` (văn bản sát thương nổi)

---

### Hệ Thống Bản Đồ

**Lớp để triển khai:**
- `Grid` (lưới ô M×N)
- `MapManager` (tải bản đồ từ CSV)

---

### Hệ Thống Sóng

**Lớp để triển khai:**
- `Wave` (cấu hình sóng)
- `WaveManager` (quản lý tiến trình sóng)

---

### Hệ Thống Xem Và Hiển Thị

**Lớp để triển khai:**
- `HUD` (render giao diện người dùng)
- `SpriteSheet` (tải/render sprite hình ảnh)
- `AnimationPlayer` (phát hoạt hình sprite)

---

## Ví Dụ Sử Dụng

### Ví dụ 1: Đăng ký Sự Kiện

```java
// Trong một số mã khởi tạo:
EventBus bus = EventBus.getInstance();

bus.subscribe(GameEvent.ENEMY_DIED, (data) -> {
    Enemy enemy = (Enemy) data;
    playerState.addGold(enemy.getReward());
    System.out.println("Kẻ thù chết, kiếm được " + enemy.getReward() + " vàng");
});
```

### Ví dụ 2: Sử Dụng Object Pool

```java
// Thiết lập
ObjectPool<Projectile> projectilePool = new ObjectPool<>(
    Projectile::new,
    100
);

// Bắn
Projectile p = projectilePool.acquire();
p.initialize(tower.getX(), tower.getY(), target);

// Khi trúng
projectilePool.release(p);
```

### Ví dụ 3: Kiểm Tra Khoảng Cách Thực Thể

```java
float distance = tower.distanceTo(enemy);
if (distance < tower.getRange()) {
    tower.fire(enemy);
}
```

### Ví dụ 4: Quản Lý Trạng Thái Người Chơi

```java
// Người chơi nhận sát thương
player.takeDamage(5);

// Người chơi mua tháp
if (player.spendGold(100)) {
    // Tháp được đặt thành công
}

// Kiểm tra xem còn sống hay không
if (!player.isAlive()) {
    sceneManager.switchToMenuScene();
}
```

---

## Các Mẫu Thiết Kế Sử Dụng

| Mẫu | Vị Trí | Mục Đích |
|-----|--------|---------|
| **Singleton** | `EventBus`, `GameLoop` | Instance duy nhất được chia sẻ |
| **Observer** | `EventBus` | Phát hành/đăng ký sự kiện |
| **Object Pool** | `ObjectPool<T>` | Tái sử dụng đối tượng, giảm GC |
| **Factory** | Sẽ dùng: `TowerFactory`, `EnemyFactory` | Tạo thực thể |
| **Abstract Base** | `Entity` | Giao diện chung cho các đối tượng game |
| **State Pattern** | Sẽ dùng: `EnemyState`, `TowerState` | Quản lý trạng thái thực thể |

---

## Ghi Chú An Toàn Luồng

- **AnimationTimer:** Tất cả mã chạy trong luồng ứng dụng FX (an toàn bởi thiết kế)
- **EventBus:** An toàn cho truy cập đồng thời; subscriber được gọi đồng bộ
- **ObjectPool:** Không an toàn luồng; chỉ dùng trong vòng lặp game một luồng

---

## Cân Nhắc Hiệu Suất

- **Object Pool:** Dùng cho Projectile (1000+/game), Particles, DamageNumbers
- **Cập nhật Entity:** O(n) trong đó n là số lượng thực thể; tối ưu hóa với phân vùng không gian nếu > 1000
- **Rendering:** Đặt các thao tác vẽ hàng loạt; canvas duy nhất mỗi cảnh
- **Phát hành Sự Kiện:** O(m) trong đó m là số lượng subscriber; giữ số lượng subscriber thấp

---

*Cập nhật lần cuối: 2026-07-13 | Phiên bản 1.0*
