/* Generated by AN DISI Unibo */ 
package controller

import it.unibo.kactor.QakContext
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class MainCtxcarparking

@ObsoleteCoroutinesApi
fun main(args: Array<String>) {
    runBlocking {
        launch(newSingleThreadContext("QakThread")) {
            QakContext.createContexts("localhost", this, "carparking.pl", "sysRules.pl")
        }
        launch(newSingleThreadContext("SpringThread")) {
            runApplication<MainCtxcarparking>(*args)
        }
    }

}

