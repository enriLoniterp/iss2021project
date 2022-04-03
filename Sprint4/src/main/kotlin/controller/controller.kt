package controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils


@Controller
class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/temperature")
    @kotlin.jvm.Throws(Exception::class)
    fun temperature(message: String): String {
        //Thread.sleep(1000) // simulated delay
        val temperatura=""
        return temperatura
    }
}