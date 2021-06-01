
// Gebruikte bibliotheken
#include <WiFi.h>
#include <PubSubClient.h>
#include <ESP32Servo.h>

// pinnumber for house servo's
const int SERVO_HOUSE_1_PIN = 2;
const int SERVO_UP = 180;
const int SERVO_DOWN = 0;
const char *MQTT_TOPIC_HOUSE1 = "ti/1.4/b1/house/1";

// pinnumber for blowreader
const int BLOW_READER_PIN = 34;
const float BLOW_TOTAL_COMPLETE = 50;
const char *MQTT_TOPIC_BLOWER = "ti/1.4/b1/blower/speed";
const char *MQTT_TOPIC_TOTALBLOW = "ti/1.4/b1/blower/total";

// Setting the pins for the LED's
const int NR_OF_LEDS = 3;
const int LED_PINS[NR_OF_LEDS] = {13, 15, 14};
const int LED_CHANNELS[NR_OF_LEDS] = {1, 2, 3}; // Voor de ESP32 LED control module

const int LINE_LENGTH = 4;

// END OF CONSTANT VALUES   //
// ------------------------ //
// MQTT SETUP SETTINGS      //


// Zelf instellen voor je eigen WLAN
const char *WLAN_SSID = "Ziggo89DC852";
const char *WLAN_ACCESS_KEY = "zhbNc5f3fjst";

// CLIENT_ID moet uniek zijn, dus zelf aanpassen (willekeurige letters en cijfers)
const char *MQTT_CLIENT_ID = "MQTTExampleTryout_zeer_unieke_code_PPOPKKKINBR4352Ad";

// Gegevens van de MQTT broker die we in TI-1.4 kunnen gebruiken
const char *MQTT_BROKER_URL = "sendlab.nl";
const int MQTT_PORT = 11884;
const char *MQTT_USERNAME = "ti";
const char *MQTT_PASSWORD = "tiavans";

// Definieer de te gebruiken Quality of Service (QoS)
const int MQTT_QOS = 0;

WiFiClient wifiClient;
//WiFiClientSecure wifiClient; // Om een met TLS beveiligde verbinding te kunnen gebruiken
PubSubClient mqttClient(wifiClient);


// END OF MQTT SETTINGS     //
// ------------------------ //
// DECALRATION OF VARIABLES //


// Servo controls
Servo servoHouse1;

// Led controls
bool ledIsOn = true; // Voor de ingebouwde LED, soort keep-alive knipperlicht
byte ledIntensities[4] = {0, 0, 0, 0};

// blower controls
int currentBlow = 0;
int totalBlowPercent = 0;


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

/*
* Readsout blower and updates all the correct value's
*/
void handleBlower()
{
  // Reading the blow-motor, if value changed print it 
  int newBlow = analogRead(BLOW_READER_PIN) / 20;
  
  // upload turning speed
  if(newBlow != currentBlow) {
    char payload[30];
    sprintf(payload, "%d", newBlow);
    mqttClient.publish(MQTT_TOPIC_BLOWER, payload);
  
    Serial.print("blowing changed: ");
    Serial.println(payload);
  }

  // updating percentage blow
  int percent = ((newBlow / BLOW_TOTAL_COMPLETE) * 100);
  if (percent != 0) {
    totalBlowPercent += percent;
    char payloadPer[10];
    sprintf(payloadPer, "%d", totalBlowPercent);
    mqttClient.publish(MQTT_TOPIC_TOTALBLOW, payloadPer);

    Serial.print("blowing percentage changed: ");
    Serial.println(payloadPer);
    Serial.println(totalBlowPercent);  
  }

  currentBlow = newBlow;
}

void handleLED()
{
  // clearing all LEDs
  ledIntensities[0] = 255;
  ledIntensities[1] = 255;
  ledIntensities[2] = 255;

  // Setting red LED
  if (totalBlowPercent > 10) 
  {
    ledIntensities[2] = 0;
  }

  // Setting amber LED
  if (totalBlowPercent > 50) 
  {
    ledIntensities[1] = 0;
  }

  // Setting green LED
  if (totalBlowPercent > 100) 
  {
    ledIntensities[0] = 0;
  }

  setLedIntensity(0, ledIntensities[0]);
  setLedIntensity(1, ledIntensities[1]);
  setLedIntensity(2, ledIntensities[2]);
}

/*
 * De MQTT client callback die wordt aangeroepen bij elk bericht dat
 * we van de MQTT broker ontvangen
 */
void mqttCallback(char *topic, byte *payload, unsigned int length)
{
  // Logging
  Serial.print("MQTT callback called for topic ");
  Serial.println(topic);
  Serial.print("Payload length ");
  Serial.println(length);

  if (strcmp(topic, MQTT_TOPIC_HOUSE1) == 0)
  {
    servoHouse1.write(SERVO_DOWN);
  }
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
  Serial.println("ESP32 MOBAPP B!");

   // Zet de WiFi verbinding op
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  Serial.println("Connecting to ");
  Serial.println(WLAN_SSID);
  WiFi.begin(WLAN_SSID, WLAN_ACCESS_KEY);
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(1000);
  }
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  // Om een met TLS beveiligde verbinding te kunnen gebruiken zonder certificaten
  //wifiClient.setInsecure(); // Skip de controle, niet ideaal maar werkt voorlopig wel

  // Zet de MQTT client op
  mqttClient.setServer(MQTT_BROKER_URL, MQTT_PORT);
  mqttClient.setCallback(mqttCallback);

  // Maak verbinding met de MQTT broker
  if (!mqttClient.connect(MQTT_CLIENT_ID, MQTT_USERNAME, MQTT_PASSWORD))
  {
    Serial.println("Failed to connect to MQTT broker");
  }
  else
  {
    Serial.println("Connected to MQTT broker");
  }

  // Subscribe op de house 1 topic
  if (!mqttClient.subscribe(MQTT_TOPIC_HOUSE1, MQTT_QOS))
  {
    Serial.print("Failed to subscribe to topic ");
    Serial.println(MQTT_TOPIC_HOUSE1);
  }
  else
  {
    Serial.print("Subscribed to topic ");
    Serial.println(MQTT_TOPIC_HOUSE1);
  }
}


void loop()
{
  // Nodig om de MQTT client zijn werk te laten doen
  mqttClient.loop();
  
  if (!servoHouse1.attached()) {
    servoHouse1.setPeriodHertz(50); // standard 50 hz servo
    servoHouse1.attach(33, 50, 2400); // Attach the servo after it has been detatched
    servoHouse1.write(SERVO_UP);
  }

  // Handle blower
  handleBlower();
  
  // Handle LEDs
  handleLED();

  // Laat de ingebouwde LED knipperen
  ledIsOn = !ledIsOn;
  digitalWrite(LED_BUILTIN, ledIsOn);
  
  delay(500);
}
