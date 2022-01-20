



function reqenter() {
    console.log('siamo qui?')
 document.getElementById('pu').innerHTML = "tua mamma"
   var xmlHttp = new XMLHttpRequest();
    var url = 'http://localhost:8081/reqenter'
      xmlHttp.open( "GET", url , true );
      xmlHttp.send( );
      xmlHttp.onreadystatechange = function(){
	  if ( xmlHttp.readyState == 4  )
		{
			if(xmlHttp.status == 200){
            alert(xmlHttp.responseText);

					if(xmlHttp.parkingSlot == 0) {
						alertWithIcon("No parking slots available, try again later", 'warning')
					} else {
						// successAlert("Your parking slot is n. " + res.parkingSlot)

						  //btnToChange.onclick = function() { carenter(res.parkingSlot) }

						  //document.getElementById("returnDiv").setAttribute("style", "visibility:visible;")
						/*  timeout = setInterval(function() {
							seconds--
							document.getElementById("countdown").innerHTML = seconds + "s ";
							if (seconds == 0) {
							  clearInterval(x);
							  window.location = '/client'
							}
						  }, 1000);
						  */
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
