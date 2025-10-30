package om.example.getitnow.backend_api.repository;

import om.example.getitnow.backend_api.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // เราอาจจะอยากค้นหา Reviews ทั้งหมดของ Order เดียว
    // List<Review> findByOrderId(Long orderId);
}