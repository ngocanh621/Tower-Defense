# 🏰 Tower Defense 2D MVP

A 2D Tower Defense game built with Java 17+ and JavaFX 21.

## 🎮 About the Project

**Status:** MVP Development (Week 1)  
**Tech Stack:** Java 17+ · JavaFX 21 · Maven  
**Theme:** Medieval / Sci-fi Pixel Art  
**Timeline:** 3 weeks (18 working days)

## 📐 Project Structure

```
tower-defense/
├── src/main/java/com/game/
│   ├── core/           # Game loop, scene manager, main entry point
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
├── pom.xml
└── README.md
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build & Run

```bash
# Build the project
mvn clean compile

# Run the game
mvn javafx:run

# Package as executable JAR
mvn clean package
java -jar target/tower-defense-mvp.jar
```

## 📝 Development Notes

### Current Progress (Week 1 - Day 1)
- [x] Maven project setup
- [x] JavaFX dependencies configured
- [x] Module system setup (module-info.java)
- [x] Game loop foundation with AnimationTimer
- [x] Scene manager for menu/game transitions
- [x] Basic placeholder game scene

### Next Steps
- [ ] Implement Grid system (Day 2)
- [ ] Add pathfinding and Enemy movement (Day 3)
- [ ] Create Tower placement system (Day 4)
- [ ] Build combat and projectile system (Day 5)
- [ ] Implement wave system (Day 6)

## 🎮 Controls (Placeholder)
- **SPACE** - Start game from menu
- **ESC** - Return to menu / Exit
- **MOUSE** - Tower placement (coming soon)

## 📚 Design Patterns Used

1. **AnimationTimer** - 60 FPS game loop
2. **Object Pool** - For efficient projectile/particle management
3. **Observer/Event Bus** - For inter-system communication
4. **State Pattern** - For game and entity states
5. **Factory Pattern** - For entity creation

## 📄 License

This project is part of a learning initiative.

---

**Let's build an awesome Tower Defense game! 🚀**
