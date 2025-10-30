package om.example.getitnow.backend_api.repository;

import om.example.getitnow.backend_api.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    // เราอาจจะอยากค้นหาคนขับที่ "ว่าง" (Available)
    // List<Driver> findByIsAvailable(boolean isAvailable);
}