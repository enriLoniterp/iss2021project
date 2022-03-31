var prefix = "http://localhost:8081"
var temperatureSystem= document.getElementById('temperature')
var fanElement = document.getElementById('fan_state')
var trolleyState = document.getElementById('trolley_state')

fun updateParkingArea(){

   var xhr = new XMLHttpRequest();
   var url = prefix + '/manager/ParkingState';

   xhr.onreadystatechange = function(){
	  if ( xhr.readyState == 4  )
	  {
			if(xhr.status == 200){
					if(xhr.responseText == 0) {
						alert("No parking slots available, try again later", 'warning')
					} else {
					 var slotnum = xhr.responseText
					// alert("Your parking slot is n. " + slotnum)
                     window.location = prefix + "/deposit?slotnum=" + slotnum
                     //deposit(slotnum)
				}
				}
				else
				{
					var info = eval ( "(" + xhr.responseText + ")" );
					console.log('error')
				}
		}
	   };


   xhr.open( "GET", url , true );
   xhr.send( );
}

var stompClient

//WEBSOCKETS//

function connect() {
    //var location = prefix + '/app/timer'
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            console.log("timeout")
        });
    });

}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

}
