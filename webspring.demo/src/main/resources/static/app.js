prefix = "http://localhost:8081"

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