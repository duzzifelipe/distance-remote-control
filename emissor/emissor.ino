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
#define max_distance_turn 35
#define max_distance_speed 65
#define min_distance 5
#define max_distance 200

Ultrasonic ultrasonic_1(ultra_pin_trigger_1, ultra_pin_echo_1);
Ultrasonic ultrasonic_2(ultra_pin_trigger_2, ultra_pin_echo_2);
RF24 radio(rf_ce,rf_cs);

int data[2];
const uint64_t pipe = 0xE14BC8F482LL;

void setup(){
  Serial.begin(9600);
  Serial.println("Reading sensors data...");
  radio.begin();
  radio.openWritingPipe(pipe);
}

void loop(){
  float dist_1 = get_distance(ultrasonic_1);
  float dist_2 = get_distance(ultrasonic_2);
  
  data[0] = calc_accelerate(dist_1);
  data[1] = calc_turn(dist_2);
 
  Serial.print("Distancia em cm 1: ");
  Serial.println(data[0]);
  Serial.print("Distancia em cm 2: ");
  Serial.println(data[1]);

  delay(300);
}

float get_distance(Ultrasonic ultra) {
  long microsec = ultra.timing();
  return ultra.convert(microsec, Ultrasonic::CM);
}

int calc_accelerate(float dist) {
  if (dist > max_distance_turn && dist <= max_distance) {
    return 255;
  
  } else if (dist < min_distance || dist > max_distance) {
    return 0;
  
  } else {
    return 128; 
  }
}

int calc_turn(float dist) {
  if ((dist > max_distance_turn && dist <= max_distance) || (dist < min_distance)) {
    return 128;
    
  } else
    return 1; 
  }
}

