var pageScriptSchedule = {
	
	/******************************************************
	/	Deklaracje	zmiennych
	/*****************************************************/
	
	// zmienne globalne
	global: {
		domainUrl: '',
		resourcesUrl: '',
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
		
		
		
		/******************************************************
		/	Definicje tabulatorów - wygenerowanie i wstawienie tabulatorów
		/*****************************************************/
			
		// Tabulator profili pracowników
		$('#Slc').tabulator({
		ajaxURL: global.domainUrl + 'api/module/profile/getPracownik',
		index: 'profileId',
		fitColumns:true,
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
				formatter: function(){ return icons.editIcon; },
				width: 32,
				onClick: function(e, cell, value, data){
					
					var collapse = $('#Apc');
					var form = $('#ApcForm');
										
					$('input[type!="radio"]', $(form)).each(function(){
												
						var attr = $(this).attr('name');
						var valu = data[attr];
						
						$(this).val(valu);
					});
					
					$('input[type="radio"]', $(form)).each(function(){
												
						var attr = $(this).attr('name');
						var valu = data[attr];
						
						var radi = $(this).val();
						
						if (valu == radi) {
							$(this).closest('.btn').button('toggle');
						}
					});
					
					$('#ApcButton').removeClass('btn-success').addClass('btn-danger');
					$('#ApcButton').html('Anuluj edycję');
					$('#ApcFormButton').html('Zapisz zmiany');
					$('#ApcFormButton').attr('profileId',data.profileId);
					$('#ApcInfoEdit').show();
					$('#ApcInfoNew').hide();
					
					$(collapse).collapse('show');
					
					$('html, body').animate({
						scrollTop: $(collapse).offset().top - 40
					}, 1000);
					
				}
			},
			 {
				// delete user
				onClick: function(e, cell, value, data){
					var modal = $('#Dcm');
					
					$('#DcmButton').prop('profileId',data.profileId);
					$('#DcmBody').html('Usunięcie profilu <u>spowoduje</u> również usunięcie użytkownika, <br>który jest przypisany do profilu.<br><br>Czy na pewno chcesz usunąć profil <b>' + data.nazwisko + ' ' + data.imie + ' (' + data.type + ')</b>?');
					
					modal.modal('show');
				},
				formatter: function(){ return icons.deleteIcon; },
				width: 32
			 },
			]
	});
	}
}