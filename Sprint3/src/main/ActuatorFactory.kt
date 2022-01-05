package main

import adapters.TransportTrolleyAdapter
import adapters.FanAdapter


class ActuatorFactory {
	
	fun getActuatorAdapter(type : ActuatorType) : ActuatorPort{
		
		when(type){
			ActuatorType.TROLLEY -> return TransportTrolleyAdapter()
			ActuatorType.FAN -> return FanAdapter()//
		}
	}
	
}
