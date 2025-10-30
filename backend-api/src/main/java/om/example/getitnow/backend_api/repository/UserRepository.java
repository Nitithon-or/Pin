package om.example.getitnow.backend_api.repository;

import om.example.getitnow.backend_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // 👈 1. เพิ่ม Import นี้

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 2. ⭐️⭐️⭐️ เพิ่มเมธอดนี้ ⭐️⭐️⭐️
    // Spring จะสร้างคำสั่ง SQL "SELECT * FROM users WHERE username = ?" ให้อัตโนมัติ
    Optional<User> findByUsername(String username);

}