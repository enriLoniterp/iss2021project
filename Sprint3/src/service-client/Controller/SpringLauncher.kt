import it.unibo.kactor.QakContext
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebComunication

fun main(args: Array<String>) {
    runBlocking {
        launch(newSingleThreadContext("SpringThread")) {
            runApplication<WebComunication>(*args)
        }
    }
}

// launch(newSingleThreadContext("QakThread")) {
//    QakContext.createContexts("localhost", this, "carparking.pl", "sysRules.pl")
// }