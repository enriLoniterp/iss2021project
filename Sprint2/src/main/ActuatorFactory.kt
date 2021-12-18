package main

import transportTrolley.TransportTrolleyAdapter
import fan.FanAdapter


class ActuatorFactory {
	
	fun getActuatorAdapter(type : ActuatorType) : ActuatorPort{
		
		when(type){
			ActuatorType.TROLLEY -> return TransportTrolleyAdapter()
			ActuatorType.FAN -> return FanAdapter()//
		}
	}
	
}
