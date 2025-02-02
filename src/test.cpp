// #include <Arduino.h>
// #include <TinyGPSPlus.h>
// #if defined(ESP32)
//   #include <WiFi.h>
// #elif defined(ESP8266)
//   #include <ESP8266WiFi.h>
// #endif
// #include <Firebase_ESP_Client.h>

// // Provide the token generation process info.
// #include "addons/TokenHelper.h"
// // Provide the RTDB payload printing info and other helper functions.
// #include "addons/RTDBHelper.h"

// // Thư viện TinyGPSPlus để xử lý dữ liệu GPS
// TinyGPSPlus gps;

// // Thông tin Wi-Fi và Firebase
// #define WIFI_SSID "Tp- Link 2.4G"
// #define WIFI_PASSWORD "12356789"
// #define API_KEY "AIzaSyDZhfBTefiU2vvY5yQDkTt7v6OsA-pI9S0"
// #define DATABASE_URL "https://rentwise-6c767-default-rtdb.firebaseio.com/" 

// // Đối tượng Firebase và cấu hình
// FirebaseData fbdo;
// FirebaseAuth auth;
// FirebaseConfig config;

// unsigned long sendDataPrevMillis = 0;
// bool signupOK = false;

// // Serial GPS
// #define GPS_BAUD 9600

// // Mô phỏng dữ liệu GPS cho các xe giả lập
// struct Vehicle {
//   String idVehicle;
//   String latitude;
//   String longitude;
// };

// Vehicle vehicles[] = {
//   {"H-02", "12.2388", "109.1967"}, // Xe giả lập thứ hai
//   {"H-03", "11.7981", "108.2023"}  // Xe giả lập thứ ba
// };

// void setup() {
//   Serial.begin(115200);           // Serial debug
//   Serial1.begin(GPS_BAUD);        // Serial GPS (Serial1 RX - ESP8266)

//   // Kết nối Wi-Fi
//   WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
//   Serial.print("Connecting to Wi-Fi");
//   while (WiFi.status() != WL_CONNECTED) {
//     Serial.print(".");
//     delay(300);
//   }
//   Serial.println();
//   Serial.print("Connected with IP: ");
//   Serial.println(WiFi.localIP());

//   // Cấu hình Firebase
//   config.api_key = API_KEY;
//   config.database_url = DATABASE_URL;
//   config.token_status_callback = tokenStatusCallback; // Xem addons/TokenHelper.h

//   // Đăng nhập Firebase
//   if (Firebase.signUp(&config, &auth, "", "")) {
//     Serial.println("Firebase Sign-Up Successful");
//     signupOK = true;
//   } else {
//     Serial.printf("Sign-Up Error: %s\n", config.signer.signupError.message.c_str());
//   }
  
//   Firebase.begin(&config, &auth);
//   Firebase.reconnectWiFi(true);
// }

// void sendVehicleData(const String &vehiclePath, const String &idVehicle, const String &latitude, const String &longitude) {
//   // Đường dẫn tới nhánh của từng xe
//   String idPath = vehiclePath + "/idVehicle";
//   String latitudePath = vehiclePath + "/latitude";
//   String longitudePath = vehiclePath + "/longitude";

//   // Cập nhật idVehicle
//   if (Firebase.RTDB.setString(&fbdo, idPath.c_str(), idVehicle)) {
//     Serial.println("idVehicle sent successfully");
//   } else {
//     Serial.println("Failed to send idVehicle");
//     Serial.println("REASON: " + fbdo.errorReason());
//   }

//   // Cập nhật latitude
//   if (Firebase.RTDB.setString(&fbdo, latitudePath.c_str(), latitude)) {
//     Serial.println("Latitude sent successfully");
//   } else {
//     Serial.println("Failed to send latitude");
//     Serial.println("REASON: " + fbdo.errorReason());
//   }

//   // Cập nhật longitude
//   if (Firebase.RTDB.setString(&fbdo, longitudePath.c_str(), longitude)) {
//     Serial.println("Longitude sent successfully");
//   } else {
//     Serial.println("Failed to send longitude");
//     Serial.println("REASON: " + fbdo.errorReason());
//   }
// }

// void loop() {
//   // Đọc dữ liệu từ GPS NEO-6M cho xe thực
//     while (Serial1.available() > 0) {
//         if (gps.encode(Serial1.read())) {
//             if (gps.location.isUpdated()) {
//                 // Nhận tọa độ GPS
//                 float latitude = gps.location.lat();
//                 float longitude = gps.location.lng();
                
//                 Serial.print("Latitude: ");
//                 Serial.println(latitude, 6);
//                 Serial.print("Longitude: ");
//                 Serial.println(longitude, 6);

//                 // Gửi dữ liệu xe thực lên Firebase
//                 if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 15000 || sendDataPrevMillis == 0)) {
//                     sendDataPrevMillis = millis();
//                     sendVehicleData("GPS/gps1", "H-01", String(latitude, 6), String(longitude, 6));
//                 }
//             }
//         }
//     }

//   // Gửi dữ liệu xe giả lập lên Firebase
//   if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 15000 || sendDataPrevMillis == 0)) {
//     sendDataPrevMillis = millis();
//     sendVehicleData("GPS/gps2", vehicles[0].idVehicle, vehicles[0].latitude, vehicles[0].longitude);
//     sendVehicleData("GPS/gps3", vehicles[1].idVehicle, vehicles[1].latitude, vehicles[1].longitude);

//     // Giả lập sự di chuyển của các xe giả lập
//     for (int i = 0; i < 2; i++) {
//       vehicles[i].latitude = String(vehicles[i].latitude.toFloat() + 0.0001, 6);
//       vehicles[i].longitude = String(vehicles[i].longitude.toFloat() + 0.0001, 6);
//     }
//   }
// }

