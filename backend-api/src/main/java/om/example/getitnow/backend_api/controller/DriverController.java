package om.example.getitnow.backend_api.controller;

import om.example.getitnow.backend_api.model.Driver;
import om.example.getitnow.backend_api.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drivers") // 1. URL หลักสำหรับ API คนขับ
public class DriverController {

    @Autowired // 2. ฉีด DriverRepository เข้ามา
    private DriverRepository driverRepository;

    // --- API 1: สมัครเป็นคนขับใหม่ ---
    // (POST) http://localhost:8080/api/drivers/register
    @PostMapping("/register")
    public ResponseEntity<Driver> registerDriver(@RequestBody Driver driver) {
        Driver savedDriver = driverRepository.save(driver);
        return ResponseEntity.ok(savedDriver);
    }

    // --- API 2: ดึงข้อมูลคนขับทั้งหมด ---
    // (GET) http://localhost:8080/api/drivers
    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    // --- API 3: ดึงข้อมูลคนขับที่ "ว่างงาน" (Available) เท่านั้น ---
    // (GET) http://localhost:8080/api/drivers/available
    @GetMapping("/available")
    public List<Driver> getAvailableDrivers() {
        // 3. ค้นหาคนขับทั้งหมด แล้วกรอง (filter) เอาเฉพาะ isAvailable == true
        return driverRepository.findAll().stream()
                .filter(driver -> driver.isAvailable())
                .collect(Collectors.toList());
    }

    // --- API 4: อัปเดตสถานะคนขับ (เช่น ว่าง/ไม่ว่าง) ---
    // (PUT) http://localhost:8080/api/drivers/1/status?available=false
    @PutMapping("/{id}/status")
    public ResponseEntity<Driver> updateDriverStatus(
            @PathVariable Long id, 
            @RequestParam boolean available) {
        
        return driverRepository.findById(id)
                .map(driver -> {
                    driver.setAvailable(available); // 4. อัปเดตสถานะ
                    Driver updatedDriver = driverRepository.save(driver); // 5. บันทึก
                    return ResponseEntity.ok(updatedDriver);
                })
                .orElse(ResponseEntity.notFound().build()); // 6. ถ้าไม่เจอคนขับ ID นี้
    }
}