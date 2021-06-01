/*
  Button

  Prints shit( or does smething,
  when pressing a pushbutton attached to pin 2.

  The circuit:
  - pushbutton attached to pin 2 from +3.3V
  - 10K resistor attached to pin 2 from ground

  - Note: on most Arduinos there is already an LED on the board
    attached to pin 13.
*/

// constants won't change. They're used here to set pin numbers:
const int buttonPin = 2;     // the number of the pushbutton pin
const int ledPin = 4;

// variables will change:
int newButtonState = 0;         // variable for reading the pushbutton status
int oldButtonState = 0;         // variable for comparing the new and old states
int counter = 0;

void setup() {
  // initialize the pushbutton pin as an input:
  pinMode(buttonPin, INPUT);
  pinMode(ledPin, OUTPUT);
}

void loop() {
  // read the state of the pushbutton value:
  newButtonState = digitalRead(buttonPin);

  //sprintf(newButtonState);

  if (newButtonState == HIGH && oldButtonState == LOW) {
    printf("beep ");
    digitalWrite(ledPin, HIGH);
  } else if (newButtonState == LOW && oldButtonState == HIGH) {
    printf("boop ");
    digitalWrite(ledPin, LOW);
  }
  oldButtonState = newButtonState;
  delay(100);
}
