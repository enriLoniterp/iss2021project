package main

import adapters.TrolleyAdapter
import adapters.FanAdapter


class ActuatorFactory {
	
	fun getActuatorAdapter(type : ActuatorType) : ActuatorPort{
		
		when(type){
			ActuatorType.TROLLEY -> return TrolleyAdapter()
			ActuatorType.FAN -> return FanAdapter()//
		}
	}
	
}
