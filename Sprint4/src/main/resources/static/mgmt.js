var prefix = "http://localhost:8081"
var temperatureSystem= document.getElementById('temperature')
var fanElement = document.getElementById('fan_state')
var trolleyState = document.getElementById('trolley_state')

setInterval(function (){
parkState()
},2000);



function parkState(){

   var xhr = new XMLHttpRequest();
   var url = prefix + '/manager/parkingstate';

   xhr.onreadystatechange = function(){
	  if ( xhr.readyState == 4  )
	  {
            var resp=xhr.responseText
			if(xhr.status == 200)
			{
			   // recupero dati
			   var info = resp.split(",")

			   trolleyState.innerHTML = info.
			   fanElement.innerHTML = response.fanState
			   temperatureSystem.innerHTML = response.temperature
               var startButton= document.getElementById("start")
               var stopButton=document.getElementById("stop")
               //setto i valori dei pulsanti coerentemente
               //fan non c'è ancora
               switch (trolley_state.innerHTML)
			    {
			        case "working":
			             startButton.style.visibility="hidden"
			             stopButton.style.visibility="visible"
			        case "idle":
			             startButton.style.visibility="visible"
			             stopButton.style.visibility="visible"
			    }

			}
			else
			{
                console.log("errore messaggio")
			}
		}


   };

   xhr.open( "GET", url , true );
   xhr.send( );
}


function trolleystate(element){

   var xhr = new XMLHttpRequest();
   var button = element.id
   var url = prefix + '/manager/trolleystate';
   var urlcomplete = url + "/"+ button

   xhr.onreadystatechange = function(){
	  if ( xhr.readyState == 4  )
	  {
            var response = xhr.responseText
			if(xhr.status == 200)
			{
			   // recupero dati
			   console.log(response)
			   alert("operazione eseguita con successo")
               switch (trolley_state.innerHTML)
			    {
			        case "working":
			             startButton.style.visibility="hidden"
			             stopButton.style.visibility="visible"
			        case "idle":
			             startButton.style.visibility="visible"
			             stopButton.style.visibility="visible"
			    }

			}
			else
			{
                console.log("errore messaggio")
			}
		}


   };

   xhr.open( "GET", urlcomplete , true );
   xhr.send( );
}

var stompClient = null

//WEBSOCKETS//

function connect() {
    //var location = prefix + '/app/timer'
    var socket = new SockJS('/information_ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/temperature', function (response) {
            console.log("temperature")
            var temperature = response.body
            console.log(temperature)
        });

         stompClient.subscribe('/topic/sonar', function (message) {
                    console.log(message)

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

/*
function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/temperature', function (message) {
            console.log(message)
            var myMessage =message.body

        });
        stompClient.subscribe('/topic/sonar', function (message) {
            console.log(message)

        });

    });
}
*/
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function startRobot(){
    var state ="1"
    stompClient.send("/app/trolleystate",state)

}

function stopRobot(){
    var state ="0"
    stompClient.send("/app/trolleystate", state)
}

connect()