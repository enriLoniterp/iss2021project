package Sonar

import java.util.concurrent.TimeUnit
import main.SensorFactory
import main.SensorPort

class SonarController{
	lateinit var sonar : SensorPort
	lateinit var sensorFactory : SensorFactory
}

