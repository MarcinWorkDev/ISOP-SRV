var pageScriptSchedule = {
	
	/******************************************************
	/	Deklaracje	zmiennych
	/*****************************************************/
	
	// zmienne globalne
	global: {
		domainUrl: '',
		resourcesUrl: '',
		elementsUrl: ''
	},
		
	// wygenerowanie adresów obrazków
	icons: function(){
		
		var global = this.global;
				
		var icons = {
			avatarMaleIcon: '<img src="' + global.resourcesUrl + 'img/avatar_male.png" style="width: 20px;">',
			avatarFemaleIcon:  '<img src="' + global.resourcesUrl + 'img/avatar_female.png" style="width: 20px;">',
			avatarOtherIcon:  '<img src="' + global.resourcesUrl + 'img/avatar_other.png" style="width: 20px;">',
			avatarMale: '<img src="' + global.resourcesUrl + 'img/avatarMale.png" style="width: 128px;">',
			avatarFemale:  '<img src="' + global.resourcesUrl + 'img/avatarFemale.png" style="width: 128px;">',
			avatarOther:  '<img src="' + global.resourcesUrl + 'img/avatarOther.png" style="width: 128px;">',
			activeIcon:  '<img src="' + global.resourcesUrl + 'img/active.png" style="width: 18;">',
			deleteIcon:  '<img src="' + global.resourcesUrl + 'img/delete.png" style="width: 20;">',
			detailIcon:  '<img src="' + global.resourcesUrl + 'img/detail.png" style="width: 20;">',
			editIcon:  '<img src="' + global.resourcesUrl + 'img/edit.png" style="width: 20;">',
			userIcon:  '<img src="' + global.resourcesUrl + 'img/web_account.png" style="width: 20;">',
			scheduleIcon: '<img src="' + global.resourcesUrl + 'img/schedule.png" style="width: 20;">',
			scheduleIcon2: '<img src="' + global.resourcesUrl + 'img/schedule2.png" style="width: 20;">'
		};
		return icons;
	},
		
	// konstruktor
	_create: function(){
				
		console.log('SOPI PageScript for Schedule page loaded.');
		
		var self = this;
		var global = self.global;
		var icons = self.icons();
		
		/******************************************************
		/	Funkcje obsługi onClick - dodanie eventów
		/*****************************************************/
		
		$('#SccCloseButton').on('click',function(){
			$('#Scc').collapse('hide');
		});
		
		/******************************************************
		/	Definicje tabulatorów - wygenerowanie i wstawienie tabulatorów
		/*****************************************************/
			
		// Tabulator profili pracowników
		$('#Slc').tabulator({
		ajaxURL: global.domainUrl + 'api/module/profile/getPracownik',
		index: 'profileId',
		//fitColumns:true,
		height: 300,
		sortable: false,
		columns: 
			[
			 {
				title: "ID",
				field: "profileId",
				width: 40
			 },
			{
				title: 'Typ profilu',
				field: 'type',
				width: 100
			},
			{
				field: 'hasUser',
				formatter: function(value){ return value ? icons.userIcon : ''; },
				width: 32
			},
			{
				title: 'Nazwisko',
				field: 'nazwisko',
				width: 130,
				formatter: function(value){ return '<b>' + value + '</b>'; }
			},
			{
				title: 'Imie',
				field: 'imie',
				width: 110,
				formatter: function(value){ return '<b>' + value + '</b>'; }
			},
			{
				title: 'Pesel',
				field: 'pesel',
				width: 115
			},
			{
				title: 'Płeć',
				field: 'plecName',
				width: 130,
				formatter: function(value, data){
					var avatarIcon;
					
					switch (data.plec){
						case 'M':
							avatarIcon = icons.avatarMaleIcon;
							break;
						case 'F':
							avatarIcon = icons.avatarFemaleIcon;
							break;
						default:
							avatarIcon = icons.avatarOtherIcon;
							break;
					}
					
					return avatarIcon + ' ' + value;
				}
			},
			{
				formatter: function(){ return icons.detailIcon; },
				width: 32,
				onClick: function(e, cell, value, data){
					
					var titleItem = $('#PdmTitle');						
					var modal = $('#Pdm');
					
					titleItem.html('Profil');
					
					$('.PdmDataRow').each(function(index){
						
						var attr = $(this).attr("pdm-field");
						var val = data[attr]; 
						
						var more = {
							avatar: function(){
									var icon;
									
									switch (data.plec){
										case 'F': icon = icons.avatarFemale; break;
										case 'M': icon = icons.avatarMale; break;
										default: icon = icons.avatarOther; break;
									}
									
									return icon;
								},
							userStatus: function(){ return data.hasUser ? icons.userIcon + ' Do profilu jest przypisane konto użytkownika.' : 'Profil nie posiada przypisanego konta użytkownika.' }
						};
						
						if (val == null) {
							$(this).html(more[attr]);
						} else {
							$(this).html(val);
						}
					});
					
					modal.modal("show");
				},
			},
			{
				formatter: function(){ return icons.scheduleIcon2; },
				width: 32,
				onClick: function(e, cell, value, data){
					
					$('#SccCalendarOwner').html(data.nazwisko + ' ' + data.imie + ' (' + data.pesel + ')');
					
					$('#Scc').collapse('show');
					
					$('#SccCalendar').fullCalendar('render');
					
					$('#SccCalendar').fullCalendar('removeEvents');
					$('#SccCalendar').fullCalendar('removeEventSources');
					var event = { url: global.domainUrl + 'api/module/visit/schedule/' + data.profileId };				
					$('#SccCalendar').fullCalendar('addEventSource',event);
					
				}
			}
			]
		});
		
		$('#SccCalendar').fullCalendar({
			loading: function( isLoading, view ) {
				if(isLoading) {
					$('#SccCalendarLoaderStatus').removeClass('progress-bar-success').addClass('progress-bar-striped');
					$('#SccCalendarLoaderStatus').html('Trwa wczytywanie danych...');
				} else {
					$('#SccCalendarLoaderStatus').removeClass('progress-bar-striped').addClass('progress-bar-success');
					$('#SccCalendarLoaderStatus').html('Dane zostały wczytane.');
				}
			},
			editable: true,
			eventDurationEditable: false,
			selectable: true,
			selectHelper: true,
			defaultView: 'agendaWeek',
			//hiddenDays: [ 6,0 ],
			columnFormat: 'dd, D MMMM',
			views: 
				{
					agenda:
						{
							allDaySlot: false,
							slotDuration: '01:00:00',
							minTime: '06:00',
							maxTime: '21:00'
						}
				},
			/*eventClick:  function(event, jsEvent, view) {
				$('#modalTitle').html(event.title);
				$('#modalBody').html(event.description);
				
				switch(event.eventType) {
					case -1:
						$('#eventUrl').hide();
						$('#eventUrl2').hide();
						break;
					case 0:
						$('#eventUrl').show();
						$('#eventUrl2').hide();
						$('#eventUrl').attr('href','[[@{/grafik/wizyta/nowy/}]]/'+event.grafikId);
						$('#eventUrl').html('Umów wizytę');
						break;
					case 1:
						$('#eventUrl').show();
						$('#eventUrl2').hide();
						$('#eventUrl').attr('href','[[@{/pacjent/profil}]]/'+event.pacjentId);
						$('#eventUrl').html('Profil pacjenta');
						//$('#eventUrl2').attr('href','[[@{/grafik/wizyta}]]/'+event.wizytaId);
						break;
					case 2:
						$('#eventUrl').hide();
						$('#eventUrl2').hide();
						//$('#eventUrl2').attr('href','[[@{/grafik/wizyta}]]/'+event.wizytaId);
						break;
				}
				
				$('#eventUrl').attr('href',event.url);
				$('#eventUrl').attr('href',event.url);
				$('#eventUrl').attr('href',event.url);
				
				$('#fullCalModal').modal();
			}*/
		});
	}
}