package com.game.controller;

import com.game.map.MapModel;
import com.game.model.Enemy;
import com.game.model.Enemy.EnemyType;
import com.game.system.EventBus;
import com.game.system.GameEvent;
import com.game.util.GameConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Quản lý các đợt sóng quái vật vô tận (Endless Wave Manager).
 * Tự động tăng độ khó và danh sách quái theo đợt sóng không giới hạn.
 */
public class WaveManager {

    /**
     * Đại diện cho cấu hình danh sách quái của một đợt sóng.
     */
    public static class Wave {
        private final List<EnemyType> enemiesToSpawn;

        public Wave(List<EnemyType> enemiesToSpawn) {
            this.enemiesToSpawn = enemiesToSpawn;
        }

        public List<EnemyType> getEnemiesToSpawn() {
            return enemiesToSpawn;
        }
    }

    private final List<Wave> predefinedWaves = new ArrayList<>();
    private int currentWaveIndex = 0;
    private final Queue<EnemyType> currentSpawnQueue = new LinkedList<>();

    private float spawnTimer = 0f;
    private float waveDelayTimer = 0f;
    private boolean waveInProgress = false;

    private final float spawnInterval;
    private final float waveDelay;

    /**
     * Khởi tạo WaveManager cho chế độ chơi vô tận.
     */
    public WaveManager() {
        this.spawnInterval = GameConfig.WAVE_SPAWN_INTERVAL;
        this.waveDelay = GameConfig.WAVE_DELAY;
        setupDefaultWaves();
        this.waveDelayTimer = 3.0f; // Chờ 3 giây trước đợt sóng đầu tiên
    }

    /**
     * Khởi tạo các đợt sóng cơ bản ban đầu.
     */
    private void setupDefaultWaves() {
        predefinedWaves.add(createWave(5, 0, 0));
        predefinedWaves.add(createWave(8, 2, 0));
        predefinedWaves.add(createWave(5, 5, 1));
        predefinedWaves.add(createWave(0, 10, 3));
        predefinedWaves.add(createWave(15, 10, 5));
    }

    /**
     * Tạo đợt sóng quái vật dựa trên số lượng từng loại.
     */
    private Wave createWave(int goblins, int orcs, int dragons) {
        List<EnemyType> list = new ArrayList<>();
        for (int i = 0; i < goblins; i++) {
            list.add(EnemyType.GOBLIN);
        }
        for (int i = 0; i < orcs; i++) {
            list.add(EnemyType.ORC);
        }
        for (int i = 0; i < dragons; i++) {
            list.add(EnemyType.DRAGON);
        }
        return new Wave(list);
    }

    /**
     * Lấy cấu hình sóng quái. Sinh động độ khó tăng dần nếu vượt quá số sóng mặc định.
     */
    private Wave getOrCreateWave(int waveIndex) {
        if (waveIndex < predefinedWaves.size()) {
            return predefinedWaves.get(waveIndex);
        }
        // Công thức tự động tăng độ khó cho chế độ vô tận (Endless Mode)
        int goblins = 5 + (waveIndex * 2);
        int orcs = Math.max(0, (waveIndex - 1) * 2);
        int dragons = Math.max(0, (waveIndex - 3) * 1);
        return createWave(goblins, orcs, dragons);
    }

    /**
     * Cập nhật logic sóng quái theo thời gian thực.
     */
    public void update(double deltaTime, MapModel mapModel, List<Enemy> activeEnemies) {
        // Đếm ngược thời gian nghỉ trước đợt sóng tiếp theo
        if (!waveInProgress) {
            waveDelayTimer -= deltaTime;
            if (waveDelayTimer <= 0) {
                startNextWave();
            }
            return;
        }

        // Đang diễn ra đợt sóng: Sinh quái theo từng khoảng thời gian
        if (!currentSpawnQueue.isEmpty()) {
            spawnTimer -= deltaTime;
            if (spawnTimer <= 0) {
                spawnTimer = spawnInterval;
                EnemyType enemyType = currentSpawnQueue.poll();
                if (enemyType != null) {
                    Enemy enemy = new Enemy();
                    enemy.initialize(enemyType, mapModel);
                    activeEnemies.add(enemy);
                }
            }
        } else {
            // Đã sinh hết quái và tất cả quái trên bản đồ đã bị tiêu diệt hoặc chạm đích
            if (activeEnemies.isEmpty()) {
                waveInProgress = false;
                int completedWaveNumber = currentWaveIndex + 1;
                EventBus.getInstance().publish(GameEvent.WAVE_COMPLETED, completedWaveNumber);

                currentWaveIndex++;
                waveDelayTimer = waveDelay; // Bắt đầu thời gian chờ chuyển sang sóng tiếp theo
            }
        }
    }

    /**
     * Khởi động đợt sóng tiếp theo.
     */
    public void startNextWave() {
        waveInProgress = true;
        Wave wave = getOrCreateWave(currentWaveIndex);
        currentSpawnQueue.clear();
        currentSpawnQueue.addAll(wave.getEnemiesToSpawn());
        spawnTimer = 0f;

        EventBus.getInstance().publish(GameEvent.WAVE_STARTED, currentWaveIndex + 1);
    }

    // --- GETTERS ---

    public int getCurrentWaveNumber() {
        return currentWaveIndex + 1;
    }

    public boolean isWaveInProgress() {
        return waveInProgress;
    }

    public float getTimeUntilNextWave() {
        return Math.max(0, waveDelayTimer);
    }

    /**
     * Reset lại đợt sóng khi chơi lại game mới.
     */
    public void reset() {
        this.currentWaveIndex = 0;
        this.currentSpawnQueue.clear();
        this.spawnTimer = 0f;
        this.waveDelayTimer = 3.0f;
        this.waveInProgress = false;
    }
}
