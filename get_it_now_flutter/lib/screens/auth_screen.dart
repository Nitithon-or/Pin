import 'package:flutter/material.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'home_screen.dart'; 

class AuthScreen extends StatefulWidget {
  const AuthScreen({super.key});

  @override
  State<AuthScreen> createState() => _AuthScreenState();
}

class _AuthScreenState extends State<AuthScreen> {
  bool _isLoginMode = true;
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _emailController = TextEditingController();
  final _phoneController = TextEditingController();
  bool _isLoading = false;

  // 🚨 แก้ไข: ตั้งค่า IP สำหรับ LDPlayer
  String get _baseUrl {
    return 'http://192.168.88.251:8080'; // ✅ สำหรับ LDPlayer/Android Emulator
    // ถ้าใช้ Chrome ให้เปลี่ยนเป็น 'http://localhost:8080';
  }

  // --- เมธอดสำหรับยิง API สมัครสมาชิก ---
  Future<void> _submitRegister() async {
    setState(() { _isLoading = true; });

    try {
      final body = jsonEncode({
        'username': _usernameController.text, 'password': _passwordController.text,
        'email': _emailController.text, 'phoneNumber': _phoneController.text,
      });
      final response = await http.post(
        Uri.parse('$_baseUrl/api/users/register'),
        headers: {'Content-Type': 'application/json'},
        body: body,
      );

      if (!context.mounted) return;

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('สมัครสมาชิกสำเร็จ!'), backgroundColor: Colors.green),
        );
        Navigator.of(context).pop();
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('สมัครล้มเหลว: ${response.body}'), backgroundColor: Colors.red),
        );
      }
    } catch (error) {
      if (!context.mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('เกิดข้อผิดพลาด: $error'), backgroundColor: Colors.red),
      );
    }
    if (!context.mounted) return;
    setState(() { _isLoading = false; });
  }

  // --- เมธอดสำหรับยิง API เข้าสู่ระบบ ---
  Future<void> _submitLogin() async {
    setState(() { _isLoading = true; });

    try {
      final body = jsonEncode({
        // 🚨 สังเกต: เราไม่ได้ใช้ .trim() ตรงนี้ เพราะ Flutter จะส่งค่าตามที่กรอก
        'username': _usernameController.text,
        'password': _passwordController.text, 
      });

      final response = await http.post(
        Uri.parse('$_baseUrl/api/users/login'),
        headers: {'Content-Type': 'application/json'},
        body: body,
      );

      if (!context.mounted) return;

      if (response.statusCode == 200) {
        final user = jsonDecode(response.body);
        
        Navigator.of(context).pushReplacement(
          MaterialPageRoute(
            builder: (ctx) => HomeScreen(
              username: user['username'],
              userId: user['id'], 
            ),
          ),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Login ล้มเหลว: รหัสผ่านไม่ถูกต้อง'), backgroundColor: Colors.red),
        );
      }
    } catch (error) {
      if (!context.mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('เกิดข้อผิดพลาด: $error'), backgroundColor: Colors.red),
      );
    }

    if (!context.mounted) return;
    setState(() { _isLoading = false; });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_isLoginMode ? 'เข้าสู่ระบบ' : 'สมัครสมาชิก'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            children: [
              if (!_isLoginMode)
                TextField(
                  controller: _emailController,
                  decoration: const InputDecoration(labelText: 'Email'),
                  keyboardType: TextInputType.emailAddress,
                ),
              TextField(
                controller: _usernameController,
                decoration: const InputDecoration(labelText: 'Username'),
              ),
              TextField(
                controller: _passwordController,
                decoration: const InputDecoration(labelText: 'Password'),
                obscureText: true,
              ),
              if (!_isLoginMode)
                TextField(
                  controller: _phoneController,
                  decoration: const InputDecoration(labelText: 'Phone Number'),
                  keyboardType: TextInputType.phone,
                ),
              const SizedBox(height: 20),
              if (_isLoading)
                const CircularProgressIndicator()
              else
                ElevatedButton(
                  onPressed: () {
                    if (_isLoginMode) {
                      _submitLogin(); 
                    } else {
                      _submitRegister(); 
                    }
                  },
                  child: Text(_isLoginMode ? 'Login' : 'Register'),
                ),
              TextButton(
                onPressed: () {
                  setState(() { _isLoginMode = !_isLoginMode; });
                },
                child: Text(
                  _isLoginMode ? 'ยังไม่มีบัญชี? สมัครสมาชิก' : 'มีบัญชีแล้ว? เข้าสู่ระบบ',
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}