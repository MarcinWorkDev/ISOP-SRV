var pageScriptUser = {
	
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
			rolesIcon:  '<img src="' + global.resourcesUrl + 'img/roles.png" style="width: 20px;">',
			activeIcon:  '<img src="' + global.resourcesUrl + 'img/active.png" style="width: 18;">',
			deleteIcon:  '<img src="' + global.resourcesUrl + 'img/delete.png" style="width: 20;">',
			userIcon:  '<img src="' + global.resourcesUrl + 'img/web_account.png" style="width: 20;">',
		};
		return icons;
	},
		
	// konstruktor
	_create: function(){
				
		console.log('SOPI PageScript for Visit page loaded.');
		
		var self = this;
		var global = self.global;
		var icons = self.icons();
		
		/******************************************************
		/	Funkcje obsługi onClick - dodanie eventów
		/*****************************************************/
				
		$('#SgcButton').on('click',function(){
			$('#Sgc').collapse('toggle');
			$('#SgcCalendar').fullCalendar('render');
		});
				
		/******************************************************
		/	Definicje tabulatorów - wygenerowanie i wstawienie tabulatorów
		/*****************************************************/
				
		// Tabulator użytkowników
		$('#Vlc').tabulator({
		ajaxURL: global.domainUrl + 'api/module/visit/list',
		index: 'visitId',
		fitColumns:true,
		pagination: true,
		paginationSize: 15,
		height: 550,
		sortable: false,
		tooltips:true,
		columns: 
			[
			 {
				title: "ID",
				field: "visitId",
				width: 40
			 },
			 {
				title: "Data",
				formatter: function(value, data, cell, row, options){
					return data.schedule.dateId.date;
				},
				width: 100
			 },
			 {
				title: "Godzina",
				formatter: function(value, data, cell, row, options){
					return data.schedule.timeId.timeText;
				},
				width: 100
			 },{
				title: "Status",
				field: "statusText",
				width: 160
			 },{
				title: "Odwołania",
				field: "canceled",
				formatter: "tickCross",
				width: 120
			 },{
				title: "Rezultat",
				formatter: function(){
					return '<button class="btn btn-xs">Pokaż rezultat</button>';
				},
				onClick: function(e, cell, value, data){
					
					$('#VdmTitle').html('Rezultat wizyty');
					
					$('#VdmStatus').html('');
					if (data.canceled){
						$('#VdmStatus').append(new Option("Wizyta odwołana", "ODW"));
					} else {
						if (data.schedule.past){
							$('#VdmStatus').append(new Option("Wizyta odbyta", "ZAP"));
							$('#VdmStatus').append(new Option("Wizyta nieodbyta", "ZAN"));
						} else {
							$('#VdmStatus').append(new Option("Wizyta planowana", "PLA"));
						}
					}
					
					$('#VdmStatus').val(data.status).change();
					
					CKEDITOR.instances.VdmResult.setData(data.result);
					
					$('#Vdm').modal('show');
					
				},
				width: 120
			 },
			 {
				title: "Pacjent",
				field: "profile",
				onClick: function(e, cell, value, data){
					
					var titleItem = $('#PdmTitle');						
					var modal = $('#Pdm');
					
					titleItem.html('Profil pacjenta: ' + data.profile.nazwisko + ' ' + data.profile.imie);
					
					$('.PdmDataRow').each(function(index){
						
						var attr = $(this).attr("pdm-field");
						var val = data.profile[attr]; 
												
						var more = {
							avatar: function(){
									var icon;
									
									switch (data.profile.plec){
										case 'F': icon = icons.avatarFemale; break;
										case 'M': icon = icons.avatarMale; break;
										default: icon = icons.avatarOther; break;
									}
									
									return icon;
								},
							userStatus: function(){ return data.profile.hasUser ? icons.userIcon + ' Do profilu jest przypisane konto użytkownika.' : 'Profil nie posiada przypisanego konta użytkownika.' }
						};
						
						if (val == null) {
							$(this).html(more[attr]);
						} else {
							$(this).html(val);
						}
					});
					
					modal.modal("show");
				},
				formatter: function(value, data, cell, row, options, formatterParams){
					
					var avatarIcon;
					
					switch (value.plec){
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
					
					return '<span class="btn-xs">' + avatarIcon + value.nazwisko + ' ' + value.imie + '</span>';
				}
			 },
			 {
				title: "Specjalista",
				field: "schedule",
				onClick: function(e, cell, value, data){
					
					scheduleProfile = data.schedule.profile;
										
					var titleItem = $('#PdmTitle');						
					var modal = $('#Pdm');
					
					titleItem.html('Profil specjalisty: ' + scheduleProfile.nazwisko + ' ' + scheduleProfile.imie);
					
					$('.PdmDataRow').each(function(index){
						
						var attr = $(this).attr("pdm-field");
						var val = scheduleProfile[attr]; 
												
						var more = {
							avatar: function(){
									var icon;
									
									switch (scheduleProfile.plec){
										case 'F': icon = icons.avatarFemale; break;
										case 'M': icon = icons.avatarMale; break;
										default: icon = icons.avatarOther; break;
									}
									
									return icon;
								},
							userStatus: function(){ return scheduleProfile.hasUser ? icons.userIcon + ' Do profilu jest przypisane konto użytkownika.' : 'Profil nie posiada przypisanego konta użytkownika.' }
						};
						
						if (val == null) {
							$(this).html(more[attr]);
						} else {
							$(this).html(val);
						}
					});
					
					modal.modal("show");
				},
				formatter: function(value, data, cell, row, options, formatterParams){
					
					var avatarIcon;
					
					switch (data.schedule.profile.plec){
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
					
					return '<span class="btn-xs">' + avatarIcon + data.schedule.profile.nazwisko + ' ' + data.schedule.profile.imie + '</span>';
				}
			 },
			 {
				onClick: function(e, cell, value, data){
					
					if (!data.schedule.past && !data.canceled){
						var modal = $('#Dcm');
						
						$('#DcmButton').attr('visitId',data.visitId);
						$('#DcmBody').html('Odwołania wizyty nie da się cofnąć.<br><br>Czy na pewno chcesz anulować wybraną wizytę?');
						$('#DcmTitle').html('Anulowanie wizyty');
						$('#DcmButton').html('Odwołaj');
						
						modal.modal('show');
					}
				},
				formatter: function(value, data){
					if (data.schedule.past || data.canceled){
						return '';
					} else {
						return '<button class="btn btn-xs">Odwołaj</button>' 
					}
				},
				width: 72
			 },
			]
		});
	
		$('#SgcCalendar').fullCalendar({
			loading: function( isLoading, view ) {
				if(isLoading) {
					$('#SgcCalendarLoaderStatus').removeClass('progress-bar-success').addClass('progress-bar-striped');
					$('#SgcCalendarLoaderStatus').html('Trwa wczytywanie danych...');
				} else {
					$('#SgcCalendarLoaderStatus').removeClass('progress-bar-striped').addClass('progress-bar-success');
					$('#SgcCalendarLoaderStatus').html('Dane zostały wczytane.');
				}
			},
			editable: false,
			events: global.domainUrl + 'api/module/visit/schedule/all',
			timezone: 'UTC',
			defaultView: 'agendaWeek',
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
			height: 400,
			eventClick:  function(event, jsEvent, view) {
				
			}
		});
	}
}