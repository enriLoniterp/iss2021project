package it.unibo.webspring.demo

import connQak.connQakBase
import connQak.connQakTcp
import it.unibo.kactor.MsgUtil
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
    final val connParkClientService: connQakBase = connQakTcp()

    init {
        connParkClientService.createConnection("localhost", 8002)
    }

    @Value("\${spring.application.name}")
    var appName: String? = null
    @GetMapping("/home")
    fun homePage(model: Model): String {
        return "Home"
    }


    @GetMapping("/reqenter")
    fun pickUpPage(model: Model): ResponseEntity<Int>  {
        var request = MsgUtil.buildRequest("springcontroller", "acceptIn", "acceptIn(X)", "park_client_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))

        //Error
        //interazione con clientService
        return ResponseEntity.ok(reply.msgContent.toInt())
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
        var request = MsgUtil.buildRequest("springcontroller", "carenter", "carenter($slotnum)", "park_client_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))

        //Error

        return ResponseEntity.ok(reply.msgContent)
        println("/carenter")

    }


    @GetMapping("/reqexit")
    fun reqexit(@RequestParam tokenid: String): ResponseEntity<String> {
        var request = MsgUtil.buildRequest("springcontroller", "acceptOut", "acceptOut($tokenid)", "park_client_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))

        //Error
        println("/reqexit")
        println("la tua macchina è in fase di trasporto")

        return ResponseEntity.ok(reply.msgContent)
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    @Throws(java.lang.Exception::class)
    fun greeting(message: String): String? {

        return "TAKE OUT THE CAR! TIME FINISHED"
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