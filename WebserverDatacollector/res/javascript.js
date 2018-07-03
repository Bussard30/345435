var temps = new Array(13);
var percsh = new Array(13);
var speeds = new Array(13);
var press = new Array(13);
var tempBox = document.getElementById("tempBox");
var humidBox = document.getElementById("humidBox");
var speedBox = document.getElementById("speedBox");
var pressBox = document.getElementById("pressBox");
var rainBox = document.getElementById("rainBox");
var weatherBox = document.getElementById("weatherBox");
var dataError = "Beim Auslesen der Daten ist ein Fehler aufgetreten";
/*var str = "23:34:12:45:2:89 1:10:14:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110 1:10:11:100:101:110"*/
/*
var strSemi = new Array(13);

var strComp = new Array(13);
*/


function strSplit(strIn,splitAt,ArrayIn) {
	var strIndex = 0;
	for (var i = 0; i < strIn.length; i++) {
		if (strIn.charAt(i) == splitAt) {
			strIndex++;
		} else {
			ArrayIn[strIndex]+= strIn.charAt(i);
		}
	}
}
/*
strSplit(str,";",strSemi);

for (var i = 0; i < 13; i++) {
	strSplit(strSemi[i],":",strComp[i]);
}

for (var i = 0; i < 13; i++) {
	for (var j = 0; j < 6; j++) {
		alert(strComp[i][j]);
	}
}	

for (var i = 0; i < 13; i++) {
	temps[i] = parseInt(strComp[i][2],10);
	percsh[i] = parseInt(strComp[i][1],10);
	speeds[i] = parseInt(strComp[i][3],10);
	press[i] = parseInt(strComp[i][0],10);
}

tempBox.innerHTML = strComp[0][2];
humidBox.innerHTML = strComp[0][1];
speedBox.innerHTML = strComp[0][3];
pressBox.innerHTML = strComp[0][0];
if (strComp[0][5] == "1") {
	rainBox.innerHTML = "Yes / Ja";
} else if (strComp[0][5] == "0") {
	rainBox.innerHTML = "No / Nein";
} else {
	rainBox.innerHTML = dataError;
}
if (strComp[0][4] == "0") {
	rainBox.innerHTML = "Sunny / Sonnig";
} else if (strComp[0][4] == "1") {
	rainBox.innerHTML = "cloudy / Wölkig";
} else if (strComp[0][4] == "2") {
	rainBox.innerHTML = "rainy / Regnerisch";
} else if (strComp[0][4] == "3") {
	rainBox.innerHTML = "stormy / Stürmisch";
} else {
	rainBox.innerHTML = dataError;
}
*/
function loadUrl(newLocation)
{
window.location = newLocation;
return false;
}

/*url="file:///H:/Robotik/test.txt";

var request = new XMLHttpRequest();
request.onreadystatechange = function() {
    if (request.readyState === 4) {
        if (request.status === 200) {
            document.body.className = 'ok';
            console.log(request.responseText);
        } else if (!isValid(this.response) && this.status == 0) {
            document.body.className = 'error offline';
            console.log("The computer appears to be offline.");                
        } else {
            document.body.className = 'error';
        }
    }
};
request.open("GET", url , true);
request.send(null);
*/

httpGet("dataFile.txt");

function httpGet(theUrl)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 

    	//das hier wird sozusagen ausgeführt wenn man die Datei zurückbekommt
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)   {     	
        	//console.log("here comes the response text: " + xmlHttp.responseText);
			
			var strSemi = new Array(13);

			var strComp = new Array(13);

			for (var i = 0; i < 13; i++) {
				strComp[i] = new Array(6);
				strSemi[i] = "";
				for (var j = 0; j < 6; j++) {
					strComp[i][j] = "";
				}
			}
			
			strSplit(xmlHttp.responseText," ",strSemi);
			//strSplit(str," ",strSemi);

			for (var i = 0; i < 13; i++) {
				strSplit(strSemi[i],":",strComp[i]);
			}

			/*for (var i = 0; i < 13; i++) {
				for (var j = 0; j < 6; j++) {
					console.log(strComp[i][j]);
				}
			}*/	

			for (var i = 0; i < 13; i++) {
				temps[i] = parseFloat(strComp[i][2],10);
				percsh[i] = parseFloat(strComp[i][1],10);
				speeds[i] = parseFloat(strComp[i][3],10);
				press[i] = (parseFloat(strComp[i][0],10))-950;
			}

			tempBox.innerHTML = strComp[0][2] + "°C";
			humidBox.innerHTML = strComp[0][1] + "%";
			speedBox.innerHTML = strComp[0][3] + "km/h";
			pressBox.innerHTML = strComp[0][0] + "hPa";
			if (strComp[0][5] == "0.0") {
				rainBox.innerHTML = "Ja";
			} else if (strComp[0][5] == "1.0") {
				rainBox.innerHTML = "Nein";
			} else {
				rainBox.innerHTML = dataError;
			}
			if (strComp[0][4] == "0.0") {
				weatherBox.innerHTML = "Nacht";
			} else if (strComp[0][4] == "1.0") {
				weatherBox.innerHTML = "Bewölkt";
			} else if (strComp[0][4] == "2.0") {
				weatherBox.innerHTML = "Sonnig";
			} else {
				weatherBox.innerHTML = dataError;
			}
			
			initDiags();
			
		}

    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous 
    xmlHttp.send();    
}

function initDiags() {
var pillarWidth = 40;
var shadowWidth = 10;

//Temperature/Temperatur
var c = document.getElementById('diagram_temperature');
var ctx = c.getContext("2d");
ctx.lineWidth = 1;
var unit = "°C";
var canvasWidth = 800;
var canvasHeight = 400;
c.setAttribute('width', canvasWidth);
c.setAttribute('height', canvasHeight);
var graphGridSize = 20;
var graphGridX = (canvasWidth / graphGridSize).toFixed();
ctx.fillStyle="white";
ctx.fillRect(0,0,800,400);
for(var i = 0; i < graphGridX; i ++){
	ctx.moveTo(canvasWidth, graphGridSize*i);
	ctx.lineTo(0, graphGridSize*i);
}
ctx.strokeStyle = "grey";
ctx.stroke();
ctx.strokeStyle="grey";
var drawPadding = 100;

/*for (var i = 0; i < temps.length; i++) {
	var drawHeight = 400-(temps[i]*8);
	ctx.strokeRect(drawPadding-1, drawHeight-1, 42, 400);
	drawPadding += 52;
}

ctx.stroke();*/
drawPadding=100;
ctx.fillStyle="rgb(224, 155, 87)";
/*for (var i = 0; i < temps.length; i++) {
	var drawHeight = 320-(temps[i]*8);
	ctx.fillRect(drawPadding, drawHeight, pillarWidth, 320-drawHeight);
	ctx.fillStyle="rgb(198, 126, 55)";
	ctx.fillRect(drawPadding+(pillarWidth-shadowWidth), drawHeight, shadowWidth, 320-drawHeight);
	if(temps[i] == 0) {
		ctx.fillStyle="rgb(224, 155, 87)";
		ctx.fillRect(drawPadding, 319, pillarWidth, 2);
		ctx.fillStyle="rgb(198, 126, 55)";
		ctx.fillRect(drawPadding+(pillarWidth-shadowWidth), 319, shadowWidth, 2);
	}
	ctx.font = "13px Arial";
	ctx.fillStyle = "rgba(255,255,255,0.4)";
	if (i == 0) {
		ctx.fillRect(drawPadding,306,pillarWidth,28);
		ctx.fillStyle = "black";
		ctx.fillText("jetzt",drawPadding+2,324);
	} else if (i < 4) {
		ctx.fillRect(drawPadding,306,pillarWidth,28);
		ctx.fillStyle = "black";
		ctx.fillText(i*15 + "min",drawPadding + 2, 331);
		ctx.fillText("vor",drawPadding + 4,317);
	} else {
		ctx.fillRect(drawPadding,306,pillarWidth,28);
		ctx.fillStyle = "black";
		ctx.fillText(i*0.25+"h",drawPadding + 2, 331);
		ctx.fillText("vor",drawPadding + 4, 317);
	}
	ctx.fillStyle="rgb(224, 155, 87)";
	drawPadding += 52;
}
*/
makeDiagram(320,52,ctx,temps,"rgb(224, 155, 87)",8);
ctx.fillStyle="black";
ctx.font = "15px Arial";
ctx.fillText("Temperatur", 4, 14);
ctx.fillStyle="black";
var distance = 35;
var textheight = 38;
for (var i = 0; i < 10; i++) {
	ctx.fillText(distance+unit,4,textheight);
	distance -= 5;
	textheight += 40;
}
ctx.fillStyle="black";
//Percs
var c = document.getElementById('diagram_percs');
var ctx = c.getContext("2d");
ctx.lineWidth = 1;
var unit = "%";
var canvasWidth = 800;
var canvasHeight = 400;
c.setAttribute('width', canvasWidth);
c.setAttribute('height', canvasHeight);
var graphGridSize = 20;
var graphGridX = (canvasWidth / graphGridSize).toFixed();
ctx.fillStyle="white";
ctx.fillRect(0,0,800,400);
for(var i = 0; i < graphGridX; i ++){
	ctx.moveTo(canvasWidth, graphGridSize*i);
	ctx.lineTo(0, graphGridSize*i);
}
ctx.strokeStyle = "grey";
/*ctx.fillStyle="rgb(61, 132, 247)"; // blau
ctx.fillRect(80, 4, 10, 10);
ctx.strokeRect(80, 4, 10, 10);*/
ctx.stroke();
/*var drawPadding = 100;

for (var i = 0; i < percsh.length; i++) {
	var drawHeightHumi = 400-(400*(percsh[i]/100));
	ctx.fillStyle="rgb(61, 132, 247)"; // blau
	ctx.fillRect(drawPadding, drawHeightHumi, pillarWidth, 400+(400*(percsh[i]/100)));
	ctx.fillStyle="rgb(38, 97, 193)"; // dunkelblau
	ctx.fillRect(drawPadding+(pillarWidth-shadowWidth), drawHeightHumi, shadowWidth, 400+(400*(percsh[i]/100)));
	ctx.font = "13px Arial";
	ctx.fillStyle="rgba(255,255,255,0.4)";
	timeDescript(i,drawPadding,pillarWidth,ctx);
	drawPadding += 52;
}*/
makeDiagram(400,52,ctx,percsh,"rgb(61, 132, 247)",4);
ctx.fillStyle="black";
ctx.font = "15px Arial";
ctx.fillText("Luftfeuchtigkeit", 4, 14);
ctx.fillStyle="black";
var distance = 90;
var textheight = 38;
for (var i = 0; i < 10; i++) {
	ctx.fillText(distance+unit,4,textheight);
	distance -= 10;
	textheight += 40;
}
ctx.fillStyle="black";

//windspeed

var c = document.getElementById('diagram_windspeed');
var ctx = c.getContext("2d");
ctx.lineWidth = 1;
var unit = "km/h";
var canvasWidth = 800;
var canvasHeight = 400;
c.setAttribute('width', canvasWidth);
c.setAttribute('height', canvasHeight);
var graphGridSize = 20;
var graphGridX = (canvasWidth / graphGridSize).toFixed();
ctx.fillStyle="white";
ctx.fillRect(0,0,800,400);
ctx.strokeStyle = "grey";
for(var i = 0; i < graphGridX; i ++){
	ctx.moveTo(canvasWidth, graphGridSize*i);
	ctx.lineTo(0, graphGridSize*i);
}
ctx.stroke();
ctx.strokeStyle="grey";
var drawPadding = 100;

ctx.fillStyle="rgb(220, 220, 255)";
/*for (var i = 0; i < speeds.length; i++) {
	var drawHeight = 400-(speeds[i]*4);
	ctx.strokeRect(drawPadding-1, drawHeight-1, 42, 400+(speeds[i]*4));
	drawPadding += 52;
}
ctx.stroke();*/
/*drawPadding = 100;
ctx.fillStyle="rgb(150, 241, 255)";
for (var i = 0; i < speeds.length; i++) {
	var drawHeight = 400-(speeds[i]*4);
	ctx.fillRect(drawPadding, drawHeight, pillarWidth, 400+(speeds[i]*4));
	ctx.fillStyle="rgb(118, 190, 201)"
	ctx.fillRect(drawPadding+(pillarWidth-shadowWidth), drawHeight, shadowWidth, 400+(speeds[i]*4));
	ctx.font = "13px Arial";
	ctx.fillStyle="rgba(255,255,255,0.4)";
	timeDescript(i,drawPadding,pillarWidth,ctx);
	ctx.fillStyle="rgb(150, 241, 255)"
	drawPadding += 52;
}*/
makeDiagram(400,52,ctx,speeds,"blue",4);
ctx.fillStyle="black";
ctx.font = "15px Arial";
ctx.fillText("Windgeschwindigkeit", 4, 14);
ctx.fillStyle="black";
var distance = 90;
var textheight = 38;
for (var i = 0; i < 10; i++) {
	ctx.fillText(distance+unit,4,textheight);
	distance -= 10;
	textheight += 40;
}
ctx.fillStyle="black";

//airpressure

var c = document.getElementById('diagram_airpressure');
var ctx = c.getContext("2d");
ctx.lineWidth = 1;
var unit = "hPa";
var canvasWidth = 800;
var canvasHeight = 400;
c.setAttribute('width', canvasWidth);
c.setAttribute('height', canvasHeight);
var graphGridSize = 20;
var graphGridX = (canvasWidth / graphGridSize).toFixed();
ctx.fillStyle="white";
ctx.fillRect(0,0,800,400);
ctx.strokeStyle = "grey";
for(var i = 0; i < graphGridX; i ++){
	ctx.moveTo(canvasWidth, graphGridSize*i);
	ctx.lineTo(0, graphGridSize*i);
}
ctx.stroke();
var drawPadding = 100;

ctx.fillStyle="rgb(255, 150, 150)";
ctx.strokeStyle="grey";
drawPadding=100;
/*for (var i = 0; i < press.length; i++) {
	var drawHeight = 400-((press[i]-950)*4);
	ctx.strokeRect(drawPadding-1, drawHeight-1, 42, 400);
	drawPadding += 52;
}
ctx.stroke();*/
/*drawPadding = 100;
ctx.fillStyle="rgb(219, 0, 80)";
for (var i = 0; i < press.length; i++) {
	var drawHeight = 400-((press[i]-950)*4);
	console.log(press[i]);
	ctx.fillRect(drawPadding, drawHeight, pillarWidth, 400+((press[i]-950)*4));
	ctx.fillStyle="rgb(145, 0, 53)"
	ctx.fillRect(drawPadding+(pillarWidth-shadowWidth), drawHeight, shadowWidth, 400+((press[i]-950)*4));
	ctx.font = "13px Arial";
	ctx.fillStyle="rgba(255,255,255,0.4)";
	timeDescript(i,drawPadding,pillarWidth,ctx);
	ctx.fillStyle="rgb(219, 0, 80)";
	drawPadding += pillarWidth + 12;
}

*/
makeDiagram(400,52,ctx,press,"rgb(145, 0, 53)",4);
ctx.fillStyle="black";
ctx.font = "15px Arial";
ctx.fillText("Luftdruck", 4, 14);
ctx.fillStyle="black";
var distance = 1040;
var textheight = 38;
for (var i = 0; i < 10; i++) {
	ctx.fillText(distance+unit,4,textheight);
	distance -= 10;
	textheight += 40;
}
ctx.fillStyle="black";
}

function timeDescript(index,drawPad,pilWidth,con) {
	con.fillRect(drawPad,370,pilWidth,30);
	con.fillStyle="black";
	if (index == 0) {
		con.fillText("jetzt",drawPad+2,390);
	} else if (index < 4) {
		con.fillText(index*15 + "min",drawPad + 2, 397);
		con.fillText("vor",drawPad + 4,382);
	} else {
		con.fillText(index*0.25+"h",drawPad + 2, 397);
		con.fillText("vor",drawPad + 4, 382);
	}
}

function makeDiagram(zero,distance,context,dataArray,color,heightFactor) {
	context.strokeStyle = "red";
	context.beginPath();
	context.moveTo(0,zero);
	context.lineTo(800,zero);
	context.stroke();
	var drawPadding = 100;
	
	for (var i = 0; i < dataArray.length;i++) {
		context.lineWidth = 2;
		context.fillStyle = "black";
		context.strokeStyle = color;
		var drawHeight = zero-(dataArray[i]*heightFactor);
		if (i == 0) {
			//context.fillRect(drawPadding-2,drawHeight-2,4,4);
		} else {
			
			var lastDrawHeight = zero-(dataArray[i-1]*heightFactor);
			var lastPixelIndex;
			var lastYCoord;
			/*context.beginPath();
			context.moveTo(drawPadding-distance,lastDrawHeight);
			context.lineTo(drawPadding,drawHeight);
			context.stroke();*/
			for (var pixelIndex = -1; pixelIndex < distance; pixelIndex++) {
				lastIpoWert = -Math.sin(((pixelIndex-1)/distance)*Math.PI - (0.5*Math.PI))*0.5+0.5;
				lastYCoord = drawHeight-lastIpoWert*(dataArray[i-1]*heightFactor-dataArray[i]*heightFactor);
				if (i>0) {
					var ipoWert;
					ipoWert = -Math.sin((pixelIndex/distance)*Math.PI - (0.5*Math.PI))*0.5+0.5;
					yCoord = drawHeight-ipoWert*(dataArray[i-1]*heightFactor-dataArray[i]*heightFactor);
					context.fillStyle=color;
					//context.fillRect(drawPadding+pixelIndex-distance,yCoord,2,2);
					context.beginPath();
					context.moveTo(drawPadding+pixelIndex-distance-1,lastYCoord);
					context.lineTo(drawPadding+pixelIndex-distance,yCoord);
					context.stroke();
					
				}
				
			}
			context.fillStyle="black";
			//context.fillRect(drawPadding-2,drawHeight-2,5,5);
			
		}
		
		context.font = "13px Arial";
		context.fillStyle = "rgba(150,150,150,0.4)";
		var txtHeight = drawHeight+2;
		if (i == 0) {
			context.fillRect(drawPadding-20,txtHeight,40,28);
			context.fillStyle = "black";
			context.fillText("jetzt",(drawPadding+2)-20,txtHeight+18);
		} else if (i < 4) {
			context.fillRect(drawPadding-20,txtHeight,40,28);
			context.fillStyle = "black";
			context.fillText(i*15 + "min",(drawPadding + 2)-20, txtHeight+24);
			context.fillText("vor",(drawPadding + 4)-20,txtHeight+10);
		} else {
			context.fillRect(drawPadding-20,txtHeight,40,28);
			context.fillStyle = "black";
			context.fillText(i*0.25+"h",(drawPadding + 2)-20, txtHeight+24);
			context.fillText("vor",(drawPadding + 4)-20, txtHeight+10);
		}
		drawPadding += distance;
	}
	var drawPadding = 100;
	for (var i = 0; i < dataArray.length; i++){
		var drawHeight = zero-(dataArray[i]*heightFactor);
		
		if (i == 0) {
			context.fillRect(drawPadding-2,drawHeight-2,5,5);
		} else {
			context.fillRect(drawPadding-2,drawHeight-2,5,5);
		}
		drawPadding += distance;
	}
	
}