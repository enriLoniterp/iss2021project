prefix = "http://localhost:8081"
var tokenid = ""

function reqenter() {

   var xmlHttp = new XMLHttpRequest();
   var url = prefix + '/reqenter';

   var resp = xmlHttp.responseText
   xmlHttp.open( "GET", url , true );
   xmlHttp.send( );
   xmlHttp.onreadystatechange = function(){

	  if ( xmlHttp.readyState == 4  )
	  {
			if(xmlHttp.status == 200){


					if(xmlHttp.parkingSlot == 0) {
						alert("No parking slots available, try again later", 'warning')
					} else {
					 alert("Your parking slot is n. " + resp)

                     window.location.replace(url + "/deposit")
                     var deposit = document.getElementById("deposit")
                     deposit.onclick = function() { carenter(resp) }
			}
				}
				else
				{
					var info = eval ( "(" + xmlHttp.responseText + ")" );
					console.log('error')


				}

		}
	   };

}



function carenter(slotnum){

  seconds = timeoutValue
  let resStatus = 0
  console.log(slotnum)
  var apiUrl = prefix + '/carenter?slotnum=' + slotnum;
  fetch(apiUrl).then(response => {
      resStatus = response.status
      return response.json()
  }).then(res => {
    switch (resStatus) {
      case 200:
        console.log('success')
        console.log(res)
        var btnToChange = document.getElementById("btnToChange")
        btnToChange.style.visibilty="visible"
        var tokenidblock = document.getElementById("tokenid")
        tokenid=res
        tokenidblock.value = tokenid
        btnToChange.onclick = function() {window.location.replace(prefix + "/home") }
      case 403:
        console.log('error')
        console.log(res)
        errorAlert(res.message)
        break
      default:
        console.log('unhandled')
        break
    }
  })
  .catch(err => {
    console.error(err)
  })

}


function reqexit() {
  seconds = timeoutValue
  let resStatus = 0
  var tokenid = document.getElementById("token").value
  if(tokenid == "") {
    alertWithIcon("Insert a token id", 'warning')
    var btnToChange = document.getElementById("btnToChange")
    btnToChange.setAttribute('data-bs-dismiss', 'alert')
    btnToChange.setAttribute('data-bs-target', '#my-alert')
    return
  }
  var apiUrl = prefix + '/client/reqexit?tokenid=' + tokenid;
  fetch(apiUrl).then(response => {
      resStatus = response.status
      if(resStatus != 200)
        return response.json()
      else
        return response
  }).then(res => {
    switch (resStatus) {
      case 200:
        console.log('success')
        console.log(res)
        successAlert("The trolley will collect your car and bring it to you! Please stand by...<br><b>WHEN YOUR CAR ARRIVES, PLEASE LEAVE THE AREA IN ONE MINUTE:</b> <br> Thank you, and see you soon!", 'success')
        var btnToChange = document.getElementById("btnToChange")
        btnToChange.setAttribute('value', "Return to Home Page")
        btnToChange.onclick = function() { window.location = '/clientOut' }
        btnToChange.setAttribute('data-bs-dismiss', 'alert')
        btnToChange.setAttribute('data-bs-target', '#my-alert')
        document.getElementById("form").setAttribute("style", "display:none;")
        document.getElementById("returnDiv").setAttribute("style", "visibility:visible;")
        var x = setInterval(function() {
          seconds--
          document.getElementById("countdown").innerHTML = seconds + "s ";
          if (seconds == 0) {
            clearInterval(x);
            window.location = '/clientOut'
          }
        }, 1000);
        break
      case 400:
        console.log('error')
        console.log(res)
        alertWithIcon(res.message, 'warning')
        var btnToChange = document.getElementById("btnToChange")
        btnToChange.setAttribute('data-bs-dismiss', 'alert')
        btnToChange.setAttribute('data-bs-target', '#my-alert')
        break
      case 403:
        console.log('error')
        console.log(res)
        errorAlert(res.message)
        var btnToChange = document.getElementById("btnToChange")
        btnToChange.setAttribute('data-bs-dismiss', 'alert')
        btnToChange.setAttribute('data-bs-target', '#my-alert')
        break
      default:
        console.log('unhandled')
        break
    }
  })
  .catch(err => {
    console.error(err)
  })
}
