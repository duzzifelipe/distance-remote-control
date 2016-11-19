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
      
      analogWrite(pin_motor, data[0]);
      wheel.write(data[1]);

      if (data[2] == 1) {
        digitalWrite(pin_way, LOW);
  
      } else {
        digitalWrite(pin_way, HIGH); 
      }
      
      Serial.println("");
    }
  } else {
    //Serial.println("waiting");
    // defaultValues();
  }
}

void defaultValues() {
  digitalWrite(pin_way, LOW);
  analogWrite(pin_motor, 1);
  wheel.write(90);
}

