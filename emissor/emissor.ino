#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
#include <Ultrasonic.h>

#define ultra_pin_trigger_1 4
#define ultra_pin_echo_1 5
#define ultra_pin_trigger_2 6
#define ultra_pin_echo_2 7
#define rf_ce 9
#define rf_cs 10

Ultrasonic ultrasonic_1(ultra_pin_trigger_1, ultra_pin_echo_1);
Ultrasonic ultrasonic_2(ultra_pin_trigger_2, ultra_pin_echo_2);
RF24 radio(rf_ce,rf_cs);

float dados[2];
const uint64_t pipe = 0xE14BC8F482LL;

void setup(){
  Serial.begin(9600);
  Serial.println("Lendo dados do sensor...");
  radio.begin();
  radio.openWritingPipe(pipe);
}

void loop(){
  float dist_1 = get_distance(ultrasonic_1);
  float dist_2 = get_distance(ultrasonic_2);
 
  Serial.print("Distancia em cm 1: ");
  Serial.println(dist_1);
  Serial.print("Distancia em cm 2: ");
  Serial.println(dist_2);
}

float get_distance(Ultrasonic ultra) {
  long microsec = ultra.timing();
  return ultra.convert(microsec, Ultrasonic::CM);
}

