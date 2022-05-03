/* Generated by AN DISI Unibo */ 
package park_client_service

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import resources.ObjectHelper.Companion.loadObject
import resources.ObjectHelper.Companion.saveObject
import resources.ParkingState

class Park_client_service ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				//val weightSensorAdapter = weightsensor.WeightSensorAdapter()
		        //val outSonarAdapter = outsonar.OutSonarAdapter()
				lateinit var currentJob : String
				
				var RESPONSE  = ""
				var SLOTNUM = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("parkclientservice STARTS")
						  
									try{
									loadObject("ParkingState.json", ParkingState)
									println(ParkingState.slotState.toString())
									}catch(e : Exception){
									   var slots: HashMap<Int,String> = HashMap()
									   for(i in 1..6){
									   	slots.put(i, "")
									   }
									   ParkingState.slotState = slots
									}
									
					}
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						println("parkclientservice waiting ...")
					}
					 transition(edgeName="t00",targetState="error",cond=whenDispatch("trolleyError"))
					transition(edgeName="t01",targetState="working",cond=whenRequest("acceptIn"))
					transition(edgeName="t02",targetState="working",cond=whenRequest("acceptOut"))
					transition(edgeName="t03",targetState="working",cond=whenRequest("carenter"))
				}	 
				state("working") { //this:State
					action { //it:State
						 
						            	    RESPONSE = ""
											SLOTNUM = 0
											var TOKENID= "0"
						println("WORKING")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("acceptIn(req)"), Term.createTerm("acceptIn(req)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  ParkingState.indoorFree && ParkingState.trolleyState != ("trolley STOPPED")  
								 ){
								                		for(i in 1..6) {
								                    			if(ParkingState.slotState.get(i).equals("")) {
								                        			SLOTNUM = i
								                        			break
								                    			}
								                		}
								if(  SLOTNUM == 0  
								 ){ RESPONSE = "0"  
								}
								else
								 { RESPONSE = "$SLOTNUM" 
								 					ParkingState.slotState.put(SLOTNUM, "R")  
								 }
								}
								else
								 { RESPONSE= "The indoor area is engaged or the trolley is stopped"  
								 }
								println("parkclientservice reply enter($SLOTNUM)")
								updateResourceRep( "$SLOTNUM"  
								)
								answer("acceptIn", "responseAcceptIn", "responseAcceptIn($RESPONSE)"   )  
								forward("goToIdle", "goToIdle(go)" ,"park_client_service" ) 
						}
						if( checkMsgContent( Term.createTerm("acceptOut(TI)"), Term.createTerm("acceptOut(TI)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 TOKENID = payloadArg(0)  
								if(  ParkingState.outdoorFree && ParkingState.trolleyState != ("trolley STOPPED")  
								 ){ 
														ParkingState.slotState.forEach { (k, v) -> 
															if(v == TOKENID)
																SLOTNUM = k
														}
								if(  SLOTNUM != 0  
								 ){ RESPONSE ="Success"  
								answer("acceptOut", "responseAcceptOut", "responseAcceptOut($RESPONSE)"   )  
								request("moveToSlot", "moveToSlot($SLOTNUM)" ,"trolley_controller" )  
								currentJob = "parkOut" 
								println("parkclientservice moves the car from SLOTNUM = $SLOTNUM to out")
								updateResourceRep( "clientservice moves the car from SLOTNUM = $SLOTNUM"  
								)
								ParkingState.slotState.put(SLOTNUM, "")  
								
															saveObject("ParkingState.json", ParkingState)
								}
								else
								 { RESPONSE = "Invalid tokenid"  
								 answer("acceptOut", "responseAcceptOut", "responseAcceptOut($RESPONSE)"   )  
								 }
								}
								else
								 { RESPONSE = "The trolley is stopped or outdoor not free"  
								 answer("acceptOut", "responseAcceptOut", "responseAcceptOut($RESPONSE)"   )  
								 }
								println("parkclientservice reply")
								updateResourceRep( "clientservice reply"  
								)
						}
						if( checkMsgContent( Term.createTerm("carenter(SN)"), Term.createTerm("carenter(SN)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								//ParkingState.indoorFree = false
								 SLOTNUM = payloadArg(0).toInt()  
								if(  SLOTNUM in 1..6 && ParkingState.slotState.get(SLOTNUM).equals("R")  
								 ){if(  !ParkingState.indoorFree  
								 ){
														val sdf = java.text.SimpleDateFormat("dd/MM/yyyy-hh:mm:ss")
														val currentDate = sdf.format(java.util.Date())	
														val TOKENID_RAW = "$currentDate-$SLOTNUM"
														val TOKENID = TOKENID_RAW.filter{it.isDigit()}
								RESPONSE = "$TOKENID"  
								answer("carenter", "responseCarenter", "responseCarenter($RESPONSE)"   )  
								request("moveToIn", "moveToIn(move)" ,"trolley_controller" )  
								println("parkclientservice moves the car to SLOTNUM = $SLOTNUM")
								updateResourceRep( "parkingclientservice moves the car to SLOTNUM = $SLOTNUM"  
								)
								 ParkingState.slotState.put(SLOTNUM, "$TOKENID")  
								
														saveObject("ParkingState.json", ParkingState)
								}
								else
								 { RESPONSE = "The indoor area is free"  
								 answer("carenter", "responseCarenter", "responseCarenter($RESPONSE)"   )  
								 }
								}
								else
								 { RESPONSE = "Invalid parking slot number"  
								 answer("carenter", "responseCarenter", "responseCarenter($RESPONSE)"   )  
								 }
								println("parkclientservice reply")
								updateResourceRep( "clientservice reply"  
								)
						}
					}
					 transition(edgeName="t04",targetState="error",cond=whenDispatch("trolleyError"))
					transition(edgeName="t05",targetState="processReply",cond=whenReply("movedToSlot"))
					transition(edgeName="t06",targetState="processReply",cond=whenReply("movedToIn"))
					transition(edgeName="t07",targetState="idle",cond=whenDispatch("goToIdle"))
				}	 
				state("error") { //this:State
					action { //it:State
						println("Error in transportTrolley! Technical intervention required")
					}
					 transition(edgeName="t08",targetState="idle",cond=whenDispatch("restart"))
				}	 
				state("processReply") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("movedToSlot(SUCCESS)"), Term.createTerm("movedToSlot(done)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(currentJob == "parkOut"){
								request("moveToOut", "moveToOut(move)" ,"trolley_controller" )  
								}else{ 
								forward("goToIdle", "goToIdle(go)" ,"park_client_service" ) 
								}
						}
						if( checkMsgContent( Term.createTerm("movedToIn(MOVED)"), Term.createTerm("movedToIn(done)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								request("moveToSlot", "moveToSlot($SLOTNUM)" ,"trolley_controller" )  
								currentJob = "parkIn"
						}
						if( checkMsgContent( Term.createTerm("movedToOut(SUCCESS)"), Term.createTerm("movedToOut(done)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("goToIdle", "goToIdle(go)" ,"park_client_service" ) 
						}
					}
					 transition(edgeName="t09",targetState="idle",cond=whenDispatch("goToIdle"))
					transition(edgeName="t010",targetState="processReply",cond=whenReply("movedToOut"))
					transition(edgeName="t011",targetState="processReply",cond=whenReply("movedToSlot"))
				}	 
			}
		}
}
