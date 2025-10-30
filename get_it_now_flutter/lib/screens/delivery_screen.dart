import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart'; // 1. Import Google Maps
import 'package:http/http.dart' as http; // 2. Import http
import 'dart:convert'; // 3. Import jsonEncode

class DeliveryScreen extends StatefulWidget {
  // 4. รับ userId ที่เราส่งมาจาก HomeScreen
  final String username;
  final int userId; 

  const DeliveryScreen({super.key, required this.username, required this.userId});

  @override
  State<DeliveryScreen> createState() => _DeliveryScreenState();
}

class _DeliveryScreenState extends State<DeliveryScreen> {
  // --- ตัวแปรสำหรับควบคุม ---
  GoogleMapController? mapController;
  
  // ตำแหน่งเริ่มต้น (กลางกรุงเทพ)
  static const CameraPosition initialCameraPosition = CameraPosition(
    target: LatLng(13.7563, 100.5018), 
    zoom: 14,
  );

  LatLng? pickupLocation;
  LatLng? dropoffLocation;
  bool isSelectingPickup = true; // สถานะ: กำลังเลือกจุดรับ (true) หรือจุดส่ง (false)
  final Set<Marker> markers = {};
  
  // เพิ่มตัวควบคุมรายละเอียดเพิ่มเติม
  final _descriptionController = TextEditingController(); 
  bool _isLoading = false;

  String get _baseUrl {
    // 🚨 แก้ไขตรงนี้: เลือก URL ตามอุปกรณ์ที่คุณใช้
    // return 'http://10.0.2.2:8080'; // สำหรับ Android Emulator
    return 'http://localhost:8080'; // สำหรับ Chrome (Web)
  }

  void _onMapCreated(GoogleMapController controller) {
    mapController = controller;
  }

  void _onTap(LatLng point) {
    setState(() {
      // 5. กำหนดตำแหน่งที่เลือกบนแผนที่
      if (isSelectingPickup) {
        pickupLocation = point;
      } else {
        dropoffLocation = point;
      }
      
      // 6. จัดการ Markers
      markers.clear();
      if (pickupLocation != null) {
        markers.add(Marker(
          markerId: const MarkerId('pickup'),
          position: pickupLocation!,
          infoWindow: const InfoWindow(title: 'จุดรับ'),
          icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueGreen),
        ));
      }
      if (dropoffLocation != null) {
        markers.add(Marker(
          markerId: const MarkerId('dropoff'),
          position: dropoffLocation!,
          infoWindow: const InfoWindow(title: 'จุดส่ง'),
          icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueRed),
        ));
      }
    });
  }

  // 7. ⭐️⭐️⭐️ เมธอดสำหรับยิง API สร้าง Order ⭐️⭐️⭐️
  Future<void> _submitOrder() async {
    if (pickupLocation == null || dropoffLocation == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('กรุณาระบุจุดรับและจุดส่งให้ครบ'), backgroundColor: Colors.red),
      );
      return;
    }

    setState(() { _isLoading = true; });

    const url = '/api/orders'; 

    try {
      // 8. สร้าง JSON Body สำหรับ Spring Boot
      final body = jsonEncode({
        'user': {'id': widget.userId}, // ส่ง User ID ที่ Login มาแล้ว
        'serviceType': 'รับ-ส่ง', 
        'status': 'searching', 
        
        // 9. ส่งพิกัด Lat/Lng และรายละเอียด
        'pickupLocation': 'Lat: ${pickupLocation!.latitude.toStringAsFixed(6)}, Long: ${pickupLocation!.longitude.toStringAsFixed(6)} (Details: ${_descriptionController.text})', 
        'dropoffLocation': 'Lat: ${dropoffLocation!.latitude.toStringAsFixed(6)}, Long: ${dropoffLocation!.longitude.toStringAsFixed(6)}',
        
        'totalPrice': 0.00, 
      });

      final response = await http.post(
        Uri.parse('$_baseUrl$url'), // 10. ยิง API
        headers: {'Content-Type': 'application/json'},
        body: body,
      );

      if (!context.mounted) return;

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('ส่งคำสั่งซื้อสำเร็จ! กำลังค้นหาคนขับ...'), backgroundColor: Colors.green),
        );
        Navigator.of(context).pop(); 
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('ส่งออร์เดอร์ล้มเหลว: ${response.body}'), backgroundColor: Colors.red),
        );
      }
    } catch (e) {
      if (!context.mounted) return;
       ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('เกิดข้อผิดพลาดในการเชื่อมต่อ: $e'), backgroundColor: Colors.red),
        );
    }

    setState(() { _isLoading = false; });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          isSelectingPickup 
            ? '1. เลือกจุดรับ (Pickup)' 
            : '2. เลือกจุดส่ง (Dropoff)'
        ),
      ),
      body: Stack(
        children: [
          // --- 11. GoogleMap ไว้ด้านหลัง (เต็มจอ) ---
          GoogleMap(
            onMapCreated: _onMapCreated,
            initialCameraPosition: initialCameraPosition,
            onTap: _onTap, 
            markers: markers, 
            myLocationEnabled: true,
          ),
          
          // --- 12. วาง UI ควบคุมด้านหน้า (Floating UI) ---
          Positioned(
            top: 10,
            left: 10,
            right: 10,
            child: Card(
              child: Padding(
                padding: const EdgeInsets.all(12.0),
                child: Column(
                  children: [
                    // --- ช่องรายละเอียดเพิ่มเติม ---
                    TextField(
                      controller: _descriptionController,
                      decoration: const InputDecoration(
                        labelText: 'รายละเอียดของ/ผู้รับ (Optional)',
                        border: OutlineInputBorder(),
                        isDense: true,
                      ),
                      maxLines: 2,
                    ),
                    const SizedBox(height: 10),

                    // --- แสดงสถานะที่กำลังเลือก ---
                    Text(
                      'สถานะ: ${isSelectingPickup ? 'จุดรับ' : 'จุดส่ง'}',
                      style: const TextStyle(fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 5),
                    Text(
                      'จุดรับ: ${pickupLocation != null ? '✅' : '❌'} | จุดส่ง: ${dropoffLocation != null ? '✅' : '❌'}',
                      style: const TextStyle(fontSize: 12),
                    ),
                    const SizedBox(height: 10),

                    // --- ปุ่มสลับโหมดและปุ่มยืนยัน ---
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceAround,
                      children: [
                        Expanded(
                          child: OutlinedButton(
                            onPressed: () {
                              setState(() {
                                isSelectingPickup = !isSelectingPickup;
                              });
                            },
                            child: Text(isSelectingPickup ? 'สลับไปจุดส่ง' : 'สลับไปจุดรับ'),
                          ),
                        ),
                        const SizedBox(width: 10),
                        Expanded(
                          child: ElevatedButton(
                            onPressed: _submitOrder,
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.green,
                              padding: const EdgeInsets.symmetric(vertical: 12),
                            ),
                            child: _isLoading 
                                ? const SizedBox(width: 20, height: 20, child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2)) 
                                : const Text('ยืนยันออร์เดอร์'),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}