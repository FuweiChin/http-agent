<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>Download Proxy</title>
</head>
<body>

<form autocomplete="off">
<fieldset>
	<legend>Proxy 1: specify url with search param 'url'</legend>
	<input type="text" name="url" autocomplete="off" placeholder="Enter an URL here"
			required="required" pattern="https?://.+" maxlength="4096"
			oninput="handleChange(event)"
			style="width:calc(100% - 6em);" />
	<a href="javascript:;" target="_blank" id="btnGet">Get</a>
</fieldset>
</form>

<form autocomplete="off">
<fieldset>
	<legend>Proxy 2: speficy url with path name and search</legend>
	<input type="text" name="url" autocomplete="off" placeholder="Enter an URL here"
			required="required" pattern="https?://.+" maxlength="4096"
			oninput="handleChange2(event)"
			style="width:calc(100% - 6em);" />
	<a href="javascript:;" target="_blank" id="btnGet2">Get</a>
</fieldset>
</form>

<script>
function handleChange(e){
	var a=document.getElementById("btnGet");
	if(!e.target.checkValidity()){
		a.href="javascript:;";
		return;
	}
	a.href="http://proxy.bldgos.net/get?url="+encodeURIComponent(e.target.value);
}

function handleChange2(e){
	var a=document.getElementById("btnGet2");
	if(!e.target.checkValidity()){
		a.href="javascript:;";
		return;
	}
	a.href="http://proxy.bldgos.net/"+e.target.value.replace(/^(https?):\//,"$1")
}
</script>
</body>
</html>
