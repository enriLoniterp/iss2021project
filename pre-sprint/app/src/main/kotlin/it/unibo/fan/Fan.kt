/* Generated by AN DISI Unibo */ 
package it.unibo.fan

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Fan ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("fan STARTS")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("fan waiting ........... ")
					}
					 transition(edgeName="t00",targetState="switchOn",cond=whenDispatch("on"))
					transition(edgeName="t01",targetState="switchOff",cond=whenDispatch("off"))
				}	 
				state("switchOn") { //this:State
					action { //it:State
						println("fan switched on !")
					}
				}	 
				state("switchOff") { //this:State
					action { //it:State
						println("fan switched off !")
					}
				}	 
			}
		}
}
