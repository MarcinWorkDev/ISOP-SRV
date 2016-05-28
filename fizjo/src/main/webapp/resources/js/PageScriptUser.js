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
				
		console.log('SOPI PageScript for User page loaded.');
		
		var self = this;
		var global = self.global;
		var icons = self.icons();
		
		/******************************************************
		/	Funkcje obsługi onClick - dodanie eventów
		/*****************************************************/
		
		// Usuwanie użytkownika AJAX
		$('#DcmButton').on('click',function(){
		//$('body').on('click','#DcmButton',function(){
			var userId = parseInt($(this).attr('userId'));
			
			$('#Dcm').modal('toggle');
			
			SOPI_ajaxJson({
				url: global.domainUrl + 'api/module/user/delete/' + userId,
				method: 'DELETE',
				contentType: "application/json; charset=utf-8",
				record: userId,
				message: 'Usunięcie użytkownika',
				actionSuccess: function(){
					$('#Ulc').tabulator("deleteRow", userId);
				}
			});
			
		});
		
		// Wczytanie listy profili bez użytkowników
		$('#AucButton').on('click',function(){
			$('#AucContainer').tabulator("setData", global.domainUrl + 'api/module/user/getAvailProfiles');
		});
		
		// Dodanie użytkownika AJAX
		$('#AucContainerButton').on('click',function(){
					
			var profile = $('#AucContainerProfile').prop('profile');			
			var data = 
				{
					profile: profile,
					username: $('#AucContainerUsername').val(),
					credentialsNonExpired: true,
					enabled: true,
					userId: profile.profileId
				};
				
			SOPI_ajaxJson({
				url: global.domainUrl + 'api/module/user/add',
				method: 'POST',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				record: 'Nowy',
				message: 'Dodawanie użytkownika',
				actionSuccess: function(){
					$('#Ulc').tabulator("setData");
				}
			});
								
		});
		
		// Dodanie nowej roli do użytkownika AJAX
		$('#RlmButton').on('click',function(){
				
			var selected = $('#RlmRolesList').val();
			var userId = $('#RlmContainer').attr('userId');
			
			SOPI_ajaxJson({
				url: global.domainUrl + 'api/module/user/setUserRole/' + userId + '/' + selected,
				method: 'POST',
				contentType: "application/json; charset=utf-8",
				record: userId,
				message: 'Dodanie roli ' + selected + ' do użytkownika'
			});
						
			$('#Rlm').modal('toggle');
		});
		
		/******************************************************
		/	Definicje tabulatorów - wygenerowanie i wstawienie tabulatorów
		/*****************************************************/
		
		// Tabulator uprawnień
		$('#RlmContainer').tabulator({
			index: 'role',
			fitColumns:true,
			sortable: false,
			height: 200,
			columns: [
				{title: "Nazwa roli", field: "role", width: 150},
				{title: "Opis roli", field: "description"},
				{
					onClick: function(e, cell, value, data){
						
						userId = $('#RlmContainer').attr('userId');
						
						SOPI_ajaxJson({
							url: global.domainUrl + 'api/module/user/deleteUserRole/' + userId + '/' + data.role,
							method: 'DELETE',
							contentType: "application/json; charset=utf-8",
							record: userId,
							message: 'Usuwanie roli użytkownika: ' + data.role
						});
						
						$('#Rlm').modal('toggle');
					},
					formatter: function(){ return icons.deleteIcon },
					width: 32
				},
			]
		});
		
		// Tabulator dostępnych profili
		$('#AucContainer').tabulator({
			index: 'profileId',
			fitColumns:true,
			sortable: false,
			height: 200,
			rowClick: function(e, id, data, row){
				var rowString = data.type + ': ' + data.nazwisko + ' ' + data.imie + ' (' + data.email + ') ';
				$('#AucContainerProfile').val(rowString);
				
				$('#AucContainerProfile').prop('profile',data);
				
				if (data.type=='PACJENT'){
					$('#AucContainerUsername').val(data.pesel);
					$('#AucContainerUsernameGroup').prop('hidden','hidden');
				} else {
					$('#AucContainerUsername').val(data.pesel);
					$('#AucContainerUsernameGroup').prop('hidden',false);
				}
				
				$('#AucContainerButton').prop('disabled', false);
				
			},
			columns: [
				{title: "Typ profilu", field: "type", width: 100},
				{title: "Nazwisko", field: "nazwisko"},
				{title: "Imię", field: "imie"},
				{title: "Email", field: "email"},
				{title: "Pesel", field: "pesel"},
				{
					title: "Szczegóły",
					onClick: function(e, cell, value, data){
											
						var titleItem = $('#PdmTitle');							
						var modal = $('#Pdm');
						
						titleItem.html('Szczegóły profilu');
						
						$('.PdmDataRow').each(function(index){
							$(this).html(data[$(this).attr("pdm-field")]);
						});
						
						modal.modal("show");
					},
					formatter: function(value, data, cell, row, options, formatterParams){
						
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
						
						return '<span class="btn-xs">' + avatarIcon + '</span>';
					}
				}
			]
		});
		
		// Tabulator użytkowników
		$('#Ulc').tabulator({
		ajaxURL: global.domainUrl + 'api/module/user/get',
		index: 'userId',
		fitColumns:true,
		rowEdit:function(id, data, row){
			
			SOPI_ajaxJson({
				url: global.domainUrl + 'api/module/user/set/' + id,
				method: 'PUT',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(data),
				record: id,
				message: 'Modyfikacja danych użytkownika'
			});
			
		},
		pagination: true,
		paginationSize: 15,
		height: 550,
		sortable: false,
		tooltips:true,
		columns: 
			[
			 {
				title: "ID",
				field: "userId",
				width: 40
			 },
			 {
				title: "Użytkownik",
				field: "username",
				editor: "input",
				width: 120
			 },
			 {
				title: icons.activeIcon,
				field: "enabled",
				formatter: "tickCross",
				editor: "tick",
				tooltip: "Status konta użytkownika (aktywne/nieaktywne)",
				width: 30
			 },
			 {
				title: "Hasło aktualne",
				field: "credentialsNonExpired",
				formatter: "tickCross",
				width: 80
			 },
			 {
				title: "Email",
				field: "email",
				width: 180,
				formatter: function(value, data, cell, row, options, formatterParams){ return data.profile.email; }
			 },
			 {
				title: "Mobile",
				field: "mobile",
				width: 100,
				formatter: function(value, data, cell, row, options, formatterParams){ return data.profile.mobile; }
			 },					 
			 {
				title: "Utworzony",
				field: "createdAt",
				width: 130
			 },
			 {
				title: "Twórca",
				field: "createdBy",
				width: 120
			 },
			 {
				title: "Profil",
				field: "profile",
				onClick: function(e, cell, value, data){
					
					var titleItem = $('#PdmTitle');						
					var modal = $('#Pdm');
					
					titleItem.html('Profil użytkownika: ' + data.username);
					
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
				title: "Uprawnienia",
				field: "roles",
				width: 120,
				onClick: function(e, cell, value, data){
												
					$('#RlmContainer').tabulator("setData", global.domainUrl + 'api/module/user/getRoles/' + data.userId);
					$('#RlmContainer').attr("userId",data.userId);
										
					var titleItem = $('#RlmTitle');							
					var modal = $('#Rlm');
					
					titleItem.html('Uprawnienia użytkownika: ' + data.username);	
					
					$('#RlmRolesList').html('<option value="-1">Wczytywanie dostępnych roli...</option>');
					$('#RlmRolesList').prop('disabled', 'disabled');
					$('#RlmButton').prop('disabled', 'disabled');
										
					$.ajax({
						url: global.domainUrl + 'api/module/user/getAvailRoles/' + $('#RlmContainer').attr('userId'),
						method: 'GET',
						contentType: "application/json; charset=utf-8",
						success: function(result) {
							$('#RlmRolesList').html('');
							
							if (result.length > 0) {
								$('#RlmRolesList').prop('disabled', false);
								$('#RlmButton').prop('disabled', false);
							} else {
								$('#RlmRolesList').html(new Option('Brak dostępnych uprawnień...',-1));
							}
							
							$.each(result,function(i, item){
							  var option = new Option(item.role + ' - ' + item.description, item.role);
							  $('#RlmRolesList').append(option);
							});
						},
						error: function(xhr, resp, text) {
							errorMessage = {id: id, errors: jQuery.parseJSON(xhr.responseText)};
						}
					});
					
					modal.modal("show");
				},
				formatter: function(value, data, cell, row, options, formatterParams){
					return '<span class="btn-xs">' + icons.rolesIcon + 'Uprawnienia</span>';
				}
			 },
			 {
				// delete user
				onClick: function(e, cell, value, data){
					var modal = $('#Dcm');
					
					$('#DcmButton').attr('userId',data.userId);
					$('#DcmBody').html('Usunięcie użytkownika <u>nie powoduje</u> usunięcia profilu, <br>do którego użytkownik jest przypisany.<br><br>Czy na pewno chcesz usunąć użytkownika <b>' + data.profile.nazwisko + ' ' + data.profile.imie + ' (' + data.username + ')</b>?');
					$('#DcmTitle').html('Usuwanie użytkownika');
					
					modal.modal('show');
				},
				formatter: function(){ return icons.deleteIcon },
				width: 32
			 },
			]
	});
	}
}