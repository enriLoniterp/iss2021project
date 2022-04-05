package controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils
import resources.ParkingState


@Controller
class GreetingController {
    @MessageMapping("/fanstate")
    @SendTo("/topic/temperature")
    @kotlin.jvm.Throws(Exception::class)
    fun temperature(message: String) {
        //Thread.sleep(1000) // simulated delay
        ParkingState.fanState=message
    }

    @MessageMapping("/trolleystate")
    @kotlin.jvm.Throws(Exception::class)
    fun trolleystate(message: String) {
        //message changed
        ParkingState.trolleyState=message
        print("trolley state changed in:" + message + "\n")
    }
}