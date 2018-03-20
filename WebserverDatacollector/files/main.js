//httpGet("test.txt");
//httpGet("test2.txt");
//httpGet("test3.txt");
httpGet("dataFile.txt");


function httpGet(theUrl)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 

    	//das hier wird sozusagen ausgeführt wenn man die Datei zurückbekommt
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)        	
        	console.log("here comes the response text: '" + xmlHttp.responseText + "'");

    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous 
    xmlHttp.send();    
}
 
