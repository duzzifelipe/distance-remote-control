#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
#include <Ultrasonic.h>
#include <math.h>

#define ultra_pin_trigger_1 4
#define ultra_pin_echo_1 5
#define ultra_pin_trigger_2 6
#define ultra_pin_echo_2 7
#define rf_ce 9
#define rf_cs 10
#define max_distance_turn 50
#define max_distance_speed 50
#define min_distance 5
#define max_value_serial 255
#define min_value_serial 0

Ultrasonic ultrasonic_1(ultra_pin_trigger_1, ultra_pin_echo_1);
Ultrasonic ultrasonic_2(ultra_pin_trigger_2, ultra_pin_echo_2);
RF24 radio(rf_ce,rf_cs);

int data[2];
const uint64_t pipe = 0xE8E8F0F0E1LL;

void setup(){
  Serial.begin(9600);
  Serial.println("Reading sensors data...");
  radio.begin();
  radio.openWritingPipe(pipe);
}

void loop(){
  // send to serial and radio
  send_all();
  
  float dist_1 = get_distance(ultrasonic_1);
  float dist_2 = get_distance(ultrasonic_2);
  
  data[0] = calc_accelerate(dist_1);
  data[1] = calc_turn(dist_2);
}

float get_distance(Ultrasonic ultra) {
  long microsec = ultra.timing();
  return ultra.convert(microsec, Ultrasonic::CM);
}

int calc_accelerate(float dist) {
  float value = 0.0f;
  
  if (dist > max_distance_speed || dist < min_distance) {
    value = min_value_serial;
    
  } else {
    value = (max_value_serial / max_distance_speed) * dist; 
  }

  return round(value);
}

int calc_turn(float dist) {
  float value = 0.0f;
  
  if ((dist > max_distance_turn) || (dist < min_distance)) {
    value = max_value_serial / 2;
    
  } else {
    value = (max_value_serial / max_distance_turn) * dist; 
  }

  return round(value);
}

boolean send_all() {
  radio.write(data, sizeof(data));
  Serial.println("p1:" + String(data[0], DEC));
  Serial.println("p2:" + String(data[1], DEC));
}

