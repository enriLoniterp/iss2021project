package main

import Sonar.SonarAdapter

class SensorFactory {
	
	fun getSensorAdapter(type : SensorType) : SensorPort{
		
		when(type){
			SensorType.SONAR -> return SonarAdapter()
			SensorType.WEIGHTSENSOR -> return SonarAdapter() //da cambiare
			SensorType.THERMOMETER -> return SonarAdapter()	//da cambiare
		}
	}
	
}
