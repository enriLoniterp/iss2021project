/* Generated by AN DISI Unibo */ 
package it.unibo.trolley

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Trolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("trolley STARTS")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("trolley waiting ........... ")
					}
					 transition(edgeName="t02",targetState="tMoveToIn",cond=whenRequest("moveToIn"))
					transition(edgeName="t03",targetState="tMoveToSlotIn",cond=whenRequest("moveToSlotIn"))
					transition(edgeName="t04",targetState="tMoveToSlotOut",cond=whenRequest("moveToSlotOut"))
					transition(edgeName="t05",targetState="tMoveToOut",cond=whenRequest("moveToOut"))
					transition(edgeName="t06",targetState="tMoveToHome",cond=whenRequest("backToHome"))
				}	 
				state("tMoveToIn") { //this:State
					action { //it:State
						println("trolley moving to INDOOR ")
						delay(500) 
						println("trolley moved to INDOOR ")
						updateResourceRep( "moved(indoor)"  
						)
						answer("moveToIn", "movedToIn", "movedToIn(success)"   )  
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("tMoveToSlotIn") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("moveToSlotIn(X,Y)"), Term.createTerm("moveToSlotIn(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var H = payloadArg(0).toInt() 
												var G = payloadArg(1).toInt()
								println("trolley moving to coordinate ($H, $G)")
								delay(500) 
								println("trolley moved to coordinate ($H, $G)")
								updateResourceRep( "moved(slotIn)"  
								)
								answer("moveToSlotIn", "movedToSlotIn", "movedToSlotIn(success,$H,$G)"   )  
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("tMoveToSlotOut") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("moveToSlotOut(X,Y)"), Term.createTerm("moveToSlotOut(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var G = payloadArg(0).toInt() 
												var H = payloadArg(1).toInt()
								println("trolley moving to coordinate ($G, $H)")
								delay(500) 
								println("trolley moved to coordinate ($G, $H)")
								updateResourceRep( "moved(slotOut)"  
								)
								answer("moveToSlotOut", "movedToSlotOut", "movedToSlotOut(success,$G,$H)"   )  
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("tMoveToOut") { //this:State
					action { //it:State
						println("trolley moving to OUTDOOR ")
						delay(500) 
						println("trolley moved to OUTDOOR ")
						updateResourceRep( "moved(out)"  
						)
						answer("moveToOut", "movedToOut", "movedToOut(success)"   )  
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("tMoveToHome") { //this:State
					action { //it:State
						println("trolley moving to home ")
						delay(500) 
						println("trolley moved to home ")
						updateResourceRep( "moved(home)"  
						)
						answer("backToHome", "movedToHome", "movedToHome(success)"   )  
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
