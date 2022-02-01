prefix = "http://localhost:8081"
/*
alertok(message)
{
    alert(message)
}
*/

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
					 alert("Your parking slot is n. " + resp.responseText)
					 deposit()
			}

/*
					document.getElementById( "TextBoxCustomerName"    ).value = "Not found";
					document.getElementById( "TextBoxCustomerAddress" ).value = "";
					*/
				}
				else
				{
					var info = eval ( "(" + xmlHttp.responseText + ")" );

					// No parsing necessary with JSON!
					document.getElementById( "TextBoxCustomerName"    ).value = info.jsonData[ 0 ].cmname;
					document.getElementById( "TextBoxCustomerAddress" ).value = info.jsonData[ 0 ].cmaddr1;
				}

		}
	   };

}




function deposit(){

    window.location.replace(url + "/deposit")
						  //btnToChange.onclick = function() { carenter(res.parkingSlot) }

						  //document.getElementById("returnDiv").setAttribute("style", "visibility:visible;")
	timeout = setInterval(function()
	{
		seconds--
		document.getElementById("countdown").innerHTML = seconds + "s ";
		if (seconds == 0) {
			clearInterval(x);
			window.location = prefix
		}
	}, 1000);
}


//////////
 /* fetch(apiUrl).then(response => {
      resStatus = response.status
      return response.json()
  }).then(res => {
    switch (resStatus) {
      case 200:
        console.log('success')
        console.log(res)
        var btnToChange = document.getElementById("btnToChange")
        //TODO Make it better
        btnToChange.setAttribute('data-bs-dismiss', 'alert')
        btnToChange.setAttribute('data-bs-target', '#my-alert')
        if(res.parkingSlot == 0) {
          alertWithIcon("No parking slots available, try again later", 'warning')
        }
        else {
          successAlert("Your parking slot is n. " + res.parkingSlot)
          btnToChange.innerHTML = "Car placed"
          btnToChange.onclick = function() { carenter(res.parkingSlot) }
          document.getElementById("returnDiv").setAttribute("style", "visibility:visible;")
          timeout = setInterval(function() {
            seconds--
            document.getElementById("countdown").innerHTML = seconds + "s ";
            if (seconds == 0) {
              clearInterval(x);
              window.location = '/client'
            }
          }, 1000);
        }
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

*/
