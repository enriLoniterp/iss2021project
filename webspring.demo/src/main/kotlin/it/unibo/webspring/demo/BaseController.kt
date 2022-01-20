package it.unibo.webspring.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


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

        println("CIAOOOOO\n")
        println("logica...c'è posto....\n\n")

        return ResponseEntity.ok(3) //ovviamente numero a caso, E' UNA PROVA
    }



    /*
    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<*> {
        val responseHeaders = HttpHeaders()
        return ResponseEntity<Any?>(
            "BaseController ERROR ${ex.message}",
            responseHeaders, HttpStatus.CREATED
        )
    }
    */

}