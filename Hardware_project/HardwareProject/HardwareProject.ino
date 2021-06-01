
// Gebruikte bibliotheken
#include <WiFi.h>
#include <PubSubClient.h>
#include <LiquidCrystal_I2C.h>
#include <ESP32Servo.h>

const int SERVO_HOUSE_1_PIN = 2;

const int BLOW_READER_PIN = 34;

// Setting the pins for the LED's
const int NR_OF_LEDS = 3;
const int LED_PINS[NR_OF_LEDS] = {12, 13, 14};
const int LED_CHANNELS[NR_OF_LEDS] = {1, 2, 3}; // Voor de ESP32 LED control module

Servo servoHouse1;
int pos = 0;

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

  //Allow allocation of all timers
  ESP32PWM::allocateTimer(0);
  ESP32PWM::allocateTimer(1);
  ESP32PWM::allocateTimer(2);
  ESP32PWM::allocateTimer(3);

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
  if (!servoHouse1.attached()) {
    servoHouse1.setPeriodHertz(50); // standard 50 hz servo
    servoHouse1.attach(33, 500, 2400); // Attach the servo after it has been detatched
  }


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

  if(ledIsOn == true) {
    ledIntensities[0] = 255;
    ledIntensities[1] = 0;
    ledIntensities[2] = 255;

    servoHouse1.write(0);
  } else {
    ledIntensities[0] = 0;
    ledIntensities[1] = 255;
    ledIntensities[2] = 0;

    servoHouse1.write(180);
  }
  setLedIntensity(0, ledIntensities[0]);
  setLedIntensity(1, ledIntensities[1]);
  setLedIntensity(2, ledIntensities[2]);

  
  
  delay(5000);
}