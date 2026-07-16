# 🏰 Kế Hoạch Phát Triển: Tower Defense 2D MVP
**Vai trò:** Game Producer + Senior Software Engineer  
**Công nghệ:** Java 17+ · JavaFX 21  
**Thể loại:** 2D Pixel Art — Medieval / Sci-fi  
**Deadline:** 3 tuần · 6 ngày/tuần = 18 ngày làm việc

---

## 📐 Kiến Trúc Dự Án Đề Xuất

```
tower-defense/
├── src/main/java/com/game/
│   ├── core/           # Game loop, scene manager
│   ├── model/          # Entity, Tower, Enemy, Projectile
│   ├── view/           # JavaFX Canvas rendering
│   ├── controller/     # Input, Wave controller
│   ├── system/         # Object Pool, Event Bus
│   ├── map/            # Grid, Pathfinding
│   └── util/           # Config, Constants
├── src/main/resources/
│   ├── sprites/        # Pixel art assets
│   ├── maps/           # Map data (JSON/CSV)
│   └── fxml/           # UI layouts
└── pom.xml / build.gradle
```

---

## 🗓️ TUẦN 1: CORE MECHANICS (Ngày 1–6)

> **Mục tiêu:** Có một game loop chạy được, grid render, tower đặt được, enemy di chuyển theo path.

---

### Ngày 1 — Project Setup & Game Loop
- [ ] Tạo Maven/Gradle project với JavaFX dependency
- [ ] Cấu hình `module-info.java` (JavaFX module system)
- [ ] Tạo `GameApplication.java` — main entry point
- [ ] Implement `GameLoop` dùng `AnimationTimer` của JavaFX
  ```java
  // AnimationTimer cho phép update 60fps mà không blocking UI thread
  AnimationTimer timer = new AnimationTimer() {
      public void handle(long now) {
          update(deltaTime);
          render();
      }
  };
  ```
- [ ] Tạo `SceneManager` — quản lý chuyển đổi màn hình (Menu → Game → GameOver)
- [ ] Setup `Canvas` + `GraphicsContext` để vẽ pixel art
- [ ] Commit: `feat: project setup + game loop foundation`

---

### Ngày 2 — Grid System & Map
- [ ] Thiết kế class `Grid` (N×M cells, mỗi cell biết trạng thái: EMPTY/PATH/OCCUPIED)
- [ ] Render Grid lên Canvas (vẽ lines + fill màu theo trạng thái)
- [ ] Load map từ file CSV (định nghĩa path bằng số 0/1)
- [ ] Implement `Cell` class: `{int row, col, CellType type}`
- [ ] Tạo `MapManager` — đọc & validate map data
- [ ] Highlight cell khi hover chuột (chuẩn bị cho tower placement)
- [ ] Commit: `feat: grid system + CSV map loading`

---

### Ngày 3 — Pathfinding & Enemy Movement
- [ ] Implement path từ điểm spawn → base (dùng **waypoint list** từ map CSV, KHÔNG dùng A* để tiết kiệm thời gian)
- [ ] Tạo abstract `Entity` base class: `{float x, y, speed, hp}`
- [ ] Tạo class `Enemy` extends `Entity`:
  - `Goblin` — HP thấp, speed nhanh
  - `Orc` — HP cao, speed chậm  
  - `Dragon` — HP rất cao, miễn nhiễm slow một phần
- [ ] Logic di chuyển enemy theo waypoints (lerp giữa các điểm)
- [ ] Enemy đến base → trừ máu người chơi, despawn
- [ ] Commit: `feat: enemy entity + waypoint movement`

---

### Ngày 4 — Tower Placement System
- [ ] Tạo abstract `Tower` class: `{float range, float fireRate, int cost}`
- [ ] Implement 2 loại tháp:
  - `GunTower` (bắn nhanh, damage thấp, Medieval: Archer / Sci-fi: Laser)
  - `SlowTower` (bắn chậm, áp dụng debuff "slow 50%" lên enemy, Medieval: Ice / Sci-fi: EMP)
- [ ] Click ô EMPTY trên grid → đặt tower (nếu đủ tiền)
- [ ] Tower tự tìm enemy trong range (spatial check đơn giản: distance < range)
- [ ] Tạo `PlayerState`: `{int hp, int gold}`
- [ ] UI HUD cơ bản: hiển thị HP và Gold
- [ ] Commit: `feat: tower placement + player state`

---

### Ngày 5 — Combat: Projectile & Damage
- [ ] Tạo `Projectile` class: `{float x, y, float speed, int damage, Enemy target}`
- [ ] Tower fire theo `fireRate` (cooldown timer)
- [ ] Projectile di chuyển về phía target (homing)
- [ ] Va chạm Projectile–Enemy → trừ HP, despawn projectile
- [ ] Enemy HP = 0 → despawn, cộng gold cho player
- [ ] **Object Pool** cho Projectile (xem phần Design Patterns)
- [ ] Commit: `feat: combat system + projectile object pool`

---

### Ngày 6 — Wave System & Integration Test
- [ ] Tạo `WaveManager`:
  - Load wave config từ JSON `{waveNumber, enemies: [{type, count, interval}]}`
  - Spawn enemy theo interval (ví dụ: 1 enemy/0.5s)
  - Delay giữa các wave (10 giây chuẩn bị)
- [ ] Implement 3 wave đầu để test
- [ ] Integration test: đặt tower → wave spawn → tower bắn → game over nếu HP = 0
- [ ] Fix bugs phát sinh từ integration
- [ ] Commit: `feat: wave system + full integration test W1`

---

## 🎨 TUẦN 2: ĐỒ HỌA & NỘI DUNG (Ngày 7–12)

> **Mục tiêu:** Thay thế placeholder bằng pixel art thật, UI hoàn chỉnh, âm thanh cơ bản.

---

### Ngày 7 — Asset Pipeline & Sprite System
- [ ] Quyết định theme: **Medieval** (Goblin/Orc/Dragon + Archer/Ice Tower) hoặc **Sci-fi**
- [ ] Thu thập/tạo sprite sheets miễn phí (nguồn: itch.io, OpenGameArt.org)
  - Kích thước đề xuất: 16×16 hoặc 32×32 pixel per tile
- [ ] Tạo `SpriteSheet` class: cắt frame từ atlas PNG
- [ ] Tạo `AnimationPlayer`: cycle qua frames theo fps
- [ ] Tích hợp vào `Enemy` và `Tower` rendering
- [ ] Commit: `feat: sprite system + animation player`

---

### Ngày 8 — Map Tileset & Visual Polish
- [ ] Thay grid placeholder bằng tileset pixel art (grass, stone path, water)
- [ ] Vẽ background map hoàn chỉnh với tile layers
- [ ] Tower placement preview (ghost sprite khi hover)
- [ ] Enemy death animation (fade out hoặc explosion)
- [ ] Projectile sprite (arrow / laser beam)
- [ ] Commit: `art: tileset map + entity sprites integrated`

---

### Ngày 9 — UI/UX: HUD & Menu
- [ ] Design Main Menu (JavaFX FXML): Play, Settings, Quit
- [ ] In-game HUD hoàn chỉnh:
  - HP bar (màu đỏ) + Gold counter
  - Wave indicator: "Wave 2/5"
  - Tower selection panel (bottom bar)
  - Tower range preview khi chọn
- [ ] Pause Menu (ESC key)
- [ ] Game Over screen + "Try Again" button
- [ ] Commit: `feat: full HUD + menu screens`

---

### Ngày 10 — Tower Upgrade & Sell (Bonus nếu kịp)
- [ ] Click vào tower đã đặt → hiện popup: Upgrade / Sell
- [ ] Upgrade tăng damage/range (cost thêm gold)
- [ ] Sell trả lại 50% giá trị
- [ ] Nếu không kịp → bỏ qua feature này, focus polish
- [ ] Commit: `feat: tower upgrade/sell system`

---

### Ngày 11 — Sound Effects & Music
- [ ] Dùng `javax.sound.sampled` (built-in) hoặc thêm lib nhỏ
- [ ] SFX: tower shoot, enemy death, wave start, game over
- [ ] Background music (loop) — tìm royalty-free trên freesound.org
- [ ] Settings: volume slider (Music / SFX)
- [ ] `AudioManager` singleton quản lý tất cả audio
- [ ] Commit: `feat: audio system + sfx integration`

---

### Ngày 12 — Content Expansion & Map Polish
- [ ] Hoàn thiện 5 wave với độ khó tăng dần
- [ ] Thêm wave boss (enemy HP x3, tốc độ chậm)
- [ ] Căn chỉnh lại balance sơ bộ (gold reward, tower cost)
- [ ] Kiểm tra toàn bộ flow game: Menu → Game → Win/Lose
- [ ] Screenshot & demo nội bộ
- [ ] Commit: `content: 5 waves + boss wave + map finalize`

---

## ⚖️ TUẦN 3: CÂN BẰNG GAME & POLISH (Ngày 13–18)

> **Mục tiêu:** Game vui và cảm giác "done". Bug-free, balance tốt, có thể ship.

---

### Ngày 13 — Game Balance Pass 1
- [ ] Điều chỉnh số liệu (tuning sheet):
  - Enemy HP, speed, gold reward
  - Tower damage, fire rate, cost, range
  - Slow debuff duration & magnitude
- [ ] Chơi thử toàn bộ 5 wave — ghi chép điểm gãy
- [ ] Điều chỉnh wave spawn rate cho cảm giác "áp lực nhưng không frustrating"
- [ ] Commit: `balance: enemy + tower stat tuning pass 1`

---

### Ngày 14 — Performance & Bug Fix
- [ ] Profile game với Java VisualVM (kiểm tra GC pressure, render time)
- [ ] Tối ưu: bỏ heap allocation trong game loop (dùng Object Pool)
- [ ] Fix tất cả bug tìm được trong ngày 13
- [ ] Spatial partitioning đơn giản nếu cần (chia grid zone để giảm O(n²) range check)
- [ ] Commit: `fix: performance optimization + bug fixes`

---

### Ngày 15 — Visual Polish & Particle Effects
- [ ] Particle system đơn giản: vẽ nhiều chấm nhỏ với velocity/lifetime
- [ ] Hiệu ứng: enemy chết (máu/tia sáng), tower bắn (muzzle flash), slow effect (icon đóng băng)
- [ ] Screen shake khi bị hit (lắc Canvas transform)
- [ ] Smooth camera pan nếu map to hơn screen
- [ ] Commit: `polish: particle effects + screen shake`

---

### Ngày 16 — Game Balance Pass 2 + Difficulty
- [ ] Thêm lựa chọn độ khó: Easy / Normal / Hard (nhân số liệu enemy)
- [ ] Test với người dùng ngoài (nếu có thể)
- [ ] Balance pass cuối dựa trên feedback
- [ ] Đảm bảo game có thể WIN được ở Normal
- [ ] Commit: `balance: difficulty system + final tuning`

---

### Ngày 17 — Final QA & Edge Cases
- [ ] Kiểm tra tất cả edge case: 
  - Đặt tower trên path
  - Mua tower khi không đủ tiền
  - Wave kết thúc khi còn enemy
  - Overflow gold/HP
- [ ] Cross-platform test (Windows/macOS nếu có)
- [ ] Viết `README.md` với hướng dẫn chạy game
- [ ] Đóng gói: `mvn package` → fat JAR runnable
- [ ] Commit: `qa: edge case fixes + packaging`

---

### Ngày 18 — Release & Demo
- [ ] Tạo release tag trên GitHub: `v1.0.0-mvp`
- [ ] Quay video demo gameplay (30-60 giây)
- [ ] Viết release notes
- [ ] Backup source code
- [ ] 🎉 **SHIP IT!**

---

## 🧩 DESIGN PATTERNS ĐỀ XUẤT

### 1. 🏊 Object Pool Pattern — Quản lý Projectile & Particle

**Vấn đề:** Tạo/hủy hàng trăm `Projectile` mỗi giây → GC pressure → stuttering  
**Giải pháp:** Pre-allocate pool, tái sử dụng object thay vì `new`

```java
// ObjectPool.java
public class ObjectPool<T extends Poolable> {
    private final Queue<T> pool = new ArrayDeque<>();
    private final Supplier<T> factory;

    public ObjectPool(Supplier<T> factory, int initialSize) {
        this.factory = factory;
        for (int i = 0; i < initialSize; i++)
            pool.offer(factory.get());
    }

    public T acquire() {
        T obj = pool.poll();
        return obj != null ? obj : factory.get();
    }

    public void release(T obj) {
        obj.reset(); // reset về trạng thái ban đầu
        pool.offer(obj);
    }
}

// Sử dụng:
// ObjectPool<Projectile> projectilePool = new ObjectPool<>(Projectile::new, 100);
// Khi tower bắn: Projectile p = projectilePool.acquire();
// Khi hit: projectilePool.release(p);
```
**Dùng cho:** `Projectile`, `ParticleEffect`, `DamageNumber`

---

### 2. 📣 Observer / Event Bus Pattern — Giao tiếp giữa các hệ thống

**Vấn đề:** Tower cần biết khi Enemy chết để cộng gold, HUD cần biết HP thay đổi → nếu reference trực tiếp → spaghetti code  
**Giải pháp:** Event Bus trung tâm, các module đăng ký lắng nghe event

```java
// GameEvent.java
public enum GameEvent {
    ENEMY_DIED, ENEMY_REACHED_BASE, WAVE_STARTED, WAVE_COMPLETED,
    TOWER_PLACED, PLAYER_GOLD_CHANGED, PLAYER_HP_CHANGED
}

// EventBus.java (Singleton)
public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    private final Map<GameEvent, List<Consumer<Object>>> listeners = new EnumMap<>(GameEvent.class);

    public void subscribe(GameEvent event, Consumer<Object> listener) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    public void publish(GameEvent event, Object data) {
        List<Consumer<Object>> subs = listeners.getOrDefault(event, List.of());
        subs.forEach(l -> l.accept(data));
    }
}

// Sử dụng:
// Enemy.java: EventBus.INSTANCE.publish(ENEMY_DIED, this);
// PlayerState.java: EventBus.INSTANCE.subscribe(ENEMY_DIED, e -> gold += ((Enemy)e).reward);
// HUD.java: EventBus.INSTANCE.subscribe(PLAYER_GOLD_CHANGED, e -> updateGoldLabel());
```

---

### 3. 🔄 State Pattern — Quản lý trạng thái Enemy & Game

**Vấn đề:** Enemy có nhiều trạng thái (Moving, Slowed, Dead), Game có nhiều state (Menu, Playing, Paused, GameOver) → if-else lồng nhau rất tệ  
**Giải pháp:** Encapsulate mỗi state thành class riêng

```java
// EnemyState interface
public interface EnemyState {
    void enter(Enemy enemy);
    void update(Enemy enemy, float deltaTime);
    void exit(Enemy enemy);
}

// MovingState.java
public class MovingState implements EnemyState {
    public void update(Enemy enemy, float dt) {
        enemy.moveAlongPath(dt);
        if (enemy.isSlowed()) enemy.setState(new SlowedState());
        if (enemy.isDead()) enemy.setState(new DeadState());
    }
}

// SlowedState.java
public class SlowedState implements EnemyState {
    private float timer;
    public void update(Enemy enemy, float dt) {
        timer -= dt;
        enemy.moveAlongPath(dt * 0.5f); // di chuyển 50% speed
        if (timer <= 0) enemy.setState(new MovingState());
    }
}
```

**Dùng cho:**
- `GameState`: `MenuState`, `PlayingState`, `PausedState`, `GameOverState`
- `EnemyState`: `MovingState`, `SlowedState`, `DeadState`
- `TowerState`: `IdleState`, `TargetingState`, `FiringState`

---

### 4. 🏭 Factory Pattern — Tạo Enemy & Tower

**Vấn đề:** `new Goblin()`, `new Orc()` rải rác khắp code → khó maintain  
**Giải pháp:** Tập trung logic khởi tạo vào Factory

```java
public class EnemyFactory {
    public static Enemy create(EnemyType type, float x, float y) {
        return switch (type) {
            case GOBLIN -> new Enemy(x, y, hp: 50,  speed: 120f, reward: 10);
            case ORC    -> new Enemy(x, y, hp: 200, speed: 60f,  reward: 25);
            case DRAGON -> new Enemy(x, y, hp: 500, speed: 40f,  reward: 60);
        };
    }
}
// WaveManager dùng: Enemy e = EnemyFactory.create(EnemyType.GOBLIN, spawnX, spawnY);
```

---

## ⚠️ TOP 3 RỦI RO & CÁCH PHÒNG TRÁNH

---

### 🔴 Rủi ro #1: "Feature Creep" — Thêm tính năng không có trong kế hoạch

**Biểu hiện:** "Hay là mình thêm skill cây tower đi", "hay là làm thêm bản đồ 2 đi"…  
**Hậu quả:** Tuần 3 không còn thời gian polish, game unfinished cả ở feature cũ lẫn mới.

**Phòng tránh:**
> ✅ Tạo file `BACKLOG.md` — ghi mọi ý tưởng hay vào đó, KHÔNG implement ngay.  
> ✅ Rule cứng: **Tuần 1 & 2 = zero new features ngoài kế hoạch**. Tuần 3 chỉ được implement backlog nếu đã xong tất cả checklist.  
> ✅ Đánh dấu rõ ràng scope trong `README.md`: "MVP v1.0 chỉ bao gồm..."

---

### 🔴 Rủi ro #2: JavaFX Threading & Game Loop Bug

**Biểu hiện:** `IllegalStateException: Not on FX application thread`, game bị freeze, rendering lag.  
**Hậu quả:** Mất 1-2 ngày debug vấn đề architecture thay vì build tính năng.

**Phòng tránh:**
> ✅ Từ ngày 1: **TOÀN BỘ game logic và rendering chạy trong `AnimationTimer` (FX thread)**.  
> ✅ KHÔNG dùng `Thread` hay `ExecutorService` cho game loop. JavaFX `AnimationTimer` đã là solution tốt nhất cho single-thread game loop.  
> ✅ Nếu cần load asset async → dùng `Task<Void>` của JavaFX, kết quả trả về qua `Platform.runLater()`.  
> ✅ Template kiến trúc:
```
FX Thread (AnimationTimer):
  ├── update(deltaTime)  ← game logic
  └── render()           ← Canvas drawing

Ngoài FX Thread (nếu cần):
  └── Task<Void>: load files → Platform.runLater(() -> onLoaded())
```

---

### 🔴 Rủi ro #3: Đồ Họa Mất Quá Nhiều Thời Gian

**Biểu hiện:** Tuần 2 dành 3-4 ngày tìm/vẽ asset, không kịp tích hợp.  
**Hậu quả:** Tuần 3 vẫn còn nợ đồ họa, không có thời gian balance & polish.

**Phòng tránh:**
> ✅ **Ngày 1: Quyết định và download asset ngay**, không để qua tuần 2.  
> ✅ Danh sách nguồn asset pixel art **miễn phí & sẵn dùng:**
> - [itch.io/game-assets/free/tag-pixel-art](https://itch.io/game-assets/free/tag-pixel-art)
> - [opengameart.org](https://opengameart.org) (filter: 2D, top rated)
> - **Kenny Assets** (kenney.nl) — Medieval/Sci-fi đều có, license CC0  
> 
> ✅ **Placeholder-first approach:** Tuần 1 dùng màu solid (tower = hình vuông xanh, enemy = hình tròn đỏ). Sprite chỉ swap vào Tuần 2.  
> ✅ Nếu ngày 10 chưa xong art → **freeze art, dùng placeholder**, chuyển sang balance/polish.

---

## 📊 TỔNG KẾT TIMELINE

| Tuần | Trọng Tâm | Deliverable |
|------|-----------|-------------|
| **Tuần 1** (Ngày 1–6) | Core Mechanics | Game loop ✓, Grid ✓, Enemy di chuyển ✓, Tower bắn ✓, Wave ✓ |
| **Tuần 2** (Ngày 7–12) | Đồ Họa & Nội Dung | Pixel art ✓, HUD ✓, Audio ✓, 5 Waves ✓ |
| **Tuần 3** (Ngày 13–18) | Balance & Polish | Bug-free ✓, Balanced ✓, Packaged ✓, Demo ✓ |

---

## 🛠️ TECH STACK & DEPENDENCIES

```xml
<!-- pom.xml -->
<dependencies>
    <!-- JavaFX Controls + FXML -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.2</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.2</version>
    </dependency>
    
    <!-- JSON parsing (cho wave config) -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
</dependencies>
```

**IDE Khuyến Nghị:** IntelliJ IDEA Community (free) — hỗ trợ JavaFX SDK tốt nhất.

---

*📝 Tài liệu này là living document — cập nhật khi có thay đổi. Good luck! 🚀*
