import 'package:flutter/material.dart';
import 'delivery_screen.dart'; // ✅ ชี้ไปที่ delivery_screen ที่อยู่ในโฟลเดอร์เดียวกัน (screens)

class HomeScreen extends StatelessWidget {
  final String username;
  final int userId; // ⭐️ รับ userId มาจาก AuthScreen

  const HomeScreen({super.key, required this.username, required this.userId}); // ⭐️ เพิ่ม userId ใน Constructor

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('หน้าหลัก (Home)'),
        automaticallyImplyLeading: false,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'ยินดีต้อนรับ, $username!',
              style: const TextStyle(fontSize: 24),
            ),
            const SizedBox(height: 40),
            
            // --- ปุ่มบริการรับ-ส่ง ---
            ElevatedButton(
              onPressed: () {
                // เปิดหน้า Delivery พร้อมส่ง User ID
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (ctx) => DeliveryScreen(
                      // ⭐️⭐️⭐️ แก้ไข: ส่งค่าที่ required ทั้งสองตัว ⭐️⭐️⭐️
                      username: username, 
                      userId: userId, 
                    ),
                  ),
                );
              },
              child: const Text('บริการรับ-ส่ง'),
            ),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: () {
                // (บริการซื้อของ)
              },
              child: const Text('บริการซื้อของ'),
            ),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: () {
                // (บริการสั่งอาหาร)
              },
              child: const Text('บริการสั่งอาหาร'),
            ),
            
            const SizedBox(height: 40),
            TextButton(
              onPressed: () {
                Navigator.of(context).pop(); 
              },
              child: const Text('ออกจากระบบ'),
            ),
          ],
        ),
      ),
    );
  }
}