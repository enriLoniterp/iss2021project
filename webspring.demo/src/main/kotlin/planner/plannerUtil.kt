package planner

import java.util.ArrayList
import aima.core.agent.Action
import aima.core.search.framework.SearchAgent
import aima.core.search.framework.problem.GoalTest
import aima.core.search.framework.problem.Problem
import aima.core.search.framework.qsearch.GraphSearch
import aima.core.search.uninformed.BreadthFirstSearch
import java.io.PrintWriter
import java.io.FileWriter
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.FileInputStream
//it.unibo.planner20-1.0
import kotlinx.coroutines.delay
import mapRoomKotlin.RobotState
import mapRoomKotlin.Functions
import mapRoomKotlin.RoomMap
import mapRoomKotlin.Box
import mapRoomKotlin.RobotAction
import mapRoomKotlin.RobotState.Direction
import java.io.BufferedReader
import java.io.InputStreamReader

object plannerUtil { 
    private var robotState: RobotState? = null
	private var actions: List<Action>?    = null
 	
    private var curPos    : Pair<Int,Int> = Pair(0,0)
	private var curDir    : RobotState.Direction  = RobotState.Direction.DOWN
    private var curGoal: GoalTest = Functions()		 

	private var mapDims   : Pair<Int,Int> = Pair(0,0)
 	
	private var direction             = "downDir"
	private	var currentGoalApplicable = true;

	private var actionSequence : Iterator<Action>? = null

	private var storedactionSequence : Iterator<Action>? = null
    private var storedPos  : Pair<Int,Int> = Pair(0,0)

    private var search: BreadthFirstSearch? = null
    private var timeStart: Long = 0
	
	
/* 
 * ------------------------------------------------
 * CREATE AND MANAGE PLANS
 * ------------------------------------------------
 */
    @Throws(Exception::class)
    @JvmStatic fun initAI() {
         robotState = RobotState(0, 0, RobotState.Direction.DOWN)
         search     = BreadthFirstSearch(GraphSearch())
       println("plannerUtil initAI done")
    }

    @JvmStatic
	fun setGoal( x: Int, y: Int) {
        try {
             println("setGoal $x,$y while robot in cell: ${getPosX()},${getPosY()} direction=${getDirection()} ") //canMove=$canMove
            
			if( RoomMap.getRoomMap().isObstacle(x,y) ) {
				println("ATTEMPT TO GO INTO AN OBSTACLE ")
				currentGoalApplicable = false
 				resetActions()
				return
			}else currentGoalApplicable = true
            
			RoomMap.getRoomMap().put(x, y, Box(false, true, false))  //set dirty

			curGoal = GoalTest { state  : Any ->
                val robotState = state as RobotState
				(robotState.x == x && robotState.y == y)
            }
			showMap()
         } catch (e: Exception) {
             //e.printStackTrace()
        }
    }
 
    @Throws(Exception::class)
    @JvmStatic
	fun doPlan(): List<Action>? {
		//println("plannerUtil doPlan  " )
		
		if( ! currentGoalApplicable ){
			println("plannerUtil doPlan cannot go into an obstacle")
			actions = listOf<Action>()
			return actions		//empty list
		} 
		
        val searchAgent: SearchAgent
        //println("plannerUtil doPlan newProblem (A) $curGoal" );
		val problem = Problem(robotState, Functions(), Functions(), curGoal, Functions())
        //println("plannerUtil doPlan newProblem (A) search " );
        searchAgent = SearchAgent(problem, search!!)
        actions  = searchAgent.actions
		
		println("plannerUtil doPlan actions=$actions")
		
        if (actions == null || actions!!.isEmpty()) {
            println("plannerUtil doPlan NO MOVES !!!!!!!!!!!! $actions!!"   )
            if (!RoomMap.getRoomMap().isClean) RoomMap.getRoomMap().setObstacles()
            //actions = ArrayList()
            return null
        } else if (actions!![0].isNoOp) {
            println("plannerUtil doPlan NoOp")
            return null
        }
		
        //println("plannerUtil doPlan actions=$actions")
		setActionMoveSequence()
        return actions
    }
	
	@JvmStatic fun planForGoal( x: String, y: String) {
		val vx = Integer.parseInt(x)
		val vy = Integer.parseInt(y)
		setGoal(vx,vy)		
		doPlan()   
 	}	
  	
	@JvmStatic fun planForNextDirty() {
		val rmap = RoomMap.getRoomMap()
		for( i in 0..getMapDimX( )-1 ){
			for( j in 0..getMapDimY( )-1 ){
				//println( ""+ i + "," + j + " -> " + rmap.isDirty(i,j)   );
				if( rmap.isDirty(i,j)  ){
					setGoal( i,j )
					doPlan() 
					return
				}
			}
		}
 	}	


	@JvmStatic fun memoCurentPlan(){
		storedPos            = curPos
		storedactionSequence = actionSequence;
	}
	
	@JvmStatic fun restorePlan(){
		//Goto storedcurPos
		actionSequence = storedactionSequence;
	}
	
/*
 * ------------------------------------------------
 * MANAGE PLANS AS ACTION SEQUENCES
 * ------------------------------------------------
*/	
	@JvmStatic fun setActionMoveSequence(){
		if( actions != null ) {
			 actionSequence = actions!!.iterator()
		}
	}
	
	@JvmStatic fun getNextPlannedMove() : String{
		if( actionSequence == null ) return ""
		else if( actionSequence!!.hasNext()) return actionSequence!!.next().toString()
				else return ""
	}

	@JvmStatic fun getActions() : List<Action>{
        return actions!!
    }
	@JvmStatic fun existActions() : Boolean{
		//println("existActions ${actions!!.size}")
		return actions!!.size>0   
	}
	@JvmStatic fun resetActions(){
		actions = listOf<Action>()
	}
	
	@JvmStatic fun get_actionSequence() : Iterator<Action>?{
		return actionSequence
	}
 	
/*
 * ------------------------------------------------
 * INSPECTING ROBOT POSITION AND DIRECTION
 * ------------------------------------------------
*/		
	@JvmStatic fun get_curPos() : Pair<Int,Int>{
		return curPos
	}

	@JvmStatic fun getPosX() : Int{ return robotState!!.getX() }
    @JvmStatic fun getPosY() : Int{ return robotState!!.getY() }
	
	@JvmStatic fun getDir()  : RobotState.Direction{
		return robotState!!.getDirection()
	}

	@JvmStatic fun getDirection() : String{
 		val direction = getDir()
		when( direction ){
			Direction.UP    -> return "upDir"
			Direction.RIGHT -> return "rightDir"
			Direction.LEFT  -> return "leftDir"
			Direction.DOWN  -> return "downDir"
			else            -> return "unknownDir"
 		}
  	}
	
	@JvmStatic fun atHome() : Boolean{
		return curPos.first == 0 && curPos.second == 0
	}
	
	@JvmStatic fun atPos( x: Int, y: Int ) : Boolean{
		return curPos.first == x && curPos.second == y
	}
	
	@JvmStatic fun showCurrentRobotState(){
		println("===================================================")
		showMap()
		direction = getDirection()
		println("RobotPos=(${curPos.first}, ${curPos.second})  direction=$direction  " )  
		println("===================================================")
	}

/*
* ------------------------------------------------
* MANAGING THE ROOM MAP
* ------------------------------------------------
*/	
 	@JvmStatic fun getMapDimX( ) 	: Int{ return mapDims.first }
	@JvmStatic fun getMapDimY( ) 	: Int{ return mapDims.second }
	@JvmStatic fun mapIsEmpty() : Boolean{return (getMapDimX( )==0 &&  getMapDimY( )==0 ) }

	@JvmStatic fun showMap() {
        println(RoomMap.getRoomMap().toString() )
    }
	@JvmStatic fun getMap() : String{
		return RoomMap.getRoomMap().toString() 
	}
	@JvmStatic fun getMapOneLine() : String{ 
		return  "'"+RoomMap.getRoomMap().toString().replace("\n","@").replace("|","").replace(",","") +"'" 
	}

	@JvmStatic fun getMapDims() : Pair<Int,Int> {
		if( RoomMap.getRoomMap() == null ){
			return Pair(0,0)
		}
	    val dimMapx = RoomMap.getRoomMap().getDimX()
	    val dimMapy = RoomMap.getRoomMap().getDimY()
	    //println("getMapDims dimMapx = $dimMapx, dimMapy=$dimMapy")
		return Pair(dimMapx,dimMapy)	
	}
 	
	@JvmStatic fun loadRoomMap( fname: String  )   {
 		println("EI")
		try{
 			val inps = ObjectInputStream(FileInputStream("${fname}.bin"))
			val map  = inps.readObject() as RoomMap;
 			println("loadRoomMap = $fname DONE")
			RoomMap.setRoomMap( map )
		}catch(e:Exception){			
			println("loadRoomMap = $fname FAILURE")
		}
		mapDims = getMapDims()//Pair(dimMapx,dimMapy)
	}
	
 
	@JvmStatic fun saveRoomMap(  fname : String)   {		
        println("saveMap in $fname")
		val pw = PrintWriter( FileWriter(fname+".txt") )
		pw.print( RoomMap.getRoomMap().toString() )
		pw.close()
		
		val os = ObjectOutputStream( FileOutputStream(fname+".bin") )
		os.writeObject(RoomMap.getRoomMap())
		os.flush()
		os.close()
		mapDims = getMapDims()
    }
	
	fun loadRoomMapFromTxt(file : String){
		var map = RoomMap.getRoomMap()
		val reader = BufferedReader(InputStreamReader(FileInputStream(file)))
		var line = reader.readLine()

		var r = 0
		var toks : List<String>
		var cols = 0
		var box : Box? = null
		var s : String

		while(line != null) {

			line = line.trim()
			if(line.endsWith(","))
				line = line.substring(0..(line.length - 2))

			toks = line.trim().split(",")
			if(r == 0)
				cols = toks.size - 1
			else
				if((toks.size-1) != cols) {
					println("MapLoader | Bad formatted txt. Aborting")
					System.exit(-1)
				}

			for(c in 0..cols) {
				s = toks[c].trim()
				if(s.length == 1) box = parseBox(s.get(0))
				else if(s.length == 2 && s.get(0) == '|') box = parseBox(s.get(1))
				else {
					println("MapLoader | Invalid length for sequence \'$s\' in line ${r+1}")
					System.exit(-1)
				}

				if(box != null)
					map.put(c, r, box)
				if(box == null) {
					println("MapLoader | Invalid sequence \'$s\' in line ${r+1}")
					System.exit(-1)
				}
			}

			line = reader.readLine()
			r++
		}
	}

	private fun parseBox(c : Char) : Box? {
		when(c) {
			'0' -> return Box(false, true, false)
			'r' -> return Box(false, false, true)
			'1' -> return Box(false, false, false)
			'X' -> return Box(true, false, false)
		}
		return null
	}

/*
* ------------------------------------------------
* UPDATING THE ROOM MAP
* ------------------------------------------------
*/		
 	@JvmStatic fun updateMap( move: String, msg: String="" ){
		doMove( move )
		setPositionOnMap( )
		if( msg.length > 0 ) println(msg) 
 	}
	
	@JvmStatic fun updateMapObstacleOnCurrentDirection(   ){
		doMove( direction )
		setPositionOnMap( )
	}
	
	@JvmStatic fun setPositionOnMap( ){
		direction     =  getDirection()
		val posx      =  getPosX()
		val posy      =  getPosY()
		curPos        =  Pair( posx,posy )
	}
 	
    @JvmStatic fun doMove(move: String) {
        val x   = getPosX()  
        val y   = getPosY()  
		val map = RoomMap.getRoomMap()
       // println("plannerUtil: doMove move=$move  dir=$dir x=$x y=$y dimMapX=$dimMapx dimMapY=$dimMapy")
       try {
            when (move) {
                "w" -> {
                    //map.put(x, y, Box(false, false, false)) //clean the cell
					map.cleanCell(x,y)
                    robotState = Functions().result(robotState!!, RobotAction.wAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "s" -> {
                    robotState = Functions().result(robotState!!, RobotAction.sAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "a"  -> {
                    robotState = Functions().result(robotState!!, RobotAction.lAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "l" -> {
                    robotState = Functions().result(robotState!!, RobotAction.lAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "d" -> {
                    robotState = Functions().result(robotState!!, RobotAction.rAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "r" -> {
                    robotState = Functions().result(robotState!!, RobotAction.rAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
				//Used by WALL-UPDATING
				//Box(boolean isObstacle, boolean isDirty, boolean isRobot)
                "rightDir" -> map.put(x + 1, y, Box(true, false, false)) 
                "leftDir"  -> map.put(x - 1, y, Box(true, false, false))
                "upDir"    -> map.put(x, y - 1, Box(true, false, false))
                "downDir"  -> map.put(x, y + 1, Box(true, false, false))
		   }//when
        } catch (e: Exception) {
            println("plannerUtil doMove: ERROR:" + e.message)
        }
    }
	
	@JvmStatic fun moveRobotInTheMap(){
		RoomMap.getRoomMap().put(robotState!!.x, robotState!!.y, Box(false, false, true))
	}
	
/*
* ------------------------------------------------
* UPDATING THE ROOM MAP FOR WALLS
* ------------------------------------------------
*/		
 	@JvmStatic fun setObstacleWall(  dir: Direction, x:Int, y:Int){
		if( dir == Direction.DOWN  ){ RoomMap.getRoomMap().put(x, y + 1, Box(true, false, false)) }
		if( dir == Direction.RIGHT ){ RoomMap.getRoomMap().put(x + 1, y, Box(true, false, false)) }
	}
	@JvmStatic fun wallFound(){
 		 val dimMapx = RoomMap.getRoomMap().getDimX()
		 val dimMapy = RoomMap.getRoomMap().getDimY()
		 val dir     = getDir()
		 val x       = getPosX()
		 val y       = getPosY()
		 setObstacleWall( dir,x,y )
 		 println("plannerUtil wallFound dir=$dir  x=$x  y=$y dimMapX=$dimMapx dimMapY=$dimMapy");
		 doMove( dir.toString() )  //set cell
  		 if( dir == Direction.RIGHT) setWallDown(dimMapx,y)  
		 if( dir == Direction.UP)    setWallRight(dimMapy,x)
 	}
	@JvmStatic fun setWallDown(dimMapx: Int, y: Int ){
		 var k   = 0
		 while( k < dimMapx ) {
			RoomMap.getRoomMap().put(k, y+1, Box(true, false, false))
			k++
		 }		
	}	
	@JvmStatic fun setWallRight(dimMapy: Int, x: Int){
 		 var k   = 0
		 while( k < dimMapy ) {
			RoomMap.getRoomMap().put(x+1, k, Box(true, false, false))
			k++
		 }		
	}

	
/*
* ------------------------------------------------
* TIME UTILITIES
* ------------------------------------------------
*/		
    @JvmStatic fun startTimer() {
        timeStart = System.currentTimeMillis()
    }
	
    @JvmStatic fun getDuration() : Int{
        val duration = (System.currentTimeMillis() - timeStart).toInt()
		println("DURATION = $duration")
		return duration
    }
	
	
}
