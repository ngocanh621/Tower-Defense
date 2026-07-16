# 📚 API DOCUMENTATION - Tower Defense 2D MVP

**Version:** 1.0.0  
**Last Updated:** 2026-07-13  
**Author:** Tower Defense Development Team

---

## Table of Contents

1. [Core Package (`com.game.core`)](#core-package)
2. [Model Package (`com.game.model`)](#model-package)
3. [System Package (`com.game.system`)](#system-package)
4. [Map Package (`com.game.map`)](#map-package)
5. [Controller Package (`com.game.controller`)](#controller-package)
6. [Utility Package (`com.game.util`)](#utility-package)

---

## Core Package

### `GameApplication` (Main Entry Point)

**Location:** `src/main/java/com/game/core/GameApplication.java`

**Role:** Main application entry point that initializes JavaFX application, creates the primary stage, sets up scene manager, and starts the game loop.

#### Methods

##### `start(Stage primaryStage) : void`

**Purpose:** JavaFX lifecycle method called when application starts.

**Parameters:**
- `primaryStage: Stage` - The primary stage (window) for the application

**Return Value:** `void`

**Side Effects:**
- Sets window title to "Tower Defense 2D MVP"
- Creates window with dimensions 1280×720
- Initializes `SceneManager`
- Creates initial menu scene
- Starts `GameLoop`
- Sets window close handler

**Throws:** Exception (from JavaFX start signature)

**Example:**
```java
// Called automatically by JavaFX
// Executed from main()
```

---

##### `main(String[] args) : void`

**Purpose:** Program entry point, launches JavaFX application.

**Parameters:**
- `args: String[]` - Command-line arguments (unused)

**Return Value:** `void`

**Side Effects:**
- Calls `launch(args)` which triggers `start(Stage)`

**Example:**
```bash
java com.game.core.GameApplication
```

---

### `GameLoop` (60 FPS Update/Render Loop)

**Location:** `src/main/java/com/game/core/GameLoop.java`

**Role:** Core game loop running at ~60 FPS using JavaFX's `AnimationTimer`. Manages delta time calculation and delegates update/render to scene manager.

#### Constants

```java
TARGET_FPS = 60.0              // Target frames per second
FRAME_TIME = 1/60.0 ≈ 0.01667 // Expected frame time in seconds
```

#### Methods

##### `GameLoop(SceneManager sceneManager) : constructor`

**Purpose:** Initialize game loop with a scene manager reference.

**Parameters:**
- `sceneManager: SceneManager` - The scene manager to delegate updates to

**Side Effects:**
- Stores scene manager reference
- Creates `AnimationTimer` instance
- Sets `lastFrameTime` to 0

**Example:**
```java
SceneManager sceneManager = new SceneManager(primaryStage);
GameLoop gameLoop = new GameLoop(sceneManager);
gameLoop.start();
```

---

##### `start() : void`

**Purpose:** Start the game loop (begins calling update/render every frame).

**Parameters:** None

**Return Value:** `void`

**Side Effects:**
- Starts `animationTimer`
- Prints "GameLoop started" to console

**Thread Safety:** Must be called from FX application thread

**Example:**
```java
gameLoop.start();
```

---

##### `stop() : void`

**Purpose:** Stop the game loop (pauses update/render cycle).

**Parameters:** None

**Return Value:** `void`

**Side Effects:**
- Stops `animationTimer`
- Prints "GameLoop stopped" to console

**Example:**
```java
gameLoop.stop();
```

---

##### `update(double deltaTime) : void` (Private)

**Purpose:** Update game logic for current frame by delegating to current scene.

**Parameters:**
- `deltaTime: double` - Time elapsed since last frame in seconds

**Return Value:** `void`

**Side Effects:**
- Calls `currentGame.update(deltaTime)` if game scene exists

**Note:** Private helper, not intended for external use

---

##### `render() : void` (Private)

**Purpose:** Render current frame by delegating to current scene.

**Parameters:** None

**Return Value:** `void`

**Side Effects:**
- Calls `currentGame.render()` if game scene exists

**Note:** Private helper, not intended for external use

---

### `SceneManager` (Scene Transition Manager)

**Location:** `src/main/java/com/game/core/SceneManager.java`

**Role:** Manages transitions between different game scenes (Menu, Game, GameOver). Provides factory methods for creating scenes and handles scene switching.

#### Methods

##### `SceneManager(Stage primaryStage) : constructor`

**Purpose:** Initialize scene manager with primary stage reference.

**Parameters:**
- `primaryStage: Stage` - The window to manage scenes for

**Side Effects:**
- Stores stage reference
- Sets `currentGame` to null

**Example:**
```java
SceneManager sceneManager = new SceneManager(primaryStage);
```

---

##### `createMenuScene() : Scene`

**Purpose:** Create and return the main menu scene with basic UI and input handlers.

**Parameters:** None

**Return Value:** `Scene` - The created menu scene

**Side Effects:**
- Creates new BorderPane with canvas
- Draws menu UI (title, instructions)
- Registers key press handlers (SPACE to start, ESC to exit)
- Returns new Scene

**Key Handlers:**
- `SPACE` - Calls `switchToGameScene()`
- `ESC` - Exits application

**Example:**
```java
Scene menuScene = sceneManager.createMenuScene();
primaryStage.setScene(menuScene);
```

---

##### `createGameScene() : Scene`

**Purpose:** Create and return the main game scene with rendering canvas and input handlers.

**Parameters:** None

**Return Value:** `Scene` - The created game scene

**Side Effects:**
- Creates new `GameScene` instance
- Sets `currentGame` to new game scene
- Registers input handlers (keyboard, mouse)
- Returns new Scene

**Input Handlers:**
- `ESC` key - Returns to menu
- `Mouse click` - Delegates to `GameScene.handleMouseClick()`
- `Mouse move` - Delegates to `GameScene.handleMouseMove()`
- Other keys - Delegates to `GameScene.handleKeyPress()`

**Example:**
```java
Scene gameScene = sceneManager.createGameScene();
primaryStage.setScene(gameScene);
```

---

##### `switchToMenuScene() : void`

**Purpose:** Switch application to main menu scene.

**Parameters:** None

**Return Value:** `void`

**Side Effects:**
- Calls `createMenuScene()` to get new menu scene
- Sets primaryStage scene to menu scene
- Sets `currentGame` to null

**Example:**
```java
sceneManager.switchToMenuScene();
```

---

##### `switchToGameScene() : void`

**Purpose:** Switch application to game scene.

**Parameters:** None

**Return Value:** `void`

**Side Effects:**
- Calls `createGameScene()` to get new game scene
- Sets primaryStage scene to game scene
- Sets `currentGame` to new game scene

**Example:**
```java
sceneManager.switchToGameScene();
```

---

##### `getCurrentGame() : GameScene`

**Purpose:** Get reference to currently active game scene.

**Parameters:** None

**Return Value:** `GameScene` - The current game scene, or null if on menu

**Usage:** Used by GameLoop to get scene to update/render

**Example:**
```java
GameScene current = sceneManager.getCurrentGame();
if (current != null) {
    current.update(deltaTime);
}
```

---

### `GameScene` (Game State & Rendering)

**Location:** `src/main/java/com/game/core/GameScene.java`

**Role:** Represents a single game play session. Manages update logic and rendering for the game board, entities, and UI.

#### Methods

##### `GameScene(Canvas canvas, GraphicsContext gc) : constructor`

**Purpose:** Initialize game scene with rendering canvas and graphics context.

**Parameters:**
- `canvas: Canvas` - The JavaFX canvas to render on
- `gc: GraphicsContext` - The graphics context for drawing

**Side Effects:**
- Stores canvas and graphics context references

**Example:**
```java
Canvas gameCanvas = new Canvas(1280, 720);
GraphicsContext gc = gameCanvas.getGraphicsContext2D();
GameScene scene = new GameScene(gameCanvas, gc);
```

---

##### `update(double deltaTime) : void`

**Purpose:** Update all game logic for the current frame.

**Parameters:**
- `deltaTime: double` - Time elapsed since last frame in seconds

**Return Value:** `void`

**Responsibilities (To Be Implemented):**
- Update grid state
- Update enemies (movement, damage)
- Update towers (targeting, firing)
- Update projectiles (movement, collision)
- Check win/lose conditions
- Handle event publications

**Example:**
```java
scene.update(0.01667); // Called every frame
```

---

##### `render() : void`

**Purpose:** Render the complete game frame to canvas.

**Parameters:** None

**Return Value:** `void`

**Responsibilities:**
- Clear canvas
- Draw grid cells
- Draw towers
- Draw enemies
- Draw projectiles
- Draw HUD (health, gold, wave info)

**Side Effects:**
- Modifies canvas content via graphics context

**Performance Note:** Called ~60 times per second

**Example:**
```java
scene.render(); // Called every frame after update()
```

---

##### `handleKeyPress(KeyEvent event) : void`

**Purpose:** Handle keyboard input during gameplay.

**Parameters:**
- `event: KeyEvent` - The key press event

**Return Value:** `void`

**To Be Implemented:**
- Tower selection (number keys 1-9?)
- Tower rotation/placement preview
- Speed up/slow down game
- Toggle pause

**Example:**
```java
// Triggered when user presses key in game scene
scene.handleKeyPress(event);
```

---

##### `handleMouseClick(MouseEvent event) : void`

**Purpose:** Handle mouse click events during gameplay.

**Parameters:**
- `event: MouseEvent` - The mouse click event

**Return Value:** `void`

**To Be Implemented:**
- Tower placement at clicked position
- Tower selection/deselection
- Tower upgrades/selling

**Example:**
```java
// Triggered when user clicks in game scene
scene.handleMouseClick(event);
```

---

##### `handleMouseMove(MouseEvent event) : void`

**Purpose:** Handle mouse movement events during gameplay.

**Parameters:**
- `event: MouseEvent` - The mouse move event

**Return Value:** `void`

**To Be Implemented:**
- Tower placement preview (ghost tower following cursor)
- Cell highlighting
- Tower range preview display

**Example:**
```java
// Triggered when user moves mouse in game scene
scene.handleMouseMove(event);
```

---

##### `drawPlaceholderGrid() : void` (Private)

**Purpose:** Draw a placeholder grid for development testing.

**Parameters:** None

**Return Value:** `void`

**Side Effects:**
- Draws grid lines on canvas
- Draws UI text overlay

**Note:** Temporary method for development; will be replaced by actual grid rendering

---

## Model Package

### `Poolable` (Interface)

**Location:** `src/main/java/com/game/model/Poolable.java`

**Role:** Interface for objects that can be recycled through ObjectPool. Enables efficient memory reuse.

#### Methods

##### `reset() : void`

**Purpose:** Reset object to initial/empty state for reuse.

**Parameters:** None

**Return Value:** `void`

**Implementers Must:** Clear all object state (position, target, damage, etc.)

**Example:**
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

**Purpose:** Check if this object is currently in use.

**Parameters:** None

**Return Value:** `boolean` - true if object is active, false if pooled

**Example:**
```java
if (projectile.isActive()) {
    projectile.update(deltaTime);
}
```

---

##### `setActive(boolean active) : void`

**Purpose:** Set the active state of this object.

**Parameters:**
- `active: boolean` - true to mark active, false to mark as available for pooling

**Return Value:** `void`

**Example:**
```java
projectile.setActive(true);  // Mark as in use
projectile.setActive(false); // Mark as available for pool
```

---

### `Entity` (Abstract Base Class)

**Location:** `src/main/java/com/game/model/Entity.java`

**Role:** Base class for all game objects with position, size, and basic lifecycle. Parent for Tower, Enemy, and Projectile.

#### Fields

```java
protected float x           // X position (pixels)
protected float y           // Y position (pixels)
protected float width       // Width (pixels)
protected float height      // Height (pixels)
protected boolean active    // Whether this entity is active
```

#### Methods

##### `Entity(float x, float y, float width, float height) : constructor`

**Purpose:** Initialize entity with position and dimensions.

**Parameters:**
- `x: float` - X position in pixels
- `y: float` - Y position in pixels
- `width: float` - Width in pixels
- `height: float` - Height in pixels

**Side Effects:**
- Sets position and dimensions
- Sets `active` to true

**Example:**
```java
Enemy goblin = new Goblin(100, 50, 32, 32);
```

---

##### `update(double deltaTime) : void` (Abstract)

**Purpose:** Update entity logic for current frame.

**Parameters:**
- `deltaTime: double` - Time elapsed since last frame in seconds

**Return Value:** `void`

**Contract:** Every entity must implement this method

**Example:**
```java
// Implemented by Enemy, Tower, Projectile, etc.
enemy.update(deltaTime);
```

---

##### `isActive() : boolean`

**Purpose:** Check if entity is active.

**Parameters:** None

**Return Value:** `boolean` - Active state

**Example:**
```java
if (entity.isActive()) {
    entity.update(deltaTime);
}
```

---

##### `setActive(boolean active) : void`

**Purpose:** Set entity active state.

**Parameters:**
- `active: boolean` - New active state

**Return Value:** `void`

**Example:**
```java
entity.setActive(false); // Deactivate
```

---

##### `distanceTo(Entity other) : float`

**Purpose:** Calculate distance to another entity.

**Parameters:**
- `other: Entity` - The other entity

**Return Value:** `float` - Distance in pixels

**Formula:** `sqrt((x1-x2)² + (y1-y2)²)`

**Example:**
```java
float distance = tower.distanceTo(enemy);
if (distance < tower.getRange()) {
    tower.fire(enemy);
}
```

---

##### `overlaps(Entity other) : boolean`

**Purpose:** Check if this entity's bounding box overlaps with another's.

**Parameters:**
- `other: Entity` - The other entity

**Return Value:** `boolean` - true if bounding boxes overlap

**Example:**
```java
if (projectile.overlaps(enemy)) {
    projectile.hit(enemy);
}
```

---

##### Getters and Setters

```java
getX() : float              // Get X position
setX(float x) : void        // Set X position
getY() : float              // Get Y position
setY(float y) : void        // Set Y position
getWidth() : float          // Get width
setWidth(float width) : void
getHeight() : float         // Get height
setHeight(float height) : void
```

---

## System Package

### `GameEvent` (Enum)

**Location:** `src/main/java/com/game/system/GameEvent.java`

**Role:** Enumeration of all possible game events for event bus publishing/subscription.

#### Events

```java
ENEMY_DIED              // Published when enemy HP reaches 0
ENEMY_REACHED_BASE      // Published when enemy reaches player base
WAVE_STARTED            // Published at start of new wave
WAVE_COMPLETED          // Published when all enemies in wave defeated
TOWER_PLACED            // Published when tower is placed
TOWER_REMOVED           // Published when tower is sold
PLAYER_GOLD_CHANGED     // Published when player gold amount changes
PLAYER_HP_CHANGED       // Published when player health changes
GAME_OVER               // Published when player loses
GAME_WON                // Published when player wins
```

---

### `EventBus` (Singleton)

**Location:** `src/main/java/com/game/system/EventBus.java`

**Role:** Central event distribution system. Enables loose coupling by allowing systems to communicate via events instead of direct references.

#### Methods

##### `getInstance() : EventBus` (Static)

**Purpose:** Get singleton instance of EventBus.

**Parameters:** None

**Return Value:** `EventBus` - The singleton instance

**Thread Safety:** Safe for concurrent access (class loading guarantees)

**Example:**
```java
EventBus bus = EventBus.getInstance();
```

---

##### `subscribe(GameEvent event, Consumer<Object> listener) : void`

**Purpose:** Subscribe to a game event.

**Parameters:**
- `event: GameEvent` - The event to subscribe to
- `listener: Consumer<Object>` - Callback function when event is published

**Return Value:** `void`

**Side Effects:**
- Adds listener to the event's subscriber list

**Example:**
```java
EventBus.getInstance().subscribe(GameEvent.ENEMY_DIED, e -> {
    Enemy enemy = (Enemy) e;
    playerState.addGold(enemy.getReward());
});
```

---

##### `unsubscribe(GameEvent event, Consumer<Object> listener) : void`

**Purpose:** Unsubscribe from a game event.

**Parameters:**
- `event: GameEvent` - The event to unsubscribe from
- `listener: Consumer<Object>` - The listener to remove

**Return Value:** `void`

**Side Effects:**
- Removes listener from event's subscriber list

**Example:**
```java
EventBus.getInstance().unsubscribe(GameEvent.WAVE_STARTED, myListener);
```

---

##### `publish(GameEvent event, Object data) : void`

**Purpose:** Publish event to all subscribers with data payload.

**Parameters:**
- `event: GameEvent` - The event type
- `data: Object` - Event data (can be null)

**Return Value:** `void`

**Side Effects:**
- Calls all registered listeners for this event
- Wraps exceptions from listeners so one failed listener doesn't stop others

**Error Handling:** Catches and logs exceptions from individual listeners

**Example:**
```java
EventBus.getInstance().publish(GameEvent.ENEMY_DIED, deadEnemy);
```

---

##### `publish(GameEvent event) : void`

**Purpose:** Publish event to all subscribers without data.

**Parameters:**
- `event: GameEvent` - The event type

**Return Value:** `void`

**Convenience Method:** Calls `publish(event, null)`

**Example:**
```java
EventBus.getInstance().publish(GameEvent.WAVE_COMPLETED);
```

---

##### `clear() : void`

**Purpose:** Clear all event listeners (useful for testing/cleanup).

**Parameters:** None

**Return Value:** `void`

**Side Effects:**
- Removes all subscribers from all events

**Example:**
```java
EventBus.getInstance().clear(); // For testing
```

---

##### `clear(GameEvent event) : void`

**Purpose:** Clear listeners for specific event.

**Parameters:**
- `event: GameEvent` - Event to clear

**Return Value:** `void`

**Side Effects:**
- Removes all subscribers from specified event

**Example:**
```java
EventBus.getInstance().clear(GameEvent.WAVE_STARTED);
```

---

### `ObjectPool<T>` (Generic)

**Location:** `src/main/java/com/game/system/ObjectPool.java`

**Role:** Generic object pool for reusing instances and reducing garbage collection pressure. Ideal for projectiles, particles, and other frequently created/destroyed objects.

#### Type Parameter

```java
<T extends Poolable>  // Generic type must implement Poolable interface
```

#### Methods

##### `ObjectPool(Supplier<T> factory, int initialSize) : constructor`

**Purpose:** Create a new object pool with pre-allocated instances.

**Parameters:**
- `factory: Supplier<T>` - Factory function to create new instances
- `initialSize: int` - Number of objects to pre-allocate

**Side Effects:**
- Creates initial pool of objects
- Stores factory reference for creating additional objects as needed

**Example:**
```java
ObjectPool<Projectile> projectilePool = new ObjectPool<>(
    () -> new Projectile(),  // Factory
    100                       // Pre-allocate 100 projectiles
);
```

---

##### `acquire() : T`

**Purpose:** Get an object from the pool.

**Parameters:** None

**Return Value:** `T` - An object from the pool (or newly created if pool is empty)

**Side Effects:**
- Marks object as active
- Adds to inUse set
- Creates new object if pool is empty (automatic expansion)

**Example:**
```java
Projectile p = projectilePool.acquire();
p.initialize(tower, target);
```

---

##### `release(T obj) : void`

**Purpose:** Return an object to the pool for reuse.

**Parameters:**
- `obj: T` - The object to return

**Return Value:** `void`

**Side Effects:**
- Calls `obj.reset()` to clear state
- Marks object as inactive
- Removes from inUse set
- Adds to available queue

**Example:**
```java
projectilePool.release(projectile);
```

---

##### `getAvailableCount() : int`

**Purpose:** Get number of available objects in pool.

**Parameters:** None

**Return Value:** `int` - Count of unused objects

**Example:**
```java
int available = projectilePool.getAvailableCount();
```

---

##### `getInUseCount() : int`

**Purpose:** Get number of objects currently in use.

**Parameters:** None

**Return Value:** `int` - Count of active objects

**Example:**
```java
int inUse = projectilePool.getInUseCount();
```

---

##### `getTotalCount() : int`

**Purpose:** Get total objects in pool (available + in use).

**Parameters:** None

**Return Value:** `int` - Total count

**Example:**
```java
int total = projectilePool.getTotalCount();
```

---

##### `expand(int count) : void`

**Purpose:** Add more objects to the pool.

**Parameters:**
- `count: int` - Number of objects to create

**Return Value:** `void`

**Side Effects:**
- Creates new objects using factory
- Adds them to available queue

**Example:**
```java
projectilePool.expand(50); // Add 50 more objects
```

---

##### `clear() : void`

**Purpose:** Clear entire pool.

**Parameters:** None

**Return Value:** `void`

**Side Effects:**
- Empties available queue
- Empties inUse set

**Example:**
```java
projectilePool.clear(); // For testing/cleanup
```

---

## Map Package

### `CellType` (Enum)

**Location:** `src/main/java/com/game/map/CellType.java`

**Role:** Enumeration of different grid cell types.

#### Cell Types

```java
EMPTY       // Can place towers
PATH        // Enemy path
OCCUPIED    // Already has a tower
SPAWN       // Enemy spawn point
BASE        // Player base
```

---

### `Cell` (Grid Cell)

**Location:** `src/main/java/com/game/map/Cell.java`

**Role:** Represents a single cell in the game grid with position and type information.

#### Fields

```java
final int row           // Row index
final int col           // Column index
CellType type           // Cell type
```

#### Methods

##### `Cell(int row, int col, CellType type) : constructor`

**Purpose:** Create a grid cell.

**Parameters:**
- `row: int` - Row index (0-based)
- `col: int` - Column index (0-based)
- `type: CellType` - Initial cell type

**Example:**
```java
Cell cell = new Cell(5, 10, CellType.EMPTY);
```

---

##### `getRow() : int`

**Purpose:** Get row index.

**Return Value:** `int` - Row (0-based)

---

##### `getCol() : int`

**Purpose:** Get column index.

**Return Value:** `int` - Column (0-based)

---

##### `getType() : CellType`

**Purpose:** Get cell type.

**Return Value:** `CellType` - Current cell type

---

##### `setType(CellType type) : void`

**Purpose:** Set cell type (e.g., place tower).

**Parameters:**
- `type: CellType` - New type

**Example:**
```java
cell.setType(CellType.OCCUPIED); // Tower placed
```

---

##### `canPlaceTower() : boolean`

**Purpose:** Check if tower can be placed on this cell.

**Parameters:** None

**Return Value:** `boolean` - true if cell is EMPTY, false otherwise

**Example:**
```java
if (cell.canPlaceTower()) {
    // Place tower
}
```

---

## Controller Package

### `PlayerState` (Game State Manager)

**Location:** `src/main/java/com/game/controller/PlayerState.java`

**Role:** Manages player's health and gold, publishes state change events.

#### Fields

```java
int health          // Current health
int maxHealth        // Maximum health (immutable)
int gold            // Current gold amount
```

#### Methods

##### `PlayerState(int initialHealth, int initialGold) : constructor`

**Purpose:** Create player state with initial values.

**Parameters:**
- `initialHealth: int` - Starting health (also becomes maxHealth)
- `initialGold: int` - Starting gold amount

**Example:**
```java
PlayerState player = new PlayerState(20, 100);
```

---

##### `takeDamage(int damage) : void`

**Purpose:** Reduce player health, publish event, check for game over.

**Parameters:**
- `damage: int` - Damage amount to take

**Return Value:** `void`

**Side Effects:**
- Reduces health (minimum 0)
- Publishes `PLAYER_HP_CHANGED` event
- Publishes `GAME_OVER` event if health reaches 0

**Example:**
```java
player.takeDamage(5); // Take 5 damage
```

---

##### `addGold(int amount) : void`

**Purpose:** Add gold to player, publish event.

**Parameters:**
- `amount: int` - Gold to add

**Return Value:** `void`

**Side Effects:**
- Increases gold
- Publishes `PLAYER_GOLD_CHANGED` event

**Example:**
```java
player.addGold(25); // Gain 25 gold from enemy
```

---

##### `spendGold(int amount) : boolean`

**Purpose:** Attempt to spend gold (e.g., buying tower).

**Parameters:**
- `amount: int` - Gold to spend

**Return Value:** `boolean` - true if enough gold, false otherwise

**Side Effects:**
- If successful: reduces gold, publishes `PLAYER_GOLD_CHANGED` event
- If failed: no changes

**Example:**
```java
if (player.spendGold(100)) {
    // Successfully bought tower
} else {
    // Not enough gold
}
```

---

##### `getHealth() : int`

**Purpose:** Get current health.

**Return Value:** `int` - Current health

---

##### `getMaxHealth() : int`

**Purpose:** Get maximum health.

**Return Value:** `int` - Max health

---

##### `getGold() : int`

**Purpose:** Get current gold.

**Return Value:** `int` - Current gold

---

##### `setHealth(int health) : void`

**Purpose:** Directly set health (use with caution).

**Parameters:**
- `health: int` - New health value

---

##### `setGold(int gold) : void`

**Purpose:** Directly set gold (use with caution).

**Parameters:**
- `gold: int` - New gold value

---

##### `isAlive() : boolean`

**Purpose:** Check if player is still alive.

**Parameters:** None

**Return Value:** `boolean` - true if health > 0

**Example:**
```java
if (!player.isAlive()) {
    gameScene.gameOver();
}
```

---

## Utility Package

### `GameConfig` (Configuration Constants)

**Location:** `src/main/java/com/game/util/GameConfig.java`

**Role:** Central repository for all game configuration values. Modify these constants to tune gameplay.

#### Window Settings

```java
WINDOW_WIDTH = 1280           // pixels
WINDOW_HEIGHT = 720           // pixels
WINDOW_TITLE = "Tower Defense 2D MVP"
```

#### Game Settings

```java
TARGET_FPS = 60.0             // Frames per second
FRAME_TIME = 1/60 ≈ 0.01667   // Seconds per frame
```

#### Grid Settings

```java
GRID_CELL_SIZE = 40           // Pixels per cell
GRID_WIDTH = 32               // Cells (1280/40)
GRID_HEIGHT = 18              // Cells (720/40)
```

#### Player Settings

```java
STARTING_HEALTH = 20          // Initial health
STARTING_GOLD = 100           // Initial gold
```

#### Tower Settings (GunTower)

```java
TOWER_GUN_COST = 100f         // Gold cost
TOWER_GUN_RANGE = 150f        // Pixels
TOWER_GUN_FIRE_RATE = 1.0f    // Shots per second
```

#### Tower Settings (SlowTower)

```java
TOWER_SLOW_COST = 150f        // Gold cost
TOWER_SLOW_RANGE = 200f       // Pixels
TOWER_SLOW_FIRE_RATE = 0.5f   // Shots per second
```

#### Enemy Settings (Goblin)

```java
ENEMY_GOBLIN_HP = 50f         // Health points
ENEMY_GOBLIN_SPEED = 120f     // Pixels per second
ENEMY_GOBLIN_REWARD = 10      // Gold on death
```

#### Enemy Settings (Orc)

```java
ENEMY_ORC_HP = 200f           // Health points
ENEMY_ORC_SPEED = 60f         // Pixels per second
ENEMY_ORC_REWARD = 25         // Gold on death
```

#### Enemy Settings (Dragon)

```java
ENEMY_DRAGON_HP = 500f        // Health points
ENEMY_DRAGON_SPEED = 40f      // Pixels per second
ENEMY_DRAGON_REWARD = 60      // Gold on death
```

#### Projectile Settings

```java
PROJECTILE_SPEED = 500f       // Pixels per second
PROJECTILE_DAMAGE = 10        // Damage per hit
```

#### Wave Settings

```java
WAVE_SPAWN_INTERVAL = 0.5f    // Seconds between enemy spawns
WAVE_DELAY = 10f              // Seconds between waves
```

**Note:** Static class, cannot be instantiated

---

### `Constants` (Visual & Debug Constants)

**Location:** `src/main/java/com/game/util/Constants.java`

**Role:** Runtime visual and debugging constants.

#### Colors (Hex Strings)

```java
COLOR_BACKGROUND = "#0a0e27"      // Dark blue background
COLOR_GRID = "#333333"             // Dark grid lines
COLOR_PATH = "#556677"             // Path color
COLOR_EMPTY = "#1a1a2e"            // Empty cell color
COLOR_TOWER_SELECTED = "#ffff00"   // Yellow highlight
```

#### UI Constants

```java
HUD_PADDING = 10                   // Pixels
HUD_FONT_SIZE = 16                 // Points
```

#### Animation

```java
SPRITE_FRAME_TIME = 100            // Milliseconds per sprite frame
```

#### Physics

```java
GRAVITY = 0f                       // No gravity (2D top-down)
```

#### Debug Flags

```java
DEBUG_MODE = true                  // Enable/disable all debug features
DEBUG_GRID = true                  // Show grid overlay
DEBUG_COLLISION = false            // Show collision boxes
```

**Note:** Static class, cannot be instantiated

---

## Future Classes (To Be Implemented)

### Tower System

**Classes to Implement:**
- `Tower` (abstract base class)
- `GunTower` (fast firing, low damage)
- `SlowTower` (slow firing, applies debuff)
- `TowerFactory` (creates towers)

---

### Enemy System

**Classes to Implement:**
- `Enemy` (abstract base class)
- `Goblin` (fast, low HP)
- `Orc` (slow, high HP)
- `Dragon` (very slow, very high HP, slow-immune)
- `EnemyFactory` (creates enemies)

---

### Combat System

**Classes to Implement:**
- `Projectile` (implements Poolable)
- `DamageNumber` (floating damage text)

---

### Map System

**Classes to Implement:**
- `Grid` (N×M cell grid)
- `MapManager` (loads maps from CSV)

---

### Wave System

**Classes to Implement:**
- `Wave` (wave configuration)
- `WaveManager` (manages wave progression)

---

### View System

**Classes to Implement:**
- `HUD` (renders player UI)
- `SpriteSheet` (loads/renders sprite graphics)
- `AnimationPlayer` (plays sprite animations)

---

## Usage Examples

### Example 1: Subscribing to Events

```java
// In some initialization code:
EventBus bus = EventBus.getInstance();

bus.subscribe(GameEvent.ENEMY_DIED, (data) -> {
    Enemy enemy = (Enemy) data;
    playerState.addGold(enemy.getReward());
    System.out.println("Enemy died, gained " + enemy.getReward() + " gold");
});
```

### Example 2: Using Object Pool

```java
// Setup
ObjectPool<Projectile> projectilePool = new ObjectPool<>(
    Projectile::new,
    100
);

// Firing
Projectile p = projectilePool.acquire();
p.initialize(tower.getX(), tower.getY(), target);

// On hit
projectilePool.release(p);
```

### Example 3: Entity Distance Check

```java
float distance = tower.distanceTo(enemy);
if (distance < tower.getRange()) {
    tower.fire(enemy);
}
```

### Example 4: Player State Management

```java
// Player takes damage
player.takeDamage(5);

// Player buys tower
if (player.spendGold(100)) {
    // Tower placed successfully
}

// Check if alive
if (!player.isAlive()) {
    sceneManager.switchToMenuScene();
}
```

---

## Design Patterns Used

| Pattern | Location | Purpose |
|---------|----------|---------|
| **Singleton** | `EventBus`, `GameLoop` | Single shared instance |
| **Observer** | `EventBus` | Event publishing/subscription |
| **Object Pool** | `ObjectPool<T>` | Reuse objects, reduce GC |
| **Factory** | Will use: `TowerFactory`, `EnemyFactory` | Create entities |
| **Abstract Base** | `Entity` | Common interface for game objects |
| **State Pattern** | Will use: `EnemyState`, `TowerState` | Manage entity states |

---

## Thread Safety Notes

- **AnimationTimer:** All code runs in FX application thread (thread-safe by design)
- **EventBus:** Safe for concurrent access; subscribers called synchronously
- **ObjectPool:** Not thread-safe; only use in single-threaded game loop

---

## Performance Considerations

- **Object Pool:** Use for Projectile (1000+/game), Particles, DamageNumbers
- **Entity Updates:** O(n) where n is number of entities; optimize with spatial partitioning if > 1000
- **Rendering:** Batch draw operations; single Canvas per scene
- **Event Publishing:** O(m) where m is number of subscribers; keep subscriber count low

---

*Last Updated: 2026-07-13 | Version 1.0*
