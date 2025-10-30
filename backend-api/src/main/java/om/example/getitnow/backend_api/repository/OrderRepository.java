package om.example.getitnow.backend_api.repository;

import om.example.getitnow.backend_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // เราอาจจะอยากค้นหา Orders ทั้งหมดของ User คนเดียว
    // List<Order> findByUserId(Long userId);
}