package it.unibo.webspring.demo

import org.json.JSONObject
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException

@Component
class SocketTextHandler : TextWebSocketHandler() {
    @Throws(InterruptedException::class, IOException::class)
    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        val jsonObject = JSONObject(payload)
        session.sendMessage(TextMessage("Hi " + jsonObject["user"] + " how may we help you?"))
    }


}