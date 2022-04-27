package controller

import connQak.connQakBase
import connQak.connQakTcp
import it.unibo.kactor.MsgUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import outsonar.OutSonarAdapter
import resources.ParkingState
import thermometer.ThermometerAdapter
import weightsensor.WeightSensorAdapter
import java.lang.StringBuilder


@Controller
class GuiAdapter {
    final val connParkClientService: connQakBase = connQakTcp()

    private lateinit var template: SimpMessagingTemplate
    private lateinit var oAd : OutSonarAdapter
    private lateinit var wAd : WeightSensorAdapter
    private lateinit var tAd : ThermometerAdapter

     @Autowired
     constructor (template: SimpMessagingTemplate) : this() {
         this.template = template
     }

    constructor()

    init {
        connParkClientService.createConnection("localhost", 8002)
        oAd = OutSonarAdapter()
        wAd = WeightSensorAdapter()
        tAd = ThermometerAdapter()

        tAd.addObserver {
            if(ParkingState.highTemperature) {
                template.convertAndSend("/manager/temperature", "High Temperature! Fan on")
            }else{
                template.convertAndSend("/manager/temperature", "Low Temperature! Fan off")
            }
        }

        oAd.addAlert {
            template.convertAndSend("/manager/alarm", "Too much time on the outdoor!")
        }
    }

    @GetMapping("/pickup")
    fun pickPage(): String  {
        println("/pickup")
        return "Pickup"
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
        val request = MsgUtil.buildRequest("springcontroller", "getParkingState", "getParkingState(x)", "park_manager_service")
        val reply = ApplMessageUtil.messageFromString(connParkClientService.request(request))
        return ResponseEntity.ok(reply.msgContent)
    }

    @RequestMapping("/manager/trolley")
    fun trolleystate(@RequestParam state:String): ResponseEntity<String> {
            val request = MsgUtil.buildRequest("springcontroller", "changeTrolley", "changeTrolley($state)", "park_manager_service")
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