package resources

import org.json.JSONObject
import resources.IssObservableCommSupport
import resources.AnswerAvailable
import java.io.IOException
import java.lang.Exception
import java.net.URI
import javax.websocket.*

/**
 * IssWsSupport.java
 * ===============================================================
 * See also AnswerAvailable
 * ===============================================================
 */
/**
 * IssWsSupport.java
 * ===============================================================
 * Support for WS interaction with a remote server
 * The correct format of the arguments of operations forward/request
 * must be provided by the user
 * ===============================================================
 */
@ClientEndpoint //javax.websocket annotation
class IssWsSupport(url: String) : IssObservableCommSupport(), IssCommSupport {
    private var URL = "unknown"
    private var userSession: Session? = null
    private var answerSupport: AnswerAvailable? = null

    //Callback hook for Connection open events.
    @OnOpen
    fun onOpen(userSession: Session) { //, @PathParam("username") String username, EndpointConfig epConfig
        //ClientEndpointConfig clientConfig = (ClientEndpointConfig) epConfig;
        val userPrincipal = userSession.userPrincipal
        //System.out.println("        IssWsSupport | onOpen userPrincipal=" + userPrincipal );
        if (userPrincipal != null) { //there is an authenticated user
            println("        IssWsSupport | onOpen user=" + userPrincipal.name)
        }
        this.userSession = userSession
    }

    //Callback hook for Connection close events.
    @OnClose
    fun onClose(userSession: Session?, reason: CloseReason?) {
        println("IssWsSupport | closing websocket")
        this.userSession = null
    }

    //Callback hook for Message Events.
    //THe WENv system sends always an answer for any command sent to it
    @OnMessage
    fun onMessage(message: String?) {
        try {
            //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
            //System.out.println("        IssWsSupport | onMessage:" + message);
            val jsonObj = JSONObject(message)
            if (jsonObj.has("endmove")) {
                //HANDLE THE ANSWER
                val endmove = jsonObj.getString("endmove")
                val move = jsonObj.getString("move")
                if (endmove != "notallowed") answerSupport!!.put(endmove, move)
                //System.out.println("        IssWsSupport | onMessage endmove=" + endmove);
            } else if (jsonObj.has("collision")) {
                val collision = jsonObj.getBoolean("collision")
                //System.out.println("        IssWsSupport | onMessage collision=" + collision );
            } else if (jsonObj.has("sonarName")) {
                //String sonarName = jsonObj.getString( "sonarName");
                //String distance  = jsonObj.get("distance").toString();
                //System.out.println("        IssWsSupport | onMessage sonarName=" + sonarName + " distance=" + distance);
            }
            updateObservers(jsonObj) //Requires time to update all ...
            //Why we must wait for the execution of all the observers?
        } catch (e: Exception) {
            println("        IssWsSupport | onMessage ERROR " + e.message)
        }
    }

    @OnError
    fun disconnected(session: Session?, error: Throwable) {
        println("IssWsSupport | disconnected  " + error.message)
    }

    //------------------------------ IssOperations ----------------------------------
    override fun forward(msg: String) {
        try {
            //System.out.println("        IssWsSupport | forward:" + msg);
            //this.userSession.getAsyncRemote().sendText(message);
            userSession!!.basicRemote.sendText(msg) //synch: blocks until the message has been transmitted
            //System.out.println("        IssWsSupport | DONE forward " + msg);
        } catch (e: Exception) {
            println("        IssWsSupport | ERROR forward  " + e.message)
        }
    }

    override fun request(msg: String) {
        try {
            //System.out.println("        IssWsSupport | request " + msg);
            //this.userSession.getAsyncRemote().sendText(message);
            userSession!!.basicRemote.sendText(msg) //synch: blocks until the message has been transmitted
        } catch (e: Exception) {
            println("        IssWsSupport | request ERROR " + e.message)
        }
    }

    override fun requestSynch(msg: String): String {
        return try {
            //System.out.println("        IssWsSupport | requestSynch " + msg);
            //this.userSession.getAsyncRemote().sendText(message);
            request(msg)
            //WAIT for the answer (reply) received by onMessage
            //answerSupport.engage();   //OVERCOMED: see version 2.0 of virtualrobot
            answerSupport!!.get() //wait for the answer
        } catch (e: Exception) {
            println("        IssWsSupport | request ERROR " + e.message)
            "error"
        }
    }

    override fun reply(msg: String) {
        //System.out.println( "         IssWsSupport | WARNING: reply NOT IMPLEMENTED HERE"  );
    }

    //------------------------------ IssCommSupport ----------------------------------
    override fun close() {
        try {
            userSession!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    init {
        try {
            URL = url
            val container = ContainerProvider.getWebSocketContainer()
            container.connectToServer(this, URI("ws://$url"))
            answerSupport = AnswerAvailable()
        } catch (e: Exception) {
            println("IssWsSupport | ERROR: " + e.message)
        }
    }
}