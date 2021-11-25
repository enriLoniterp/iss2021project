package main

import Sonar.SonarAdapter
import Thermometer.ThermometerAdapter
import Thermometer.WeightsensorController
import org.eclipse.paho.client.mqttv3.MqttCallback
import WeightSensor.WeightSensorCallback
import Thermometer.WeightSensorAdapter


class mainnn {


}

		fun main(){
			var weight:WeightsensorController=WeightsensorController()
			var wa:SensorPort=WeightSensorAdapter()
			weight.sensorFactory=SensorFactory()
			wa=weight.sensorFactory.getSensorAdapter(SensorType.WEIGHTSENSOR)		
			System.out.println(wa.toString())		
		
	}