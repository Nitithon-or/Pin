package om.example.getitnow.backend_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews") // 1. แมปกับตาราง "reviews" 
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id") // 2. Primary Key [cite: 64]
    private Long id;

    // --- ความสัมพันธ์ (Relationship) ---
    // ความสัมพันธ์แบบ N:1 (หลาย Reviews ต่อ 1 Order)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false) // 3. Foreign Key "order_id" [cite: 74]
    private Order order;

    @Column(name = "rating", nullable = false) // 4. "rating" [cite: 75]
    private int rating; // เช่น 1, 2, 3, 4, 5

    @Column(name = "comment", columnDefinition = "TEXT") // 5. "comment" [cite: 76]
    private String comment;

    @Column(name = "created_at", updatable = false) // 6. "created_at" [cite: 77]
    private LocalDateTime createdAt;

    // --- Lifecycle Callbacks ---
    @PrePersist // ทำงานก่อน Save ครั้งแรก
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Constructors ---
    public Review() {
        // Constructor เปล่าที่ JPA ต้องการ
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}