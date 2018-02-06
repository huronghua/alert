function radioClick(radio) {
	$('#selectId').attr("value", radio.value);
}

function selectOK() {
	var returnValue = $('#selectId').val();
	if (returnValue == "") {
		alert("请选择记录");
	} else {
		if (window.opener) {
			window.opener.returnValue = returnValue;
		}
		window.returnValue = returnValue;
		window.close();
	}
}

function addJs(addUrl) {
	var statusParms = "dialogHeight: 500px; dialogWidth: 1000px;center: yes; help: no;resizable: yes; status: no;";
	window.showModalDialog(addUrl, "model", statusParms);
}

function showJs(showUrl) {
	var statusParms = "dialogHeight: 500px; dialogWidth: 1000px;center: yes; help: no;resizable: yes; status: no;";
	window.showModalDialog(showUrl, "model", statusParms);
}