package adapters

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapObserveRelation
import org.eclipse.californium.core.CoapResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import it.unibo.kactor.QakContext
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking


class RealTrolleyObserver(url : String) {
    private var relation: CoapObserveRelation? = null
    private var client: CoapClient? = null
	val stepDispatch = MsgUtil.buildDispatch("tester", "error", "error(1)", "trolley_controller")
	var myactor  = QakContext.getActor("trolley_controller")
    fun observe() {
        relation = client!!.observe(
            object : CoapHandler {
                override fun onLoad(response: CoapResponse) {
                    val content = response.responseText
					if(content.contains("stepFail") ||content.contains("obstacle") ){
						runBlocking{
							MsgUtil.sendMsg(stepDispatch, myactor!!)
						}
						
					}
                   // println("ResourceObserver | value=$content")
                }

                override fun onError() {
                    System.err.println("OBSERVING FAILED (press enter to exit)")
                }
            })
    }
    init {
        client = CoapClient(url)
    }
}