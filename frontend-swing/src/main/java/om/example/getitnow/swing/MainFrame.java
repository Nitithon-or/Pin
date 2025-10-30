package om.example.getitnow.swing;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import om.example.getitnow.swing.model.Driver;

import javax.swing.*;
import javax.swing.plaf.FontUIResource; // 👈 1. Import ตัวจัดการฟอนต์
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Enumeration; // 👈 2. Import ตัววนลูป
import java.util.List;

public class MainFrame extends JFrame {

    // (ส่วนตัวแปร... เหมือนเดิม)
    private DefaultTableModel tableModel;
    private JTable driverTable;
    private JButton refreshButton;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // (Constructor... เหมือนเดิม)
    public MainFrame() {
        setTitle("GetItNow - Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "ชื่อ", "นามสกุล", "ทะเบียนรถ", "สถานะ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        driverTable = new JTable(tableModel);
        refreshButton = new JButton("โหลดข้อมูลคนขับ (Refresh)");

        refreshButton.addActionListener(e -> {
            loadDriverData(); 
        });

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(new JScrollPane(driverTable), BorderLayout.CENTER);
        pane.add(refreshButton, BorderLayout.SOUTH);
    }

    // (เมธอด loadDriverData()... เหมือนเดิม)
    private void loadDriverData() {
        refreshButton.setText("กำลังโหลด...");
        refreshButton.setEnabled(false); 

        SwingWorker<List<Driver>, Void> worker = new SwingWorker<>() {
            
            @Override
            protected List<Driver> doInBackground() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/drivers")) 
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                TypeToken<List<Driver>> token = new TypeToken<>() {};
                return gson.fromJson(response.body(), token.getType());
            }

            @Override
            protected void done() {
                try {
                    List<Driver> drivers = get(); 
                    tableModel.setRowCount(0); 
                    for (Driver driver : drivers) {
                        tableModel.addRow(new Object[]{
                            driver.getId(),
                            driver.getFirstName(),
                            driver.getLastName(),
                            driver.getLicensePlate(),
                            driver.isAvailable() ? "ว่าง" : "ไม่ว่าง"
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "ไม่สามารถโหลดข้อมูลได้: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    refreshButton.setText("โหลดข้อมูลคนขับ (Refresh)");
                    refreshButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    // --- เมธอด 'main' (จุดเริ่มต้นการรันโปรแกรม) ---
    public static void main(String[] args) {
        
        // --- 3. ⭐️⭐️⭐️ โค้ดแก้ฟอนต์สี่เหลี่ยม (เพิ่มตรงนี้) ⭐️⭐️⭐️ ---
        try {
            // 1. ตั้งค่า Look and Feel (หน้าตา) ให้เหมือนระบบ (เช่น Windows)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // 2. ตั้งค่าฟอนต์ใหม่ (ใช้ "Tahoma" เพราะรองรับภาษาไทยและมีใน Windows)
            Font thaiFont = new FontUIResource("Tahoma", Font.PLAIN, 14); // 14 คือขนาด

            // 3. สั่งให้ UI ทั้งหมด (ปุ่ม, ตาราง, ลาเบล ฯลฯ) ใช้ฟอนต์นี้
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource) {
                    UIManager.put(key, thaiFont);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // --- จบโค้ดแก้ฟอนต์ ---


        // โค้ดเดิม: สั่งให้รัน MainFrame
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}