package it.unibo.webspring.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.HtmlUtils

@Controller
class BaseController {
    //final val connParkClientService: connQakBase = connQakTcp()

    @Value("\${spring.application.name}")
    var appName: String? = null
    @GetMapping("/home")
    fun homePage(model: Model): String {
        return "Home"
    }

    init {
       // connParkClientService.createConnection("localhost", 8023)
    }


    @GetMapping("/reqenter")
    fun pickUpPage(model: Model): ResponseEntity<Int>  {

        //interazione con clientService

        println("logica...c'è posto....\n")
        return ResponseEntity.ok(3)
    }



    @GetMapping("/deposit")
    fun depositPage(@RequestParam slotnum: Int): String  {

        //interazione con clientService
        println("/deposit")
        return "Deposit"
    }

    @GetMapping("/pickup")
    fun pickupPage(): String  {

        println("/pickup")
        return "Pickup"
    }

    @GetMapping("/carenter")
    fun carenter(@RequestParam slotnum: Int): ResponseEntity<String> {
       // val TOKENID = "ABC"
        println("/carenter")
        return ResponseEntity.ok("abc")
    }


    @GetMapping("/reqexit")
    fun reqexit(@RequestParam tokenid: String): ResponseEntity<Int> {

        println("/reqexit")
        println("la tua macchina è in fase di trasporto")

        return ResponseEntity.ok(0)
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    @Throws(java.lang.Exception::class)
    fun greeting(message: Int): Int? {
        Thread.sleep(5000) // simulated delay
        return 10
    }


    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<*> {
        val responseHeaders = HttpHeaders()
        responseHeaders.set("stato", "ERROR")
        return ResponseEntity<Any?>(
            "BaseController ERROR ${ex.message}",
            responseHeaders, HttpStatus.CREATED
        )
    }


}