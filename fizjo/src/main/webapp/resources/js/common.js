console.log('SOPI Common JS loaded.');

// Zmienne globalne // mogą być nadpisane
var SOPI_resourcesUrl = '';
var SOPI_elementsUrl = '';

// Logowanie zmian
var SOPI_globalUpdateLastId = 0;
var SOPI_globalUpdatesStarted = 0;
var SOPI_globalUpdatesDone = 0;
var SOPI_globalUpdatesDoneSuccess = 0;
var SOPI_globalUpdatesDoneError = 0; 
var SOPI_globalUpdates = [];

/******************************************************************
*
*	Obsługa globalnych statusów zmian
*
******************************************************************/
function SOPI_setUpdateStart(record,message){
	
	var updateId;
	var d = new Date();
	
	updateId = SOPI_globalUpdateLastId + 1;
	SOPI_globalUpdateLastId = updateId;
	
	var updateLog = {
		id: updateId,
		record: record,
		status: 0,
		statusText: 'w trakcie',
		event: message,
		result: null,
		startedAt: d,
		doneAt: null
	};
	
	SOPI_globalUpdatesStarted += 1;
	SOPI_globalUpdates.push(updateLog);
	
	SOPI__globalUpdateBox();
	
	return updateId;
	
}

function SOPI_setUpdateEnd(id,status,result) {
	
	var d = new Date();
	
	var updateLog = SOPI_globalUpdates[id-1];
	
	updateLog.doneAt = d;
	updateLog.result = result;
	updateLog.status = status;
	updateLog.statusText = status == 1 ? 'OK' : 'błąd';
	
	SOPI_globalUpdates[id-1] = updateLog;
	SOPI_globalUpdatesDone += 1;
	
	if (status==1){
		SOPI_globalUpdatesDoneSuccess += 1;
	} else {
		SOPI_globalUpdatesDoneError += 1;
	}
	
	SOPI__globalUpdateBox();
	
}

function SOPI__globalUpdateBox(){
			
	var SOPI_updateIcon = '<img src="' + SOPI_resourcesUrl + 'img/update.png" style="width: 24px;">';
	var SOPI_updateSuccessIcon = '<img src="' + SOPI_resourcesUrl + 'img/update_success.png" style="width: 24px;">';
	var SOPI_updateErrorIcon = '<img src="' + SOPI_resourcesUrl + 'img/update_error.png" style="width: 24px;">';
	var icon;
			
	var button = '<button class="btn btn-xs btn-primary" data-toggle="modal" data-target="#' + $('#SopiSdm').attr('id') + '">Historia zmian</button>';
	
	// komunikat na pasku 
	if (SOPI_globalUpdatesStarted == SOPI_globalUpdatesDone && SOPI_globalUpdatesDoneError == 0) {
		// KONIEC - SUKCES
		icon = SOPI_updateSuccessIcon;
		message = "Wszystkie zmiany zakończone sukcesem. Liczba wprowadzonych zmian: <b>" + SOPI_globalUpdatesDone + "</b> " + button;
		$('#SopiSco').removeClass().addClass('alert alert-success');
		
	} else if (SOPI_globalUpdatesStarted == SOPI_globalUpdatesDone && SOPI_globalUpdatesDoneError > 0) {
		// KONIEC - BŁĘDY
		icon = SOPI_updateErrorIcon;			
		message = "Wystąpiły błędy podczas zapisywania zmian. Liczba zmian zakończonych sukcesem: <b>" + SOPI_globalUpdatesDoneSuccess + "</b>, Liczba zmian zakończonych błędem: <b>" + SOPI_globalUpdatesDoneError + '</b> ' + button;
		$('#SopiSco').removeClass().addClass('alert alert-danger');
		
	} else {
		// W TRAKCIE...
		icon = SOPI_updateIcon;
		message = "Zapisywanie zmian w trakcie.... Liczba wprowadzonych zmian: <b>" + SOPI_globalUpdatesStarted + '</b>, Liczba zmian zakończonych sukcesem: <b>' + SOPI_globalUpdatesDoneSuccess + '</b>, Liczba zmian zakończonych błędem: <b>' + SOPI_globalUpdatesDoneError + '</b> ' + button;
		$('#SopiSco').removeClass().addClass('alert alert-info');
		
	}
	
	//$('#SopiSdmBody').html(errorMessages);
	
	// event, statusText, record
	
	var logMessages = "";
	
	$.each(SOPI_globalUpdates,function(index,value){
		/*
		if (value.hasOwnProperty("errors")) {
			var errors = value.errors;
			$.each(errors.errors,function(index,value){
				errorMessages += '<b>ID rekordu: ' + id + '</b><br>Treść błędu: ' + value + '<hr>';
			});
		}
		if (value.hasOwnProperty("message")) {
			var message = value.message;
			errorMessages += '<b>ID rekordu: ' + id + '</b><br>' + message + '<hr>';
		}*/
		
		if (value.doneAt) {
			logMessages += '<i>' + value.doneAt + '</i><br>';
		} else {
			logMessages += '<i>' + value.startedAt + '</i><br>';
		}
		
		logMessages += 'Rekord: <b>' + value.record + '</b>; ' + value.event + ' - ' + value.statusText;

		if (value.status == 2) {
			logMessages += '<br><u>Błędy:</u><br>';
			
			if (value.result.hasOwnProperty('errors')){
				$.each(value.result.errors,function(index2,value2){
					logMessages += '<li>' + value2 + '</li>';
				});
			}
			
			if (value.result.hasOwnProperty('status')){
				logMessages += '<li>' + value.result.status + '</li>';
			}
	
		}

		logMessages += '<hr>';
	});
	
	$('#SopiSdmBody').html(logMessages);
			
	var result = '<span>' + icon + ' ' + message + '</span>';
	$('#SopiScoMessage').html(result);
	$('#SopiSco').show();
	
};

/******************************************************************
*
*	Obsługa filtrowania tabel - tabulator
*
******************************************************************/
function SOPI_filterTabulator(container, filterValueField, filterResetField){
	
	/// filter
	function rowFilter(data) {
	
		var val = '^(?=.*\\b' + $.trim($(filterValueField).val()).split(/\s+/).join('\\b)(?=.*\\b') + ').*$',
		reg = RegExp(val, 'i'),
		text,
		row = ""; 
	 
		for(var key in data){
			if (data.hasOwnProperty(key)) {
				if ($.isArray(data[key]) || jQuery.type(data[key]) === 'object') {
					data2 = data[key];
					for(var key2 in data2){
						if (data2.hasOwnProperty(key2)) {
							row += data2[key2] + " ";
						}
					}
				} else {
					row += data[key] + " ";
				}
			}
		}
		
		text = row.replace(/\s+/g, ' ');
		if (reg.test(text)) {
			return true;
		} else {
			return false;
		}
	}
	
	$(filterValueField).keyup(function(){
		$(container).tabulator("setFilter", rowFilter);
	});
	
	$(filterResetField).on("click",function(){
		$(container).tabulator("clearFilter");
		filterValueField.val('');
	});
};

/******************************************************************
*
*	Wyświetlanie kalendarza dla pól typu input=date
*
******************************************************************/
function SOPI_inputDateCalendar(){
	console.log('SOPI Zebra DatePicker for date2 inputs activated.');
	//$('input[type=date2]').data('Zebra_DatePicker');
	$('input[type=date2]').Zebra_DatePicker({
		days: [
			'Niedziela',
			'Poniedziałek',
			'Wtorek',
			'Środa',
			'Czwartek',
			'Piątek',
			'Sobota'
		],
		months: [
			'Styczeń',
			'Luty',
			'Marzec',
			'Kwiecień',
			'Maj',
			'Czerwiec',
			'Lipiec',
			'Sierpień',
			'Wrzesień',
			'Październik',
			'Listopad',
			'Grudzień'
		],
		lang_clear_date: 'Wyczyść'
	});
};

/******************************************************************
*
*	Wczytanie zewnętrznego elementu (template .html)
*
******************************************************************/
function SOPI_loadElement(name){
	
	var element = SOPI_elementsUrl + name + '.html';
	var status = 0;

	/*$( "#SOPI_"+name ).load(element,function(response, status, xhr){
		if (status == 'error'){
			console.log('SOPI External Element not loaded. Error:' + xhr.status + ' ' + xhr.statusText);
		} else {
			console.log('SOPI Template Element: ' + name + ' from path ' + SOPI_elementsUrl + ' is loaded to #' + $(this).attr('id') + '.');
		}
		status = 1;
	});*/
		
	$.ajax({
		url: element,
		method: 'GET',
		dataType: 'html',
		async: false,
		success: function(data) {
			
			if ($('#SOPI_'+name).length>0) {
				$('#SOPI_'+name).html(data);
				console.log('SOPI Template Element: ' + name + ' from path ' + SOPI_elementsUrl + ' is loaded to #SOPI_' + name + '.');
			} else {
				var div = $('<div>', { id: 'SOPI_' + name, class: 'SOPI_TmplLoaded' });
				$('body').prepend($(div));
				$(div).html(data);
				console.log('SOPI Template Element: ' + name + ' from path ' + SOPI_elementsUrl + ' is loaded to #SOPI_' + name + ' (new element).');
			}
		},
		error: function(xhr, resp, text, responseText ) {
			console.log('SOPI Template Element: ' + element + ' not loaded. Error:' + xhr.responseText);
		}
	});

	//$( "#SOPI_").html(SOPI_element_ProfileDetailModal); 
	
}

/******************************************************************
*
*	Zapytanie AJAX z logowaniem globalnym
*
******************************************************************/

// SOPI_setUpdateStart(record, message)
// SOPI_setUpdateEnd(id,status,errors)

function SOPI_ajaxJson(conf) { // update, record, status, message
	
	// konfiguracja domyślna
	var options = {
		url: '',
		method: 'GET',
		data: '',
		record: '?',
		message: '',
		contentType: "application/json; charset=utf-8",
		actionSuccess: function(){},
		actionFailure: function(){}
	};		

	// nadpisanie konfiguracji wartościami podanymi w parametrze funkcji
	$.each(conf,function(index,value){
		options[index] = value;
	});	
	
	// dodanie loga
	var logId = SOPI_setUpdateStart(options.record, options.message);
	var alreadyLogged = 0;	
	var result = {};
	
	var events = {
		success: function(result) {
			if (result.ok != null){
				if (result.ok == true) {
					SOPI_setUpdateEnd(logId,1,result);
					setReturn({ok: true, details: result});
				} else {
					SOPI_setUpdateEnd(logId,2,result);
					setReturn({ok: false, details: result});
				}
			} else {
				SOPI_setUpdateEnd(logId, 1, result);
				setReturn({ok: true, details: result});
			}
			alreadyLogged = 1;
		},
		error: function(xhr, resp, text) {
			errorMessage = jQuery.parseJSON(xhr.responseText);
			SOPI_setUpdateEnd(logId, 2, errorMessage);
			setReturn({ok: false, details: {jqXHR: xhr, textStatus: text, resp: resp}});
			alreadyLogged = 1;
		}
	}
		
	$.each(conf,function(index,value){
		events[index] = value;
	});
		
	// wywołanie akcji
	var jqxhr = $.ajax({
		url: options.url,
		method: options.method,
		data: options.data,
		contentType: options.contentType,
		success: function(result){
			events.success(result);
			if (alreadyLogged == 0) {
				SOPI_setUpdateEnd(logId, 1, result);
				setReturn({ok: true, details: result});
			};
		},
		error: function(xhr, resp, text){
			events.error(xhr, resp, text);
			if (alreadyLogged == 0) { 
				errorMessage = jQuery.parseJSON(xhr.responseText);
				SOPI_setUpdateEnd(logId, 2, errorMessage);
				setReturn({ok: false, details: {jqXHR: xhr, textStatus: text, resp: resp}});
			};
		}
	});
	
	var setReturn = function(res){
		if (res.ok) {
			options.actionSuccess();
		} else {
			options.actionFailure();
		}
	};

}
