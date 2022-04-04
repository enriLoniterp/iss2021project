var prefix = "http://localhost:8081"
var temperatureSystem= document.getElementById('temperature')
var fanElement = document.getElementById('fan_state')
var trolleyState = document.getElementById('trolley_state')

setInterval(function (){
parkState()
},500);


fun parkState(){

   var xhr = new XMLHttpRequest();
   var url = prefix + '/manager/parkingstate';

   xhr.onreadystatechange = function(){
	  if ( xhr.readyState == 4  )
	  {
			if(xhr.status == 200)
			{
			   var response= JSON.parse(xhr.responseText)
			   // recupero dati
			   trolleyState.innerHTML = response.trolleyState
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
                alert("conncetion error")
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
    var socket = new SockJS('/information_ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/temperature', function (temperature) {
            console.log("temperature")
            var temper =
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


function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/manager/temperature', function (message) {
            console.log(message)
            var myMessage = JSON.parse(message.body)

            if(myMessage.code == 1) {
                alertWithIcon(myMessage.message, 'warning', false, "temperatureAlert")
            }
            else if(myMessage.code == 2) {
                var myAlert = document.getElementById('temperatureAlert')
                var bsAlert = new bootstrap.Alert(myAlert)
                bsAlert.close()
            }
        });
        stompClient.subscribe('/manager/sonar', function (message) {
            console.log(message)
            warningAlert(JSON.parse(message.body).message, true)
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

connect()