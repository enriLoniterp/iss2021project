package main

import Sonar.SonarAdapter
import Thermometer.ThermometerAdapter
import WeightSensor.WeightSensorAdapter


class SensorFactory {
	
	fun getSensorAdapter(type : SensorType) : SensorPort{
		
		when(type){
			SensorType.SONAR -> return SonarAdapter()
			SensorType.WEIGHTSENSOR -> return WeightSensorAdapter() //da cambiare
			SensorType.THERMOMETER -> return ThermometerAdapter()	//da cambiare
		}
	}
	
}
