# 🚀 QUICK START - Tower Defense Development

**Tài liệu này giúp bạn nhanh chóng bắt đầu code từ hôm nay**

---

## 📋 Tài Liệu Chính (Read These First!)

| File | Mục đích | Thời gian |
|------|---------|----------|
| **API_DOCUMENTATION.md** | Reference chi tiết tất cả methods/classes | 📖 Dùng khi code |
| **IMPLEMENTATION_PLAN.md** | Plan 18 ngày với task breakdown | 📋 Xem task hôm nay |
| **WEEK1_CHECKLIST.md** | Checklist tuần 1, day-by-day | ✅ Update hàng ngày |

---

## 🎯 Today's Task (Hôm Nay)

### Status
- ✅ **Day 1 (2026-07-13):** Foundation Complete
- ⏳ **Tomorrow (Day 2):** Grid System & Map Loading

### If You're Starting Now (Day 2)

**Go read:** 
1. IMPLEMENTATION_PLAN.md → "DAY 2: Grid System & Map" section
2. API_DOCUMENTATION.md → Search for "Cell", "Grid" (for future reference)

**Your tasks for today:**
- [ ] Create `Grid.java` class
- [ ] Create `MapManager.java` class  
- [ ] Implement grid rendering
- [ ] Add cell hover highlighting
- [ ] Create sample maps
- [ ] Test everything works

**Estimated time:** 3 hours

---

## 🏗️ Project Structure at a Glance

```
src/main/java/com/game/
├── core/              ✅ DONE (Day 1)
│   ├── GameApplication.java    (Entry point)
│   ├── GameLoop.java           (60 FPS loop)
│   ├── SceneManager.java       (Scene transitions)
│   └── GameScene.java          (Game state)
│
├── model/             ✅ PARTIAL (Day 1)
│   ├── Entity.java             (Base class)
│   └── Poolable.java           (Interface)
│
├── system/            ✅ DONE (Day 1)
│   ├── EventBus.java           (Event system)
│   ├── GameEvent.java          (Event enum)
│   └── ObjectPool.java         (Object pooling)
│
├── map/               ⏳ TODO (Day 2)
│   ├── Cell.java               (Cell class)
│   ├── CellType.java           (Type enum)
│   ├── Grid.java               (TO CREATE)
│   └── MapManager.java         (TO CREATE)
│
├── controller/        ✅ DONE (Day 1)
│   └── PlayerState.java        (Player state)
│
├── view/              ⏳ TODO (Day 7+)
│   ├── HUD.java
│   ├── SpriteSheet.java
│   └── AnimationPlayer.java
│
└── util/              ✅ DONE (Day 1)
    ├── GameConfig.java         (Constants)
    └── Constants.java          (Visual consts)
```

---

## 🔧 Build & Run (Every Session)

```bash
# Compile the project
mvn clean compile

# Run the game
mvn javafx:run

# Package as JAR (at end)
mvn clean package
java -jar target/tower-defense-mvp.jar
```

---

## 📖 Quick API Reference

### How to Use EventBus

```java
// Subscribe to event
EventBus.getInstance().subscribe(GameEvent.ENEMY_DIED, (data) -> {
    Enemy enemy = (Enemy) data;
    // Do something
});

// Publish event
EventBus.getInstance().publish(GameEvent.ENEMY_DIED, enemy);
```

**Docs:** → API_DOCUMENTATION.md → "EventBus" section

---

### How to Use ObjectPool

```java
// Create pool
ObjectPool<Projectile> pool = new ObjectPool<>(Projectile::new, 100);

// Get object
Projectile p = pool.acquire();
p.initialize(x, y, target);

// Return object
pool.release(p);
```

**Docs:** → API_DOCUMENTATION.md → "ObjectPool" section

---

### How to Use PlayerState

```java
PlayerState player = new PlayerState(20, 100); // hp=20, gold=100

player.takeDamage(5);           // Damage
player.addGold(25);             // Gain gold
if (player.spendGold(100)) {    // Spend gold
    // Tower placed
}
if (!player.isAlive()) {        // Check alive
    gameOver();
}
```

**Docs:** → API_DOCUMENTATION.md → "PlayerState" section

---

## 🎮 Game Loop Flow

```
1. GameApplication.start()
   ↓
2. GameLoop.start()  (AnimationTimer at 60 FPS)
   ↓
3. Each Frame:
   - update(deltaTime)        ← Game logic
   - render()                 ← Drawing
   ↓
4. SceneManager delegates to current GameScene
```

**Docs:** → API_DOCUMENTATION.md → "GameLoop" section

---

## ✅ Daily Workflow

### Morning (Start of Day)

1. **Check today's task**
   - Open `IMPLEMENTATION_PLAN.md`
   - Find "DAY X: Task Name" section
   - Read through all sub-tasks

2. **Understand blockers/dependencies**
   - Check if previous days are done
   - Look at "Dependencies" section

3. **Reference the API**
   - For any class you'll use, open API_DOCUMENTATION.md
   - Search for class name
   - Read Purpose, Methods, Examples

### During Day (Code)

1. **Implement one task at a time**
   - Don't skip around

2. **Reference API_DOCUMENTATION.md frequently**
   - Bookmark it
   - Use Ctrl+F to search

3. **Build & test every 30 minutes**
   - `mvn clean compile` to check for errors
   - `mvn javafx:run` to test
   - Any compilation errors = fix immediately

4. **Log time & issues**
   - How long did task take?
   - Any blockers?
   - Note in WEEK1_CHECKLIST.md

### Evening (End of Day)

1. **Update WEEK1_CHECKLIST.md**
   - Mark completed tasks as "✅"
   - Update "Status" and "Completion %"

2. **Update STATUS.md**
   - Update overall progress
   - Note any blockers

3. **Commit to Git**
   - `git add .`
   - `git commit -m "feat(scope): description"` 
   - Use commit message from IMPLEMENTATION_PLAN.md

4. **Plan tomorrow**
   - Review next day's tasks
   - Any prep work?

---

## 🐛 Common Issues & Fixes

### Issue: "Cannot find symbol"
**Fix:** Check you're in correct package, import statement exists
**Docs:** → API_DOCUMENTATION.md → Search class name → Check import

### Issue: "Entity methods not found"
**Fix:** Make sure your class extends Entity, call super()
```java
public class Enemy extends Entity {
    public Enemy(float x, float y, float w, float h) {
        super(x, y, w, h);  // Call parent constructor
    }
}
```

### Issue: "EventBus not publishing"
**Fix:** Make sure you called `publish()` with correct enum value
```java
EventBus.getInstance().publish(GameEvent.ENEMY_DIED, enemy);
```

### Issue: "ObjectPool returns null"
**Fix:** Make sure objects implement Poolable interface and have reset() method

### Issue: "Game won't start"
**Fix:** 
1. Check console for error messages
2. Verify GameApplication extends Application
3. Verify javafx.run in pom.xml

---

## 📚 Document Quick Links

**Need to find something?**

- **"How do I do X?"** → DEVELOPMENT_GUIDE.md
- **"What does method Y do?"** → API_DOCUMENTATION.md
- **"What's my task today?"** → WEEK1_CHECKLIST.md + IMPLEMENTATION_PLAN.md
- **"What's our project status?"** → STATUS.md
- **"I have an idea for new feature"** → BACKLOG.md
- **"Which file should I read first?"** → DOCUMENTATION_INDEX.md

---

## 🎯 Success Checklist (By Day 6)

After Week 1, you should have:

- [x] ✅ Day 1: Foundation & game loop
- [ ] Day 2: Grid system from CSV
- [ ] Day 3: Enemy movement on paths
- [ ] Day 4: Tower placement & targeting
- [ ] Day 5: Combat & projectiles
- [ ] Day 6: Wave system & full gameplay

**Success:** Can place tower → tower fires → enemy dies → gold awarded → complete waves

---

## 💡 Pro Tips

1. **Use incremental commits** - Commit after each task (not at day end)
2. **Keep it simple first** - Placeholder graphics before pixel art
3. **Test frequently** - Build every 30 min, don't accumulate errors
4. **Reference docs** - API_DOCUMENTATION.md is your friend
5. **Track time** - Note actual vs estimated for better planning
6. **No scope creep** - New ideas → BACKLOG.md, not implemented immediately
7. **Read API docs** - Before using any class, read its section

---

## 🚀 Ready to Code?

1. ✅ Understand project structure (above)
2. ✅ Know today's task (IMPLEMENTATION_PLAN.md + WEEK1_CHECKLIST.md)
3. ✅ Have API docs open (API_DOCUMENTATION.md)
4. ✅ Build project (`mvn clean compile`)
5. ✅ Start coding!

---

## 📞 Reference Card

| Need | File |
|------|------|
| Current task | WEEK1_CHECKLIST.md |
| Method signature | API_DOCUMENTATION.md |
| Project plan | IMPLEMENTATION_PLAN.md |
| Coding standards | DEVELOPMENT_GUIDE.md |
| Build commands | README.md |
| Progress status | STATUS.md |
| Future ideas | BACKLOG.md |

---

**Version:** 1.0  
**Last Updated:** 2026-07-13  
**Next Milestone:** Day 6 (2026-07-19) - Core Mechanics Complete

🎮 **Happy coding! Reference the docs frequently!**
