package controller

import connQak.connQakBase
import connQak.connQakTcp
import it.unibo.kactor.MsgUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import resources.ParkingState

@Controller
class GuiAdapter {
    final val connParkClientService: connQakBase = connQakTcp()

    init {
        connParkClientService.createConnection("localhost", 8002)
    }

    //@Value("\${spring.application.name}")
    var appName: String? = null
    @GetMapping("/home")
    fun homePage(model: Model): String {
        return "home"
    }


    @GetMapping("/reqenter")
    fun pickUpPage(model: Model): ResponseEntity<String>  {

        var request = MsgUtil.buildRequest("springcontroller", "acceptIn", "acceptIn(X)", "park_client_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))
        //Error
        return ResponseEntity.ok(reply.msgContent)
    }



    @GetMapping("/deposit")
    fun depositPage(@RequestParam slotnum: Int): String  {
        println("/deposit")
        return "Deposit"
    }

    @GetMapping("/pickup")
    fun pickPage(): String  {
        println("/pickup")
        return "Pickup"
    }

    @GetMapping("/carenter")
    fun carenter(@RequestParam slotnum: Int): ResponseEntity<String> {
        println("/carenter")
        var request = MsgUtil.buildRequest("springcontroller", "carenter", "carenter($slotnum)", "park_client_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))
        //Error
        return ResponseEntity.ok(reply.msgContent)
    }


    @GetMapping("/reqexit")
    fun reqexit(@RequestParam tokenid: String): ResponseEntity<String> {
        println("/reqexit")
        var request = MsgUtil.buildRequest("springcontroller", "acceptOut", "acceptOut($tokenid)", "park_client_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))
        //Error
        return ResponseEntity.ok(reply.msgContent)
    }
    /*

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    @Throws(java.lang.Exception::class)
    fun greeting(message: String): String? {

        return "TAKE OUT THE CAR! TIME FINISHED"
    }

     */


    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<*> {
        val responseHeaders = HttpHeaders()
        responseHeaders.set("stato", "ERROR")
        return ResponseEntity<Any?>(
            "GuiAdapter ERROR ${ex.message}",
            responseHeaders, HttpStatus.CREATED
        )
    }

    fun send(){}


}