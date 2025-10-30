package om.example.getitnow.backend_api.repository;

import om.example.getitnow.backend_api.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // (ปกติเราไม่ค่อยค้นหา Item เดี่ยวๆ แต่จะดึงผ่าน Order)
}