package resources

import adapters.FanAdapter
import adapters.TrolleyAdapter


class ActuatorFactory {
	
	fun getActuatorAdapter(type : ActuatorType) : ActuatorPort{
		
		when(type){
			ActuatorType.TROLLEY -> return TrolleyAdapter()
			ActuatorType.FAN -> return FanAdapter()//
		}
	}
	
}
