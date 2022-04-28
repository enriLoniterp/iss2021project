var prefix = "http://localhost:8081"

setInterval(function (){
parkState()
},3000);

function parkState(){

   var xhr = new XMLHttpRequest();
   var url = prefix + '/manager/parkingstate';

   xhr.onreadystatechange = function(){
    console.log(xhr.readyState)
    console.log(xhr.responseText)
	if( xhr.readyState == 4)
	{
        if(xhr.status == 200)
        {
            var respjson=JSON.parse(xhr.responseText)
            document.getElementById('fan_state').innerHTML=respjson.fanState
            document.getElementById('trolley_state').innerHTML=respjson.trolleyState
            document.getElementById('temperature').innerHTML=respjson.temperature
            var startButton= document.getElementById("start")
            var stopButton=document.getElementById("stop")
            //setto i valori dei pulsanti coerentemente
            var temp=0

            var slotS = new Map (Object.entries(respjson.slotState))
            var mapIter = slotS.entries()

            for (var i = 1; i<=6; i++){
                console.log(slotS.get(i.toString()))
                if(slotS.get(i.toString()) != ""){
                    //var btn = document.getElementById((i.toString()))
                    //var t1 = btn.getElementById("park_not_free")
                  //  if(t1 != null){
                       // t1.id = "park_free"
                   // }
                }else{
                   // var t1 = btn.getElementById("park_free")
                   // if(t1 != null){
                       // t1.id = "park_not_free"
                   // }
                }
            }
/*
              for(const park in respjson.slotState){
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
            if(respjson.temperature >= 30 && respjson.trolleyState=="working"){
                startButton.style.visibility="hidden"
                stopButton.style.visibility="visible"
            }

             if(respjson.temperature < 30 && respjson.trolleyState=="stopped"){
                            startButton.style.visibility="visible"
                            stopButton.style.visibility="hidden"
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




//WEBSOCKETS//
var stompClient = null

function connect() {
    //var location = prefix + '/app/timer'
    var socket = new SockJS('/information_manager');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/manager/temperatureAlarm', function (response) {
            console.log("temperatureAlarm")
            var temperature = JSON.parse(response.body)

        });

         stompClient.subscribe('/manager/sonarAlarm', function (message) {
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
connect()