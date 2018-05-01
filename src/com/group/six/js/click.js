/*custom Insert*/ ;

var pcIp;
var keyUser='####keyUser####';
var keyTarea='####keyTarea####';

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
		if(event.target){id = event.target.id;}
		$j.ajax({
			url:"http://www.myService.com",
			type:"POST",
			data:{keyUser:keyUser, keyTarea:keyTarea, event:event.type, url:url, id:id, time:time, pcIp:pcIp},
			success: function(result) { return true; }
		});
	}
}
