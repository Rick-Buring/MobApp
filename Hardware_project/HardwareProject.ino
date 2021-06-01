// Gebruikte bibliotheken
#include <WiFi.h>
//#include <WiFiClientSecure.h>
#include <PubSubClient.h>
#include <LiquidCrystal_I2C.h>

const int BLOW_READER_PIN = 34;

// Setting the pins for the LED's
// The leds we use:
// Red = pin 14, index 2
// Orange = pin 13, index 1
// Green = pin12, index 0
// Intensity can be set via setLedIntensity(index, ledIntensities[index]);
const int NR_OF_LEDS = 3;
const int LED_PINS[NR_OF_LEDS] = {12, 13, 14};
const int LED_CHANNELS[NR_OF_LEDS] = {1, 2, 3}; // Voor de ESP32 LED control module


bool ledIsOn = true; // Voor de ingebouwde LED, soort keep-alive knipperlicht
byte ledIntensities[4] = {0, 0, 0, 0};

int currentBlow = 0;

/*
 * Stel de LED helderheid in
 * Keer de waarde om (255 wordt uit, 0 wordt aan) omdat de LEDs aangesloten zijn
 * tussen 3.3 V en de pin
 */
void setLedIntensity(int ledNr, byte intensity)
{
  byte pwmValue = map(intensity, 255, 0, 0, 255);
  ledcWrite(LED_CHANNELS[ledNr], pwmValue);
}

void setup()
{
  // Ingebouwde LED wordt als een soort keep-alive knipperLED gebruikt
  pinMode(LED_BUILTIN, OUTPUT);

  // Gekleurde LEDs worden aangestuurd met de ESP32 LED control (LEDC) library
  // Initialiseer de LED control kanalen
  for (int led = 0; led < NR_OF_LEDS; led++)
  {
    ledcAttachPin(LED_PINS[led], LED_CHANNELS[led]);
    ledcSetup(LED_CHANNELS[led], 12000, 8); // 12 kHz PWM, 8-bit resolutie
    ledcWrite(LED_CHANNELS[led], ledIntensities[led]);
  }

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

  // Laat de ingebouwde LED knipperen
  ledIsOn = !ledIsOn;
  digitalWrite(LED_BUILTIN, ledIsOn);

  delay(500);
}