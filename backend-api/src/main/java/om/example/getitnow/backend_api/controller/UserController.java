package om.example.getitnow.backend_api.controller;

import om.example.getitnow.backend_api.model.User;
import om.example.getitnow.backend_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") 
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userRepository.save(user); 
        return ResponseEntity.ok(savedUser); 
    }

    // ⭐️⭐️⭐️ API Login ฉบับแก้ไข (เพิ่ม .trim()) ⭐️⭐️⭐️
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password"); // รหัสผ่านที่ส่งมา

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // 🚨 แก้ไข: ใช้ password.trim().equals(...) เพื่อป้องกันช่องว่าง
            if (password.trim().equals(user.getPassword())) { 
                return ResponseEntity.ok(user);
            }
        }

        // ถ้า user ไม่เจอ หรือ รหัสผ่านผิด
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }


    // (API / (GET all) และ /{id} (GET by ID) ... เหมือนเดิม)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user)) 
                .orElse(ResponseEntity.notFound().build());
    }
}