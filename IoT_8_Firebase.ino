/*
 * Created by K. Suwatchai (Mobizt)
 * 
 * Email: k_suwatchai@hotmail.com
 * 
 * Github: https://github.com/mobizt
 * 
 * Copyright (c) 2019 mobizt
 * 
 * This example is for the beginner
 *
*/


#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

//1. Change the following info
#define FIREBASE_HOST "iot-71160036.firebaseio.com"
#define FIREBASE_AUTH "pvlVBKfUOg2hSl7eBo636fb04jBUoOAWhjsoI1AA"
#define WIFI_SSID "SSID"
#define WIFI_PASSWORD "PASSWORD"

#define led1 14
#define led2 13
#define btn 12
#define ldr A0

//2. Define FirebaseESP8266 data object for data sending and receiving
FirebaseData firebaseData;

String path = "/Node1";
int oldAdcLdr;
int newAdcLdr;
int btnPress;

void setup()
{

  Serial.begin(115200);

  pinMode (led1,OUTPUT);
  pinMode (led2,OUTPUT);
  pinMode (ldr,INPUT);
  pinMode (btn, INPUT);
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();


  //3. Set your Firebase info

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  //4. Enable auto reconnect the WiFi when connection lost
  Firebase.reconnectWiFi(true);

  oldAdcLdr = analogRead(ldr);
  
}

void loop() {
  delay (100);
  btnPress = digitalRead(btn);
  if(btnPress == 1){
      Firebase.setInt(firebaseData, path + "/btn", btnPress);
    }else if (btnPress == 0){
      Firebase.setInt(firebaseData, path + "/btn", btnPress);
    }

   if(Firebase.getInt(firebaseData, path + "/btn")){
    if(firebaseData.intData()==1)
      Firebase.setInt(firebaseData, path + "/lampu2", 1);
      else
      Firebase.setInt(firebaseData, path + "/lampu2", 0);
    }

  
  newAdcLdr = analogRead(ldr);
  Serial.print("newAdcLdr: ");
  Serial.println(newAdcLdr);
  double volLdr = (newAdcLdr*3.3)/4095.0;
  Serial.print("volLdr: ");
  Serial.println(volLdr);
  if(newAdcLdr != oldAdcLdr){
    Firebase.setInt(firebaseData, path + "/ldr", newAdcLdr);
    oldAdcLdr = newAdcLdr;
  }

  if(Firebase.getInt(firebaseData, path + "/ldr")){
    if(firebaseData.intData()>=350)
      Firebase.setInt(firebaseData, path + "/lampu1", 1);
      else
      Firebase.setInt(firebaseData, path + "/lampu1", 0);
    }

  if(Firebase.getInt(firebaseData, path + "/lampu1")){
    if(firebaseData.intData() == 0)
      digitalWrite(led1,0);
    else
      digitalWrite(led1,1);
  }
  if(Firebase.getInt(firebaseData, path + "/lampu2")){
    if(firebaseData.intData() == 0)
      digitalWrite(led2,0);
    else
      digitalWrite(led2,1);
  }
}
