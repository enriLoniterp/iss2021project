package it.unibo.webspring.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


@Controller
class BaseController {
    @Value("\${spring.application.name}")
    var appName: String? = null
    @GetMapping("/home")
    fun homePage(model: Model): String {
        return "Home"
    }


    @GetMapping("/reqenter")
    fun pickUpPage(model: Model): ResponseEntity<Int>  {

        //interazione con clientService

        println("logica...c'è posto....\n")
        return ResponseEntity.ok(3) //ovviamente numero a caso, PROVA
    }

    @GetMapping("/reqenter/deposit")
    fun depositPage(model: Model): String  {

        //interazione con clientService

        println("logica...c'è posto....\n")
        return "Deposit" //ovviamente numero a caso, PROVA
    }

    @GetMapping("/client/carenter")
    fun carenter(@RequestParam slotnum: Int): ResponseEntity<Int> {

        return ResponseEntity.ok(3)
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