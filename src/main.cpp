#include <Arduino.h>
#include <TinyGPSPlus.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>

// Provide the token generation process info.
#include "addons/TokenHelper.h"
// Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert your network credentials
#define WIFI_SSID "Redmi 12"
#define WIFI_PASSWORD "17112004"

// Insert Firebase project API Key
#define API_KEY "AIzaSyDZhfBTefiU2vvY5yQDkTt7v6OsA-pI9S0"

// Insert RTDB URL
#define DATABASE_URL "https://rentwise-6c767-default-rtdb.firebaseio.com/" 

// Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
bool signupOK = false;

// Mô phỏng dữ liệu GPS cho các xe
struct Vehicle {
  String idVehicle;
  String latitude;
  String longitude;
};

Vehicle vehicles[] = {
  {"H-01", "10.732340614389907", "106.69920343652902"},// Xe đầu tiên: 10.732340614389907, 106.69920343652902
  {"H-02", "11.578247389597207", "107.82294494956088"},                     // Xe thứ hai: 11.578247389597207, 107.82294494956088
  {"H-03", "11.7981", "108.2023"}                      // Xe thứ ba
};

void setup() {
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  // Assign the API key (required)
  config.api_key = API_KEY;

  // Assign the RTDB URL (required)
  config.database_url = DATABASE_URL;

  // Sign up
  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("ok");
    signupOK = true;
  } else {
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  // Assign the callback function for the long running token generation task
  config.token_status_callback = tokenStatusCallback; // see addons/TokenHelper.h

  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void sendVehicleData(const Vehicle &vehicle, const String &vehiclePath) {
  // Đường dẫn tới nhánh của từng xe
  String idPath = vehiclePath + "/idVehicle";
  String latitudePath = vehiclePath + "/latitude";
  String longitudePath = vehiclePath + "/longitude";

  // Cập nhật idVehicle
  if (Firebase.RTDB.setString(&fbdo, idPath.c_str(), vehicle.idVehicle)) {
    Serial.println("idVehicle sent successfully");
  } else {
    Serial.println("Failed to send idVehicle");
    Serial.println("REASON: " + fbdo.errorReason());
  }

  // Cập nhật latitude
  if (Firebase.RTDB.setString(&fbdo, latitudePath.c_str(), vehicle.latitude)) {
    Serial.println("Latitude sent successfully");
  } else {
    Serial.println("Failed to send latitude");
    Serial.println("REASON: " + fbdo.errorReason());
  }

  // Cập nhật longitude
  if (Firebase.RTDB.setString(&fbdo, longitudePath.c_str(), vehicle.longitude)) {
    Serial.println("Longitude sent successfully");
  } else {
    Serial.println("Failed to send longitude");
    Serial.println("REASON: " + fbdo.errorReason());
  }
}

void loop() {
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 10000 || sendDataPrevMillis == 0)) {
    sendDataPrevMillis = millis();

    // Gửi dữ liệu của từng xe lên Firebase
    sendVehicleData(vehicles[0], "GPS/gps1");
    sendVehicleData(vehicles[1], "GPS/gps2");
    sendVehicleData(vehicles[2], "GPS/gps3");

    // Giả lập sự di chuyển của mỗi xe bằng cách tăng giá trị tọa độ
    for (int i = 0; i < 3; i++) {
      // Chuyển đổi từ String sang float để tăng giá trị, sau đó chuyển ngược lại thành String với định dạng cố định
      float lat = vehicles[i].latitude.toFloat() + 0.01;
      float lon = vehicles[i].longitude.toFloat() + 0.01;

      // Chuyển đổi float về String với 6 chữ số thập phân
      vehicles[i].latitude = String(lat, 6);
      vehicles[i].longitude = String(lon, 6);
    }
  }
}

