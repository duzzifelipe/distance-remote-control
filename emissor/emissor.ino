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

float max_distance_turn = 50.0f;
float max_distance_speed = 50.0f;
float min_distance = 5.0f;

Ultrasonic ultrasonic_1(ultra_pin_trigger_1, ultra_pin_echo_1);
Ultrasonic ultrasonic_2(ultra_pin_trigger_2, ultra_pin_echo_2);
RF24 radio(rf_ce,rf_cs);

int data[3];
const uint64_t pipe = 0xE8E8F0F0E1LL;
long previous = 0;

void setup(){
  data[2] = 1; // starts with forward
  Serial.begin(57600);
  radio.begin();
  radio.openWritingPipe(pipe);
  radio.setPALevel(RF24_PA_MAX);
  // radio.setDataRate(RF24_250KBPS);
  radio.setChannel(108);
  send_all();
}

void loop(){
  // send to serial and radio
  unsigned long currentTime = millis();
  if (currentTime - previous >= 250) {
    // send
    send_all(); 
    // save time
    previous = currentTime;
  }

  // receive serial
  read_serial();
  
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
    value = 0.0f;
    
  } else {
    value = (255.0f / max_distance_speed) * dist; 
  }

  return round(value);
}

int calc_turn(float dist) {
  float value;
  
  if ((dist > max_distance_turn) || (dist < min_distance)) {
    value = 90.0f;
    
  } else {
    value = (180.0f / max_distance_turn) * dist; 
  }

  // Limit max value
  if (value < 20.0f) {
    value = 20.0f;
  } else if (value > 160.0f) {
    value = 160.0f;
  }

  return round(value);
}

boolean send_all() {
  radio.write(data, sizeof(data));
  Serial.println("p1:" + String(data[0], DEC));
  Serial.println("p2:" + String(data[1], DEC));
  Serial.println("d:" + String(data[2], DEC));
}

void read_serial() {
  if(Serial.available() > 0) {
    int serial_data = Serial.read();
    data[2] = serial_data;
  }
}

