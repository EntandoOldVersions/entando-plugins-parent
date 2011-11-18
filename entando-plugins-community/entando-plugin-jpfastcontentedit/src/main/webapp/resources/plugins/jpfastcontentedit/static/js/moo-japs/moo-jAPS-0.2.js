/*
 
	Title: moo-jAPS

	Sottotitolo da editare
 */

/*
	Class: Taboo
	Classe che gestisce blocchi di informazioni in stile TAB
	
	Parameters:
		tabs - *classe* css dei contenitori dei tab
		tabTogglers - *classe* css degli elementi che attivano e disattivano la visualizzazione dei tab (default: 0)
		startTabIndex - *indice* di quale tab mostrare in fase di istanziazione
		startTab - *id* (html) del tab da mostrare in fase di istanziazione
		hideClass - *classe* css che da applicare quando il tab si trova in stato *nascosto* (default: noscreen) 	
		showClass - *classe* css che da applicare quando il tab si trova in stato *visibile* (default: showClass)


		Esempio di struttura con due tab: 
		
		Taboo prevede che vi siano dei gli elementi *<a>* che puntino ai contenitori dei tab.
		Le ancore devono avere come attributo *href* l'id del tab.
		Gli attivatori (tabTogglers) devono avere una classe comune. Anche i contenitori dei tab devono avere una classe in comune.
		
		(start code)
		
		
		<a href="#idTab1" class="classeAttivatoreTab">Tab Uno</a>
		<a href="#idTab2" class="classeAttivatoreTab">Tab Due</a>
		
		<div id="idTab1" class="classeContenitoreTab" >
			Contenuto del tab numero uno.
		</div>
		
		<div id="idTab2" class="classeContenitoreTab">
			Questo il contenuto del tab numero due.
		</div>
		
		
		(end)
*/

var Taboo = new Class({
	Implements: [Events, Options],
	options: {
		tabs: "",						//classe dei contenitori dei tab
		tabTogglers: "",				//classe css degli elementi che attivano e disattivano la visualizzazione dei tab
		startTabIndex: "",				//quale tab mostrare per primo (vince su startTab)
		startTab: "",					//come sopra però arriva un ID
		activeTabClass: "activetab",
		hideClass: "noscreen",			//classe css che viene applicata in stato "nascosto"
		showClass: "showClass"			//classe css che viene applicata in stato "visibile"
		
		/*
		 * 		tabs: "tab",
		 *		tabTogglers: "tab-toggle"
		 *		startTabIndex: 1,
		 */
		
	},

	/*
		Function: initialize
		
		Esegue le operazioni necessarie ad istanziare un Taboo. 
		
		- Setta le opzioni passate al costruttore
		- Crea l'hashmap con gli elementi dom interessati 
		- Prepara la visualizzazione 
		- Visualizza il tab desiderato
		 
		Nelle varie fasi dell'inizializzazione del Taboo è presente il controllo per la visualizzazione del tab desiderato. 
		
		L'ordine di controllo è
		- startTabIndex
		- startTab
		- ancora nell'url (document.location.hash che restituisce #id)
		
		Parameters:
		
			options - oggetto contenente: tabs, tabTogglers, startTabIndex, startTab, hideClass, showClass
	*/
	initialize: function(options){
		this.setOptions(options);		//setto le opzioni entranti
		this.options.Map = new Hash();	//creo la mappa bwahahah! :E
	   
		//creo/aggiorno la struttura del Taboo
		this.refreshMap();
		
		//chiudo tutto
		this.collapseAll();
		
		//posso mostrare il tab desiderato. L'ordine di importanza: startTabIndex, startTab, document.location.hash
		
		if (this.options.startTabIndex != '') {
			this.showTabByIndex(this.options.startTabIndex);
		}
		else if(this.options.startTab != '') {
			this.showTabById(this.options.startTab);	
		}
		else if (document.location.hash != '') {
			this.showTabById(document.location.hash.replace('#',''));
		}
		else { 
			this.showTabByIndex(0); 
		}
    },
        
	/*
		Function: collapseAll

		Chiude tutti i tab.
		Per ogni coppia della mappa viene rimossa la classe css showClass ed aggiunta la classe hideClass. 
	*/
    collapseAll: function() {
    	
    	//per ogni coppia della mappa rimuovo la classe showClass e aggiungo la classe hideClass 
    	this.options.Map.each( function(mapValue, key, hash){
    			$(mapValue.tab).removeClass(this.options.showClass);
    			$(mapValue.tab).addClass(this.options.hideClass);
    	} ,this);
    	
    },

	/*
		Function: showTabByIndex
		
		Visualizza un determinato tab passandogli la posizione (indice)
		
		Parameters:
		
			index - *int*, rappresenta l'indice del tab che si vuole visualizzare
	*/
	showTabByIndex: function(index){
		
		var toggler = $$('.'+this.options.tabTogglers)[index];
		
		//se non è un numero non si fa niente
		if ($type(index) == 'number' ) {
			//prendo in riferimento tutte le chiavi
			var keys = this.options.Map.getKeys();
			//se esiste una chiave in quella posizione indice
			if(keys[index]) {
				//passo la palla a chi di dovere...
				this.showTabByMapKey(keys[index], toggler);
			}
		}
	},

	/*
		Function: showTabById
		
		Visualizza un determinato tab passandogli l'id (html)
		
		Parameters:
		
			id - *string*, rappresenta l'id (html) del tab che si vuole visualizzare
	*/
	showTabById: function(id) {
		//se l'id non è nullo
		if (null != id) {
			
			//preparo il posto per il toggler
			var toggler;
			
			//da tutti i toggler presenti
			$$('a.'+this.options.tabTogglers).each(
				function(item, index) {
					//controllo se quello corrente è quello che cerco (che ha href uguale all'id)
			        if (item.getProperty('href') == '#'+id ) {
			        	//prendo il riferimento
			        	toggler = item;
			        }
			    }
			); 
			//se il toggler esiste...
			if (toggler != null) {
				
				//ricostruisco il valore per la mappa
				var value = { 'tab': $(id), 'toggler': toggler };
				
				//se esiste questa chiave nella mappa del Taboo
				if (this.options.Map.has(value.toggler.toString())) {
					//apro questo tab utilizzando il metodo opportuno
					this.showTabByMapKey(value.toggler.toString());
				}
			} 
			//altrimenti se non esiste questo tab mostro il tab in posizione 0
			else this.showTabByIndex(0);

		}

	},
	
	/*
		Function: showTabByMapKey
		
		Visualizza un determinato tab.
		
		Nasconde il tab visualizzato corrente e visualizza il tab estratto dalla mappa fornendo una chiave.
		
		Parameters:
		
			key - chiave della mappa *Taboo.options.Map*
	*/
	showTabByMapKey: function(key){
		
		if (null != this.options.currentToggler) { //se non siamo allo stato 0
			//rimuovo lo stato attivo al toggler vecchio
			$(this.options.currentToggler).removeClass(this.options.activeTabClass);
		}
		
		if (null != this.options.currentTab) {
			this.options.Map.get(this.options.currentTab).tab.removeClass(this.options.showClass);
			this.options.Map.get(this.options.currentTab).tab.addClass(this.options.hideClass);
		}

		//per ogni coppia 
		/*
			 this.options.Map.each(function(value, key){
				//rimuovo la classe showclass
				$(value).removeClass(this.options.showClass);
				//aggiungo la classe hide class
				$(value).addClass(this.options.hideClass);
			}.bind(this));	
		*/
		
		//al tab desiderato tolgo la hideClass e aggiungo showclass
		this.options.Map.get(key).tab.toggleClass(this.options.hideClass);	
		this.options.Map.get(key).tab.toggleClass(this.options.showClass);	
		
		//aggiungo lo stato attivo al toggler selezionato
		this.options.Map.get(key).toggler.addClass(this.options.activeTabClass);
		
		//setto i correnti tab e toggler
		this.options.currentTab = key;
		this.options.currentToggler = this.options.Map.get(key).toggler;
		//document.location.hash = key.substring(key.lastIndexOf('#'));
	},

	/*
		Function: refreshMap
		
		Aggiorna il set di tab/toggler.
		
		Si preoccupa di preparare anche gli eventi per la visualizzazione.
	
	*/		
	refreshMap: function() {

		 //quanti tab?
		 var tabsLength = $$("."+this.options.tabs).length;			
		 //quanti togg?
		 var toggLength = $$("."+this.options.tabTogglers).length;	
		 
		//se ad ogni tab corrisponde un toggler popolo la mappa con il toggler(a cui ho appena applicato gli eventi desiderati)
		if ((tabsLength == toggLength) && (tabsLength > 0) ) {		//se esistono tanti tab quanti toggler e sono più di 0...
			
			//per ognuni coppia di toggler/tab rimuovo gli eventi, aggiungo l'eventi, imposto la mappa
			for (index=0; index < tabsLength;index++) {
				//cancello gli eventi... ma boh forse non serve TO-TEST
				this.cleanEventToggler(index);			
				//aggiungo evento...
				this.addEventToggler(index);			
				//popolo la mappa
				this.options.Map.set($$("."+this.options.tabTogglers)[index], { 'tab': $$("."+this.options.tabs)[index], 'toggler': $$("."+this.options.tabTogglers)[index] } ); 
			}
			
		}
	},

	/*
		Function: addEventToggler
		
		*Di servizio* aggiunge l'evento per la visualizzazione del tab ad un toggler.
		
		Parameters:
		
			index - l'indice (riferito al dom) del toggler
	*/		
	addEventToggler: function(index) {
		//se non un numero non faccio niente
		if ($type(index) == 'number') {									
				//chiamato con il selettore generale perche' sto costruendo la mappa
				$$("."+this.options.tabTogglers)[index].addEvents({ 	
					
					'click': function(tab){
						//evento chiamato al click (mostrami il tab tizio)
						this.showTabByIndex(index, tab.target); 	
						//prevengo altri eventi
						return false;					
	
						}.bind(this)
									
					});
		}
	},

	/*
		Function: cleanEventToggler
		
		*Di servizio* ripulisce un toggler dagli eventi.
		
		Parameters:
		
			index - l'indice (riferito al dom) del toggler
	*/		
	cleanEventToggler: function(index) {			
		//se non un numero non faccio niente
		if ($type(index) == 'number') {									
			//chiamato con il selettore generale perche' sto costruendo la mappa	
			$$("."+this.options.tabTogglers)[index].removeEvents();		
		}			
	}
		
});

/*
   Class: Wood
   Classe che gestisce gli alberi e i menu
   
   Parameters:
		rootId - id (html) dell'elemento di riferimento, necessario se showTools è true
		menuToggler - classe css degli elementi attivatori dell'albero/menu
		hideClass - classe css utilizzata nei rami per lo stato *nascosto* (default: noscreen) 
		showClass - classe css utilizzata nei rami per lo stato *visibile* (default: undoNoscreen)
		openClass - classe utilizzata nei rami per lo stato *aperto* (default: openmenu)
		closedClass - classe utilizzata nei rami per lo stato *chiuso* (default: closedMenu)
		showTools - boolean, decide se creare la toolbar. (opzionale)
		expandAllLabel - etichetta per il link che *espande tutto* (default: +)
		collapseAllLabel - etichetta per il link che "chiude tutto" (default: -)
		startIndex - id (html) del ramo da aprire in fase di istanziazione  
		toolClass - classe css utilizzata per gli strumenti (default: toolClass)
		type - tipo di oggetto, tipi attualmente implentati *tree* e *menu*
		enableHistory - boolean, che attiva o disattiva la funzionalità per la memoria del Wood menu
		cookieName - nome del cookie dove salvare lo stato del Wood menu

		Wood è stato pensato per essere un menu oppure un albero.
		
		La struttura di esempio per il menu:
		E' possibile annidare all'infinito i rami del menu.
		(start code)
		
		<ul id="MyMenu">
			<li class="menuAperto">
				<a href="#menu_PrimaVoce" rel="menu_PrimaVoce" class="attivatoreDelMenu">Prima Voce</a>
				<ul id="menu_PrimaVoce">
					<li><a href="http://link1">Uno</a></li>
					<li><a href="http://link2">Due</a></li>
				</ul>
			</li>
			<li class="menuAperto">
				<a href="#menu_SecondaVoce" rel="menu_SecondaVoce" class="attivatoreDelMenu">Seconda Voce</a>
				<ul id="menu_SecondaVoce">
					<li><a href="http://link3">Tre</a></li>
					<li><a href="http://link4">Quattro</a></li>
				</ul>
			</li>
		</ul>
		
		(end)

		La struttura di esempio per il tree:
		E' possibile annidare all'infinito i rami del tree.
		(start code)
		
		<ul id="MyTree">
			<li>
				<input type="radio" name="" id="id_numero_1" value="" class="attivatoreMyTree" />
				<label for="id_numero_1">Primo Ramo</label>
				<ul class="treeToggler"> 
					<li>
						<input type="radio" name="" id="id_numero_2" value="" />
						<label for="id_numero_2">Primo Ramo del primo ramo</label>
					</li>
					<li>
						<input type="radio" name="" id="id_numero_3" value="" />
						<label for="id_numero_3">Secondo Ramo del primo ramo</label>
					</li>
					<li>
						<input type="radio" name="" id="id_numero_4" value="" class="attivatoreMyTree" />
						<label for="id_numero_4">Terzo Ramo del primo ramo</label>
						
						<ul class="treeToggler"> 
							<li>
								<input type="radio" name="" id="id_numero_5" value="" />
								<label for="id_numero_5">Sottovoce Uno</label>
							</li>
							<li>
								<input type="radio" name="" id="id_numero_6" value="" />
								<label for="id_numero_6">Sottovoce Due</label>
							</li>
						</ul>
					</li>		
				</ul>
			</li>
		</ul>
		
		(end)
   
*/
var Wood = new Class({
	
	Implements: [Events, Options],
			 
	options: { 
		rootId: "rootId", 				//id dell'elemento di riferimento, necessario se showTools è true
		menuToggler: "",			//classe degli elementi che "cliccano"
		hideClass: "noscreen",				//classe utilizzata nei rami per lo stato "nascosto" 
		showClass: "undoNoscreen",				//classe utilizzata nei rami per lo stato "visibile"
		openClass: "openmenu",				//classe utilizzata nei rami per lo stato "aperto"
		closedClass: "closedmenu",			//classe utilizzata nei rami per lo stato "chiuso"
		//treeIdPrefix: "",			//deprecato?			
		showTools: "false",				//boolean(true | false), opzionale
		expandAllLabel: "+",			//etichetta per il link che "espande tutto"
		collapseAllLabel: "-",		//etichetta per il link che "chiude tutto"
		startIndex: "",				//id del ramo da aprire 
		
		/*aggiunto*/
		toolClass: "toolClass",				//classe css utilizzata per gli strumenti
		type: "",					//tipo di oggetto(tree | menu)
		enableHistory: false,
		cookieName: 'jAPS2Full'
		/*
			rootId: "rootId",
			menuToggler: "subMenuToggler",
			hideClass: "hideClass",
			showClass: "showClass",
			openClass: "openClass",
			closedClass: "closedClass",
			treeIdPrefix: "treeIdPrefix",
			showTools: "false",
			expandAllLabel: "expandAllLabel",
			collapseAllLabel: "collapseAllLabel",
			toolClass: "treeTool",
			type: "menu",
		*/
	},	

	/*
		Function: initialize
		
		*Di servizio* esegue le operazioni necessarie ad istanziare un Wood. 
		
		- Setta le opzioni passate al costruttore
		- Crea l'hashmap
		- Chiama il metodo initMenu
		
		Parameters:
		
			options - oggetto contenente i parametri di istanza
	*/
	initialize: function(options){ //brodo primordiale: set delle opzioni di istanza...
		this.setOptions(options);
		this.options.Map = new Hash();	//creo la mappa bwahahah! :E
		
		//se la storicizzazione è attiva leggo il cookie, popolo il set e lo pulisco
		if (this.options.enableHistory == true){
			var CookieRead = Cookie.read(this.options.cookieName);
			if ($defined(CookieRead)) {
				this.options.bouquet = CookieRead.split(",");
				this.options.bouquet = this.options.bouquet.clean(); 		
			} else { this.options.bouquet = []; }
		}
		
		this.initMenu();
    },

	/*
	 	Function: initMenu
	 	*Di servizio* per l'istanza del menu
	 	
	 	- Controlla se creare la toolbar
	 	- Popola la mappa con i rami
	 	- Chiude tutti i rami
	 	- Mostra quello desiderato
	 	
	 */	
	initMenu: function() { // let's go!
		// mostro o no la barra degli strumenti 
		// (senza rootId la barra non puo' funzionare perche' non sa dove posizionarsi)
		if ((this.options.showTools == "true") && (this.options.rootId != "")) { this.injectTools(); }
		
		//popolo la mappa...inizializzo il menu
		this.refreshMap();
		
		//chiudo tutto
		this.collapseAll();
		
		if(this.options.startIndex != "")  {
			this.openWoodById(this.options.startIndex);			
		}
		
		//se la storicizzazzione è attiva... leggo dal set gli id e li apro uno ad uno
		if (this.options.enableHistory == true) {
			if (this.options.startIndex != "" && this.options.bouquet.length > 0) {
				this.options.bouquet.erase(this.options.bouquet);
			}
			
			if (this.options.bouquet.length > 0) {
				this.options.bouquet.each(
					function(item, index){
					if (this.options.startIndex != item) this.makeWoodById(item);	
					}.bind(this));
			}
		}
		
		
	},

	/*
		Function: openWoodById
		Apre/chiude il ramo dato l'id (html) dell'elemento
		
		Accetta solo stringhe
		
		- prepara il livelo padre
		- prepara il toggler
		- prepara la lista degli elementi da aprire
		- id da cui partire per l'individuazione dei rami
		- controllo sul tipo di Wood
		- rifermimenti per gli elementi da aprire
		- cicla su tutti gli elementi da aprire 
		
		Parameters:
			id - stringa, id (html) del ramo da visualizzare
	*/
	openWoodById: function(id) {		//accetta SOLO stringhe
		if ( ($type(id) == 'string') && (id != "") ) {
			var parentEl;							//preparo il livello padre
			var previousEl;							//preparo il toggler
			var elementsToOpen = new Array();		//preparo la lista degli elementi da "aprire"
			var idToRead = id;						//id da cui partire...
			
			switch (this.options.type) {			//controllo sul tipo... prendo gli elementi necessari
				case 'tree': 
					parentEl = "ul";								//adattamento per tree
					previousEl = "input";							//adattamento per tree
					idToRead = $(id);								//adattamento per tree
					elementsToOpen.include($(idToRead));
				break;
				
				case 'menu': 
					parentEl = "ul";								//adattamento per menu
					previousEl = "a";								//adattamento per menu
					idToRead = $(id).getPrevious(previousEl);		//adattamento per menu
					elementsToOpen.include($(idToRead));					
				break; 
			}
			var test = false;										//init
			while(test == false) {					
				if ($(idToRead).getParent(parentEl) != null ) {		//se esiste un livello padre 
					elementsToOpen.include($(idToRead).getParent(parentEl).getPrevious(previousEl));	//prendo il toggler del livello padre
					idToRead = $(idToRead).getParent(parentEl);		//salto un gradino e mi posiziono sul livello padre corrente per proseguire la lettura 
				}
				else { test = true; }								//se non esiste un livello padre... esco dal while
			}
		
		
			for(var i=0;i<elementsToOpen.length;i++) {				//ciclo su tutti gli elementi trovati...
				this.makeWoodByElement(elementsToOpen[i]);			//chiamo il metodo solito per far cambiare stato
			}
		}
	},

	/*
		Function: makeWoodById
		Apre/chiude il ramo desiderato passandogli un id (html)
		
		- Individua il tipo di wood
		- Chiama makeWoodByElement a seconda del tipo di wood presente
		
		Parameters:
			id - tipo string, l'id (html) del ramo da visualizzare
		
	*/
	makeWoodById: function(id) {		//accetta solo stringhe
		if ($type(id) == 'string') {
			switch (this.options.type) {
				case 'tree': 
					this.makeWoodByElement($(id));						
				break;
				
				case 'menu': 
					if(this.options.Map.has(id)) {
						this.makeWoodByElement(this.options.Map.get(id)[1]);	//la mappa del menu è leggermente diversa
					} else if (this.options.enableHistory == true) {
						this.options.bouquet.erase(id);
						this.updateWoodCookie();
					}
				break; 
			}
		}
	},

	/*
	 	Function: makeWoodByElement
	 	Apre/chiude il ramo desiderato passandogli un elemento dal dom

	 	- individua il tipo di wood
	 	- prende il riferimento
	 	- ricerca il toggler e il suo ramo associato
	 	- dalla mappa effettua il cambio di classi css
	 	
	 	Parameters:
	 		el - elemento del dom, rappresenta il ramo che si vuole manipolare
	 		open - boolean, forza il ramo in stato "aperto" indipendentemente dal suo stato attuale
	 		store - boolean, indica che questa azione è da "storicizzare"
	*/
	makeWoodByElement: function(el, forceOpen, store) {
		if ($type(el) == 'element'){
		//controllo che l'elemento passato sia in mappa
		var key = null;
		switch (this.options.type) {
		
		  case 'tree':
			key = el.get('id');
		  break;
		
		  case 'menu':
		  	key = el.get('rel');
		  break;
		}
		if (this.options.Map.has(key)) {
			//prendo il ramo...
			var toggParent = $(el.parentNode); 
			//piglio il menu... (che è mappato in posizione zero)
			var menu = this.options.Map.get(key)[0];
			
			//agisco sulle classi css...
			if (forceOpen == true) {
				toggParent.addClass(this.options.openClass);
				toggParent.removeClass(this.options.closedClass);
				
				menu.removeClass(this.options.hideClass);
				menu.addClass(this.options.showClass);
				}
			else {
				toggParent.toggleClass(this.options.openClass);
				toggParent.toggleClass(this.options.closedClass);
				
				menu.toggleClass(this.options.hideClass);
				menu.toggleClass(this.options.showClass);

				//se è da salvare...
			if (this.options.enableHistory == true) {
				if (store == true) {
					
					//se il ramo è aperto procedo nel marcarlo come da salvare
					if (toggParent.hasClass(this.options.openClass)) { 
						//aggiorno l'indice dei rami aperti
						this.options.bouquet.include(key); 
						}
					//se il ramo è chiuso procedo a rimuoverlo dall'insieme di quelli aperti
					else { this.options.bouquet.erase(key); }
					
					//aggiorno il cookie
					this.updateWoodCookie();
				}
			}	
	
			}
			
			}
		}
	},

	/* 
	 	Function: updateWoodCookie
	 	Aggiorna il cookie per il Wood di tipo "menu".
	  
	  	- legge e pulisce il set di menu attivi e li scrive nel cookie del browser
	  	
	 */

	updateWoodCookie: function() {
		this.options.bouquet = this.options.bouquet.clean();
		Cookie.write(this.options.cookieName,this.options.bouquet, { path: this.options.cookieName});
	} ,
	
	 
	/*
		Function: collapseAll
		Chiude ricorsivamente tutti i rami di un Wood.
		
		- per ogni elemento della mappa dei rami rimuove le classs openClass e showClass ed applica closedClass e hideClass 
	*/
	collapseAll: function() {
		this.options.Map.each(									//per ogni elemento della mappa....
			function(value, key){
			    var toggParent = $(value[1].parentNode);
				var menu = value[0];

				toggParent.removeClass(this.options.openClass);
				toggParent.addClass(this.options.closedClass);
				menu.removeClass(this.options.showClass);
				menu.addClass(this.options.hideClass);
				

		}.bind(this));
	},

	/*
		Function: expandAll
		Apre ricorsivamente tutti i rami di un wood
	
		- Per ogni elemento della mappa rimuove closedClass e hideClass ed applica openClass e showClass
	*/	
	expandAll: function() {
		this.options.Map.each(
			function(value, key){
			    var toggParent = value[1].parentNode;
				var menu = value[0];
	
				toggParent.removeClass(this.options.closedClass);
				toggParent.addClass(this.options.openClass);
				menu.removeClass(this.options.hideClass);
				menu.addClass(this.options.showClass);
	
		}.bind(this));
	},

	/*
		Function: injectTools
		*Di servizio* genera la toolbar per Wood
		
		- crea un nuovo elemento dom "p" e gli applica la classe css "toolClass"
		- inietta prima di "rootId"
		- crea nuovo elemento dom "a" per *espandere tutto"
		- crea nuovo elemento dom "a" per *chiudere tutto"
		- inietta i due *a* dentro il *p*
		
	 */
	injectTools: function(){
		//istanzio un nuovo elemendo DOM di tipo P e lo inietto prima della rootId
		var toolbar = new Element('p', {
			'class': this.options.toolClass
		}).injectBefore($(this.options.rootId));

		//istanzio un nuovo elemento Dom di tipo A
		var cLink = new Element('a');
		
		//applico stile css
		cLink.addClass(this.options.toolClass);
		
		//setto href a niente
		cLink.set('href','#');
		
		//setto l'etichetta del link
		cLink.set('html',this.options.collapseAllLabel)
		
		//aggiungo l'evento click di cui blocco la propagazione
		cLink.addEvents({
			'click': function(menu, cLink) {
				menu.collapseAll();
				return false;
				}.pass(this,cLink)
				
		});
		
		//lo piazzo dentro alla toolbar
		cLink.injectInside(toolbar);
		
		//hack per lo spazio tra i due link
		toolbar.appendText(' ','bottom');

		//idem sopra, cambia solo l'evento
		var oLink = new Element('a');
		oLink.addClass(this.options.toolClass);
		oLink.set('href','#');
		oLink.set('html',this.options.expandAllLabel)
		oLink.addEvents({
			'click': function(ev) {
				ev.expandAll();
				return false;
				}.pass(this)
		});
		oLink.injectInside(toolbar);
 	},

	/*
		Function: refreshMap
		Aggiorna la mappa dei rami del Wood.
		
		Utile quando si rimuovono o si aggiungono rami e si vuole aggiornare il Wood 

	 */
 	refreshMap: function() {
		//svuoto la mappa	
		this.options.Map.empty();
			
		//quanti togg?
		var toggLength = $$("."+this.options.menuToggler).length;
		//quanti menu/rami?
		var menuLength;
		
		if (this.options.type == 'tree') {
			menuLength = $$("."+this.options.menuToggler);
			var tmp = 0;
			menuLength.each(
				function(item, index, array) {
					if (item.getNext('ul')) tmp=tmp+1;
				});
			menuLength = tmp;
		}
		
		if (this.options.type == 'menu') { 
			menuLength = $$("."+this.options.menuToggler);
			var tmp = 0;
			menuLength.each(function(item, index, array){
				if (item.getProperty('rel')) tmp = tmp+1; 
			});
			menuLength = tmp;
			}
		 
		//se ad ogni tab corrisponde un toggler popolo la mappa con il toggler(a cui ho appena applicato gli eventi desiderati)
			if ((toggLength == menuLength) && (toggLength > 0)) {
			//per ognuni coppia di toggler/tab rimuovo gli eventi, aggiungo l'eventi, imposto la mappa
				for (index=0; index < toggLength;index++) {
				//cancello gli eventi... ma boh forse non serve TO-TEST
				//aggiungo evento...
				var MapKey = null;
				var MapValue = null;
				
				switch (this.options.type) {
					case 'tree':
						//prendo il toggler
						MapKey = $$("."+this.options.menuToggler)[index];
						
						//rimuovo gli eventi
						MapKey.removeEvents();
						
						//aggiugo gli eventi
						this.addEventToggler(MapKey);
						
						//prendo il menu corrispondente
						MapValue = $(MapKey).getNext('ul'); 
						
						//popolo la mappa
						this.options.Map.set(MapKey.get('id'),[MapValue,MapKey]);
					break;
					
					case 'menu':
						//prendo il toggler
						MapKey = $$("."+this.options.menuToggler)[index];
						
						//rimuovo gli eventi
						$(MapKey).removeEvents();
						
						//aggiungo gli eventi
						this.addEventToggler(MapKey);
						
						//prendo il menu corrispondente
						MapValue = $($(MapKey).getProperty('rel')); 
						
						//popolo la mappa
						this.options.Map.set(MapKey.get('rel'),[MapValue,MapKey]);
					break;
					
				}
				
				}
			}
			
 	},
	
	/*
		Function: addEventToggler
		*Di servizio* aggiunge gli eventi ai togglers
		
		Gli eventi possono essere considerati principalmente 2: il click e la pressione del tasto TAB.
		
		Per Intercettare la pressione del tasto TAB è stato necessario aggiungere un evento di controllo che esegua il controllo sul tasto premuto. Dunque l'evento *keypress* si preoccupa principalmente di intercettare il codice del tasto premuto e lanciare l'evento associato al quel tasto.
	 */
 	addEventToggler: function(el) {
		el.addEvents({

			'click': function(el){
				this.makeWoodByElement(el,false,true);
				if (this.options.type == 'menu') { 
					return false; 
				} else { 
					return true; };
				}.pass(el, this),

			//questo evento serve per controlla esclusivamente quale tasto viene premuto, passera' la palla poi ad altri eventi bindati
			'keypress' : function(ev) {
					//se il tasto premuto è il TAB allora chiamo l'evento per la pressione del tab.
					if (ev.code == 9) {				
						this.fireEvent('pressTab',this);
					}
				}, 

			'pressTab': function(el) {
					this.makeWoodByElement(el,true);
				}.pass(el, this)
			});
 	}
});


/*
	Class: HoofEd
	Genera un piccolo editor da utilizzare per la formattazione del testo nelle textarea
	
	Parameters:
		buttonsFileName - stringa, suffisso con il quale indicare i file catalogo e delle lingue
		basePath - stringa, URI della cartella contentende i file di catalogo e della lingua
		textareaID - stringa, l'id (html) della textarea di riferimento
		toolClass - stringa, classe css da applicare all'HoofEd
		toolPosition - stringa, indica dove posizionare Hoofed rispetto <textareaID>. Accetta i valori: *before* ed *after* (default: before)
		buttons - array, array js dove dichiarare i bottoni (i bottoni devono essere presenti nel file di catalogo)
		lang - stringa, di 2 caratteri rappresenta la lingua dell'editor
	
	
	
	Per il funzionamento di HoofEd si pressupone che esistano due file: il file con il catalogo dei bottoni disponibili e il file contenente le etichette tradotte.
	
	Il file di catalogo si deve chiamare *<buttonsFileName>*.js
	
	Il file con delle traduzioni si deve chiamare *<buttonsFileName>_<lang>*.js

File di supporto: 
	Esempio di file catalogo *<buttonsFileName>*.js 
	(start code)
	
	var HoofEd_buttons = { //il nome dell'oggetto DEVE rimanere inviariato
		"bold" : ['<span class="bold">',"</span>"],
		"underline" : ['<span class="underline"','</span>'],
		"italic" : ['<span class="italic"','</span>']
	}
	(end)
	
	Esempio di file delle traduzioni in italiano
	(start code)
	var HoofEd_buttons_it = {
		"bold" : "Grassetto",
		"underline" : "Sottolineato",
		"italic" : "Corsivo"
	}
	(end) 
		
*/
var HoofEd = new Class({
	Implements: [Events, Options],
	options: {
		buttonsFileName: 'HoofEd_buttons',
		basePath: './hoofed',
		textareaID: "textarea",						//id della textarea
		toolElement: "p",
		toolClass: "HoofEd_tools",					
		toolPosition: "before",						//posizione dei tools: before | after
		buttons: [],								//oggetto contenente Codici!
		lang: 'it' 
	},	

	/*
		Function: initialize
		*Di servizio* chiamato al momento dell'istanziazione.
		
		Parameters:
			options - obj che racchiude i parametri di istanza: <textareaID> <toolClass> <toolPosition> <button>
	 */	
	initialize: function(options){ //brodo primordiale: set delle opzioni di istanza...
		this.setOptions(options);
		this.options.Map = new Hash();
		//this.options.Map.combine(this.options.buttons);
		this.createButtonMap();
		this.injectTools();

	},
	
	/*
		Function: injectTools
		*Di servizio* inietta la toolbar dove richiesto
		
	 */
	injectTools: function () {
		//istanzio un nuovo elemendo DOM di tipo P e lo inietto prima della rootId
		this.toolBar = new Element(this.options.toolElement, {
			'class': this.options.toolClass
		}).inject($(this.options.textareaID),this.options.toolPosition);
	
		//per ogni elemento della mappa, creo il bottone e lo inietto
		$each(this.options.Map, function(value, key, index){
			var createdButton = this.createButton(key,value[0],value[1]);
			createdButton.inject(this.toolBar,'bottom');
			
			//hack per lo spazio tra i due link
			this.toolBar.appendText(' ','bottom');
			
		},this);
		
	},
	
	/*
		Function: createButtonMap
		*Di servizio* crea la mappa dei bottoni (etichette + tags)
		
		Carica dinamicamente il file con la configurazione dei bottoni/tag disponibili e carica dinamicamente il file delle etichette nella lingua selezionata
	
		Vengono richiamati i file con suffisso *<buttonsFileName>*.
		 
		Il file contentente il set di bottoni disponibili si chiama *<buttonsFileName>.js* mentre il file della lingua *<buttonsFileName>_<lang>.js*.
		
		Al caricamento di questi due file vengono richiamati i tag e le etichette, necessarie a comporre l'editor che si vuole istanziare, e caricate nella mappa principale dell'editor 
		
	*/
	createButtonMap: function() {
		//buttons = this.options.buttons;
		
		var loadfile = new Asset.javascript(this.options.basePath+'/'+this.options.buttonsFileName+'.js');
		loadfile = new Asset.javascript(this.options.basePath+'/'+this.options.buttonsFileName+'_'+this.options.lang+'.js');

		try {
				this.options.buttons.each(
					function(button) {
						var newB = eval('HoofEd_buttons.'+button);
						var newL = eval('HoofEd_buttons_'+this.options.lang +'.'+button);
						this.options.Map.include(newL,newB);
					},this);
		}
		catch(err) {
		}
		
		
	},
	
	/*
		Funtion: createButton
		Crea un nuovo bottone nella toolbar
		
		Parameters:
			lab - etichetta del nuovo bottone
			startCode - tag di apertura
			endCode - tag di chiusura
			
	 */
 	createButton: function(lab, startCode, endCode) {
		//nuovo bottone
		var b = new Element('a');
		
		//setto href a niente
		b.set('href','#');
		
		//setto l'etichetta del link
		b.set('html',lab);
		
		//just for testing
		// b.set('id','tool'+$random(1,9000));
		//setto il tag
		
		// inutile!
		//b.set('BBcode',lab);
		
		//aggiungo l'evento click di cui blocco la propagazione
		this.addEventButton(b);
		
		//rendo a cesare quel che è di cesare
		return b;
 	},
	
	/*
		Function: addEventButton
		*Di servizio* aggiunge gli eventi necessari ai bottoni della toolbar
		
		Parameters:
			el - elemento del dom che rappresenta il bottone
	*/
 	addEventButton: function(el) {
		el.addEvents({
			'click': function(el){
				//ad ogni click: prendo il tagcode corretto  
				this.encloseSelection(el.get('text'));
				return false; 
				}.pass(el, this)
			});
	},

	/*
		Function: encloseSelection
		Servizio che viene chiamato per applicare i tag
		
		- legge dalla mappa dei tag, gli startTag e gli endTag
		- legge la selezione nella textarea
		- applica il tab
	*/
	encloseSelection: function(key) {
		//prendo il tag dalla mappa...
		var tagCode = this.options.Map.get(key);
	
		//setto il marcatore di apertura (delimitatori + valore in posizione 0)
		var prefix = tagCode[0];
		
		//setto il marcatore di chiusura (delimitatori + valore in posizione 1)
		var suffix = tagCode[1];
		
		//prendo il riferimento la textarea...
		var textarea = $(this.options.textareaID);
		
		//codice cross browser... da qui in poi non ci sono personalizzazioni.
		textarea.focus();
		
		var start, end, sel, scrollPos, subst;
		if (typeof(document["selection"]) != "undefined") {
			sel = document.selection.createRange().text;
	 	} 
	 	else if (typeof(textarea["setSelectionRange"]) != "undefined") {
	 		start = textarea.selectionStart;
	 		end = textarea.selectionEnd;
	 		scrollPos = textarea.scrollTop;
	 		sel = textarea.value.substring(start, end);
	 	}
	 	
	 	if (sel.match(/ $/)) { // exclude ending space char, if any
	 		sel = sel.substring(0, sel.length - 1);
	 		suffix = suffix + " ";
	 	}
	 	
	 	subst = prefix + sel + suffix;
	 	if (typeof(document["selection"]) != "undefined") {
	 		var range = document.selection.createRange().text = subst;
	 		textarea.caretPos -= suffix.length;
	 	} 
	 	else if (typeof(textarea["setSelectionRange"]) != "undefined") {
	 		textarea.value = textarea.value.substring(0, start) + subst +
	 		textarea.value.substring(end);
	 		if (sel) {
	 			textarea.setSelectionRange(start + subst.length, start + subst.length);
		 	} else {
		 		textarea.setSelectionRange(start + prefix.length, start + prefix.length);
		 	}
		 	textarea.scrollTop = scrollPos;
	 	}
	}
});