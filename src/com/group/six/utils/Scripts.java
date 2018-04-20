package com.group.six.utils;

public class Scripts {
	
	public String clickScript() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n /*custom Insert*/ \n");
		builder.append("\\$j(document).ready(function() {\n");
		builder.append("var location = window.location;\n");
		builder.append("var url = location.href;\n");
		builder.append("var bodyEle = \\$j(\"body\").get(0);\n");
		builder.append("if (bodyEle.addEventListener) {\n");
		builder.append("bodyEle.addEventListener(\"click\",function(){},true);\n");
		builder.append("bodyEle.addEventListener(\"submit\",function(){},true);\n");
		builder.append("}\n");
		builder.append("}).click(function(){\n");
		builder.append("submitData(arguments[0], window.location.href);\n");
		builder.append("return true;\n");
		builder.append("}).on(\"submit\",function(e){\n");
		builder.append("submitData(e,window.location.href);\n");
		builder.append("});\n");
		builder.append("\n");
		builder.append("function submitData(event,url) {\n");
		builder.append("if(event){\n");
		builder.append("var id = 0; \n");
		builder.append("var time = \\$j.now(); \n");
		builder.append("if(event.target){id = event.target.id;}\n");
		builder.append("\\$j.ajax({\n");
		builder.append("url:\"http://www.myService.com\",\n");
		builder.append("type:\"POST\",\n");
		builder.append("data:{event:event.type, url:url, id:id, time:time},\n");
		builder.append("success: function(result) {\n");
		builder.append("return true;\n");
		builder.append("}\n");
		builder.append("});\n");
		builder.append("}\n");
		builder.append("}\n");
		return builder.toString();
	}

}
