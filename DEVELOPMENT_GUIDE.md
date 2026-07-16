# 🚀 DEVELOPMENT GUIDE

## Cấu trúc Dự Án

```
tower-defense/
├── src/main/java/com/game/
│   ├── core/           # Game loop, scene manager, main application
│   ├── model/          # Entity, Tower, Enemy, Projectile
│   ├── view/           # Canvas rendering, HUD
│   ├── controller/     # Input handling, PlayerState
│   ├── system/         # EventBus, ObjectPool
│   ├── map/            # Grid, Cell, MapManager
│   └── util/           # GameConfig, Constants
├── src/main/resources/
│   ├── sprites/        # Pixel art assets (PNG)
│   ├── maps/           # Map data (CSV/JSON)
│   └── fxml/           # UI layouts
├── pom.xml
├── README.md
├── WEEK1_CHECKLIST.md (này)
└── .gitignore
```

## Quy Tắc Phát Triển

### 1. **Naming Conventions**
- **Classes:** PascalCase (e.g., `GameLoop`, `PlayerState`)
- **Methods:** camelCase (e.g., `update`, `handleKeyPress`)
- **Constants:** UPPER_SNAKE_CASE (e.g., `TOWER_COST`, `GRID_SIZE`)
- **Variables:** camelCase (e.g., `playerHealth`, `enemySpeed`)

### 2. **Code Organization**
- **Package by feature:** Các class liên quan được tập trung trong cùng package
- **Interfaces before implementation:** Define interface/abstract class trước khi implement
- **Single Responsibility:** Mỗi class có một trách nhiệm duy nhất

### 3. **Game Loop Pattern**
```java
while (game is running) {
    deltaTime = calculate time since last frame
    update(deltaTime)          // Logic update
    render()                   // Canvas drawing
}
```

### 4. **Event Communication**
Thay vì direct reference, dùng EventBus:

```java
// BAD - direct reference (tightly coupled)
public class Tower {
    private PlayerState player;
    public void onEnemyKilled() {
        player.addGold(100);  // ❌ Tightly coupled
    }
}

// GOOD - event-based (loosely coupled)
public class Tower {
    public void onEnemyKilled() {
        EventBus.getInstance().publish(GameEvent.ENEMY_DIED, this.enemy);
    }
}

// PlayerState listens
public class PlayerState {
    public PlayerState() {
        EventBus.getInstance().subscribe(GameEvent.ENEMY_DIED, e -> {
            this.addGold(((Enemy)e).getReward());
        });
    }
}
```

### 5. **Object Pooling**
Cho Projectile, Particle, DamageNumber:

```java
// Setup pool
ObjectPool<Projectile> projectilePool = new ObjectPool<>(
    Projectile::new,  // Factory
    100               // Initial size
);

// Acquire object
Projectile p = projectilePool.acquire();
p.initialize(tower, target);

// Release when done
projectilePool.release(p);
```

### 6. **Git Commit Messages**
Format: `type(scope): description`

```bash
git commit -m "feat(grid): implement grid system with CSV loading"
git commit -m "fix(enemy): correct pathfinding waypoint interpolation"
git commit -m "docs: update README with setup instructions"
```

**Types:**
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation
- `refactor:` - Code refactoring
- `perf:` - Performance improvement
- `test:` - Test additions

---

## Build & Run

### Compile
```bash
mvn clean compile
```

### Run from IDE
```bash
mvn javafx:run
```

### Debug
```bash
mvn javafx:run -Ddebug
```

### Package
```bash
mvn clean package
java -jar target/tower-defense-mvp.jar
```

---

## Debugging Tips

### 1. **Enable Debug Mode**
Sửa `Constants.java`:
```java
public static final boolean DEBUG_MODE = true;
public static final boolean DEBUG_GRID = true;
public static final boolean DEBUG_COLLISION = true;
```

### 2. **Print Statements**
```java
if (Constants.DEBUG_MODE) {
    System.out.println("Grid position: " + gridX + ", " + gridY);
}
```

### 3. **Common Issues**

| Issue | Solution |
|-------|----------|
| Game không render | Check `GameScene.render()` is called |
| Input không hoạt động | Ensure canvas has focus: `canvas.requestFocus()` |
| Flickering | Check delta time calculation, ensure single render pass |
| Memory leak | Use ObjectPool, remove event listeners |
| Thread error | Ensure all code runs in FX thread (AnimationTimer) |

---

## Key Classes Overview

### Core Package
- **GameApplication:** Main entry point, starts everything
- **GameLoop:** AnimationTimer loop, 60 FPS
- **SceneManager:** Manages scene transitions (Menu ↔ Game)
- **GameScene:** Represents current game state, handles update/render

### Model Package
- **Entity:** Base class for all game objects
- **Enemy:** Enemy unit, has HP, follows path
- **Tower:** Tower unit, fires at enemies
- **Projectile:** Projectile fired by towers

### System Package
- **EventBus:** Singleton for event publishing/subscription
- **GameEvent:** Enum of all game events
- **ObjectPool:** Generic pool for reusable objects

### Map Package
- **Grid:** N×M cell grid
- **Cell:** Individual cell with type
- **MapManager:** Loads map from CSV/JSON

### Util Package
- **GameConfig:** All game constants (costs, speeds, etc.)
- **Constants:** Visual constants (colors, debug flags)

---

## Performance Considerations

### Memory
- Pre-allocate ObjectPool to avoid GC spikes
- Reuse arrays instead of creating new ones
- Use primitives instead of objects when possible

### CPU
- Use spatial partitioning for collision checks (if needed)
- Cache calculations (e.g., distance-to-target)
- Minimize Canvas operations per frame

### Rendering
- One Canvas per scene (not per entity)
- Use GraphicsContext efficiently
- Batch similar drawing operations

---

## Resource Management

### Assets Paths (relative to `src/main/resources/`)
```
sprites/
├── towers/
├── enemies/
├── projectiles/
└── ui/

maps/
├── map1.csv
└── waves.json

fxml/
├── main_menu.fxml
└── game_hud.fxml
```

### Loading Assets
```java
// CSV map
MapManager.loadMap("maps/map1.csv");

// JSON waves
WaveManager.loadWaves("maps/waves.json");

// Images
Image image = new Image(
    getClass().getResourceAsStream("/sprites/tower.png")
);
```

---

## Testing Checklist

- [ ] Game starts without crashing
- [ ] Menu loads and responds to input
- [ ] Game scene renders with placeholder grid
- [ ] Canvas accepts focus for input
- [ ] Events publish correctly
- [ ] ObjectPool acquire/release works
- [ ] EntityMap loads without errors

---

## Next Steps

1. **Day 2:** Implement `Grid.java` and load CSV map
2. **Day 3:** Implement `Enemy` classes and waypoint movement
3. **Day 4:** Implement `Tower` classes and placement
4. **Day 5:** Implement `Projectile` and combat system
5. **Day 6:** Implement `WaveManager` and full integration

---

*Keep moving forward! Every line of code is progress. 🎮*
