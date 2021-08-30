
// Libraries
#include <WiFi.h>
#include <PubSubClient.h>
#include <ESP32Servo.h>

/// CONST VARIABLES //

// MQTT consts Reset and clearing
const char *MQTT_TOPIC_RESET = "ti/1.4/b1/TheWulfAndThreePigs/reset";
const char *MQTT_TOPIC_CLEAR_BLOW = "ti/1.4/b1/TheWulfAndThreePigs/clear_blow";

// MQTT consts Locking
const char *MQTT_TOPIC_LOCK = "ti/1.4/b1/TheWulfAndThreePigs";
const char *MQTT_TOPIC_LOCK_PING = "ti/1.4/b1/availability/request";

// MQTT ping for lastwill system
const char *MQTT_TOPIC_APP_LASTWILL = "ti/1.4/b1/TheWulfAndThreePigs/lastwill";

// Pinnumber for house servo's + MQTT consts
const int SERVO_UP = 180;
const int SERVO_DOWN = 0;

const int SERVO_HOUSE_1_PIN = 23;
const char *MQTT_TOPIC_HOUSE1 = "ti/1.4/b1/TheWulfAndThreePigs/house/1";

const int SERVO_HOUSE_2_PIN = 33;
const char *MQTT_TOPIC_HOUSE2 = "ti/1.4/b1/TheWulfAndThreePigs/house/2";

const int SERVO_HOUSE_3_PIN = 32;
const char *MQTT_TOPIC_HOUSE3 = "ti/1.4/b1/TheWulfAndThreePigs/house/3";

// consts for button
const int BUTTON1_PIN = 2;
const char *MQTT_TOPIC_BUTTON1 = "ti/1.4/b1/TheWulfAndThreePigs/startBtn";

// consts for blowreader
const int BLOW_READER_PIN = 34;
const float BLOW_TOTAL_COMPLETE = 65;  // value that represents 100% of blowing (difficulty)

const char *MQTT_TOPIC_BLOWER = "ti/1.4/b1/TheWulfAndThreePigs/blower/speed";
const char *MQTT_TOPIC_TOTALBLOW = "ti/1.4/b1/TheWulfAndThreePigs/blower/total";
const char *MQTT_TOPIC_START_BLOW = "ti/1.4/b1/TheWulfAndThreePigs/next";

// Setting the pins for the LED's
const int NR_OF_LEDS = 3;
const int LED_PINS[NR_OF_LEDS] = { 13, 15, 14 };
const int LED_CHANNELS[NR_OF_LEDS] = { 1, 2, 3 };  // Voor de ESP32 LED control module


// END OF CONSTANT VALUES   //
// ------------------------ //
// MQTT SETUP SETTINGS      //


////Wifi Jesse
//const char *WLAN_SSID = "Ziggo89DC852";
//const char *WLAN_ACCESS_KEY = "zhbNc5f3fjst";

// Wifi school laptop Jesse
const char *WLAN_SSID = "ESP_WIFI_B1";
const char *WLAN_ACCESS_KEY = "Wijzijnechtdebestegroep";

// Wifi max
// const char *WLAN_SSID = "Peer";
// const char *WLAN_ACCESS_KEY = "Welkom Niemand";


// CLIENT_ID
const char *MQTT_CLIENT_ID = "MQTTExampleTryout_zeer_unieke_code_PPOPKKKINBR4352Ad";

// Gegevens van de MQTT broker
const char *MQTT_BROKER_URL = "sendlab.nl";
const int MQTT_PORT = 11884;
const char *MQTT_USERNAME = "ti";
const char *MQTT_PASSWORD = "tiavans";
const int MQTT_QOS = 0;


WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);


// END OF MQTT SETTINGS     //
// ------------------------ //
// DECALRATION OF VARIABLES //


// non constant MQTT variable, for lastwil
char *mqttLastWillTopic = "";

// lock variables
int lock = 0;

// Servo controls
Servo servoHouse1;
boolean house1up = true;

Servo servoHouse2;
boolean house2up = true;

Servo servoHouse3;

// Led controls
bool ledIsOn = true;  // Voor de ingebouwde LED, soort keep-alive knipperlicht
byte ledIntensities[4] = { 0, 0, 0, 0 };

// blower controls
int currentBlow = 0;
int totalBlowPercent = 0;

// button pressed
bool button1PressedPrev = false;

bool allowBlow = false;
int timer = 0;

/**
 * moves a servo / house to the given point
 * 
 * Why does it attach and detach each time it needs to move a sero?
 * The servo we use uses 5V, provided by the ESP32. However if you use 
 * more that one servo at the same time it won't run on the ESP power, it wil damage the board.
 * 
 * To use multiple servo's you need to provid seperate power (wich we did not see as a correct option)
 * By detaching the servo after it has moved it does not draw the power anymore and we can go around the 
 * 1 servo limit.
 * 
 * dissatvantage: the servo, when not in use, is not held at its position by power (just by the gears, less strong)
 * 
 * Servo: the servo object to move
 * HOUSE_PIN: the pin the servo is on
 * degree: the position the servo should move to
 */
void moveHouseServo(Servo house, int HOUSE_PIN, int degree) {
  house.setPeriodHertz(50);                    // standard 50 hz servo
  house.attach(HOUSE_PIN, 500, 2400);
  house.write(degree);
  delay(200);
  house.detach();
}

/*
 * Resets all the value's
 */
void reset() {
  currentBlow = 0;
  totalBlowPercent = 0;

  house1up = true;
  house2up = true;

  // Reseting first servo 1
  moveHouseServo(servoHouse1, SERVO_HOUSE_1_PIN, SERVO_UP);

  // Reseting first servo 2
  moveHouseServo(servoHouse2, SERVO_HOUSE_2_PIN, SERVO_UP);

  // Reseting first servo 3
  moveHouseServo(servoHouse3, SERVO_HOUSE_3_PIN, SERVO_UP);

  ledIntensities[0] = 255;
  ledIntensities[1] = 255;
  ledIntensities[2] = 255;

  setLedIntensity(0, ledIntensities[0]);
  setLedIntensity(1, ledIntensities[1]);
  setLedIntensity(2, ledIntensities[2]);
}

/*
 * Stel de LED helderheid in
 * pull-up
 */
void setLedIntensity(int ledNr, byte intensity) {
  byte pwmValue = map(intensity, 255, 0, 0, 255);
  ledcWrite(LED_CHANNELS[ledNr], pwmValue);
}

/*
* Readsout blower and updates all the correct value's
*/
void handleBlower() {
  // Reading the blow-motor, if value changed print it
  int newBlow = analogRead(BLOW_READER_PIN) / 20;

  // upload turning speed
  if (newBlow != currentBlow) {
    char payload[30];
    sprintf(payload, "%d", newBlow);
    mqttClient.publish(MQTT_TOPIC_BLOWER, payload);

    Serial.print("blowing changed: ");
    Serial.println(payload);
  }

  // updating percentage blow
  int percent = ((newBlow / BLOW_TOTAL_COMPLETE) * 100);
  if (percent != 0) {
    if (house1up) {
      shakeHouse(servoHouse1, SERVO_HOUSE_1_PIN);
    } else if (house2up) {
      shakeHouse(servoHouse2, SERVO_HOUSE_2_PIN);
    } else {
      shakeHouse(servoHouse3, SERVO_HOUSE_3_PIN);
    }


    // publishing the blowing percent to the MQTT broker
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

/**
 * Set the diffetent LEDs
 * blowpercent > 10 == red on
 * blowpercent > 50 == amber on
 * blowpervent >= 100 = green on
 */
void handleLED()
{
  // Setting red LED
  if (totalBlowPercent > 10) 
  {
    ledIntensities[2] = 0;
  } else
  {
    ledIntensities[2] = 255;
  }

  // Setting amber LED
  if (totalBlowPercent > 50) 
  {
    ledIntensities[1] = 0;
  } else
  {
    ledIntensities[1] = 255;
  }

  // Setting green LED
  if (totalBlowPercent > 100) 
  {
    ledIntensities[0] = 0;
  } else
  {
    ledIntensities[0] = 255;
  }

  setLedIntensity(0, ledIntensities[0]);
  setLedIntensity(1, ledIntensities[1]);
  setLedIntensity(2, ledIntensities[2]);
}

/**
 * resets the blowpercentage to 0 and publishes is to the MQTT broker
 */
void clearBlow() {
  totalBlowPercent = 0;
  char payloadPer[10];
  sprintf(payloadPer, "%d", totalBlowPercent);
  mqttClient.publish(MQTT_TOPIC_TOTALBLOW, payloadPer);
}

/**
 * Shakes a given servo to the left, then right, then back to centre
 * Servo: The servo to shake
 * SERVO_PIN: the pin to servo is on
 */
void shakeHouse(Servo servo, int SERVO_PIN) {
  servo.setPeriodHertz(50);             // standard 50 hz servo
  servo.attach(SERVO_PIN, 500, 2400);  // Attach the servo after it has been detatched
  servo.write(195);
  delay(150);
  servo.write(165);
  delay(150);
  servo.write(180);
  delay(150);
  servo.detach();
}



void subscribeToBroker(const char *topic) {
  if (!mqttClient.subscribe(topic, MQTT_QOS)) {
    Serial.print("Failed to subscribe to topic ");
    Serial.println(topic);
  } else {
    Serial.print("Subscribed to topic ");
    Serial.println(topic);
  }
}

/*
 * De MQTT client callback die wordt aangeroepen bij elk bericht dat
 * we van de MQTT broker ontvangen
 */
void mqttCallback(char *topic, byte *payload, unsigned int length) {
  // Logging
  Serial.print("MQTT callback called for topic ");
  Serial.println(topic);

  if (strcmp(topic, MQTT_TOPIC_HOUSE1) == 0) {
    // turn down the first house, then clear the value's
    moveHouseServo(servoHouse1, SERVO_HOUSE_1_PIN, SERVO_DOWN);
    house1up = false;
    clearBlow();
    allowBlow = false;
  } else if (strcmp(topic, MQTT_TOPIC_HOUSE2) == 0) {
    // turn down the second house, then clear the value's
    moveHouseServo(servoHouse2, SERVO_HOUSE_2_PIN, SERVO_DOWN);
    house2up = false;
    clearBlow();
    allowBlow = false;
  } else if (strcmp(topic, MQTT_TOPIC_HOUSE3) == 0) {
    // shake the third house 5 times repeatedly
    for (int i = 0; i < 5; i++) {
      shakeHouse(servoHouse3, SERVO_HOUSE_3_PIN);
      delay(500);
    }

  } else if (strcmp(topic, MQTT_TOPIC_RESET) == 0) {
    // reset everything
    reset();
  } else if (strcmp(topic, MQTT_TOPIC_START_BLOW) == 0) {
    // allow the user to blow in the sensor
    allowBlow = true;
    clearBlow();

  } else if (strcmp(topic, MQTT_TOPIC_LOCK) == 0) {
    char validPayload[16];  
    byte value;             // Hierin komt de getalwaarde na conversie
    strncpy(validPayload, (const char *)payload, length > 16 ? 16 : length);
    // Zet de tekst om in een byte waarde, veronderstel een unsigned int in de tekst
    sscanf((const char *)validPayload, "%u", &value);

    lock = value;
    if (lock == 1) {
      timer = 0;
    }
    Serial.println(lock);
  } else if (strcmp(topic, MQTT_TOPIC_LOCK_PING) == 0) {
    char validPayload[16];
    byte value;           
    strncpy(validPayload, (const char *)payload, length > 16 ? 16 : length);
   
    sscanf((const char *)validPayload, "%u", &value);

    char payloadQ[10];
    sprintf(payloadQ, "%d", lock);

    Serial.println("lock ping, send: ");
    Serial.println(lock);
    mqttClient.publish(MQTT_TOPIC_LOCK, payloadQ);
  } else if (strcmp(topic, MQTT_TOPIC_CLEAR_BLOW) == 0) {
    clearBlow();
  } else if (strcmp(topic, MQTT_TOPIC_APP_LASTWILL) == 0) {
    Serial.print("last will");
    Serial.println(length);

    char txt[length];
    for (int i = 0; i < length + 1; i++) {
      txt[i] = '\0'; 
      }
    strncpy(txt, (const char *) payload, length);

    Serial.print("received lastwil: ");
    Serial.println(txt);

    char subText[] = "ti/1.4/b1/";
    char dest[10 + length];

    strcpy(dest, subText);
    strcat(dest, txt);

    mqttLastWillTopic = dest;
    subscribeToBroker(mqttLastWillTopic);
  } else if (strcmp(topic, mqttLastWillTopic) == 0) {
    Serial.println("received last will, reseting");
    reset();
    lock = 2;

    char payloadQ[10];
    sprintf(payloadQ, "%d", lock);

    Serial.print("lock state now is lifted ");
    Serial.println(lock);
    mqttClient.publish(MQTT_TOPIC_LOCK, payloadQ);
  }

  
}

void setup() {
  // Ingebouwde LED wordt als een soort keep-alive knipperLED gebruikt
  pinMode(LED_BUILTIN, OUTPUT);

  pinMode(BUTTON1_PIN, INPUT_PULLUP);

  //Allow allocation of all timers
  ESP32PWM::allocateTimer(0);
  ESP32PWM::allocateTimer(1);
  ESP32PWM::allocateTimer(2);
  ESP32PWM::allocateTimer(3);

  // Gekleurde LEDs worden aangestuurd met de ESP32 LED control (LEDC) library
  // Initialiseer de LED control kanalen
  for (int led = 0; led < NR_OF_LEDS; led++) {
    ledcAttachPin(LED_PINS[led], LED_CHANNELS[led]);
    ledcSetup(LED_CHANNELS[led], 12000, 8);  // 12 kHz PWM, 8-bit resolutie
    ledcWrite(LED_CHANNELS[led], ledIntensities[led]);
  }

  // Setting all servo's and value's in position
  reset();

  // Open de verbinding naar de seriële terminal
  Serial.begin(115200);
  Serial.println("ESP32 MOBAPP B!");

  // Zet de WiFi verbinding op
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  Serial.println("Connecting to ");
  Serial.println(WLAN_SSID);
  WiFi.begin(WLAN_SSID, WLAN_ACCESS_KEY);
  while (WiFi.status() != WL_CONNECTED) {
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
  if (!mqttClient.connect(MQTT_CLIENT_ID, MQTT_USERNAME, MQTT_PASSWORD, MQTT_TOPIC_LOCK, MQTT_QOS, true, "2")) {
    Serial.println("Failed to connect to MQTT broker");
  } else {
    Serial.println("Connected to MQTT broker");
  }

  // Subscribe op de house 1 topic
  subscribeToBroker(MQTT_TOPIC_HOUSE1);
  
  // Subscribe op de house 2 topic
  subscribeToBroker(MQTT_TOPIC_HOUSE2);

  // Subscribe op de house 3 topic
  subscribeToBroker(MQTT_TOPIC_HOUSE3);

  // Subscribe op de reset topic
  subscribeToBroker(MQTT_TOPIC_RESET);

  // Subscribe op de start topic
  subscribeToBroker(MQTT_TOPIC_START_BLOW);

  // Subscribe op de MQTT_TOPIC_LOCK
  subscribeToBroker(MQTT_TOPIC_LOCK);

  // Subscribe op de MQTT_TOPIC_LOCK_PING
  subscribeToBroker(MQTT_TOPIC_LOCK_PING);

  // Subscribe op de MQTT_TOPIC_CLEAR_BLOW
  subscribeToBroker(MQTT_TOPIC_CLEAR_BLOW);

  subscribeToBroker(MQTT_TOPIC_APP_LASTWILL);
}

void loop() {
  // Nodig om de MQTT client zijn werk te laten doen
  mqttClient.loop();

  // Handle blower
  if (allowBlow == true) {
    handleBlower();
  }

  // Handle LEDs
  handleLED();

  if (timer >= 30000) {
    lock = 2;

   // sending the lock open state
    char payloadQ[10];
    sprintf(payloadQ, "%d", lock);

    Serial.print("lock state now is locked: ");
    Serial.println(lock);
    mqttClient.publish(MQTT_TOPIC_LOCK, payloadQ);

    timer = 0;
  }

  // Controleer of een knop is ingedrukt en handel dat af
  bool button1PressedNow = !digitalRead(BUTTON1_PIN);
  if (lock == 2 && button1PressedNow && !button1PressedPrev) {
    // Handel knopdruk af
    lock = 0;

    char payloadQ[10];
    sprintf(payloadQ, "%d", lock);

    Serial.print("lock state now is lifted ");
    Serial.println(lock);
    mqttClient.publish(MQTT_TOPIC_LOCK, payloadQ);
  }
  button1PressedPrev = button1PressedNow;

  // Laat de ingebouwde LED knipperen
  ledIsOn = !ledIsOn;
  digitalWrite(LED_BUILTIN, ledIsOn);

  if (lock == 0) {
    timer += 150;

    Serial.print("timer now is at: ");
    Serial.println(timer);
  }
  delay(100);
}
