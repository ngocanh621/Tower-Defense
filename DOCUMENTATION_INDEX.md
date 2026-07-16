# 📖 DOCUMENTATION INDEX

Tất cả tài liệu dự án Tower Defense 2D MVP

---

## 📚 Tài Liệu Chính

### 1. **API_DOCUMENTATION.md** 📖
**Mục đích:** Tham khảo chi tiết về tất cả classes, methods, parameters, return values, side effects

**Nội dung:**
- `GameApplication` - Entry point
- `GameLoop` - 60 FPS loop engine
- `SceneManager` - Scene transitions
- `GameScene` - Game state & rendering
- `Entity` - Base class cho tất cả objects
- `EventBus` - Event publishing system
- `ObjectPool` - Generic object pooling
- `GameConfig` - Tất cả game constants
- `PlayerState` - Player HP/Gold manager
- `Cell` & `CellType` - Grid cell system

**Dùng khi nào:**
- Cần tìm signature của một method
- Muốn biết input/output của function
- Tìm hiểu vai trò của một class
- Cần design pattern reference

**Ngoài ra còn:**
- Usage examples cho từng hệ thống
- Design patterns overview
- Thread safety notes
- Performance considerations

---

### 2. **IMPLEMENTATION_PLAN.md** 📊
**Mục đích:** Kế hoạch chi tiết 18 ngày với task breakdown, timeline, dependencies

**Nội dung:**
- 4 phases (Foundation, Core Mechanics, Graphics, Balance)
- Day-by-day task list (Days 1-18)
- Mỗi task có:
  - Time estimate
  - Dependencies
  - Acceptance criteria
  - Git commit message
- Critical path analysis
- Risk management
- Success criteria
- Effort tracking table

**Dùng khi nào:**
- Lên plan ngày hôm nay
- Xác định task tiếp theo
- Kiểm tra dependencies
- Đánh giá progress

**Key Dates:**
- **Day 1** ✅ Foundation Complete
- **Day 6** Milestone: Core Mechanics Done
- **Day 12** Milestone: Graphics & Content Done
- **Day 18** Milestone: MVP Released

---

### 3. **API_DOCUMENTATION.md** 📚
Giải thích từng hàm, class, parameter chi tiết

**Classes Documented:**

| Class | Package | Vai Trò |
|-------|---------|---------|
| GameApplication | core | Main entry point |
| GameLoop | core | 60 FPS game loop |
| SceneManager | core | Scene transitions |
| GameScene | core | Game state & rendering |
| Entity | model | Base class |
| Poolable | model | Interface for pooling |
| EventBus | system | Event publishing |
| GameEvent | system | Event enum |
| ObjectPool | system | Generic object pool |
| Cell | map | Grid cell |
| CellType | map | Cell type enum |
| PlayerState | controller | Player state manager |
| GameConfig | util | Game constants |
| Constants | util | Visual/debug constants |

**Mỗi class document:**
- Constructor signature
- All public methods with:
  - Purpose (tác dụng)
  - Parameters (input)
  - Return value (output)
  - Side effects
  - Examples
  - Usage notes

---

## 📋 Tài Liệu Hỗ Trợ

### 4. **README.md** 📖
Getting started guide
- Project overview
- Build & run commands
- Development progress
- Controls

---

### 5. **DEVELOPMENT_GUIDE.md** 🛠️
Hướng dẫn phát triển cho developers
- Naming conventions
- Code organization
- Game loop pattern
- Event communication
- Object pooling
- Git commit messages
- Build & run commands
- Debugging tips
- Performance notes

---

### 6. **WEEK1_CHECKLIST.md** ✅
Checklist chi tiết tuần 1
- Day-by-day tasks
- Status tracking
- Key focus points
- Metrics table

---

### 7. **STATUS.md** 📊
Project status tracking
- Progress summary (%)
- Current sprint status
- Build status
- Gameplay features checklist
- Technical debt
- Notes

---

### 8. **BACKLOG.md** 📝
Future features & ideas
- Game features (post-MVP)
- Technical improvements
- Polish items
- Known bugs
- Future content packs

---

## 🗂️ File Organization

```
Tower-Defense/
├── Documentation/
│   ├── API_DOCUMENTATION.md          (THIS - API Reference)
│   ├── IMPLEMENTATION_PLAN.md        (18-day task plan)
│   ├── README.md                     (Getting started)
│   ├── DEVELOPMENT_GUIDE.md          (Dev guidelines)
│   ├── WEEK1_CHECKLIST.md           (Week 1 tasks)
│   ├── STATUS.md                     (Progress tracking)
│   ├── BACKLOG.md                    (Future features)
│   └── DOCUMENTATION_INDEX.md        (THIS FILE)
│
├── Source Code/
│   ├── src/main/java/com/game/
│   │   ├── core/                 (Game loop, scenes)
│   │   ├── model/                (Entities, poolable)
│   │   ├── system/               (EventBus, ObjectPool)
│   │   ├── map/                  (Grid, Cell)
│   │   ├── controller/           (PlayerState)
│   │   ├── view/                 (Rendering - TODO)
│   │   └── util/                 (Config, Constants)
│   │
│   └── src/main/resources/
│       ├── sprites/              (Pixel art - TODO)
│       ├── maps/                 (CSV/JSON files)
│       └── fxml/                 (UI layouts - TODO)
│
├── Configuration/
│   ├── pom.xml                   (Maven config)
│   ├── module-info.java          (Java modules)
│   └── .gitignore                (Git ignore)
│
└── Build/
    └── target/                   (Output, JARs - auto generated)
```

---

## 🔍 Quick Reference By Use Case

### "Tôi muốn tìm hiểu method X"
→ **API_DOCUMENTATION.md** (search for method name)

### "Tôi cần biết task tiếp theo là gì"
→ **IMPLEMENTATION_PLAN.md** → Current week section

### "Tôi muốn biết class Y được dùng như thế nào"
→ **API_DOCUMENTATION.md** → Usage Examples section

### "Tôi cần setup project lần đầu"
→ **README.md** → Getting Started

### "Tôi cần coding standards"
→ **DEVELOPMENT_GUIDE.md** → Naming Conventions, Code Organization

### "Tôi muốn thêm feature mới"
→ **BACKLOG.md** → ghi vào đó, không implement ngay

### "Tôi cần biết deadline"
→ **IMPLEMENTATION_PLAN.md** → Executive Summary, Dependencies section

### "Tôi cần debug issue"
→ **DEVELOPMENT_GUIDE.md** → Debugging Tips section

---

## 📊 Document Mapping

| Tài Liệu | Type | Scope | Audience | Update Frequency |
|---------|------|-------|----------|------------------|
| API_DOCUMENTATION | Reference | All Classes | Developers | When adding classes |
| IMPLEMENTATION_PLAN | Plan | Full project | Team | Daily (tasks) |
| README | Guide | Getting started | Everyone | As needed |
| DEVELOPMENT_GUIDE | Guidelines | Code standards | Developers | As needed |
| WEEK1_CHECKLIST | Checklist | Week 1 | Dev | Daily |
| STATUS | Tracking | All weeks | Team | Daily |
| BACKLOG | Ideas | Future | Product | Weekly |
| This File | Index | All docs | Everyone | As needed |

---

## 🚀 Reading Order for New Developers

1. **README.md** - Get oriented (5 min)
2. **DEVELOPMENT_GUIDE.md** - Learn standards (15 min)
3. **API_DOCUMENTATION.md** - Understand codebase (30 min)
4. **IMPLEMENTATION_PLAN.md** - Know the plan (20 min)
5. **Current task in WEEK1_CHECKLIST.md** - Start coding (ongoing)

---

## 📝 Document Maintenance

### What To Update When

**After implementing a new class:**
- [ ] Add to API_DOCUMENTATION.md
- [ ] Document all public methods
- [ ] Add usage example
- [ ] Update DEVELOPMENT_GUIDE.md if new pattern

**At end of each day:**
- [ ] Update WEEK1_CHECKLIST.md (task status)
- [ ] Update STATUS.md (progress %)
- [ ] Make Git commit with message from IMPLEMENTATION_PLAN.md
- [ ] Note any blockers/bugs

**Weekly (Friday):**
- [ ] Review BACKLOG.md
- [ ] Plan next week
- [ ] Update README if needed

---

## 🔗 Cross References

### Core Loop Flow
1. `GameApplication.start()` → 📖 API_DOCUMENTATION.md, "GameApplication" section
2. Calls `GameLoop.start()` → 📖 API_DOCUMENTATION.md, "GameLoop" section
3. Calls `GameScene.update()` & `.render()` → 📖 API_DOCUMENTATION.md, "GameScene" section

### Event Flow
1. Enemy dies
2. Publish `GameEvent.ENEMY_DIED` → 📖 API_DOCUMENTATION.md, "EventBus.publish()"
3. `PlayerState` subscribed → 📖 API_DOCUMENTATION.md, "PlayerState"
4. Calls `addGold()` → 📖 API_DOCUMENTATION.md, "PlayerState.addGold()"

### Object Pool Flow
1. Tower fires → acquire `Projectile` from pool
2. Calls `ObjectPool.acquire()` → 📖 API_DOCUMENTATION.md, "ObjectPool.acquire()"
3. Initialize & move projectile → 📖 API_DOCUMENTATION.md, "Entity" & "Projectile"
4. Hit enemy → `ObjectPool.release()` → 📖 API_DOCUMENTATION.md, "ObjectPool.release()"

---

## ✅ Checklist for Documentation

- [x] API_DOCUMENTATION.md - Complete
- [x] IMPLEMENTATION_PLAN.md - Complete  
- [x] README.md - Complete
- [x] DEVELOPMENT_GUIDE.md - Complete
- [x] WEEK1_CHECKLIST.md - Complete
- [x] STATUS.md - Complete
- [x] BACKLOG.md - Complete
- [x] DOCUMENTATION_INDEX.md (THIS) - Complete

---

## 📞 Questions?

- **"What does method X do?"** → API_DOCUMENTATION.md
- **"What's the deadline?"** → IMPLEMENTATION_PLAN.md
- **"What should I code today?"** → WEEK1_CHECKLIST.md + IMPLEMENTATION_PLAN.md
- **"What's the project status?"** → STATUS.md
- **"How should I name variables?"** → DEVELOPMENT_GUIDE.md
- **"I have a new idea!"** → BACKLOG.md

---

**Last Updated:** 2026-07-13  
**Maintained By:** Tower Defense Dev Team  
**Next Review:** 2026-07-19 (End of Week 1)

---

🎮 **Ready to start? Pick your task from WEEK1_CHECKLIST.md and reference API_DOCUMENTATION.md as needed!**
