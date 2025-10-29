package om.example.getitnow.backend_api.model; // ✅ สำคัญ: ต้องตรงกับ User.java

import jakarta.persistence.*;

@Entity // 1. บอก Spring ว่านี่คือตาราง
@Table(name = "drivers") // 2. บอกว่าให้แมปกับตารางชื่อ "drivers"
public class Driver {

    @Id // 3. นี่คือ Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. ให้ DB สร้าง ID อัตโนมัติ
    @Column(name = "driver_id") // 5. แมปกับคอลัมน์ "driver_id" [cite: 106]
    private Long id;

    // (ผมขอเพิ่มข้อมูลพื้นฐานของคนขับเข้าไปด้วยนะครับ เพื่อให้สมบูรณ์ขึ้น)
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
    // จบส่วนที่เพิ่ม

    @Column(name = "license_plate", nullable = false) // 6. แมปกับ "license_plate" [cite: 107]
    private String licensePlate;

    @Column(name = "vehicle_type") // (จากในรูป PDF [cite: 55])
    private String vehicleType; // เช่น "Motorcycle", "Car"

    @Column(name = "is_available", columnDefinition = "BOOLEAN DEFAULT true") // 7. แมปกับ "is_available" [cite: 108]
    private boolean isAvailable;

    // --- Constructor ---
    public Driver() {
        // Constructor เปล่าที่ JPA ต้องการ
    }

    // --- Getters and Setters ---
    // (จำเป็นมากสำหรับ JPA)
    // (ใน VS Code: คลิกขวา -> Source Action... -> Generate Getters and Setters... -> เลือกทั้งหมด)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}