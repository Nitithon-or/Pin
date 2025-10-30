package om.example.getitnow.backend_api.controller;

import om.example.getitnow.backend_api.model.Order;
import om.example.getitnow.backend_api.model.User;
import om.example.getitnow.backend_api.repository.OrderRepository;
import om.example.getitnow.backend_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders") // 1. URL หลักสำหรับ API คำสั่งซื้อ
public class OrderController {

    @Autowired // 2. ฉีดคลังข้อมูลที่จำเป็น
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // (เราจะยังไม่ทำระบบจับคู่คนขับอัตโนมัติในตอนนี้)
    // (เราจะเน้นที่การ "สร้าง" Order ขึ้นมาก่อน)

    // --- API 1: สร้างคำสั่งซื้อใหม่ ---
    // (POST) http://localhost:8080/api/orders
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order orderRequest) {
        // หมายเหตุ: JSON ที่ส่งมาจากแอปมือถือจะมีหน้าตาประมาณนี้:
        // {
        //     "user": { "id": 1 }, // <-- ส่งแค่ ID ของ User ที่สั่ง
        //     "serviceType": "ซื้อของ",
        //     "pickupLocation": "ร้าน A",
        //     "dropoffLocation": "บ้าน",
        //     "totalPrice": 150.00,
        //     "orderItems": [
        //         { "itemName": "โค้ก", "quantity": 1, "price": 20.00 },
        //         { "itemName": "เลย์", "quantity": 2, "price": 10.00 }
        //     ]
        // }

        // 3. ดึง User ฉบับเต็มจากฐานข้อมูล โดยใช้ ID ที่ส่งมา
        User user = userRepository.findById(orderRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // 4. ตั้งค่าต่างๆ ให้ครบ
        orderRequest.setUser(user); // เปลี่ยนจาก user (ที่มีแค่ id) เป็น user (ข้อมูลเต็ม)
        orderRequest.setStatus("searching"); // 5. สถานะเริ่มต้น "กำลังค้นหาคนขับ"

        // 6. สำคัญ: ต้องบอกให้ OrderItem รู้ว่ามันเป็นลูกของ Order นี้
        if (orderRequest.getOrderItems() != null) {
            orderRequest.getOrderItems().forEach(item -> item.setOrder(orderRequest));
        }

        // 7. บันทึก Order (และ OrderItem ลูกๆ จะถูกบันทึกไปด้วยเพราะเราตั้งค่า CascadeType.ALL ไว้)
        Order savedOrder = orderRepository.save(orderRequest);
        
        return ResponseEntity.ok(savedOrder);
    }

    // --- API 2: ดึงข้อมูล Order ทั้งหมด (สำหรับ Admin) ---
    // (GET) http://localhost:8080/api/orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // --- API 3: ดึงข้อมูล Order เดียวด้วย ID ---
    // (GET) http://localhost:8080/api/orders/1
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok(order))
                .orElse(ResponseEntity.notFound().build());
    }
}