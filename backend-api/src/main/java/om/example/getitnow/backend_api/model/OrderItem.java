package om.example.getitnow.backend_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items") // 1. แมปกับตาราง "order_items"
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id") //
    private Long id;

    // --- ความสัมพันธ์ ---
    // ความสัมพันธ์แบบ N:1 (หลาย Items ต่อ 1 Order)
    // นี่คือ Foreign Key "order_id"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // --- คอลัมน์อื่นๆ ตาม PDF ---
    
    @Column(name = "item_name", nullable = false) // [cite: 120]
    private String itemName;

    @Column(name = "quantity", nullable = false) // [cite: 121]
    private int quantity;

    @Column(name = "price", precision = 10, scale = 2) // (ใน PDF ไม่มี แต่ควรมีราคาสินค้า)
    private BigDecimal price;

    // --- Constructors ---
    public OrderItem() {
        // Constructor เปล่า
    }

    // --- Getters and Setters ---
    // (อย่าลืม Generate ให้ครบทุกตัวนะครับ)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}