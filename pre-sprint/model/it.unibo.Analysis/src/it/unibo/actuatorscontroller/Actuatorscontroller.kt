/* Generated by AN DISI Unibo */ 
package it.unibo.actuatorscontroller

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Actuatorscontroller ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var pb = true  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("actuatorscontroller STARTS")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("actuatorscontroller waiting .................. ")
					}
					 transition(edgeName="t015",targetState="moveTrolleyToIn",cond=whenRequest("moveToIn"))
					transition(edgeName="t016",targetState="moveTrolleyToSlotIn",cond=whenRequest("moveToSlotIn"))
					transition(edgeName="t017",targetState="moveTrolleyToSlotOut",cond=whenRequest("moveToSlotOut"))
					transition(edgeName="t018",targetState="moveTrolleyToOut",cond=whenRequest("moveToOut"))
					transition(edgeName="t019",targetState="moveTrolleyToHome",cond=whenRequest("backToHome"))
				}	 
				state("moveTrolleyToIn") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("moveToIn(MOVETOIN)"), Term.createTerm("moveToIn(MTOIN)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("actuatorscontroller moves the Trolley to the INDOOR ")
								request("tMoveToIn", "tMoveToIn(move)" ,"trolley" )  
						}
					}
					 transition(edgeName="t020",targetState="checkTMovedToIn",cond=whenReply("tMovedToIn"))
				}	 
				state("checkTMovedToIn") { //this:State
					action { //it:State
						pb = true  
						if( checkMsgContent( Term.createTerm("tMovedToIn(SUCCESS)"), Term.createTerm("tMovedToIn(MTOIN)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var m =  payloadArg(0)  
								if(  m != "problem"  
								 ){println("actuatorscontroller: car picked up by the Trolley ")
								answer("moveToIn", "movedToIn", "movedToIn(moved)"   )  
								}
								else
								 {println("actuatorscontroller: car not picked up by the Trolley ")
								 answer("moveToIn", "movedToIn", "movedToIn(problem)"   )  
								 }
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("moveTrolleyToSlotIn") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("moveToSlotIn(X,Y)"), Term.createTerm("moveToSlotIn(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var X = payloadArg(0).toInt()
												var Y = payloadArg(1).toInt()
								println("actuatorscontroller move the Trolley (with the car) to the parking-slot in ($X,$Y) ")
								request("tMoveToSlotIn", "tMoveToSlotIn($X,$Y)" ,"trolley" )  
						}
					}
					 transition(edgeName="t021",targetState="checkTMovedToSlotIn",cond=whenReply("tMovedToSlotIn"))
				}	 
				state("checkTMovedToSlotIn") { //this:State
					action { //it:State
						pb = true  
						if( checkMsgContent( Term.createTerm("tMovedToSlotIn(SUCCESS,X,Y)"), Term.createTerm("tMovedToSlotIn(SUCCESS,X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var m =  payloadArg(0)  
								if(  m != "problem"  
								 ){println("actuatorscontroller: car parked")
								answer("moveToSlotIn", "movedToSlotIn", "movedToSlotIn(moved)"   )  
								}
								else
								 {println("actuatorscontroller: car not parked")
								 answer("moveToSlotIn", "movedToSlotIn", "movedToSLotIn(problem)"   )  
								 }
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("moveTrolleyToSlotOut") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("moveToSlotOut(X,Y)"), Term.createTerm("moveToSlotOut(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var X = payloadArg(0).toInt()
												var Y = payloadArg(1).toInt()
								println("actuatorscontroller: move the Trolley to the parking-slot in ($X,$Y) ")
								request("tMoveToSlotOut", "tMoveToSlotOut($X,$Y)" ,"trolley" )  
						}
					}
					 transition(edgeName="t022",targetState="checkTMovedToSlotOut",cond=whenReply("tMovedToSlotOut"))
				}	 
				state("checkTMovedToSlotOut") { //this:State
					action { //it:State
						pb = true  
						if( checkMsgContent( Term.createTerm("tMovedToSlotOut(SUCCESS,X,Y)"), Term.createTerm("tMovedToSlotOut(SUCCES,X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var m =  payloadArg(0)  
								if(  m != "problem"  
								 ){println("actuatorscontroller: trolley moved to slot out")
								answer("moveToSlotOut", "movedToSlotOut", "movedToSlotOut(moved,3,2)"   )  
								}
								else
								 {println("actuatorscontroller: trolley not moved to slot out")
								 answer("moveToSlotOut", "movedToSlotOut", "movedToSlotOut(problem,3,2)"   )  
								 }
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("moveTrolleyToOut") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("moveToOut(X,Y)"), Term.createTerm("moveToOut(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var X = payloadArg(0).toInt()
												var Y = payloadArg(1).toInt()
								println("actuatorscontroller moves the Trolley to out")
								request("tMoveToOut", "tMoveToOut(X,Y)" ,"trolley" )  
						}
					}
					 transition(edgeName="t023",targetState="checkTMovedToOut",cond=whenReply("tMovedToOut"))
				}	 
				state("checkTMovedToOut") { //this:State
					action { //it:State
						pb = true  
						if( checkMsgContent( Term.createTerm("tMovedToOut(SUCCESS)"), Term.createTerm("tMovedToOut(SUCCESS)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var m =  payloadArg(0)  
								if(  m != "problem"  
								 ){println("actuatorscontroller: trolley with car moved to out")
								answer("moveToOut", "movedToOut", "movedToOut(moved)"   )  
								}
								else
								 {println("actuatorscontroller: trolley not moved to out")
								 answer("moveToOut", "movedToOut", "movedToOut(problem)"   )  
								 }
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("moveTrolleyToHome") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("backToHome(X)"), Term.createTerm("backToHome(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var X = payloadArg(0)
												
								println("actuatorscontrolle moves the Trolley to home")
								request("tBackToHome", "tBackToHome(X)" ,"trolley" )  
						}
					}
					 transition(edgeName="t024",targetState="checkTMovedToHome",cond=whenReply("tMovedToHome"))
				}	 
				state("checkTMovedToHome") { //this:State
					action { //it:State
						pb = true  
						if( checkMsgContent( Term.createTerm("tMovedToHome(Y)"), Term.createTerm("tMovedToHome(MTOIN)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var m =  payloadArg(0)  
								if(  m != "problem"  
								 ){println("actuatorscontroller: trolley moved to home")
								answer("backToHome", "movedToHome", "movedToHome(moved)"   )  
								}
								else
								 {println("actuatorscontroller: trolley not moved to home")
								 answer("backToHome", "movedToHome", "movedToHome(problem)"   )  
								 }
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
