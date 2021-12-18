package fan

import com.andreapivetta.kolor.green
import com.andreapivetta.kolor.magenta
import com.andreapivetta.kolor.red
import org.eclipse.californium.core.CoapResource
import org.eclipse.californium.core.coap.CoAP
import org.eclipse.californium.core.server.resources.CoapExchange
import org.eclipse.californium.core.CoapServer

class FanMock() : CoapResource("fan") {
    var state: Boolean = false

    constructor(defaultValue: Boolean) : this() {
        state = defaultValue
    }

    override fun handleGET(exchange: CoapExchange) {
        exchange.respond("$state")
    }

    override fun handlePUT(exchange: CoapExchange) {
        when(exchange.requestText) {
            "ON" -> {
				state = true
                exchange.respond(CoAP.ResponseCode.CHANGED, "$state")
            }
            "OFF" -> {
				state = false
                exchange.respond(CoAP.ResponseCode.CHANGED, "$state")
            }
            else -> exchange.respond(CoAP.ResponseCode.BAD_REQUEST)
        }
    }
}

fun main(args : Array<String>) {
	lateinit var fanMock: FanMock
	fanMock = FanMock(false)
	val server = CoapServer(8003)
	server.add(CoapResource("parkingarea").add(fanMock))
    server.start()
}
	

