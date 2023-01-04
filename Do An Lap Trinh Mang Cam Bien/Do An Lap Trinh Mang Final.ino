#include <Wire.h>
#include "MAX30100_PulseOximeter.h"
#include <Blynk.h>
#include <ESP8266WiFi.h>
#include <BlynkSimpleEsp8266.h>
#include "Adafruit_GFX.h"
#include "OakOLED.h"
#include <FirebaseESP8266.h>
#define FIREBASE_URL "https://lt-mang-622c4-default-rtdb.firebaseio.com/"
#define FIREBASE_SECRET "q6LasXygOizeEyQRlFfLGB4gBz5DPqPMTtw8KwDy"
OakOLED oled;
char auth[] = "Wl154mODxrZdFSEv1Gsg6D22EHkBMG0F";
char ssid[] = "Be Kind Home";
char pass[] = "tothon1%";
FirebaseData fbdo;
PulseOximeter pox;
unsigned long previousMillis = 0;
const long interval = 1500;
volatile boolean heartBeatDetected = false;

const unsigned char bitmap[] PROGMEM = {
  0x00, 0x00, 0x00, 0x00, 0x01, 0x80, 0x18, 0x00, 0x0f, 0xe0, 0x7f, 0x00, 0x3f, 0xf9, 0xff, 0xc0,
  0x7f, 0xf9, 0xff, 0xc0, 0x7f, 0xff, 0xff, 0xe0, 0x7f, 0xff, 0xff, 0xe0, 0xff, 0xff, 0xff, 0xf0,
  0xff, 0xf7, 0xff, 0xf0, 0xff, 0xe7, 0xff, 0xf0, 0xff, 0xe7, 0xff, 0xf0, 0x7f, 0xdb, 0xff, 0xe0,
  0x7f, 0x9b, 0xff, 0xe0, 0x00, 0x3b, 0xc0, 0x00, 0x3f, 0xf9, 0x9f, 0xc0, 0x3f, 0xfd, 0xbf, 0xc0,
  0x1f, 0xfd, 0xbf, 0x80, 0x0f, 0xfd, 0x7f, 0x00, 0x07, 0xfe, 0x7e, 0x00, 0x03, 0xfe, 0xfc, 0x00,
  0x01, 0xff, 0xf8, 0x00, 0x00, 0xff, 0xf0, 0x00, 0x00, 0x7f, 0xe0, 0x00, 0x00, 0x3f, 0xc0, 0x00,
  0x00, 0x0f, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
};

void onBeatDetected() {
  Serial.println("Beat Detected!");
  oled.drawBitmap(60, 20, bitmap, 28, 28, 1);
  oled.display();
  heartBeatDetected = true;
}

void setup() {
  Serial.begin(115200);

  WiFi.begin(ssid, pass);
  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());


  oled.begin();
  oled.clearDisplay();
  oled.setTextSize(1);
  oled.setTextColor(1);
  oled.setCursor(0, 0);

  oled.println("Initializing pulse oximeter..");
  oled.display();

  pinMode(16, OUTPUT);

  Blynk.begin(auth, ssid, pass, "blynk.cloud", 80);
  Firebase.begin(FIREBASE_URL, FIREBASE_SECRET);
  Firebase.reconnectWiFi(true);
  Firebase.setwriteSizeLimit(fbdo, "tiny");
  Serial.print("Initializing Pulse Oximeter..");

  if (!pox.begin()) {
    Serial.println("FAILED");
    oled.clearDisplay();
    oled.setTextSize(1);
    oled.setTextColor(1);
    oled.setCursor(0, 0);
    oled.println("FAILED");
    oled.display();
    for (;;)
      ;
  } else {
    oled.clearDisplay();
    oled.setTextSize(1);
    oled.setTextColor(1);
    oled.setCursor(0, 0);
    oled.println("SUCCESS");
    oled.display();
    Serial.println("SUCCESS");

    oled.clearDisplay();
  }
  pox.setOnBeatDetectedCallback(onBeatDetected);
}

void leerDatos() {
  float BPM = pox.getHeartRate();
  int SpO2 = pox.getSpO2();
  if (heartBeatDetected && BPM != 0) {

    if (SpO2 > 0) {

      Firebase.setInt(fbdo, "/SpO2", SpO2);
      delay(100);

      Firebase.setFloat(fbdo, "/BPM", BPM);
      delay(100);

      Serial.print("Heart rate:");
      Serial.print(BPM);
      Serial.print(" bpm / SpO2:");
      Serial.print(SpO2);
      Serial.println(" %");

      Blynk.virtualWrite(V7, BPM);
      Blynk.virtualWrite(V8, SpO2);

      oled.clearDisplay();
      oled.setTextSize(1);
      oled.setTextColor(1);
      oled.setCursor(0, 16);
      oled.println(pox.getHeartRate());

      oled.setTextSize(1);
      oled.setTextColor(1);
      oled.setCursor(0, 0);
      oled.println("Heart BPM");

      oled.setTextSize(1);
      oled.setTextColor(1);
      oled.setCursor(0, 30);
      oled.println("Spo2");

      oled.setTextSize(1);
      oled.setTextColor(1);
      oled.setCursor(0, 45);
      oled.println(pox.getSpO2());
      oled.display();

      Serial.print("Envia");
      heartBeatDetected = false;
    }
  } else {
    BPM = 0;
    SpO2 = 0;
    pox.setOnBeatDetectedCallback(onBeatDetected);
  }
}

void loop() {
  Blynk.run();
  while (true) {
    pox.update();

    unsigned long currentMillis = millis();

    if (currentMillis - previousMillis >= interval) {
      pox.shutdown();
      leerDatos();
      pox.resume(); 
      previousMillis = currentMillis;
    }
  }
}