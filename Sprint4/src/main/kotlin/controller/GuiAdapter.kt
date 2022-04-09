package controller

import connQak.connQakBase
import connQak.connQakTcp
import it.unibo.kactor.MsgUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import resources.ParkingState
import java.lang.StringBuilder


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

        val request = MsgUtil.buildRequest("springcontroller", "acceptIn", "acceptIn(X)", "park_client_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))
        //Error
        return ResponseEntity.ok(reply.msgContent)
    }



    @GetMapping("/deposit")
    fun depositPage(@RequestParam slotnum: Int): String  {
        println("/deposit")
        return "Deposit"
    }

    @GetMapping("/carenter")
    fun carenter(@RequestParam slotnum: Int): ResponseEntity<String> {
        println("/carenter")
        val request = MsgUtil.buildRequest("springcontroller", "carenter", "carenter($slotnum)", "park_client_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))
        //Error
        return ResponseEntity.ok(reply.msgContent)
    }


    @GetMapping("/reqexit")
    fun reqexit(@RequestParam tokenid: String): ResponseEntity<String> {
        println("/reqexit")
        val request = MsgUtil.buildRequest("springcontroller", "acceptOut", "acceptOut($tokenid)", "park_client_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))
        //Error
        return ResponseEntity.ok(reply.msgContent)
    }




    //GESTIONE DELLO STATO DEL PARCHEGGIO

    //richiesta dati
    @RequestMapping("/manager/parkingstate")
    fun parkingstate():ResponseEntity<String>{
        var sep = "/"
        var sb:StringBuilder = StringBuilder()

        sb.append(ParkingState.fanState)
            .append(sep)
            .append(ParkingState.trolleyState.toString())
            .append(sep)
            .append(ParkingState.indoorFree.toString())
            .append(sep)
            .append(ParkingState.outdoorFree.toString())
            .append(sep)
            .append(ParkingState.temperature.toString())
            .append(sep).append(ParkingState.slotState.toString())
        val message:JsonElement = Json.decodeFromString(sb.toString())
        print(message)
        return ResponseEntity.ok(sb.toString())
    }

    @RequestMapping("/manager/trolley")
    fun trolleystate(@RequestParam state:String): ResponseEntity<String> {
            val request = MsgUtil.buildRequest("springcontroller", "trolleyState", "trolleystate($state)", "park_client_service")
            val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))
             return ResponseEntity.ok(reply.msgContent)
    }


    ////LOGIN REQUESTS

    //loginconsuccesso
    @RequestMapping("/manager/login")
    fun perform_login() : String{
        println("ciao ciao")
        return "homeMgmt"
    }


    //logout con successo
    @RequestMapping("/manager/performlogout")
    fun perform_logout(): String {
        return "login"
    }


    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<*> {
        val responseHeaders = HttpHeaders()
        responseHeaders.set("stato", "ERROR")
        return ResponseEntity<Any?>(
            "GuiAdapter ERROR ${ex.message}",
            responseHeaders, HttpStatus.CREATED
        )
    }
}