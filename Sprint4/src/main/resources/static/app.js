prefix = "http://localhost:8081"
var stompClient = null

function reqenter() {

   var xhr = new XMLHttpRequest();
   var url = prefix + '/reqenter';


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

function carenter(slotnum){
  var apiUrl = prefix + '/carenter?slotnum=' + slotnum;
  var xhr = new XMLHttpRequest();

     xhr.onreadystatechange = function(){
  	  if ( xhr.readyState == 4  )
  	  {
  			if(xhr.status == 200){
  				 var tokenid = xhr.responseText
  				 document.getElementById("tokenid").setAttribute("style", "display:inline")
  				 document.getElementById("tokenid").setAttribute("value", tokenid)
  				  document.getElementById("message").setAttribute("style", "display:inline")
  			     //alert(tokenid)
  			}
  				else
  				{
  					var info = eval ( "(" + xhr.responseText + ")" );
  					alert("alert in fase di carenter")
  				}

  		}
  	   };
  	    xhr.open( "GET", apiUrl , true );
        xhr.send( );
}


function deposit(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const slotnum = urlParams.get('slotnum')
    //alert(slotnum)
    carenter(slotnum)

}

function reqexit() {

    window.location = prefix + "/pickup"
}


function pickup(){
  var tokenid = document.getElementById("tokenid").value
  alert(tokenid)
  if(tokenid == "") {
    return
  }

  var apiUrl = prefix + '/reqexit?tokenid=' + tokenid;
  var xhr = new XMLHttpRequest();

     xhr.onreadystatechange = function(){
  	  if ( xhr.readyState == 4  )
  	  {
  			if(xhr.status == 200){
  				 var resp = xhr.responseText
  				 if(resp==0){
  				     document.getElementById("success").setAttribute("style", "display:inline")
  				     document.getElementById("pickup").setAttribute("style", "display:none")

  				 }else if(resp==1){
  				     document.getElementById("fail").setAttribute("style", "display:inline")
  				 }

  		}
  	   }

};
	    xhr.open( "GET", apiUrl , true );
        xhr.send( );
        connect()

}
