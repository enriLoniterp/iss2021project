var prefix = "http://localhost:8081"


setInterval(function (){
parkState()
},5000);

function parkState(){

   var xhr = new XMLHttpRequest();
   var url = prefix + '/manager/parkingstate';

   xhr.onreadystatechange = function(){

	if( xhr.readyState == 4)
	{
        if(xhr.status == 200)
        {
            var respjson=JSON.parse(xhr.responseText)
            document.getElementById('fan_state').innerHTML=respjson.fanState
            document.getElementById('trolley_state').innerHTML=respjson.trolleystate
            document.getElementById('temperature').innerHTML=respjson.temperature
            var startButton= document.getElementById("start")
            var stopButton=document.getElementById("stop")
            //setto i valori dei pulsanti coerentemente
            var temp=0
/*
              for(const park in respjson.slots){
                    var slot = temp.toString()
                    console.log(slot)
                    document.getElementById(slot).innerHTML=respjson.fanState
                     temp++
                }
            //var loadedData = JSON.parse(localStorage.getItem("tblArrayJson"));
             var loadedTable = document.getElementById("slot");
/*
              for (var key in loadedData) {
                var row = loadedTable.insertRow();
                for (var i = 0; i < loadedData[key].length; i += 1) {
                  var cell = row.insertCell(i);
                  cell.innerHTML = loadedData[key][i];
                }
              }

            */

            switch (respjson.trolleystate)
            {
                case "working":
                    startButton.style.visibility="hidden"
                    stopButton.style.visibility="visible"
                case "idle":
                    startButton.style.visibility="visible"
                    stopButton.style.visibility="visible"
            }
        }
        }
		else
		{
            console.log("errore messaggio")
		}

   };

    xhr.open("GET", url, true)
    xhr.send( );
}


function trolleystate(element){

   var xhr = new XMLHttpRequest();
   console.log(element.id)
   var button = element.id
   var url = prefix + '/manager/trolley';
   var urlcomplete = url +"?state="+ button

   xhr.onreadystatechange = function(){
	  if ( xhr.readyState == 4  )
	  {
	        //risultato ricevuto è una stringa
			if(xhr.status == 200)
			{
			  var response = xhr.responseText
               console.log(response)
			   // recupero dat
			}
			else
			{
                console.log("errore invio msg trolley")
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
        stompClient.subscribe('/topic/temperatureAlarm', function (response) {
            console.log("temperatureAlarm")
            var temperature = JSON.parse(response.body)
            

        });

         stompClient.subscribe('/topic/sonarAlarm', function (message) {
             console.log("sonarAlarm")


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


/*
//websocket for starting robot with Spring
function startRobot(){
    var state ="1"
    stompClient.send("/app/trolleystate",state)

}

function stopRobot(){
    var state ="0"
    stompClient.send("/app/trolleystate", state)
}
*/
connect()