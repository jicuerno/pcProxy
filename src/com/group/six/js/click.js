/*custom Insert*/ ;
var $j = jQuery.noConflict(true);
var pcIp;

$j(document).ready(function() {
	var url = location.href;
	var bodyEle = $j("body").get(0);
	
	$j.getJSON("http://jsonip.com/?callback=?", function (data) {
		pcIp=data.ip;
	});
	
	if (bodyEle.addEventListener) {
		bodyEle.addEventListener("click",function(){},true);
	}
}).click(function(){
	submitData(arguments[0], window.location.href);
	return true;
});
	
function submitData(event,url) {
	if(event){
		var id = 0; 
		var time = $j.now(); 
		var key = getDeviceId() + '|' + pcIp;
		if(event.target){id = event.target.id;}
		$j.ajax({
			url:"http://www.myService.com",
			type:"POST",
			data:{key: key, event:event.type, url:url, id:id, time:time},
			success: function(result) { return true; }
		});
	}
}

var uuid=function(){
	var u = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
	function(c) {
	var r = Math.random() * 16 | 0,
	v = c == 'x' ? r : (r & 0x3 | 0x8);
	return v.toString(16);
	});
	return u;
}


var getDeviceId = function(){
	var current = window.localStorage.getItem("_DEVICEID_")
	if (current) return current;
	var id = uuid();
	window.localStorage.setItem("_DEVICEID_",id);   
	return id;
}


