package om.example.getitnow.backend_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal; // สำหรับเก็บข้อมูลเงิน (Price)
import java.time.LocalDateTime;
import java.util.ArrayList; // สำหรับเก็บ List ของ OrderItem
import java.util.List; // สำหรับเก็บ List ของ OrderItem

@Entity
@Table(name = "orders") // 1. แมปกับตาราง "orders"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id") //
    private Long id;

    // --- ความสัมพันธ์ (Relationship) ---

    // ความสัมพันธ์แบบ N:1 (หลาย Orders ต่อ 1 User)
    // นี่คือ Foreign Key "user_id"
    @ManyToOne(fetch = FetchType.LAZY) // LAZY = ดึงข้อมูล User เมื่อจำเป็นเท่านั้น
    @JoinColumn(name = "user_id", nullable = false) // "user_id" ห้ามเป็นค่าว่าง
    private User user;

    // ความสัมพันธ์แบบ N:1 (หลาย Orders ต่อ 1 Driver)
    // นี่คือ Foreign Key "driver_id"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id") // อาจเป็น null ได้ ถ้ายังไม่มีคนรับงาน
    private Driver driver;

    // ความสัมพันธ์แบบ 1:N (1 Order ต่อหลาย OrderItems)
    // "mappedBy = 'order'" หมายถึง ให้ไปดูตัวแปรชื่อ 'order' ในคลาส OrderItem
    // CascadeType.ALL = ถ้าลบ Order นี้ ให้ลบ Item ลูกๆ (OrderItem) ออกไปด้วย
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();

    // --- คอลัมน์อื่นๆ ตาม PDF ---

    @Column(name = "service_type", nullable = false) // [cite: 114]
    private String serviceType; // เช่น "รับ-ส่ง", "ซื้อของ"

    @Column(name = "status", nullable = false) // [cite: 115]
    private String status; // เช่น "searching", "pending", "completed"

    @Column(name = "pickup_location", columnDefinition = "TEXT")
    private String pickupLocation;

    @Column(name = "dropoff_location", columnDefinition = "TEXT")
    private String dropoffLocation;
    
    @Column(name = "total_price", precision = 10, scale = 2) //
    private BigDecimal totalPrice;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- เมธอด Lifecycle Callbacks ---
    
    @PrePersist // ทำงานก่อน Save ครั้งแรก
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // ทำงานก่อน Update
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    // (อย่าลืม Generate ให้ครบทุกตัวนะครับ)
    // (คลิกขวา -> Source Action... -> Generate Getters and Setters... -> เลือกทั้งหมด)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}