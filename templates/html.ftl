<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN">
<html>
<head>
<title>model.html</title>

<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="this is my page">
<meta http-equiv="content-type" content="text/html; charset=utf-8">

<!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
<style type="text/css">
table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
.oddrowcolor{
	background-color:#d4e3e5;
}
.evenrowcolor{
	background-color:#c3dde0;
}
a:link{color:black  }
a:visited{color:purple}
a:hover{color:red}
#aa{
background: #CAE1FF;
}
#bb{
background: #FFFFFF;
}
.cc{
background: #FFFFFF;
}
     .css1 {background-color: #ffffff;}
     .css2 {background-color: #1E90FF;}
</style>
<script language="javascript">
   function mouseOver(obj){
      if(obj.className="css1")
         obj.className="css2";
   }
   function mouseOut(obj){
      if(obj.className="css2")
         obj.className="css1";
   }
</script>
</head>

<body>
　　　　 <center><h1>接口文档注释</h1></center>
	<a name="top"></a>
	<#list interfaceTitles as interfaceTitle>
		* ${interfaceTitle}
	</#list>

	<#list interfaceDetails as interfaceDetail>
		<span id="request"></span>
		<h2>${interfaceDetail_index+1!}. ${interfaceDetail["title"]!}</h2>
		<p>${interfaceDetail["requestType"]!}</p>
		请求参数:
		<br/>
		<table  width="90%" height="100%"  border="1" cellpadding="0" cellspacing="0" style="margin-top: 20px; margin-left: 20px;" id="alternatecolor" class="altrowstable" >
			<tr class="cc">
				<td align="center" width="70">字段名称</td>
				<td align="center" width="70">类型</td>
				<td align="center" width="70">是否必填</td>
				<td align="center" >说明</td>
			</tr>
			<#list interfaceDetail["request"] as request>
				<tr onMouseOver='mouseOver(this)'  onMouseOut='mouseOut(this)'>
					<td align="center">${request.name!}</td>
					<td align="center">${request.type!}</td>
					<td align="center">
						<#if request.isNotNull == 0>
							否
						<#else>
							是
						</#if>
					</td>
					<td align="center">${request.desc!}</td>
				</tr>
			</#list>
		</table>

		<br/>
			<span id="response"></span>
			响应参数:
			<table  width="90%" height="100%"  border="1" cellpadding="0" cellspacing="0" style="margin-top: 20px; margin-left: 20px;" id="alternatecolor" class="altrowstable" >
				<tr class="cc">
					<td align="center" width="70">字段名称</td>
					<td align="center" width="70">类型</td>
					<td align="center" >说明</td>
				</tr>
				<#list interfaceDetail["response"] as response>
				<tr onMouseOver='mouseOver(this)'  onMouseOut='mouseOut(this)'>
					<td align="center">${response.name!}</td>
					<td align="center">${response.type!}</td>
					<td align="center">${response.desc!}</td>
				</tr>
				</#list>
			</table>
		<br/>
		<a href="#top">回到顶部</a>
		<br/>
		<hr/>
	</#list>


</body>
</html>
