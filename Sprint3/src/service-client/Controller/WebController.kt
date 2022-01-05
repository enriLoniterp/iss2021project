package com.example.servingwebcontent

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping

@Controller
class GreetingController {

    @GetMapping("/")
    fun ClientView(
        @RequestParam(name = "name", required = false, defaultValue = "World") name: String?,
        model: org.springframework.ui.Model): String {

        model.addAttribute("name", name)
        return "ClientHomePage"
    }

    @GetMapping("/clientIn")
    fun clientInHomepage(): String {
        return "ClientInHomePage"
    }

    @GetMapping("/clientOut")
    fun clientOutHomepage(): String {
        return "ClientOutHomePage"
    }


}