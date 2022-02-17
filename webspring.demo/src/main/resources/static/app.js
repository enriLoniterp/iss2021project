prefix = "http://localhost:8081"
var tokenid = ""

function reqenter() {

   var xhr = new XMLHttpRequest();
   var url = prefix + '/reqenter';


   xhr.onreadystatechange = function(){
	  if ( xhr.readyState == 4  )
	  {
			if(xhr.status == 200){
					if(xhr.parkingSlot == 0) {
						alert("No parking slots available, try again later", 'warning')
					} else {
					 slotnum = xhr.responseText
					 alert("Your parking slot is n. " + slotnum)
					 deposit(slotnum)
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
    alert("carenter" + slotnum)
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

function deposit2(){

}

function deposit(slotnum){
var slot=slotnum
alert(slot)
window.location.replace(prefix + "/deposit")
var dep = document.getElementById("deposit")
   dep.innerHTML='ciao'
   dep.onclick = function() { carenter(slot) }
/*
var apiUrl = prefix + "/deposit"
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

*/

}
function timer(tokenid){

  var apiUrl = prefix + '/timer?tokenid=' + tokenid;

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
  var tokenid = document.getElementById("tokenid").value
  if(tokenid == "") {
    var btnToChange = document.getElementById("btnToChange")
    return
  }
  var apiUrl = prefix + '/reqexit?tokenid=' + tokenid;
  fetch(apiUrl).then(response => {
      resStatus = response.status
      if(resStatus != 200)
        return response.json()
      else
        return response
  }).then(res => {
    switch (resStatus) {
      case 200:
        console.log(res)

      //  btnToChange.onclick = function() { window.location = '/clientOut' }
        document.getElementById("form").setAttribute("style", "display:none;")
        document.getElementById("returnDiv").setAttribute("style", "visibility:visible;")

       /* var x = setInterval(function() {
          seconds--
          document.getElementById("countdown").innerHTML = seconds + "s ";
          if (seconds == 0) {
            clearInterval(x);
            window.location = '/clientOut'
          }
        }, 1000);
        break
        */
      case 400:
        console.log('error')
        console.log(res)
        var btnToChange = document.getElementById("btnToChange")
        break
      case 403:
        console.log('error')
        console.log(res)
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
