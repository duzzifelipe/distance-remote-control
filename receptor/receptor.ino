#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
#include <Servo.h>

#define rf_ce 9
#define rf_cs 10

RF24 radio(rf_ce,rf_cs);
Servo wheel;

int data[3];
int pin_way = 2;
int pin_motor = 3;
int pin_wheel = 5;
const uint64_t pipe = 0xE8E8F0F0E1LL;

void setup(){
  pinMode(pin_way, OUTPUT);
  pinMode(pin_motor, OUTPUT);
  wheel.attach(pin_wheel);
  
  defaultValues();
  
  Serial.begin(9600);
  radio.begin();
  radio.openReadingPipe(1, pipe);
  radio.setPALevel(RF24_PA_MAX);
  radio.setDataRate(RF24_250KBPS);
  radio.setChannel(108);
  radio.startListening();
}


void loop() {
  //Verifica se ha sinal de radio
  if (radio.available()) {
    bool done = false;    
    while (!done) {
      done = radio.read(data, sizeof(data));
      setMotor(data[0]);
      setWheel(data[1]);
      setWay(data[2]);
    }
  } else {
    defaultValues();
  }
}

void setMotor(int value) {
  analogWrite(pin_motor, value);
}

void setWheel(int value) {
  wheel.write(value);
}

void setWay(int value) {
  if (value == 1) {
    digitalWrite(pin_way, HIGH);
  
  } else {
    digitalWrite(pin_way, LOW); 
  }
}

void defaultValues() {
  digitalWrite(pin_way, LOW);
  analogWrite(pin_motor, 0);
  wheel.write(90);
}

