package main

import outsonar.OutSonarAdapter

class SensorFactory {
	
	fun getSensorAdapter(type : SensorType) : SensorPort{
		
		when(type){
			SensorType.OUTSONAR -> return OutSonarAdapter()
			SensorType.WEIGHTSENSOR -> return OutSonarAdapter() //da cambiare
			SensorType.THERMOMETER -> return OutSonarAdapter()	//da cambiare
		}
	}
	
}
