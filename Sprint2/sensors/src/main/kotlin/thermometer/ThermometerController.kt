package thermometer

import java.util.concurrent.TimeUnit
import main.SensorFactory
import main.SensorPort

class ThermometerController{
	lateinit var thermometer : SensorPort
	lateinit var sensorFactory : SensorFactory
}

