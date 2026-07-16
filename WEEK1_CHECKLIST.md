# 📋 TUẦN 1 - CORE MECHANICS CHECKLIST

## Ngày 1 ✅ — Project Setup & Game Loop
- [x] Tạo Maven project với JavaFX dependency
- [x] Cấu hình `module-info.java` (JavaFX module system)
- [x] Tạo `GameApplication.java` — main entry point
- [x] Implement `GameLoop` dùng `AnimationTimer` của JavaFX
- [x] Tạo `SceneManager` — quản lý chuyển đổi màn hình (Menu → Game → GameOver)
- [x] Setup `Canvas` + `GraphicsContext` để vẽ pixel art
- [x] Tạo base classes: `Entity`, `Poolable`, `GameConfig`, `Constants`
- [x] Tạo `EventBus` system cho inter-system communication
- [x] Tạo `ObjectPool` generic class
- [x] Git repository initialization

**Status:** ✅ HOÀN THÀNH  
**Commits:** `feat: project setup + game loop foundation`

---

## Ngày 2 — Grid System & Map
- [ ] Thiết kế class `Grid` (N×M cells, mỗi cell biết trạng thái: EMPTY/PATH/OCCUPIED)
- [ ] Render Grid lên Canvas (vẽ lines + fill màu theo trạng thái)
- [ ] Load map từ file CSV (định nghĩa path bằng số 0/1)
- [ ] Implement `Cell` class: `{int row, col, CellType type}` ✅ (đã có)
- [ ] Tạo `MapManager` — đọc & validate map data
- [ ] Highlight cell khi hover chuột (chuẩn bị cho tower placement)
- [ ] **Commit:** `feat: grid system + CSV map loading`

**Tasks:**
1. Tạo `Grid.java` class
2. Tạo `MapManager.java` class
3. Tạo sample map CSV file ở `src/main/resources/maps/map1.csv`
4. Implement grid rendering trong `GameScene.render()`
5. Implement mouse hover highlighting

---

## Ngày 3 — Pathfinding & Enemy Movement
- [ ] Implement path từ điểm spawn → base (dùng waypoint list từ map CSV, KHÔNG dùng A*)
- [ ] Tạo abstract `Enemy` class extends `Entity`
- [ ] Implement 3 loại enemy:
  - `Goblin` — HP thấp (50), speed nhanh (120)
  - `Orc` — HP cao (200), speed chậm (60)
  - `Dragon` — HP rất cao (500), miễn nhiễm slow một phần
- [ ] Logic di chuyển enemy theo waypoints (lerp giữa các điểm)
- [ ] Enemy đến base → trừ máu người chơi, despawn
- [ ] **Commit:** `feat: enemy entity + waypoint movement`

**Tasks:**
1. Tạo `Enemy.java` abstract class
2. Tạo `Goblin.java`, `Orc.java`, `Dragon.java` classes
3. Tạo `EnemyFactory.java`
4. Implement waypoint following logic
5. Tích hợp vào `GameScene.update()`

---

## Ngày 4 — Tower Placement System
- [ ] Tạo abstract `Tower` class: `{float range, float fireRate, int cost}`
- [ ] Implement 2 loại tháp:
  - `GunTower` (bắn nhanh, damage thấp)
  - `SlowTower` (bắn chậm, áp dụng debuff "slow 50%" lên enemy)
- [ ] Click ô EMPTY trên grid → đặt tower (nếu đủ tiền)
- [ ] Tower tự tìm enemy trong range (spatial check đơn giản: distance < range)
- [ ] Tạo `PlayerState`: `{int hp, int gold}` ✅ (đã có)
- [ ] UI HUD cơ bản: hiển thị HP và Gold
- [ ] **Commit:** `feat: tower placement + player state`

**Tasks:**
1. Tạo `Tower.java` abstract class
2. Tạo `GunTower.java`, `SlowTower.java` classes
3. Tạo `TowerFactory.java`
4. Implement tower placement logic
5. Tạo `HUD.java` class để render player state
6. Implement mouse click handling cho tower placement

---

## Ngày 5 — Combat: Projectile & Damage
- [ ] Tạo `Projectile` class: `{float x, y, float speed, int damage, Enemy target}`
- [ ] Tower fire theo `fireRate` (cooldown timer)
- [ ] Projectile di chuyển về phía target (homing)
- [ ] Va chạm Projectile–Enemy → trừ HP, despawn projectile
- [ ] Enemy HP = 0 → despawn, cộng gold cho player
- [ ] **Object Pool** cho Projectile (dùng ObjectPool<Projectile>)
- [ ] **Commit:** `feat: combat system + projectile object pool`

**Tasks:**
1. Tạo `Projectile.java` class implements `Poolable`
2. Implement firing logic trong `Tower` class
3. Implement collision detection
4. Setup projectile pool trong `GameScene`
5. Implement enemy death logic

---

## Ngày 6 — Wave System & Integration Test
- [ ] Tạo `WaveManager`:
  - Load wave config từ JSON `{waveNumber, enemies: [{type, count, interval}]}`
  - Spawn enemy theo interval
  - Delay giữa các wave (10 giây chuẩn bị)
- [ ] Implement 3 wave đầu để test
- [ ] Integration test: đặt tower → wave spawn → tower bắn → game over nếu HP = 0
- [ ] Fix bugs phát sinh từ integration
- [ ] **Commit:** `feat: wave system + full integration test W1`

**Tasks:**
1. Tạo `WaveManager.java` class
2. Tạo sample wave config JSON file
3. Implement wave spawning logic
4. Full gameplay integration test
5. Bug fixes và balance adjustments

---

## 🎯 Key Focus Points

✅ **Do:**
- Placeholder-first: dùng color solid, không cần pixel art ngay
- Test thường xuyên: build và chạy `mvn javafx:run`
- Commit mỗi feature xong: `git add . && git commit -m "feat: ..."`
- Ghi chép issue/bug tìm được vào file `BACKLOG.md`

❌ **Don't:**
- Thêm feature không trong kế hoạch (ghi vào BACKLOG thay)
- Dùng Thread hay ExecutorService cho game loop
- Bỏ qua placeholder → ngay lập tức phải có pixel art

---

## 📊 Metrics

| Day | Feature | Est. Hours | Status |
|-----|---------|-----------|--------|
| Day 1 | Project Setup | 2h | ✅ DONE |
| Day 2 | Grid System | 3h | ⏳ TODO |
| Day 3 | Enemy Movement | 4h | ⏳ TODO |
| Day 4 | Tower Placement | 4h | ⏳ TODO |
| Day 5 | Combat | 3h | ⏳ TODO |
| Day 6 | Wave System | 3h | ⏳ TODO |
| **Total** | **Week 1** | **19h** | |

---

*Updated: 2026-07-13 — Let's build! 🚀*
