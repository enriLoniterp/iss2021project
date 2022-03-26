package weightsensor

import java.util.concurrent.TimeUnit
import resources.SensorFactory
import resources.SensorPort

class WeightsensorController{
	lateinit var weightSensorPort : SensorPort
	lateinit var sensorFactory : SensorFactory
}

