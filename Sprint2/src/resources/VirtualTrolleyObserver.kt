package resources

import it.unibo.interaction.IssObserver
import org.json.JSONObject
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.QakContext

class VirtualTrolleyObserver :  IssObserver {
	
	val stepDispatch = MsgUtil.buildDispatch("tester", "error", "error(1)", "trolley_controller")
	var myactor  = QakContext.getActor("trolley_controller")
	
	override fun handleInfo(infoJson: String?) {
        handleInfo(JSONObject(infoJson))
    }
	
	override fun handleInfo(infoJson: JSONObject) {
        if (infoJson.has("endmove")) handleEndMove(infoJson) else if (infoJson.has("sonarName")) handleSonar(infoJson) else if (infoJson.has(
                "collision"
            )
        ) handleCollision(infoJson)
    }

    protected fun handleSonar(sonarinfo: JSONObject) {
        val sonarname = sonarinfo["sonarName"] as String
        val distance = sonarinfo["distance"] as Int
        println("RobotControllerBoundary | handleSonar:$sonarname distance=$distance")
    }

    protected fun handleCollision(collisioninfo: JSONObject?) {
		 runBlocking{
			 MsgUtil.sendMsg(stepDispatch, myactor!!)
		 }
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        //String move   = (String) collisioninfo.get("move");
        //System.out.println("RobotControllerBoundary | handleCollision move=" + move  );
    }
    protected fun handleEndMove(endmove: JSONObject) {
        val answer = endmove["endmove"] as String
        val move = endmove["move"] as String
		//println("Answer $answer")
		if(answer == "false"){
			 runBlocking{
			 MsgUtil.sendMsg(stepDispatch, myactor!!)
		 }
		}
		
    }

}