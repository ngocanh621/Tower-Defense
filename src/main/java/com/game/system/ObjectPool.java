package com.game.system;

import com.game.model.Poolable;
import java.util.*;
import java.util.function.Supplier;

/**
 * Kho tái chế đối tượng (Object Pool) dùng chung cho các thực thể kế thừa Poolable
 * Giúp tối ưu bộ nhớ và giảm hiện tượng giật lag do Garbage Collector dọn dẹp bộ nhớ liên tục
 */
public class ObjectPool<T extends Poolable> {

    private final Queue<T> available;
    private final Set<T> inUse;
    private final Supplier<T> factory;
    private final int initialSize;

    public ObjectPool(Supplier<T> factory, int initialSize) {
        this.factory = factory;
        this.initialSize = initialSize;
        this.available = new LinkedList<>();
        this.inUse = new HashSet<>();

        // khởi tạo sẵn danh sách đối tượng ban đầu trong kho
        for (int i = 0; i < initialSize; i++) {
            available.offer(factory.get());
        }
    }

    /**
     * lấy một đối tượng từ kho ra để sử dụng
     * nếu kho hết đối tượng sẵn có, sẽ tạo mới một đối tượng
     */
    public T acquire() {
        T obj = available.poll();
        if (obj == null) {
            obj = factory.get();
        }
        obj.setActive(true);
        inUse.add(obj);
        return obj;
    }

    /**
     * cất một đối tượng không còn sử dụng trở lại kho tái chế
     */
    public void release(T obj) {
        if (inUse.remove(obj)) {
            obj.reset();
            obj.setActive(false);
            available.offer(obj);
        }
    }

    /**
     * lấy số lượng đối tượng đang rảnh rỗi trong kho
     */
    public int getAvailableCount() {
        return available.size();
    }

    /**
     * lấy số lượng đối tượng đang được sử dụng trên màn hình
     */
    public int getInUseCount() {
        return inUse.size();
    }

    /**
     * lấy tổng số đối tượng do kho quản lý (rảnh rỗi + đang dùng)
     */
    public int getTotalCount() {
        return available.size() + inUse.size();
    }

    /**
     * mở rộng thêm số lượng đối tượng trong kho
     */
    public void expand(int count) {
        for (int i = 0; i < count; i++) {
            available.offer(factory.get());
        }
    }

    /**
     * xóa toàn bộ đối tượng trong kho
     */
    public void clear() {
        available.clear();
        inUse.clear();
    }
}
