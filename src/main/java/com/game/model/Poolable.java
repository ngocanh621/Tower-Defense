package com.game.model;

/**
 * Interface cơ sở cho các đối tượng có thể đưa vào Kho tái chế (Object Pool).
 * Bắt buộc các lớp con (như Bullet, Enemy) phải cài đặt cơ chế reset và quản lý trạng thái.
 */
public interface Poolable {

    /**
     * reset và sẵn sàng lấy ra dùng
     */
    void reset();

    /**
     * kiểm tra xem đối tượng có đang được sử dụng trên màn hình hay không
     * @return true nếu đang hoạt động, false ngược lại
     */
    boolean isActive();

    /**
     * cập nhật trạng thái hoạt động cho đối tượng
     * * @param active true khi mang đối tượng ra xài, false khi cất lại vào kho
     */
    void setActive(boolean active);
}