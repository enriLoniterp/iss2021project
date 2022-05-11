var prefix = "http://localhost:8081"

setInterval(function (){
parkState()
},2000);

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

            document.getElementById('ss').style.display = 'inline'
            var respjson=JSON.parse(xhr.responseText)
            document.getElementById('fan_state').innerHTML='Fan state: '+respjson.fanState
             document.getElementById('img2').style.display = 'inline'
            document.getElementById('trolley_state').innerHTML='Trolley state: '+respjson.trolleyState
            document.getElementById('img1').style.display = 'inline'
            document.getElementById('temperature').innerHTML='Temperature: '+respjson.temperature
            document.getElementById('img3').style.display = 'inline'
            var startButton= document.getElementById("start")
            var stopButton=document.getElementById("stop")
            //setto i valori dei pulsanti coerentemente
            var temp=0

            var slotS = new Map (Object.entries(respjson.slotState))
            var mapIter = slotS.entries()

            for (var i = 1; i<=6; i++){
                console.log(slotS.get(i.toString()))
                var d = document.getElementById((i.toString()))
               // console.log(d.id)
                if(slotS.get(i.toString()) == "" || slotS.get(i.toString()) == "R"){
                    var t1 = d.getElementsByClassName("park_not_free")
                    //console.log(t1)
                    if(t1[0] != null){
                        t1[0].setAttribute('class', 'park_free')
                    }
                }else{
                    var t1 = d.getElementsByClassName("park_free")
                    if(t1[0] != null){
                       t1[0].setAttribute('class', 'park_not_free')

                    }
                }
            }

            if(respjson.temperature >= 30 && (respjson.trolleyState=="working" || respjson.trolleyState=="idle")){
                startButton.style.visibility="hidden"
                stopButton.style.visibility="visible"
            }

             if(respjson.temperature < 30 && respjson.trolleyState=="stopped"){
                            startButton.style.visibility="visible"
                            stopButton.style.visibility="hidden"
             }
             if(respjson.trolleyState=="stopped"){
             topButton.style.visibility="hidden"
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
   if(button == "start"){button = "resume"}
   var urlcomplete = url +"?state="+ button

   xhr.onreadystatechange = function(){
	  if ( xhr.readyState == 4  )
	  {
	        //risultato ricevuto è una stringa
			if(xhr.status == 200)
			{
			  var response = xhr.responseText
               console.log(response)
                var startButton= document.getElementById("start")
                var stopButton=document.getElementById("stop")
               if (button == "resume"){
                    startButton.style.visibility="hidden"
               }else{
               stopButton.style.visibility="hidden"
               }
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
        stompClient.subscribe('/manager/temperatureAlarm', function (message) {
           alert (message.body)
        });

         stompClient.subscribe('/manager/sonarAlarm', function (message) {
              alert (message.body)
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