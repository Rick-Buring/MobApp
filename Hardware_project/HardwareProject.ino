
// Gebruikte bibliotheken
#include <WiFi.h>
//#include <WiFiClientSecure.h>
#include <PubSubClient.h>
#include <LiquidCrystal_I2C.h>

const int BLOW_READER_PIN = 34;

int currentBlow = 0;


void setup()
{
  // Ingebouwde LED wordt als een soort keep-alive knipperLED gebruikt
  pinMode(LED_BUILTIN, OUTPUT);


  // Open de verbinding naar de seriële terminal
  Serial.begin(115200);
  Serial.println("ESP32 MQTT example");
}


void loop()
{

  // Reading the blow-motor, if value changed print it 
  int newBlow = analogRead(BLOW_READER_PIN) / 20;
  if(newBlow != currentBlow) {
    Serial.print("blowing changed: ");
    Serial.println(newBlow);
  }
  currentBlow = newBlow;

  delay(100);
}