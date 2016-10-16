#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>

#define rf_ce 9
#define rf_cs 10

RF24 radio(rf_ce,rf_cs);

int data[2];
const uint64_t pipe = 0xE14BC8F482LL;

void setup(){
  Serial.begin(57600);
  radio.begin();
  radio.openReadingPipe(1, pipe);
  radio.startListening();
}


void loop() {
  //Verifica se ha sinal de radio
  if (radio.available()) {
    bool done = false;    
    while (!done) {
      done = radio.read(data, 1);
      Serial.println(data[0]);
    }
  } else {
    Serial.println("Waiting!");
  }
}
