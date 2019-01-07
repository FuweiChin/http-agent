<%@page session="false" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=720, initial-scale=1" />
<title>HTTP Download Agent</title>
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
<link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="HTTP Download Agent" />
<style>
td{
	vertical-align: top;
}
input[name="url"]{
	width: calc(100% - 15px);
}
input[name="url"].pattern-mismatch{
	background-color: #ffe3e3;
}
</style>
</head>
<body>
<form method="get" action="http://proxy.bldgos.net:8080/get" autocomplete="off" target="_blank">
	<h2>HTTP Download Agent</h2>
	<table style="width:100%;">
	<colgroup>
		<col style="width:100px" />
		<col style="width:auto;" />
		<col style="width:200px" />
	</colgroup>
	<tr>
		<td>Resource:</td>
		<td><input type="text" name="url" autocomplete="off" autofocus="autofocus"
				required="required" pattern="https?://.+" maxlength="4096"
				placeholder="Enter resource link to download" oninput="validateLink(event)" /></td>
		<td><button type="submit">Download</button>
			<button type="submit" formaction="https://proxy.bldgos.net:8443/get">with HTTPS</button>
		</td>
	</tr>
	<tr>
		<th>&nbsp;</th>
		<td colspan="2"><output id="proxyUrl">&nbsp;</output></td>
	</tr>
	</table>
</form>
<div>
	<h2>Usage</h2>
	<ul>
		<li>In above text box, paste a resource link and press 'Enter'</li>
		<li>In Chrome address bar, type 'proxy.bldgos.net' and press 'Tab', paste a resource link and press 'Enter'</li>
	</ul>
	<h3>Proxy Info</h3>
	<ul>
		<li>GeoIP: Central District, Hong Kong</li>
		<li>Exchange Carrier: IT7 Networks</li>
	</ul>
</div>
<script>
function validateLink(event){
	var input=event.target;
	input.classList.toggle("pattern-mismatch",input.validity.patternMismatch);
	input.setCustomValidity("");
	if(input.validity.valid){
		input.setCustomValidity("");
		document.getElementById("proxyUrl").textContent=input.form.action+"?url="+encodeURIComponent(input.value);
	}else{
		if(input.validity.valueMissing){
			input.setCustomValidity("Resource link expected");
		}else{
			input.setCustomValidity("Invalid resource link");
		}
		document.getElementById("proxyUrl").textContent="";
	}
}
if(external.IsSearchProviderInstalled&&!external.IsSearchProviderInstalled("/opensearch.xml")){
	external.AddSearchProvider("/opensearch.xml");
}
</script>

</body>
</html>
