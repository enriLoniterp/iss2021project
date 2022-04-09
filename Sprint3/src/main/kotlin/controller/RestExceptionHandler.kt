package controller

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.core.annotation.Order
import org.springframework.core.Ordered
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.http.ResponseEntity

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {



}
