package thermometer

import java.util.concurrent.TimeUnit
import resources.SensorFactory
import resources.SensorPort

class ThermometerController{
	lateinit var thermometer : SensorPort
	lateinit var sensorFactory : SensorFactory
}

